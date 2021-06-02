#!/bin/sh

# Log all subsequent commands to logfile. FD 3 is now the console
# for things we want to show up in "docker logs".
LOGFILE=/opt/couchbase/var/lib/couchbase/logs/container-startup.log
exec 3>&1 1>>${LOGFILE} 2>&1

CONFIG_DONE_FILE=/opt/couchbase/var/lib/couchbase/container-configured
config_done() {
  touch ${CONFIG_DONE_FILE}
  echo "Couchbase Admin UI: http://localhost:8091" \
     "\nLogin credentials: admin / admin1" | tee /dev/fd/3
  echo "Stopping config-couchbase service"
  sv stop /etc/service/config-couchbase
}

if [ -e ${CONFIG_DONE_FILE} ]; then
  echo "Container previously configured." | tee /dev/fd/3
  config_done
else
  echo "Configuring Couchbase Server.  Please wait (~60 sec)..." | tee /dev/fd/3
fi

export PATH=/opt/couchbase/bin:${PATH}

wait_for_uri() {
  expected=$1
  shift
  uri=$1
  echo "Waiting for $uri to be available..."
  while true; do
    status=$(curl -s -w "%{http_code}" -o /dev/null $*)
    if [ "x$status" = "x$expected" ]; then
      break
    fi
    echo "$uri not up yet, waiting 2 seconds..."
    sleep 2
  done
  echo "$uri ready, continuing"
}

panic() {
  cat <<EOF 1>&3

@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@
Error during initial configuration - aborting container
Here's the log of the configuration attempt:
@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@
EOF
  cat $LOGFILE 1>&3
  echo 1>&3
  kill -HUP 1
  exit
}

#couchbase_cli_check() {
#  couchbase-cli $* || {
#    echo Previous couchbase-cli command returned error code $?
#    panic
#  }
#}

curl_check() {
  status=$(curl -sS -w "%{http_code}" -o /tmp/curl.txt $*)
  cat /tmp/curl.txt
  rm /tmp/curl.txt
  if [ "$status" -lt 200 -o "$status" -ge 300 ]; then
    echo
    echo Previous curl command returned HTTP status $status
    panic
  fi
}

wait_for_uri 200 http://127.0.0.1:8091/ui/index.html

echo "Setting memory quotas with curl:"
curl_check http://127.0.0.1:8091/pools/default -d memoryQuota=1536 -d indexMemoryQuota=1536 -d ftsMemoryQuota=1536
echo

echo "Configuring Services with curl:"
curl_check http://127.0.0.1:8091/node/controller/setupServices -d services=kv%2Cn1ql%2Cindex%2Cfts
echo

echo "Setting up credentials with curl:"
curl_check http://127.0.0.1:8091/settings/web -d port=8091 -d username=admin -d password=admin1
echo

echo "Enabling memory-optimized indexes with curl:"
curl_check -u admin:admin1 -X POST http://127.0.0.1:8091/settings/indexes -d 'storageMode=memory_optimized'
echo

#create designer bucket
curl_check -u admin:admin1 -X POST http://127.0.0.1:8091/pools/default/buckets \
          -d 'name=designer' \
          -d ramQuotaMB=128 \
          -d replicaNumber=0

#curl_check -u admin -p admin1 -s "CREATE PRIMARY INDEX \`#primary\` ON \`designer\`;"

wait_for_uri 200 http://127.0.0.1:8091/pools/default/buckets/designer -u admin:admin1

#create project_manager bucket
curl_check -u admin:admin1 -X POST http://127.0.0.1:8091/pools/default/buckets \
          -d 'name=project_manager' \
          -d ramQuotaMB=512 \
          -d replicaNumber=0

wait_for_uri 200 http://127.0.0.1:8091/pools/default/buckets/project_manager -u admin:admin1

#curl_check -u admin -p admin1 -s "CREATE PRIMARY INDEX \`#primary\` ON \`project_manager\`;"


#create common product_builder
curl_check -u admin:admin1 -X POST http://127.0.0.1:8091/pools/default/buckets \
          -d 'name=product_builder' \
          -d ramQuotaMB=512 \
          -d replicaNumber=0


wait_for_uri 200 http://127.0.0.1:8091/pools/default/buckets/product_builder -u admin:admin1

#curl_check -u admin -p admin1 -s "CREATE PRIMARY INDEX \`#primary\` ON \`product_builder\`;"

echo "Creating RBAC 'admin' user on buckets"
couchbase_cli_check user-manage --set \
  --rbac-username admin --rbac-password admin \
  --roles 'bucket_full_access[designer,project_manager,product_builder]' --auth-domain local \
  -c 127.0.0.1 -u admin -p admin1
echo

echo "Configuration completed!" | tee /dev/fd/3

config_done

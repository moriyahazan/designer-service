set -m

/entrypoint.sh couchbase-server &

sleep 15

# Initialize Node
curl  -u Administrator:password -v -X POST \
  http://127.0.0.1:8091/nodes/self/controller/settings \
  -d 'path=%2Fopt%2Fcouchbase%2Fvar%2Flib%2Fcouchbase%2Fdata&' \
  -d 'index_path=%2Fopt%2Fcouchbase%2Fvar%2Flib%2Fcouchbase%2Fdata&' \
  -d 'cbas_path=%2Fopt%2Fcouchbase%2Fvar%2Flib%2Fcouchbase%2Fdata&' \
  -d 'eventing_path=%2Fopt%2Fcouchbase%2Fvar%2Flib%2Fcouchbase%2Fdata&'

# Rename Cluster
curl  -u Administrator:password -v -X POST http://127.0.0.1:8091/node/controller/rename \
  -d 'hostname=127.0.0.1'

# Setup Services
curl  -u Administrator:password -v -X POST http://127.0.0.1:8091/node/controller/setupServices \
  -d 'services=kv%2Cn1ql%2Cindex%2Cfts'

# Setup Memory Quotas
curl  -u Administrator:password -v -X POST http://127.0.0.1:8091/pools/default \
  -d 'memoryQuota=256' \
  -d 'indexMemoryQuota=256' \
  -d 'ftsMemoryQuota=256'

# Setup Administrator username and password
curl  -u Administrator:password -v -X POST http://127.0.0.1:8091/settings/web \
  -d 'password=password&username=Administrator&port=SAME'

# Setup Bucket
curl  -u Administrator:password -v -X POST http://127.0.0.1:8091/pools/default/buckets \
  -d 'flushEnabled=1&threadsNumber=3&replicaIndex=0&replicaNumber=0& \
  evictionPolicy=valueOnly&ramQuotaMB=100&bucketType=membase&name=Product'

cbq -u Administrator -p password -s "CREATE PRIMARY INDEX \`#primary\` ON \`product\`;"

fg 1
FROM couchbase:7.0.0-beta

COPY scripts/configure-node.sh /etc/service/config-couchbase/run
RUN sed -i.bak 's/\r$//' /etc/service/config-couchbase/run
RUN chown -R couchbase:couchbase /etc/service

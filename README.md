# Designer Service Application

## How to Build and Run the application

Build couchbase with multibuckets:
``` cd couchbasedb/ && docker build . -t couchbaseidit && Docker run -p 8091-8094:8091-8094 -p 11210:11210 -v data:/opt/couchbase/var/lib/couchbase/data couchbaseidit ```

Build springboot (cd to root folder):
```./mvnw clean spring-boot:run```

Create entity (exception is thrown due to converter):
```curl -X POST localhost:9001/projects -H 'Content-type:application/json' -d '{"name": "aaa"}' ```

if you comment out method: configureRepositoryOperationsMapping, the Project entity will be mapped to default bucket so no exception and entity will be saved

##############
##   Build   #
##############
FROM maven:3.8.1-jdk-11 AS BUILD
ARG WORKDIR=/opt/workspace

COPY . $WORKDIR/
RUN --mount=type=cache,target=/root/.m2 cd /opt/workspace && mvn clean package
#
##CMD "/bin/bash"
#
#################
###   Runtime   #
#################
FROM openjdk:11-jre-slim as RUNTIME
RUN mkdir /app
WORKDIR /app
RUN useradd -ms /bin/bash javauser
RUN chown -R javauser:javauser /app

COPY --from=BUILD /opt/workspace/app/target/*.jar /app/designer-service.jar
COPY --from=BUILD /opt/workspace/wait-for-it.sh /app/

# Fix line separator when executing from intellij
RUN sed -i.bak 's/\r$//' /app/wait-for-it.sh

USER javauser
ENV SERVER_PORT=9001

ENTRYPOINT ["./wait-for-it.sh", "couchbase:8091","--timeout=60", "--" , "java", "-jar", "/app/designer-service.jar" ]
#CMD "/bin/bash"
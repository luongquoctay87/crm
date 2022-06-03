FROM openjdk:8-jdk-alpine

ARG JAR_FILE=target/crm-service.jar

RUN mkdir /opt/crm-service

COPY ${JAR_FILE} /opt/crm-service/crm-service.jar

ENTRYPOINT ["java","-jar","/opt/crm-service/crm-service.jar"]

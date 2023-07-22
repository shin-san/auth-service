FROM registry.access.redhat.com/ubi8/openjdk-17-runtime:1.16

WORKDIR /opt/app

ARG JAR_FILE=./target/*.jar
COPY ${JAR_FILE} app.jar

EXPOSE 8080
ENTRYPOINT ["java","-jar", "-Djava.security.egd=file:/dev/./urandom", "/opt/app/app.jar"]


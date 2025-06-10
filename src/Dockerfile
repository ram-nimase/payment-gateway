FROM openjdk:17-jdk-slim
ENTRYPOINT "payment-gateway project"
WORKDIR   /app


COPY ./target/*.jar  ./app/payment-0.0.1-SNAPSHOT.jar
ENTRYPOINT ["java", "-jar", "./app/payment-0.0.1-SNAPSHOT.jar"]
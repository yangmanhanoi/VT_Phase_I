FROM ubuntu:latest AS build

RUN apt-get update
RUN apt-get install openjdk-17-jdk -y
COPY . .

RUN chmod +x ./mvnw
RUN ./mvnw package -DskipTests

FROM openjdk:17-jdk-slim

EXPOSE 8888
COPY --from=build /target/webhook-0.0.1-SNAPSHOT.jar app.jar

ENTRYPOINT ["java", "-jar", "app.jar"]
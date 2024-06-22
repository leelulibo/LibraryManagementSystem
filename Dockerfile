# Stage 1: Build with Maven
FROM maven:3.8.6-openjdk-17 AS builder

WORKDIR /app

COPY pom.xml ./
COPY src/main/java ./src/main/java  

RUN mvn package

# Stage 2: Slim image for running the application
FROM openjdk:17-slim

WORKDIR /app

COPY target/Library-1.0-SNAPSHOT.jar .  
EXPOSE 8080 

CMD ["java", "-jar", "Library-1.0-SNAPSHOT.jar"]  # Replace with your JAR name

# End of Dockerfile

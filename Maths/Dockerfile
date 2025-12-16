# ---------- build stage ----------
FROM maven:3.9.11-eclipse-temurin-21 AS build
WORKDIR /app
COPY pom.xml .
RUN mvn -B dependency:go-offline
COPY src ./src
RUN mvn -B clean package -DskipTests

# ---------- runtime stage ----------
FROM eclipse-temurin:21-jre
#FROM openjdk:17-jdk-slim

WORKDIR /app
COPY target/maths-framework-1.0-SNAPSHOT.war app.war
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.war"]

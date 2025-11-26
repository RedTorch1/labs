# Stage 1: build with Maven
FROM maven:3.9.4-eclipse-temurin-17 as build
WORKDIR /app

# copy only pom first to leverage Docker cache for dependencies
COPY pom.xml /app/
# copy source
COPY src /app/src

# build the jar (skip tests for speed; remove -DskipTests to run tests)
RUN mvn -B -DskipTests package

# Stage 2: runtime
FROM eclipse-temurin:17-jre
WORKDIR /app

# Copy the produced jar from the build stage.
# If your artifact has a different name, update the wildcard or specific filename accordingly.
COPY --from=build /app/target/*.jar app.jar

# Optional: expose port
EXPOSE 8080

# Optional healthcheck (Docker will mark container healthy/unhealthy)
HEALTHCHECK --interval=30s --timeout=5s --start-period=15s --retries=3 \
  CMD curl -f http://localhost:8080/actuator/health || exit 1

ENTRYPOINT ["java","-jar","/app/app.jar"]

FROM maven:3.9.0-eclipse-temurin-17 AS build
WORKDIR /app
COPY pom.xml .
COPY src ./src
RUN mvn clean package -DskipTests

FROM tomcat:9.0-jre17
RUN rm -rf /usr/local/tomcat/webapps/*
COPY --from=build /app/target/lab6.war /usr/local/tomcat/webapps/ROOT.war

# Скачиваем PostgreSQL драйвер напрямую
RUN wget -O /usr/local/tomcat/lib/postgresql-42.7.3.jar \
    https://jdbc.postgresql.org/download/postgresql-42.7.3.jar

EXPOSE 8080

# Запускаем с выводом логов в консоль
CMD ["catalina.sh", "run"]
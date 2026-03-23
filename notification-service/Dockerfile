FROM maven:3.9.6-eclipse-temurin-17 AS build
WORKDIR /build
COPY common-library/pom.xml common-library/pom.xml
COPY common-library/src common-library/src
RUN mvn -f common-library/pom.xml install -DskipTests
COPY notification-service/pom.xml notification-service/pom.xml
COPY notification-service/src notification-service/src
RUN mvn -f notification-service/pom.xml package -DskipTests

FROM eclipse-temurin:17-jre-alpine
WORKDIR /app
COPY --from=build /build/notification-service/target/*.jar app.jar
EXPOSE 7074
ENTRYPOINT ["java", "-jar", "app.jar"]

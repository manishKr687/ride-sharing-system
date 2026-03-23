FROM maven:3.9.6-eclipse-temurin-17 AS build
WORKDIR /build
COPY common-library/pom.xml common-library/pom.xml
COPY common-library/src common-library/src
RUN mvn -f common-library/pom.xml install -DskipTests
COPY location-service/pom.xml location-service/pom.xml
COPY location-service/src location-service/src
RUN mvn -f location-service/pom.xml package -DskipTests

FROM eclipse-temurin:17-jre-alpine
WORKDIR /app
COPY --from=build /build/location-service/target/*.jar app.jar
EXPOSE 7077
ENTRYPOINT ["java", "-jar", "app.jar"]

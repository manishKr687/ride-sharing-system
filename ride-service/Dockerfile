FROM maven:3.9.6-eclipse-temurin-17 AS build
WORKDIR /build
COPY common-library/pom.xml common-library/pom.xml
COPY common-library/src common-library/src
RUN mvn -f common-library/pom.xml install -DskipTests
COPY ride-service/pom.xml ride-service/pom.xml
COPY ride-service/src ride-service/src
RUN mvn -f ride-service/pom.xml package -DskipTests

FROM eclipse-temurin:17-jre-alpine
WORKDIR /app
COPY --from=build /build/ride-service/target/*.jar app.jar
EXPOSE 7071
ENTRYPOINT ["java", "-jar", "app.jar"]

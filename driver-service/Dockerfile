FROM maven:3.9.6-eclipse-temurin-17 AS build
WORKDIR /build
COPY common-library/pom.xml common-library/pom.xml
COPY common-library/src common-library/src
RUN mvn -f common-library/pom.xml install -DskipTests
COPY driver-service/pom.xml driver-service/pom.xml
COPY driver-service/src driver-service/src
RUN mvn -f driver-service/pom.xml package -DskipTests

FROM eclipse-temurin:17-jre-alpine
WORKDIR /app
COPY --from=build /build/driver-service/target/*.jar app.jar
EXPOSE 7073
ENTRYPOINT ["java", "-jar", "app.jar"]

FROM maven:3.9.6-eclipse-temurin-17 AS build
WORKDIR /build
COPY common-library/pom.xml common-library/pom.xml
COPY common-library/src common-library/src
RUN mvn -f common-library/pom.xml install -DskipTests
COPY matching-service/pom.xml matching-service/pom.xml
COPY matching-service/src matching-service/src
RUN mvn -f matching-service/pom.xml package -DskipTests

FROM eclipse-temurin:17-jre-alpine
WORKDIR /app
COPY --from=build /build/matching-service/target/*.jar app.jar
EXPOSE 7078
ENTRYPOINT ["java", "-jar", "app.jar"]

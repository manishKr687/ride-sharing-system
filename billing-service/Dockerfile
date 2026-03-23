FROM maven:3.9.6-eclipse-temurin-17 AS build
WORKDIR /build
COPY common-library/pom.xml common-library/pom.xml
COPY common-library/src common-library/src
RUN mvn -f common-library/pom.xml install -DskipTests
COPY billing-service/pom.xml billing-service/pom.xml
COPY billing-service/src billing-service/src
RUN mvn -f billing-service/pom.xml package -DskipTests

FROM eclipse-temurin:17-jre-alpine
WORKDIR /app
COPY --from=build /build/billing-service/target/*.jar app.jar
EXPOSE 7076
ENTRYPOINT ["java", "-jar", "app.jar"]

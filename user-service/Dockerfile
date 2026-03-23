FROM maven:3.9.6-eclipse-temurin-17 AS build
WORKDIR /build
COPY common-library/pom.xml common-library/pom.xml
COPY common-library/src common-library/src
RUN mvn -f common-library/pom.xml install -DskipTests
COPY user-service/pom.xml user-service/pom.xml
COPY user-service/src user-service/src
RUN mvn -f user-service/pom.xml package -DskipTests

FROM eclipse-temurin:17-jre-alpine
WORKDIR /app
COPY --from=build /build/user-service/target/*.jar app.jar
EXPOSE 7072
ENTRYPOINT ["java", "-jar", "app.jar"]

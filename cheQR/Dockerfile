# Build Stage
FROM maven:3.9.4-eclipse-temurin-17 AS build
WORKDIR /app
COPY . /app/.
RUN --mount=type=cache,target=/app/.m2/repository mvn -DoutputFile=target/mvn-dependency-list.log -B -DskipTests clean dependency:list install

# Runtime Stage
FROM openjdk:17-jdk-slim
WORKDIR /app
COPY --from=build /app/target/*.jar app.jar
ENTRYPOINT ["java", "-jar", "app.jar"]
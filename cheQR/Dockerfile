# Base image
FROM maven:3.9.9-eclipse-temurin-17 AS build

# Set the working directory inside the container
WORKDIR /app

# Copy all files from the build context to the working directory
COPY . .

# Debugging: List the contents of the /app directory
RUN ls -la /app

# Cache Maven dependencies
RUN --mount=type=cache,id=maven-repo,target=/root/.m2/repository \
    mvn -DoutputFile=target/mvn-dependency-list.log -B -DskipTests clean dependency:list install

# Stage 2: Create a minimal runtime image
FROM openjdk:17-jdk-slim

# Set the working directory
WORKDIR /app

# Copy the built JAR file from the build stage
COPY --from=build /app/target/cheqr-0.0.1-SNAPSHOT.jar /app/cheqr-0.0.1-SNAPSHOT.jar

# Expose the application port
EXPOSE 8080

# Run the application
ENTRYPOINT ["java", "-jar", "/app/cheqr-0.0.1-SNAPSHOT.jar"]
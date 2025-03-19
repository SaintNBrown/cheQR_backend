# Build the app.# Base image
FROM maven:3.8.6-openjdk-17 AS build

# Set the working directory inside the container
WORKDIR /app

# Copy the pom.xml file
COPY pom.xml .

# Copy the source code
COPY src ./src

# Build the application
RUN mvn -DoutputFile=target/mvn-dependency-list.log -B -DskipTests clean dependency:list install

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
RUN ./mvnw -DoutputFile=target/mvn-dependency-list.log -B -DskipTests clean dependency:list install

# Run the app by dynamically finding the JAR file in the target directory
CMD ["sh", "-c", "java -jar target/*.jar"]
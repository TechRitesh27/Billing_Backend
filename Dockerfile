# Step 1: Use Maven image to build the app
FROM maven:3.9.6-eclipse-temurin-17 AS build

WORKDIR /app

# Copy all files
COPY . .

# Build the project
RUN mvn clean package -DskipTests

# Step 2: Use a lightweight JRE image to run the app
FROM eclipse-temurin:17-jre-alpine

WORKDIR /app

# Copy only the built jar from previous stage
COPY --from=build /app/target/*.jar app.jar

# Expose the port your app runs on
EXPOSE 8080

# Run the app
ENTRYPOINT ["java", "-jar", "app.jar"]

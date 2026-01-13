# -------------------------------
# Build stage
# -------------------------------
FROM maven:3.9.9-eclipse-temurin-17 AS build

WORKDIR /app

# Copy pom.xml first for dependency caching
COPY pom.xml .
RUN mvn dependency:go-offline

# Copy source code
COPY src ./src

# Build the application
RUN mvn clean package -DskipTests

# -------------------------------
# Runtime stage
# -------------------------------
FROM eclipse-temurin:17-jre

WORKDIR /app

# Copy the jar from build stage
COPY --from=build /app/target/restaurantpicker-0.0.1-SNAPSHOT.jar app.jar

# Expose application port
EXPOSE 8080

# Run Spring Boot app
ENTRYPOINT ["java", "-jar", "app.jar"]
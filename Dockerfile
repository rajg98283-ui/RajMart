# Multi-stage Docker build for RajMart E-Commerce Platform
# Stage 1: Build JAR using Maven and OpenJDK 17
FROM maven:3.9.6-eclipse-temurin-17 AS builder
WORKDIR /app

# Cache dependencies
COPY pom.xml .
RUN mvn dependency:go-offline -B

# Copy source code and build production package
COPY src ./src
RUN mvn clean package -DskipTests -B

# Stage 2: Minimal Production JRE image
FROM eclipse-temurin:17-jre-alpine
WORKDIR /app

# Run as non-root user for container security
RUN addgroup -S rajmart && adduser -S rajmart -G rajmart
USER rajmart

# Copy compiled jar
COPY --from=builder /app/target/rajmart-1.0.0.jar app.jar

# Dynamic PORT support (defaults to 8080)
ENV PORT=8080
ENV SPRING_PROFILES_ACTIVE=dev
EXPOSE 8080

ENTRYPOINT ["java", "-Djava.security.egd=file:/dev/./urandom", "-Dserver.port=${PORT}", "-jar", "app.jar"]

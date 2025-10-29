# Use Eclipse Temurin JRE 21 for runtime
FROM eclipse-temurin:21-jre-alpine

WORKDIR /app

# Copy the pre-built JAR file
COPY build/libs/*.jar app.jar

# Create a non-root user
RUN addgroup -S spring && adduser -S spring -G spring
USER spring:spring

# Expose port
EXPOSE 8080

# Set JVM options
ENV JAVA_OPTS="-Xmx512m -Xms256m"

# Run the application
ENTRYPOINT ["sh", "-c", "java $JAVA_OPTS -jar /app/app.jar"]

#!/bin/bash

# Quick Start Script for Football Standings Backend
# This script helps you get the Spring Boot application running quickly

set -e

echo "=========================================="
echo "Football Standings Backend - Quick Start"
echo "=========================================="
echo ""

# Check Java version
echo "Checking Java version..."
if command -v java &> /dev/null; then
    JAVA_VERSION=$(java -version 2>&1 | awk -F '"' '/version/ {print $2}' | cut -d'.' -f1)
    echo "Java version: $JAVA_VERSION"
    
    if [ "$JAVA_VERSION" -lt 21 ]; then
        echo "❌ Error: Java 21 or higher is required"
        echo "Please install Java 21 from: https://adoptium.net/"
        exit 1
    fi
    echo "✅ Java 21+ detected"
else
    echo "❌ Error: Java is not installed"
    echo "Please install Java 21 from: https://adoptium.net/"
    exit 1
fi

echo ""
echo "Building the application..."
echo "This may take a few minutes on the first run..."
echo ""

# Navigate to backend directory
cd backend

# Make gradlew executable
chmod +x gradlew

# Build the project
./gradlew clean build

echo ""
echo "=========================================="
echo "Build completed successfully! ✅"
echo "=========================================="
echo ""
echo "Starting the application..."
echo ""

# Run the application
./gradlew bootRun &

# Wait for application to start
echo "Waiting for application to start..."
sleep 10

echo ""
echo "=========================================="
echo "🎉 Application is running!"
echo "=========================================="
echo ""
echo "Access points:"
echo "  - API Base:     http://localhost:8080"
echo "  - Swagger UI:   http://localhost:8080/swagger-ui.html"
echo "  - Health Check: http://localhost:8080/api/health"
echo ""
echo "Try it out:"
echo "  curl http://localhost:8080/api/health"
echo ""
echo "Press Ctrl+C to stop the application"
echo "=========================================="
echo ""

# Keep script running
wait

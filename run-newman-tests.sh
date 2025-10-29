#!/bin/bash

# Football Standings API - Newman Test Runner Script
# This script builds and runs the Docker container, then executes Newman tests

set -e  # Exit on error

echo "================================================"
echo "Football Standings API - Newman Test Runner"
echo "================================================"
echo ""

# Colors for output
RED='\033[0;31m'
GREEN='\033[0;32m'
YELLOW='\033[1;33m'
BLUE='\033[0;34m'
NC='\033[0m' # No Color

# Configuration
DOCKER_IMAGE_NAME="football-standings-api"
DOCKER_CONTAINER_NAME="football-api-test"
APP_PORT=8080
WAIT_TIMEOUT=60
POSTMAN_COLLECTION="Football-Standings-API.postman_collection.json"
NEWMAN_REPORT_DIR="newman-reports"

# Function to check if Docker is running
check_docker() {
    echo -e "${BLUE}Checking Docker...${NC}"
    if ! docker info > /dev/null 2>&1; then
        echo -e "${RED}Error: Docker is not running. Please start Docker and try again.${NC}"
        exit 1
    fi
    echo -e "${GREEN}✓ Docker is running${NC}"
}

# Function to check if Newman is installed
check_newman() {
    echo -e "${BLUE}Checking Newman...${NC}"
    if ! command -v newman &> /dev/null; then
        echo -e "${YELLOW}Newman is not installed. Installing globally...${NC}"
        npm install -g newman newman-reporter-htmlextra
    else
        echo -e "${GREEN}✓ Newman is installed${NC}"
    fi
}

# Function to stop and remove existing container
cleanup_container() {
    echo -e "${BLUE}Cleaning up existing containers...${NC}"
    if docker ps -a | grep -q $DOCKER_CONTAINER_NAME; then
        docker stop $DOCKER_CONTAINER_NAME > /dev/null 2>&1 || true
        docker rm $DOCKER_CONTAINER_NAME > /dev/null 2>&1 || true
        echo -e "${GREEN}✓ Removed existing container${NC}"
    fi
}

# Function to build Docker image
build_image() {
    echo -e "${BLUE}Building application locally first...${NC}"
    cd backend
    if command -v gradle &> /dev/null; then
        gradle clean build -x test
    else
        echo -e "${RED}Gradle not found. Please install Gradle first.${NC}"
        exit 1
    fi
    
    echo -e "${BLUE}Building Docker image...${NC}"
    docker build -t $DOCKER_IMAGE_NAME:latest .
    cd ..
    echo -e "${GREEN}✓ Docker image built successfully${NC}"
}

# Function to run Docker container
run_container() {
    echo -e "${BLUE}Starting Docker container...${NC}"
    
    # Check if .env file exists
    if [ -f ".env" ]; then
        docker run -d \
            --name $DOCKER_CONTAINER_NAME \
            -p $APP_PORT:8080 \
            --env-file .env \
            $DOCKER_IMAGE_NAME:latest
    else
        echo -e "${YELLOW}Warning: .env file not found. Using default configuration.${NC}"
        docker run -d \
            --name $DOCKER_CONTAINER_NAME \
            -p $APP_PORT:8080 \
            -e API_FOOTBALL_KEY="9bb66184e0c8145384fd2cc0f7b914ada57b4e8fd2e4d6d586adcc27c257a978" \
            $DOCKER_IMAGE_NAME:latest
    fi
    
    echo -e "${GREEN}✓ Container started${NC}"
}

# Function to wait for application to be ready
wait_for_app() {
    echo -e "${BLUE}Waiting for application to be ready...${NC}"
    
    COUNTER=0
    until curl -f http://localhost:$APP_PORT/api/health > /dev/null 2>&1; do
        COUNTER=$((COUNTER+1))
        if [ $COUNTER -ge $WAIT_TIMEOUT ]; then
            echo -e "${RED}Error: Application failed to start within ${WAIT_TIMEOUT} seconds${NC}"
            echo -e "${YELLOW}Container logs:${NC}"
            docker logs $DOCKER_CONTAINER_NAME
            cleanup_container
            exit 1
        fi
        echo -e "${YELLOW}Waiting... ($COUNTER/$WAIT_TIMEOUT)${NC}"
        sleep 1
    done
    
    echo -e "${GREEN}✓ Application is ready!${NC}"
}

# Function to run Newman tests
run_newman_tests() {
    echo -e "${BLUE}Running Newman tests...${NC}"
    echo ""
    
    # Create reports directory
    mkdir -p $NEWMAN_REPORT_DIR
    
    # Run Newman with HTML report
    newman run $POSTMAN_COLLECTION \
        --reporters cli,htmlextra,json \
        --reporter-htmlextra-export $NEWMAN_REPORT_DIR/report.html \
        --reporter-json-export $NEWMAN_REPORT_DIR/report.json \
        --bail \
        --color on
    
    NEWMAN_EXIT_CODE=$?
    
    echo ""
    echo -e "${GREEN}✓ Newman tests completed${NC}"
    echo -e "${BLUE}HTML Report: $NEWMAN_REPORT_DIR/report.html${NC}"
    echo -e "${BLUE}JSON Report: $NEWMAN_REPORT_DIR/report.json${NC}"
    
    return $NEWMAN_EXIT_CODE
}

# Function to show container logs
show_logs() {
    echo -e "${BLUE}Application logs:${NC}"
    docker logs $DOCKER_CONTAINER_NAME --tail 50
}

# Main execution
main() {
    echo -e "${BLUE}Step 1: Pre-flight checks${NC}"
    check_docker
    check_newman
    echo ""
    
    echo -e "${BLUE}Step 2: Docker setup${NC}"
    cleanup_container
    build_image
    echo ""
    
    echo -e "${BLUE}Step 3: Starting application${NC}"
    run_container
    wait_for_app
    echo ""
    
    echo -e "${BLUE}Step 4: Running tests${NC}"
    if run_newman_tests; then
        echo ""
        echo -e "${GREEN}========================================${NC}"
        echo -e "${GREEN}✓ ALL TESTS PASSED!${NC}"
        echo -e "${GREEN}========================================${NC}"
        TEST_RESULT=0
    else
        echo ""
        echo -e "${RED}========================================${NC}"
        echo -e "${RED}✗ SOME TESTS FAILED!${NC}"
        echo -e "${RED}========================================${NC}"
        TEST_RESULT=1
    fi
    
    echo ""
    echo -e "${BLUE}Step 5: Cleanup${NC}"
    
    # Ask user if they want to keep the container running
    echo -e "${YELLOW}Do you want to stop the container? (y/n)${NC}"
    read -t 10 -n 1 -r RESPONSE || RESPONSE='y'
    echo ""
    
    if [[ $RESPONSE =~ ^[Yy]$ ]]; then
        cleanup_container
        echo -e "${GREEN}✓ Container stopped and removed${NC}"
    else
        echo -e "${YELLOW}Container is still running. Access the API at http://localhost:$APP_PORT${NC}"
        echo -e "${YELLOW}Swagger UI: http://localhost:$APP_PORT/swagger-ui.html${NC}"
        echo -e "${YELLOW}To stop: docker stop $DOCKER_CONTAINER_NAME${NC}"
    fi
    
    echo ""
    echo -e "${BLUE}Test Summary:${NC}"
    echo -e "Reports available at: ${GREEN}$NEWMAN_REPORT_DIR/${NC}"
    echo -e "Open HTML report: ${GREEN}open $NEWMAN_REPORT_DIR/report.html${NC}"
    echo ""
    
    exit $TEST_RESULT
}

# Trap to ensure cleanup on script exit
trap cleanup_container EXIT

# Run main function
main

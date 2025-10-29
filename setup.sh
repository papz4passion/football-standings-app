#!/bin/bash

# Football Standings Application - Full Stack Setup Script
# This script sets up and runs both backend and frontend applications

set -e  # Exit on error

# Color codes for output
RED='\033[0;31m'
GREEN='\033[0;32m'
YELLOW='\033[1;33m'
BLUE='\033[0;34m'
NC='\033[0m' # No Color

# Function to print colored output
print_info() {
    echo -e "${BLUE}[INFO]${NC} $1"
}

print_success() {
    echo -e "${GREEN}[SUCCESS]${NC} $1"
}

print_warning() {
    echo -e "${YELLOW}[WARNING]${NC} $1"
}

print_error() {
    echo -e "${RED}[ERROR]${NC} $1"
}

# Function to check if a command exists
command_exists() {
    command -v "$1" >/dev/null 2>&1
}

# Function to check if a port is in use
port_in_use() {
    lsof -i:"$1" >/dev/null 2>&1
}

# Print banner
echo "================================================"
echo "  Football Standings Application Setup"
echo "================================================"
echo ""

# Check prerequisites
print_info "Checking prerequisites..."

# Check Java
if command_exists java; then
    JAVA_VERSION=$(java -version 2>&1 | head -n 1 | cut -d'"' -f2 | cut -d'.' -f1)
    if [ "$JAVA_VERSION" -ge 21 ]; then
        print_success "Java $JAVA_VERSION found"
    else
        print_error "Java 21 or higher required. Found Java $JAVA_VERSION"
        exit 1
    fi
else
    print_error "Java not found. Please install Java 21 or higher"
    exit 1
fi

# Check Gradle
if command_exists gradle || [ -f "./gradlew" ]; then
    print_success "Gradle found"
else
    print_error "Gradle not found. Please install Gradle or ensure gradlew is present"
    exit 1
fi

# Check Node.js
if command_exists node; then
    NODE_VERSION=$(node --version | cut -d'v' -f2 | cut -d'.' -f1)
    if [ "$NODE_VERSION" -ge 16 ]; then
        print_success "Node.js $(node --version) found"
    else
        print_warning "Node.js 16+ recommended. Found version $NODE_VERSION"
    fi
else
    print_error "Node.js not found. Please install Node.js 16 or higher"
    exit 1
fi

# Check npm
if command_exists npm; then
    print_success "npm $(npm --version) found"
else
    print_error "npm not found. Please install npm"
    exit 1
fi

echo ""
print_info "All prerequisites satisfied!"
echo ""

# Check if ports are available
print_info "Checking port availability..."
if port_in_use 8080; then
    print_warning "Port 8080 is already in use. Backend may fail to start."
    read -p "Do you want to continue? (y/n) " -n 1 -r
    echo
    if [[ ! $REPLY =~ ^[Yy]$ ]]; then
        exit 1
    fi
fi

if port_in_use 3000; then
    print_warning "Port 3000 is already in use. Frontend may fail to start."
    read -p "Do you want to continue? (y/n) " -n 1 -r
    echo
    if [[ ! $REPLY =~ ^[Yy]$ ]]; then
        exit 1
    fi
fi

echo ""

# Setup mode selection
echo "Select setup mode:"
echo "1) Full stack (Backend + Frontend)"
echo "2) Backend only"
echo "3) Frontend only"
echo "4) Run Newman tests"
echo "5) Docker Compose"
echo "6) Build uber JAR"
read -p "Enter your choice (1-6): " -n 1 -r SETUP_MODE
echo ""
echo ""

case $SETUP_MODE in
    1)
        print_info "Setting up full stack..."
        
        # Backend setup
        print_info "Building backend..."
        ./gradlew clean build -x test
        print_success "Backend build completed"
        
        # Start backend in background
        print_info "Starting backend on port 8080..."
        ./gradlew bootRun > backend.log 2>&1 &
        BACKEND_PID=$!
        echo $BACKEND_PID > backend.pid
        print_success "Backend started with PID: $BACKEND_PID"
        
        # Wait for backend to be ready
        print_info "Waiting for backend to be ready..."
        for i in {1..30}; do
            if curl -s http://localhost:8080/actuator/health >/dev/null 2>&1; then
                print_success "Backend is ready!"
                break
            fi
            if [ $i -eq 30 ]; then
                print_error "Backend failed to start. Check backend.log for details."
                kill $BACKEND_PID 2>/dev/null || true
                exit 1
            fi
            sleep 2
        done
        
        # Frontend setup
        print_info "Setting up frontend..."
        cd frontend
        
        if [ ! -d "node_modules" ]; then
            print_info "Installing frontend dependencies..."
            npm install
            print_success "Dependencies installed"
        else
            print_info "Dependencies already installed"
        fi
        
        print_info "Starting frontend on port 3000..."
        npm start &
        FRONTEND_PID=$!
        echo $FRONTEND_PID > frontend.pid
        cd ..
        
        print_success "Frontend started with PID: $FRONTEND_PID"
        echo ""
        print_success "===================================="
        print_success "Application is running!"
        print_success "===================================="
        echo ""
        echo -e "${GREEN}Frontend:${NC} http://localhost:3000"
        echo -e "${GREEN}Backend API:${NC} http://localhost:8080"
        echo -e "${GREEN}Swagger UI:${NC} http://localhost:8080/swagger-ui.html"
        echo -e "${GREEN}Health Check:${NC} http://localhost:8080/actuator/health"
        echo ""
        echo -e "${YELLOW}To stop the application:${NC}"
        echo "  Backend: kill $BACKEND_PID"
        echo "  Frontend: kill $FRONTEND_PID"
        echo "  Or run: kill \$(cat backend.pid) \$(cat frontend/frontend.pid) 2>/dev/null"
        echo ""
        echo "Logs:"
        echo "  Backend: tail -f backend.log"
        echo "  Frontend: Check terminal output"
        ;;
        
    2)
        print_info "Setting up backend only..."
        ./gradlew clean build -x test
        print_success "Backend build completed"
        
        print_info "Starting backend on port 8080..."
        ./gradlew bootRun
        ;;
        
    3)
        print_info "Setting up frontend only..."
        cd frontend
        
        if [ ! -d "node_modules" ]; then
            print_info "Installing frontend dependencies..."
            npm install
            print_success "Dependencies installed"
        fi
        
        print_info "Starting frontend on port 3000..."
        print_warning "Make sure backend is running on port 8080!"
        npm start
        ;;
        
    4)
        print_info "Running Newman tests..."
        if [ ! -d "postman" ]; then
            print_error "Postman directory not found!"
            exit 1
        fi
        
        cd postman
        if [ -f "run-newman-tests.sh" ]; then
            chmod +x run-newman-tests.sh
            ./run-newman-tests.sh
        else
            print_error "Newman test script not found!"
            exit 1
        fi
        ;;
        
    5)
        print_info "Starting with Docker Compose..."
        if ! command_exists docker; then
            print_error "Docker not found. Please install Docker first."
            exit 1
        fi
        
        if ! command_exists docker-compose; then
            print_error "Docker Compose not found. Please install Docker Compose first."
            exit 1
        fi
        
        print_info "Building and starting containers..."
        docker-compose up --build
        ;;
        
    6)
        print_info "Building uber JAR..."
        if [ -f "build-uber-jar.sh" ]; then
            chmod +x build-uber-jar.sh
            ./build-uber-jar.sh
        else
            print_error "Uber JAR build script not found!"
            exit 1
        fi
        ;;
        
    *)
        print_error "Invalid choice. Please run the script again."
        exit 1
        ;;
esac

echo ""
print_info "Setup completed!"

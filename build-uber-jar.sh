#!/bin/bash

# Build Uber JAR Script for Football Standings API
# This script builds a fat/uber JAR containing all dependencies

set -e  # Exit on error

# Colors for output
RED='\033[0;31m'
GREEN='\033[0;32m'
YELLOW='\033[1;33m'
BLUE='\033[0;34m'
NC='\033[0m' # No Color

echo "================================================"
echo "Football Standings API - Uber JAR Builder"
echo "================================================"
echo ""

# Configuration
PROJECT_DIR="backend"
BUILD_DIR="$PROJECT_DIR/build/libs"
OUTPUT_DIR="target"
TIMESTAMP=$(date +"%Y%m%d_%H%M%S")

# Function to check prerequisites
check_prerequisites() {
    echo -e "${BLUE}Checking prerequisites...${NC}"
    
    # Check if backend directory exists
    if [ ! -d "$PROJECT_DIR" ]; then
        echo -e "${RED}Error: Backend directory not found!${NC}"
        exit 1
    fi
    
    # Check for Gradle
    if command -v gradle &> /dev/null; then
        GRADLE_CMD="gradle"
        echo -e "${GREEN}✓ Using system Gradle: $(gradle --version | head -1)${NC}"
    elif [ -f "$PROJECT_DIR/gradlew" ]; then
        GRADLE_CMD="./gradlew"
        echo -e "${GREEN}✓ Using Gradle wrapper${NC}"
    else
        echo -e "${RED}Error: Gradle not found. Please install Gradle or add gradle wrapper.${NC}"
        exit 1
    fi
    
    # Check Java version
    if command -v java &> /dev/null; then
        JAVA_VERSION=$(java -version 2>&1 | head -1 | cut -d'"' -f2 | cut -d'.' -f1)
        echo -e "${GREEN}✓ Java version: $(java -version 2>&1 | head -1)${NC}"
        if [ "$JAVA_VERSION" -lt 21 ]; then
            echo -e "${YELLOW}Warning: Java 21 or higher is recommended. Current version: $JAVA_VERSION${NC}"
        fi
    else
        echo -e "${RED}Error: Java not found. Please install Java 21 or higher.${NC}"
        exit 1
    fi
    
    echo ""
}

# Function to clean previous builds
clean_build() {
    echo -e "${BLUE}Cleaning previous builds...${NC}"
    cd "$PROJECT_DIR"
    $GRADLE_CMD clean
    cd ..
    echo -e "${GREEN}✓ Clean completed${NC}"
    echo ""
}

# Function to build the uber jar
build_jar() {
    echo -e "${BLUE}Building uber JAR...${NC}"
    echo -e "${YELLOW}This may take a few minutes...${NC}"
    echo ""
    
    cd "$PROJECT_DIR"
    
    # Build with bootJar task (creates uber/fat JAR by default)
    if [ "$SKIP_TESTS" = "true" ]; then
        echo -e "${YELLOW}Building without tests...${NC}"
        $GRADLE_CMD clean bootJar -x test
    else
        echo -e "${BLUE}Building with tests...${NC}"
        $GRADLE_CMD clean bootJar
    fi
    
    BUILD_STATUS=$?
    cd ..
    
    if [ $BUILD_STATUS -ne 0 ]; then
        echo -e "${RED}✗ Build failed!${NC}"
        exit 1
    fi
    
    echo ""
    echo -e "${GREEN}✓ Build completed successfully${NC}"
    echo ""
}

# Function to get JAR information
get_jar_info() {
    JAR_FILE=$(find "$BUILD_DIR" -name "*.jar" -type f | head -1)
    
    if [ -z "$JAR_FILE" ]; then
        echo -e "${RED}Error: JAR file not found in $BUILD_DIR${NC}"
        exit 1
    fi
    
    JAR_NAME=$(basename "$JAR_FILE")
    JAR_SIZE=$(du -h "$JAR_FILE" | cut -f1)
    
    echo -e "${BLUE}JAR Information:${NC}"
    echo -e "  Name: ${GREEN}$JAR_NAME${NC}"
    echo -e "  Size: ${GREEN}$JAR_SIZE${NC}"
    echo -e "  Path: ${GREEN}$JAR_FILE${NC}"
    echo ""
}

# Function to verify JAR contents
verify_jar() {
    echo -e "${BLUE}Verifying JAR contents...${NC}"
    
    # Check if it's an executable JAR
    if unzip -l "$JAR_FILE" | grep -q "org/springframework/boot/loader/JarLauncher"; then
        echo -e "${GREEN}✓ JAR is an executable Spring Boot uber JAR${NC}"
    else
        echo -e "${YELLOW}⚠ Warning: JAR may not be a Spring Boot executable JAR${NC}"
    fi
    
    # Check for manifest
    if unzip -l "$JAR_FILE" | grep -q "META-INF/MANIFEST.MF"; then
        echo -e "${GREEN}✓ Manifest file found${NC}"
    fi
    
    # Check for application classes
    if unzip -l "$JAR_FILE" | grep -q "BOOT-INF/classes/com/sapient/football"; then
        echo -e "${GREEN}✓ Application classes found${NC}"
    fi
    
    # Check for dependencies
    if unzip -l "$JAR_FILE" | grep -q "BOOT-INF/lib/"; then
        DEP_COUNT=$(unzip -l "$JAR_FILE" | grep "BOOT-INF/lib/" | wc -l | tr -d ' ')
        echo -e "${GREEN}✓ Dependencies bundled: $DEP_COUNT JARs${NC}"
    fi
    
    echo ""
}

# Function to copy JAR to target directory
copy_to_target() {
    echo -e "${BLUE}Copying JAR to target directory...${NC}"
    
    # Create target directory if it doesn't exist
    mkdir -p "$OUTPUT_DIR"
    
    # Copy with versioned name
    VERSIONED_NAME="${JAR_NAME%.jar}_${TIMESTAMP}.jar"
    cp "$JAR_FILE" "$OUTPUT_DIR/$JAR_NAME"
    cp "$JAR_FILE" "$OUTPUT_DIR/$VERSIONED_NAME"
    
    echo -e "${GREEN}✓ JAR copied to:${NC}"
    echo -e "  ${GREEN}$OUTPUT_DIR/$JAR_NAME${NC}"
    echo -e "  ${GREEN}$OUTPUT_DIR/$VERSIONED_NAME${NC}"
    echo ""
}

# Function to test JAR execution
test_jar() {
    echo -e "${BLUE}Testing JAR execution...${NC}"
    echo -e "${YELLOW}Starting application for 5 seconds...${NC}"
    
    # Start the application in background
    java -jar "$JAR_FILE" > /tmp/jar-test.log 2>&1 &
    JAR_PID=$!
    
    # Wait a few seconds
    sleep 5
    
    # Check if process is still running
    if ps -p $JAR_PID > /dev/null; then
        echo -e "${GREEN}✓ Application started successfully${NC}"
        kill $JAR_PID 2>/dev/null
        wait $JAR_PID 2>/dev/null
        echo -e "${GREEN}✓ Application stopped cleanly${NC}"
    else
        echo -e "${RED}✗ Application failed to start${NC}"
        echo -e "${YELLOW}Check logs in /tmp/jar-test.log${NC}"
        exit 1
    fi
    
    echo ""
}

# Function to display usage instructions
show_usage() {
    echo -e "${BLUE}Usage Instructions:${NC}"
    echo ""
    echo -e "  ${GREEN}Run the JAR:${NC}"
    echo -e "    java -jar $OUTPUT_DIR/$JAR_NAME"
    echo ""
    echo -e "  ${GREEN}Run with custom port:${NC}"
    echo -e "    java -jar $OUTPUT_DIR/$JAR_NAME --server.port=9090"
    echo ""
    echo -e "  ${GREEN}Run with environment variables:${NC}"
    echo -e "    API_FOOTBALL_KEY=your-key java -jar $OUTPUT_DIR/$JAR_NAME"
    echo ""
    echo -e "  ${GREEN}Run with JVM options:${NC}"
    echo -e "    java -Xmx1g -Xms512m -jar $OUTPUT_DIR/$JAR_NAME"
    echo ""
    echo -e "  ${GREEN}Run as background process:${NC}"
    echo -e "    nohup java -jar $OUTPUT_DIR/$JAR_NAME > app.log 2>&1 &"
    echo ""
}

# Function to create run script
create_run_script() {
    echo -e "${BLUE}Creating run script...${NC}"
    
    RUN_SCRIPT="$OUTPUT_DIR/run-app.sh"
    
    cat > "$RUN_SCRIPT" << 'EOF'
#!/bin/bash

# Football Standings API - Run Script
# This script runs the uber JAR with proper configuration

# Colors
GREEN='\033[0;32m'
BLUE='\033[0;34m'
NC='\033[0m'

# Find the JAR file
JAR_FILE=$(find . -name "football-standings-*.jar" | grep -v "_[0-9]" | head -1)

if [ -z "$JAR_FILE" ]; then
    echo "Error: JAR file not found!"
    exit 1
fi

echo -e "${BLUE}Starting Football Standings API...${NC}"
echo -e "JAR: ${GREEN}$JAR_FILE${NC}"
echo ""

# Load environment variables from .env if it exists
if [ -f ".env" ]; then
    echo -e "${BLUE}Loading environment from .env${NC}"
    export $(cat .env | grep -v '^#' | xargs)
fi

# Set default values if not provided
export SERVER_PORT=${SERVER_PORT:-8080}
export API_FOOTBALL_KEY=${API_FOOTBALL_KEY:-"9bb66184e0c8145384fd2cc0f7b914ada57b4e8fd2e4d6d586adcc27c257a978"}

# JVM options
JVM_OPTS="-Xmx512m -Xms256m"

echo -e "${GREEN}Starting application on port $SERVER_PORT...${NC}"
echo ""

# Run the application
java $JVM_OPTS -jar "$JAR_FILE" --server.port=$SERVER_PORT
EOF
    
    chmod +x "$RUN_SCRIPT"
    
    echo -e "${GREEN}✓ Run script created: $RUN_SCRIPT${NC}"
    echo ""
}

# Main execution
main() {
    # Parse arguments
    SKIP_TESTS=false
    RUN_TEST=false
    
    while [[ $# -gt 0 ]]; do
        case $1 in
            --skip-tests|-s)
                SKIP_TESTS=true
                shift
                ;;
            --test|-t)
                RUN_TEST=true
                shift
                ;;
            --help|-h)
                echo "Usage: $0 [OPTIONS]"
                echo ""
                echo "Options:"
                echo "  -s, --skip-tests    Skip running tests during build"
                echo "  -t, --test          Test JAR execution after build"
                echo "  -h, --help          Show this help message"
                echo ""
                exit 0
                ;;
            *)
                echo "Unknown option: $1"
                echo "Use --help for usage information"
                exit 1
                ;;
        esac
    done
    
    # Execute build steps
    check_prerequisites
    clean_build
    build_jar
    get_jar_info
    verify_jar
    copy_to_target
    create_run_script
    
    if [ "$RUN_TEST" = "true" ]; then
        test_jar
    fi
    
    show_usage
    
    echo -e "${GREEN}========================================${NC}"
    echo -e "${GREEN}✓ Uber JAR build completed successfully!${NC}"
    echo -e "${GREEN}========================================${NC}"
    echo ""
    echo -e "${BLUE}Quick start:${NC}"
    echo -e "  cd $OUTPUT_DIR && ./run-app.sh"
    echo ""
}

# Run main function
main "$@"

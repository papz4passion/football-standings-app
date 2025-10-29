# Getting Started with Football Standings Application

This guide will help you get the Spring Boot backend up and running.

## Prerequisites

Before you begin, ensure you have the following installed:

1. **Java 21 or higher**
   - Download from: https://adoptium.net/
   - Verify installation: `java -version`
   - Should show version 21 or higher

2. **Git** (optional, for version control)
   - Download from: https://git-scm.com/

3. **Docker** (optional, for containerization)
   - Download from: https://www.docker.com/

## Quick Start (Automated)

The easiest way to get started is using the provided script:

```bash
# From the project root directory
./start.sh
```

This script will:
- Check your Java version
- Build the application
- Start the Spring Boot server
- Display access points

## Manual Setup

If you prefer to run commands manually:

### Step 1: Navigate to Backend Directory

```bash
cd backend
```

### Step 2: Build the Application

```bash
# On Unix/Linux/MacOS
./gradlew clean build

# On Windows
gradlew.bat clean build
```

This will:
- Download all dependencies
- Compile the code
- Run tests
- Create the JAR file

### Step 3: Run the Application

```bash
# On Unix/Linux/MacOS
./gradlew bootRun

# On Windows
gradlew.bat bootRun
```

The application will start on **http://localhost:8080**

### Step 4: Verify It's Running

Open your browser or use curl:

```bash
curl http://localhost:8080/api/health
```

Expected response:
```json
{
  "status": "UP",
  "timestamp": "2025-10-28T...",
  "service": "Football Standings API",
  "version": "1.0.0"
}
```

## Accessing the Application

Once the application is running:

### Swagger UI (Interactive API Documentation)
Open in browser: **http://localhost:8080/swagger-ui.html**

This provides:
- Interactive API testing
- Complete endpoint documentation
- Request/response examples
- Try it out functionality

### API Endpoints

#### 1. Get All Countries
```bash
curl http://localhost:8080/api/countries
```

#### 2. Get Leagues for a Country
```bash
# Example: Get leagues for England (country_id=44)
curl "http://localhost:8080/api/leagues?country_id=44"
```

#### 3. Get Teams in a League
```bash
# Example: Get teams in Premier League (league_id=152)
curl "http://localhost:8080/api/teams?league_id=152"
```

#### 4. Get Standings
```bash
# Get all standings for a league
curl "http://localhost:8080/api/standings?league_id=152"

# Filter by team name
curl "http://localhost:8080/api/standings?league_id=152&team_name=Arsenal"
```

#### 5. Toggle Offline Mode
```bash
# Enable offline mode
curl -X POST "http://localhost:8080/api/offline-mode?enabled=true"

# Disable offline mode
curl -X POST "http://localhost:8080/api/offline-mode?enabled=false"
```

#### 6. Clear Cache
```bash
curl -X DELETE http://localhost:8080/api/cache
```

## Docker Setup

### Option 1: Docker Build and Run

```bash
# Build the Docker image
cd backend
docker build -t football-standings-backend .

# Run the container
docker run -p 8080:8080 \
  -e FOOTBALL_API_KEY=9bb66184e0c8145384fd2cc0f7b914ada57b4e8fd2e4d6d586adcc27c257a978 \
  football-standings-backend
```

### Option 2: Docker Compose

```bash
# From project root
docker-compose up

# Run in detached mode
docker-compose up -d

# Stop containers
docker-compose down
```

## Running Tests

```bash
cd backend

# Run all tests
./gradlew test

# Run tests with coverage report
./gradlew test jacocoTestReport

# View coverage report
open build/reports/jacoco/test/html/index.html
```

## Configuration

### Environment Variables

You can customize the application using environment variables:

```bash
# Copy the example env file
cd backend
cp .env.example .env

# Edit .env file with your values
```

Available variables:
- `FOOTBALL_API_KEY` - API key for external football API
- `SERVER_PORT` - Server port (default: 8080)
- `CACHE_TTL_COUNTRIES` - Cache TTL for countries in milliseconds
- `CACHE_TTL_LEAGUES` - Cache TTL for leagues in milliseconds
- `CACHE_TTL_TEAMS` - Cache TTL for teams in milliseconds
- `CACHE_TTL_STANDINGS` - Cache TTL for standings in milliseconds

### Application Configuration

Edit `backend/src/main/resources/application.yml` to configure:
- Server port
- Cache TTL values
- API base URL
- Logging levels
- Actuator endpoints

## Troubleshooting

### Issue: "Java version is too old"

**Solution**: Install Java 21 or higher from https://adoptium.net/

Verify: `java -version`

### Issue: "Port 8080 is already in use"

**Solution**: Either stop the other application using port 8080, or change the port:

```bash
# Run on different port
./gradlew bootRun --args='--server.port=8081'
```

### Issue: "Gradle build fails"

**Solution**: 
1. Make sure you have internet connection (Gradle needs to download dependencies)
2. Try cleaning first: `./gradlew clean`
3. Check Java version: `java -version`

### Issue: "Cannot connect to external API"

**Solution**:
1. Check your internet connection
2. Verify the API key is correct
3. Try enabling offline mode to use cached data

### Issue: "Tests failing"

**Solution**:
```bash
# Clean and rebuild
./gradlew clean build

# Run tests with more details
./gradlew test --info
```

## Development Mode

For development with auto-reload:

```bash
cd backend
./gradlew bootRun --continuous
```

This will automatically restart the application when you make code changes.

## IDE Setup

### IntelliJ IDEA
1. Open the `backend` folder
2. IDEA will auto-detect Gradle project
3. Wait for dependencies to download
4. Run `FootballStandingsApplication.java`

### VS Code
1. Install Java Extension Pack
2. Install Spring Boot Extension Pack
3. Open the `backend` folder
4. Run using the Spring Boot Dashboard

### Eclipse
1. Import as "Existing Gradle Project"
2. Wait for dependencies to download
3. Right-click on project → Run As → Spring Boot App

## Next Steps

1. **Explore the API**: Use Swagger UI at http://localhost:8080/swagger-ui.html
2. **Run Tests**: Execute `./gradlew test` to see test coverage
3. **Check Logs**: Application logs appear in the console
4. **Review Code**: Explore the source code in `backend/src/main/java`
5. **Read Documentation**: Check `backend/README.md` for detailed information

## Support

If you encounter any issues:

1. Check the logs in the console
2. Review the `backend/README.md` for detailed documentation
3. Check the `IMPLEMENTATION_SUMMARY.md` for architecture details
4. Verify all prerequisites are installed correctly

## Useful Commands Summary

```bash
# Build
./gradlew clean build

# Run
./gradlew bootRun

# Test
./gradlew test

# Generate Coverage Report
./gradlew test jacocoTestReport

# Docker Build
docker build -t football-standings-backend ./backend

# Docker Run
docker run -p 8080:8080 football-standings-backend

# Docker Compose
docker-compose up
```

## Quick Testing Examples

```bash
# Test health endpoint
curl http://localhost:8080/api/health

# Get countries
curl http://localhost:8080/api/countries | jq

# Get leagues for England
curl "http://localhost:8080/api/leagues?country_id=44" | jq

# Get Premier League teams
curl "http://localhost:8080/api/teams?league_id=152" | jq

# Get standings
curl "http://localhost:8080/api/standings?league_id=152" | jq
```

*(Note: `jq` is a JSON formatter. Install it or remove `| jq` from commands)*

---

**Congratulations!** 🎉 You now have the Football Standings backend running.

Next: Build the React frontend to create a complete full-stack application!

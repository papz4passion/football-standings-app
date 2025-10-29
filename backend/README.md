# Football Standings Application - Backend

## Overview

This is a Spring Boot REST API application built with **Java 21 LTS** that provides endpoints for fetching football standings, leagues, teams, and countries data from an external Football API service. The application implements caching, offline mode support, and follows SOLID principles and 12-factor app methodology.

## Tech Stack

- **Java 21 LTS** - Latest Long-Term Support version
- **Spring Boot 3.3.5** - Application framework
- **Gradle** - Build tool
- **Spring WebFlux** - Reactive HTTP client
- **Swagger/OpenAPI** - API documentation
- **JUnit 5 & Mockito** - Testing
- **Docker** - Containerization
- **Jenkins** - CI/CD

## Features

- ✅ RESTful API endpoints for football data
- ✅ In-memory caching with configurable TTL
- ✅ Offline mode support
- ✅ Swagger UI for API documentation
- ✅ Spring Actuator for health checks
- ✅ Global exception handling
- ✅ CORS configuration
- ✅ Comprehensive unit and integration tests
- ✅ Docker support
- ✅ Jenkins CI/CD pipeline

## Design Patterns Used

1. **Singleton Pattern** - WebClient bean, CacheManager
2. **Strategy Pattern** - Caching strategy for different data types
3. **Dependency Injection** - Constructor-based DI throughout
4. **Factory Pattern** - WebClient configuration
5. **Repository Pattern** - Service layer abstraction

## SOLID Principles

- **Single Responsibility** - Each class has one responsibility
- **Open/Closed** - Services are open for extension, closed for modification
- **Liskov Substitution** - Interfaces used for abstraction
- **Interface Segregation** - Small, focused interfaces
- **Dependency Inversion** - Depends on abstractions, not concretions

## Prerequisites

- Java 21 or higher
- Gradle 8.x or higher
- Docker (optional)
- IDE (IntelliJ IDEA, Eclipse, or VS Code)

## Setup Instructions

### 1. Clone the Repository

```bash
git clone <repository-url>
cd football-standings-app/backend
```

### 2. Configure Environment Variables

Create a `.env` file in the backend directory:

```bash
FOOTBALL_API_KEY=9bb66184e0c8145384fd2cc0f7b914ada57b4e8fd2e4d6d586adcc27c257a978
```

### 3. Build the Application

```bash
./gradlew clean build
```

### 4. Run Tests

```bash
./gradlew test
```

### 5. Run the Application

```bash
./gradlew bootRun
```

The application will start on `http://localhost:8080`

## API Endpoints

### Countries
```
GET /api/countries
```
Fetches all available countries.

### Leagues
```
GET /api/leagues?country_id={id}
```
Fetches leagues for a specific country.

### Teams
```
GET /api/teams?league_id={id}
```
Fetches teams in a specific league.

### Standings
```
GET /api/standings?league_id={id}&team_name={name}
```
Fetches standings for a league with optional team filter.

### Offline Mode
```
POST /api/offline-mode?enabled={true|false}
```
Toggles offline mode.

### Clear Cache
```
DELETE /api/cache
```
Clears all cached data.

### Health Check
```
GET /api/health
GET /api/actuator/health
```
Returns application health status.

## API Documentation

Swagger UI is available at:
```
http://localhost:8080/swagger-ui.html
```

OpenAPI JSON specification:
```
http://localhost:8080/api-docs
```

## Docker

### Build Docker Image

```bash
docker build -t football-standings-backend .
```

### Run Docker Container

```bash
docker run -p 8080:8080 \
  -e FOOTBALL_API_KEY=your-api-key \
  football-standings-backend
```

## Configuration

Configuration is in `src/main/resources/application.yml`:

- **Server Port**: 8080
- **Cache TTL**:
  - Countries: 24 hours
  - Leagues: 1 hour
  - Teams: 1 hour
  - Standings: 5 minutes
- **API Timeout**: 10 seconds

## Testing

### Run All Tests
```bash
./gradlew test
```

### Run with Coverage
```bash
./gradlew test jacocoTestReport
```

Coverage report available at: `build/reports/jacoco/test/html/index.html`

### Test Structure

- **Unit Tests**: `src/test/java/com/sapient/football/service/`
- **Integration Tests**: `src/test/java/com/sapient/football/controller/`
- **Target Coverage**: >80%

## CI/CD

Jenkins pipeline is configured in `Jenkinsfile` with the following stages:

1. Checkout
2. Build Backend
3. Test Backend
4. Code Quality Check
5. Build Docker Image
6. Archive Artifacts

## Caching Strategy

The application uses an in-memory cache with the following strategy:

- **Countries**: Cached for 24 hours (rarely changes)
- **Leagues**: Cached for 1 hour
- **Teams**: Cached for 1 hour
- **Standings**: Cached for 5 minutes (frequently updated)

Cache automatically evicts expired entries and supports offline mode.

## Error Handling

Global exception handler provides consistent error responses:

```json
{
  "timestamp": "2025-10-28T10:30:00",
  "status": 400,
  "error": "Bad Request",
  "message": "League ID is required",
  "path": "/api/standings"
}
```

## Logging

Application uses SLF4J with Logback:

- **INFO**: General application flow
- **DEBUG**: Detailed debugging information
- **ERROR**: Error conditions

## Project Structure

```
backend/
├── src/
│   ├── main/
│   │   ├── java/com/sapient/football/
│   │   │   ├── FootballStandingsApplication.java
│   │   │   ├── cache/
│   │   │   │   └── CacheManager.java
│   │   │   ├── config/
│   │   │   │   ├── CorsConfig.java
│   │   │   │   ├── OpenAPIConfig.java
│   │   │   │   └── WebClientConfig.java
│   │   │   ├── controller/
│   │   │   │   ├── FootballController.java
│   │   │   │   └── HealthController.java
│   │   │   ├── dto/
│   │   │   │   ├── CountryDTO.java
│   │   │   │   ├── LeagueDTO.java
│   │   │   │   ├── StandingDTO.java
│   │   │   │   ├── TeamDTO.java
│   │   │   │   └── VenueDTO.java
│   │   │   ├── exception/
│   │   │   │   ├── ErrorResponse.java
│   │   │   │   ├── FootballApiException.java
│   │   │   │   └── GlobalExceptionHandler.java
│   │   │   └── service/
│   │   │       ├── FootballApiService.java
│   │   │       └── FootballService.java
│   │   └── resources/
│   │       └── application.yml
│   └── test/
│       └── java/com/sapient/football/
│           ├── FootballStandingsApplicationTests.java
│           ├── controller/
│           │   └── FootballControllerTest.java
│           └── service/
│               └── FootballServiceTest.java
├── build.gradle
├── settings.gradle
├── Dockerfile
├── .dockerignore
├── Jenkinsfile
└── README.md
```

## Contributing

1. Follow SOLID principles
2. Write tests for new features
3. Maintain >80% code coverage
4. Follow Java coding conventions
5. Document API changes in Swagger

## License

MIT License

## Support

For issues and questions, please create an issue in the repository.

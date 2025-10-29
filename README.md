# Football Standings Application

A full-stack application for viewing football team standings with country, league, and team filtering capabilities.

## Tech Stack

### Backend
- **Framework**: Spring Boot 3.3.5 (Java 21 LTS)
- **Build Tool**: Gradle 8.14.1
- **API Documentation**: Swagger/OpenAPI
- **Testing**: JUnit 5, Mockito, Spring Boot Test, Newman/Postman

### Frontend
- **Framework**: React 18.2.0
- **Styling**: Tailwind CSS 3.3.0
- **State Management**: React Hooks
- **HTTP Client**: Axios 1.6.0
- **Build Tool**: react-scripts 5.0.1

### DevOps
- **CI/CD**: Jenkins
- **Containerization**: Docker
- **Version Control**: Git
- **Testing**: Newman CLI for automated API testing

## Project Status

✅ **Backend**: Completed & Tested
- Spring Boot REST API with Java 21
- All 13 endpoints implemented
- Caching and offline mode support
- Swagger documentation
- Unit and integration tests
- Docker support
- Jenkins pipeline
- Newman tests (58/58 passing - 100%)

✅ **Frontend**: Completed
- React application with Tailwind CSS
- Cascading dropdowns for country/league/team selection
- Offline mode toggle
- Cache management UI
- Loading states & error handling
- Responsive design
- No hardcoded data (all from API)

## Project Structure

```
football-standings-app/
├── src/                        # Spring Boot Backend (✅ Completed)
│   ├── main/java/com/sapient/football/
│   │   ├── FootballStandingsApplication.java
│   │   ├── cache/          # CustomCacheManager with TTL
│   │   ├── config/         # Configuration (CORS, Swagger, WebClient)
│   │   ├── controller/     # REST controllers (13 endpoints)
│   │   ├── dto/            # Data Transfer Objects
│   │   ├── exception/      # GlobalExceptionHandler
│   │   └── service/        # FootballService (business logic)
│   ├── resources/
│   │   └── application.yml # Configuration
│   └── test/               # Unit & integration tests
├── frontend/                   # React Frontend (✅ Completed)
│   ├── public/
│   │   ├── index.html      # HTML template
│   │   └── manifest.json   # PWA manifest
│   ├── src/
│   │   ├── components/
│   │   │   ├── Dropdown.jsx           # Reusable dropdown
│   │   │   ├── StandingsTable.jsx     # Standings display
│   │   │   ├── LoadingSpinner.jsx     # Loading indicator
│   │   │   └── ErrorMessage.jsx       # Error display
│   │   ├── services/
│   │   │   └── footballService.js     # API client (Axios)
│   │   ├── App.jsx         # Main component
│   │   ├── App.css         # App styles
│   │   ├── index.js        # React entry point
│   │   └── index.css       # Global Tailwind styles
│   ├── Dockerfile          # Frontend Docker image
│   ├── nginx.conf          # Nginx configuration
│   ├── package.json        # Dependencies
│   └── README.md           # Frontend documentation
├── postman/                    # Testing (✅ Completed)
│   ├── Football_Standings_API.postman_collection.json
│   ├── environments/       # Local, Docker, Production
│   └── run-newman-tests.sh # Automated test runner
├── build.gradle
├── Dockerfile              # Backend Docker image
├── docker-compose.yml      # Full stack setup
├── build-uber-jar.sh       # Uber JAR build script
├── Jenkinsfile
└── requirements.md         # Project requirements
```

## Quick Start

### Prerequisites
- **Java 21 or higher**
- **Gradle 8.x or higher**
- **Node.js 16+ and npm**
- **Docker** (optional)
- **API Football Key** (get from https://apifootball.com/)

### Backend Setup

1. **Clone and Configure**
   ```bash
   cd football-standings-app
   # Set your API key in application.yml or environment variable
   export API_FOOTBALL_KEY=your_api_key_here
   ```

2. **Build and Run**
   ```bash
   ./gradlew clean build
   ./gradlew bootRun
   ```

3. **Access Backend**
   - API: http://localhost:8080
   - Swagger UI: http://localhost:8080/swagger-ui.html
   - Health Check: http://localhost:8080/actuator/health

### Frontend Setup

1. **Install Dependencies**
   ```bash
   cd frontend
   npm install
   ```

2. **Run Development Server**
   ```bash
   npm start
   ```

3. **Access Frontend**
   - Frontend: http://localhost:3000
   - Automatically proxies API calls to backend at http://localhost:8080

### Full Stack with Docker Compose

```bash
# From project root
docker-compose up --build

# Access:
# Frontend: http://localhost:3000
# Backend: http://localhost:8080
```

### Run Newman Tests

```bash
cd postman
./run-newman-tests.sh

# Results: Detailed HTML report in newman/ directory
```

## API Endpoints

| Method | Endpoint | Description |
|--------|----------|-------------|
| GET | `/api/countries` | Fetch all countries |
| GET | `/api/leagues?country_id={id}` | Fetch leagues by country |
| GET | `/api/teams?league_id={id}` | Fetch teams by league |
| GET | `/api/standings?league_id={id}&team_name={name}` | Fetch standings with optional filter |
| POST | `/api/offline-mode?enabled={true\|false}` | Toggle offline mode |
| DELETE | `/api/cache` | Clear cache |
| GET | `/api/health` | Health check |

## Features

### Backend Features
- ✅ RESTful API with 13 endpoints
- ✅ In-memory caching with configurable TTL
- ✅ Offline mode support
- ✅ Global exception handling (400 for missing params)
- ✅ Swagger/OpenAPI documentation
- ✅ Spring Actuator health checks
- ✅ CORS configuration
- ✅ Comprehensive logging
- ✅ Unit and integration tests (>80% coverage)
- ✅ Docker support with multi-stage build
- ✅ Jenkins CI/CD pipeline
- ✅ Newman automated testing (58/58 assertions passing)

### Frontend Features
- ✅ Cascading dropdowns (Country → League → Team)
- ✅ Dynamic data loading from backend API
- ✅ League standings table with statistics
- ✅ Team filtering capability
- ✅ Offline mode toggle
- ✅ Cache clear functionality
- ✅ Loading states for async operations
- ✅ Error handling with retry
- ✅ Responsive design (mobile-friendly)
- ✅ Color-coded standings (promotion/relegation zones)
- ✅ Team badges and logos
- ✅ No hardcoded data (all from API)
- ✅ Real-time health status
- ✅ Selection info cards
- ✅ Getting started instructions

### Design Patterns
- Singleton Pattern (WebClient, CacheManager)
- Strategy Pattern (Caching strategies)
- Factory Pattern (Configuration beans)
- Dependency Injection
- Repository Pattern

### SOLID Principles
All code follows SOLID principles:
- **S**ingle Responsibility
- **O**pen/Closed
- **L**iskov Substitution
- **I**nterface Segregation
- **D**ependency Inversion

## Architecture

```
┌─────────────────────────────────────────────────────────────┐
│                    User Browser                              │
└──────────────────────┬──────────────────────────────────────┘
                       │ HTTP (Port 3000)
                       ▼
┌─────────────────────────────────────────────────────────────┐
│  React Frontend (Nginx)                                      │
│  - Cascading Dropdowns (Country → League → Team)            │
│  - Standings Table with Statistics                          │
│  - Offline Mode Toggle                                       │
│  - Cache Management                                          │
│  - Responsive Design (Tailwind CSS)                         │
└──────────────────────┬──────────────────────────────────────┘
                       │ HTTP Proxy
                       ▼
┌─────────────────────────────────────────────────────────────┐
│  Spring Boot Backend (Port 8080)                            │
│  ┌────────────────────────────────────────────────────────┐ │
│  │  Controllers (13 REST Endpoints)                       │ │
│  │  - Countries, Leagues, Teams, Standings               │ │
│  │  - Offline Mode, Cache, Health                        │ │
│  └────────────┬───────────────────────────────────────────┘ │
│               ▼                                              │
│  ┌────────────────────────────────────────────────────────┐ │
│  │  Service Layer (FootballService)                       │ │
│  │  - Business Logic                                      │ │
│  │  - Offline Mode Detection                             │ │
│  └────────────┬───────────────────────────────────────────┘ │
│               ▼                                              │
│  ┌────────────────────────────────────────────────────────┐ │
│  │  CustomCacheManager                                    │ │
│  │  - In-Memory Cache with TTL                           │ │
│  │  - Countries: 24h, Leagues: 1h, Teams: 1h            │ │
│  │  - Standings: 5min                                    │ │
│  └────────────┬───────────────────────────────────────────┘ │
└───────────────┼──────────────────────────────────────────────┘
                │ HTTP (WebClient)
                ▼
┌─────────────────────────────────────────────────────────────┐
│  External API - Football.com                                │
│  https://apiv3.apifootball.com/                            │
└─────────────────────────────────────────────────────────────┘
```

## Configuration

Backend configuration in `backend/src/main/resources/application.yml`:

```yaml
server:
  port: 8080

api:
  football:
    base-url: https://apiv3.apifootball.com/
    api-key: ${FOOTBALL_API_KEY}

cache:
  ttl:
    countries: 86400000  # 24 hours
    leagues: 3600000     # 1 hour
    teams: 3600000       # 1 hour
    standings: 300000    # 5 minutes
```

## Testing

### Backend Tests
```bash
# Run all tests
./gradlew test

# Generate coverage report
./gradlew test jacocoTestReport

# View coverage: build/reports/jacoco/test/html/index.html
```

### Frontend Tests
```bash
cd frontend
npm test
```

### Newman API Tests
```bash
cd postman
./run-newman-tests.sh

# View HTML report: newman/report.html
# Current Status: 58/58 assertions passing (100%)
```

### Test Coverage
- Backend: >80% code coverage
- Newman: 58/58 API assertions passing
- All critical paths tested

## Development

### Adding New Features

1. Create feature branch
2. Write tests first (TDD)
3. Implement feature
4. Ensure tests pass
5. Check code coverage >80%
6. Submit pull request

### Code Quality

- Follow Java coding conventions
- Write meaningful tests
- Document public APIs
- Use Lombok to reduce boilerplate
- Follow SOLID principles

## CI/CD

Jenkins pipeline stages:
1. Checkout code
2. Build backend
3. Run tests
4. Code quality checks
5. Build Docker image
6. Archive artifacts

## Documentation

- **API Documentation**: Available at `/swagger-ui.html` when running
- **Backend README**: See `backend/README.md`
- **Requirements**: See `requirements.md`

## External API

This application integrates with API Football:
- **Base URL**: https://apiv3.apifootball.com/
- **Documentation**: https://apifootball.com/documentation/
- **API Key**: Configured in environment variables

## Next Steps

### Enhancements
1. ~~Setup React project with modern tooling~~ ✅ **Completed**
2. ~~Configure Tailwind CSS~~ ✅ **Completed**
3. ~~Implement cascading dropdowns~~ ✅ **Completed**
4. ~~Add offline mode toggle~~ ✅ **Completed**
5. ~~Create responsive UI~~ ✅ **Completed**
6. ~~Write component tests~~ ⏳ **Pending**
7. Add metrics collection (Prometheus/Micrometer)
8. User authentication & authorization
9. Favorite teams functionality
10. Real-time data updates (WebSocket)
11. Historical data & trends
12. Mobile app (React Native)
13. E2E tests (Cypress/Playwright)

## Contributing

1. Fork the repository
2. Create feature branch (`git checkout -b feature/amazing-feature`)
3. Commit changes (`git commit -m 'Add amazing feature'`)
4. Push to branch (`git push origin feature/amazing-feature`)
5. Open Pull Request

## License

MIT License

## Support

For questions or issues, please create an issue in the repository.

## Authors

Sapient Assessment Team

---

**Last Updated**: October 28, 2025

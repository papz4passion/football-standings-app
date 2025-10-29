# Football Standings Application - Requirements Document

## Tech Stack

### Backend
- **Framework**: Spring Boot (Java 21 LTS)
- **Build Tool**: Gradle
- **API Documentation**: Swagger/OpenAPI
- **Testing**: JUnit, Mockito, Spring Boot Test

### Frontend
- **Framework**: ReactJS
- **Styling**: Tailwind CSS
- **State Management**: React Hooks/Context API
- **HTTP Client**: Axios or Fetch API

### DevOps
- **CI/CD**: Jenkins
- **Containerization**: Docker
- **Version Control**: Git

---

## Project Overview

Build a full-stack Football Standings application that allows users to view team standings in league football matches by searching with country name, league name, and team name.

---

## Functional Requirements

### 1. Core Features

#### 1.1 Backend Service (Spring Boot)
- Create a RESTful microservice to fetch football standings data
- **Build with Gradle** as the build tool
- **Implement the following REST endpoints for your application** (these will internally call the external API Football service):
  - `GET /api/countries` - Fetch all available countries
  - `GET /api/leagues?country_id={id}` - Fetch leagues by country
  - `GET /api/teams?league_id={id}` - Fetch teams by league
  - `GET /api/standings?league_id={id}&team_name={name}` - Fetch standings with optional team filter
- Integrate with the external API Football service (see External API Integration section below for external API details)
- API Key: `9bb66184e0c8145384fd2cc0f7b914ada57b4e8fd2e4d6d586adcc27c257a978`
- Return structured JSON responses with team standings information
- Implement service layer to fetch data from external API Football endpoints
- Implement caching layer for offline mode support

**Note**: The endpoints listed above are YOUR Spring Boot application's REST APIs that the React frontend will call. Your backend will then call the external API Football service (documented in the External API Integration section) to fetch the actual data.

#### 1.2 Frontend Application (ReactJS + Tailwind CSS)
- Create a responsive user interface for searching team standings
- Implement cascading dropdowns for selection:
  1. **Country Dropdown**: Fetch and display all countries
  2. **League Dropdown**: Dynamically populated based on selected country
  3. **Team Dropdown**: Dynamically populated based on selected league
- Display results showing:
  - Country ID & Name: `(<ID>) - <name>`
  - League ID & Name: `(<ID>) - <name>`
  - Team ID & Name: `(<ID>) - <name>`
  - Overall League Position: `<position>`
  - Additional standings information (wins, losses, draws, goals, points)
- Allow users to dynamically change selections and view updated results
- Implement loading states and error handling
- Style with Tailwind CSS for modern, responsive design

#### 1.3 Offline Mode Support
- Implement a toggle feature for offline mode
- Cache previously fetched data for offline access
- Use in-memory data structures (no external database)
- Ensure service returns cached results when the external API is unavailable
- Implement graceful error handling and fallback mechanisms

---

## Non-Functional Requirements (NFRs)

### 2.1 Architecture & Design Principles
- **SOLID Principles**: Apply Single Responsibility, Open/Closed, Liskov Substitution, Interface Segregation, and Dependency Inversion
- **12-Factor App**: Follow 12-factor methodology for cloud-native applications
- **HATEOAS**: Implement Hypermedia as the Engine of Application State in REST APIs
- **Design Patterns**: Use appropriate design patterns (e.g., Singleton, Factory, Strategy, Circuit Breaker)

### 2.2 Performance & Optimization
- Implement caching mechanisms for API responses
- Optimize response times and resource utilization
- Handle concurrent requests efficiently
- Minimize unnecessary API calls

### 2.3 Security
- **API Key Protection**: Encrypt and secure API keys (use environment variables or Spring Cloud Config)
- **Input Validation**: Validate and sanitize all user inputs
- **HTTPS**: Support secure communication
- **CORS Configuration**: Properly configure Cross-Origin Resource Sharing
- **Error Handling**: Don't expose sensitive information in error messages

### 2.4 Production Readiness
- Implement comprehensive logging (SLF4J with Logback)
- Add health check endpoints (Spring Actuator)
- Include metrics and monitoring capabilities
- Implement proper exception handling and error responses
- Use appropriate HTTP status codes

### 2.5 Testing & Quality
- **TDD (Test-Driven Development)**: Write unit tests before implementation
- **BDD (Behavior-Driven Development)**: Create feature specifications
- **Unit Tests**: Achieve high code coverage (>80%)
- **Integration Tests**: Test API endpoints and service layer
- **Frontend Tests**: Implement React component tests
- Use JUnit, Mockito, and Spring Boot Test for backend
- Use Jest and React Testing Library for frontend

---

## API Documentation

### 3.1 Swagger/OpenAPI Specification
- Include OpenAPI 3.0 specification in the codebase
- Configure Swagger UI to visualize and test APIs
- Document all endpoints with:
  - Request/Response schemas
  - Query parameters
  - HTTP status codes
  - Error responses and error codes
- Access Swagger UI at: `/swagger-ui.html`

### 3.2 Your Application's API Endpoints

**Note**: These are the REST endpoints YOUR Spring Boot application should expose. The React frontend will call these endpoints. Your backend will internally call the external API Football service.

```
# Countries
GET /api/countries
Response: List of all countries with ID, name, and logo
Example: http://localhost:8080/api/countries

# Leagues
GET /api/leagues?country_id={id}
Response: List of leagues for the specified country
Example: http://localhost:8080/api/leagues?country_id=44

# Teams
GET /api/teams?league_id={id}
Response: List of teams in the specified league
Example: http://localhost:8080/api/teams?league_id=152

# Standings
GET /api/standings?league_id={id}&team_name={name}
Response: Standings for the league, optionally filtered by team name
Example: http://localhost:8080/api/standings?league_id=152&team_name=Arsenal

# Health & Monitoring
GET /api/health
GET /api/actuator/health
Response: Application health status
```

---

## Documentation Requirements

### 4.1 README.md
- Explain the design and implementation approach
- Include architecture overview
- List all design patterns used in the code
- Provide setup and installation instructions
- Include API documentation links
- Add testing instructions
- Document environment variables and configuration

### 4.2 Diagrams
- Create a sequence diagram showing the flow from user request to response
- Create a flowchart for offline mode handling
- Use draw.io (https://www.draw.io) for diagram creation
- Include diagrams in the README.md or separate docs folder

---

## Build & Deployment

### 5.1 Continuous Integration (CI)
- Create a Jenkins pipeline for automated builds
- Pipeline should include:
  - Code checkout
  - Backend build (Maven/Gradle)
  - Frontend build (npm/yarn)
  - Run unit and integration tests
  - Code quality checks
  - Generate test reports
- Store Jenkinsfile in the project root
- Include Jenkins job configuration files in the repository

### 5.2 Continuous Deployment (CD)
- Create Dockerfile for backend Spring Boot application
- Create Dockerfile for frontend React application
- Build and publish Docker images
- Run containerized services locally
- Include docker-compose.yml for multi-container setup
- Store all Docker files in the repository

### 5.3 Docker Requirements
```
- Backend Dockerfile
- Frontend Dockerfile
- docker-compose.yml
- .dockerignore files
```

---

## Constraints

- **No External Database**: Use in-memory data structures only
- **Minimal Third-Party Libraries**: Implement core logic without excessive dependencies
- **Custom Implementation**: Write own code/logic for data handling and caching

---

## External API Integration

**IMPORTANT**: This section describes the EXTERNAL API Football service that your Spring Boot backend will call to fetch data. These are NOT the endpoints you expose to your React frontend.

### API Football Service
- **Base URL**: `https://apiv3.apifootball.com/`
- **Documentation**: https://apifootball.com/documentation/
- **API Key**: `9bb66184e0c8145384fd2cc0f7b914ada57b4e8fd2e4d6d586adcc27c257a978`

### External API Endpoints & Request/Response Structure

Your Spring Boot backend will call these external endpoints to fetch football data.

#### 1. Get Countries
**Purpose**: Fetch list of all available countries

**Endpoint**: 
```
GET https://apiv3.apifootball.com/?action=get_countries&APIkey={APIkey}
```

**Request Parameters**:
- `action`: `get_countries` (required)
- `APIkey`: Your API key (required)

**Response Structure**:
```json
[
  {
    "country_id": "44",
    "country_name": "England",
    "country_logo": "https://apiv3.apifootball.com/badges/logo_country/44_england.png"
  },
  {
    "country_id": "6",
    "country_name": "Spain",
    "country_logo": "https://apiv3.apifootball.com/badges/logo_country/6_spain.png"
  }
]
```

---

#### 2. Get Leagues/Competitions
**Purpose**: Fetch list of leagues for a specific country

**Endpoint**: 
```
GET https://apiv3.apifootball.com/?action=get_leagues&country_id={country_id}&APIkey={APIkey}
```

**Request Parameters**:
- `action`: `get_leagues` (required)
- `APIkey`: Your API key (required)
- `country_id`: Country ID (optional - if not provided, returns all leagues)

**Response Structure**:
```json
[
  {
    "country_id": "6",
    "country_name": "Spain",
    "league_id": "302",
    "league_name": "La Liga",
    "league_season": "2020/2021",
    "league_logo": "https://apiv3.apifootball.com/badges/logo_leagues/302_la-liga.png",
    "country_logo": "https://apiv3.apifootball.com/badges/logo_country/6_spain.png"
  }
]
```

---

#### 3. Get Teams
**Purpose**: Fetch list of teams in a specific league

**Endpoint**: 
```
GET https://apiv3.apifootball.com/?action=get_teams&league_id={league_id}&APIkey={APIkey}
```

**Request Parameters**:
- `action`: `get_teams` (required)
- `APIkey`: Your API key (required)
- `league_id`: League ID (required if team_id is not set)
- `team_id`: Team ID (optional - mandatory if league_id is not set)

**Response Structure**:
```json
[
  {
    "team_key": "73",
    "team_name": "Atletico Madrid",
    "team_country": "Spain",
    "team_founded": "1903",
    "team_badge": "https://apiv3.apifootball.com/badges/73_atletico-madrid.jpg",
    "venue": {
      "venue_name": "Estadio Wanda Metropolitano",
      "venue_address": "Rosas",
      "venue_city": "Madrid",
      "venue_capacity": "68032",
      "venue_surface": "grass"
    },
    "players": [...],
    "coaches": [...]
  }
]
```

---

#### 4. Get Standings
**Purpose**: Fetch league standings for a specific league

**Endpoint**: 
```
GET https://apiv3.apifootball.com/?action=get_standings&league_id={league_id}&APIkey={APIkey}
```

**Request Parameters**:
- `action`: `get_standings` (required)
- `APIkey`: Your API key (required)
- `league_id`: League ID (required)

**Response Structure**:
```json
[
  {
    "country_name": "England",
    "league_id": "152",
    "league_name": "Premier League",
    "team_id": "141",
    "team_name": "Arsenal",
    "overall_promotion": "Promotion - Champions League (Group Stage: )",
    "overall_league_position": "1",
    "overall_league_payed": "0",
    "overall_league_W": "0",
    "overall_league_D": "0",
    "overall_league_L": "0",
    "overall_league_GF": "0",
    "overall_league_GA": "0",
    "overall_league_PTS": "0",
    "home_league_position": "1",
    "home_promotion": "",
    "home_league_payed": "0",
    "home_league_W": "0",
    "home_league_D": "0",
    "home_league_L": "0",
    "home_league_GF": "0",
    "home_league_GA": "0",
    "home_league_PTS": "0",
    "away_league_position": "1",
    "away_promotion": "",
    "away_league_payed": "0",
    "away_league_W": "0",
    "away_league_D": "0",
    "away_league_L": "0",
    "away_league_GF": "0",
    "away_league_GA": "0",
    "away_league_PTS": "0",
    "league_round": "",
    "team_badge": "https://apiv3.apifootball.com/badges/141_arsenal.jpg",
    "fk_stage_key": "6",
    "stage_name": "Current"
  }
]
```

---

### API Integration Flow

**Architecture Overview**:
```
React Frontend (Port 3000)
    ↓ HTTP Requests
    ↓ (Calls YOUR Spring Boot API)
    ↓
Spring Boot Backend (Port 8080)
    ↓ HTTP Requests  
    ↓ (Calls External API Football)
    ↓
API Football Service (apiv3.apifootball.com)
```

1. **User Flow**:
   - User opens the React application (http://localhost:3000)
   - React calls: `GET http://localhost:8080/api/countries`
   - Spring Boot receives request and calls external API: `https://apiv3.apifootball.com/?action=get_countries&APIkey=...`
   - Spring Boot caches the response and returns data to React
   - User selects a country from dropdown
   - React calls: `GET http://localhost:8080/api/leagues?country_id=44`
   - Spring Boot calls external API: `https://apiv3.apifootball.com/?action=get_leagues&country_id=44&APIkey=...`
   - Spring Boot caches and returns leagues to React
   - User selects a league from dropdown
   - React calls: `GET http://localhost:8080/api/teams?league_id=152`
   - Spring Boot calls external API: `https://apiv3.apifootball.com/?action=get_teams&league_id=152&APIkey=...`
   - Spring Boot caches and returns teams to React
   - User selects a team from dropdown
   - React calls: `GET http://localhost:8080/api/standings?league_id=152&team_name=Arsenal`
   - Spring Boot calls external API: `https://apiv3.apifootball.com/?action=get_standings&league_id=152&APIkey=...`
   - Spring Boot filters results by team name, caches, and returns to React

2. **Caching Strategy**:
   - Cache all countries (rarely changes)
   - Cache leagues by country_id
   - Cache teams by league_id
   - Cache standings by league_id with TTL (time-to-live)
   - Use in-memory data structures (HashMap, ConcurrentHashMap)

3. **Offline Mode**:
   - When toggle is enabled, serve only from cache
   - Show last updated timestamp
   - Display message if data not available in cache

---

## Project Structure (Suggested)

```
football-standings-app/
├── backend/                    # Spring Boot Application
│   ├── src/
│   │   ├── main/
│   │   │   ├── java/
│   │   │   │   └── com/sapient/football/
│   │   │   │       ├── controller/
│   │   │   │       ├── service/
│   │   │   │       ├── model/
│   │   │   │       ├── dto/
│   │   │   │       ├── cache/
│   │   │   │       ├── config/
│   │   │   │       └── exception/
│   │   │   └── resources/
│   │   │       ├── application.yml
│   │   │       └── application-prod.yml
│   │   └── test/
│   ├── build.gradle
│   ├── settings.gradle
│   ├── Dockerfile
│   └── Jenkinsfile
├── frontend/                   # React Application
│   ├── src/
│   │   ├── components/
│   │   ├── services/
│   │   ├── hooks/
│   │   ├── utils/
│   │   ├── App.jsx
│   │   └── index.js
│   ├── public/
│   ├── Dockerfile
│   ├── package.json
│   ├── tailwind.config.js
│   └── postcss.config.js
├── docker-compose.yml
├── README.md
└── docs/
    └── diagrams/
```

---

## Success Criteria

- ✅ Application successfully fetches and displays football standings
- ✅ Offline mode works with cached data
- ✅ All NFRs are implemented and demonstrated
- ✅ Test coverage exceeds 80%
- ✅ API documentation is complete and accessible via Swagger
- ✅ CI/CD pipeline successfully builds and deploys the application
- ✅ Docker containers run successfully locally
- ✅ README with diagrams is complete
- ✅ Code follows SOLID principles and design patterns

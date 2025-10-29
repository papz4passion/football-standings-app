# API Testing Guide - Newman & Postman Collection

This document provides comprehensive information about the Postman collection and Newman CLI testing setup for the Football Standings API.

## 📋 Table of Contents

- [Overview](#overview)
- [Postman Collection](#postman-collection)
- [Newman CLI Testing](#newman-cli-testing)
- [Quick Start](#quick-start)
- [Test Coverage](#test-coverage)
- [Running Tests](#running-tests)
- [Reports](#reports)
- [Troubleshooting](#troubleshooting)

## 🎯 Overview

The Football Standings API includes a comprehensive Postman collection with **80+ automated tests** covering all API endpoints. The collection can be imported into Postman for manual testing or executed via Newman CLI for automated testing and CI/CD integration.

## 📦 Postman Collection

### File Location
```
Football-Standings-API.postman_collection.json
```

### Collection Structure

The collection includes **13 API requests** organized as follows:

1. **Health Check** (7 tests)
2. **Get All Countries** (7 tests)
3. **Get Leagues by Country** (7 tests)
4. **Get Leagues - Missing Parameter** (4 tests)
5. **Get Teams by League** (6 tests)
6. **Get Teams - Missing Parameter** (3 tests)
7. **Get Standings by League** (6 tests)
8. **Get Standings with Team Filter** (4 tests)
9. **Get Standings - Missing Parameter** (2 tests)
10. **Enable Offline Mode** (3 tests)
11. **Disable Offline Mode** (3 tests)
12. **Clear Cache** (3 tests)
13. **Actuator Health Check** (3 tests)

### Collection Variables

The collection uses the following variables that are automatically populated during test execution:

| Variable | Description | Auto-populated |
|----------|-------------|----------------|
| `baseUrl` | API base URL (default: http://localhost:8080) | Pre-set |
| `countryId` | Country ID from countries endpoint | Yes |
| `countryName` | Country name for reference | Yes |
| `leagueId` | League ID from leagues endpoint | Yes |
| `leagueName` | League name for reference | Yes |
| `teamName` | Team name for standings filter | Yes |

### Importing into Postman

1. Open Postman Desktop or Web
2. Click **Import** button
3. Select `Football-Standings-API.postman_collection.json`
4. The collection will appear in your workspace

## 🧪 Test Coverage

### Test Categories

#### 1. **Status Code Validation** (13 tests)
- Validates correct HTTP status codes (200, 400)
- Ensures error handling returns appropriate codes

#### 2. **Response Time Tests** (9 tests)
- Health check: < 500ms
- Countries endpoint: < 2000ms
- Leagues endpoint: < 2000ms
- Teams endpoint: < 3000ms
- Standings endpoint: < 3000ms
- Cache operations: < 200ms

#### 3. **Response Structure Tests** (20 tests)
- Validates response is JSON array
- Checks required fields in DTOs
- Ensures proper error response structure

#### 4. **Data Validation Tests** (15 tests)
- Validates field presence (countryId, leagueName, etc.)
- Checks data relationships (league.countryId matches request)
- Verifies team filtering works correctly

#### 5. **Content-Type Tests** (10 tests)
- Ensures all responses are `application/json`
- Validates HTTP headers

#### 6. **Functional Tests** (13 tests)
- Health check returns "UP" status
- Offline mode enable/disable
- Cache clearing functionality
- Parameter validation (missing parameters)

### Test Assertions

Each test includes specific assertions using Postman's `pm.test()` API:

```javascript
// Status code validation
pm.test("Status code is 200", function () {
    pm.response.to.have.status(200);
});

// Response structure validation
pm.test("Response is an array", function () {
    var jsonData = pm.response.json();
    pm.expect(jsonData).to.be.an('array');
});

// Field validation
pm.test("Each country has required fields", function () {
    var jsonData = pm.response.json();
    if (jsonData.length > 0) {
        var country = jsonData[0];
        pm.expect(country).to.have.property('countryId');
        pm.expect(country).to.have.property('countryName');
    }
});

// Performance validation
pm.test("Response time is less than 2000ms", function () {
    pm.expect(pm.response.responseTime).to.be.below(2000);
});
```

## 🚀 Newman CLI Testing

Newman is Postman's command-line collection runner that enables:
- Automated API testing
- CI/CD pipeline integration
- HTML/JSON report generation
- Scheduled test execution

### Prerequisites

```bash
# Install Node.js (if not already installed)
brew install node  # macOS
# or download from https://nodejs.org/

# Install Newman and reporters globally
npm install -g newman newman-reporter-htmlextra
```

### Manual Newman Execution

#### Basic Run
```bash
newman run Football-Standings-API.postman_collection.json
```

#### Run with HTML Report
```bash
newman run Football-Standings-API.postman_collection.json \
    --reporters cli,htmlextra \
    --reporter-htmlextra-export newman-reports/report.html
```

#### Run with JSON Report
```bash
newman run Football-Standings-API.postman_collection.json \
    --reporters cli,json \
    --reporter-json-export newman-reports/report.json
```

#### Run with Multiple Reporters
```bash
newman run Football-Standings-API.postman_collection.json \
    --reporters cli,htmlextra,json \
    --reporter-htmlextra-export newman-reports/report.html \
    --reporter-json-export newman-reports/report.json \
    --bail \
    --color on
```

### Newman Options Explained

| Option | Description |
|--------|-------------|
| `--reporters cli,htmlextra,json` | Generate CLI, HTML, and JSON reports |
| `--reporter-htmlextra-export` | Path for HTML report |
| `--reporter-json-export` | Path for JSON report |
| `--bail` | Stop on first test failure |
| `--color on` | Enable colored CLI output |
| `--delay-request 100` | Add 100ms delay between requests |
| `--timeout-request 10000` | Set request timeout to 10 seconds |

## 🏃 Quick Start

### Automated Script (Recommended)

The project includes `run-newman-tests.sh` that automates the entire process:

```bash
./run-newman-tests.sh
```

This script will:
1. ✅ Check Docker and Newman installation
2. 🏗️ Build Docker image from Dockerfile
3. 🚀 Start the application in Docker container
4. ⏳ Wait for application to be healthy
5. 🧪 Run Newman tests with HTML/JSON reports
6. 📊 Generate test reports in `newman-reports/`
7. 🧹 Cleanup (optional)

### Manual Process

#### Step 1: Build Docker Image
```bash
cd backend
docker build -t football-standings-api:latest .
cd ..
```

#### Step 2: Run Docker Container
```bash
docker run -d \
    --name football-api-test \
    -p 8080:8080 \
    -e API_FOOTBALL_KEY="9bb66184e0c8145384fd2cc0f7b914ada57b4e8fd2e4d6d586adcc27c257a978" \
    football-standings-api:latest
```

#### Step 3: Wait for Application to Start
```bash
# Check health endpoint
curl http://localhost:8080/api/health
```

#### Step 4: Run Newman Tests
```bash
newman run Football-Standings-API.postman_collection.json \
    --reporters cli,htmlextra,json \
    --reporter-htmlextra-export newman-reports/report.html \
    --reporter-json-export newman-reports/report.json
```

#### Step 5: View Reports
```bash
# Open HTML report in browser
open newman-reports/report.html  # macOS
xdg-open newman-reports/report.html  # Linux
start newman-reports/report.html  # Windows
```

#### Step 6: Cleanup
```bash
docker stop football-api-test
docker rm football-api-test
```

## 📊 Reports

### HTML Report (newman-reporter-htmlextra)

The HTML report includes:
- **Summary Dashboard**: Total requests, tests passed/failed, response times
- **Request Details**: Individual request results with test outcomes
- **Response Preview**: JSON response bodies for each request
- **Test Results**: Detailed test assertions with pass/fail status
- **Performance Metrics**: Response time charts and statistics
- **Request/Response Headers**: Full HTTP header information

**Location**: `newman-reports/report.html`

### JSON Report

The JSON report provides:
- Programmatic access to test results
- Integration with CI/CD systems
- Custom reporting and analytics
- Test history tracking

**Location**: `newman-reports/report.json`

**Example Structure**:
```json
{
  "collection": {...},
  "run": {
    "stats": {
      "requests": {"total": 13},
      "assertions": {"total": 80, "failed": 0},
      "testScripts": {"total": 13},
      "prerequestScripts": {"total": 0}
    },
    "timings": {
      "responseAverage": 234,
      "responseMin": 12,
      "responseMax": 1456
    },
    "executions": [...]
  }
}
```

### CLI Output

Real-time test execution feedback:

```
┌─────────────────────────┬────────────────────┬───────────────────┐
│                         │           executed │            failed │
├─────────────────────────┼────────────────────┼───────────────────┤
│              iterations │                  1 │                 0 │
├─────────────────────────┼────────────────────┼───────────────────┤
│                requests │                 13 │                 0 │
├─────────────────────────┼────────────────────┼───────────────────┤
│            test-scripts │                 13 │                 0 │
├─────────────────────────┼────────────────────┼───────────────────┤
│      prerequest-scripts │                  0 │                 0 │
├─────────────────────────┼────────────────────┼───────────────────┤
│              assertions │                 80 │                 0 │
├─────────────────────────┴────────────────────┴───────────────────┤
│ total run duration: 5.2s                                         │
├───────────────────────────────────────────────────────────────────┤
│ total data received: 45.6kB (approx)                            │
├───────────────────────────────────────────────────────────────────┤
│ average response time: 234ms [min: 12ms, max: 1456ms, s.d.: 89ms]│
└───────────────────────────────────────────────────────────────────┘
```

## 🐛 Troubleshooting

### Newman Installation Issues

**Problem**: `newman: command not found`

**Solution**:
```bash
# Check npm global bin path
npm config get prefix

# Install Newman globally
npm install -g newman newman-reporter-htmlextra

# Verify installation
newman --version
```

### Docker Container Not Starting

**Problem**: Container exits immediately or fails to start

**Solution**:
```bash
# Check container logs
docker logs football-api-test

# Verify image was built successfully
docker images | grep football-standings-api

# Check port availability
lsof -i :8080

# Rebuild image with no cache
cd backend && docker build --no-cache -t football-standings-api:latest .
```

### Application Health Check Failing

**Problem**: `/api/health` endpoint returns error or times out

**Solution**:
```bash
# Check if application is running
docker ps

# Check application logs
docker logs football-api-test --tail 100

# Verify environment variables
docker inspect football-api-test | grep -A 10 Env

# Check if API key is set
docker exec football-api-test env | grep API_FOOTBALL_KEY
```

### Tests Failing

**Problem**: Newman tests fail with connection errors

**Solution**:
```bash
# Verify application is accessible
curl -v http://localhost:8080/api/health

# Check if port 8080 is in use by another process
lsof -i :8080

# Increase wait time in script (edit run-newman-tests.sh)
WAIT_TIMEOUT=120  # Increase from 60 to 120 seconds

# Run tests with increased timeout
newman run Football-Standings-API.postman_collection.json \
    --timeout-request 15000
```

### External API Rate Limiting

**Problem**: Some tests fail due to API rate limits

**Solution**:
```bash
# Enable offline mode first, then run tests
curl -X POST http://localhost:8080/api/offline-mode?enabled=true

# Or add delay between requests
newman run Football-Standings-API.postman_collection.json \
    --delay-request 500
```

### Permission Denied Error

**Problem**: `./run-newman-tests.sh: Permission denied`

**Solution**:
```bash
chmod +x run-newman-tests.sh
```

## 🔄 CI/CD Integration

### GitHub Actions Example

```yaml
name: API Tests

on: [push, pull_request]

jobs:
  test:
    runs-on: ubuntu-latest
    steps:
      - uses: actions/checkout@v3
      
      - name: Setup Node.js
        uses: actions/setup-node@v3
        with:
          node-version: '18'
      
      - name: Install Newman
        run: npm install -g newman newman-reporter-htmlextra
      
      - name: Build Docker Image
        run: cd backend && docker build -t football-api .
      
      - name: Run Container
        run: |
          docker run -d --name api-test -p 8080:8080 \
            -e API_FOOTBALL_KEY=${{ secrets.API_KEY }} \
            football-api
      
      - name: Wait for API
        run: |
          for i in {1..30}; do
            if curl -f http://localhost:8080/api/health; then
              break
            fi
            sleep 2
          done
      
      - name: Run Newman Tests
        run: |
          newman run Football-Standings-API.postman_collection.json \
            --reporters cli,htmlextra,json \
            --reporter-htmlextra-export reports/report.html \
            --reporter-json-export reports/report.json
      
      - name: Upload Reports
        uses: actions/upload-artifact@v3
        if: always()
        with:
          name: newman-reports
          path: reports/
      
      - name: Cleanup
        if: always()
        run: docker stop api-test && docker rm api-test
```

### Jenkins Pipeline Example

```groovy
pipeline {
    agent any
    
    stages {
        stage('Setup') {
            steps {
                sh 'npm install -g newman newman-reporter-htmlextra'
            }
        }
        
        stage('Build Docker Image') {
            steps {
                dir('backend') {
                    sh 'docker build -t football-api .'
                }
            }
        }
        
        stage('Run Application') {
            steps {
                sh '''
                    docker run -d --name api-test -p 8080:8080 \
                        -e API_FOOTBALL_KEY=${API_KEY} \
                        football-api
                '''
            }
        }
        
        stage('Wait for Health') {
            steps {
                sh '''
                    for i in {1..30}; do
                        if curl -f http://localhost:8080/api/health; then
                            break
                        fi
                        sleep 2
                    done
                '''
            }
        }
        
        stage('Run Tests') {
            steps {
                sh '''
                    newman run Football-Standings-API.postman_collection.json \
                        --reporters cli,htmlextra,json \
                        --reporter-htmlextra-export reports/report.html \
                        --reporter-json-export reports/report.json
                '''
            }
        }
    }
    
    post {
        always {
            sh 'docker stop api-test && docker rm api-test'
            publishHTML([
                reportDir: 'reports',
                reportFiles: 'report.html',
                reportName: 'Newman Test Report'
            ])
        }
    }
}
```

## 📝 Best Practices

1. **Run tests sequentially**: The collection uses collection variables that are populated during execution
2. **Clear cache between test runs**: Use the "Clear Cache" endpoint to ensure fresh data
3. **Monitor response times**: Adjust timeout values based on your environment
4. **Use environment variables**: Store API keys and base URLs in environment variables
5. **Review reports**: Always check HTML reports for detailed test results
6. **Version control**: Keep the collection in Git for tracking changes

## 📚 Additional Resources

- [Postman Learning Center](https://learning.postman.com/)
- [Newman Documentation](https://github.com/postmanlabs/newman)
- [Newman HTML Extra Reporter](https://github.com/DannyDainton/newman-reporter-htmlextra)
- [Postman Test Scripts](https://learning.postman.com/docs/writing-scripts/test-scripts/)

## 🎉 Summary

You now have a complete API testing setup with:
- ✅ 13 API requests covering all endpoints
- ✅ 80+ automated tests
- ✅ Docker containerization
- ✅ Newman CLI automation
- ✅ HTML and JSON reports
- ✅ One-command test execution
- ✅ CI/CD ready configuration

Run `./run-newman-tests.sh` to see it in action! 🚀

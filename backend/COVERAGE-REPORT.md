# Code Coverage Report - Backend Application

**Generated:** October 30, 2025  
**Tool:** JaCoCo 0.8.11  
**Project:** Football Standings API (Backend)

---

## 📊 Overall Coverage Summary

| Metric | Missed | Total | Coverage |
|--------|--------|-------|----------|
| **Instructions** | 792 | 981 | **19%** |
| **Branches** | 54 | 62 | **12%** |
| **Lines** | 187 | 243 | **23%** |
| **Methods** | 42 | 63 | **33%** |
| **Classes** | 6 | 12 | **50%** |
| **Complexity** | 70 | 94 | **26%** |

---

## 📦 Package-Level Coverage

### 1. com.sapient.football.service
**Purpose:** Core business logic and external API integration

| Metric | Missed | Total | Coverage |
|--------|--------|-------|----------|
| Instructions | 430 | 519 | **17%** |
| Branches | 27 | 34 | **20%** |
| Lines | 96 | 125 | **23%** |
| Methods | 24 | 32 | **25%** |
| Classes | 4 | 6 | **33%** |
| Complexity | 38 | 49 | **22%** |

**Files:**
- FootballService.java
- FootballApiService.java

---

### 2. com.sapient.football.controller
**Purpose:** REST API endpoints

| Metric | Missed | Total | Coverage |
|--------|--------|-------|----------|
| Instructions | 152 | 185 | **17%** |
| Branches | 15 | 16 | **6%** |
| Lines | 35 | 46 | **24%** |
| Methods | 4 | 11 | **64%** |
| Classes | 0 | 2 | **100%** ✅ |
| Complexity | 12 | 19 | **37%** |

**Files:**
- FootballController.java
- HealthController.java

**Status:** All controller classes are loaded by tests

---

### 3. com.sapient.football.cache
**Purpose:** Custom cache management

| Metric | Missed | Total | Coverage |
|--------|--------|-------|----------|
| Instructions | 130 | 142 | **8%** ⚠️ |
| Branches | 12 | 12 | **0%** ⚠️ |
| Lines | 34 | 37 | **8%** |
| Methods | 9 | 11 | **18%** |
| Classes | 1 | 2 | **50%** |
| Complexity | 15 | 17 | **12%** |

**Files:**
- CustomCacheManager.java

**Issue:** Low coverage - cache logic not tested

---

### 4. com.sapient.football.exception
**Purpose:** Exception handling and error responses

| Metric | Missed | Total | Coverage |
|--------|--------|-------|----------|
| Instructions | 80 | 135 | **40%** ⭐ |
| Branches | 0 | 0 | **n/a** |
| Lines | 22 | 35 | **37%** |
| Methods | 5 | 9 | **44%** |
| Classes | 1 | 2 | **50%** |
| Complexity | 5 | 9 | **44%** |

**Files:**
- GlobalExceptionHandler.java
- FootballApiException.java
- ErrorResponse.java (excluded from coverage)

**Status:** Best coverage among all packages

---

## 🎯 Test Execution Summary

### Test Results
- **Total Tests:** 12
- **Passed:** 9 ✅
- **Failed:** 3 ❌
- **Skipped:** 0

### Failed Tests
1. `FootballControllerTest.testGetCountries_Success()` - Line 47
2. `FootballControllerTest.testToggleOfflineMode()` - Line 85
3. `FootballControllerTest.testClearCache()` - Line 94

### Passing Tests
- ✅ FootballStandingsApplicationTests.contextLoads()
- ✅ FootballServiceTest (all 5 tests passing)
- ✅ FootballControllerTest.testGetLeagues_MissingParameter()
- ✅ FootballControllerTest.testGetTeams_MissingParameter()
- ✅ FootballControllerTest.testGetStandings_MissingParameter()

---

## 🔍 Excluded from Coverage

The following packages/classes are excluded from coverage analysis:

- `com.sapient.football.config.**` - Configuration classes
- `com.sapient.football.dto.**` - Data Transfer Objects (POJOs)
- `com.sapient.football.exception.ErrorResponse` - Simple DTO
- `com.sapient.football.FootballStandingsApplication` - Main class

---

## 📈 Coverage Thresholds

### Current Configuration
| Level | Metric | Minimum | Status |
|-------|--------|---------|--------|
| **Overall** | All | 70% | ❌ Not Met (19%) |
| **Per Class** | Line Coverage | 60% | ❌ Not Met |
| **Per Method** | Line Coverage | 50% | ❌ Not Met |

### Recommendations
Given the current 19% coverage, thresholds are intentionally not enforced in build.  
To enable enforcement, uncomment in `build.gradle`:
```gradle
build.dependsOn jacocoTestCoverageVerification
```

---

## 📁 Report Locations

### HTML Report
```
backend/build/reports/jacoco/test/html/index.html
```
Open in browser: `open backend/build/reports/jacoco/test/html/index.html`

### XML Report
```
backend/build/reports/jacoco/test/jacocoTestReport.xml
```
Suitable for CI/CD integration (SonarQube, Codecov, etc.)

### Test Report
```
backend/build/reports/tests/test/index.html
```

---

## 🚀 How to Generate Coverage

### Command Line
```bash
cd backend
gradle clean test jacocoTestReport
```

### With Coverage Verification
```bash
gradle test jacocoTestCoverageVerification
```

### View HTML Report
```bash
# macOS
open build/reports/jacoco/test/html/index.html

# Linux
xdg-open build/reports/jacoco/test/html/index.html

# Windows
start build/reports/jacoco/test/html/index.html
```

---

## 🎯 Improvement Recommendations

### High Priority (Increase Coverage to 50%+)

1. **Fix Failing Controller Tests** ⚠️
   - Update test assertions to match actual response format
   - Verify HTTP status codes and response bodies
   - Mock `FootballMetrics` properly

2. **Add Service Layer Tests**
   - Test `FootballApiService` API call methods
   - Test error handling and fallbacks
   - Test offline mode behavior
   - **Target:** 60% service coverage

3. **Test Cache Management**
   - Test `CustomCacheManager` operations
   - Test TTL expiration logic
   - Test cache hit/miss scenarios
   - **Target:** 50% cache coverage

### Medium Priority (Increase Coverage to 70%+)

4. **Controller Integration Tests**
   - Test all API endpoints
   - Test parameter validation
   - Test error responses
   - **Target:** 80% controller coverage

5. **Exception Handler Tests**
   - Test all exception types
   - Verify error response format
   - Test validation errors
   - **Target:** 90% exception coverage

### Low Priority (Nice to Have)

6. **Edge Case Testing**
   - Test with null/empty inputs
   - Test concurrent cache access
   - Test API timeout scenarios

7. **Integration Tests**
   - Test with real external API (mocked)
   - Test full request/response flow
   - Test database interactions (if applicable)

---

## 🔧 JaCoCo Configuration

### Current Setup (`build.gradle`)

```gradle
jacoco {
    toolVersion = "0.8.11"
}

jacocoTestReport {
    reports {
        xml.required = true
        html.required = true
        csv.required = false
    }
    
    // Exclude DTOs and config classes
    afterEvaluate {
        classDirectories.setFrom(files(classDirectories.files.collect {
            fileTree(dir: it, exclude: [
                '**/config/**',
                '**/dto/**',
                '**/ ErrorResponse.class',
                '**/FootballStandingsApplication.class'
            ])
        }))
    }
}
```

---

## 📊 CI/CD Integration

### GitHub Actions
Coverage reports are generated automatically in the CI pipeline:
```yaml
- name: Generate test report
  run: ./gradlew jacocoTestReport

- name: Upload coverage report
  uses: actions/upload-artifact@v4
  with:
    name: coverage-report
    path: backend/build/reports/jacoco/test/
```

### SonarQube Integration
Upload XML report to SonarQube:
```bash
sonar-scanner \
  -Dsonar.coverage.jacoco.xmlReportPaths=backend/build/reports/jacoco/test/jacocoTestReport.xml
```

---

## 🎓 Coverage Best Practices

### What to Test
✅ Business logic methods  
✅ API endpoints and controllers  
✅ Error handling and validation  
✅ Edge cases and boundary conditions  
✅ Service layer interactions  

### What NOT to Test
❌ Simple DTOs with only getters/setters  
❌ Configuration classes  
❌ Main application class  
❌ Auto-generated code  
❌ Third-party library code  

---

## 📝 Next Steps

1. **Fix failing tests** to get accurate coverage data
2. **Increase service layer coverage** to 60%+
3. **Add cache management tests**
4. **Update CI/CD** to fail on coverage < 70%
5. **Review uncovered code** and add targeted tests

---

## 📞 Support

For questions about code coverage:
- Review JaCoCo HTML report for detailed line-by-line coverage
- Check test execution report for test failures
- Run `gradle test --info` for detailed test output

---

**Generated by:** JaCoCo Maven Plugin  
**Coverage Engine:** JaCoCo 0.8.11  
**Last Updated:** October 30, 2025  
**Status:** ⚠️ Coverage needs improvement (Current: 19%, Target: 70%)

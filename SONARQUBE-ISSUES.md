# SonarQube Issues Tracker

**Project:** Football Standings Application  
**Analysis Date:** October 29, 2025  
**Total Issues:** 12  

---

## 🔴 CRITICAL (1)

### C-001: Nginx Docker Image Vulnerabilities
- **File:** `frontend/Dockerfile`
- **Line:** 23
- **Current:** `FROM nginx:1.25-alpine`
- **Issue:** Contains 5 critical + 19 high vulnerabilities
- **Fix:**
  ```dockerfile
  FROM nginx:1.27-alpine
  ```
- **Priority:** HIGH
- **Assignee:** DevOps
- **Status:** 🔲 Open

---

## 🟡 MAJOR (6)

### M-001: Outdated Spring Boot Version
- **File:** `backend/build.gradle`
- **Line:** 1
- **Current:** Spring Boot 3.3.5
- **Issue:** Missing security patches (3.3.13 available)
- **Fix:** Update version to 3.3.13
- **Priority:** MEDIUM
- **Assignee:** Backend Team
- **Status:** 🔲 Open

### M-002: Deprecated Prometheus Property
- **File:** `backend/src/main/resources/application.yml`
- **Line:** 46
- **Current:** `management.metrics.export.prometheus.enabled`
- **Issue:** Deprecated property
- **Fix:** Use `management.prometheus.metrics.export.enabled`
- **Priority:** MEDIUM
- **Assignee:** Backend Team
- **Status:** 🔲 Open

### M-003: String Literal Duplication - GlobalExceptionHandler
- **File:** `backend/src/main/java/com/sapient/football/exception/GlobalExceptionHandler.java`
- **Line:** 52
- **Issue:** "Bad Request" duplicated 3 times
- **Fix:**
  ```java
  private static final String BAD_REQUEST = "Bad Request";
  ```
- **Priority:** LOW
- **Assignee:** Backend Team
- **Status:** 🔲 Open

### M-004: String Literal Duplication - FootballApiService (action)
- **File:** `backend/src/main/java/com/sapient/football/service/FootballApiService.java`
- **Line:** 43
- **Issue:** "action" duplicated 4 times
- **Fix:**
  ```java
  private static final String PARAM_ACTION = "action";
  ```
- **Priority:** LOW
- **Assignee:** Backend Team
- **Status:** 🔲 Open

### M-005: String Literal Duplication - FootballApiService (APIkey)
- **File:** `backend/src/main/java/com/sapient/football/service/FootballApiService.java`
- **Line:** 44
- **Issue:** "APIkey" duplicated 4 times
- **Fix:**
  ```java
  private static final String PARAM_API_KEY = "APIkey";
  ```
- **Priority:** LOW
- **Assignee:** Backend Team
- **Status:** 🔲 Open

### M-006: String Literal Duplication - FootballApiService (error message)
- **File:** `backend/src/main/java/com/sapient/football/service/FootballApiService.java`
- **Line:** 50
- **Issue:** "Returning empty list due to API error" duplicated 4 times
- **Fix:**
  ```java
  private static final String EMPTY_LIST_MESSAGE = "Returning empty list due to API error";
  ```
- **Priority:** LOW
- **Assignee:** Backend Team
- **Status:** 🔲 Open

---

## 🟢 MINOR (5)

### L-001: Inefficient Stream API Usage
- **File:** `backend/src/main/java/com/sapient/football/service/FootballService.java`
- **Line:** 178
- **Issue:** Using `.collect(Collectors.toList())` instead of `.toList()`
- **Fix:**
  ```java
  .toList(); // Instead of .collect(Collectors.toList())
  ```
- **Priority:** LOW
- **Assignee:** Backend Team
- **Status:** 🔲 Open

### L-002: Missing @NonNull Annotation
- **File:** `backend/src/main/java/com/sapient/football/config/CorsConfig.java`
- **Line:** 15
- **Issue:** Parameter should be annotated
- **Fix:**
  ```java
  public void addCorsMappings(@NonNull CorsRegistry registry) {
  ```
- **Priority:** LOW
- **Assignee:** Backend Team
- **Status:** 🔲 Open

### L-003: Unused Import
- **File:** `backend/src/main/java/com/sapient/football/config/MetricsConfig.java`
- **Line:** 7
- **Issue:** `org.springframework.context.annotation.Bean` never used
- **Fix:** Remove the import statement
- **Priority:** LOW
- **Assignee:** Backend Team
- **Status:** 🔲 Open

### L-004: Missing Private Constructor
- **File:** `backend/src/main/java/com/sapient/football/config/MetricsConfig.java`
- **Line:** 16
- **Issue:** Utility class should have private constructor
- **Fix:**
  ```java
  private MetricsConfig() {
      throw new IllegalStateException("Utility class");
  }
  ```
- **Priority:** LOW
- **Assignee:** Backend Team
- **Status:** 🔲 Open

### L-005: String Literal Duplication - MetricsConfig
- **File:** `backend/src/main/java/com/sapient/football/config/MetricsConfig.java`
- **Lines:** 50-51
- **Issue:** "football.api.requests" and "endpoint" duplicated 4 times each
- **Fix:**
  ```java
  private static final String METRIC_NAME = "football.api.requests";
  private static final String TAG_ENDPOINT = "endpoint";
  ```
- **Priority:** LOW
- **Assignee:** Backend Team
- **Status:** 🔲 Open

---

## ℹ️ INFORMATIONAL (Not Actionable)

### I-001: Tailwind CSS Directives
- **File:** `frontend/src/index.css`
- **Lines:** 1-3
- **Issue:** Unknown @tailwind directives
- **Note:** False positive - PostCSS handles these correctly
- **Action:** None required
- **Status:** ✅ Ignored

### I-002: Custom YAML Properties
- **File:** `backend/src/main/resources/application.yml`
- **Lines:** 13, 20
- **Issue:** Unknown properties 'api' and 'cache'
- **Note:** Application-specific custom properties
- **Action:** None required
- **Status:** ✅ Ignored

### I-003: YAML Keys with Special Characters
- **File:** `backend/src/main/resources/application.yml`
- **Lines:** 51, 53, 62, 63
- **Issue:** Keys contain dots and should be escaped
- **Note:** Works correctly, optional improvement
- **Priority:** VERY LOW
- **Status:** 🔲 Optional

---

## 📊 Issue Statistics

### By Severity
- 🔴 Critical: 1 (8%)
- 🟡 Major: 6 (50%)
- 🟢 Minor: 5 (42%)
- ℹ️ Info: 3 (not counted)

### By File Type
- Java Files: 10 issues
- Configuration: 2 issues
- Docker: 1 issue
- TypeScript: 0 issues ✅

### By Priority
- HIGH: 1 (needs immediate attention)
- MEDIUM: 2 (within this sprint)
- LOW: 9 (backlog items)

---

## 🎯 Sprint Planning

### Sprint 1 (This Week)
- [ ] C-001: Update Nginx image
- [ ] M-001: Update Spring Boot version
- [ ] M-002: Fix deprecated property

### Sprint 2 (Next Week)
- [ ] M-003 through M-006: Extract string constants
- [ ] L-001: Modernize Stream API

### Backlog
- [ ] L-002 through L-005: Code quality improvements

---

## ✅ Resolution Checklist

When fixing an issue:
1. [ ] Update status to 🔄 In Progress
2. [ ] Implement the fix
3. [ ] Run SonarQube analysis again
4. [ ] Verify issue is resolved
5. [ ] Update status to ✅ Resolved
6. [ ] Commit with reference to issue ID

---

## 📝 Notes

- All frontend TypeScript files are **clean** - zero issues! ✅
- No security vulnerabilities in application code
- Most issues are **code style** and **maintainability** related
- Docker image update is the only **critical** item

---

**Last Updated:** October 29, 2025  
**Reviewed By:** SonarQube Analysis  
**Next Review:** Weekly

# GitHub Actions CI/CD Pipelines

This directory contains automated CI/CD workflows for the Football Standings Application.

## 📋 Available Workflows

### 1. Backend CI/CD (`backend-ci-cd.yml`)
**Triggers:** Push/PR to `main` or `develop` branches (backend files only)

**Jobs:**
- ✅ **Build** - Compiles Java code with Gradle
- ✅ **Test** - Runs unit tests with coverage reports
- ✅ **Code Quality** - Checkstyle and PMD analysis
- 🐳 **Docker Build** - Creates Docker image
- 🚀 **Deploy (Dummy)** - Simulates deployment to AWS ECS, Kubernetes, Azure

**Artifacts:**
- Backend JAR file
- Test results and coverage reports
- Docker image (tar.gz)

---

### 2. Frontend CI/CD (`frontend-ci-cd.yml`)
**Triggers:** Push/PR to `main` or `develop` branches (frontend files only)

**Jobs:**
- ✅ **Build** - Compiles React TypeScript app
- ✅ **Lint** - ESLint and TypeScript type checking
- ✅ **Test** - Runs unit tests with coverage
- 🐳 **Docker Build** - Creates Nginx-based Docker image
- 🚀 **Deploy (Dummy)** - Simulates deployment to S3/CloudFront, Netlify, Vercel, Azure, Kubernetes

**Artifacts:**
- Production build files
- Test coverage reports
- Docker image (tar.gz)

---

### 3. Full Stack CI/CD (`full-stack-ci-cd.yml`)
**Triggers:** Push to `main`, PRs, or manual dispatch

**Jobs:**
- 🔍 **Validate** - Checks repository structure and docker-compose
- ✅ **Backend Build & Test** - Full backend pipeline
- ✅ **Frontend Build & Test** - Full frontend pipeline
- 🧪 **Newman API Tests** - API integration tests (dummy)
- 🐳 **Docker Compose Test** - Integration testing (dummy)
- 🚀 **Deploy to Staging** - Staging deployment (dummy)
- 🚀 **Deploy to Production** - Production deployment with blue-green strategy (dummy)
- 📢 **Notifications** - Deployment notifications (dummy)
- ⚠️ **Rollback Plan** - Automatic rollback documentation

**Features:**
- Complete end-to-end pipeline
- Multi-environment deployment (staging → production)
- Health checks and smoke tests
- Performance verification
- Rollback capabilities

---

### 4. Pull Request Validation (`pr-validation.yml`)
**Triggers:** New PRs, PR updates to `main` or `develop`

**Jobs:**
- 📋 **PR Info** - Displays PR details
- 🔍 **Changed Files Detection** - Identifies which parts changed
- ✅ **Backend Validation** - Runs only if backend files changed
- ✅ **Frontend Validation** - Runs only if frontend files changed
- 🔒 **Security Scan** - Trivy vulnerability scanning
- 📊 **Summary** - Consolidated validation results

**Features:**
- Conditional execution based on changed files
- Faster feedback for targeted changes
- Security vulnerability detection
- Automated PR status checks

---

## 🚀 Deployment Stages (Dummy Implementation)

All deployment stages are **simulated** as there's no cloud infrastructure available yet. The workflows print detailed logs showing what would happen in a real deployment.

### Backend Deployment Options:
1. **AWS ECS** - Elastic Container Service
2. **Kubernetes** - Self-managed or EKS
3. **Azure App Service** - PaaS deployment

### Frontend Deployment Options:
1. **AWS S3 + CloudFront** - Static hosting with CDN
2. **Azure Static Web Apps** - Serverless hosting
3. **Netlify** - Jamstack platform
4. **Vercel** - Frontend deployment platform
5. **Kubernetes + Nginx** - Container-based hosting

---

## 🔧 Configuration

### Required Secrets (for real deployment):
```yaml
# Docker Hub
DOCKER_USERNAME
DOCKER_PASSWORD

# AWS
AWS_ACCESS_KEY_ID
AWS_SECRET_ACCESS_KEY
AWS_REGION
ECR_REGISTRY

# Azure
AZURE_CREDENTIALS
AZURE_SUBSCRIPTION_ID

# Netlify
NETLIFY_AUTH_TOKEN
NETLIFY_SITE_ID

# Vercel
VERCEL_TOKEN

# Slack/Notifications
SLACK_WEBHOOK_URL
```

### Environment Variables:
- `JAVA_VERSION`: 21
- `NODE_VERSION`: 18
- `GRADLE_VERSION`: 8.14.1

---

## 📊 Workflow Status Badges

Add these to your main README.md:

```markdown
![Backend CI/CD](https://github.com/papz4passion/football-standings-app/workflows/Backend%20CI/CD/badge.svg)
![Frontend CI/CD](https://github.com/papz4passion/football-standings-app/workflows/Frontend%20CI/CD/badge.svg)
![Full Stack CI/CD](https://github.com/papz4passion/football-standings-app/workflows/Full%20Stack%20CI/CD/badge.svg)
![PR Validation](https://github.com/papz4passion/football-standings-app/workflows/Pull%20Request%20Validation/badge.svg)
```

---

## 🎯 Usage

### Automatic Triggers:
- **Push to main** → Runs full CI/CD pipeline with deployment
- **Push to develop** → Runs build and test only
- **Create PR** → Runs PR validation
- **Update PR** → Re-runs validation

### Manual Triggers:
```bash
# Trigger full stack deployment manually
gh workflow run full-stack-ci-cd.yml --ref main

# View workflow runs
gh run list

# Watch a specific run
gh run watch <run-id>
```

---

## 📝 Workflow Files

| File | Purpose | Runs On |
|------|---------|---------|
| `backend-ci-cd.yml` | Backend pipeline | Backend changes |
| `frontend-ci-cd.yml` | Frontend pipeline | Frontend changes |
| `full-stack-ci-cd.yml` | Complete E2E pipeline | Any change |
| `pr-validation.yml` | PR checks | Pull requests |

---

## 🔄 Deployment Flow

```
┌─────────────────────────────────────────────────────────┐
│                     CODE PUSH                           │
└────────────────────┬────────────────────────────────────┘
                     │
          ┌──────────┴──────────┐
          │                     │
    ┌─────▼─────┐         ┌────▼──────┐
    │  Backend  │         │  Frontend │
    │   Build   │         │   Build   │
    └─────┬─────┘         └────┬──────┘
          │                     │
    ┌─────▼─────┐         ┌────▼──────┐
    │   Tests   │         │   Tests   │
    └─────┬─────┘         └────┬──────┘
          │                     │
          └──────────┬──────────┘
                     │
              ┌──────▼──────┐
              │   Docker    │
              │    Build    │
              └──────┬──────┘
                     │
              ┌──────▼──────┐
              │   Staging   │
              │  (Dummy)    │
              └──────┬──────┘
                     │
              ┌──────▼──────┐
              │   Smoke     │
              │   Tests     │
              └──────┬──────┘
                     │
              ┌──────▼──────┐
              │ Production  │
              │  (Dummy)    │
              └──────┬──────┘
                     │
              ┌──────▼──────┐
              │   Health    │
              │   Checks    │
              └─────────────┘
```

---

## 🛠️ Customization

### To Enable Real Deployment:

1. **Add Cloud Credentials** to GitHub Secrets
2. **Uncomment Real Commands** in deployment jobs
3. **Configure Cloud Resources**:
   - Create ECS clusters/services
   - Set up S3 buckets and CloudFront
   - Configure Kubernetes clusters
   - Set up monitoring and logging

4. **Update Environment URLs**:
   ```yaml
   environment:
     name: production
     url: https://your-actual-domain.com
   ```

5. **Enable Health Checks**:
   - Replace dummy curl commands with real endpoints
   - Configure proper health check URLs

---

## 📚 Best Practices

✅ **Implemented:**
- Separate workflows for backend/frontend
- Conditional execution based on changed files
- Artifact caching for faster builds
- Comprehensive testing before deployment
- Multi-stage deployment (staging → production)
- Rollback documentation

🚀 **Recommended for Production:**
- Add SonarQube/SonarCloud integration
- Implement automated rollback triggers
- Add performance testing (Lighthouse, k6)
- Set up monitoring alerts (PagerDuty, Opsgenie)
- Enable dependency vulnerability scanning
- Add changelog generation
- Implement semantic versioning

---

## 🐛 Troubleshooting

### Build Failures:
```bash
# Check logs
gh run view <run-id> --log

# Re-run failed jobs
gh run rerun <run-id>
```

### Debugging Locally:
```bash
# Install act (GitHub Actions local runner)
brew install act

# Run workflow locally
act push -W .github/workflows/backend-ci-cd.yml
```

---

## 📞 Support

For issues with GitHub Actions:
- Check workflow logs in the Actions tab
- Review step output for error messages
- Verify secrets are configured correctly
- Ensure environment variables are set

---

**Last Updated:** October 29, 2025  
**Maintainer:** Development Team  
**Status:** ✅ All workflows operational (dummy deployment mode)

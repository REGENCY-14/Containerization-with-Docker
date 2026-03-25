# CI/CD Setup Guide

This guide explains how to set up and configure the GitHub Actions CI/CD pipeline for the Swag Labs UI automation project.

## Prerequisites

### Repository Setup

1. **Enable GitHub Actions**
   - Go to repository Settings → Actions → General
   - Select "Allow all actions and reusable workflows"

2. **Enable GitHub Pages**
   - Go to repository Settings → Pages
   - Source: "Deploy from a branch"
   - Branch: `gh-pages` / `root`

3. **Branch Protection (Recommended)**
   - Go to repository Settings → Branches
   - Add rule for `main` branch
   - Require status checks to pass before merging
   - Require branches to be up to date before merging

## Workflow Configuration

### 1. Main CI/CD Pipeline

**File:** `.github/workflows/ci-cd.yml`

**Features:**
- Multi-browser testing (Chrome, Firefox)
- Parallel execution with matrix strategy
- Allure report generation
- Artifact upload and management
- GitHub Pages deployment
- Security scanning for pull requests

### 2. Allure Report Workflow

**File:** `.github/workflows/allure-report.yml`

**Features:**
- Triggered after main CI/CD completion
- Maintains report history
- Advanced Allure report generation
- Automatic GitHub Pages deployment

### 3. Scheduled Tests

**File:** `.github/workflows/scheduled-tests.yml`

**Features:**
- Daily automated test execution
- Manual workflow dispatch with parameters
- Configurable browser and test suite selection

## Quick Start

### 1. Push Code to Trigger Workflow

```bash
# Create and push a feature branch
git checkout -b feature/my-new-feature
git add .
git commit -m "Add new feature"
git push origin feature/my-new-feature
```

### 2. Create Pull Request

- Go to GitHub repository
- Click "New Pull Request"
- Select your feature branch
- CI/CD pipeline will automatically run

### 3. View Results

**Workflow Status:**
- Go to Actions tab in GitHub repository
- Click on the running/completed workflow
- View job details and logs

**Allure Reports:**
- Access via: `https://[username].github.io/[repository]/allure-report/`
- Available after main branch deployment

## Workflow Execution Examples

### Feature Branch Development

```bash
# 1. Create feature branch
git checkout -b feature/add-cart-tests

# 2. Make changes and commit
git add .
git commit -m "feat: add cart functionality tests"

# 3. Push to trigger CI
git push origin feature/add-cart-tests
```

**Result:** Tests run on Chrome and Firefox, artifacts uploaded

### Main Branch Deployment

```bash
# 1. Merge PR to main
git checkout main
git pull origin main

# 2. Automatic deployment triggered
# - Tests run on both browsers
# - Allure reports generated
# - Reports deployed to GitHub Pages
```

### Manual Test Execution

1. Go to Actions tab
2. Select "Scheduled Test Execution"
3. Click "Run workflow"
4. Configure parameters:
   - Browser: chrome/firefox/edge
   - Test Suite: all/login/smoke
   - Headless: true/false
5. Click "Run workflow"

## Environment Variables

### Automatic Variables

```yaml
GITHUB_TOKEN: ${{ secrets.GITHUB_TOKEN }}  # Auto-provided
GITHUB_REPOSITORY: owner/repo-name         # Auto-provided
GITHUB_REF: refs/heads/branch-name         # Auto-provided
```

### Custom Variables (Optional)

Add in repository Settings → Secrets and variables → Actions:

```yaml
SLACK_WEBHOOK_URL: https://hooks.slack.com/...  # For notifications
EMAIL_RECIPIENT: team@company.com               # For email alerts
TEST_ENVIRONMENT_URL: https://staging.app.com   # For different environments
```

## Monitoring and Alerts

### Workflow Status Badges

Add to README.md:

```markdown
![CI/CD Pipeline](https://github.com/[username]/[repository]/workflows/CI%2FCD%20Pipeline%20-%20Selenium%20Tests%20with%20Allure%20Reports/badge.svg)
```

### Email Notifications

GitHub automatically sends notifications for:
- Workflow failures on main branch
- First-time workflow failures on any branch
- Workflow success after previous failure

### Slack Integration (Future)

```yaml
- name: Slack Notification
  if: failure()
  uses: 8398a7/action-slack@v3
  with:
    status: ${{ job.status }}
    webhook_url: ${{ secrets.SLACK_WEBHOOK_URL }}
```

## Artifact Management

### Download Artifacts

1. Go to Actions → Select workflow run
2. Scroll to "Artifacts" section
3. Click to download:
   - `allure-results-chrome`
   - `allure-results-firefox`
   - `allure-report-chrome`
   - `allure-report-firefox`
   - `test-screenshots-*` (on failure)

### Artifact Retention

- **Allure Results**: 30 days
- **Allure Reports**: 30 days
- **Screenshots**: 7 days
- **Security Reports**: 30 days

## Performance Optimization

### 1. Maven Dependency Caching

```yaml
- name: Cache Maven dependencies
  uses: actions/cache@v3
  with:
    path: ~/.m2
    key: ${{ runner.os }}-m2-${{ hashFiles('**/pom.xml') }}
```

**Benefits:**
- Faster build times (2-3x improvement)
- Reduced network usage
- More reliable builds

### 2. Parallel Execution

```yaml
strategy:
  matrix:
    browser: [chrome, firefox]
```

**Benefits:**
- Tests run simultaneously
- Faster feedback (50% time reduction)
- Independent failure isolation

### 3. Conditional Job Execution

```yaml
if: github.ref == 'refs/heads/main'  # Main branch only
if: github.event_name == 'pull_request'  # PR only
if: always()  # Always run (even on failure)
```

## Troubleshooting

### Common Issues

1. **Tests Pass Locally but Fail in CI**
   ```yaml
   # Add debug information
   - name: Debug Environment
     run: |
       echo "Java Version: $(java -version)"
       echo "Maven Version: $(mvn -version)"
       echo "Chrome Version: $(google-chrome --version)"
       echo "Display: $DISPLAY"
   ```

2. **Allure Report Not Generated**
   ```yaml
   # Check Allure installation
   - name: Verify Allure
     run: |
       mvn allure:help
       ls -la target/allure-results/
   ```

3. **GitHub Pages Deployment Fails**
   ```yaml
   # Verify permissions
   - name: Check Permissions
     run: |
       echo "Token: ${{ secrets.GITHUB_TOKEN }}"
       echo "Ref: ${{ github.ref }}"
   ```

### Debug Steps

1. **Enable Debug Logging**
   ```yaml
   - name: Run tests with debug
     run: mvn test -X -Dtest=LoginTest
   ```

2. **Upload Debug Artifacts**
   ```yaml
   - name: Upload Debug Info
     if: failure()
     uses: actions/upload-artifact@v4
     with:
       name: debug-info
       path: |
         target/surefire-reports/
         hs_err_pid*.log
   ```

3. **SSH Debug (Emergency)**
   ```yaml
   - name: Setup tmate session
     if: failure()
     uses: mxschmitt/action-tmate@v3
   ```

## Security Considerations

### 1. Dependency Scanning

```yaml
- name: OWASP Dependency Check
  run: mvn org.owasp:dependency-check-maven:check
```

### 2. Secret Management

- Never commit secrets to repository
- Use GitHub Secrets for sensitive data
- Rotate secrets regularly

### 3. Branch Protection

- Require status checks
- Require code review
- Restrict push to main branch

## Cost Optimization

### GitHub Actions Usage

- **Public repositories**: Unlimited minutes
- **Private repositories**: 2,000 minutes/month (free tier)

### Optimization Strategies

1. **Conditional Execution**
   ```yaml
   if: contains(github.event.head_commit.message, '[skip ci]') == false
   ```

2. **Path-based Triggers**
   ```yaml
   on:
     push:
       paths:
         - 'src/**'
         - 'pom.xml'
   ```

3. **Efficient Matrix Strategy**
   ```yaml
   strategy:
     matrix:
       include:
         - browser: chrome
           os: ubuntu-latest
         - browser: firefox
           os: ubuntu-latest
   ```

## Next Steps

1. **Monitor Initial Runs**
   - Watch first few workflow executions
   - Verify Allure reports are generated
   - Check GitHub Pages deployment

2. **Customize for Your Needs**
   - Add environment-specific configurations
   - Implement notification preferences
   - Configure additional security scans

3. **Scale and Optimize**
   - Add more browsers/environments
   - Implement parallel test execution
   - Add performance monitoring

The CI/CD pipeline is now ready to provide continuous integration and deployment with comprehensive Allure reporting for your Selenium automation framework.
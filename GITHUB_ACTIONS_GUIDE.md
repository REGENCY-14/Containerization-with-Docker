# GitHub Actions CI/CD Guide

This guide explains the unified GitHub Actions workflow implemented for the Swag Labs UI automation project with Allure Reports integration.

## Unified Workflow Overview

### Single Workflow File (`selenium-ci-cd.yml`)

**All functionality consolidated into one comprehensive workflow:**

**Triggers:**
- Push to `main`, `dev`, or `feature/**` branches
- Pull requests to `main` or `dev` branches
- Daily scheduled execution at 2 AM UTC
- Manual workflow dispatch with parameters

**Jobs:**
- **test**: Runs Selenium tests with matrix strategy (Chrome & Firefox)
- **scheduled-test**: Handles scheduled and manual test execution
- **security-scan**: Runs OWASP dependency check (PR only)
- **deploy-allure-report**: Deploys Allure reports to GitHub Pages (main branch only)
- **generate-allure-history**: Creates advanced Allure reports with historical data

## Workflow Details

### Unified Trigger Configuration

```yaml
on:
  push:
    branches: [ main, dev, feature/** ]
  pull_request:
    branches: [ main, dev ]
  schedule:
    - cron: '0 2 * * *'
  workflow_dispatch:
    inputs:
      browser: [chrome, firefox, edge]
      test_suite: [all, login, smoke]
      headless: [true, false]
```

#### Test Job Matrix Strategy

```yaml
strategy:
  matrix:
    browser: [chrome, firefox]
```

**Benefits:**
- Parallel execution across multiple browsers
- Faster feedback on cross-browser compatibility
- Independent artifact generation per browser

#### Key Steps:

1. **Environment Setup**
   - Checkout repository
   - Set up JDK 17 (Temurin distribution)
   - Cache Maven dependencies for faster builds

2. **Browser Installation**
   - Install Chrome and Firefox browsers
   - Set up display for headless execution

3. **Test Execution**
   ```bash
   mvn clean test -Dbrowser=${{ matrix.browser }} -Dheadless=true
   ```

4. **Allure Report Generation**
   ```bash
   mvn allure:report
   ```

5. **Artifact Upload**
   - Allure results (30-day retention)
   - Allure reports (30-day retention)
   - Screenshots on failure (7-day retention)

### Allure Report Deployment

#### Deployment Job (Main Branch Only)

```yaml
if: github.ref == 'refs/heads/main' && github.event_name == 'push'
```

**Process:**
1. Download artifacts from both browser test runs
2. Merge Allure results from Chrome and Firefox
3. Generate combined Allure report
4. Deploy to GitHub Pages

#### GitHub Pages Configuration

```yaml
- name: Deploy to GitHub Pages
  uses: peaceiris/actions-gh-pages@v3
  with:
    github_token: ${{ secrets.GITHUB_TOKEN }}
    publish_dir: target/allure-report
    destination_dir: allure-report
```

**Access URL:** `https://[username].github.io/[repository]/allure-report/`

### Security Scanning

#### OWASP Dependency Check (PR Only)

```yaml
if: github.event_name == 'pull_request'
```

**Purpose:**
- Scan dependencies for known vulnerabilities
- Generate security reports
- Prevent vulnerable dependencies from merging

## Allure Integration in CI/CD

### 1. Test Execution with Allure

The workflow automatically:
- Runs tests with Allure listener enabled
- Generates JSON result files in `target/allure-results/`
- Captures test metadata, steps, and attachments

### 2. Report Generation

```bash
mvn allure:report
```

**Generated Content:**
- Interactive HTML reports
- Test execution trends
- Environment information
- Test categorization and filtering

### 3. Artifact Management

**Allure Results:**
- Raw JSON data from test execution
- Uploaded per browser matrix
- Used for report generation

**Allure Reports:**
- Generated HTML reports
- Combined from multiple browser runs
- Deployed to GitHub Pages

### 4. Historical Data

The separate Allure workflow maintains:
- Test execution history
- Trend analysis over time
- Performance metrics
- Flaky test identification

## Environment Variables and Secrets

### Required Secrets

- `GITHUB_TOKEN`: Automatically provided by GitHub Actions
- Used for GitHub Pages deployment

### Environment Variables

```yaml
env:
  DISPLAY: :99  # For headless browser execution
```

### Maven Properties

```yaml
-Dbrowser=${{ matrix.browser }}  # chrome, firefox
-Dheadless=true                  # Headless execution
```

## Workflow Execution Flow

### 1. Code Push/PR Creation

```
Push to main/dev/feature → Trigger CI/CD Pipeline
├── Test Job (Chrome) → Generate Allure Results
├── Test Job (Firefox) → Generate Allure Results
└── Upload Artifacts
```

### 2. Main Branch Deployment

```
Main Branch Push → CI/CD Success → Deploy Job
├── Download All Artifacts
├── Merge Allure Results
├── Generate Combined Report
└── Deploy to GitHub Pages
```

### 3. Allure Report Workflow

```
CI/CD Completion → Allure Workflow Trigger
├── Download Artifacts
├── Generate Report with History
└── Deploy to GitHub Pages
```

## Monitoring and Notifications

### Workflow Status

- **Green Check**: All tests passed
- **Red X**: Test failures or workflow errors
- **Yellow Circle**: Workflow in progress

### Artifact Access

1. Go to Actions tab in GitHub repository
2. Click on specific workflow run
3. Scroll to "Artifacts" section
4. Download desired artifacts

### GitHub Pages Report

- Automatic deployment on main branch success
- Accessible via repository settings → Pages
- URL: `https://[username].github.io/[repository]/allure-report/`

## Best Practices

### 1. Branch Strategy

- **Feature branches**: Run tests on push
- **Main/Dev branches**: Full pipeline with deployment
- **Pull requests**: Include security scanning

### 2. Test Organization

- Use matrix strategy for cross-browser testing
- Separate critical tests (login) for faster feedback
- Implement proper test categorization

### 3. Artifact Management

- Different retention periods based on importance
- Browser-specific artifacts for debugging
- Screenshots only on failures to save space

### 4. Performance Optimization

- Maven dependency caching
- Parallel test execution
- Conditional job execution

## Troubleshooting

### Common Issues

1. **Test Failures in CI but Pass Locally**
   - Check browser versions
   - Verify headless mode compatibility
   - Review timing issues

2. **Allure Report Not Generated**
   - Verify Maven Allure plugin configuration
   - Check if tests are generating results
   - Ensure proper artifact upload/download

3. **GitHub Pages Deployment Fails**
   - Verify repository settings
   - Check GitHub token permissions
   - Ensure proper branch protection rules

### Debug Steps

1. **Check Workflow Logs**
   ```
   Actions → Select Workflow Run → View Job Logs
   ```

2. **Download Artifacts**
   ```
   Actions → Workflow Run → Artifacts Section
   ```

3. **Local Reproduction**
   ```bash
   mvn clean test -Dbrowser=chrome -Dheadless=true
   mvn allure:report
   ```

## Customization Options

### 1. Add New Browsers

```yaml
strategy:
  matrix:
    browser: [chrome, firefox, edge, safari]
```

### 2. Environment-Specific Tests

```yaml
strategy:
  matrix:
    environment: [staging, production]
    browser: [chrome, firefox]
```

### 3. Parallel Test Execution

```yaml
strategy:
  matrix:
    test-group: [login, cart, checkout, products]
```

### 4. Custom Test Categories

```bash
mvn test -Dgroups=smoke,regression -Dbrowser=chrome
```

## Integration with External Tools

### 1. Slack Notifications (Future)

```yaml
- name: Notify Slack
  if: failure()
  uses: 8398a7/action-slack@v3
```

### 2. Email Notifications (Future)

```yaml
- name: Send Email
  if: always()
  uses: dawidd6/action-send-mail@v3
```

### 3. Test Management Integration

```yaml
- name: Update Test Management
  run: |
    # Custom script to update test results
```

## Performance Metrics

### Typical Execution Times

- **Single Browser Test**: 2-3 minutes
- **Matrix Strategy (2 browsers)**: 3-4 minutes (parallel)
- **Allure Report Generation**: 30-60 seconds
- **GitHub Pages Deployment**: 1-2 minutes

### Resource Usage

- **CPU**: 2 cores per job
- **Memory**: 7GB available
- **Storage**: 14GB SSD
- **Network**: High-speed GitHub infrastructure

The GitHub Actions integration provides comprehensive CI/CD capabilities with robust Allure reporting, cross-browser testing, and automated deployment to GitHub Pages.
# Allure Reports Integration Guide

This guide provides comprehensive instructions for generating and viewing Allure reports for the Swag Labs UI automation tests.

## Prerequisites

- Java 11 or higher
- Maven 3.6 or higher
- Allure CLI (optional, for local report viewing)

## Quick Start

### 1. Run Tests and Generate Allure Results

```bash
# Run all tests and generate Allure results
mvn clean test

# Run specific test class
mvn test -Dtest=LoginTest

# Run tests in headless mode
mvn test -Dheadless=true
```

After running tests, Allure results will be generated in `target/allure-results/`

### 2. Generate and View Allure Report

#### Option A: Using Maven Plugin (Recommended)

```bash
# Generate Allure report
mvn allure:report

# Serve report locally (opens in browser)
mvn allure:serve
```

#### Option B: Using Allure CLI (if installed)

```bash
# Install Allure CLI (one-time setup)
# Windows (using Scoop)
scoop install allure

# macOS (using Homebrew)
brew install allure

# Generate and serve report
allure serve target/allure-results
```

## Detailed Commands

### Test Execution with Allure

```bash
# Basic test execution
mvn clean test

# Run with specific browser
mvn test -Dbrowser=firefox
mvn test -Dbrowser=edge
mvn test -Dbrowser=chrome

# Run in headless mode
mvn test -Dheadless=true

# Run specific test categories
mvn test -Dtest=LoginTest
mvn test -Dtest=*Test
mvn test -Dgroups=smoke

# Combine options
mvn test -Dtest=LoginTest -Dheadless=true -Dbrowser=chrome
```

### Report Generation

```bash
# Generate static HTML report
mvn allure:report

# Generate and serve interactive report
mvn allure:serve

# Clean previous results and generate new report
mvn clean test allure:report

# Generate report with custom configuration
mvn allure:report -Dallure.results.directory=target/allure-results
```

## Report Features

### 1. Test Overview
- **Dashboard**: Summary of test execution results
- **Categories**: Test failures grouped by categories
- **Suites**: Tests organized by test classes
- **Graphs**: Visual representation of test results

### 2. Test Details
- **Test Steps**: Detailed step-by-step execution
- **Attachments**: Screenshots, logs, and other files
- **Parameters**: Test input parameters and configuration
- **Timing**: Execution duration and performance metrics

### 3. Environment Information
- **Browser Configuration**: Browser type, version, headless mode
- **System Information**: OS, Java version, Maven version
- **Test Framework**: Selenium, JUnit versions
- **Application Details**: URL, version, environment

### 4. Trends and History
- **Execution Trends**: Test results over time
- **Duration Trends**: Performance trends
- **Retry Analysis**: Flaky test identification

## Allure Annotations Used

### Class Level Annotations
```java
@Epic("User Authentication")          // High-level feature grouping
@Feature("Login Functionality")       // Feature being tested
@Owner("QA Team")                    // Test owner
```

### Method Level Annotations
```java
@Story("Valid Login")                // User story
@Severity(SeverityLevel.CRITICAL)    // Test importance
@Description("Test description")      // Detailed description
@Link(name = "Test Case", url = "...") // External links
@Issue("BUG-123")                    // Issue tracking
@TmsLink("TC-001")                   // Test management system
```

### Step Annotations
```java
@Step("Navigate to login page")      // Test steps
@Attachment("Screenshot")            // File attachments
```

## Directory Structure

```
target/
├── allure-results/          # Raw test execution data
│   ├── *.json              # Test results in JSON format
│   ├── *.txt               # Test attachments
│   └── environment.properties
├── allure-report/           # Generated HTML report
│   ├── index.html          # Main report page
│   ├── data/               # Report data files
│   └── plugins/            # Report plugins
└── surefire-reports/        # Standard Maven test reports
```

## Configuration Files

### 1. allure.properties
```properties
# Located in src/test/resources/
allure.results.directory=target/allure-results
allure.report.name=Swag Labs UI Automation Test Report
```

### 2. environment.properties
```properties
# Located in src/test/resources/
Application.Name=Swag Labs
Test.Framework=Selenium WebDriver + JUnit 5
Browser.Default=Chrome
```

## Best Practices

### 1. Test Organization
- Use `@Epic` for major application areas
- Use `@Feature` for specific functionalities
- Use `@Story` for user stories or scenarios
- Use `@Severity` to prioritize test failures

### 2. Step Documentation
- Break down tests into logical steps using `@Step`
- Add meaningful descriptions to each step
- Include relevant parameters in step names

### 3. Attachments
- Add screenshots for visual verification
- Attach logs for debugging failures
- Include test data for reproducibility

### 4. Environment Information
- Document browser configuration
- Include application version and environment
- Add relevant system information

## Troubleshooting

### Common Issues

1. **No Allure results generated**
   ```bash
   # Ensure tests are running
   mvn clean test -X
   
   # Check target/allure-results directory exists
   ls -la target/allure-results
   ```

2. **Report generation fails**
   ```bash
   # Clean and regenerate
   mvn clean
   mvn test
   mvn allure:report
   ```

3. **Missing test steps**
   ```bash
   # Verify AspectJ weaver is working
   mvn test -X | grep aspectj
   ```

4. **Browser issues in headless mode**
   ```bash
   # Run with visible browser for debugging
   mvn test -Dheadless=false
   ```

## Integration with CI/CD

### Local Development
```bash
# Quick feedback loop
mvn test -Dtest=LoginTest -Dheadless=true
mvn allure:serve
```

### Continuous Integration
```bash
# Generate reports for CI
mvn clean test allure:report
# Publish target/allure-report/ directory
```

## Report Customization

### Custom Categories
Create `categories.json` in `src/test/resources/`:
```json
[
  {
    "name": "Login Issues",
    "matchedStatuses": ["failed"],
    "messageRegex": ".*login.*"
  }
]
```

### Custom Logo and Styling
- Add custom CSS in `src/test/resources/allure/`
- Configure logo in allure.properties

## Performance Considerations

- **Results Size**: Clean old results regularly
- **Report Generation**: Use `allure:serve` for development
- **CI/CD**: Generate static reports for publishing
- **Storage**: Archive reports for historical analysis

## Next Steps

After successful local integration:
1. Configure CI/CD pipeline integration
2. Set up report publishing to web server
3. Integrate with test management systems
4. Configure automated report distribution

The Allure integration provides comprehensive test reporting with rich visualizations, detailed test execution information, and powerful analysis capabilities for your Selenium automation framework.
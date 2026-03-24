# Test Execution Guide

This guide provides comprehensive instructions for running the Swag Labs UI automation tests locally using Maven.

## Prerequisites

- Java 11 or higher
- Maven 3.6 or higher
- Internet connection (for WebDriverManager to download browser drivers)

## Quick Start

```bash
# Clone the repository
git clone <repository-url>
cd swag-labs-automation

# Compile the project
mvn clean compile

# Run all tests
mvn test
```

## Test Execution Commands

### Basic Test Execution

```bash
# Run all tests (default: Chrome, non-headless)
mvn test

# Run tests in headless mode (faster, no browser window)
mvn test -Dheadless=true

# Run with specific browser
mvn test -Dbrowser=firefox
mvn test -Dbrowser=edge
mvn test -Dbrowser=chrome
```

### Using Maven Profiles

```bash
# Run tests in headless mode using profile
mvn test -Pheadless

# Run tests with Firefox using profile
mvn test -Pfirefox

# Run tests with Edge using profile
mvn test -Pedge

# Run only login tests using profile
mvn test -Plogin-only

# Combine profiles
mvn test -Pheadless,firefox
```

### Running Specific Tests

```bash
# Run specific test class
mvn test -Dtest=LoginTest
mvn test -Dtest=AddToCartTest
mvn test -Dtest=ViewCartTest
mvn test -Dtest=CheckoutTest

# Run specific test method
mvn test -Dtest=LoginTest#testSuccessfulLogin
mvn test -Dtest=LoginTest#testLoginWithInvalidCredentials

# Run multiple test classes
mvn test -Dtest=LoginTest,AddToCartTest

# Run test suite
mvn test -Dtest=SwagLabsTestSuite
```

### Advanced Test Execution

```bash
# Run tests with custom system properties
mvn test -Dbrowser=chrome -Dheadless=true -Dtest=LoginTest

# Run tests with Maven profiles and custom properties
mvn test -Pheadless -Dbrowser=firefox -Dtest=LoginTest

# Run tests with verbose output
mvn test -X

# Run tests and skip compilation if already compiled
mvn surefire:test

# Clean, compile, and test in one command
mvn clean test
```

## Test Categories

### 1. Login Tests (✅ Fully Working)
```bash
# Run all login tests
mvn test -Dtest=LoginTest

# Run specific login scenarios
mvn test -Dtest=LoginTest#testSuccessfulLogin
mvn test -Dtest=LoginTest#testLoginWithInvalidCredentials
mvn test -Dtest=LoginTest#testLoginWithLockedUser
mvn test -Dtest=LoginTest#testLoginWithEmptyUsername
mvn test -Dtest=LoginTest#testLoginWithEmptyPassword
```

### 2. Add to Cart Tests (🔄 Framework Ready)
```bash
# Run add to cart tests
mvn test -Dtest=AddToCartTest

# Note: Some tests may need refinement for full stability
```

### 3. View Cart Tests (🔄 Framework Ready)
```bash
# Run cart viewing tests
mvn test -Dtest=ViewCartTest

# Note: Framework is complete but may need stability improvements
```

### 4. Checkout Tests (🔄 Framework Ready)
```bash
# Run checkout process tests
mvn test -Dtest=CheckoutTest

# Note: Comprehensive framework ready for refinement
```

## Test Reports

After running tests, reports are generated in:
- `target/surefire-reports/` - XML and text reports
- Console output with test results and timing

### Viewing Test Reports

```bash
# View test results summary
cat target/surefire-reports/TEST-*.xml

# View detailed test output
cat target/surefire-reports/*.txt
```

## Troubleshooting

### Common Issues and Solutions

1. **Browser Driver Issues**
   ```bash
   # WebDriverManager handles drivers automatically
   # If issues persist, clear Maven cache
   mvn dependency:purge-local-repository
   ```

2. **Test Timeouts**
   ```bash
   # Run in headless mode for better performance
   mvn test -Dheadless=true
   ```

3. **Compilation Issues**
   ```bash
   # Clean and recompile
   mvn clean compile
   ```

4. **Memory Issues**
   ```bash
   # Increase Maven memory
   export MAVEN_OPTS="-Xmx1024m -XX:MaxPermSize=256m"
   mvn test
   ```

### Debug Mode

```bash
# Run with debug information
mvn test -X -Dtest=LoginTest

# Run single test with maximum logging
mvn test -X -Dtest=LoginTest#testSuccessfulLogin
```

## Performance Optimization

### Faster Test Execution

```bash
# Use headless mode (recommended for CI/CD)
mvn test -Dheadless=true

# Run only stable tests
mvn test -Dtest=LoginTest -Dheadless=true

# Skip compilation if not needed
mvn surefire:test -Dheadless=true
```

### Parallel Execution (Future Enhancement)

```bash
# Currently disabled, can be enabled in pom.xml
# mvn test -Dparallel.tests=true
```

## Best Practices

1. **Always run tests in headless mode for CI/CD**
   ```bash
   mvn test -Dheadless=true
   ```

2. **Use specific test execution for development**
   ```bash
   mvn test -Dtest=LoginTest#testSuccessfulLogin -Dheadless=true
   ```

3. **Clean before important test runs**
   ```bash
   mvn clean test -Dheadless=true
   ```

4. **Use profiles for consistent execution**
   ```bash
   mvn test -Pheadless,login-only
   ```

## Integration with IDEs

### IntelliJ IDEA
- Right-click on test class/method → Run
- Use Run Configurations with VM options: `-Dheadless=true`

### Eclipse
- Right-click on test class/method → Run As → JUnit Test
- Configure Run Configuration with system properties

### VS Code
- Use Java Test Runner extension
- Configure launch.json with system properties

## Next Steps

After verifying local execution:
1. All login tests should pass consistently
2. Framework structure is ready for Docker containerization
3. CI/CD pipeline can be configured using the same Maven commands
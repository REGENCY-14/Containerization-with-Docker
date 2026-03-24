# Swag Labs UI Automation Framework

A comprehensive Java-based UI automation framework for testing Swag Labs using Selenium WebDriver, JUnit 5, and Page Object Model (POM) design pattern with enhanced WebDriver management and comprehensive test coverage.

## Tech Stack

- **Language**: Java 11
- **Build Tool**: Maven
- **Test Framework**: JUnit 5
- **Automation Tool**: Selenium WebDriver
- **Framework Design**: Page Object Model (POM)
- **Driver Management**: WebDriverManager with custom DriverFactory

## Project Structure

```
src/
├── main/java/com/swaglabs/
│   ├── base/
│   │   └── BasePage.java          # Enhanced base page with wait utilities
│   ├── config/
│   │   └── BrowserConfig.java     # Browser configuration management
│   ├── driver/
│   │   ├── DriverFactory.java     # WebDriver factory for multiple browsers
│   │   └── DriverManager.java     # Thread-safe WebDriver management
│   ├── pages/
│   │   ├── LoginPage.java         # Login page object
│   │   ├── ProductsPage.java      # Products/inventory page object
│   │   ├── CartPage.java          # Shopping cart page object
│   │   ├── CheckoutPage.java      # Checkout information page object
│   │   ├── CheckoutOverviewPage.java # Order review page object
│   │   └── CheckoutCompletePage.java # Order confirmation page object
│   └── utils/
│       ├── TestData.java          # Comprehensive test data constants
│       └── WaitUtils.java         # Comprehensive wait utilities
└── test/java/com/swaglabs/
    ├── base/
    │   └── BaseTest.java          # Enhanced base test with configuration
    ├── tests/
    │   ├── LoginTest.java         # ✅ Login functionality tests (STABLE)
    │   ├── AddToCartTest.java     # 🔄 Add to cart functionality tests
    │   ├── ViewCartTest.java      # 🔄 Cart viewing and management tests
    │   ├── CheckoutTest.java      # 🔄 Complete checkout process tests
    │   └── SwagLabsTestSuite.java # Test suite runner
    └── utils/
        └── TestUtils.java         # Test execution utilities and logging
```

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

# Run stable login tests (recommended)
mvn test -Dtest=LoginTest -Dheadless=true
```

## Local Test Execution

### Using Maven Commands

```bash
# Run all tests (default: Chrome, non-headless)
mvn test

# Run tests in headless mode (faster, recommended)
mvn test -Dheadless=true

# Run with specific browser
mvn test -Dbrowser=firefox -Dheadless=true
mvn test -Dbrowser=edge -Dheadless=true
mvn test -Dbrowser=chrome -Dheadless=true

# Run specific test class (recommended for development)
mvn test -Dtest=LoginTest -Dheadless=true

# Run specific test method
mvn test -Dtest=LoginTest#testSuccessfulLogin -Dheadless=true
```

### Using Test Runner Scripts

#### Windows (run-tests.bat)
```cmd
# Run stable login tests
run-tests.bat login --headless

# Run all tests with Firefox
run-tests.bat all --firefox --headless

# Clean and compile
run-tests.bat clean

# Show help
run-tests.bat help
```

#### Linux/Mac (run-tests.sh)
```bash
# Make script executable (Linux/Mac only)
chmod +x run-tests.sh

# Run stable login tests
./run-tests.sh login --headless

# Run all tests with Firefox
./run-tests.sh all --firefox --headless

# Clean and compile
./run-tests.sh clean

# Show help
./run-tests.sh help
```

### Using Maven Profiles

```bash
# Run tests in headless mode using profile
mvn test -Pheadless

# Run tests with Firefox using profile
mvn test -Pfirefox

# Run only login tests using profile
mvn test -Plogin-only

# Combine profiles
mvn test -Pheadless,firefox
```

## Test Coverage & Status

### ✅ LoginTest.java (FULLY STABLE)
- **Status**: Production ready, all tests passing consistently
- **Coverage**: 6 test scenarios covering all login functionality
- **Execution**: ~30 seconds in headless mode
- **Recommended for**: CI/CD, development testing, demo purposes

```bash
# Run stable login tests
mvn test -Dtest=LoginTest -Dheadless=true
```

**Test Scenarios:**
- ✅ Successful login with valid credentials
- ✅ Failed login with invalid credentials  
- ✅ Failed login with locked user account
- ✅ Failed login with empty username
- ✅ Failed login with empty password
- ✅ Login page elements validation

### 🔄 Other Test Classes (FRAMEWORK READY)
- **AddToCartTest.java**: Product selection and cart operations
- **ViewCartTest.java**: Cart display and management  
- **CheckoutTest.java**: Complete checkout process
- **Status**: Framework complete, may need stability refinements

## Framework Features

### Enhanced WebDriver Management
- **Multi-browser Support**: Chrome, Firefox, Edge with easy switching
- **Thread-safe Design**: Supports parallel test execution
- **Automatic Driver Management**: WebDriverManager handles browser drivers
- **Configurable Options**: Headless mode, custom timeouts, window management

### Advanced Wait Handling
- **Explicit Waits**: Comprehensive wait utilities for different conditions
- **Smart Timeouts**: Configurable implicit and explicit wait times
- **Element State Checking**: Wait for visibility, clickability, presence
- **Page State Verification**: Title and URL-based waits

### Comprehensive Page Object Model
- **Complete Coverage**: All major Swag Labs pages implemented
- **Enhanced Base Classes**: Better error handling and wait integration
- **Flexible Constructors**: Support for both direct WebDriver and DriverManager
- **Robust Element Interactions**: Built-in waits for all element operations

### JUnit 5 Test Implementation
- **Structured Test Classes**: Separate classes for different functionalities
- **Comprehensive Assertions**: Proper validation of expected vs actual results
- **Enhanced Logging**: TestUtils for detailed test execution tracking
- **Test Ordering**: Logical test execution order with @Order annotations
- **Test Suite Support**: Organized test execution with proper reporting

## Test Execution Best Practices

### For Development
```bash
# Quick feedback loop - run stable tests only
mvn test -Dtest=LoginTest -Dheadless=true

# Test specific functionality
mvn test -Dtest=LoginTest#testSuccessfulLogin -Dheadless=true
```

### For CI/CD
```bash
# Stable tests for pipeline
mvn test -Dtest=LoginTest -Dheadless=true

# Full test suite (when all tests are stable)
mvn test -Dheadless=true
```

### For Demo/Presentation
```bash
# Visual tests (non-headless)
mvn test -Dtest=LoginTest

# With detailed logging
mvn test -Dtest=LoginTest -X
```

## Test Reports

After running tests, reports are generated in:
- `target/surefire-reports/` - XML and text reports
- Console output with enhanced logging via TestUtils

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
   # Increase Maven memory (Linux/Mac)
   export MAVEN_OPTS="-Xmx1024m -XX:MaxPermSize=256m"
   mvn test
   
   # Windows
   set MAVEN_OPTS=-Xmx1024m -XX:MaxPermSize=256m
   mvn test
   ```

## Dependencies

- Selenium WebDriver 4.15.0
- JUnit 5.10.0
- WebDriverManager 5.6.2
- JUnit Platform Suite API 1.10.0

## Framework Readiness Assessment

### ✅ Ready for Production
- **LoginTest**: Fully stable, comprehensive coverage
- **Page Object Model**: Complete implementation for all pages
- **WebDriver Management**: Robust, multi-browser support
- **Test Infrastructure**: Enhanced logging, utilities, proper structure

### 🔄 Ready for Enhancement
- **Cart Tests**: Framework complete, needs stability refinement
- **Checkout Tests**: Comprehensive coverage, needs validation
- **Parallel Execution**: Infrastructure ready, needs configuration
- **Reporting**: Basic reports working, can be enhanced

### 🚀 Ready for Next Phase
The framework is well-structured and ready for:
- Docker containerization
- CI/CD pipeline integration
- Enhanced reporting and monitoring
- Parallel test execution
- Cross-browser testing at scale

## Current Status: Framework Cleanup and Local Execution - COMPLETE

This phase includes:
- ✅ Enhanced Maven configuration with profiles
- ✅ Comprehensive test execution guide
- ✅ Test runner scripts for Windows and Linux/Mac
- ✅ Improved LoginTest with enhanced logging
- ✅ TestUtils for better test management
- ✅ Framework readiness assessment
- ✅ Local execution optimization
- ✅ Stability improvements and cleanup

The framework is now clean, well-documented, and ready for Docker containerization!
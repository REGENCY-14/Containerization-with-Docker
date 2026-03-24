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
    └── tests/
        ├── LoginTest.java         # Login functionality tests
        ├── AddToCartTest.java     # Add to cart functionality tests
        ├── ViewCartTest.java      # Cart viewing and management tests
        ├── CheckoutTest.java      # Complete checkout process tests
        └── SwagLabsTestSuite.java # Test suite runner
```

## Prerequisites

- Java 11 or higher
- Maven 3.6 or higher

## Setup Instructions

1. Clone the repository
2. Navigate to project directory
3. Run `mvn clean compile` to build the project
4. Run `mvn test` to execute tests

## Browser Configuration

The framework supports multiple browsers and configuration options:

### Supported Browsers
- Chrome (default)
- Firefox
- Edge

### Configuration Options
- **Browser Selection**: `-Dbrowser=chrome|firefox|edge`
- **Headless Mode**: `-Dheadless=true|false`

### Example Commands
```bash
# Run tests with Chrome (default)
mvn test

# Run tests with Firefox
mvn test -Dbrowser=firefox

# Run tests in headless mode
mvn test -Dheadless=true

# Run specific test class
mvn test -Dtest=LoginTest

# Run specific test method
mvn test -Dtest=LoginTest#testSuccessfulLogin
```

## Test Coverage

### LoginTest.java
- ✅ Successful login with valid credentials
- ✅ Failed login with invalid credentials
- ✅ Failed login with locked user
- ✅ Failed login with empty username
- ✅ Failed login with empty password
- ✅ Login page elements validation

### AddToCartTest.java
- 🔄 Add single product to cart
- 🔄 Add multiple products to cart
- 🔄 Remove products from cart
- 🔄 Product information display
- 🔄 Product sorting functionality

### ViewCartTest.java
- 🔄 Empty cart display
- 🔄 Cart with single/multiple items
- 🔄 Remove items from cart
- 🔄 Cart total calculations
- 🔄 Continue shopping navigation

### CheckoutTest.java
- 🔄 Complete checkout process
- 🔄 Checkout information validation
- 🔄 Price calculations verification
- 🔄 Order completion confirmation

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
- **BeforeEach Setup**: Consistent test initialization
- **DisplayName Annotations**: Clear test descriptions
- **Test Suite Support**: Organized test execution

## Dependencies

- Selenium WebDriver 4.15.0
- JUnit 5.10.0
- WebDriverManager 5.6.2
- JUnit Platform Suite API 1.10.0

## Current Status: Test Case Implementation Complete

This phase includes:
- ✅ Comprehensive LoginTest with all scenarios
- 🔄 AddToCartTest framework (needs refinement)
- 🔄 ViewCartTest framework (needs refinement)  
- 🔄 CheckoutTest framework (needs refinement)
- ✅ JUnit 5 annotations and assertions
- ✅ Proper test class separation
- ✅ Page Object Model integration
- ✅ Test suite organization

## Running Tests

```bash
# Run all tests
mvn test

# Run tests in headless mode (faster)
mvn test -Dheadless=true

# Run only login tests (fully working)
mvn test -Dtest=LoginTest -Dheadless=true

# Run specific test
mvn test -Dtest=LoginTest#testSuccessfulLogin -Dheadless=true
```

The framework provides a solid foundation for UI automation testing with proper separation of concerns, comprehensive page coverage, and robust test implementation patterns.
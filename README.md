# Swag Labs UI Automation Framework

A Java-based UI automation framework for testing Swag Labs using Selenium WebDriver, JUnit 5, and Page Object Model (POM) design pattern with enhanced WebDriver management.

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
│   │   └── LoginPage.java         # Enhanced login page object
│   └── utils/
│       ├── TestData.java          # Test data constants
│       └── WaitUtils.java         # Comprehensive wait utilities
└── test/java/com/swaglabs/
    ├── base/
    │   └── BaseTest.java          # Enhanced base test with configuration
    └── tests/
        ├── LoginTest.java         # Enhanced login tests
        └── WebDriverConfigTest.java # WebDriver configuration tests
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

# Run tests with Firefox in headless mode
mvn test -Dbrowser=firefox -Dheadless=true
```

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

### Improved Page Object Model
- **Enhanced Base Classes**: Better error handling and wait integration
- **Flexible Constructors**: Support for both direct WebDriver and DriverManager
- **Robust Element Interactions**: Built-in waits for all element operations
- **Page Load Verification**: Automatic page state validation

## Dependencies

- Selenium WebDriver 4.15.0
- JUnit 5.10.0
- WebDriverManager 5.6.2

## Current Phase: Enhanced WebDriver Base Setup

This phase includes:
- Multi-browser WebDriver factory
- Thread-safe driver management
- Comprehensive wait utilities
- Enhanced base classes with improved error handling
- Configuration management for different test environments
- Demonstration tests for WebDriver capabilities
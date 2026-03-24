# Swag Labs UI Automation Framework

A Java-based UI automation framework for testing Swag Labs using Selenium WebDriver, JUnit 5, and Page Object Model (POM) design pattern.

## Tech Stack

- **Language**: Java 11
- **Build Tool**: Maven
- **Test Framework**: JUnit 5
- **Automation Tool**: Selenium WebDriver
- **Framework Design**: Page Object Model (POM)
- **Driver Management**: WebDriverManager

## Project Structure

```
src/
├── main/java/com/swaglabs/
│   ├── base/
│   │   ├── BaseTest.java      # Base test class with setup/teardown
│   │   └── BasePage.java      # Base page class with common methods
│   ├── pages/
│   │   └── LoginPage.java     # Page objects for UI elements
│   └── utils/
│       └── TestData.java      # Test data constants
└── test/java/com/swaglabs/
    └── tests/
        └── LoginTest.java     # Test classes
```

## Prerequisites

- Java 11 or higher
- Maven 3.6 or higher

## Setup Instructions

1. Clone the repository
2. Navigate to project directory
3. Run `mvn clean compile` to build the project
4. Run `mvn test` to execute tests

## Dependencies

- Selenium WebDriver 4.15.0
- JUnit 5.10.0
- WebDriverManager 5.6.2

## Framework Features

- **Page Object Model**: Clean separation of page elements and test logic
- **Base Classes**: Reusable setup and common functionality
- **Automatic Driver Management**: WebDriverManager handles browser drivers
- **JUnit 5**: Modern testing framework with annotations and assertions
- **Maven Build**: Standardized build and dependency management

## Current Phase: Project Setup

This phase includes:
- Maven project structure
- Base classes for tests and pages
- Sample page object (LoginPage)
- Basic test structure
- Essential dependencies and configuration
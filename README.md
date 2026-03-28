# Swag Labs UI Automation Framework

A production-ready Selenium WebDriver test automation framework for [Swag Labs (saucedemo.com)](https://www.saucedemo.com), built with Java, JUnit 5, Page Object Model, Allure Reports, Docker, and GitHub Actions CI/CD.

---

## Table of Contents

1. [Project Overview](#project-overview)
2. [Technology Stack](#technology-stack)
3. [Project Structure](#project-structure)
4. [Phase 1 — Maven Project Setup](#phase-1--maven-project-setup)
5. [Phase 2 — WebDriver Base Setup](#phase-2--webdriver-base-setup)
6. [Phase 3 — Page Object Model](#phase-3--page-object-model)
7. [Phase 4 — Test Case Implementation](#phase-4--test-case-implementation)
8. [Phase 5 — Allure Reports Integration](#phase-5--allure-reports-integration)
9. [Phase 6 — GitHub Actions CI/CD](#phase-6--github-actions-cicd)
10. [Phase 7 — Slack Notifications](#phase-7--slack-notifications)
11. [Phase 8 — Docker Containerization](#phase-8--docker-containerization)
12. [Running the Project](#running-the-project)
13. [Configuration Reference](#configuration-reference)
14. [Secrets and Environment Variables](#secrets-and-environment-variables)

---

## Project Overview

This framework automates UI testing of the Swag Labs e-commerce demo application. It covers the full user journey from login through product selection, cart management, and checkout. The framework is designed to run both locally and inside a Docker container, with full CI/CD integration via GitHub Actions, Allure reporting, and Slack notifications.

**Application under test:** https://www.saucedemo.com

**Test coverage:**
- Login (valid credentials, invalid credentials, locked user, empty fields)
- Add to cart (single product, multiple products, remove from cart)
- View cart (cart contents, item count)
- Checkout (form validation, order completion)

---

## Technology Stack

| Tool | Version | Purpose |
|---|---|---|
| Java | 17 (Temurin) | Language |
| Maven | 3.9.6 | Build and dependency management |
| Selenium WebDriver | 4.15.0 | Browser automation |
| JUnit 5 | 5.10.0 | Test framework |
| WebDriverManager | 5.6.2 | Automatic ChromeDriver management |
| Allure | 2.24.0 | Test reporting |
| AspectJ | 1.9.20.1 | Allure annotation processing |
| Docker | latest | Test containerization |
| GitHub Actions | — | CI/CD pipeline |
| Slack GitHub Action | v1.27.0 | Notifications |

---

## Project Structure

```
ui-automation/
├── src/
│   ├── main/java/com/swaglabs/
│   │   ├── base/
│   │   │   └── BasePage.java              # Shared WebDriver utilities for all pages
│   │   ├── config/
│   │   │   └── BrowserConfig.java         # Browser type and headless mode config
│   │   ├── driver/
│   │   │   ├── DriverFactory.java         # Creates WebDriver instances per browser
│   │   │   └── DriverManager.java         # Thread-safe WebDriver lifecycle management
│   │   ├── pages/
│   │   │   ├── LoginPage.java             # Login page actions and locators
│   │   │   ├── ProductsPage.java          # Products/inventory page
│   │   │   ├── CartPage.java              # Shopping cart page
│   │   │   ├── CheckoutPage.java          # Checkout form page
│   │   │   ├── CheckoutOverviewPage.java  # Order summary page
│   │   │   └── CheckoutCompletePage.java  # Order confirmation page
│   │   └── utils/
│   │       ├── TestData.java              # All test data constants
│   │       ├── TestUtils.java             # Logging and test step utilities
│   │       ├── WaitUtils.java             # Explicit wait helpers
│   │       └── AllureUtils.java           # Allure attachment helpers
│   └── test/java/com/swaglabs/
│       ├── base/
│       │   └── BaseTest.java              # JUnit lifecycle, driver setup/teardown
│       └── tests/
│           ├── LoginTest.java             # Login test scenarios
│           ├── AddToCartTest.java         # Cart addition test scenarios
│           ├── ViewCartTest.java          # Cart view test scenarios
│           ├── CheckoutTest.java          # Checkout flow test scenarios
│           └── SwagLabsTestSuite.java     # JUnit Platform Suite runner
├── src/test/resources/
│   ├── allure.properties                  # Allure results directory config
│   └── environment.properties            # Environment info shown in Allure report
├── .github/workflows/
│   └── selenium-ci-cd.yml                # GitHub Actions CI/CD pipeline
├── Dockerfile                            # Multi-stage Docker image definition
├── entrypoint.sh                         # Container startup and results copy script
├── docker-compose.yml                    # Local Docker Compose configuration
├── docker-run.sh                         # Linux/Mac Docker run helper script
├── docker-run.bat                        # Windows Docker run helper script
├── .dockerignore                         # Files excluded from Docker build context
└── pom.xml                               # Maven project configuration
```

---

## Phase 1 — Maven Project Setup

### What was done

The project was initialised as a standard Maven project with `groupId: com.swaglabs` and `artifactId: ui-automation`. All dependency versions are centralised in the `<properties>` block of `pom.xml` so they can be updated in one place.

### Key dependencies configured

```xml
<!-- Selenium WebDriver -->
<dependency>
    <groupId>org.seleniumhq.selenium</groupId>
    <artifactId>selenium-java</artifactId>
    <version>4.15.0</version>
</dependency>

<!-- WebDriverManager — downloads matching ChromeDriver automatically -->
<dependency>
    <groupId>io.github.bonigarcia</groupId>
    <artifactId>webdrivermanager</artifactId>
    <version>5.6.2</version>
</dependency>

<!-- JUnit 5 -->
<dependency>
    <groupId>org.junit.jupiter</groupId>
    <artifactId>junit-jupiter</artifactId>
    <version>5.10.0</version>
    <scope>test</scope>
</dependency>
```

### Maven Surefire configuration

The Surefire plugin is configured to:
- Pick up all `*Test.java`, `*Tests.java`, and `*TestSuite.java` files
- Pass `browser` and `headless` as system properties to tests
- Write Allure results to `target/allure-results`
- Load the AspectJ weaver agent for Allure annotation processing

```xml
<argLine>
    -javaagent:"${settings.localRepository}/org/aspectj/aspectjweaver/
    ${aspectj.version}/aspectjweaver-${aspectj.version}.jar"
</argLine>
```

### Maven profiles

| Profile | Effect |
|---|---|
| `headless` | Sets `headless=true` |
| `firefox` | Sets `browser=firefox` |
| `edge` | Sets `browser=edge` |
| `login-only` | Runs only `LoginTest.java` |

Usage: `mvn test -Plogin-only`

---

## Phase 2 — WebDriver Base Setup

### BrowserConfig.java

Reads browser configuration from system properties at runtime, with sensible defaults:

```java
// Reads -Dbrowser=chrome / firefox / edge
public static BrowserType getBrowserType()

// Reads -Dheadless=true / false
public static boolean isHeadless()
```

Default values:
- Browser: `CHROME`
- Headless: `true`
- Implicit wait: 10 seconds
- Page load timeout: 30 seconds
- Explicit wait: 15 seconds

### DriverFactory.java

Creates the correct `WebDriver` instance based on `BrowserConfig`. For Chrome, a comprehensive set of options is applied:

**Stability flags:**
```
--no-sandbox                    # Required in Docker and CI environments
--disable-dev-shm-usage         # Prevents Chrome crashes in low-memory environments
--disable-gpu                   # No GPU in headless/container environments
--remote-allow-origins=*        # Allows WebDriver remote connections
```

**Noise suppression flags** (prevents popups and alerts during tests):
```
--disable-password-manager-reauthentication
--disable-features=PasswordManager,PasswordManagerOnboarding
--disable-notifications
--disable-infobars
--disable-save-password-bubble
```

**Chrome preferences** (set via `setExperimentalOption`):
```java
chromePrefs.put("credentials_enable_service", false);
chromePrefs.put("profile.password_manager_leak_detection", false);
chromePrefs.put("autofill.password_enabled", false);
```

These preferences were added specifically to suppress the "password found in data breach" alert that Swag Labs triggers on login.

**WebDriverManager** handles ChromeDriver automatically:
```java
WebDriverManager.chromedriver().setup();
```
It queries Google's API, downloads the exact ChromeDriver version matching the installed Chrome, and caches it. This works identically locally and inside Docker.

### DriverManager.java

Manages the WebDriver lifecycle using a `ThreadLocal<WebDriver>` for thread safety:

```java
// Set driver for current thread
public static void setDriver(WebDriver driver)

// Get driver for current thread
public static WebDriver getDriver()

// Quit and remove driver for current thread
public static void quitDriver()
```

### BaseTest.java

JUnit lifecycle class that all test classes extend:

```java
@BeforeEach
void setUp() {
    WebDriver driver = DriverFactory.createDriver();
    DriverManager.setDriver(driver);
}

@AfterEach
void tearDown() {
    DriverManager.quitDriver();
}
```

---

## Phase 3 — Page Object Model

Each page of the application has a dedicated class under `src/main/java/com/swaglabs/pages/`. All page classes extend `BasePage` which provides shared WebDriver access and wait utilities.

### Page classes and responsibilities

**LoginPage.java**
- Locators: username field, password field, login button, error message container
- Methods: `navigateToLoginPage()`, `login(username, password)`, `isLoginPageLoaded()`, `isErrorMessageDisplayed()`, `getErrorMessage()`

**ProductsPage.java**
- Locators: product list, add-to-cart buttons, cart badge, sort dropdown
- Methods: `isProductsPageLoaded()`, `addProductToCart(index)`, `addProductToCartByName(name)`, `removeProductFromCartByName(name)`, `getCartItemCount()`, `sortProductsByValue(value)`, `getAllProductNames()`, `getAllProductPrices()`

**CartPage.java**
- Locators: cart items, item names, item prices, checkout button, continue shopping button
- Methods: `isCartPageLoaded()`, `getCartItems()`, `proceedToCheckout()`, `removeItem(name)`

**CheckoutPage.java**
- Locators: first name, last name, postal code fields, continue button, error message
- Methods: `isCheckoutPageLoaded()`, `fillCheckoutForm(firstName, lastName, postalCode)`, `continueToOverview()`, `isErrorDisplayed()`

**CheckoutOverviewPage.java**
- Locators: item list, subtotal, tax, total, finish button
- Methods: `isOverviewPageLoaded()`, `getItemTotal()`, `getTax()`, `getOrderTotal()`, `finishOrder()`

**CheckoutCompletePage.java**
- Locators: confirmation header, confirmation text, back home button
- Methods: `isOrderComplete()`, `getConfirmationHeader()`, `backToHome()`

### TestData.java

All test data is stored as constants to avoid magic strings in test classes:

```java
public static final String VALID_USERNAME = "standard_user";
public static final String VALID_PASSWORD = "secret_sauce";
public static final String LOCKED_USERNAME = "locked_out_user";
public static final String INVALID_USERNAME = "invalid_user";
public static final String INVALID_PASSWORD = "wrong_password";

public static final String INVALID_CREDENTIALS_ERROR =
    "Epic sadface: Username and password do not match any user in this service";
public static final String LOCKED_USER_ERROR =
    "Epic sadface: Sorry, this user has been locked out.";
public static final String EMPTY_USERNAME_ERROR =
    "Epic sadface: Username is required";
public static final String EMPTY_PASSWORD_ERROR =
    "Epic sadface: Password is required";

public static final String SAUCE_LABS_BACKPACK = "Sauce Labs Backpack";
public static final String BACKPACK_PRICE = "$29.99";
// ... etc
```

---

## Phase 4 — Test Case Implementation

### LoginTest.java

The most complete and stable test class. Uses `@TestMethodOrder(MethodOrderer.OrderAnnotation.class)` to run tests in a defined sequence.

| Order | Test | What it verifies |
|---|---|---|
| 1 | `testSuccessfulLogin` | Valid credentials → lands on products page, correct URL, no error |
| 2 | `testLoginWithInvalidCredentials` | Wrong credentials → stays on login, error message shown |
| 3 | `testLoginWithLockedUser` | Locked account → specific locked-out error message |
| 4 | `testLoginWithEmptyUsername` | Empty username → "Username is required" error |
| 5 | `testLoginWithEmptyPassword` | Empty password → "Password is required" error |
| 6 | `testLoginPageElements` | Page title, URL, field interactions work correctly |

### AddToCartTest.java

| Test | What it verifies |
|---|---|
| `testAddSingleProductToCart` | Cart count increments to 1 |
| `testAddMultipleProductsToCart` | Cart count increments to 3 |
| `testRemoveProductFromCart` | Cart count decrements, product state resets |
| `testAddAndRemoveAllProducts` | All 6 products can be added and removed |
| `testProductInformationDisplay` | Product names and prices are displayed |
| `testProductSorting` | All 4 sort options work correctly |

### SwagLabsTestSuite.java

A JUnit Platform Suite that groups test classes for suite-level execution:

```java
@Suite
@SuiteDisplayName("Swag Labs Complete UI Test Suite")
@SelectClasses({ LoginTest.class })
public class SwagLabsTestSuite {}
```

---

## Phase 5 — Allure Reports Integration

### Dependencies added

```xml
<dependency>
    <groupId>io.qameta.allure</groupId>
    <artifactId>allure-junit5</artifactId>
    <version>2.24.0</version>
    <scope>test</scope>
</dependency>
<dependency>
    <groupId>org.aspectj</groupId>
    <artifactId>aspectjweaver</artifactId>
    <version>1.9.20.1</version>
    <scope>test</scope>
</dependency>
```

### Allure annotations used in LoginTest

```java
@Epic("User Authentication")          // Top-level grouping
@Feature("Login Functionality")        // Feature grouping
@Story("Valid Login")                  // Story within feature
@Severity(SeverityLevel.CRITICAL)      // Priority level
@Description("...")                    // Test description
@Step("Navigate to login page")        // Step-level reporting
@Owner("QA Team")                      // Test ownership
```

### Configuration files

**`src/test/resources/allure.properties`**
```properties
allure.results.directory=target/allure-results
```

**`src/test/resources/environment.properties`**
```properties
Browser=Chrome
Environment=Test
Application=Swag Labs
URL=https://www.saucedemo.com
```

### Generating the report locally

```bash
# Run tests (results go to target/allure-results)
mvn clean test

# Generate HTML report
mvn allure:report

# Open report
open target/allure-report/index.html
```

---

## Phase 6 — GitHub Actions CI/CD

### Workflow file: `.github/workflows/selenium-ci-cd.yml`

**Triggers:**
- Push to `main`, `dev`, `feature/**`
- Pull requests to `main`, `dev`

**Permissions required:**
```yaml
permissions:
  contents: write   # For gh-pages deployment
  pages: write
  id-token: write
```

### Pipeline steps in order

| Step | What it does |
|---|---|
| Checkout repository | Clones the repo |
| Set up Docker Buildx | Enables BuildKit for layer caching |
| Cache Docker layers | Caches image layers keyed on `Dockerfile + entrypoint.sh + pom.xml` |
| Build Docker image | Builds `selenium-tests:latest` using cached layers |
| Rotate build cache | Prevents unbounded cache growth |
| Prepare output directory | Creates `target/` on the runner for volume mount |
| Run tests in Docker | Runs the container with `--shm-size=2g` and mounts `target:/output` |
| Get Allure history | Checks out `gh-pages` branch for historical trend data |
| Generate Allure Report | Builds HTML report with history using `simple-elf/allure-report-action` |
| Deploy to GitHub Pages | Publishes report to `gh-pages` branch |
| Upload Allure results | Uploads raw results as artifact (30-day retention) |
| Upload Allure report | Uploads HTML report as artifact (30-day retention) |
| Upload Surefire reports | Uploads XML test reports as artifact (30-day retention) |
| Test Report Summary | Writes per-class breakdown to GitHub Actions summary tab |
| Extract test results | Parses Maven log for total/passed/failed/skipped counts |
| Slack notification | Sends test summary to Slack channel |

### Docker layer caching in CI

```yaml
- name: Cache Docker layers
  uses: actions/cache@v4
  with:
    path: /tmp/.buildx-cache
    key: ${{ runner.os }}-buildx-${{ hashFiles('Dockerfile', 'entrypoint.sh', 'pom.xml') }}
```

The cache key includes the hash of `Dockerfile`, `entrypoint.sh`, and `pom.xml`. If none of those files change between pushes, the entire Docker build is skipped and the cached image is reused — reducing CI build time significantly.

---

## Phase 7 — Slack Notifications

### Setup

1. Create an Incoming Webhook in your Slack workspace (Apps → Incoming Webhooks)
2. Add the webhook URL as a GitHub repository secret named `SLACK_WEBHOOK_URL`
3. The workflow sends a notification after every run using `slackapi/slack-github-action@v1.27.0`

### Notification content

```
CI Pipeline Status

📦 Repository: your-org/ui-automation
🌿 Branch: main
🧾 Commit: abc1234
📊 Status: success

━━━━━━━━━━━━━━━━━━
📈 Test Summary
• Total Tests: 35
• Passed: 35
• Failed: 0
• Skipped: 0
━━━━━━━━━━━━━━━━━━

🔗 Run Details: https://github.com/...
```

### How test counts are extracted

The `entrypoint.sh` script pipes Maven output to `/app/maven-test-output.txt` inside the container, then copies it to `/output/maven-test-output.txt`. The workflow reads this file and parses the single aggregate summary line that Maven prints after `[INFO] Results:`:

```
Tests run: 35, Failures: 0, Errors: 0, Skipped: 0
```

This is the same line visible at the bottom of any `mvn test` terminal output.

---

## Phase 8 — Docker Containerization

### Architecture

```
docker build
    └── Stage 1 (maven:3.9.6-eclipse-temurin-17)
            └── mvn dependency:go-offline → caches /root/.m2
    └── Stage 2 (maven:3.9.6-eclipse-temurin-17)
            ├── Install Google Chrome (from dl.google.com)
            ├── Copy /root/.m2 from Stage 1
            ├── Copy pom.xml + src/
            └── Copy entrypoint.sh

docker run --shm-size=2g -v ./target:/output selenium-tests
    └── entrypoint.sh
            ├── mvn clean test -Dheadless=true | tee maven-test-output.txt
            ├── cp target/allure-results → /output/allure-results
            ├── cp target/surefire-reports → /output/surefire-reports
            └── cp maven-test-output.txt → /output/maven-test-output.txt
```

### Why this architecture

**Multi-stage build:** Stage 1 downloads all Maven dependencies into `/root/.m2`. This layer is only invalidated when `pom.xml` changes. Stage 2 copies that cache in, so source code changes don't trigger a re-download of dependencies.

**No ChromeDriver in the image:** ChromeDriver is not installed at build time. WebDriverManager downloads the exact matching version at test runtime. This avoids the fragile version-matching problem that caused multiple build failures during development.

**`/output` mount instead of `/app/target`:** Mounting directly over `/app/target` prevents `mvn clean` from deleting the directory (the OS blocks deletion of mount points). Instead, Maven writes freely to `/app/target`, and `entrypoint.sh` copies results to `/output` after tests finish.

**`--shm-size=2g`:** Chrome uses `/dev/shm` (shared memory) heavily. Docker limits this to 64MB by default, which causes Chrome to crash. This flag increases it to 2GB.

### Dockerfile

```dockerfile
# Stage 1: cache Maven dependencies
FROM maven:3.9.6-eclipse-temurin-17 AS dependencies
WORKDIR /build
COPY pom.xml .
RUN mvn dependency:go-offline -B --no-transfer-progress

# Stage 2: runtime
FROM maven:3.9.6-eclipse-temurin-17
USER root

# Install Chrome + dependencies
RUN apt-get update && apt-get install -y wget gnupg ca-certificates ... && \
    wget -qO /tmp/google-chrome.deb \
        https://dl.google.com/linux/direct/google-chrome-stable_current_amd64.deb && \
    apt-get install -y /tmp/google-chrome.deb && \
    apt-get clean && rm -rf /var/lib/apt/lists/*

WORKDIR /app
COPY --from=dependencies /root/.m2 /root/.m2
COPY pom.xml .
COPY src ./src
COPY entrypoint.sh /entrypoint.sh
RUN chmod +x /entrypoint.sh

ENV BROWSER=chrome HEADLESS=true
ENTRYPOINT ["/entrypoint.sh"]
```

### entrypoint.sh

```bash
#!/bin/bash
set -euo pipefail

# Run tests with timeout, capture output
timeout "${TIMEOUT_MINUTES}m" mvn clean test \
    -Dheadless=true -Dbrowser=chrome -B --no-transfer-progress \
    | tee /app/maven-test-output.txt
EXIT_CODE=${PIPESTATUS[0]}

# Copy results to the bind-mounted /output directory
cp -r /app/target/allure-results/.   /output/allure-results/
cp -r /app/target/surefire-reports/. /output/surefire-reports/
cp    /app/maven-test-output.txt      /output/maven-test-output.txt

exit $EXIT_CODE
```

---

## Running the Project

### Locally without Docker

```bash
# Run all tests (headless)
mvn clean test -Dheadless=true

# Run with visible browser
mvn clean test -Dheadless=false

# Run only login tests
mvn test -Plogin-only

# Run with Firefox
mvn test -Pfirefox -Dheadless=true

# Generate Allure report after tests
mvn allure:report
open target/allure-report/index.html
```

### Locally with Docker

```bash
# Build the image
docker build -t selenium-tests .

# Run tests and get results in ./target
docker run --shm-size=2g -v ${PWD}/target:/output --name selenium-tests-run selenium-tests

# View logs from last run
docker logs selenium-tests-run

# Debug mode (interactive shell inside container)
docker run -it --shm-size=2g --entrypoint /bin/bash selenium-tests
```

### With Docker Compose

```bash
# Build and run
docker-compose up --build

# Run without rebuilding
docker-compose up
```

### Using the helper scripts

```bash
# Linux/Mac
chmod +x docker-run.sh
./docker-run.sh           # build + run
./docker-run.sh --build   # force rebuild
./docker-run.sh --debug   # interactive shell

# Windows
docker-run.bat
docker-run.bat --build
docker-run.bat --debug
```

---

## Configuration Reference

| Property | Default | How to set | Effect |
|---|---|---|---|
| `browser` | `chrome` | `-Dbrowser=firefox` | Which browser to use |
| `headless` | `true` | `-Dheadless=false` | Show/hide browser window |
| `BROWSER` | `chrome` | `-e BROWSER=chrome` (Docker) | Browser in container |
| `HEADLESS` | `true` | `-e HEADLESS=true` (Docker) | Headless in container |
| `TEST_TIMEOUT_MINUTES` | `30` | `-e TEST_TIMEOUT_MINUTES=25` | Kill hung tests after N minutes |

---

## Secrets and Environment Variables

| Secret name | Where to add | Purpose |
|---|---|---|
| `SLACK_WEBHOOK_URL` | GitHub → Settings → Secrets → Actions | Slack notification webhook |
| `GITHUB_TOKEN` | Auto-provided by GitHub Actions | GitHub Pages deployment |

To add `SLACK_WEBHOOK_URL`:
1. Go to your Slack workspace → Apps → Incoming Webhooks
2. Create a new webhook for your desired channel
3. Copy the webhook URL
4. Go to GitHub repository → Settings → Secrets and Variables → Actions
5. Click "New repository secret" → Name: `SLACK_WEBHOOK_URL` → paste the URL

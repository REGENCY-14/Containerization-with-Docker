# =============================================================================
# Stage 1: Dependency cache
# Pre-download all Maven dependencies so they are cached as a separate layer.
# This layer only rebuilds when pom.xml changes.
# =============================================================================
FROM maven:3.9.6-eclipse-temurin-17 AS dependencies

WORKDIR /build
COPY pom.xml .
RUN mvn dependency:go-offline -B --no-transfer-progress


# =============================================================================
# Stage 2: Runtime image
# Uses maven:eclipse-temurin-17 (JDK 17 + Maven pre-installed) as the base.
# We install Google Chrome on top — ChromeDriver is managed by WebDriverManager.
# This avoids the complexity of layering Java onto selenium/standalone-chrome.
# =============================================================================
FROM maven:3.9.6-eclipse-temurin-17

USER root

# -----------------------------------------------------------------------------
# Install Google Chrome stable
# Using the official Google Linux repository for a stable, up-to-date binary.
# ChromeDriver is NOT installed here — WebDriverManager handles it at runtime
# when DOCKER=false, or the system chrome is used directly when DOCKER=true.
# -----------------------------------------------------------------------------
RUN apt-get update && \
    apt-get install -y --no-install-recommends \
        wget \
        gnupg \
        ca-certificates \
        fonts-liberation \
        libasound2 \
        libatk-bridge2.0-0 \
        libatk1.0-0 \
        libcups2 \
        libdbus-1-3 \
        libgdk-pixbuf2.0-0 \
        libnspr4 \
        libnss3 \
        libx11-xcb1 \
        libxcomposite1 \
        libxdamage1 \
        libxrandr2 \
        xdg-utils && \
    wget -qO /tmp/google-chrome.deb \
        https://dl.google.com/linux/direct/google-chrome-stable_current_amd64.deb && \
    apt-get install -y /tmp/google-chrome.deb && \
    rm /tmp/google-chrome.deb && \
    apt-get clean && \
    rm -rf /var/lib/apt/lists/*

# -----------------------------------------------------------------------------
# Install ChromeDriver that matches the installed Chrome version.
# Uses the Chrome for Testing JSON endpoint to get the matching driver.
# -----------------------------------------------------------------------------
RUN CHROME_VERSION=$(google-chrome --version | grep -o '[0-9]*\.[0-9]*\.[0-9]*\.[0-9]*') && \
    CHROME_MAJOR=$(echo $CHROME_VERSION | cut -d. -f1) && \
    echo "Chrome version: $CHROME_VERSION (major: $CHROME_MAJOR)" && \
    DRIVER_URL=$(wget -qO- "https://googlechromelabs.github.io/chrome-for-testing/known-good-versions-with-downloads.json" \
        | grep -o "\"chromedriver\".*linux64.*\"url\":\"[^\"]*\"" \
        | grep "\"$CHROME_VERSION\"" \
        | grep -o 'https://[^"]*linux64[^"]*chromedriver[^"]*\.zip' \
        | head -1) && \
    if [ -z "$DRIVER_URL" ]; then \
        DRIVER_URL="https://storage.googleapis.com/chrome-for-testing-public/${CHROME_VERSION}/linux64/chromedriver-linux64.zip"; \
    fi && \
    echo "Downloading ChromeDriver from: $DRIVER_URL" && \
    wget -qO /tmp/chromedriver.zip "$DRIVER_URL" && \
    unzip -q /tmp/chromedriver.zip -d /tmp/chromedriver && \
    find /tmp/chromedriver -name "chromedriver" -exec mv {} /usr/local/bin/chromedriver \; && \
    chmod +x /usr/local/bin/chromedriver && \
    rm -rf /tmp/chromedriver.zip /tmp/chromedriver

# -----------------------------------------------------------------------------
# Verify the full toolchain before copying project files.
# A failure here gives a clear build-time error rather than a runtime surprise.
# -----------------------------------------------------------------------------
RUN java -version && \
    mvn -version && \
    google-chrome --version && \
    chromedriver --version

WORKDIR /app

# -----------------------------------------------------------------------------
# Copy pre-downloaded Maven dependencies from Stage 1.
# Placed before source files so this layer is reused on code-only changes.
# -----------------------------------------------------------------------------
COPY --from=dependencies /root/.m2 /root/.m2

# Copy project source
COPY pom.xml .
COPY src ./src

# Copy and wire up the entrypoint script
COPY entrypoint.sh /entrypoint.sh
RUN chmod +x /entrypoint.sh

# -----------------------------------------------------------------------------
# DOCKER=true  → DriverFactory uses system ChromeDriver instead of downloading
# HEADLESS=true → Chrome runs headless (required, no display in container)
# These can be overridden at docker run time with -e flags
# -----------------------------------------------------------------------------
ENV DOCKER=true \
    BROWSER=chrome \
    HEADLESS=true

ENTRYPOINT ["/entrypoint.sh"]

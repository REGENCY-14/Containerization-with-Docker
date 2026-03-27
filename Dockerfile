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
# maven:eclipse-temurin-17 already has JDK 17 + Maven.
# We install Google Chrome — WebDriverManager downloads the matching
# ChromeDriver automatically at test runtime (no version pinning needed).
# =============================================================================
FROM maven:3.9.6-eclipse-temurin-17

USER root

# -----------------------------------------------------------------------------
# Install Google Chrome stable + runtime dependencies in one layer.
# Chrome is fetched directly from Google — always gets the latest stable.
# -----------------------------------------------------------------------------
RUN apt-get update && \
    apt-get install -y --no-install-recommends \
        wget \
        gnupg \
        ca-certificates \
        unzip \
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
# Verify Chrome is installed correctly.
# ChromeDriver is NOT installed here — WebDriverManager downloads the exact
# matching version at test runtime, which is simpler and always correct.
# -----------------------------------------------------------------------------
RUN java -version && mvn -version && google-chrome --version

WORKDIR /app

# Copy pre-downloaded Maven dependencies from Stage 1
COPY --from=dependencies /root/.m2 /root/.m2

# Copy project source
COPY pom.xml .
COPY src ./src

# Copy entrypoint script
COPY entrypoint.sh /entrypoint.sh
RUN chmod +x /entrypoint.sh

# -----------------------------------------------------------------------------
# HEADLESS=true  → Chrome runs headless (no display in container)
# BROWSER=chrome → use Chrome
# NOTE: DOCKER=true is intentionally NOT set here so that WebDriverManager
# runs normally and downloads the ChromeDriver that matches the installed Chrome.
# -----------------------------------------------------------------------------
ENV BROWSER=chrome \
    HEADLESS=true

ENTRYPOINT ["/entrypoint.sh"]

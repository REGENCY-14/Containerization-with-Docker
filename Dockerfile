# =============================================================================
# Stage 1: Dependency cache
# Uses the official Maven+JDK image to pre-download all project dependencies.
# This layer is only invalidated when pom.xml changes, not on source edits.
# =============================================================================
FROM maven:3.9.6-eclipse-temurin-17-focal AS dependencies

WORKDIR /build
COPY pom.xml .
RUN mvn dependency:go-offline -B --no-transfer-progress


# =============================================================================
# Stage 2: Runtime image
# selenium/standalone-chrome ships Chrome, ChromeDriver, and Xvfb pre-matched.
# Pinned to a specific version for reproducible builds — update intentionally.
# =============================================================================
FROM selenium/standalone-chrome:4.18.1

USER root

# -----------------------------------------------------------------------------
# Install JDK 17 + Maven in a single RUN layer to minimise image layers.
# Using the official Eclipse Temurin binaries via the Adoptium apt repository.
# --no-install-recommends keeps the layer as small as possible.
# -----------------------------------------------------------------------------
ARG MAVEN_VERSION=3.9.6

RUN apt-get update && \
    apt-get install -y --no-install-recommends wget gnupg software-properties-common && \
    wget -qO - https://packages.adoptium.net/artifactory/api/gpg/key/public \
        | gpg --dearmor -o /usr/share/keyrings/adoptium.gpg && \
    echo "deb [signed-by=/usr/share/keyrings/adoptium.gpg] \
        https://packages.adoptium.net/artifactory/deb $(. /etc/os-release && echo $VERSION_CODENAME) main" \
        > /etc/apt/sources.list.d/adoptium.list && \
    apt-get update && \
    apt-get install -y --no-install-recommends temurin-17-jdk && \
    # Install Maven from Apache archive
    wget -qO /tmp/maven.tar.gz \
        https://archive.apache.org/dist/maven/maven-3/${MAVEN_VERSION}/binaries/apache-maven-${MAVEN_VERSION}-bin.tar.gz && \
    tar -xzf /tmp/maven.tar.gz -C /opt && \
    ln -s /opt/apache-maven-${MAVEN_VERSION} /opt/maven && \
    # Clean up in the same layer to avoid bloating the image
    rm /tmp/maven.tar.gz && \
    apt-get remove -y wget gnupg software-properties-common && \
    apt-get autoremove -y && \
    apt-get clean && \
    rm -rf /var/lib/apt/lists/*

# -----------------------------------------------------------------------------
# Set JAVA_HOME by resolving the real JDK path at build time.
# readlink -f handles any architecture (amd64, arm64, etc.) without hardcoding.
# Both JAVA_HOME and MAVEN_HOME are set in a single ENV instruction.
# -----------------------------------------------------------------------------
RUN JAVA_PATH=$(dirname $(dirname $(readlink -f $(which java)))) && \
    ln -sfn "$JAVA_PATH" /usr/lib/jvm/java-17

ENV JAVA_HOME=/usr/lib/jvm/java-17 \
    MAVEN_HOME=/opt/maven \
    PATH="/opt/maven/bin:/usr/lib/jvm/java-17/bin:${PATH}"

# -----------------------------------------------------------------------------
# Verify the toolchain is correctly wired before copying any project files.
# Failing here gives a clear error during build rather than at test runtime.
# -----------------------------------------------------------------------------
RUN java -version && mvn -version && google-chrome --version && chromedriver --version

WORKDIR /app

# -----------------------------------------------------------------------------
# Copy pre-downloaded Maven dependencies from Stage 1.
# Placed before source copy so this layer is reused on source-only changes.
# -----------------------------------------------------------------------------
COPY --from=dependencies /root/.m2 /root/.m2

# Copy project files
COPY pom.xml .
COPY src ./src

# Copy and wire up the entrypoint
COPY entrypoint.sh /entrypoint.sh
RUN chmod +x /entrypoint.sh

# -----------------------------------------------------------------------------
# Runtime environment variables.
# DOCKER=true  → DriverFactory skips WebDriverManager download, uses system driver
# These can be overridden at docker run time with -e flags
# -----------------------------------------------------------------------------
ENV DOCKER=true \
    BROWSER=chrome \
    HEADLESS=true

ENTRYPOINT ["/entrypoint.sh"]

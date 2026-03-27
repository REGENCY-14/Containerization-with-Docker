# =============================================================================
# Stage 1: Dependency cache
# Pre-download all Maven dependencies into a cached layer so subsequent
# builds don't re-download them when only source code changes.
# =============================================================================
FROM maven:3.9.6-eclipse-temurin-17 AS dependencies

WORKDIR /build

# Copy only the POM first — this layer is only invalidated when pom.xml changes
COPY pom.xml .

# Download all dependencies offline so the next stage can build without network
RUN mvn dependency:go-offline -B --no-transfer-progress


# =============================================================================
# Stage 2: Runtime image
# Built on top of the official Selenium standalone-chrome image which ships
# with Chrome, ChromeDriver, and Xvfb pre-installed and version-matched.
# We layer JDK 17 and Maven on top.
# =============================================================================
FROM selenium/standalone-chrome:latest

# Switch to root to install system packages
USER root

# -----------------------------------------------------------------------------
# Install JDK 17
# selenium/standalone-chrome is Ubuntu-based so we use the Adoptium repository
# -----------------------------------------------------------------------------
RUN apt-get update && \
    apt-get install -y --no-install-recommends \
        wget \
        gnupg \
        software-properties-common && \
    wget -qO - https://packages.adoptium.net/artifactory/api/gpg/key/public | \
        gpg --dearmor -o /usr/share/keyrings/adoptium.gpg && \
    echo "deb [signed-by=/usr/share/keyrings/adoptium.gpg] \
        https://packages.adoptium.net/artifactory/deb $(. /etc/os-release && echo $VERSION_CODENAME) main" \
        > /etc/apt/sources.list.d/adoptium.list && \
    apt-get update && \
    apt-get install -y --no-install-recommends temurin-17-jdk && \
    apt-get clean && \
    rm -rf /var/lib/apt/lists/*

# -----------------------------------------------------------------------------
# Install Maven 3.9
# -----------------------------------------------------------------------------
ARG MAVEN_VERSION=3.9.6
ARG MAVEN_URL=https://archive.apache.org/dist/maven/maven-3/${MAVEN_VERSION}/binaries/apache-maven-${MAVEN_VERSION}-bin.tar.gz

RUN wget -qO /tmp/maven.tar.gz ${MAVEN_URL} && \
    tar -xzf /tmp/maven.tar.gz -C /opt && \
    ln -s /opt/apache-maven-${MAVEN_VERSION} /opt/maven && \
    rm /tmp/maven.tar.gz

# Detect the actual JDK path dynamically instead of hardcoding the arch suffix
RUN JAVA_HOME_PATH=$(dirname $(dirname $(readlink -f $(which java)))) && \
    echo "JAVA_HOME=$JAVA_HOME_PATH" >> /etc/environment && \
    echo "export JAVA_HOME=$JAVA_HOME_PATH" >> /etc/profile.d/java.sh && \
    echo "export JAVA_HOME=$JAVA_HOME_PATH" >> /root/.bashrc

ENV MAVEN_HOME=/opt/maven

# Set JAVA_HOME by resolving the real path at build time
RUN echo "export PATH=$MAVEN_HOME/bin:\$JAVA_HOME/bin:\$PATH" >> /etc/profile.d/java.sh

# Use shell form to evaluate JAVA_HOME dynamically
RUN JAVA_HOME=$(dirname $(dirname $(readlink -f $(which java)))) && \
    ln -sfn $JAVA_HOME /usr/lib/jvm/java-17 && \
    echo "Resolved JAVA_HOME: $JAVA_HOME"

ENV JAVA_HOME=/usr/lib/jvm/java-17
ENV PATH="${MAVEN_HOME}/bin:${JAVA_HOME}/bin:${PATH}"

# -----------------------------------------------------------------------------
# Set working directory for the project
# -----------------------------------------------------------------------------
WORKDIR /app

# Verify Java and Maven are correctly configured
RUN java -version && mvn -version

# -----------------------------------------------------------------------------
# Copy pre-downloaded Maven dependencies from Stage 1
# This avoids re-downloading on every source code change
# -----------------------------------------------------------------------------
COPY --from=dependencies /root/.m2 /root/.m2

# -----------------------------------------------------------------------------
# Copy project source files into the container
# -----------------------------------------------------------------------------
COPY pom.xml .
COPY src ./src

# -----------------------------------------------------------------------------
# Environment variables
# DOCKER=true  → tells DriverFactory to use the system ChromeDriver on PATH
#               instead of letting WebDriverManager download one
# -----------------------------------------------------------------------------
ENV DOCKER=true
ENV BROWSER=chrome
ENV HEADLESS=true

# -----------------------------------------------------------------------------
# Run the tests in headless mode when the container starts.
# NOTE: No VOLUME declaration here — the mount is handled at docker run time
# via -v ./target:/app/target which avoids permission conflicts with mvn clean.
# -Dheadless=true  → ChromeOptions adds --headless flag
# -Dbrowser=chrome → DriverFactory creates ChromeDriver
# -B               → Maven batch mode (no progress bars, cleaner logs)
# -----------------------------------------------------------------------------
CMD ["mvn", "clean", "test", "-Dheadless=true", "-Dbrowser=chrome", "-B", "--no-transfer-progress"]

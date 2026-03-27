#!/bin/bash
set -euo pipefail
# =============================================================================
# Container entrypoint
# Runs Maven tests then copies results to /output (the bind-mounted directory).
# Maven writes freely to /app/target — no mount conflict with mvn clean.
# =============================================================================

OUTPUT_DIR="/output"
TIMEOUT_MINUTES=${TEST_TIMEOUT_MINUTES:-30}

echo "============================================"
echo " Selenium Test Runner"
echo " Browser : ${BROWSER:-chrome}"
echo " Headless: ${HEADLESS:-true}"
echo " Timeout : ${TIMEOUT_MINUTES}m"
echo "============================================"

# Run Maven with a timeout to prevent hung tests from blocking CI indefinitely
timeout "${TIMEOUT_MINUTES}m" mvn clean test \
    -Dheadless="${HEADLESS:-true}" \
    -Dbrowser="${BROWSER:-chrome}" \
    -B \
    --no-transfer-progress | tee /app/maven-test-output.txt
EXIT_CODE=${PIPESTATUS[0]}

# timeout returns 124 if the process was killed
if [ $EXIT_CODE -eq 124 ]; then
    echo "❌ Tests timed out after ${TIMEOUT_MINUTES} minutes"
fi

echo ""
echo "📦 Copying results to ${OUTPUT_DIR}..."
mkdir -p "${OUTPUT_DIR}/allure-results"
mkdir -p "${OUTPUT_DIR}/surefire-reports"

# Use rsync-style copy — only copy if source exists, never fail if empty
[ -d /app/target/allure-results ]   && cp -r /app/target/allure-results/.   "${OUTPUT_DIR}/allure-results/"   || true
[ -d /app/target/surefire-reports ] && cp -r /app/target/surefire-reports/. "${OUTPUT_DIR}/surefire-reports/" || true
[ -f /app/maven-test-output.txt ]   && cp /app/maven-test-output.txt "${OUTPUT_DIR}/maven-test-output.txt"    || true

echo ""
if [ $EXIT_CODE -eq 0 ]; then
    echo "✅ All tests passed"
else
    echo "❌ Tests finished with exit code: ${EXIT_CODE}"
    # Print the last 50 lines of surefire output to help debug without needing artifacts
    echo ""
    echo "--- Last surefire output ---"
    find /app/target/surefire-reports -name "*.txt" -exec tail -20 {} \; 2>/dev/null || true
fi

exit $EXIT_CODE

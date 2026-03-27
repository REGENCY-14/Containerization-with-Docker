#!/bin/bash
# =============================================================================
# Container entrypoint script
# Runs Maven tests, then copies results to the mounted output directory.
# This avoids mounting directly over /app/target which blocks mvn clean.
# =============================================================================

OUTPUT_DIR="/output"

echo "🚀 Starting Selenium tests..."
mvn clean test -Dheadless=true -Dbrowser=chrome -B --no-transfer-progress
EXIT_CODE=$?

echo ""
echo "📦 Copying results to $OUTPUT_DIR..."
mkdir -p "$OUTPUT_DIR/allure-results"
mkdir -p "$OUTPUT_DIR/surefire-reports"

cp -r /app/target/allure-results/. "$OUTPUT_DIR/allure-results/" 2>/dev/null || true
cp -r /app/target/surefire-reports/. "$OUTPUT_DIR/surefire-reports/" 2>/dev/null || true

if [ $EXIT_CODE -eq 0 ]; then
  echo "✅ Tests passed. Results copied to $OUTPUT_DIR"
else
  echo "❌ Tests failed (exit code: $EXIT_CODE). Results copied to $OUTPUT_DIR"
fi

exit $EXIT_CODE

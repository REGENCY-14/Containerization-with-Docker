#!/bin/bash
# =============================================================================
# Docker build and run script for Selenium tests (Linux/Mac)
# Usage:
#   ./docker-run.sh          → build + run with default settings
#   ./docker-run.sh --build  → force rebuild the image
#   ./docker-run.sh --debug  → run interactively for debugging
# =============================================================================

IMAGE_NAME="selenium-tests"
CONTAINER_NAME="selenium-tests-run"
TARGET_DIR="$(pwd)/target"

# Parse arguments
BUILD_ONLY=false
DEBUG_MODE=false
FORCE_BUILD=false

for arg in "$@"; do
  case $arg in
    --build) FORCE_BUILD=true ;;
    --debug) DEBUG_MODE=true ;;
  esac
done

# -----------------------------------------------------------------------------
# Step 1: Build the image (skip if already built and no --build flag)
# -----------------------------------------------------------------------------
if $FORCE_BUILD || ! docker image inspect "$IMAGE_NAME" > /dev/null 2>&1; then
  echo "🔨 Building Docker image: $IMAGE_NAME"
  docker build -t "$IMAGE_NAME" .
  if [ $? -ne 0 ]; then
    echo "❌ Docker build failed. Check the output above."
    exit 1
  fi
  echo "✅ Image built successfully"
else
  echo "✅ Using existing image: $IMAGE_NAME (use --build to rebuild)"
fi

# -----------------------------------------------------------------------------
# Step 2: Ensure target directory exists for volume mount
# -----------------------------------------------------------------------------
mkdir -p "$TARGET_DIR"

# -----------------------------------------------------------------------------
# Step 3: Remove any previous container with the same name
# -----------------------------------------------------------------------------
docker rm -f "$CONTAINER_NAME" > /dev/null 2>&1

# -----------------------------------------------------------------------------
# Step 4: Run the container
# --shm-size=2g         → gives Chrome enough shared memory (prevents crashes)
# -v target:/app/target → mounts local target/ so results persist after run
# --name                → named container for easy log access
# -----------------------------------------------------------------------------
if $DEBUG_MODE; then
  echo "🐛 Starting container in DEBUG (interactive) mode..."
  echo "   Run tests manually: mvn clean test -Dheadless=true -Dbrowser=chrome"
  docker run -it \
    --shm-size=2g \
    -v "$TARGET_DIR:/app/target" \
    --name "$CONTAINER_NAME" \
    --entrypoint /bin/bash \
    "$IMAGE_NAME"
else
  echo "🚀 Running Selenium tests inside Docker..."
  docker run \
    --shm-size=2g \
    -v "$TARGET_DIR:/app/target" \
    --name "$CONTAINER_NAME" \
    "$IMAGE_NAME"

  EXIT_CODE=$?

  # ---------------------------------------------------------------------------
  # Step 5: Report results
  # ---------------------------------------------------------------------------
  echo ""
  if [ $EXIT_CODE -eq 0 ]; then
    echo "✅ Tests passed!"
  else
    echo "❌ Tests failed (exit code: $EXIT_CODE)"
    echo "   Run: docker logs $CONTAINER_NAME  to see full output"
  fi

  echo ""
  echo "📁 Results available at:"
  echo "   Allure results : $TARGET_DIR/allure-results/"
  echo "   Surefire reports: $TARGET_DIR/surefire-reports/"
  echo ""
  echo "📊 To generate Allure report locally:"
  echo "   mvn allure:report"
  echo "   open target/allure-report/index.html"

  exit $EXIT_CODE
fi

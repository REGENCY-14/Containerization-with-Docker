@echo off
REM =============================================================================
REM Docker build and run script for Selenium tests (Windows)
REM Usage:
REM   docker-run.bat         -> build + run with default settings
REM   docker-run.bat --build -> force rebuild the image
REM   docker-run.bat --debug -> run interactively for debugging
REM =============================================================================

SET IMAGE_NAME=selenium-tests
SET CONTAINER_NAME=selenium-tests-run
SET TARGET_DIR=%cd%\target
SET FORCE_BUILD=false
SET DEBUG_MODE=false

FOR %%A IN (%*) DO (
  IF "%%A"=="--build" SET FORCE_BUILD=true
  IF "%%A"=="--debug" SET DEBUG_MODE=true
)

REM -----------------------------------------------------------------------------
REM Step 1: Build the image
REM -----------------------------------------------------------------------------
IF "%FORCE_BUILD%"=="true" GOTO BUILD
docker image inspect %IMAGE_NAME% >nul 2>&1
IF %ERRORLEVEL% NEQ 0 GOTO BUILD
ECHO [OK] Using existing image: %IMAGE_NAME% (use --build to rebuild)
GOTO RUN

:BUILD
ECHO [..] Building Docker image: %IMAGE_NAME%
docker build -t %IMAGE_NAME% .
IF %ERRORLEVEL% NEQ 0 (
  ECHO [FAIL] Docker build failed.
  EXIT /B 1
)
ECHO [OK] Image built successfully

REM -----------------------------------------------------------------------------
REM Step 2: Ensure target directory exists
REM -----------------------------------------------------------------------------
:RUN
IF NOT EXIST "%TARGET_DIR%" MKDIR "%TARGET_DIR%"

REM Remove previous container
docker rm -f %CONTAINER_NAME% >nul 2>&1

REM -----------------------------------------------------------------------------
REM Step 3: Run the container
REM -----------------------------------------------------------------------------
IF "%DEBUG_MODE%"=="true" (
  ECHO [DEBUG] Starting container in interactive mode...
  docker run -it ^
    --shm-size=2g ^
    -v "%TARGET_DIR%:/app/target" ^
    --name %CONTAINER_NAME% ^
    --entrypoint /bin/bash ^
    %IMAGE_NAME%
) ELSE (
  ECHO [..] Running Selenium tests inside Docker...
  docker run ^
    --shm-size=2g ^
    -v "%TARGET_DIR%:/app/target" ^
    --name %CONTAINER_NAME% ^
    %IMAGE_NAME%

  IF %ERRORLEVEL% EQU 0 (
    ECHO.
    ECHO [OK] Tests passed!
  ) ELSE (
    ECHO.
    ECHO [FAIL] Tests failed. Run: docker logs %CONTAINER_NAME%
  )

  ECHO.
  ECHO Results available at:
  ECHO   Allure results  : %TARGET_DIR%\allure-results\
  ECHO   Surefire reports: %TARGET_DIR%\surefire-reports\
)

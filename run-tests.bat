@echo off
REM Swag Labs UI Automation Test Runner for Windows
REM This script provides easy commands to run tests locally

setlocal enabledelayedexpansion

REM Function to show usage
if "%1"=="" goto :show_usage
if "%1"=="help" goto :show_usage

REM Parse arguments
set COMMAND=%1
set BROWSER=chrome
set HEADLESS=
set MAVEN_ARGS=

:parse_args
shift
if "%1"=="" goto :end_parse
if "%1"=="--headless" (
    set HEADLESS=-Dheadless=true
    goto :parse_args
)
if "%1"=="--firefox" (
    set BROWSER=firefox
    goto :parse_args
)
if "%1"=="--edge" (
    set BROWSER=edge
    goto :parse_args
)
if "%1"=="--chrome" (
    set BROWSER=chrome
    goto :parse_args
)
echo Unknown option: %1
goto :show_usage

:end_parse
set BROWSER_ARG=-Dbrowser=%BROWSER%
set MAVEN_ARGS=%BROWSER_ARG% %HEADLESS%

REM Execute commands
if "%COMMAND%"=="clean" goto :clean
if "%COMMAND%"=="all" goto :all
if "%COMMAND%"=="login" goto :login
if "%COMMAND%"=="cart" goto :cart
if "%COMMAND%"=="view-cart" goto :view_cart
if "%COMMAND%"=="checkout" goto :checkout
if "%COMMAND%"=="suite" goto :suite

echo Unknown command: %COMMAND%
goto :show_usage

:clean
echo 🧹 Cleaning and compiling project...
mvn clean compile
if %ERRORLEVEL% neq 0 (
    echo ❌ Clean failed
    exit /b 1
)
echo ✅ Clean completed successfully
goto :end

:all
echo 🚀 Running all tests with browser: %BROWSER%, headless: %HEADLESS%
mvn test %MAVEN_ARGS%
if %ERRORLEVEL% neq 0 (
    echo ❌ Tests failed
    exit /b 1
)
echo ✅ All tests completed
goto :end

:login
echo 🔐 Running login tests (stable) with browser: %BROWSER%, headless: %HEADLESS%
mvn test -Dtest=LoginTest %MAVEN_ARGS%
if %ERRORLEVEL% neq 0 (
    echo ❌ Login tests failed
    exit /b 1
)
echo ✅ Login tests completed
goto :end

:cart
echo ⚠️  Running add to cart tests (may need stability improvements)
mvn test -Dtest=AddToCartTest %MAVEN_ARGS%
if %ERRORLEVEL% neq 0 (
    echo ❌ Cart tests failed
    exit /b 1
)
echo ✅ Cart tests completed
goto :end

:view_cart
echo ⚠️  Running view cart tests (may need stability improvements)
mvn test -Dtest=ViewCartTest %MAVEN_ARGS%
if %ERRORLEVEL% neq 0 (
    echo ❌ View cart tests failed
    exit /b 1
)
echo ✅ View cart tests completed
goto :end

:checkout
echo ⚠️  Running checkout tests (may need stability improvements)
mvn test -Dtest=CheckoutTest %MAVEN_ARGS%
if %ERRORLEVEL% neq 0 (
    echo ❌ Checkout tests failed
    exit /b 1
)
echo ✅ Checkout tests completed
goto :end

:suite
echo 📋 Running test suite with browser: %BROWSER%, headless: %HEADLESS%
mvn test -Dtest=SwagLabsTestSuite %MAVEN_ARGS%
if %ERRORLEVEL% neq 0 (
    echo ❌ Test suite failed
    exit /b 1
)
echo ✅ Test suite completed
goto :end

:show_usage
echo 🚀 Swag Labs UI Automation Test Runner
echo.
echo Usage: %0 [COMMAND] [OPTIONS]
echo.
echo Commands:
echo   all                 Run all tests
echo   login               Run only login tests (recommended - stable)
echo   cart                Run add to cart tests
echo   view-cart           Run view cart tests
echo   checkout            Run checkout tests
echo   suite               Run test suite
echo   clean               Clean and compile project
echo   help                Show this help message
echo.
echo Options:
echo   --headless          Run tests in headless mode (faster)
echo   --firefox           Use Firefox browser
echo   --edge              Use Edge browser
echo   --chrome            Use Chrome browser (default)
echo.
echo Examples:
echo   %0 login --headless                 # Run login tests in headless mode
echo   %0 all --firefox                    # Run all tests with Firefox
echo   %0 suite --headless --chrome        # Run test suite headless with Chrome
echo.
goto :end

:end
echo ✅ Test execution completed!
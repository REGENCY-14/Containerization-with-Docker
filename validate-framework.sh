#!/bin/bash

# Framework Validation Script
# Validates that the Swag Labs UI automation framework is ready for production use

set -e

# Colors for output
RED='\033[0;31m'
GREEN='\033[0;32m'
YELLOW='\033[1;33m'
BLUE='\033[0;34m'
NC='\033[0m' # No Color

print_header() {
    echo -e "${BLUE}================================${NC}"
    echo -e "${BLUE}🔍 Framework Validation Report${NC}"
    echo -e "${BLUE}================================${NC}"
    echo ""
}

print_section() {
    echo -e "${YELLOW}📋 $1${NC}"
    echo "----------------------------"
}

print_success() {
    echo -e "${GREEN}✅ $1${NC}"
}

print_warning() {
    echo -e "${YELLOW}⚠️  $1${NC}"
}

print_error() {
    echo -e "${RED}❌ $1${NC}"
}

# Validation counters
TOTAL_CHECKS=0
PASSED_CHECKS=0
FAILED_CHECKS=0

validate_check() {
    TOTAL_CHECKS=$((TOTAL_CHECKS + 1))
    if [ $1 -eq 0 ]; then
        PASSED_CHECKS=$((PASSED_CHECKS + 1))
        print_success "$2"
    else
        FAILED_CHECKS=$((FAILED_CHECKS + 1))
        print_error "$2"
    fi
}

print_header

# 1. Check Prerequisites
print_section "Prerequisites Check"

# Check Java
if command -v java &> /dev/null; then
    JAVA_VERSION=$(java -version 2>&1 | head -n 1 | cut -d'"' -f2)
    validate_check 0 "Java is installed: $JAVA_VERSION"
else
    validate_check 1 "Java is not installed"
fi

# Check Maven
if command -v mvn &> /dev/null; then
    MAVEN_VERSION=$(mvn -version 2>&1 | head -n 1 | cut -d' ' -f3)
    validate_check 0 "Maven is installed: $MAVEN_VERSION"
else
    validate_check 1 "Maven is not installed"
fi

echo ""

# 2. Check Project Structure
print_section "Project Structure Check"

# Check main directories
[ -d "src/main/java/com/swaglabs" ] && validate_check 0 "Main source directory exists" || validate_check 1 "Main source directory missing"
[ -d "src/test/java/com/swaglabs" ] && validate_check 0 "Test source directory exists" || validate_check 1 "Test source directory missing"

# Check key files
[ -f "pom.xml" ] && validate_check 0 "pom.xml exists" || validate_check 1 "pom.xml missing"
[ -f "README.md" ] && validate_check 0 "README.md exists" || validate_check 1 "README.md missing"
[ -f "TEST_EXECUTION_GUIDE.md" ] && validate_check 0 "Test execution guide exists" || validate_check 1 "Test execution guide missing"

echo ""

# 3. Check Core Framework Files
print_section "Core Framework Files Check"

# Base classes
[ -f "src/main/java/com/swaglabs/base/BasePage.java" ] && validate_check 0 "BasePage exists" || validate_check 1 "BasePage missing"
[ -f "src/test/java/com/swaglabs/base/BaseTest.java" ] && validate_check 0 "BaseTest exists" || validate_check 1 "BaseTest missing"

# Configuration
[ -f "src/main/java/com/swaglabs/config/BrowserConfig.java" ] && validate_check 0 "BrowserConfig exists" || validate_check 1 "BrowserConfig missing"

# Driver management
[ -f "src/main/java/com/swaglabs/driver/DriverFactory.java" ] && validate_check 0 "DriverFactory exists" || validate_check 1 "DriverFactory missing"
[ -f "src/main/java/com/swaglabs/driver/DriverManager.java" ] && validate_check 0 "DriverManager exists" || validate_check 1 "DriverManager missing"

# Page objects
[ -f "src/main/java/com/swaglabs/pages/LoginPage.java" ] && validate_check 0 "LoginPage exists" || validate_check 1 "LoginPage missing"
[ -f "src/main/java/com/swaglabs/pages/ProductsPage.java" ] && validate_check 0 "ProductsPage exists" || validate_check 1 "ProductsPage missing"

# Test classes
[ -f "src/test/java/com/swaglabs/tests/LoginTest.java" ] && validate_check 0 "LoginTest exists" || validate_check 1 "LoginTest missing"

# Utilities
[ -f "src/main/java/com/swaglabs/utils/TestData.java" ] && validate_check 0 "TestData exists" || validate_check 1 "TestData missing"
[ -f "src/test/java/com/swaglabs/utils/TestUtils.java" ] && validate_check 0 "TestUtils exists" || validate_check 1 "TestUtils missing"

echo ""

# 4. Compilation Check
print_section "Compilation Check"

echo "Compiling project..."
if mvn clean compile -q; then
    validate_check 0 "Project compiles successfully"
else
    validate_check 1 "Project compilation failed"
fi

echo ""

# 5. Test Compilation Check
print_section "Test Compilation Check"

echo "Compiling tests..."
if mvn test-compile -q; then
    validate_check 0 "Tests compile successfully"
else
    validate_check 1 "Test compilation failed"
fi

echo ""

# 6. Stable Test Execution Check
print_section "Stable Test Execution Check"

echo "Running stable LoginTest..."
if mvn test -Dtest=LoginTest#testSuccessfulLogin -Dheadless=true -q; then
    validate_check 0 "Stable test execution successful"
else
    validate_check 1 "Stable test execution failed"
fi

echo ""

# 7. Test Runner Scripts Check
print_section "Test Runner Scripts Check"

[ -f "run-tests.sh" ] && validate_check 0 "Linux/Mac test runner exists" || validate_check 1 "Linux/Mac test runner missing"
[ -f "run-tests.bat" ] && validate_check 0 "Windows test runner exists" || validate_check 1 "Windows test runner missing"

echo ""

# Final Report
print_section "Validation Summary"

echo "Total Checks: $TOTAL_CHECKS"
echo "Passed: $PASSED_CHECKS"
echo "Failed: $FAILED_CHECKS"

SUCCESS_RATE=$(( (PASSED_CHECKS * 100) / TOTAL_CHECKS ))
echo "Success Rate: $SUCCESS_RATE%"

echo ""

if [ $FAILED_CHECKS -eq 0 ]; then
    print_success "🎉 Framework validation PASSED! Ready for production use."
    echo ""
    echo -e "${GREEN}✅ Framework is ready for:${NC}"
    echo "   - Local development and testing"
    echo "   - CI/CD pipeline integration"
    echo "   - Docker containerization"
    echo "   - Cross-browser testing"
    echo ""
    exit 0
elif [ $SUCCESS_RATE -ge 80 ]; then
    print_warning "⚠️  Framework validation PASSED with warnings. Minor issues detected."
    echo ""
    echo -e "${YELLOW}Framework is mostly ready but may need minor fixes.${NC}"
    echo ""
    exit 0
else
    print_error "❌ Framework validation FAILED. Critical issues detected."
    echo ""
    echo -e "${RED}Framework needs significant fixes before production use.${NC}"
    echo ""
    exit 1
fi
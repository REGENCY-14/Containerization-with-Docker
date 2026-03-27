#!/bin/bash

# Swag Labs UI Automation Test Runner
# This script provides easy commands to run tests locally

set -e

# Colors for output
RED='\033[0;31m'
GREEN='\033[0;32m'
YELLOW='\033[1;33m'
BLUE='\033[0;34m'
NC='\033[0m' # No Color

# Function to print colored output
print_info() {
    echo -e "${BLUE}ℹ️  $1${NC}"
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

# Function to show usage
show_usage() {
    echo -e "${BLUE}🚀 Swag Labs UI Automation Test Runner${NC}"
    echo ""
    echo "Usage: $0 [COMMAND] [OPTIONS]"
    echo ""
    echo "Commands:"
    echo "  all                 Run all tests"
    echo "  login               Run only login tests (recommended - stable)"
    echo "  cart                Run add to cart tests"
    echo "  view-cart           Run view cart tests"
    echo "  checkout            Run checkout tests"
    echo "  suite               Run test suite"
    echo "  clean               Clean and compile project"
    echo "  help                Show this help message"
    echo ""
    echo "Options:"
    echo "  --headless          Run tests in headless mode (faster)"
    echo "  --firefox           Use Firefox browser"
    echo "  --edge              Use Edge browser"
    echo "  --chrome            Use Chrome browser (default)"
    echo ""
    echo "Examples:"
    echo "  $0 login --headless                 # Run login tests in headless mode"
    echo "  $0 all --firefox                    # Run all tests with Firefox"
    echo "  $0 suite --headless --chrome        # Run test suite headless with Chrome"
    echo ""
}

# Parse command line arguments
COMMAND=""
BROWSER="chrome"
HEADLESS=""
MAVEN_ARGS=""

while [[ $# -gt 0 ]]; do
    case $1 in
        all|login|cart|view-cart|checkout|suite|clean|help)
            COMMAND="$1"
            shift
            ;;
        --headless)
            HEADLESS="-Dheadless=true"
            shift
            ;;
        --firefox)
            BROWSER="firefox"
            shift
            ;;
        --edge)
            BROWSER="edge"
            shift
            ;;
        --chrome)
            BROWSER="chrome"
            shift
            ;;
        *)
            print_error "Unknown option: $1"
            show_usage
            exit 1
            ;;
    esac
done

# Set browser argument
BROWSER_ARG="-Dbrowser=$BROWSER"

# Combine Maven arguments
MAVEN_ARGS="$BROWSER_ARG $HEADLESS"

# Function to run Maven command with proper error handling
run_maven() {
    local cmd="$1"
    print_info "Running: mvn $cmd"
    
    if mvn $cmd; then
        print_success "Command completed successfully"
        return 0
    else
        print_error "Command failed"
        return 1
    fi
}

# Main execution logic
case $COMMAND in
    "help"|"")
        show_usage
        ;;
    "clean")
        print_info "Cleaning and compiling project..."
        run_maven "clean compile"
        ;;
    "all")
        print_info "Running all tests with browser: $BROWSER, headless: ${HEADLESS:-false}"
        run_maven "test $MAVEN_ARGS"
        ;;
    "login")
        print_info "Running login tests (stable) with browser: $BROWSER, headless: ${HEADLESS:-false}"
        run_maven "test -Dtest=LoginTest $MAVEN_ARGS"
        ;;
    "cart")
        print_warning "Running add to cart tests (may need stability improvements)"
        run_maven "test -Dtest=AddToCartTest $MAVEN_ARGS"
        ;;
    "view-cart")
        print_warning "Running view cart tests (may need stability improvements)"
        run_maven "test -Dtest=ViewCartTest $MAVEN_ARGS"
        ;;
    "checkout")
        print_warning "Running checkout tests (may need stability improvements)"
        run_maven "test -Dtest=CheckoutTest $MAVEN_ARGS"
        ;;
    "suite")
        print_info "Running test suite with browser: $BROWSER, headless: ${HEADLESS:-false}"
        run_maven "test -Dtest=SwagLabsTestSuite $MAVEN_ARGS"
        ;;
    *)
        print_error "Unknown command: $COMMAND"
        show_usage
        exit 1
        ;;
esac

print_success "Test execution completed!"
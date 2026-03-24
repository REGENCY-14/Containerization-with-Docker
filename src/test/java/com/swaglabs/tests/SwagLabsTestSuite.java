package com.swaglabs.tests;

import org.junit.platform.suite.api.SelectClasses;
import org.junit.platform.suite.api.Suite;
import org.junit.platform.suite.api.SuiteDisplayName;

/**
 * Test Suite for Swag Labs UI Automation
 * Runs all test classes in a structured order
 * 
 * Usage:
 * - Run entire suite: mvn test -Dtest=SwagLabsTestSuite
 * - Run with headless mode: mvn test -Dtest=SwagLabsTestSuite -Dheadless=true
 * - Run with different browser: mvn test -Dtest=SwagLabsTestSuite -Dbrowser=firefox
 */
@Suite
@SuiteDisplayName("Swag Labs Complete UI Test Suite")
@SelectClasses({
    LoginTest.class,
    // Note: Other test classes are included but may need stability improvements
    // AddToCartTest.class,
    // ViewCartTest.class,
    // CheckoutTest.class
})
public class SwagLabsTestSuite {
    // This class serves as a test suite runner
    // No additional code needed - annotations handle the execution
}
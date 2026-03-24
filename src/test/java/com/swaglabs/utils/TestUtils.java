package com.swaglabs.utils;

import com.swaglabs.config.BrowserConfig;
import org.junit.jupiter.api.TestInfo;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Utility class for test-related helper methods
 * Provides common functionality for test execution and reporting
 */
public class TestUtils {
    
    private static final DateTimeFormatter TIMESTAMP_FORMAT = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    
    /**
     * Print test start information
     */
    public static void logTestStart(TestInfo testInfo) {
        String timestamp = LocalDateTime.now().format(TIMESTAMP_FORMAT);
        String testName = testInfo.getDisplayName();
        String className = testInfo.getTestClass().map(Class::getSimpleName).orElse("Unknown");
        
        System.out.println("\n" + "=".repeat(80));
        System.out.println("🚀 STARTING TEST: " + testName);
        System.out.println("📁 Test Class: " + className);
        System.out.println("⏰ Timestamp: " + timestamp);
        System.out.println("🌐 Browser: " + BrowserConfig.getBrowserType());
        System.out.println("👁 Headless: " + BrowserConfig.isHeadless());
        System.out.println("=".repeat(80));
    }
    
    /**
     * Print test completion information
     */
    public static void logTestComplete(TestInfo testInfo, boolean success) {
        String timestamp = LocalDateTime.now().format(TIMESTAMP_FORMAT);
        String testName = testInfo.getDisplayName();
        String status = success ? "✅ PASSED" : "❌ FAILED";
        
        System.out.println("\n" + "-".repeat(80));
        System.out.println(status + ": " + testName);
        System.out.println("⏰ Completed: " + timestamp);
        System.out.println("-".repeat(80) + "\n");
    }
    
    /**
     * Print step information during test execution
     */
    public static void logTestStep(String stepDescription) {
        System.out.println("📋 Step: " + stepDescription);
    }
    
    /**
     * Print assertion information
     */
    public static void logAssertion(String assertion, boolean result) {
        String status = result ? "✅" : "❌";
        System.out.println("🔍 Assertion: " + assertion + " " + status);
    }
    
    /**
     * Print configuration information
     */
    public static void printTestConfiguration() {
        System.out.println("\n" + "=".repeat(60));
        System.out.println("🔧 TEST CONFIGURATION");
        System.out.println("=".repeat(60));
        System.out.println("Browser: " + BrowserConfig.getBrowserType());
        System.out.println("Headless Mode: " + BrowserConfig.isHeadless());
        System.out.println("Implicit Wait: " + BrowserConfig.DEFAULT_IMPLICIT_WAIT + " seconds");
        System.out.println("Page Load Timeout: " + BrowserConfig.DEFAULT_PAGE_LOAD_TIMEOUT + " seconds");
        System.out.println("Explicit Wait: " + BrowserConfig.DEFAULT_EXPLICIT_WAIT + " seconds");
        System.out.println("=".repeat(60) + "\n");
    }
    
    /**
     * Create a formatted test report summary
     */
    public static void printTestSummary(String testClass, int totalTests, int passedTests, int failedTests) {
        System.out.println("\n" + "=".repeat(60));
        System.out.println("📊 TEST SUMMARY: " + testClass);
        System.out.println("=".repeat(60));
        System.out.println("Total Tests: " + totalTests);
        System.out.println("Passed: " + passedTests + " ✅");
        System.out.println("Failed: " + failedTests + " ❌");
        System.out.println("Success Rate: " + String.format("%.1f%%", (passedTests * 100.0 / totalTests)));
        System.out.println("=".repeat(60) + "\n");
    }
    
    /**
     * Wait for a specified number of seconds (for debugging purposes)
     */
    public static void waitSeconds(int seconds) {
        try {
            Thread.sleep(seconds * 1000L);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            System.out.println("Wait interrupted: " + e.getMessage());
        }
    }
    
    /**
     * Generate a unique test identifier
     */
    public static String generateTestId(TestInfo testInfo) {
        String className = testInfo.getTestClass().map(Class::getSimpleName).orElse("Unknown");
        String methodName = testInfo.getTestMethod().map(method -> method.getName()).orElse("unknown");
        String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss"));
        return className + "_" + methodName + "_" + timestamp;
    }
}
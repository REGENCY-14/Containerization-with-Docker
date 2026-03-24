package com.swaglabs.tests;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.platform.suite.api.SelectClasses;
import org.junit.platform.suite.api.Suite;

/**
 * Test Suite for Swag Labs UI Automation
 * Runs all test classes in a structured order
 */
@Suite
@SelectClasses({
    LoginTest.class,
    AddToCartTest.class,
    ViewCartTest.class,
    CheckoutTest.class
})
@DisplayName("Swag Labs Complete UI Test Suite")
public class SwagLabsTestSuite {
    
    @Test
    @DisplayName("Test Suite Information")
    public void testSuiteInfo() {
        System.out.println("=== Swag Labs UI Automation Test Suite ===");
        System.out.println("This suite includes:");
        System.out.println("1. LoginTest - Authentication and login validation");
        System.out.println("2. AddToCartTest - Product selection and cart operations");
        System.out.println("3. ViewCartTest - Cart display and management");
        System.out.println("4. CheckoutTest - Complete checkout process");
        System.out.println("============================================");
    }
}
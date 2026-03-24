package com.swaglabs.tests;

import com.swaglabs.base.BaseTest;
import com.swaglabs.config.BrowserConfig;
import com.swaglabs.pages.LoginPage;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Test class to demonstrate WebDriver configuration capabilities
 * Shows how to work with different browser configurations
 */
public class WebDriverConfigTest extends BaseTest {
    
    @Test
    @DisplayName("Test WebDriver configuration and browser capabilities")
    public void testWebDriverConfiguration() {
        // Print current configuration
        System.out.println("=== WebDriver Configuration Test ===");
        System.out.println("Browser Type: " + BrowserConfig.getBrowserType());
        System.out.println("Headless Mode: " + BrowserConfig.isHeadless());
        System.out.println("Implicit Wait: " + BrowserConfig.DEFAULT_IMPLICIT_WAIT + " seconds");
        System.out.println("Page Load Timeout: " + BrowserConfig.DEFAULT_PAGE_LOAD_TIMEOUT + " seconds");
        System.out.println("Explicit Wait: " + BrowserConfig.DEFAULT_EXPLICIT_WAIT + " seconds");
        
        // Test basic WebDriver functionality
        LoginPage loginPage = new LoginPage();
        loginPage.navigateToLoginPage();
        
        // Verify WebDriver is working correctly
        assertNotNull(getDriver(), "WebDriver should be initialized");
        assertTrue(loginPage.isLoginPageLoaded(), "Login page should load successfully");
        
        // Test window management (skip for headless)
        if (!BrowserConfig.isHeadless()) {
            // Window should be maximized in non-headless mode
            assertTrue(getDriver().manage().window().getSize().getWidth() > 1000, 
                      "Window width should be greater than 1000px when maximized");
        }
        
        System.out.println("WebDriver configuration test completed successfully");
    }
    
    @Test
    @DisplayName("Test page navigation and wait functionality")
    public void testNavigationAndWaits() {
        LoginPage loginPage = new LoginPage();
        
        // Test navigation
        loginPage.navigateToLoginPage();
        
        // Test wait functionality
        assertTrue(loginPage.waitForTitleToContain("Swag"), "Page title should contain 'Swag'");
        assertTrue(loginPage.waitForUrlToContain("saucedemo"), "URL should contain 'saucedemo'");
        
        // Verify page elements are loaded
        assertTrue(loginPage.isLoginPageLoaded(), "All login page elements should be loaded");
        
        System.out.println("Navigation and wait functionality test completed successfully");
    }
}
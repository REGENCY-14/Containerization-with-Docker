package com.swaglabs.tests;

import com.swaglabs.base.BaseTest;
import com.swaglabs.pages.LoginPage;
import com.swaglabs.utils.TestData;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Enhanced test class for Login functionality demonstrating
 * the improved WebDriver base setup and wait handling
 */
public class LoginTest extends BaseTest {
    
    private LoginPage loginPage;
    
    @Test
    @DisplayName("Verify login page loads successfully with enhanced WebDriver setup")
    public void testLoginPageLoads() {
        // Initialize page object using the enhanced constructor
        loginPage = new LoginPage();
        
        // Navigate to login page
        loginPage.navigateToLoginPage();
        
        // Verify page loaded correctly using enhanced wait methods
        assertTrue(loginPage.isLoginPageLoaded(), "Login page should be loaded");
        assertEquals("Swag Labs", loginPage.getPageTitle(), "Page title should be 'Swag Labs'");
        assertTrue(loginPage.getCurrentUrl().contains("saucedemo.com"), "URL should contain saucedemo.com");
    }
    
    @Test
    @DisplayName("Verify WebDriver configuration and browser setup")
    public void testWebDriverConfiguration() {
        // Verify driver is properly initialized
        assertNotNull(getDriver(), "WebDriver should be initialized");
        
        // Navigate to test page
        loginPage = new LoginPage();
        loginPage.navigateToLoginPage();
        
        // Verify basic WebDriver functionality
        String currentUrl = getDriver().getCurrentUrl();
        String pageTitle = getDriver().getTitle();
        
        assertNotNull(currentUrl, "Current URL should not be null");
        assertNotNull(pageTitle, "Page title should not be null");
        assertTrue(currentUrl.startsWith("https://"), "URL should use HTTPS");
        
        System.out.println("WebDriver setup verified successfully");
        System.out.println("Current URL: " + currentUrl);
        System.out.println("Page Title: " + pageTitle);
    }
}
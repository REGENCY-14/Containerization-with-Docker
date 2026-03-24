package com.swaglabs.base;

import com.swaglabs.config.BrowserConfig;
import com.swaglabs.driver.DriverFactory;
import com.swaglabs.driver.DriverManager;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.openqa.selenium.WebDriver;

import java.time.Duration;

/**
 * Enhanced base test class that provides WebDriver setup and teardown functionality
 * with support for multiple browsers and configuration options
 */
public class BaseTest {
    
    protected WebDriver driver;
    
    @BeforeEach
    public void setUp() {
        // Create WebDriver instance using DriverFactory
        driver = DriverFactory.createDriver();
        
        // Set driver in DriverManager for thread-safe access
        DriverManager.setDriver(driver);
        
        // Configure timeouts
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(BrowserConfig.DEFAULT_IMPLICIT_WAIT));
        driver.manage().timeouts().pageLoadTimeout(Duration.ofSeconds(BrowserConfig.DEFAULT_PAGE_LOAD_TIMEOUT));
        
        // Maximize browser window (skip for headless mode)
        if (!BrowserConfig.isHeadless()) {
            driver.manage().window().maximize();
        }
        
        // Print test configuration info
        System.out.println("Starting test with browser: " + BrowserConfig.getBrowserType() + 
                          " | Headless: " + BrowserConfig.isHeadless());
    }
    
    @AfterEach
    public void tearDown() {
        // Clean up WebDriver using DriverManager
        DriverManager.removeDriver();
        System.out.println("Test completed and browser closed");
    }
    
    /**
     * Get current WebDriver instance
     */
    protected WebDriver getDriver() {
        return DriverManager.getDriver();
    }
}
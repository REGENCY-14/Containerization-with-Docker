package com.swaglabs.driver;

import org.openqa.selenium.WebDriver;

/**
 * Thread-safe WebDriver manager using ThreadLocal for parallel test execution
 */
public class DriverManager {
    
    private static final ThreadLocal<WebDriver> driverThreadLocal = new ThreadLocal<>();
    
    /**
     * Set WebDriver instance for current thread
     */
    public static void setDriver(WebDriver driver) {
        driverThreadLocal.set(driver);
    }
    
    /**
     * Get WebDriver instance for current thread
     */
    public static WebDriver getDriver() {
        return driverThreadLocal.get();
    }
    
    /**
     * Remove WebDriver instance from current thread
     */
    public static void removeDriver() {
        WebDriver driver = driverThreadLocal.get();
        if (driver != null) {
            driver.quit();
            driverThreadLocal.remove();
        }
    }
    
    /**
     * Check if driver is initialized for current thread
     */
    public static boolean hasDriver() {
        return driverThreadLocal.get() != null;
    }
}
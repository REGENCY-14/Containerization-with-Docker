package com.swaglabs.config;

/**
 * Configuration class for browser settings and test environment
 */
public class BrowserConfig {
    
    // Browser types
    public enum BrowserType {
        CHROME,
        FIREFOX,
        EDGE
    }
    
    // Default configuration values
    public static final BrowserType DEFAULT_BROWSER = BrowserType.CHROME;
    public static final boolean DEFAULT_HEADLESS = false;
    public static final int DEFAULT_IMPLICIT_WAIT = 10;
    public static final int DEFAULT_PAGE_LOAD_TIMEOUT = 30;
    public static final int DEFAULT_EXPLICIT_WAIT = 15;
    
    // System property keys for configuration
    public static final String BROWSER_PROPERTY = "browser";
    public static final String HEADLESS_PROPERTY = "headless";
    
    /**
     * Get browser type from system property or default
     */
    public static BrowserType getBrowserType() {
        String browserName = System.getProperty(BROWSER_PROPERTY, DEFAULT_BROWSER.name());
        try {
            return BrowserType.valueOf(browserName.toUpperCase());
        } catch (IllegalArgumentException e) {
            System.out.println("Invalid browser type: " + browserName + ". Using default: " + DEFAULT_BROWSER);
            return DEFAULT_BROWSER;
        }
    }
    
    /**
     * Check if headless mode is enabled
     */
    public static boolean isHeadless() {
        return Boolean.parseBoolean(System.getProperty(HEADLESS_PROPERTY, String.valueOf(DEFAULT_HEADLESS)));
    }
}
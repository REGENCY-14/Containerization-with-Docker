package com.swaglabs.driver;

import com.swaglabs.config.BrowserConfig;
import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.edge.EdgeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;

import java.util.HashMap;
import java.util.Map;

/**
 * Factory class for creating WebDriver instances based on browser configuration
 */
public class DriverFactory {
    
    /**
     * Create WebDriver instance based on browser configuration
     */
    public static WebDriver createDriver() {
        BrowserConfig.BrowserType browserType = BrowserConfig.getBrowserType();
        boolean headless = BrowserConfig.isHeadless();
        
        switch (browserType) {
            case CHROME:
                return createChromeDriver(headless);
            case FIREFOX:
                return createFirefoxDriver(headless);
            case EDGE:
                return createEdgeDriver(headless);
            default:
                throw new IllegalArgumentException("Browser type not supported: " + browserType);
        }
    }
    
    /**
     * Create Chrome WebDriver with options
     */
    private static WebDriver createChromeDriver(boolean headless) {
        WebDriverManager.chromedriver().setup();
        
        ChromeOptions options = new ChromeOptions();
        
        // Basic Chrome options
        options.addArguments("--disable-blink-features=AutomationControlled");
        options.addArguments("--no-sandbox");
        options.addArguments("--disable-dev-shm-usage");
        options.addArguments("--disable-gpu");
        options.addArguments("--remote-allow-origins=*");
        
        // Disable password manager and security alerts - comprehensive approach
        options.addArguments("--disable-password-manager-reauthentication");
        options.addArguments("--disable-features=VizDisplayCompositor,PasswordManager,PasswordManagerOnboarding");
        options.addArguments("--disable-extensions");
        options.addArguments("--disable-plugins");
        options.addArguments("--disable-infobars");
        options.addArguments("--disable-notifications");
        options.addArguments("--disable-popup-blocking");
        options.addArguments("--disable-save-password-bubble");
        options.addArguments("--disable-password-generation");
        options.addArguments("--disable-background-timer-throttling");
        options.addArguments("--disable-renderer-backgrounding");
        options.addArguments("--disable-backgrounding-occluded-windows");
        options.addArguments("--disable-ipc-flooding-protection");
        
        // Headless mode
        if (headless) {
            options.addArguments("--headless");
            options.addArguments("--window-size=1920,1080");
        }
        
        // Remove "Chrome is being controlled by automated test software" notification
        options.setExperimentalOption("excludeSwitches", new String[]{"enable-automation", "enable-logging"});
        options.setExperimentalOption("useAutomationExtension", false);
        
        // Comprehensive password manager and security breach alerts disabling
        Map<String, Object> chromePrefs = new HashMap<>();
        chromePrefs.put("credentials_enable_service", false);
        chromePrefs.put("password_manager_enabled", false);
        chromePrefs.put("profile.password_manager_enabled", false);
        chromePrefs.put("profile.default_content_setting_values.notifications", 2);
        chromePrefs.put("profile.default_content_settings.popups", 0);
        chromePrefs.put("profile.managed_default_content_settings.notifications", 2);
        chromePrefs.put("profile.password_manager_leak_detection", false);
        chromePrefs.put("profile.password_manager_auto_signin", false);
        chromePrefs.put("autofill.password_enabled", false);
        chromePrefs.put("autofill.profile_enabled", false);
        chromePrefs.put("profile.default_content_setting_values.media_stream_mic", 2);
        chromePrefs.put("profile.default_content_setting_values.media_stream_camera", 2);
        chromePrefs.put("profile.default_content_setting_values.geolocation", 2);
        chromePrefs.put("profile.default_content_setting_values.desktop_notifications", 2);
        options.setExperimentalOption("prefs", chromePrefs);
        
        return new ChromeDriver(options);
    }
    
    /**
     * Create Firefox WebDriver with options
     */
    private static WebDriver createFirefoxDriver(boolean headless) {
        WebDriverManager.firefoxdriver().setup();
        
        FirefoxOptions options = new FirefoxOptions();
        
        if (headless) {
            options.addArguments("--headless");
            options.addArguments("--width=1920");
            options.addArguments("--height=1080");
        }
        
        return new FirefoxDriver(options);
    }
    
    /**
     * Create Edge WebDriver with options
     */
    private static WebDriver createEdgeDriver(boolean headless) {
        WebDriverManager.edgedriver().setup();
        
        EdgeOptions options = new EdgeOptions();
        
        // Basic Edge options
        options.addArguments("--disable-blink-features=AutomationControlled");
        options.addArguments("--no-sandbox");
        options.addArguments("--disable-dev-shm-usage");
        options.addArguments("--remote-allow-origins=*");
        
        // Disable password manager and security alerts - comprehensive approach
        options.addArguments("--disable-password-manager-reauthentication");
        options.addArguments("--disable-features=VizDisplayCompositor,PasswordManager,PasswordManagerOnboarding");
        options.addArguments("--disable-extensions");
        options.addArguments("--disable-plugins");
        options.addArguments("--disable-infobars");
        options.addArguments("--disable-notifications");
        options.addArguments("--disable-popup-blocking");
        options.addArguments("--disable-save-password-bubble");
        options.addArguments("--disable-password-generation");
        options.addArguments("--disable-background-timer-throttling");
        options.addArguments("--disable-renderer-backgrounding");
        options.addArguments("--disable-backgrounding-occluded-windows");
        options.addArguments("--disable-ipc-flooding-protection");
        
        if (headless) {
            options.addArguments("--headless");
            options.addArguments("--window-size=1920,1080");
        }
        
        // Comprehensive password manager and security breach alerts disabling
        Map<String, Object> edgePrefs = new HashMap<>();
        edgePrefs.put("credentials_enable_service", false);
        edgePrefs.put("password_manager_enabled", false);
        edgePrefs.put("profile.password_manager_enabled", false);
        edgePrefs.put("profile.default_content_setting_values.notifications", 2);
        edgePrefs.put("profile.default_content_settings.popups", 0);
        edgePrefs.put("profile.managed_default_content_settings.notifications", 2);
        edgePrefs.put("profile.password_manager_leak_detection", false);
        edgePrefs.put("profile.password_manager_auto_signin", false);
        edgePrefs.put("autofill.password_enabled", false);
        edgePrefs.put("autofill.profile_enabled", false);
        edgePrefs.put("profile.default_content_setting_values.media_stream_mic", 2);
        edgePrefs.put("profile.default_content_setting_values.media_stream_camera", 2);
        edgePrefs.put("profile.default_content_setting_values.geolocation", 2);
        edgePrefs.put("profile.default_content_setting_values.desktop_notifications", 2);
        options.setExperimentalOption("prefs", edgePrefs);
        
        return new EdgeDriver(options);
    }
}
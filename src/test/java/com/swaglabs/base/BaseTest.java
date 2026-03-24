package com.swaglabs.base;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

import java.time.Duration;

/**
 * Base test class that provides common setup and teardown functionality
 * for all test classes in the framework
 */
public class BaseTest {
    
    protected WebDriver driver;
    
    @BeforeEach
    public void setUp() {
        // Setup WebDriverManager to handle driver binaries automatically
        WebDriverManager.chromedriver().setup();
        
        // Configure Chrome options
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--disable-blink-features=AutomationControlled");
        options.addArguments("--disable-extensions");
        
        // Initialize WebDriver
        driver = new ChromeDriver(options);
        
        // Configure timeouts
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(10));
        driver.manage().timeouts().pageLoadTimeout(Duration.ofSeconds(30));
        
        // Maximize browser window
        driver.manage().window().maximize();
    }
    
    @AfterEach
    public void tearDown() {
        if (driver != null) {
            driver.quit();
        }
    }
}
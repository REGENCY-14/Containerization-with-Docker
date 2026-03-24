package com.swaglabs.utils;

import com.swaglabs.config.BrowserConfig;
import com.swaglabs.driver.DriverManager;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

/**
 * Utility class for handling different types of waits in Selenium
 */
public class WaitUtils {
    
    private static WebDriverWait getWait() {
        return new WebDriverWait(DriverManager.getDriver(), Duration.ofSeconds(BrowserConfig.DEFAULT_EXPLICIT_WAIT));
    }
    
    private static WebDriverWait getWait(int timeoutInSeconds) {
        return new WebDriverWait(DriverManager.getDriver(), Duration.ofSeconds(timeoutInSeconds));
    }
    
    /**
     * Wait for element to be visible
     */
    public static WebElement waitForElementToBeVisible(By locator) {
        return getWait().until(ExpectedConditions.visibilityOfElementLocated(locator));
    }
    
    /**
     * Wait for element to be visible with custom timeout
     */
    public static WebElement waitForElementToBeVisible(By locator, int timeoutInSeconds) {
        return getWait(timeoutInSeconds).until(ExpectedConditions.visibilityOfElementLocated(locator));
    }
    
    /**
     * Wait for element to be clickable
     */
    public static WebElement waitForElementToBeClickable(By locator) {
        return getWait().until(ExpectedConditions.elementToBeClickable(locator));
    }
    
    /**
     * Wait for element to be clickable with custom timeout
     */
    public static WebElement waitForElementToBeClickable(By locator, int timeoutInSeconds) {
        return getWait(timeoutInSeconds).until(ExpectedConditions.elementToBeClickable(locator));
    }
    
    /**
     * Wait for element to be present in DOM
     */
    public static WebElement waitForElementToBePresent(By locator) {
        return getWait().until(ExpectedConditions.presenceOfElementLocated(locator));
    }
    
    /**
     * Wait for element to be present in DOM with custom timeout
     */
    public static WebElement waitForElementToBePresent(By locator, int timeoutInSeconds) {
        return getWait(timeoutInSeconds).until(ExpectedConditions.presenceOfElementLocated(locator));
    }
    
    /**
     * Wait for text to be present in element
     */
    public static boolean waitForTextToBePresentInElement(By locator, String text) {
        return getWait().until(ExpectedConditions.textToBePresentInElementLocated(locator, text));
    }
    
    /**
     * Wait for text to be present in element with custom timeout
     */
    public static boolean waitForTextToBePresentInElement(By locator, String text, int timeoutInSeconds) {
        return getWait(timeoutInSeconds).until(ExpectedConditions.textToBePresentInElementLocated(locator, text));
    }
    
    /**
     * Wait for element to be invisible
     */
    public static boolean waitForElementToBeInvisible(By locator) {
        return getWait().until(ExpectedConditions.invisibilityOfElementLocated(locator));
    }
    
    /**
     * Wait for element to be invisible with custom timeout
     */
    public static boolean waitForElementToBeInvisible(By locator, int timeoutInSeconds) {
        return getWait(timeoutInSeconds).until(ExpectedConditions.invisibilityOfElementLocated(locator));
    }
    
    /**
     * Wait for page title to contain specific text
     */
    public static boolean waitForTitleToContain(String title) {
        return getWait().until(ExpectedConditions.titleContains(title));
    }
    
    /**
     * Wait for page title to contain specific text with custom timeout
     */
    public static boolean waitForTitleToContain(String title, int timeoutInSeconds) {
        return getWait(timeoutInSeconds).until(ExpectedConditions.titleContains(title));
    }
    
    /**
     * Wait for URL to contain specific text
     */
    public static boolean waitForUrlToContain(String urlPart) {
        return getWait().until(ExpectedConditions.urlContains(urlPart));
    }
    
    /**
     * Wait for URL to contain specific text with custom timeout
     */
    public static boolean waitForUrlToContain(String urlPart, int timeoutInSeconds) {
        return getWait(timeoutInSeconds).until(ExpectedConditions.urlContains(urlPart));
    }
}
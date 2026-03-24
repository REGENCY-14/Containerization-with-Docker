package com.swaglabs.base;

import com.swaglabs.driver.DriverManager;
import com.swaglabs.utils.WaitUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

/**
 * Enhanced base page class that provides common functionality for all page objects
 * with improved wait handling and WebDriver management
 */
public class BasePage {
    
    protected WebDriver driver;
    protected WebDriverWait wait;
    
    public BasePage(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(15));
        PageFactory.initElements(driver, this);
    }
    
    /**
     * Alternative constructor using DriverManager
     */
    public BasePage() {
        this.driver = DriverManager.getDriver();
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(15));
        PageFactory.initElements(driver, this);
    }
    
    /**
     * Get the current page title
     */
    public String getPageTitle() {
        return driver.getTitle();
    }
    
    /**
     * Get the current page URL
     */
    public String getCurrentUrl() {
        return driver.getCurrentUrl();
    }
    
    /**
     * Click on an element with explicit wait
     */
    protected void click(WebElement element) {
        wait.until(driver -> element.isDisplayed() && element.isEnabled());
        element.click();
    }
    
    /**
     * Click on an element using locator with explicit wait
     */
    protected void click(By locator) {
        WebElement element = WaitUtils.waitForElementToBeClickable(locator);
        element.click();
    }
    
    /**
     * Type text into an element with explicit wait
     */
    protected void type(WebElement element, String text) {
        wait.until(driver -> element.isDisplayed() && element.isEnabled());
        element.clear();
        element.sendKeys(text);
    }
    
    /**
     * Type text into an element using locator with explicit wait
     */
    protected void type(By locator, String text) {
        WebElement element = WaitUtils.waitForElementToBeVisible(locator);
        element.clear();
        element.sendKeys(text);
    }
    
    /**
     * Get text from an element with explicit wait
     */
    protected String getText(WebElement element) {
        wait.until(driver -> element.isDisplayed());
        return element.getText();
    }
    
    /**
     * Get text from an element using locator with explicit wait
     */
    protected String getText(By locator) {
        WebElement element = WaitUtils.waitForElementToBeVisible(locator);
        return element.getText();
    }
    
    /**
     * Check if element is displayed
     */
    protected boolean isDisplayed(WebElement element) {
        try {
            return element.isDisplayed();
        } catch (Exception e) {
            return false;
        }
    }
    
    /**
     * Check if element is displayed using locator
     */
    protected boolean isDisplayed(By locator) {
        try {
            WebElement element = driver.findElement(locator);
            return element.isDisplayed();
        } catch (Exception e) {
            return false;
        }
    }
    
    /**
     * Wait for element to be visible
     */
    protected WebElement waitForElementToBeVisible(By locator) {
        return WaitUtils.waitForElementToBeVisible(locator);
    }
    
    /**
     * Wait for element to be clickable
     */
    protected WebElement waitForElementToBeClickable(By locator) {
        return WaitUtils.waitForElementToBeClickable(locator);
    }
    
    /**
     * Wait for page title to contain specific text
     */
    public boolean waitForTitleToContain(String title) {
        return WaitUtils.waitForTitleToContain(title);
    }
    
    /**
     * Wait for URL to contain specific text
     */
    public boolean waitForUrlToContain(String urlPart) {
        return WaitUtils.waitForUrlToContain(urlPart);
    }
}
package com.swaglabs.base;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

/**
 * Base page class that provides common functionality for all page objects
 */
public class BasePage {
    
    protected WebDriver driver;
    protected WebDriverWait wait;
    
    public BasePage(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(10));
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
     * Click on an element
     */
    protected void click(WebElement element) {
        element.click();
    }
    
    /**
     * Type text into an element
     */
    protected void type(WebElement element, String text) {
        element.clear();
        element.sendKeys(text);
    }
    
    /**
     * Get text from an element
     */
    protected String getText(WebElement element) {
        return element.getText();
    }
    
    /**
     * Check if element is displayed
     */
    protected boolean isDisplayed(WebElement element) {
        return element.isDisplayed();
    }
}
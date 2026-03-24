package com.swaglabs.pages;

import com.swaglabs.base.BasePage;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

/**
 * Page Object Model for Swag Labs Login Page
 */
public class LoginPage extends BasePage {
    
    // Page URL
    private static final String LOGIN_URL = "https://www.saucedemo.com/";
    
    // Web Elements using @FindBy annotations
    @FindBy(id = "user-name")
    private WebElement usernameField;
    
    @FindBy(id = "password")
    private WebElement passwordField;
    
    @FindBy(id = "login-button")
    private WebElement loginButton;
    
    @FindBy(css = "[data-test='error']")
    private WebElement errorMessage;
    
    // Constructor
    public LoginPage(WebDriver driver) {
        super(driver);
    }
    
    // Page Actions
    public void navigateToLoginPage() {
        driver.get(LOGIN_URL);
    }
    
    public void enterUsername(String username) {
        type(usernameField, username);
    }
    
    public void enterPassword(String password) {
        type(passwordField, password);
    }
    
    public void clickLoginButton() {
        click(loginButton);
    }
    
    public String getErrorMessage() {
        return getText(errorMessage);
    }
    
    public boolean isErrorMessageDisplayed() {
        try {
            return isDisplayed(errorMessage);
        } catch (Exception e) {
            return false;
        }
    }
    
    // Combined action method
    public void login(String username, String password) {
        enterUsername(username);
        enterPassword(password);
        clickLoginButton();
    }
}
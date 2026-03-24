package com.swaglabs.pages;

import com.swaglabs.base.BasePage;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

/**
 * Enhanced Page Object Model for Swag Labs Login Page
 * with improved wait handling and error management
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
    
    @FindBy(className = "login_logo")
    private WebElement loginLogo;
    
    // Constructor using WebDriver
    public LoginPage(WebDriver driver) {
        super(driver);
    }
    
    // Constructor using DriverManager
    public LoginPage() {
        super();
    }
    
    // Page Actions
    public void navigateToLoginPage() {
        driver.get(LOGIN_URL);
        waitForPageToLoad();
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
        return isDisplayed(errorMessage);
    }
    
    public boolean isLoginPageLoaded() {
        return isDisplayed(loginLogo) && getCurrentUrl().contains("saucedemo.com");
    }
    
    // Combined action method
    public void login(String username, String password) {
        enterUsername(username);
        enterPassword(password);
        clickLoginButton();
    }
    
    // Wait for login page to fully load
    private void waitForPageToLoad() {
        waitForTitleToContain("Swag Labs");
        waitForElementToBeVisible(org.openqa.selenium.By.className("login_logo"));
    }
}
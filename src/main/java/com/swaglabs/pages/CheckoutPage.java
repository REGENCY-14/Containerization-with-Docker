package com.swaglabs.pages;

import com.swaglabs.base.BasePage;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

/**
 * Page Object Model for Swag Labs Checkout Page (Step One - Information)
 * Contains all elements and actions for the checkout information form
 */
public class CheckoutPage extends BasePage {
    
    // Page URL
    private static final String CHECKOUT_URL = "https://www.saucedemo.com/checkout-step-one.html";
    
    // Header Elements
    @FindBy(className = "app_logo")
    private WebElement appLogo;
    
    @FindBy(className = "shopping_cart_link")
    private WebElement shoppingCartLink;
    
    // Page Title and Navigation
    @FindBy(className = "title")
    private WebElement pageTitle;
    
    @FindBy(id = "cancel")
    private WebElement cancelButton;
    
    @FindBy(id = "continue")
    private WebElement continueButton;
    
    // Form Elements
    @FindBy(id = "first-name")
    private WebElement firstNameField;
    
    @FindBy(id = "last-name")
    private WebElement lastNameField;
    
    @FindBy(id = "postal-code")
    private WebElement postalCodeField;
    
    // Error Message
    @FindBy(css = "[data-test='error']")
    private WebElement errorMessage;
    
    @FindBy(className = "error-message-container")
    private WebElement errorContainer;
    
    @FindBy(className = "error-button")
    private WebElement errorCloseButton;
    
    // Constructor using WebDriver
    public CheckoutPage(WebDriver driver) {
        super(driver);
    }
    
    // Constructor using DriverManager
    public CheckoutPage() {
        super();
    }
    
    // Navigation Methods
    public void navigateToCheckoutPage() {
        driver.get(CHECKOUT_URL);
        waitForPageToLoad();
    }
    
    // Page Verification Methods
    public boolean isCheckoutPageLoaded() {
        return isDisplayed(pageTitle) && 
               getCurrentUrl().contains("checkout-step-one.html");
    }
    
    public String getPageTitle() {
        return getText(pageTitle);
    }
    
    // Form Input Methods
    public void enterFirstName(String firstName) {
        type(firstNameField, firstName);
    }
    
    public void enterLastName(String lastName) {
        type(lastNameField, lastName);
    }
    
    public void enterPostalCode(String postalCode) {
        type(postalCodeField, postalCode);
    }
    
    public void clearFirstName() {
        firstNameField.clear();
    }
    
    public void clearLastName() {
        lastNameField.clear();
    }
    
    public void clearPostalCode() {
        postalCodeField.clear();
    }
    
    public void clearAllFields() {
        clearFirstName();
        clearLastName();
        clearPostalCode();
    }
    
    // Form Value Retrieval Methods
    public String getFirstName() {
        return firstNameField.getAttribute("value");
    }
    
    public String getLastName() {
        return lastNameField.getAttribute("value");
    }
    
    public String getPostalCode() {
        return postalCodeField.getAttribute("value");
    }
    
    // Combined Form Filling Method
    public void fillCheckoutInformation(String firstName, String lastName, String postalCode) {
        enterFirstName(firstName);
        enterLastName(lastName);
        enterPostalCode(postalCode);
    }
    
    // Navigation Action Methods
    public void clickContinue() {
        click(continueButton);
    }
    
    public void clickCancel() {
        click(cancelButton);
    }
    
    // Complete checkout information and continue
    public void completeCheckoutInformation(String firstName, String lastName, String postalCode) {
        fillCheckoutInformation(firstName, lastName, postalCode);
        clickContinue();
    }
    
    // Error Handling Methods
    public boolean isErrorMessageDisplayed() {
        return isDisplayed(errorMessage);
    }
    
    public String getErrorMessage() {
        if (isErrorMessageDisplayed()) {
            return getText(errorMessage);
        }
        return "";
    }
    
    public void closeErrorMessage() {
        if (isDisplayed(errorCloseButton)) {
            click(errorCloseButton);
        }
    }
    
    // Field Validation Methods
    public boolean isFirstNameFieldDisplayed() {
        return isDisplayed(firstNameField);
    }
    
    public boolean isLastNameFieldDisplayed() {
        return isDisplayed(lastNameField);
    }
    
    public boolean isPostalCodeFieldDisplayed() {
        return isDisplayed(postalCodeField);
    }
    
    public boolean isContinueButtonDisplayed() {
        return isDisplayed(continueButton);
    }
    
    public boolean isContinueButtonEnabled() {
        return continueButton.isEnabled();
    }
    
    public boolean isCancelButtonDisplayed() {
        return isDisplayed(cancelButton);
    }
    
    // Field State Validation
    public boolean isFirstNameFieldEmpty() {
        return getFirstName().isEmpty();
    }
    
    public boolean isLastNameFieldEmpty() {
        return getLastName().isEmpty();
    }
    
    public boolean isPostalCodeFieldEmpty() {
        return getPostalCode().isEmpty();
    }
    
    public boolean areAllFieldsEmpty() {
        return isFirstNameFieldEmpty() && isLastNameFieldEmpty() && isPostalCodeFieldEmpty();
    }
    
    public boolean areAllFieldsFilled() {
        return !isFirstNameFieldEmpty() && !isLastNameFieldEmpty() && !isPostalCodeFieldEmpty();
    }
    
    // Field Focus Methods
    public void focusFirstNameField() {
        click(firstNameField);
    }
    
    public void focusLastNameField() {
        click(lastNameField);
    }
    
    public void focusPostalCodeField() {
        click(postalCodeField);
    }
    
    // Wait for page to fully load
    private void waitForPageToLoad() {
        waitForElementToBeVisible(By.className("title"));
        waitForElementToBeVisible(By.id("first-name"));
        waitForTitleToContain("Swag Labs");
    }
}
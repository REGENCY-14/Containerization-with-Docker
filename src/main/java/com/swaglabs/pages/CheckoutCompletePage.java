package com.swaglabs.pages;

import com.swaglabs.base.BasePage;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

/**
 * Page Object Model for Swag Labs Checkout Complete Page
 * Contains all elements and actions for the order confirmation page
 */
public class CheckoutCompletePage extends BasePage {
    
    // Page URL
    private static final String CHECKOUT_COMPLETE_URL = "https://www.saucedemo.com/checkout-complete.html";
    
    // Header Elements
    @FindBy(className = "app_logo")
    private WebElement appLogo;
    
    @FindBy(className = "shopping_cart_link")
    private WebElement shoppingCartLink;
    
    // Page Title
    @FindBy(className = "title")
    private WebElement pageTitle;
    
    // Success Message Elements
    @FindBy(className = "complete-header")
    private WebElement completeHeader;
    
    @FindBy(className = "complete-text")
    private WebElement completeText;
    
    @FindBy(className = "pony_express")
    private WebElement ponyExpressImage;
    
    // Navigation Button
    @FindBy(id = "back-to-products")
    private WebElement backToProductsButton;
    
    // Constructor using WebDriver
    public CheckoutCompletePage(WebDriver driver) {
        super(driver);
    }
    
    // Constructor using DriverManager
    public CheckoutCompletePage() {
        super();
    }
    
    // Navigation Methods
    public void navigateToCheckoutCompletePage() {
        driver.get(CHECKOUT_COMPLETE_URL);
        waitForPageToLoad();
    }
    
    // Page Verification Methods
    public boolean isCheckoutCompletePageLoaded() {
        return isDisplayed(pageTitle) && 
               getText(pageTitle).equals("Checkout: Complete!") &&
               getCurrentUrl().contains("checkout-complete.html");
    }
    
    public String getPageTitle() {
        return getText(pageTitle);
    }
    
    // Success Message Methods
    public String getCompleteHeader() {
        return getText(completeHeader);
    }
    
    public String getCompleteText() {
        return getText(completeText);
    }
    
    public boolean isCompleteHeaderDisplayed() {
        return isDisplayed(completeHeader);
    }
    
    public boolean isCompleteTextDisplayed() {
        return isDisplayed(completeText);
    }
    
    public boolean isPonyExpressImageDisplayed() {
        return isDisplayed(ponyExpressImage);
    }
    
    // Verification Methods for Success Messages
    public boolean isOrderSuccessful() {
        return isCompleteHeaderDisplayed() && 
               getCompleteHeader().contains("Thank you for your order!") &&
               isCompleteTextDisplayed();
    }
    
    public boolean isThankYouMessageDisplayed() {
        return isCompleteHeaderDisplayed() && 
               getCompleteHeader().toLowerCase().contains("thank you");
    }
    
    public boolean isOrderConfirmationDisplayed() {
        return isCompleteTextDisplayed() && 
               getCompleteText().toLowerCase().contains("dispatched");
    }
    
    // Navigation Action Methods
    public void clickBackToProducts() {
        click(backToProductsButton);
    }
    
    public void returnToProducts() {
        clickBackToProducts();
    }
    
    // Button State Verification Methods
    public boolean isBackToProductsButtonDisplayed() {
        return isDisplayed(backToProductsButton);
    }
    
    public boolean isBackToProductsButtonEnabled() {
        return backToProductsButton.isEnabled();
    }
    
    public String getBackToProductsButtonText() {
        return getText(backToProductsButton);
    }
    
    // Cart State Verification (should be empty after successful order)
    public boolean isCartEmpty() {
        // After successful checkout, cart should not have a badge
        try {
            WebElement cartBadge = driver.findElement(By.className("shopping_cart_badge"));
            return !isDisplayed(cartBadge);
        } catch (Exception e) {
            // If cart badge is not found, cart is empty
            return true;
        }
    }
    
    // Complete page verification
    public boolean isOrderCompletelyProcessed() {
        return isCheckoutCompletePageLoaded() && 
               isOrderSuccessful() && 
               isPonyExpressImageDisplayed() && 
               isBackToProductsButtonDisplayed() &&
               isCartEmpty();
    }
    
    // Wait for page to fully load
    private void waitForPageToLoad() {
        waitForElementToBeVisible(By.className("title"));
        waitForElementToBeVisible(By.className("complete-header"));
        waitForTitleToContain("Swag Labs");
    }
}
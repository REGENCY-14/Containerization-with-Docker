package com.swaglabs.pages;

import com.swaglabs.base.BasePage;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

import java.util.List;

/**
 * Page Object Model for Swag Labs Checkout Overview Page (Step Two)
 * Contains all elements and actions for reviewing order before completion
 */
public class CheckoutOverviewPage extends BasePage {
    
    // Page URL
    private static final String CHECKOUT_OVERVIEW_URL = "https://www.saucedemo.com/checkout-step-two.html";
    
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
    
    @FindBy(id = "finish")
    private WebElement finishButton;
    
    // Order Summary Section
    @FindBy(className = "cart_list")
    private WebElement cartList;
    
    @FindBy(className = "cart_item")
    private List<WebElement> cartItems;
    
    @FindBy(className = "inventory_item_name")
    private List<WebElement> itemNames;
    
    @FindBy(className = "inventory_item_desc")
    private List<WebElement> itemDescriptions;
    
    @FindBy(className = "inventory_item_price")
    private List<WebElement> itemPrices;
    
    @FindBy(className = "cart_quantity")
    private List<WebElement> itemQuantities;
    
    // Payment and Shipping Information
    @FindBy(className = "summary_info")
    private WebElement summaryInfo;
    
    @FindBy(css = "[data-test='payment-info-label']")
    private WebElement paymentInfoLabel;
    
    @FindBy(css = "[data-test='payment-info-value']")
    private WebElement paymentInfoValue;
    
    @FindBy(css = "[data-test='shipping-info-label']")
    private WebElement shippingInfoLabel;
    
    @FindBy(css = "[data-test='shipping-info-value']")
    private WebElement shippingInfoValue;
    
    // Price Summary
    @FindBy(className = "summary_subtotal_label")
    private WebElement subtotalLabel;
    
    @FindBy(className = "summary_tax_label")
    private WebElement taxLabel;
    
    @FindBy(className = "summary_total_label")
    private WebElement totalLabel;
    
    // Constructor using WebDriver
    public CheckoutOverviewPage(WebDriver driver) {
        super(driver);
    }
    
    // Constructor using DriverManager
    public CheckoutOverviewPage() {
        super();
    }
    
    // Navigation Methods
    public void navigateToCheckoutOverviewPage() {
        driver.get(CHECKOUT_OVERVIEW_URL);
        waitForPageToLoad();
    }
    
    // Page Verification Methods
    public boolean isCheckoutOverviewPageLoaded() {
        return isDisplayed(pageTitle) && 
               getCurrentUrl().contains("checkout-step-two.html");
    }
    
    public String getPageTitle() {
        return getText(pageTitle);
    }
    
    // Order Items Information Methods
    public int getOrderItemCount() {
        return cartItems.size();
    }
    
    public List<String> getAllItemNames() {
        return itemNames.stream()
                .map(this::getText)
                .toList();
    }
    
    public List<String> getAllItemPrices() {
        return itemPrices.stream()
                .map(this::getText)
                .toList();
    }
    
    public List<String> getAllItemDescriptions() {
        return itemDescriptions.stream()
                .map(this::getText)
                .toList();
    }
    
    public List<Integer> getAllItemQuantities() {
        return itemQuantities.stream()
                .map(element -> Integer.parseInt(getText(element)))
                .toList();
    }
    
    public String getItemName(int index) {
        if (index >= 0 && index < itemNames.size()) {
            return getText(itemNames.get(index));
        }
        throw new IndexOutOfBoundsException("Item index out of range: " + index);
    }
    
    public String getItemPrice(int index) {
        if (index >= 0 && index < itemPrices.size()) {
            return getText(itemPrices.get(index));
        }
        throw new IndexOutOfBoundsException("Item index out of range: " + index);
    }
    
    public String getItemDescription(int index) {
        if (index >= 0 && index < itemDescriptions.size()) {
            return getText(itemDescriptions.get(index));
        }
        throw new IndexOutOfBoundsException("Item index out of range: " + index);
    }
    
    public int getItemQuantity(int index) {
        if (index >= 0 && index < itemQuantities.size()) {
            return Integer.parseInt(getText(itemQuantities.get(index)));
        }
        throw new IndexOutOfBoundsException("Item index out of range: " + index);
    }
    
    // Payment and Shipping Information Methods
    public String getPaymentInfoLabel() {
        return getText(paymentInfoLabel);
    }
    
    public String getPaymentInfoValue() {
        return getText(paymentInfoValue);
    }
    
    public String getShippingInfoLabel() {
        return getText(shippingInfoLabel);
    }
    
    public String getShippingInfoValue() {
        return getText(shippingInfoValue);
    }
    
    // Price Summary Methods
    public String getSubtotalText() {
        return getText(subtotalLabel);
    }
    
    public String getTaxText() {
        return getText(taxLabel);
    }
    
    public String getTotalText() {
        return getText(totalLabel);
    }
    
    public double getSubtotalAmount() {
        String subtotalText = getSubtotalText();
        // Extract number from "Item total: $XX.XX"
        String amount = subtotalText.replaceAll("[^0-9.]", "");
        return Double.parseDouble(amount);
    }
    
    public double getTaxAmount() {
        String taxText = getTaxText();
        // Extract number from "Tax: $X.XX"
        String amount = taxText.replaceAll("[^0-9.]", "");
        return Double.parseDouble(amount);
    }
    
    public double getTotalAmount() {
        String totalText = getTotalText();
        // Extract number from "Total: $XX.XX"
        String amount = totalText.replaceAll("[^0-9.]", "");
        return Double.parseDouble(amount);
    }
    
    // Navigation Action Methods
    public void clickFinish() {
        click(finishButton);
    }
    
    public void clickCancel() {
        click(cancelButton);
    }
    
    public void completeOrder() {
        clickFinish();
    }
    
    // Verification Methods
    public boolean isItemInOrder(String itemName) {
        return getAllItemNames().contains(itemName);
    }
    
    public boolean isFinishButtonDisplayed() {
        return isDisplayed(finishButton);
    }
    
    public boolean isFinishButtonEnabled() {
        return finishButton.isEnabled();
    }
    
    public boolean isCancelButtonDisplayed() {
        return isDisplayed(cancelButton);
    }
    
    public boolean isSummaryInfoDisplayed() {
        return isDisplayed(summaryInfo);
    }
    
    // Price Calculation Verification
    public boolean isTotalCalculationCorrect() {
        double expectedTotal = getSubtotalAmount() + getTaxAmount();
        double actualTotal = getTotalAmount();
        // Allow for small floating point differences
        return Math.abs(expectedTotal - actualTotal) < 0.01;
    }
    
    // Calculate expected subtotal from item prices
    public double calculateExpectedSubtotal() {
        return getAllItemPrices().stream()
                .mapToDouble(price -> {
                    // Remove $ sign and convert to double
                    String cleanPrice = price.replace("$", "");
                    return Double.parseDouble(cleanPrice);
                })
                .sum();
    }
    
    public boolean isSubtotalCorrect() {
        double expectedSubtotal = calculateExpectedSubtotal();
        double actualSubtotal = getSubtotalAmount();
        // Allow for small floating point differences
        return Math.abs(expectedSubtotal - actualSubtotal) < 0.01;
    }
    
    // Wait for page to fully load
    private void waitForPageToLoad() {
        waitForElementToBeVisible(By.className("title"));
        waitForElementToBeVisible(By.className("summary_info"));
        waitForTitleToContain("Swag Labs");
    }
}
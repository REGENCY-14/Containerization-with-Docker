package com.swaglabs.pages;

import com.swaglabs.base.BasePage;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

import java.util.List;

/**
 * Page Object Model for Swag Labs Shopping Cart Page
 * Contains all elements and actions for the shopping cart functionality
 */
public class CartPage extends BasePage {
    
    // Page URL
    private static final String CART_URL = "https://www.saucedemo.com/cart.html";
    
    // Header Elements
    @FindBy(className = "app_logo")
    private WebElement appLogo;
    
    @FindBy(className = "shopping_cart_link")
    private WebElement shoppingCartLink;
    
    @FindBy(className = "shopping_cart_badge")
    private WebElement cartBadge;
    
    // Page Title and Navigation
    @FindBy(className = "title")
    private WebElement pageTitle;
    
    @FindBy(id = "continue-shopping")
    private WebElement continueShoppingButton;
    
    @FindBy(id = "checkout")
    private WebElement checkoutButton;
    
    // Cart Content
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
    
    @FindBy(css = "[data-test^='remove']")
    private List<WebElement> removeButtons;
    
    // Empty Cart Message
    @FindBy(className = "removed_cart_item")
    private WebElement removedCartItem;
    
    // Constructor using WebDriver
    public CartPage(WebDriver driver) {
        super(driver);
    }
    
    // Constructor using DriverManager
    public CartPage() {
        super();
    }
    
    // Navigation Methods
    public void navigateToCartPage() {
        driver.get(CART_URL);
        waitForPageToLoad();
    }
    
    // Page Verification Methods
    public boolean isCartPageLoaded() {
        return isDisplayed(pageTitle) && 
               getCurrentUrl().contains("cart.html");
    }
    
    public String getPageTitle() {
        return getText(pageTitle);
    }
    
    // Cart Information Methods
    public int getCartItemCount() {
        return cartItems.size();
    }
    
    public boolean isCartEmpty() {
        return cartItems.isEmpty();
    }
    
    public int getCartBadgeCount() {
        if (isDisplayed(cartBadge)) {
            String badgeText = getText(cartBadge);
            return Integer.parseInt(badgeText);
        }
        return 0;
    }
    
    // Item Information Methods
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
    
    // Item Action Methods
    public void removeItem(int index) {
        if (index >= 0 && index < removeButtons.size()) {
            click(removeButtons.get(index));
        } else {
            throw new IndexOutOfBoundsException("Item index out of range: " + index);
        }
    }
    
    public void removeItemByName(String itemName) {
        String buttonId = "remove-" + itemName.toLowerCase().replace(" ", "-");
        WebElement removeButton = driver.findElement(By.id(buttonId));
        click(removeButton);
    }
    
    public void removeAllItems() {
        // Remove items from last to first to avoid index issues
        for (int i = removeButtons.size() - 1; i >= 0; i--) {
            click(removeButtons.get(i));
            // Wait a moment for the item to be removed
            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
    }
    
    public void clickItemName(int index) {
        if (index >= 0 && index < itemNames.size()) {
            click(itemNames.get(index));
        } else {
            throw new IndexOutOfBoundsException("Item index out of range: " + index);
        }
    }
    
    // Navigation Action Methods
    public void continueShopping() {
        click(continueShoppingButton);
    }
    
    public void proceedToCheckout() {
        click(checkoutButton);
    }
    
    // Verification Methods
    public boolean isItemInCart(String itemName) {
        return getAllItemNames().contains(itemName);
    }
    
    public boolean isContinueShoppingButtonDisplayed() {
        return isDisplayed(continueShoppingButton);
    }
    
    public boolean isCheckoutButtonDisplayed() {
        return isDisplayed(checkoutButton);
    }
    
    public boolean isCheckoutButtonEnabled() {
        return checkoutButton.isEnabled();
    }
    
    // Calculate total price (utility method)
    public double calculateTotalPrice() {
        return getAllItemPrices().stream()
                .mapToDouble(price -> {
                    // Remove $ sign and convert to double
                    String cleanPrice = price.replace("$", "");
                    return Double.parseDouble(cleanPrice);
                })
                .sum();
    }
    
    // Wait for page to fully load
    private void waitForPageToLoad() {
        waitForElementToBeVisible(By.className("title"));
        waitForTitleToContain("Swag Labs");
    }
}
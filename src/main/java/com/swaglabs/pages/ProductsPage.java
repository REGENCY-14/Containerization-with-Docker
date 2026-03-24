package com.swaglabs.pages;

import com.swaglabs.base.BasePage;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.Select;

import java.util.List;

/**
 * Page Object Model for Swag Labs Products/Inventory Page
 * Contains all elements and actions for the main products listing page
 */
public class ProductsPage extends BasePage {
    
    // Page URL
    private static final String PRODUCTS_URL = "https://www.saucedemo.com/inventory.html";
    
    // Header Elements
    @FindBy(className = "app_logo")
    private WebElement appLogo;
    
    @FindBy(className = "shopping_cart_link")
    private WebElement shoppingCartLink;
    
    @FindBy(className = "shopping_cart_badge")
    private WebElement cartBadge;
    
    @FindBy(id = "react-burger-menu-btn")
    private WebElement menuButton;
    
    // Product Sorting
    @FindBy(className = "product_sort_container")
    private WebElement sortDropdown;
    
    // Product Container
    @FindBy(className = "inventory_container")
    private WebElement inventoryContainer;
    
    @FindBy(className = "inventory_list")
    private WebElement inventoryList;
    
    // Product Items (multiple elements)
    @FindBy(className = "inventory_item")
    private List<WebElement> productItems;
    
    @FindBy(className = "inventory_item_name")
    private List<WebElement> productNames;
    
    @FindBy(className = "inventory_item_price")
    private List<WebElement> productPrices;
    
    @FindBy(css = "[data-test^='add-to-cart']")
    private List<WebElement> addToCartButtons;
    
    @FindBy(css = "[data-test^='remove']")
    private List<WebElement> removeButtons;
    
    // Menu Elements (when menu is opened)
    @FindBy(id = "logout_sidebar_link")
    private WebElement logoutLink;
    
    @FindBy(id = "inventory_sidebar_link")
    private WebElement allItemsLink;
    
    @FindBy(id = "about_sidebar_link")
    private WebElement aboutLink;
    
    @FindBy(id = "reset_sidebar_link")
    private WebElement resetAppStateLink;
    
    // Constructor using WebDriver
    public ProductsPage(WebDriver driver) {
        super(driver);
    }
    
    // Constructor using DriverManager
    public ProductsPage() {
        super();
    }
    
    // Navigation Methods
    public void navigateToProductsPage() {
        driver.get(PRODUCTS_URL);
        waitForPageToLoad();
    }
    
    // Page Verification Methods
    public boolean isProductsPageLoaded() {
        return isDisplayed(appLogo) && 
               isDisplayed(inventoryContainer) && 
               getCurrentUrl().contains("inventory.html");
    }
    
    public String getPageTitle() {
        return driver.getTitle();
    }
    
    // Cart Methods
    public void clickShoppingCart() {
        click(shoppingCartLink);
    }
    
    public int getCartItemCount() {
        if (isDisplayed(cartBadge)) {
            String badgeText = getText(cartBadge);
            return Integer.parseInt(badgeText);
        }
        return 0;
    }
    
    public boolean isCartBadgeDisplayed() {
        return isDisplayed(cartBadge);
    }
    
    // Product Sorting Methods
    public void sortProductsBy(String sortOption) {
        Select sortSelect = new Select(sortDropdown);
        sortSelect.selectByVisibleText(sortOption);
        waitForPageToLoad();
    }
    
    public void sortProductsByValue(String sortValue) {
        Select sortSelect = new Select(sortDropdown);
        sortSelect.selectByValue(sortValue);
        waitForPageToLoad();
    }
    
    public String getCurrentSortOption() {
        Select sortSelect = new Select(sortDropdown);
        return sortSelect.getFirstSelectedOption().getText();
    }
    
    // Product Information Methods
    public int getProductCount() {
        return productItems.size();
    }
    
    public List<String> getAllProductNames() {
        return productNames.stream()
                .map(this::getText)
                .toList();
    }
    
    public List<String> getAllProductPrices() {
        return productPrices.stream()
                .map(this::getText)
                .toList();
    }
    
    public String getProductName(int index) {
        if (index >= 0 && index < productNames.size()) {
            return getText(productNames.get(index));
        }
        throw new IndexOutOfBoundsException("Product index out of range: " + index);
    }
    
    public String getProductPrice(int index) {
        if (index >= 0 && index < productPrices.size()) {
            return getText(productPrices.get(index));
        }
        throw new IndexOutOfBoundsException("Product index out of range: " + index);
    }
    
    // Product Action Methods
    public void addProductToCart(int index) {
        if (index >= 0 && index < addToCartButtons.size()) {
            click(addToCartButtons.get(index));
        } else {
            throw new IndexOutOfBoundsException("Product index out of range: " + index);
        }
    }
    
    public void addProductToCartByName(String productName) {
        String buttonId = "add-to-cart-" + productName.toLowerCase().replace(" ", "-");
        WebElement addButton = driver.findElement(By.id(buttonId));
        click(addButton);
    }
    
    public void removeProductFromCart(int index) {
        if (index >= 0 && index < removeButtons.size()) {
            click(removeButtons.get(index));
        } else {
            throw new IndexOutOfBoundsException("Product index out of range: " + index);
        }
    }
    
    public void removeProductFromCartByName(String productName) {
        String buttonId = "remove-" + productName.toLowerCase().replace(" ", "-");
        WebElement removeButton = driver.findElement(By.id(buttonId));
        click(removeButton);
    }
    
    public void clickProductName(int index) {
        if (index >= 0 && index < productNames.size()) {
            click(productNames.get(index));
        } else {
            throw new IndexOutOfBoundsException("Product index out of range: " + index);
        }
    }
    
    // Menu Methods
    public void openMenu() {
        click(menuButton);
        waitForElementToBeVisible(By.id("logout_sidebar_link"));
    }
    
    public void logout() {
        openMenu();
        click(logoutLink);
    }
    
    public void resetAppState() {
        openMenu();
        click(resetAppStateLink);
    }
    
    public void clickAllItems() {
        openMenu();
        click(allItemsLink);
    }
    
    public void clickAbout() {
        openMenu();
        click(aboutLink);
    }
    
    // Utility Methods
    public boolean isProductInCart(String productName) {
        String removeButtonId = "remove-" + productName.toLowerCase().replace(" ", "-");
        try {
            WebElement removeButton = driver.findElement(By.id(removeButtonId));
            return isDisplayed(removeButton);
        } catch (Exception e) {
            return false;
        }
    }
    
    // Wait for page to fully load
    private void waitForPageToLoad() {
        waitForElementToBeVisible(By.className("inventory_container"));
        waitForElementToBeVisible(By.className("inventory_list"));
    }
}
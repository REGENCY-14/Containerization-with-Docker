package com.swaglabs.tests;

import com.swaglabs.base.BaseTest;
import com.swaglabs.pages.CartPage;
import com.swaglabs.pages.LoginPage;
import com.swaglabs.pages.ProductsPage;
import com.swaglabs.utils.TestData;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Test class for View Cart functionality
 * Covers cart display, item information, cart operations, and navigation
 */
public class ViewCartTest extends BaseTest {
    
    private LoginPage loginPage;
    private ProductsPage productsPage;
    private CartPage cartPage;
    
    @BeforeEach
    public void setUpTest() {
        loginPage = new LoginPage();
        productsPage = new ProductsPage();
        cartPage = new CartPage();
        
        // Login before each test
        loginPage.navigateToLoginPage();
        loginPage.login(TestData.VALID_USERNAME, TestData.VALID_PASSWORD);
        assertTrue(productsPage.isProductsPageLoaded(), "Products page should be loaded");
    }
    
    @Test
    @DisplayName("Verify empty cart display")
    public void testEmptyCartDisplay() {
        // Navigate to cart page
        productsPage.clickShoppingCart();
        assertTrue(cartPage.isCartPageLoaded(), "Cart page should be loaded");
        
        // Verify empty cart state
        assertTrue(cartPage.isCartEmpty(), "Cart should be empty");
        assertEquals(0, cartPage.getCartItemCount(), "Cart item count should be 0");
        assertEquals(TestData.CART_PAGE_TITLE, cartPage.getPageTitle(), "Cart page title should be correct");
        
        // Verify navigation buttons are present
        assertTrue(cartPage.isContinueShoppingButtonDisplayed(), "Continue shopping button should be displayed");
        assertTrue(cartPage.isCheckoutButtonDisplayed(), "Checkout button should be displayed");
        
        System.out.println("✅ Empty cart display verified");
    }
    
    @Test
    @DisplayName("Verify cart with single item")
    public void testCartWithSingleItem() {
        // Add one product to cart
        productsPage.addProductToCartByName(TestData.SAUCE_LABS_BACKPACK);
        assertEquals(1, productsPage.getCartItemCount(), "Cart should contain 1 item");
        
        // Navigate to cart page
        productsPage.clickShoppingCart();
        assertTrue(cartPage.isCartPageLoaded(), "Cart page should be loaded");
        
        // Verify cart contains the item
        assertFalse(cartPage.isCartEmpty(), "Cart should not be empty");
        assertEquals(1, cartPage.getCartItemCount(), "Cart should contain 1 item");
        
        // Verify item details
        assertTrue(cartPage.isItemInCart(TestData.SAUCE_LABS_BACKPACK), "Backpack should be in cart");
        assertEquals(TestData.SAUCE_LABS_BACKPACK, cartPage.getItemName(0), "Item name should match");
        assertEquals(TestData.BACKPACK_PRICE, cartPage.getItemPrice(0), "Item price should match");
        assertEquals(1, cartPage.getItemQuantity(0), "Item quantity should be 1");
        
        // Verify cart badge count matches
        assertEquals(1, cartPage.getCartBadgeCount(), "Cart badge should show 1 item");
        
        System.out.println("✅ Cart with single item verified");
    }
    
    @Test
    @DisplayName("Verify cart with multiple items")
    public void testCartWithMultipleItems() {
        // Add multiple products to cart
        productsPage.addProductToCartByName(TestData.SAUCE_LABS_BACKPACK);
        productsPage.addProductToCartByName(TestData.SAUCE_LABS_BIKE_LIGHT);
        productsPage.addProductToCartByName(TestData.SAUCE_LABS_BOLT_TSHIRT);
        assertEquals(3, productsPage.getCartItemCount(), "Cart should contain 3 items");
        
        // Navigate to cart page
        productsPage.clickShoppingCart();
        assertTrue(cartPage.isCartPageLoaded(), "Cart page should be loaded");
        
        // Verify cart contains all items
        assertEquals(3, cartPage.getCartItemCount(), "Cart should contain 3 items");
        
        // Verify all items are present
        List<String> itemNames = cartPage.getAllItemNames();
        assertTrue(itemNames.contains(TestData.SAUCE_LABS_BACKPACK), "Backpack should be in cart");
        assertTrue(itemNames.contains(TestData.SAUCE_LABS_BIKE_LIGHT), "Bike light should be in cart");
        assertTrue(itemNames.contains(TestData.SAUCE_LABS_BOLT_TSHIRT), "T-shirt should be in cart");
        
        // Verify prices are displayed correctly
        List<String> itemPrices = cartPage.getAllItemPrices();
        assertTrue(itemPrices.contains(TestData.BACKPACK_PRICE), "Backpack price should be correct");
        assertTrue(itemPrices.contains(TestData.BIKE_LIGHT_PRICE), "Bike light price should be correct");
        assertTrue(itemPrices.contains(TestData.BOLT_TSHIRT_PRICE), "T-shirt price should be correct");
        
        // Verify all quantities are 1
        List<Integer> quantities = cartPage.getAllItemQuantities();
        assertEquals(3, quantities.size(), "Should have 3 quantity entries");
        assertTrue(quantities.stream().allMatch(q -> q == 1), "All quantities should be 1");
        
        System.out.println("✅ Cart with multiple items verified");
    }
    
    @Test
    @DisplayName("Verify removing items from cart")
    public void testRemoveItemsFromCart() {
        // Add multiple products to cart
        productsPage.addProductToCartByName(TestData.SAUCE_LABS_BACKPACK);
        productsPage.addProductToCartByName(TestData.SAUCE_LABS_BIKE_LIGHT);
        productsPage.addProductToCartByName(TestData.SAUCE_LABS_BOLT_TSHIRT);
        
        // Navigate to cart page
        productsPage.clickShoppingCart();
        assertTrue(cartPage.isCartPageLoaded(), "Cart page should be loaded");
        assertEquals(3, cartPage.getCartItemCount(), "Cart should contain 3 items initially");
        
        // Remove one item
        cartPage.removeItemByName(TestData.SAUCE_LABS_BIKE_LIGHT);
        
        // Verify item was removed
        assertEquals(2, cartPage.getCartItemCount(), "Cart should contain 2 items after removal");
        assertFalse(cartPage.isItemInCart(TestData.SAUCE_LABS_BIKE_LIGHT), "Bike light should not be in cart");
        assertTrue(cartPage.isItemInCart(TestData.SAUCE_LABS_BACKPACK), "Backpack should still be in cart");
        assertTrue(cartPage.isItemInCart(TestData.SAUCE_LABS_BOLT_TSHIRT), "T-shirt should still be in cart");
        
        // Remove another item
        cartPage.removeItemByName(TestData.SAUCE_LABS_BACKPACK);
        
        // Verify item was removed
        assertEquals(1, cartPage.getCartItemCount(), "Cart should contain 1 item after second removal");
        assertFalse(cartPage.isItemInCart(TestData.SAUCE_LABS_BACKPACK), "Backpack should not be in cart");
        assertTrue(cartPage.isItemInCart(TestData.SAUCE_LABS_BOLT_TSHIRT), "T-shirt should still be in cart");
        
        System.out.println("✅ Item removal from cart verified");
    }
    
    @Test
    @DisplayName("Verify cart total calculation")
    public void testCartTotalCalculation() {
        // Add products with known prices
        productsPage.addProductToCartByName(TestData.SAUCE_LABS_BACKPACK);  // $29.99
        productsPage.addProductToCartByName(TestData.SAUCE_LABS_BIKE_LIGHT); // $9.99
        
        // Navigate to cart page
        productsPage.clickShoppingCart();
        assertTrue(cartPage.isCartPageLoaded(), "Cart page should be loaded");
        
        // Calculate expected total
        double expectedTotal = 29.99 + 9.99; // $39.98
        double actualTotal = cartPage.calculateTotalPrice();
        
        // Verify total calculation (allow small floating point differences)
        assertEquals(expectedTotal, actualTotal, 0.01, "Cart total should be calculated correctly");
        
        System.out.println("✅ Cart total calculation verified: $" + actualTotal);
    }
    
    @Test
    @DisplayName("Verify continue shopping navigation")
    public void testContinueShoppingNavigation() {
        // Add item to cart and navigate to cart page
        productsPage.addProductToCartByName(TestData.SAUCE_LABS_BACKPACK);
        productsPage.clickShoppingCart();
        assertTrue(cartPage.isCartPageLoaded(), "Cart page should be loaded");
        
        // Click continue shopping
        cartPage.continueShopping();
        
        // Verify navigation back to products page
        assertTrue(productsPage.isProductsPageLoaded(), "Should navigate back to products page");
        assertTrue(productsPage.getCurrentUrl().contains("inventory.html"), "URL should contain inventory.html");
        
        // Verify cart badge still shows item count
        assertEquals(1, productsPage.getCartItemCount(), "Cart should still contain 1 item");
        
        System.out.println("✅ Continue shopping navigation verified");
    }
    
    @Test
    @DisplayName("Verify cart page elements and layout")
    public void testCartPageElements() {
        // Add item to cart and navigate to cart page
        productsPage.addProductToCartByName(TestData.SAUCE_LABS_BACKPACK);
        productsPage.clickShoppingCart();
        assertTrue(cartPage.isCartPageLoaded(), "Cart page should be loaded");
        
        // Verify page elements
        assertEquals(TestData.CART_PAGE_TITLE, cartPage.getPageTitle(), "Cart page title should be correct");
        assertTrue(cartPage.getCurrentUrl().contains("cart.html"), "URL should contain cart.html");
        
        // Verify buttons are present and enabled
        assertTrue(cartPage.isContinueShoppingButtonDisplayed(), "Continue shopping button should be displayed");
        assertTrue(cartPage.isCheckoutButtonDisplayed(), "Checkout button should be displayed");
        assertTrue(cartPage.isCheckoutButtonEnabled(), "Checkout button should be enabled");
        
        // Verify item details are displayed
        assertNotNull(cartPage.getItemName(0), "Item name should be displayed");
        assertNotNull(cartPage.getItemPrice(0), "Item price should be displayed");
        assertNotNull(cartPage.getItemDescription(0), "Item description should be displayed");
        
        System.out.println("✅ Cart page elements verified");
    }
}
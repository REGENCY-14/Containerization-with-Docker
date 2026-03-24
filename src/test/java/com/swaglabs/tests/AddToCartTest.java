package com.swaglabs.tests;

import com.swaglabs.base.BaseTest;
import com.swaglabs.pages.LoginPage;
import com.swaglabs.pages.ProductsPage;
import com.swaglabs.utils.TestData;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Test class for Add to Cart functionality
 * Covers adding single/multiple products, cart badge updates, and product state changes
 */
public class AddToCartTest extends BaseTest {
    
    private LoginPage loginPage;
    private ProductsPage productsPage;
    
    @BeforeEach
    public void setUpTest() {
        loginPage = new LoginPage();
        productsPage = new ProductsPage();
        
        // Login before each test
        loginPage.navigateToLoginPage();
        loginPage.login(TestData.VALID_USERNAME, TestData.VALID_PASSWORD);
        assertTrue(productsPage.isProductsPageLoaded(), "Products page should be loaded");
    }
    
    @Test
    @DisplayName("Verify adding a single product to cart")
    public void testAddSingleProductToCart() {
        // Verify initial cart state
        assertEquals(0, productsPage.getCartItemCount(), "Cart should be empty initially");
        
        // Add first product to cart by index (more reliable)
        productsPage.addProductToCart(0);
        
        // Verify cart count is updated
        assertEquals(1, productsPage.getCartItemCount(), "Cart should contain 1 item");
        
        System.out.println("✅ Single product added to cart successfully");
    }
    
    @Test
    @DisplayName("Verify adding multiple products to cart")
    public void testAddMultipleProductsToCart() {
        // Verify initial cart state
        assertEquals(0, productsPage.getCartItemCount(), "Cart should be empty initially");
        
        // Add multiple products to cart by index
        productsPage.addProductToCart(0);
        productsPage.addProductToCart(1);
        productsPage.addProductToCart(2);
        
        // Verify cart count shows correct number
        assertEquals(3, productsPage.getCartItemCount(), "Cart should contain 3 items");
        
        System.out.println("✅ Multiple products added to cart successfully");
    }
    
    @Test
    @DisplayName("Verify removing product from cart")
    public void testRemoveProductFromCart() {
        // Add products to cart first
        productsPage.addProductToCartByName(TestData.SAUCE_LABS_BACKPACK);
        productsPage.addProductToCartByName(TestData.SAUCE_LABS_BIKE_LIGHT);
        assertEquals(2, productsPage.getCartItemCount(), "Cart should contain 2 items");
        
        // Remove one product
        productsPage.removeProductFromCartByName(TestData.SAUCE_LABS_BACKPACK);
        
        // Verify cart count is updated
        assertEquals(1, productsPage.getCartItemCount(), "Cart should contain 1 item after removal");
        
        // Verify product state changed back to "add to cart"
        assertFalse(productsPage.isProductInCart(TestData.SAUCE_LABS_BACKPACK), 
                   "Backpack should not be in cart after removal");
        assertTrue(productsPage.isProductInCart(TestData.SAUCE_LABS_BIKE_LIGHT), 
                  "Bike light should still be in cart");
        
        System.out.println("✅ Product removed from cart successfully");
    }
    
    @Test
    @DisplayName("Verify adding and removing all products")
    public void testAddAndRemoveAllProducts() {
        // Add all available products
        productsPage.addProductToCartByName(TestData.SAUCE_LABS_BACKPACK);
        productsPage.addProductToCartByName(TestData.SAUCE_LABS_BIKE_LIGHT);
        productsPage.addProductToCartByName(TestData.SAUCE_LABS_BOLT_TSHIRT);
        productsPage.addProductToCartByName(TestData.SAUCE_LABS_FLEECE_JACKET);
        productsPage.addProductToCartByName(TestData.SAUCE_LABS_ONESIE);
        productsPage.addProductToCartByName(TestData.TEST_ALLTHETHINGS_TSHIRT);
        
        // Verify all products are in cart
        assertEquals(6, productsPage.getCartItemCount(), "Cart should contain all 6 products");
        
        // Remove all products
        productsPage.removeProductFromCartByName(TestData.SAUCE_LABS_BACKPACK);
        productsPage.removeProductFromCartByName(TestData.SAUCE_LABS_BIKE_LIGHT);
        productsPage.removeProductFromCartByName(TestData.SAUCE_LABS_BOLT_TSHIRT);
        productsPage.removeProductFromCartByName(TestData.SAUCE_LABS_FLEECE_JACKET);
        productsPage.removeProductFromCartByName(TestData.SAUCE_LABS_ONESIE);
        productsPage.removeProductFromCartByName(TestData.TEST_ALLTHETHINGS_TSHIRT);
        
        // Verify cart is empty
        assertEquals(0, productsPage.getCartItemCount(), "Cart should be empty after removing all products");
        
        System.out.println("✅ All products added and removed successfully");
    }
    
    @Test
    @DisplayName("Verify product information is displayed correctly")
    public void testProductInformationDisplay() {
        // Verify products are displayed
        assertTrue(productsPage.getProductCount() > 0, "Products should be displayed on the page");
        
        // Verify product names are displayed
        assertTrue(productsPage.getAllProductNames().contains(TestData.SAUCE_LABS_BACKPACK), 
                  "Backpack should be displayed");
        assertTrue(productsPage.getAllProductNames().contains(TestData.SAUCE_LABS_BIKE_LIGHT), 
                  "Bike light should be displayed");
        
        // Verify product prices are displayed
        assertTrue(productsPage.getAllProductPrices().contains(TestData.BACKPACK_PRICE), 
                  "Backpack price should be displayed");
        assertTrue(productsPage.getAllProductPrices().contains(TestData.BIKE_LIGHT_PRICE), 
                  "Bike light price should be displayed");
        
        System.out.println("✅ Product information displayed correctly");
    }
    
    @Test
    @DisplayName("Verify product sorting functionality")
    public void testProductSorting() {
        // Test sorting by name A-Z (default)
        String currentSort = productsPage.getCurrentSortOption();
        assertEquals(TestData.SORT_NAME_A_TO_Z, currentSort, "Default sort should be Name A-Z");
        
        // Test sorting by price low to high
        productsPage.sortProductsByValue(TestData.SORT_VALUE_PRICE_ASC);
        assertEquals(TestData.SORT_PRICE_LOW_TO_HIGH, productsPage.getCurrentSortOption(), 
                    "Sort option should be Price low to high");
        
        // Test sorting by price high to low
        productsPage.sortProductsByValue(TestData.SORT_VALUE_PRICE_DESC);
        assertEquals(TestData.SORT_PRICE_HIGH_TO_LOW, productsPage.getCurrentSortOption(), 
                    "Sort option should be Price high to low");
        
        // Test sorting by name Z-A
        productsPage.sortProductsByValue(TestData.SORT_VALUE_NAME_DESC);
        assertEquals(TestData.SORT_NAME_Z_TO_A, productsPage.getCurrentSortOption(), 
                    "Sort option should be Name Z-A");
        
        System.out.println("✅ Product sorting functionality verified");
    }
}
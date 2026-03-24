package com.swaglabs.tests;

import com.swaglabs.base.BaseTest;
import com.swaglabs.pages.*;
import com.swaglabs.utils.TestData;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Comprehensive test class demonstrating Page Object Model implementation
 * Shows how all page objects work together in a complete user journey
 */
public class PageObjectModelDemoTest extends BaseTest {
    
    @Test
    @DisplayName("Complete E2E journey demonstrating all Page Objects")
    public void testCompleteUserJourneyWithPageObjects() {
        // Step 1: Login using LoginPage
        LoginPage loginPage = new LoginPage();
        loginPage.navigateToLoginPage();
        assertTrue(loginPage.isLoginPageLoaded(), "Login page should be loaded");
        
        loginPage.login(TestData.VALID_USERNAME, TestData.VALID_PASSWORD);
        
        // Step 2: Products page interaction using ProductsPage
        ProductsPage productsPage = new ProductsPage();
        assertTrue(productsPage.isProductsPageLoaded(), "Products page should be loaded");
        
        // Verify products are displayed
        assertTrue(productsPage.getProductCount() > 0, "Products should be displayed");
        
        // Add products to cart
        productsPage.addProductToCartByName(TestData.SAUCE_LABS_BACKPACK);
        productsPage.addProductToCartByName(TestData.SAUCE_LABS_BIKE_LIGHT);
        
        // Verify cart badge shows correct count
        assertEquals(2, productsPage.getCartItemCount(), "Cart should contain 2 items");
        
        // Step 3: Navigate to cart using CartPage
        productsPage.clickShoppingCart();
        
        CartPage cartPage = new CartPage();
        assertTrue(cartPage.isCartPageLoaded(), "Cart page should be loaded");
        
        // Verify cart contents
        assertEquals(2, cartPage.getCartItemCount(), "Cart should contain 2 items");
        assertTrue(cartPage.isItemInCart(TestData.SAUCE_LABS_BACKPACK), "Backpack should be in cart");
        assertTrue(cartPage.isItemInCart(TestData.SAUCE_LABS_BIKE_LIGHT), "Bike light should be in cart");
        
        // Step 4: Proceed to checkout using CheckoutPage
        cartPage.proceedToCheckout();
        
        CheckoutPage checkoutPage = new CheckoutPage();
        assertTrue(checkoutPage.isCheckoutPageLoaded(), "Checkout page should be loaded");
        
        // Fill checkout information
        checkoutPage.completeCheckoutInformation(
            TestData.FIRST_NAME, 
            TestData.LAST_NAME, 
            TestData.POSTAL_CODE
        );
        
        // Step 5: Review order using CheckoutOverviewPage
        CheckoutOverviewPage overviewPage = new CheckoutOverviewPage();
        assertTrue(overviewPage.isCheckoutOverviewPageLoaded(), "Checkout overview page should be loaded");
        
        // Verify order details
        assertEquals(2, overviewPage.getOrderItemCount(), "Order should contain 2 items");
        assertTrue(overviewPage.isItemInOrder(TestData.SAUCE_LABS_BACKPACK), "Backpack should be in order");
        assertTrue(overviewPage.isItemInOrder(TestData.SAUCE_LABS_BIKE_LIGHT), "Bike light should be in order");
        
        // Verify payment and shipping info
        assertEquals(TestData.PAYMENT_INFO_VALUE, overviewPage.getPaymentInfoValue(), "Payment info should match");
        assertEquals(TestData.SHIPPING_INFO_VALUE, overviewPage.getShippingInfoValue(), "Shipping info should match");
        
        // Verify price calculations
        assertTrue(overviewPage.isSubtotalCorrect(), "Subtotal should be calculated correctly");
        assertTrue(overviewPage.isTotalCalculationCorrect(), "Total should be calculated correctly");
        
        // Complete the order
        overviewPage.completeOrder();
        
        // Step 6: Verify order completion using CheckoutCompletePage
        CheckoutCompletePage completePage = new CheckoutCompletePage();
        assertTrue(completePage.isCheckoutCompletePageLoaded(), "Checkout complete page should be loaded");
        assertTrue(completePage.isOrderSuccessful(), "Order should be completed successfully");
        assertTrue(completePage.isCartEmpty(), "Cart should be empty after successful order");
        
        // Return to products
        completePage.returnToProducts();
        
        // Verify we're back to products page
        assertTrue(productsPage.isProductsPageLoaded(), "Should return to products page");
        assertEquals(0, productsPage.getCartItemCount(), "Cart should be empty");
        
        System.out.println("✅ Complete E2E journey with Page Objects completed successfully!");
    }
    
    @Test
    @DisplayName("Demonstrate individual Page Object capabilities")
    public void testIndividualPageObjectCapabilities() {
        // Login
        LoginPage loginPage = new LoginPage();
        loginPage.navigateToLoginPage();
        loginPage.login(TestData.VALID_USERNAME, TestData.VALID_PASSWORD);
        
        // Products Page capabilities
        ProductsPage productsPage = new ProductsPage();
        
        // Test sorting functionality
        productsPage.sortProductsByValue(TestData.SORT_VALUE_PRICE_ASC);
        assertEquals(TestData.SORT_PRICE_LOW_TO_HIGH, productsPage.getCurrentSortOption());
        
        // Test product information retrieval
        assertTrue(productsPage.getAllProductNames().contains(TestData.SAUCE_LABS_BACKPACK));
        assertTrue(productsPage.getAllProductPrices().contains(TestData.BACKPACK_PRICE));
        
        // Add and remove products
        productsPage.addProductToCartByName(TestData.SAUCE_LABS_ONESIE);
        assertTrue(productsPage.isProductInCart(TestData.SAUCE_LABS_ONESIE));
        
        productsPage.removeProductFromCartByName(TestData.SAUCE_LABS_ONESIE);
        assertFalse(productsPage.isProductInCart(TestData.SAUCE_LABS_ONESIE));
        
        System.out.println("✅ Individual Page Object capabilities demonstrated successfully!");
    }
    
    @Test
    @DisplayName("Demonstrate error handling in Page Objects")
    public void testPageObjectErrorHandling() {
        // Test login error handling
        LoginPage loginPage = new LoginPage();
        loginPage.navigateToLoginPage();
        loginPage.login(TestData.INVALID_USERNAME, TestData.INVALID_PASSWORD);
        
        assertTrue(loginPage.isErrorMessageDisplayed(), "Error message should be displayed");
        assertEquals(TestData.INVALID_CREDENTIALS_ERROR, loginPage.getErrorMessage());
        
        // Login with valid credentials
        loginPage.login(TestData.VALID_USERNAME, TestData.VALID_PASSWORD);
        
        // Test checkout error handling
        ProductsPage productsPage = new ProductsPage();
        productsPage.addProductToCart(0); // Add first product
        productsPage.clickShoppingCart();
        
        CartPage cartPage = new CartPage();
        cartPage.proceedToCheckout();
        
        CheckoutPage checkoutPage = new CheckoutPage();
        
        // Try to continue without filling required fields
        checkoutPage.clickContinue();
        assertTrue(checkoutPage.isErrorMessageDisplayed(), "Error message should be displayed for empty fields");
        
        // Fill only first name and try again
        checkoutPage.enterFirstName(TestData.FIRST_NAME);
        checkoutPage.clickContinue();
        assertTrue(checkoutPage.isErrorMessageDisplayed(), "Error message should be displayed for missing fields");
        
        System.out.println("✅ Page Object error handling demonstrated successfully!");
    }
}
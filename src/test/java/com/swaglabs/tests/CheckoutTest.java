package com.swaglabs.tests;

import com.swaglabs.base.BaseTest;
import com.swaglabs.pages.*;
import com.swaglabs.utils.TestData;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Test class for Complete Checkout functionality
 * Covers the entire checkout process from cart to order completion
 */
public class CheckoutTest extends BaseTest {
    
    private LoginPage loginPage;
    private ProductsPage productsPage;
    private CartPage cartPage;
    private CheckoutPage checkoutPage;
    private CheckoutOverviewPage overviewPage;
    private CheckoutCompletePage completePage;
    
    @BeforeEach
    public void setUpTest() {
        loginPage = new LoginPage();
        productsPage = new ProductsPage();
        cartPage = new CartPage();
        checkoutPage = new CheckoutPage();
        overviewPage = new CheckoutOverviewPage();
        completePage = new CheckoutCompletePage();
        
        // Login and add products to cart before each test
        loginPage.navigateToLoginPage();
        loginPage.login(TestData.VALID_USERNAME, TestData.VALID_PASSWORD);
        assertTrue(productsPage.isProductsPageLoaded(), "Products page should be loaded");
        
        // Add products to cart for checkout tests
        productsPage.addProductToCartByName(TestData.SAUCE_LABS_BACKPACK);
        productsPage.addProductToCartByName(TestData.SAUCE_LABS_BIKE_LIGHT);
        assertEquals(2, productsPage.getCartItemCount(), "Cart should contain 2 items");
    }
    
    @Test
    @DisplayName("Verify successful complete checkout process")
    public void testSuccessfulCompleteCheckout() {
        // Navigate to cart and proceed to checkout
        productsPage.clickShoppingCart();
        assertTrue(cartPage.isCartPageLoaded(), "Cart page should be loaded");
        assertEquals(2, cartPage.getCartItemCount(), "Cart should contain 2 items");
        
        cartPage.proceedToCheckout();
        
        // Fill checkout information
        assertTrue(checkoutPage.isCheckoutPageLoaded(), "Checkout page should be loaded");
        checkoutPage.completeCheckoutInformation(TestData.FIRST_NAME, TestData.LAST_NAME, TestData.POSTAL_CODE);
        
        // Verify checkout overview page
        assertTrue(overviewPage.isCheckoutOverviewPageLoaded(), "Checkout overview page should be loaded");
        assertEquals(2, overviewPage.getOrderItemCount(), "Order should contain 2 items");
        
        // Verify order items
        assertTrue(overviewPage.isItemInOrder(TestData.SAUCE_LABS_BACKPACK), "Backpack should be in order");
        assertTrue(overviewPage.isItemInOrder(TestData.SAUCE_LABS_BIKE_LIGHT), "Bike light should be in order");
        
        // Verify payment and shipping information
        assertEquals(TestData.PAYMENT_INFO_VALUE, overviewPage.getPaymentInfoValue(), "Payment info should be correct");
        assertEquals(TestData.SHIPPING_INFO_VALUE, overviewPage.getShippingInfoValue(), "Shipping info should be correct");
        
        // Verify price calculations
        assertTrue(overviewPage.isSubtotalCorrect(), "Subtotal should be calculated correctly");
        assertTrue(overviewPage.isTotalCalculationCorrect(), "Total should be calculated correctly");
        
        // Complete the order
        overviewPage.completeOrder();
        
        // Verify order completion
        assertTrue(completePage.isCheckoutCompletePageLoaded(), "Checkout complete page should be loaded");
        assertTrue(completePage.isOrderSuccessful(), "Order should be completed successfully");
        assertTrue(completePage.isThankYouMessageDisplayed(), "Thank you message should be displayed");
        assertTrue(completePage.isOrderConfirmationDisplayed(), "Order confirmation should be displayed");
        
        // Verify cart is empty after successful order
        assertTrue(completePage.isCartEmpty(), "Cart should be empty after successful order");
        
        System.out.println("✅ Complete checkout process successful");
    }
    
    @Test
    @DisplayName("Verify checkout with single item")
    public void testCheckoutWithSingleItem() {
        // Remove one item to test with single item
        productsPage.removeProductFromCartByName(TestData.SAUCE_LABS_BIKE_LIGHT);
        assertEquals(1, productsPage.getCartItemCount(), "Cart should contain 1 item");
        
        // Navigate to cart and proceed to checkout
        productsPage.clickShoppingCart();
        cartPage.proceedToCheckout();
        
        // Fill checkout information
        checkoutPage.completeCheckoutInformation(TestData.FIRST_NAME, TestData.LAST_NAME, TestData.POSTAL_CODE);
        
        // Verify single item in overview
        assertTrue(overviewPage.isCheckoutOverviewPageLoaded(), "Checkout overview page should be loaded");
        assertEquals(1, overviewPage.getOrderItemCount(), "Order should contain 1 item");
        assertTrue(overviewPage.isItemInOrder(TestData.SAUCE_LABS_BACKPACK), "Backpack should be in order");
        
        // Complete the order
        overviewPage.completeOrder();
        assertTrue(completePage.isOrderSuccessful(), "Single item order should be successful");
        
        System.out.println("✅ Single item checkout successful");
    }
    
    @Test
    @DisplayName("Verify checkout information validation")
    public void testCheckoutInformationValidation() {
        // Navigate to checkout page
        productsPage.clickShoppingCart();
        cartPage.proceedToCheckout();
        assertTrue(checkoutPage.isCheckoutPageLoaded(), "Checkout page should be loaded");
        
        // Test empty form submission
        checkoutPage.clickContinue();
        assertTrue(checkoutPage.isErrorMessageDisplayed(), "Error message should be displayed for empty form");
        
        // Test with only first name
        checkoutPage.enterFirstName(TestData.FIRST_NAME);
        checkoutPage.clickContinue();
        assertTrue(checkoutPage.isErrorMessageDisplayed(), "Error message should be displayed for incomplete form");
        
        // Test with first and last name only
        checkoutPage.enterLastName(TestData.LAST_NAME);
        checkoutPage.clickContinue();
        assertTrue(checkoutPage.isErrorMessageDisplayed(), "Error message should be displayed for missing postal code");
        
        // Complete form and verify success
        checkoutPage.enterPostalCode(TestData.POSTAL_CODE);
        checkoutPage.clickContinue();
        assertTrue(overviewPage.isCheckoutOverviewPageLoaded(), "Should proceed to overview with complete information");
        
        System.out.println("✅ Checkout information validation verified");
    }
    
    @Test
    @DisplayName("Verify checkout form field interactions")
    public void testCheckoutFormFields() {
        // Navigate to checkout page
        productsPage.clickShoppingCart();
        cartPage.proceedToCheckout();
        assertTrue(checkoutPage.isCheckoutPageLoaded(), "Checkout page should be loaded");
        
        // Verify form fields are displayed
        assertTrue(checkoutPage.isFirstNameFieldDisplayed(), "First name field should be displayed");
        assertTrue(checkoutPage.isLastNameFieldDisplayed(), "Last name field should be displayed");
        assertTrue(checkoutPage.isPostalCodeFieldDisplayed(), "Postal code field should be displayed");
        
        // Test field input and retrieval
        checkoutPage.enterFirstName(TestData.FIRST_NAME);
        checkoutPage.enterLastName(TestData.LAST_NAME);
        checkoutPage.enterPostalCode(TestData.POSTAL_CODE);
        
        assertEquals(TestData.FIRST_NAME, checkoutPage.getFirstName(), "First name should be entered correctly");
        assertEquals(TestData.LAST_NAME, checkoutPage.getLastName(), "Last name should be entered correctly");
        assertEquals(TestData.POSTAL_CODE, checkoutPage.getPostalCode(), "Postal code should be entered correctly");
        
        // Test field clearing
        checkoutPage.clearAllFields();
        assertTrue(checkoutPage.areAllFieldsEmpty(), "All fields should be empty after clearing");
        
        System.out.println("✅ Checkout form fields verified");
    }
    
    @Test
    @DisplayName("Verify checkout overview price calculations")
    public void testCheckoutOverviewPriceCalculations() {
        // Navigate through checkout process
        productsPage.clickShoppingCart();
        cartPage.proceedToCheckout();
        checkoutPage.completeCheckoutInformation(TestData.FIRST_NAME, TestData.LAST_NAME, TestData.POSTAL_CODE);
        
        assertTrue(overviewPage.isCheckoutOverviewPageLoaded(), "Checkout overview page should be loaded");
        
        // Verify price information is displayed
        assertNotNull(overviewPage.getSubtotalText(), "Subtotal should be displayed");
        assertNotNull(overviewPage.getTaxText(), "Tax should be displayed");
        assertNotNull(overviewPage.getTotalText(), "Total should be displayed");
        
        // Verify price calculations
        double subtotal = overviewPage.getSubtotalAmount();
        double tax = overviewPage.getTaxAmount();
        double total = overviewPage.getTotalAmount();
        
        assertTrue(subtotal > 0, "Subtotal should be greater than 0");
        assertTrue(tax > 0, "Tax should be greater than 0");
        assertTrue(total > subtotal, "Total should be greater than subtotal");
        
        // Verify calculation accuracy
        assertTrue(overviewPage.isSubtotalCorrect(), "Subtotal should match item prices");
        assertTrue(overviewPage.isTotalCalculationCorrect(), "Total should equal subtotal plus tax");
        
        System.out.println("✅ Price calculations verified - Subtotal: $" + subtotal + ", Tax: $" + tax + ", Total: $" + total);
    }
    
    @Test
    @DisplayName("Verify checkout cancellation")
    public void testCheckoutCancellation() {
        // Navigate to checkout page
        productsPage.clickShoppingCart();
        cartPage.proceedToCheckout();
        assertTrue(checkoutPage.isCheckoutPageLoaded(), "Checkout page should be loaded");
        
        // Cancel checkout
        checkoutPage.clickCancel();
        
        // Verify navigation back to cart
        assertTrue(cartPage.isCartPageLoaded(), "Should navigate back to cart page");
        assertEquals(2, cartPage.getCartItemCount(), "Cart should still contain items after cancellation");
        
        // Test cancellation from overview page
        cartPage.proceedToCheckout();
        checkoutPage.completeCheckoutInformation(TestData.FIRST_NAME, TestData.LAST_NAME, TestData.POSTAL_CODE);
        assertTrue(overviewPage.isCheckoutOverviewPageLoaded(), "Should be on overview page");
        
        overviewPage.clickCancel();
        assertTrue(productsPage.isProductsPageLoaded(), "Should navigate back to products page");
        assertEquals(2, productsPage.getCartItemCount(), "Cart should still contain items after overview cancellation");
        
        System.out.println("✅ Checkout cancellation verified");
    }
    
    @Test
    @DisplayName("Verify return to products after successful order")
    public void testReturnToProductsAfterOrder() {
        // Complete full checkout process
        productsPage.clickShoppingCart();
        cartPage.proceedToCheckout();
        checkoutPage.completeCheckoutInformation(TestData.FIRST_NAME, TestData.LAST_NAME, TestData.POSTAL_CODE);
        overviewPage.completeOrder();
        
        assertTrue(completePage.isCheckoutCompletePageLoaded(), "Order should be completed");
        
        // Return to products
        completePage.returnToProducts();
        
        // Verify navigation back to products page
        assertTrue(productsPage.isProductsPageLoaded(), "Should navigate back to products page");
        assertEquals(0, productsPage.getCartItemCount(), "Cart should be empty after successful order");
        assertFalse(productsPage.isCartBadgeDisplayed(), "Cart badge should not be displayed when empty");
        
        System.out.println("✅ Return to products after order verified");
    }
}
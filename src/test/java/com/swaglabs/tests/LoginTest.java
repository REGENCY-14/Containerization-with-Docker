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
 * Test class for Login functionality
 * Covers successful login, failed login, and login validation scenarios
 */
public class LoginTest extends BaseTest {
    
    private LoginPage loginPage;
    private ProductsPage productsPage;
    
    @BeforeEach
    public void setUpTest() {
        loginPage = new LoginPage();
        productsPage = new ProductsPage();
    }
    
    @Test
    @DisplayName("Verify successful login with valid credentials")
    public void testSuccessfulLogin() {
        // Navigate to login page
        loginPage.navigateToLoginPage();
        assertTrue(loginPage.isLoginPageLoaded(), "Login page should be loaded");
        
        // Perform login with valid credentials
        loginPage.login(TestData.VALID_USERNAME, TestData.VALID_PASSWORD);
        
        // Verify successful login by checking products page is loaded
        assertTrue(productsPage.isProductsPageLoaded(), "Products page should be loaded after successful login");
        assertEquals(TestData.PRODUCTS_PAGE_TITLE, productsPage.getPageTitle(), "Page title should be 'Swag Labs'");
        assertTrue(productsPage.getCurrentUrl().contains("inventory.html"), "URL should contain inventory.html");
        
        // Verify no error messages are displayed
        assertFalse(loginPage.isErrorMessageDisplayed(), "No error message should be displayed after successful login");
        
        System.out.println("✅ Successful login test completed");
    }
    
    @Test
    @DisplayName("Verify login fails with invalid credentials")
    public void testLoginWithInvalidCredentials() {
        // Navigate to login page
        loginPage.navigateToLoginPage();
        assertTrue(loginPage.isLoginPageLoaded(), "Login page should be loaded");
        
        // Attempt login with invalid credentials
        loginPage.login(TestData.INVALID_USERNAME, TestData.INVALID_PASSWORD);
        
        // Verify login failed - should remain on login page
        assertTrue(loginPage.isLoginPageLoaded(), "Should remain on login page after failed login");
        assertTrue(loginPage.getCurrentUrl().contains("saucedemo.com"), "Should still be on login URL");
        
        // Verify error message is displayed
        assertTrue(loginPage.isErrorMessageDisplayed(), "Error message should be displayed");
        assertEquals(TestData.INVALID_CREDENTIALS_ERROR, loginPage.getErrorMessage(), 
                    "Error message should match expected invalid credentials message");
        
        System.out.println("✅ Invalid credentials test completed");
    }
    
    @Test
    @DisplayName("Verify login fails with locked out user")
    public void testLoginWithLockedUser() {
        // Navigate to login page
        loginPage.navigateToLoginPage();
        assertTrue(loginPage.isLoginPageLoaded(), "Login page should be loaded");
        
        // Attempt login with locked user
        loginPage.login(TestData.LOCKED_USERNAME, TestData.VALID_PASSWORD);
        
        // Verify login failed - should remain on login page
        assertTrue(loginPage.isLoginPageLoaded(), "Should remain on login page after locked user login attempt");
        
        // Verify locked user error message is displayed
        assertTrue(loginPage.isErrorMessageDisplayed(), "Error message should be displayed for locked user");
        assertEquals(TestData.LOCKED_USER_ERROR, loginPage.getErrorMessage(), 
                    "Error message should match expected locked user message");
        
        System.out.println("✅ Locked user test completed");
    }
    
    @Test
    @DisplayName("Verify login fails with empty username")
    public void testLoginWithEmptyUsername() {
        // Navigate to login page
        loginPage.navigateToLoginPage();
        assertTrue(loginPage.isLoginPageLoaded(), "Login page should be loaded");
        
        // Attempt login with empty username
        loginPage.login(TestData.EMPTY_STRING, TestData.VALID_PASSWORD);
        
        // Verify login failed - should remain on login page
        assertTrue(loginPage.isLoginPageLoaded(), "Should remain on login page after empty username");
        
        // Verify error message is displayed
        assertTrue(loginPage.isErrorMessageDisplayed(), "Error message should be displayed for empty username");
        assertEquals(TestData.EMPTY_USERNAME_ERROR, loginPage.getErrorMessage(), 
                    "Error message should match expected empty username message");
        
        System.out.println("✅ Empty username test completed");
    }
    
    @Test
    @DisplayName("Verify login fails with empty password")
    public void testLoginWithEmptyPassword() {
        // Navigate to login page
        loginPage.navigateToLoginPage();
        assertTrue(loginPage.isLoginPageLoaded(), "Login page should be loaded");
        
        // Attempt login with empty password
        loginPage.login(TestData.VALID_USERNAME, TestData.EMPTY_STRING);
        
        // Verify login failed - should remain on login page
        assertTrue(loginPage.isLoginPageLoaded(), "Should remain on login page after empty password");
        
        // Verify error message is displayed
        assertTrue(loginPage.isErrorMessageDisplayed(), "Error message should be displayed for empty password");
        assertEquals(TestData.EMPTY_PASSWORD_ERROR, loginPage.getErrorMessage(), 
                    "Error message should match expected empty password message");
        
        System.out.println("✅ Empty password test completed");
    }
    
    @Test
    @DisplayName("Verify login page elements are displayed correctly")
    public void testLoginPageElements() {
        // Navigate to login page
        loginPage.navigateToLoginPage();
        assertTrue(loginPage.isLoginPageLoaded(), "Login page should be loaded");
        
        // Verify page title and URL
        assertEquals(TestData.LOGIN_PAGE_TITLE, loginPage.getPageTitle(), "Login page title should be correct");
        assertTrue(loginPage.getCurrentUrl().contains("saucedemo.com"), "URL should contain saucedemo.com");
        
        // Test individual field interactions
        loginPage.enterUsername("test_user");
        loginPage.enterPassword("test_pass");
        
        // Clear fields to verify they work
        loginPage.enterUsername("");
        loginPage.enterPassword("");
        
        System.out.println("✅ Login page elements test completed");
    }
}
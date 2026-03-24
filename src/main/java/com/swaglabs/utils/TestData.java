package com.swaglabs.utils;

/**
 * Comprehensive test data constants for the automation framework
 * Contains all test data needed for different pages and scenarios
 */
public class TestData {
    
    // Valid user credentials
    public static final String VALID_USERNAME = "standard_user";
    public static final String VALID_PASSWORD = "secret_sauce";
    
    // Alternative valid users
    public static final String PERFORMANCE_GLITCH_USER = "performance_glitch_user";
    public static final String PROBLEM_USER = "problem_user";
    public static final String VISUAL_USER = "visual_user";
    
    // Invalid credentials for negative testing
    public static final String INVALID_USERNAME = "invalid_user";
    public static final String INVALID_PASSWORD = "invalid_password";
    public static final String LOCKED_USERNAME = "locked_out_user";
    
    // Expected error messages
    public static final String INVALID_CREDENTIALS_ERROR = "Epic sadface: Username and password do not match any user in this service";
    public static final String LOCKED_USER_ERROR = "Epic sadface: Sorry, this user has been locked out.";
    public static final String EMPTY_USERNAME_ERROR = "Epic sadface: Username is required";
    public static final String EMPTY_PASSWORD_ERROR = "Epic sadface: Password is required";
    
    // URLs
    public static final String BASE_URL = "https://www.saucedemo.com/";
    public static final String LOGIN_URL = "https://www.saucedemo.com/";
    public static final String INVENTORY_URL = "https://www.saucedemo.com/inventory.html";
    public static final String CART_URL = "https://www.saucedemo.com/cart.html";
    public static final String CHECKOUT_URL = "https://www.saucedemo.com/checkout-step-one.html";
    public static final String CHECKOUT_OVERVIEW_URL = "https://www.saucedemo.com/checkout-step-two.html";
    public static final String CHECKOUT_COMPLETE_URL = "https://www.saucedemo.com/checkout-complete.html";
    
    // Product names (as they appear on the site)
    public static final String SAUCE_LABS_BACKPACK = "Sauce Labs Backpack";
    public static final String SAUCE_LABS_BIKE_LIGHT = "Sauce Labs Bike Light";
    public static final String SAUCE_LABS_BOLT_TSHIRT = "Sauce Labs Bolt T-Shirt";
    public static final String SAUCE_LABS_FLEECE_JACKET = "Sauce Labs Fleece Jacket";
    public static final String SAUCE_LABS_ONESIE = "Sauce Labs Onesie";
    public static final String TEST_ALLTHETHINGS_TSHIRT = "Test.allTheThings() T-Shirt (Red)";
    
    // Product prices
    public static final String BACKPACK_PRICE = "$29.99";
    public static final String BIKE_LIGHT_PRICE = "$9.99";
    public static final String BOLT_TSHIRT_PRICE = "$15.99";
    public static final String FLEECE_JACKET_PRICE = "$49.99";
    public static final String ONESIE_PRICE = "$7.99";
    public static final String ALLTHETHINGS_TSHIRT_PRICE = "$15.99";
    
    // Sort options
    public static final String SORT_NAME_A_TO_Z = "Name (A to Z)";
    public static final String SORT_NAME_Z_TO_A = "Name (Z to A)";
    public static final String SORT_PRICE_LOW_TO_HIGH = "Price (low to high)";
    public static final String SORT_PRICE_HIGH_TO_LOW = "Price (high to low)";
    
    // Sort values (for dropdown selection by value)
    public static final String SORT_VALUE_NAME_ASC = "az";
    public static final String SORT_VALUE_NAME_DESC = "za";
    public static final String SORT_VALUE_PRICE_ASC = "lohi";
    public static final String SORT_VALUE_PRICE_DESC = "hilo";
    
    // Checkout information
    public static final String FIRST_NAME = "John";
    public static final String LAST_NAME = "Doe";
    public static final String POSTAL_CODE = "12345";
    
    // Alternative checkout information for testing
    public static final String ALT_FIRST_NAME = "Jane";
    public static final String ALT_LAST_NAME = "Smith";
    public static final String ALT_POSTAL_CODE = "54321";
    
    // Invalid checkout information
    public static final String EMPTY_STRING = "";
    public static final String SPECIAL_CHARS = "!@#$%";
    public static final String LONG_STRING = "ThisIsAVeryLongStringThatExceedsNormalLimits";
    
    // Checkout error messages
    public static final String FIRST_NAME_REQUIRED_ERROR = "Error: First Name is required";
    public static final String LAST_NAME_REQUIRED_ERROR = "Error: Last Name is required";
    public static final String POSTAL_CODE_REQUIRED_ERROR = "Error: Postal Code is required";
    
    // Page titles
    public static final String LOGIN_PAGE_TITLE = "Swag Labs";
    public static final String PRODUCTS_PAGE_TITLE = "Swag Labs";
    public static final String CART_PAGE_TITLE = "Your Cart";
    public static final String CHECKOUT_PAGE_TITLE = "Checkout: Your Information";
    public static final String CHECKOUT_OVERVIEW_TITLE = "Checkout: Overview";
    public static final String CHECKOUT_COMPLETE_TITLE = "Checkout: Complete!";
    
    // Success messages
    public static final String ORDER_SUCCESS_HEADER = "Thank you for your order!";
    public static final String ORDER_DISPATCHED_MESSAGE = "Your order has been dispatched";
    
    // Payment and shipping info (as displayed on checkout overview)
    public static final String PAYMENT_INFO_LABEL = "Payment Information:";
    public static final String PAYMENT_INFO_VALUE = "SauceCard #31337";
    public static final String SHIPPING_INFO_LABEL = "Shipping Information:";
    public static final String SHIPPING_INFO_VALUE = "Free Pony Express Delivery!";
    
    // Tax rate (8% as used by the application)
    public static final double TAX_RATE = 0.08;
    
    // Common test data arrays
    public static final String[] ALL_PRODUCT_NAMES = {
        SAUCE_LABS_BACKPACK,
        SAUCE_LABS_BIKE_LIGHT,
        SAUCE_LABS_BOLT_TSHIRT,
        SAUCE_LABS_FLEECE_JACKET,
        SAUCE_LABS_ONESIE,
        TEST_ALLTHETHINGS_TSHIRT
    };
    
    public static final String[] ALL_PRODUCT_PRICES = {
        BACKPACK_PRICE,
        BIKE_LIGHT_PRICE,
        BOLT_TSHIRT_PRICE,
        FLEECE_JACKET_PRICE,
        ONESIE_PRICE,
        ALLTHETHINGS_TSHIRT_PRICE
    };
    
    public static final String[] VALID_USERS = {
        VALID_USERNAME,
        PERFORMANCE_GLITCH_USER,
        PROBLEM_USER,
        VISUAL_USER
    };
}
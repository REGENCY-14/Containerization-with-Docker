package com.swaglabs.utils;

/**
 * Test data constants for the automation framework
 */
public class TestData {
    
    // Valid user credentials
    public static final String VALID_USERNAME = "standard_user";
    public static final String VALID_PASSWORD = "secret_sauce";
    
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
    public static final String INVENTORY_URL = "https://www.saucedemo.com/inventory.html";
}
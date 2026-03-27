package com.swaglabs.utils;

import io.qameta.allure.Allure;
import io.qameta.allure.Attachment;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;

import java.io.ByteArrayInputStream;
import java.nio.charset.StandardCharsets;

/**
 * Utility class for Allure reporting integration
 * Provides methods for attachments, screenshots, and logging
 */
public class AllureUtils {
    
    /**
     * Attach screenshot to Allure report
     */
    @Attachment(value = "Screenshot", type = "image/png")
    public static byte[] takeScreenshot(WebDriver driver) {
        return ((TakesScreenshot) driver).getScreenshotAs(OutputType.BYTES);
    }
    
    /**
     * Attach text to Allure report
     */
    @Attachment(value = "{attachName}", type = "text/plain")
    public static String attachText(String attachName, String text) {
        return text;
    }
    
    /**
     * Attach HTML content to Allure report
     */
    @Attachment(value = "{attachName}", type = "text/html")
    public static String attachHtml(String attachName, String html) {
        return html;
    }
    
    /**
     * Attach JSON content to Allure report
     */
    @Attachment(value = "{attachName}", type = "application/json")
    public static String attachJson(String attachName, String json) {
        return json;
    }
    
    /**
     * Add step to Allure report with parameter
     */
    public static void addStep(String stepName, String parameter) {
        Allure.step(stepName + ": " + parameter);
    }
    
    /**
     * Add parameter to current test
     */
    public static void addParameter(String name, String value) {
        Allure.parameter(name, value);
    }
    
    /**
     * Add environment information
     */
    public static void addEnvironmentInfo(String name, String value) {
        Allure.addAttachment("Environment: " + name, 
            new ByteArrayInputStream(value.getBytes(StandardCharsets.UTF_8)));
    }
    
    /**
     * Add test description
     */
    public static void addDescription(String description) {
        Allure.description(description);
    }
    
    /**
     * Add link to test
     */
    public static void addLink(String name, String url) {
        Allure.link(name, url);
    }
    
    /**
     * Log step with attachment
     */
    public static void logStepWithAttachment(String stepName, String content) {
        Allure.step(stepName, () -> {
            attachText("Step Details", content);
        });
    }
}
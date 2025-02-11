package com.pageobjectmodels;

import java.time.Duration;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class BasePage {

    protected WebDriver driver;  // WebDriver instance
    protected WebDriverWait wait;  // Explicit wait instance

    public BasePage(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(10)); // Initialize wait with a timeout of 10 seconds
    }

    protected void click(WebElement element) {
        wait.until(ExpectedConditions.elementToBeClickable(element)).click(); // Wait until element is clickable, then click
    }

    protected void enterText(WebElement element, String text) {
        wait.until(ExpectedConditions.visibilityOf(element)).sendKeys(text); // Wait until element is visible, then enter text
    }

    protected void waitForElement(WebElement element) {
        wait.until(ExpectedConditions.visibilityOf(element)); // Wait until element is visible
    }
}

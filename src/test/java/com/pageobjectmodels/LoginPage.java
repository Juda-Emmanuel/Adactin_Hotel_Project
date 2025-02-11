package com.pageobjectmodels;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;

import java.util.List;

public class LoginPage extends BasePage {

    // Locators for login elements
    @FindBy(id = "username")
    private WebElement usernameField;

    @FindBy(id = "password")
    private WebElement passwordField;

    @FindBy(id = "login")
    private WebElement loginButton;

    // XPath for different possible error messages
    @FindBy(xpath = "//span[@id='username_span'] | //span[@id='password_span'] | //b[contains(text(),'Invalid Login details or Your Password might have ')]")
    private List<WebElement> errorMessages;

    @FindBy(xpath = "//a[text()='Logout']")
    private WebElement logoutButton;

    public LoginPage(WebDriver driver) {
        super(driver);
        PageFactory.initElements(driver, this); // Initialize elements
    }

    public void enterUsername(String username) {
        wait.until(ExpectedConditions.visibilityOf(usernameField)).sendKeys(username); // Wait & enter username
    }

    public void enterPassword(String password) {
        wait.until(ExpectedConditions.visibilityOf(passwordField)).sendKeys(password); // Wait & enter password
    }

    public void clickLogin() {
        wait.until(ExpectedConditions.elementToBeClickable(loginButton)).click(); // Wait & click login button
    }

    public boolean isLoginSuccessful() {
        try {
            return wait.until(ExpectedConditions.visibilityOf(logoutButton)).isDisplayed(); // Check if logout is visible
        } catch (Exception e) {
            return false; // Return false if exception occurs
        }
    }

    public boolean isErrorMessageDisplayed() {
        for (WebElement errorMessage : errorMessages) {
            if (errorMessage.isDisplayed()) {
                return true; // Return true if any error message is visible
            }
        }
        return false;
    }

    public boolean login(String username, String password) {
        enterUsername(username);
        enterPassword(password);
        clickLogin();
        return isLoginSuccessful(); // Return login status
    }
}

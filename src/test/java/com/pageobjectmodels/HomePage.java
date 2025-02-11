package com.pageobjectmodels;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;

public class HomePage extends BasePage {

    // Locators for login elements
    @FindBy(id = "username")
    private WebElement usernameField;

    @FindBy(id = "password")
    private WebElement passwordField;

    @FindBy(id = "login")
    private WebElement loginButton;

    public HomePage(WebDriver driver) {
        super(driver);
        PageFactory.initElements(driver, this); // Initialize WebElements
    }

    public void enterUsername(String username) {
        wait.until(ExpectedConditions.visibilityOf(usernameField)).sendKeys(username); // Wait & enter username
    }

    public void enterPassword(String password) {
        wait.until(ExpectedConditions.visibilityOf(passwordField)).sendKeys(password); // Wait & enter password
    }

    public SearchHotelPage clickLogin() {
        wait.until(ExpectedConditions.elementToBeClickable(loginButton)).click(); // Wait & click login button
        return new SearchHotelPage(driver); // Navigate to SearchHotelPage
    }
}

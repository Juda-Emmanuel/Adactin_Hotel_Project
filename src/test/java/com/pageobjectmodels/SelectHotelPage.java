package com.pageobjectmodels;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;

public class SelectHotelPage extends BasePage {

    private static final Logger logger = LogManager.getLogger(SelectHotelPage.class);

    @FindBy(xpath = "//input[@id='radiobutton_0']")
    private WebElement selectHotelRadioButton;

    @FindBy(xpath = "//input[@id='continue']")
    private WebElement continueButton;

    public SelectHotelPage(WebDriver driver) {
        super(driver);
        PageFactory.initElements(driver, this);
    }

    // Select hotel if available
    public void selectHotel() {
        if (isElementVisible(selectHotelRadioButton)) {
            click(selectHotelRadioButton);
            logger.info("Hotel selected successfully.");
        } else {
            logger.warn("No hotel available to select.");
        }
    }

    // Click 'Continue' button and navigate to Booking Page
    public BookingPage clickContinue() {
        if (isElementVisible(continueButton)) {
            click(continueButton);
            logger.info("Clicked Continue button successfully.");
            return new BookingPage(driver);
        } else {
            logger.error("Continue button is not visible.");
            throw new IllegalStateException("Continue button is not available.");
        }
    }

    // Generic method to check element visibility
    public boolean isElementVisible(WebElement element) {
        try {
            return wait.until(ExpectedConditions.visibilityOf(element)).isDisplayed();
        } catch (Exception e) {
            logger.warn("Element not visible: {}", element, e);
            return false;
        }
    }

    // Check if hotel list is displayed
    public boolean isHotelListDisplayed() {
        return isElementVisible(selectHotelRadioButton);
    }
}

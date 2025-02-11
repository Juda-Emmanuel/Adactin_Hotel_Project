package com.pageobjectmodels;

import java.text.SimpleDateFormat;
import java.time.Duration;
import java.util.Calendar;
import java.util.concurrent.TimeoutException;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class BookingPage extends BasePage {

    // Locators for various elements on the Booking Page
    @FindBy(id = "username")
    private WebElement usernameField;

    @FindBy(id = "password")
    private WebElement passwordField;

    @FindBy(id = "login")
    private WebElement loginButton;

    @FindBy(xpath = "//select[@id='location']")
    private WebElement locationDropdown;

    @FindBy(xpath = "//select[@id='hotels']")
    private WebElement hotelDropdown;

    @FindBy(xpath = "//select[@id='room_type']")
    private WebElement roomTypeDropdown;

    @FindBy(xpath = "//select[@id='room_nos']")
    private WebElement roomNosDropdown;

    @FindBy(xpath = "//select[@id='adult_room']")
    private WebElement adultsDropdown;

    @FindBy(xpath = "//select[@id='child_room']")
    private WebElement childrenDropdown;

    @FindBy(xpath = "//input[@id='datepick_in']")
    private WebElement checkInDateField;

    @FindBy(xpath = "//input[@id='datepick_out']")
    private WebElement checkOutDateField;

    @FindBy(xpath = "//input[@id='Submit']")
    private WebElement searchButton;

    @FindBy(xpath = "//input[@id='radiobutton_0']")
    private WebElement selectHotelButton;

    @FindBy(xpath = "//input[@id='continue']")
    private WebElement continueButton;

    @FindBy(xpath = "//input[@id='first_name']")
    private WebElement firstNameField;

    @FindBy(xpath = "//input[@id='last_name']")
    private WebElement lastNameField;

    @FindBy(xpath = "//textarea[@id='address']")
    private WebElement billingAddressField;

    @FindBy(xpath = "//input[@id='cc_num']")
    private WebElement creditCardNumberField;

    @FindBy(xpath = "//select[@id='cc_type']")
    private WebElement ccTypeDropdown;

    @FindBy(xpath = "//select[@id='cc_exp_month']")
    private WebElement expMonthDropdown;

    @FindBy(xpath = "//select[@id='cc_exp_year']")
    private WebElement expYearDropdown;

    @FindBy(xpath = "//input[@id='cc_cvv']")
    private WebElement cvvField;

    @FindBy(xpath = "//input[@id='book_now']")
    private WebElement bookButton;

    @FindBy(xpath = "//input[@id='order_no']")
    private WebElement confirmationMessage;

    private WebDriverWait wait;

    public BookingPage(WebDriver driver) {
        super(driver);
        PageFactory.initElements(driver, this);
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(5)); // Set explicit wait timeout to 5 seconds
    }

    private WebElement waitForElementToBeClickable(WebElement element) {
        return wait.until(ExpectedConditions.elementToBeClickable(element)); // Wait until element is clickable
    }

    private WebElement waitForElementToBeVisible(WebElement element) {
        return wait.until(ExpectedConditions.visibilityOf(element)); // Wait until element is visible
    }

    public void login(String username, String password) {
        waitForElementToBeVisible(usernameField).sendKeys(username); // Enter username
        waitForElementToBeVisible(passwordField).sendKeys(password); // Enter password
        waitForElementToBeClickable(loginButton).click(); // Click login button
    }

    public void searchHotel(String location, String hotel, String roomType, String roomNos, String adults, String children) {
        waitForElementToBeClickable(locationDropdown).sendKeys(location); // Select location
        waitForElementToBeClickable(hotelDropdown).sendKeys(hotel); // Select hotel
        waitForElementToBeClickable(roomTypeDropdown).sendKeys(roomType); // Select room type
        waitForElementToBeClickable(roomNosDropdown).sendKeys(roomNos); // Select number of rooms
        waitForElementToBeClickable(adultsDropdown).sendKeys(adults); // Select number of adults per room
        waitForElementToBeClickable(childrenDropdown).sendKeys(children); // Select number of children per room
    }

    public void enterCheckInCheckOutDates(String checkInDate, String checkOutDate) {
        waitForElementToBeVisible(checkInDateField).sendKeys(checkInDate); // Enter check-in date
        waitForElementToBeVisible(checkOutDateField).sendKeys(checkOutDate); // Enter check-out date
    }

    public String getFutureDate(int daysFromToday) {
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DAY_OF_MONTH, daysFromToday);
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy"); // Format date as "dd/MM/yyyy"
        return sdf.format(calendar.getTime()); // Return formatted future date
    }

    public void clickSearch() {
        waitForElementToBeClickable(searchButton).click(); // Click search button
    }

    public void selectHotel() {
        waitForElementToBeClickable(selectHotelButton).click(); // Select first available hotel
    }

    public void clickContinue() {
        waitForElementToBeClickable(continueButton).click(); // Click continue to proceed with booking
    }

    public void enterBookingDetails(String firstName, String lastName, String billingAddress, String ccNumber, String ccType, String expMonth, String expYear, String cvv) {
        waitForElementToBeVisible(firstNameField).sendKeys(firstName); // Enter first name
        waitForElementToBeVisible(lastNameField).sendKeys(lastName); // Enter last name
        waitForElementToBeVisible(billingAddressField).sendKeys(billingAddress); // Enter billing address
        waitForElementToBeVisible(creditCardNumberField).sendKeys(ccNumber); // Enter credit card number
        waitForElementToBeClickable(ccTypeDropdown).sendKeys(ccType); // Select credit card type
        waitForElementToBeClickable(expMonthDropdown).sendKeys(expMonth); // Select expiration month
        waitForElementToBeClickable(expYearDropdown).sendKeys(expYear); // Select expiration year
        waitForElementToBeVisible(cvvField).sendKeys(cvv); // Enter CVV
    }

    public void clickBook() {
        waitForElementToBeClickable(bookButton).click(); // Click book button
    }

    public boolean isBookingConfirmed() {
        try {
            return waitForElementToBeVisible(confirmationMessage).isDisplayed(); // Check if confirmation message is visible
        } catch (Exception e) {
            return false;
        }
    }
    
    public boolean isBookingSuccessful() throws TimeoutException {
        return wait.until(ExpectedConditions.textToBePresentInElement(confirmationMessage, "Order No"));
    }


    @SuppressWarnings("deprecation")
    public String getOrderNumber() {
        return wait.until(ExpectedConditions.visibilityOf(confirmationMessage)).getAttribute("value"); // Retrieve order number
    }
}

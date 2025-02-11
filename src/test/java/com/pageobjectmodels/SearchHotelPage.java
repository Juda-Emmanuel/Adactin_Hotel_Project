package com.pageobjectmodels;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;

public class SearchHotelPage extends BasePage {

    // Dropdown elements for hotel search
    @FindBy(xpath = "//select[@id='location']")
    private WebElement locationDropdown;

    @FindBy(xpath = "//select[@id='hotels']")
    private WebElement hotelDropdown;

    @FindBy(xpath = "//select[@id='room_type']")
    private WebElement roomTypeDropdown;

    @FindBy(xpath = "//select[@id='room_nos']")
    private WebElement roomNosDropdown;

    @FindBy(xpath = "//select[@id='adult_room']")
    private WebElement adultsPerRoomDropdown;

    @FindBy(xpath = "//select[@id='child_room']")
    private WebElement childrenPerRoomDropdown;

    // Date input fields
    @FindBy(xpath = "//input[@id='datepick_in']")
    private WebElement checkInDate;

    @FindBy(xpath = "//input[@id='datepick_out']")
    private WebElement checkOutDate;

    // Search button
    @FindBy(xpath = "//input[@id='Submit']")
    private WebElement searchButton;

    public SearchHotelPage(WebDriver driver) {
        super(driver);
        PageFactory.initElements(driver, this); // Initialize page elements
    }

    // Generic method to select a value from a dropdown
    private void selectDropdownByText(WebElement element, String value) {
        wait.until(ExpectedConditions.elementToBeClickable(element)); // Ensure element is clickable
        new Select(element).selectByVisibleText(value);
    }

    // Dropdown selection methods
    public void selectLocation(String location) {
        selectDropdownByText(locationDropdown, location);
    }

    public void selectHotel(String hotel) {
        selectDropdownByText(hotelDropdown, hotel);
    }

    public void selectRoomType(String roomType) {
        selectDropdownByText(roomTypeDropdown, roomType);
    }

    public void selectRoomNos(String roomNos) {
        selectDropdownByText(roomNosDropdown, roomNos);
    }

    public void selectAdultsPerRoom(String adults) {
        selectDropdownByText(adultsPerRoomDropdown, adults);
    }

    public void selectChildrenPerRoom(String children) {
        selectDropdownByText(childrenPerRoomDropdown, children);
    }

    // Date selection methods
    public void enterCheckInDate(String checkIn) {
        wait.until(ExpectedConditions.elementToBeClickable(checkInDate)).clear();
        checkInDate.sendKeys(checkIn);
    }

    public void enterCheckOutDate(String checkOut) {
        wait.until(ExpectedConditions.elementToBeClickable(checkOutDate)).clear();
        checkOutDate.sendKeys(checkOut);
    }

    // Click the search button and navigate to the next page
    public SelectHotelPage clickSearch() {
        wait.until(ExpectedConditions.elementToBeClickable(searchButton)).click();
        return new SelectHotelPage(driver);
    }
}

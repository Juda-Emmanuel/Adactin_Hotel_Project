package com.testcases;

import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import com.base.BaseTest;
import com.pageobjectmodels.BookingPage;
import com.utils.ExcelUtils;

public class BookingHotelTest extends BaseTest {

    private BookingPage bookingPage;

    // Initialize BookingPage before each test
    @BeforeMethod
    public void setupPages() {
        bookingPage = new BookingPage(driver);
    }

    // Fetch test data from Excel
    @DataProvider(name = "bookingData")
    public Object[][] getBookingData() {
        return ExcelUtils.getExcelData(
                "C:\\Users\\lenovo\\eclipse-workspace\\Adactin_Hotel\\src\\test\\resource\\TestData.xlsx",
                "Booking_Data");
    }

    // Test booking functionality with different data sets
    @Test(dataProvider = "bookingData")
    public void bookingHotelTest(String username, String password, String location, String hotel, String roomType,
                                 String roomNos, String adults, String children, String firstName, String lastName, String billingAddress,
                                 String ccNumber, String ccType, String expMonth, String expYear, String cvv) {

        // Perform login
        bookingPage.login(username, password);

        // Search for hotel
        bookingPage.searchHotel(location, hotel, roomType, roomNos, adults, children);

        // Enter check-in and check-out dates
        String checkInDate = bookingPage.getFutureDate(3);
        String checkOutDate = bookingPage.getFutureDate(8);
        bookingPage.enterCheckInCheckOutDates(checkInDate, checkOutDate);

        // Click search and proceed with hotel selection
        bookingPage.clickSearch();
        bookingPage.selectHotel();
        bookingPage.clickContinue();

        // Enter booking details and confirm booking
        bookingPage.enterBookingDetails(firstName, lastName, billingAddress, ccNumber, ccType, expMonth, expYear, cvv);
        bookingPage.clickBook();

        // Validate booking confirmation
        boolean isConfirmed = bookingPage.isBookingConfirmed();
        Assert.assertTrue(isConfirmed, "Booking failed! Confirmation not displayed.");

        // Log assertion results
        System.out.println("✅ Booking confirmed for User: " + username);
        System.out.println("📜 Booking successful! Order No: " + bookingPage.getOrderNumber());
    }
}

package com.testcases;

import java.time.Duration;
import java.util.concurrent.TimeoutException;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import com.base.BaseTest;
import com.pageobjectmodels.BookingPage;
import com.pageobjectmodels.HomePage;
import com.pageobjectmodels.SearchHotelPage;
import com.pageobjectmodels.SelectHotelPage;

public class BookingTest extends BaseTest {

	protected WebDriver driver;
	protected WebDriverWait wait;
	protected HomePage homePage;
	protected SearchHotelPage searchHotelPage;
	protected SelectHotelPage selectHotelPage;
	protected BookingPage bookingPage;

	@BeforeMethod
	public void setUp() {
		// ✅ Initialize WebDriver before PageObjects
		driver = initializeDriver("chrome"); // Ensure 'initializeDriver()' is implemented in BaseTest
		wait = new WebDriverWait(driver, Duration.ofSeconds(10));

		// ✅ Load the website
		driver.get(properties.getProperty("baseURL"));

		// ✅ Initialize PageObjects AFTER driver is initialized
		homePage = new HomePage(driver);
		searchHotelPage = new SearchHotelPage(driver);
		selectHotelPage = new SelectHotelPage(driver);
		bookingPage = new BookingPage(driver);
	}

	@DataProvider(name = "bookingDataProvider")
	public Object[][] bookingDataProvider() {
		return new Object[][]{
			{"JudaEmmanuel", "Apria@2020", "Sydney", "Hotel Creek", "Deluxe", "1 - One", "2 - Two", 
			 "10/02/2025", "15/02/2025", "John", "Doe", "123 Street, Sydney",
			 "4111111111111111", "VISA", "March", "2027", "123"},
		};
	}

	@Test(dataProvider = "bookingDataProvider")
	public void bookingTest(String username, String password, String location, String hotel, String roomType, 
	                        String roomNos, String adults, String checkIn, String checkOut, 
	                        String firstName, String lastName, String address, String ccNumber, 
	                        String ccType, String expMonth, String expYear, String cvv) throws TimeoutException {

		// ✅ Login
		homePage.enterUsername(username);
		homePage.enterPassword(password);
		homePage.clickLogin();

		// ✅ Search for a hotel
		searchHotelPage.selectLocation(location);
		searchHotelPage.selectHotel(hotel);
		searchHotelPage.selectRoomType(roomType);
		searchHotelPage.selectRoomNos(roomNos);
		searchHotelPage.selectAdultsPerRoom(adults);
		searchHotelPage.enterCheckInDate(checkIn);
		searchHotelPage.enterCheckOutDate(checkOut);
		searchHotelPage.clickSearch();

		// ✅ Select hotel
		selectHotelPage.selectHotel();
		selectHotelPage.clickContinue();

		// ✅ Enter booking details
		bookingPage.enterBookingDetails(firstName, lastName, address, ccNumber, ccType, expMonth, expYear, cvv);
		bookingPage.clickBook();

		// ✅ Assert booking success
		assert bookingPage.isBookingSuccessful() : "Booking failed!";
		System.out.println("Booking successful! Order No: " + bookingPage.getOrderNumber());
	}
}

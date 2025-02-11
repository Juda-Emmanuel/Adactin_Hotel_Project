package com.testcases;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.concurrent.TimeoutException;

import org.testng.Assert;
import org.testng.ITestResult;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import com.base.BaseTest;
import com.pageobjectmodels.BookingPage;
import com.pageobjectmodels.HomePage;
import com.pageobjectmodels.SearchHotelPage;
import com.pageobjectmodels.SelectHotelPage;
import com.utils.ExcelUtils;

public class SelectHotelTest extends BaseTest {

	private HomePage homePage;
	private SearchHotelPage searchHotelPage;
	private SelectHotelPage selectHotelPage;
	private BookingPage bookingPage;

	// Initialize page objects before each test
	@BeforeMethod
	public void setupPages() {
		homePage = new HomePage(driver);
		searchHotelPage = new SearchHotelPage(driver);
	}

	// Fetch test data from Excel
	@DataProvider(name = "hotelSelectionData")
	public Object[][] getHotelSelectionData() {
		return ExcelUtils.getExcelData(
				"C:\\Users\\lenovo\\eclipse-workspace\\Adactin_Hotel\\src\\test\\resource\\TestData.xlsx",
				"Search_Hotel_Data");
	}

	// Test case to select a hotel after searching
	@Test(dataProvider = "hotelSelectionData")
	public void selectHotelTest(String username, String password, String location, String hotel, String roomType,
			String roomNos, String adults, String children) throws TimeoutException {

		test = extent.createTest("Select Hotel Test - " + username);

		System.out.println("Starting Select Hotel Test for user: " + username);

		// Login
		homePage.enterUsername(username);
		homePage.enterPassword(password);
		searchHotelPage = homePage.clickLogin();

		// Verify login success
		Assert.assertNotNull(searchHotelPage, "Login failed! SearchHotelPage is NULL.");
		System.out.println("Login successful!");
		test.pass("Login successful for: " + username);

		// Enter hotel search details
		searchHotelPage.selectLocation(location);
		searchHotelPage.selectHotel(hotel);
		searchHotelPage.selectRoomType(roomType);
		searchHotelPage.selectRoomNos(roomNos);
		searchHotelPage.selectAdultsPerRoom(adults);
		searchHotelPage.selectChildrenPerRoom(children);

		// Set check-in and check-out dates
		String checkInDate = getFutureDate(3);
		String checkOutDate = getFutureDate(8);
		searchHotelPage.enterCheckInDate(checkInDate);
		searchHotelPage.enterCheckOutDate(checkOutDate);
		System.out.println("Hotel search details entered.");
		test.info("Check-in Date: " + checkInDate + " | Check-out Date: " + checkOutDate);

		// Perform hotel search
		selectHotelPage = searchHotelPage.clickSearch();

		// Verify search results
		Assert.assertNotNull(selectHotelPage, "Hotel search failed! SelectHotelPage is NULL.");
		Assert.assertTrue(selectHotelPage.isHotelListDisplayed(), "Hotel list is not displayed!");
		System.out.println("Hotel search results displayed.");
		test.pass("Hotel list displayed successfully.");

		// Select a hotel and proceed
		selectHotelPage.selectHotel();
		bookingPage = selectHotelPage.clickContinue();

		// Verify booking page navigation
		Assert.assertNotNull(bookingPage, "Booking page is not loaded after selecting the hotel.");
		System.out.println("Hotel selection successful for user: " + username);
		test.pass("Successfully navigated to booking page.");
	}

	// Utility function to get a future date
	private String getFutureDate(int daysFromToday) {
		Calendar calendar = Calendar.getInstance();
		calendar.add(Calendar.DAY_OF_MONTH, daysFromToday);
		SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
		return sdf.format(calendar.getTime());
	}

	// Capture screenshot on failure and close browser after each test
	@AfterMethod(alwaysRun = true)
	public void tearDown(ITestResult result) {
		if (result.getStatus() == ITestResult.FAILURE) {
			System.out.println("Test failed: " + result.getName());
			String screenshotPath = captureScreenshot(result.getName());
			test.fail("Test failed: " + result.getThrowable().getMessage()).addScreenCaptureFromPath(screenshotPath);
		} else if (result.getStatus() == ITestResult.SUCCESS) {
			test.pass("Test passed successfully.");
		}
		extent.flush();
		super.tearDown(result);
	}
}

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
import com.pageobjectmodels.HomePage;
import com.pageobjectmodels.SearchHotelPage;
import com.pageobjectmodels.SelectHotelPage;
import com.utils.ExcelUtils;

public class SearchHotelTest extends BaseTest {

	private HomePage homePage;
	private SearchHotelPage searchHotelPage;
	private SelectHotelPage selectHotelPage;

	// Initialize Page Objects before each test
	@BeforeMethod
	public void setupPages() {
		homePage = new HomePage(driver);
		searchHotelPage = new SearchHotelPage(driver);
	}

	// Read data from Excel for Search Hotel Test
	@DataProvider(name = "hotelSearchData")
	public Object[][] getHotelSearchData() {
		return ExcelUtils.getExcelData(
				"C:\\Users\\lenovo\\eclipse-workspace\\Adactin_Hotel\\src\\test\\resource\\TestData.xlsx",
				"Search_Hotel_Data");
	}

	// Test for searching a hotel
	@Test(dataProvider = "hotelSearchData")
	public void searchHotelTest(String username, String password, String location, String hotel, String roomType,
			String roomNos, String adults, String children) throws TimeoutException {

		test = extent.createTest("Search Hotel Test - " + username);

		// Login with provided credentials
		homePage.enterUsername(username);
		homePage.enterPassword(password);
		searchHotelPage = homePage.clickLogin();

		// Verify login success
		System.out.println("Verifying login success for: " + username);
		Assert.assertNotNull(searchHotelPage, "Login failed! SearchHotelPage is NULL.");
		test.info("Login successful for " + username);

		// Fill search hotel form
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
		System.out.println("Check-in Date: " + checkInDate + " | Check-out Date: " + checkOutDate);

		// Click Search and verify results
		selectHotelPage = searchHotelPage.clickSearch();
		Assert.assertNotNull(selectHotelPage, "Hotel search failed! SelectHotelPage is NULL.");
		test.pass("Hotel search successful");

		// Verify that the hotel list is displayed
		System.out.println("Verifying if hotel list is displayed...");
		Assert.assertTrue(selectHotelPage.isHotelListDisplayed(), "Hotel list is not displayed.");
		test.pass("Hotel list displayed successfully");
	}

	// Utility method to get a future date in "dd/MM/yyyy" format
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

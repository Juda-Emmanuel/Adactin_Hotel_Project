package com.testcases;

import org.testng.Assert;
import org.testng.ITestResult;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import com.base.BaseTest;
import com.pageobjectmodels.LoginPage;
import com.utils.ExcelUtils;

public class LoginTest extends BaseTest {

	@Test(dataProvider = "LoginData")
	public void loginTest(String username, String password, String expectedResult) {
		test = extent.createTest("Login Test - " + username);
		LoginPage loginPage = new LoginPage(driver);

		// Perform login
		loginPage.enterUsername(username);
		loginPage.enterPassword(password);
		loginPage.clickLogin();

		// Check login result based on expected outcome
		if (expectedResult.equalsIgnoreCase("Valid")) {
			String expectedUrl = "https://adactinhotelapp.com/SearchHotel.php";
			test.info("Valid login attempt for username: " + username)
					.addScreenCaptureFromPath(captureScreenshot("ValidLoginInfo"));

			Assert.assertEquals(driver.getCurrentUrl(), expectedUrl, "Login Failed with valid credentials!");

			test.pass("Login successful with valid credentials.")
					.addScreenCaptureFromPath(captureScreenshot("ValidLoginPass"));
		} else {
			test.info("Invalid login attempt for username: " + username)
					.addScreenCaptureFromPath(captureScreenshot("InvalidLoginInfo"));

			boolean isErrorDisplayed = loginPage.isErrorMessageDisplayed();
			Assert.assertTrue(isErrorDisplayed, "Error message not displayed for invalid login!");

			test.pass("Error message displayed correctly for invalid login.")
					.addScreenCaptureFromPath(captureScreenshot("InvalidLoginPass"));
		}
	}

	// Fetch login data from Excel
	@DataProvider(name = "LoginData")
	public Object[][] getData() {
		return ExcelUtils.getExcelData(
				"C:\\Users\\lenovo\\eclipse-workspace\\Adactin_Hotel\\src\\test\\resource\\TestData.xlsx",
				"Login_Data");
	}

	// Capture test results and clean up
	@AfterMethod(alwaysRun = true)
	public void tearDown(ITestResult result) {
		if (result.getStatus() == ITestResult.FAILURE) {
			String screenshotPath = captureScreenshot(result.getName());
			test.fail("Test failed: " + result.getThrowable().getMessage()).addScreenCaptureFromPath(screenshotPath);
		} else if (result.getStatus() == ITestResult.SUCCESS) {
			test.pass("Test passed successfully.").addScreenCaptureFromPath(captureScreenshot("TestPass"));
		}
		extent.flush();
		super.tearDown(result);
	}
}

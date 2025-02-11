package com.base;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.time.Duration;
import java.util.Date;
import java.util.Properties;

import org.apache.commons.io.FileUtils;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.edge.EdgeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.ITestResult;
import org.testng.annotations.*;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.reporter.ExtentSparkReporter;

import io.github.bonigarcia.wdm.WebDriverManager;

public class BaseTest {
	protected WebDriver driver;
	protected WebDriverWait wait;
	protected Properties properties;
	protected static ExtentReports extent;
	protected ExtentTest test;

	// Initialize Extent Reports
	@BeforeSuite
	public void setupExtent() {
		ExtentSparkReporter sparkReporter = new ExtentSparkReporter(
				"C:\\Users\\lenovo\\eclipse-workspace\\Adactin_Hotel\\Reports\\ExtentReport.html");
		extent = new ExtentReports();
		extent.attachReporter(sparkReporter);
	}

	// Setup WebDriver before each test
	@BeforeMethod
	@Parameters("browser")
	public void setUp(@Optional("chrome") String browser) {
		loadConfig();
		driver = initializeDriver(browser);
		wait = new WebDriverWait(driver, Duration.ofSeconds(10));
		driver.get(properties.getProperty("baseURL"));
		System.out.println("Browser launched: " + properties.getProperty("baseURL"));
	}

	// Initialize WebDriver based on browser type
	public WebDriver initializeDriver(String browser) {
		if (browser == null || browser.isEmpty()) {
			browser = properties.getProperty("browser");
		}
		switch (browser.toLowerCase()) {
		case "chrome":
			WebDriverManager.chromedriver().setup();
			return new ChromeDriver(new ChromeOptions().addArguments("--start-maximized"));

		case "firefox":
			WebDriverManager.firefoxdriver().setup();
			FirefoxOptions firefoxOptions = new FirefoxOptions();
			firefoxOptions.addArguments("--width=1920", "--height=1080"); // Ensure maximization
			WebDriver firefoxDriver = new FirefoxDriver(firefoxOptions);
			firefoxDriver.manage().window().maximize(); // Manual maximization
			return firefoxDriver;

		case "edge":
			WebDriverManager.edgedriver().setup();
			return new EdgeDriver(new EdgeOptions().addArguments("--start-maximized"));

		default:
			throw new IllegalArgumentException("Invalid browser: " + browser);
		}
	}

	// Load configuration properties
	private void loadConfig() {
		try {
			properties = new Properties();
			FileInputStream file = new FileInputStream("./src/test/resource/config.properties");
			properties.load(file);
			System.out.println("Config properties loaded.");
		} catch (IOException e) {
			System.err.println("Error loading config.properties: " + e.getMessage());
		}
	}

	// Capture a screenshot
	public String captureScreenshot(String testName) {
		try {
			String timestamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
			String screenshotPath = "C:\\Users\\lenovo\\eclipse-workspace\\Adactin_Hotel\\Screenshots\\" + testName
					+ "_" + timestamp + ".png";
			File screenshotFile = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
			FileUtils.copyFile(screenshotFile, new File(screenshotPath));
			System.out.println("Screenshot saved: " + screenshotPath);
			return new File(screenshotPath).getAbsolutePath();
		} catch (IOException e) {
			System.err.println("Failed to capture screenshot: " + e.getMessage());
			return null;
		}
	}

	// Handle test result logging and cleanup
	@AfterMethod(alwaysRun = true)
	public void tearDown(ITestResult result) {
		if (test != null) {
			if (result.getStatus() == ITestResult.FAILURE) {
				String screenshotPath = captureScreenshot(result.getName());
				test.fail("Test Failed: " + result.getThrowable());
				if (screenshotPath != null) {
					test.addScreenCaptureFromPath(screenshotPath);
				}
				System.out.println("Test Failed: " + result.getName());
			} else if (result.getStatus() == ITestResult.SUCCESS) {
				test.pass("Test Passed");
				System.out.println("Test Passed: " + result.getName());
			} else {
				test.skip("Test Skipped: " + result.getThrowable());
				System.out.println("Test Skipped: " + result.getName());
			}
		}
		if (driver != null) {
			driver.quit();
			System.out.println("Browser closed.");
		}
	}

	// Flush Extent Reports
	@AfterSuite
	public void tearDownExtent() {
		if (extent != null) {
			extent.flush();
			System.out.println("Extent Report generated.");
		}
	}
}

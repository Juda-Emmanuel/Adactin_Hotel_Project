package com.utils;

import java.io.File;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.reporter.ExtentSparkReporter;
import com.aventstack.extentreports.reporter.configuration.Theme;

public class ExtentManager {

	private static ExtentReports extent;

    public static ExtentReports getInstance() {
        if (extent == null) {
            // Define the exact report path
            String reportDirPath = "C:\\Users\\lenovo\\eclipse-workspace\\Adactin_Hotel\\Reports\\";
            String reportPath = reportDirPath + "ExtentReport.html";

            // Ensure the Reports directory exists
            File reportDir = new File(reportDirPath);
            if (!reportDir.exists()) {
                reportDir.mkdir();  // Creates the directory if not present
            }

            // Set up ExtentReports
            ExtentSparkReporter sparkReporter = new ExtentSparkReporter(reportPath);
            sparkReporter.config().setDocumentTitle("Automation Test Report");
            sparkReporter.config().setReportName("Adactin Hotel Test Execution Report");
            sparkReporter.config().setTheme(Theme.STANDARD);

            extent = new ExtentReports();
            extent.attachReporter(sparkReporter);

            // Additional system info
            extent.setSystemInfo("Senior_Automation_Tester", "Juda Emmanuuel");
            extent.setSystemInfo("Browser", "Chrome, Firefox, Edge");
            extent.setSystemInfo("Environment", "QA");
        }
        return extent;
    }
}

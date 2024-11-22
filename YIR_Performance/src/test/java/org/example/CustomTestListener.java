package org.example;

import com.aventstack.extentreports.MediaEntityBuilder;
import com.aventstack.extentreports.ExtentTest;
import org.openqa.selenium.WebDriver;
import org.testng.ITestContext;
import org.testng.ITestListener;
import org.testng.ITestResult;

public class CustomTestListener implements ITestListener {

    private ThreadLocal<ExtentTest> extentTest = new ThreadLocal<>();
    private WebDriver driver;

    // Set the driver in the listener
    public void setDriver(WebDriver driver) {
        this.driver = driver;
    }

    @Override
    public void onStart(ITestContext context) {
        // Initialize ExtentReports or setup logging here
    }

    @Override
    public void onTestFailure(ITestResult result) {
        if (driver != null) {
            String screenshotPath = ScreenshotUtils.captureScreenshot(driver, result.getName());
            extentTest.get().fail("Test failed",
                    MediaEntityBuilder.createScreenCaptureFromPath(screenshotPath).build());
        }
    }

    // Other ITestListener methods can be implemented if needed
    @Override
    public void onTestStart(ITestResult result) {
        // Any setup before test starts
    }

    @Override
    public void onTestSkipped(ITestResult result) {
        // Any actions on skipped tests
    }

    @Override
    public void onTestSuccess(ITestResult result) {
        // Actions on test success
    }

    @Override
    public void onFinish(ITestContext context) {
        // Cleanup or finalize reports
    }
}

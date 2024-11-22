package org.example;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.MediaEntityBuilder;
import com.aventstack.extentreports.reporter.ExtentSparkReporter;
import org.openqa.selenium.WebDriver;
import org.testng.ITestContext;
import org.testng.ITestListener;
import org.testng.ITestResult;

import java.io.IOException;

public class ExtentTestNGReportListener implements ITestListener {

    private ExtentReports extent;
    private static ThreadLocal<ExtentTest> test = new ThreadLocal<>();

    // Called before any test starts
    @Override
    public void onStart(ITestContext context) {
        ExtentSparkReporter sparkReporter = new ExtentSparkReporter("extent-report.html");
        extent = new ExtentReports();
        extent.attachReporter(sparkReporter);
    }

    // Called when a test starts
    @Override
    public void onTestStart(ITestResult result) {
        ExtentTest extentTest = extent.createTest(result.getMethod().getMethodName());
        test.set(extentTest);  // Set the test in the current thread
    }

    // Called when a test is successful

    @Override
    public void onTestSuccess(ITestResult result) {
        test.get().pass("---------------------------------------");
    }

    // Called when a test fails
    @Override
    public void onTestFailure(ITestResult result) {

        WebDriver driver = (WebDriver) result.getTestContext().getAttribute("WebDriver");

        if (driver != null) {
            System.out.println("Driver is not null");
            String screenshotPath = ScreenshotUtils.captureScreenshot(driver, result.getName());
            if (screenshotPath != null && !screenshotPath.isEmpty()) {
                System.out.println("screenshotPath : " + screenshotPath);
                // Mark the test as failed in the Extent report
                test.get().fail(result.getThrowable());
                test.get().fail("Test failed",
                        MediaEntityBuilder.createScreenCaptureFromPath(screenshotPath).build());
            } else {
                test.get().fail("Test failed: Screenshot not available");
            }
        }else{
            System.out.println("Driver is null");
        }
    }

    // Called when a test is skipped
    @Override
    public void onTestSkipped(ITestResult result) {
        test.get().skip("Test skipped");
    }


    @Override
    public void onFinish(ITestContext context) {
        extent.flush();
    }

    public static ExtentTest getTest() {
        return test.get();
    }
}

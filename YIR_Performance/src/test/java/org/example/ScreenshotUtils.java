package org.example;

import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.io.FileHandler;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class ScreenshotUtils {

    public static String captureScreenshot(WebDriver driver, String screenshotName) {
        String dateName = new SimpleDateFormat("MMddhhmmss").format(new Date());
        File source = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
        String destination = "screenshots/" + screenshotName + dateName + ".png";
        File finalDestination = new File(destination);

        try {
            FileHandler.copy(source, finalDestination);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return destination;  // Return the path to the screenshot
    }
}

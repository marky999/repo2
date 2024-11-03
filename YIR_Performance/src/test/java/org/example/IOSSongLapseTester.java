package org.example;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import io.appium.java_client.AppiumDriver;
import io.appium.java_client.PerformsTouchActions;
import io.appium.java_client.TouchAction;
import io.appium.java_client.ios.IOSDriver;
import io.appium.java_client.ios.options.XCUITestOptions;
import io.appium.java_client.touch.WaitOptions;
import io.appium.java_client.touch.offset.PointOption;
import org.openqa.selenium.*;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.interactions.PointerInput;
import org.openqa.selenium.interactions.Sequence;
import org.openqa.selenium.remote.DesiredCapabilities;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.net.URL;
import java.net.MalformedURLException;
import java.time.Duration;
import java.util.*;

import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;

public class IOSSongLapseTester {

    private AppiumDriver driver;
    private WebDriverWait wait;
    private Helper helper;
    private ValidateEachFrame validateEachFrame;
    // Constructor
    public IOSSongLapseTester(AppiumDriver driver, WebDriverWait wait, Helper helper, ValidateEachFrame validateEachFrame) {
        this.driver = driver;
        this.wait = wait;
        this.helper = helper;
    }


    public void verifyTheMyTopSongs2024(){
        //===   Close the YIR reel and verify My Top Songs 2024 page  ===//
        wait = new WebDriverWait(driver, Duration.ofSeconds(20)); // 10 seconds max wait
        wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//*[@name=\"Button,Recap_StoryView_DeeplinkButton\"]")));
        driver.findElement(By.xpath("//*[@name=\"Button,Recap_StoryView_DeeplinkButton\"]")).click();
        WebElement element = driver.findElement(By.id("AMMPageHeaderHeadlineAccessibilityIdentifier"));
        Assert.assertEquals(element.getAttribute("label"), "My Top Songs 2024");
        System.out.println("Came to \"My Top Songs 2024\" page ");
    }
    public void navigateAndValidate() throws MalformedURLException, InterruptedException, FileNotFoundException {
       // ValidateEachFrame validateEachFrame = new ValidateEachFrame(driver);
       // helper = new Helper(driver);

        //+++++++++++++ Test Cases +++++++++++//

        boolean isFound = false;
        while (!isFound) {
            validateEachFrame.openSharePageOtherwiseGotoNextPage();
            //Check whether you came to last page
            isFound = helper.isElementExist(By.xpath("//*[@name=\"Button,Recap_StoryView_DeeplinkButton\"]"));
        }
    }



//    public static void main(String[] args) throws Exception {
//        IOSSongLapseTester tester = new IOSSongLapseTester();
//
//        tester.setup();
//        JasonExtraction.extractJson();
//        tester.navigateAndValidate();
//        tester.verifyTheMyTopSongs2024();
//        tester.tearDown();
//    }
//    public void tearDown() {
//        driver.quit();
//    }
}

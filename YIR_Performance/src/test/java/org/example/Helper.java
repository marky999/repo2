package org.example;

import io.appium.java_client.AppiumDriver;
import org.openqa.selenium.*;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.interactions.PointerInput;
import org.openqa.selenium.interactions.Sequence;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.text.NumberFormat;
import java.text.ParseException;
import java.time.Duration;
import java.util.*;

import static org.testng.Assert.assertNotNull;

public class Helper {
    private AppiumDriver driver;
    private WebDriverWait wait;
    public final String shareSheet_cancelButton =  "//*[@name=\"IconButton,ShareSheet_CancelButton\"]" ;
    public final String cancelButton = "//XCUIElementTypeButton[@name=\"\u200ECancel\"]";
    public final String weFound_xpath = "//XCUIElementTypeStaticText[@name=\"We spent \"]";


    public Helper(AppiumDriver driver, WebDriverWait wait){
        this.driver = driver;
        this.wait = wait;
    }

    public boolean elementFound(String xpath, int waitDuration ) {
        wait = new WebDriverWait(driver, Duration.ofSeconds(waitDuration));
        try {
            WebElement element = wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(xpath)));
            assertNotNull(element);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public  void nextPage() throws InterruptedException {
        Dimension screenSize = driver.manage().window().getSize();
        int screenWidth = screenSize.width;
        int screenHeight = screenSize.height;

        // Calculate coordinates for the right side
        int x = (int) (screenWidth * 0.9); // Adjust 0.9 to control how far right you want
        int y = screenHeight / 2;          // Middle of the screen height

        // Perform the tap
        PointerInput finger = new PointerInput(PointerInput.Kind.TOUCH, "finger");
        Sequence tap = new Sequence(finger, 1)
                .addAction(finger.createPointerMove(Duration.ZERO, PointerInput.Origin.viewport(), x, y))
                .addAction(finger.createPointerDown(PointerInput.MouseButton.LEFT.asArg()))
                .addAction(finger.createPointerUp(PointerInput.MouseButton.LEFT.asArg()));

        // Perform the action
        System.out.println("Go to Next Page");
        driver.perform(Collections.singletonList(tap));
        Thread.sleep(1500);
    }

    public String getMonth(String peakListeningMonth){
        if ("1".equals(peakListeningMonth)) {
            peakListeningMonth = "January";
        } else if ("2".equals(peakListeningMonth)) {
            peakListeningMonth = "February";
        } else if ("3".equals(peakListeningMonth)) {
            peakListeningMonth = "March";
        } else if ("4".equals(peakListeningMonth)) {
            peakListeningMonth = "April";
        } else if ("5".equals(peakListeningMonth)) {
            peakListeningMonth = "May";
        } else if ("6".equals(peakListeningMonth)) {
            peakListeningMonth = "June";
        } else if ("7".equals(peakListeningMonth)) {
            peakListeningMonth = "July";
        } else if ("8".equals(peakListeningMonth)) {
            peakListeningMonth = "August";
        } else if ("9".equals(peakListeningMonth)) {
            peakListeningMonth = "September";
        } else if ("10".equals(peakListeningMonth)) {
            peakListeningMonth = "October";
        } else if ("11".equals(peakListeningMonth)) {
            peakListeningMonth = "November";
        } else if ("12".equals(peakListeningMonth)) {
            peakListeningMonth = "December";
        } else {
            peakListeningMonth = "MONTH NOT FOUND";
        }
        return peakListeningMonth;
    }

    public boolean isShareButtonExist() {
        try {
            WebElement actionButton = driver.findElement(By.id("ActionButton_View"));
            String buttonLabel = actionButton.getText();
            if ("share".equals(buttonLabel)) {
                return true;
            }
        } catch (NoSuchElementException e) {
            System.out.println("Share button not found");
            return false;
        }
        return false;
    }
    public int getRandomNumber(){
        Random rand = new Random();
        rand.setSeed(System.currentTimeMillis());
        return rand.nextInt();
    }
    public void openShareCard(String caseName) throws InterruptedException {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10)); // 10 seconds max wait
        System.out.println("Open Share card");
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("ActionButton_View"))).click();
        Random rand = new Random();
        rand.setSeed(System.currentTimeMillis());
        int num = rand.nextInt();
        //Thread.sleep(2000);
       // captureScreenshot("screenshots/share_screenshot" + num + ".png");
        checkShareButtons(caseName);
        openAndCloseShareButtons();
        System.out.println("Close Share card");
        driver.findElement(By.xpath(shareSheet_cancelButton)).click();
        Thread.sleep(2000);
    }

    public boolean isElementExist(By xpath) {
//        try {
//            if (!driver.findElements(xpath).isEmpty()) {
//                return true;
//            }
//        } catch (NoSuchElementException e) {
//            System.out.println("element not found");
//            return false;
//        }
//        return false;
        return !driver.findElements(xpath).isEmpty();
    }


    public String captureScreenshot(AppiumDriver driver, String filePath) {
        File screenshotDirectory = new File(filePath).getParentFile();
        if (!screenshotDirectory.exists()) {
            screenshotDirectory.mkdirs(); // Create directories
        }

        try {
            // Capture screenshot and save it to the specified path
            File screenshot = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
            File destinationFile = new File(filePath);
            Files.copy(screenshot.toPath(), destinationFile.toPath(), StandardCopyOption.REPLACE_EXISTING);
            System.out.println("Screenshot saved at: " + filePath);
        } catch (WebDriverException e) {
            System.out.println("Error capturing screenshot: " + e.getMessage());
        } catch (IOException e) {
            System.out.println("Error saving screenshot: " + e.getMessage());
        }

        return filePath;
    }


    public void goBack() throws InterruptedException {
        int x = 50; // X coordinate
        int y = 20; // Y coordinate

        PointerInput finger = new PointerInput(PointerInput.Kind.TOUCH, "finger");
        Sequence tap = new Sequence(finger, 1)
                .addAction(finger.createPointerMove(Duration.ZERO, PointerInput.Origin.viewport(), x, y))
                .addAction(finger.createPointerDown(PointerInput.MouseButton.LEFT.asArg()))
                .addAction(finger.createPointerUp(PointerInput.MouseButton.LEFT.asArg()));

        // Perform the action
        System.out.println("Go to Previous Page");
        driver.perform(Collections.singletonList(tap));
        Thread.sleep(2500);
    }


    public boolean goNextFrame(String  xpath) throws InterruptedException {
        int i = 0;
        while (!isElementExist(By.xpath(xpath))) {
            driver.findElement(By.xpath("//*[@name=\"Recap_LeftRightTapHandler_TouchableOpacityRight\"]")).click();
            //System.out.println("Go next frame");
            Thread.sleep(2000);//Do not remove
            if(i++ > 16){
                System.out.println("Frame Not found");
                return false;
            }
        }
        System.out.println("Found the frame");
        return true;
    }
    public void goNextFrame(int n) throws InterruptedException {
       for(int i = 0; i < n; i++){
           driver.findElement(By.xpath("//*[@name=\"Recap_LeftRightTapHandler_TouchableOpacityRight\"]")).click();
           Thread.sleep(1000);
       }
    }

    public String formatWithCommas(String numberString) {
        try {
            // Parse the string to a number
            Number number = NumberFormat.getInstance().parse(numberString);

            // Format the number with commas
            return NumberFormat.getNumberInstance().format(number);
        } catch (ParseException e) {
            e.printStackTrace();
            return numberString; // Return original if parsing fails
        }
    }

    public void openAndCloseShareButtons() throws InterruptedException {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10)); // 10 seconds max wait
        List<String> buttonIds = Arrays.asList(
                "IconButton,ShareSheet_Instagram",
                "IconButton,ShareSheet_Facebook",
                "IconButton,ShareSheet_Snapchat",
                "IconButton,ShareSheet_WhatsApp",
                "IconButton,ShareSheet_Messages"
        );

        for (String buttonId : buttonIds) {
            driver.findElement(By.id(buttonId)).click();

            // Add a delay before the next action
            Thread.sleep(3000); // Adjust as needed
            System.out.println("Open Share buttons: " + buttonId);
            captureScreenshot(driver, "screenshots/" + buttonId.split("_")[1] + "_screenshot.png");

            switch (buttonId) {
                case "IconButton,ShareSheet_Instagram":
                    driver.findElement(By.id("discard-story-preview")).click();
                    Thread.sleep(2000);
                    driver.findElement(By.id("Discard")).click();
                    break;

                case "IconButton,ShareSheet_Facebook":
                    driver.findElement(By.id("exit_back_button")).click();
                    Thread.sleep(3000);
                    driver.findElement(By.id("Discard")).click();
                    break;

                case "IconButton,ShareSheet_Snapchat":
                    driver.findElement(By.id("discard")).click();
                    Thread.sleep(2000);
                    try {
                        driver.findElement(By.id("abandonment_alert_conf_button")).click();
                    } catch (Exception e) {
                        // Handle exception if needed
                    }
                    break;

                case "IconButton,ShareSheet_WhatsApp":
                    driver.findElement(By.xpath(cancelButton)).click();
                    break;

                case "IconButton,ShareSheet_Messages":
                    driver.findElement(By.id("Cancel")).click();
                    break;

                default:
                    System.out.println("No specific actions defined for: " + buttonId);
                    break;
            }
            Thread.sleep(3000);
            goBack();

        }

    }

    public void checkShareButtons(String caseName){
        List<String> buttonIds = Arrays.asList(
                "IconButton,ShareSheet_Instagram",
                "IconButton,ShareSheet_Facebook",
                "IconButton,ShareSheet_Snapchat",
                "IconButton,ShareSheet_WhatsApp",
                "IconButton,ShareSheet_Messages",
                "IconButton,ShareSheet_Clipboard"
        );

        boolean allDisplayed = true;
        for (String buttonId : buttonIds) {
            try {
                driver.findElement(By.id(buttonId)).isDisplayed();
            } catch (NoSuchElementException e) {
                System.out.println("Element not found: " + buttonId + " - " + e.getMessage());
                allDisplayed = false;
            }
        }

        if (allDisplayed) {
            System.out.println("All six buttons are shown.");
        } else {
            System.out.println("Some buttons are missing.");
        }
        captureScreenshot(driver, "screenshots/" + caseName + ".png");
    }
}

package org.example;

import com.google.gson.Gson;
import io.appium.java_client.AppiumDriver;
import org.openqa.selenium.By;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class ValidateEachFrame {
    private AppiumDriver driver;
    private Helper helper;
    WebDriverWait wait;// = new WebDriverWait(driver, Duration.ofSeconds(10));
    JasonExtraction jasonExtraction;

    public ValidateEachFrame(AppiumDriver driver, Helper helper, WebDriverWait wait) {
        this.driver = driver;
        this.helper = helper;
        this.wait = wait;
        jasonExtraction = new JasonExtraction();
    }

    public boolean isFirstHalfDisplayed(){
        wait = new WebDriverWait(driver, Duration.ofSeconds(120));
        try {
            wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//XCUIElementTypeStaticText[@name=\"First you were obsessed with \"]")));
            return true;
        }catch(TimeoutException e){
            helper.captureScreenshot(driver,"screenshots/error_firstHalf.png");
            return false;
        }
    }

  //  public getFirstHalf

    public void openSharePageOtherwiseGotoNextPage() throws InterruptedException {
        while(true)
        {
            if (helper.isShareButtonExist()) {
                helper.openShareCard("xxxx");
            } else {
                helper.nextPage();
                if(isLastFrame()){
                    break;
                }
            }
        }
    }

    public boolean genreFrameValidation(){
        boolean isElementPresent = false;
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(30));
        try {
            wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//*[@name=\"There were some\"]")));
            isElementPresent = true;
            System.out.println("Frame displayed");
        } catch (TimeoutException ignored) {
            System.out.println("Frame NOT displayed\n");
        }

        return isElementPresent;
    }

    public boolean isPartOfStringFound(String str) {
        try {
            List<WebElement> elements = driver.findElements(By.xpath("//XCUIElementTypeStaticText"));
            int i = 0;
            while (true) {
                System.out.println(Objects.requireNonNull(elements.get(i).getAttribute("label")));
                if (Objects.requireNonNull(elements.get(i).getAttribute("label")).contains(str)) {
                    return true;
                }
                i++;
                if(i>= elements.size())
                {
                    helper.captureScreenshot(driver,"screenshots/error_stringNotFound.png");
                    return false;
                }
            }

        } catch (Exception e) {
            helper.captureScreenshot(driver, "screenshots/error_stringNotFound.png");
        }
        return false;
    }

    public List<String> getTopGenres(String lastGenre) throws InterruptedException {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(30));
        List<String> genreArray = new ArrayList<>();

        WebElement element = wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(String.format("//*[@name='%s']", lastGenre))));

        try {
                System.out.println("'Top Genre' frame appeared.");
                List<WebElement> elements = driver.findElements(By.xpath("//XCUIElementTypeStaticText"));
                //System.out.println("elements.size() = " + elements.size());
                for (int i = 10; i < elements.size(); i++) {
                    genreArray.add(elements.get(i).getText());
                }
        } catch (Exception e ) {
            helper.captureScreenshot(driver, "screenshots/error_getTopGenres.png");
            System.out.println("Page NOT displayed\n");
        }
        return genreArray;
    }

    public List<String> getTopArtists()  {
        List<String> artistsArray = new ArrayList<>();
        try {
            List<WebElement> elements = driver.findElements(By.xpath("//XCUIElementTypeStaticText"));
            for (int i = 15; i < elements.size(); i++) {
                artistsArray.add(elements.get(i).getText());
            }
        } catch (Exception e ) {
            helper.captureScreenshot(driver, "screenshots/error_getTopArtists.png");
            System.out.println("Artists  NOT displayed fully\n");
        }
        return artistsArray.size() > 5 ? artistsArray.subList(0, Math.max(5, artistsArray.size() - 2)) : artistsArray;
    }

    public List<String> getTopPodcasts()  {
        List<String> artistsArray = new ArrayList<>();
        try {
            List<WebElement> elements = driver.findElements(By.xpath("//XCUIElementTypeStaticText"));
            for (int i = 14; i < elements.size(); i++) {
                artistsArray.add(elements.get(i).getText());
            }
        } catch (Exception e ) {
            helper.captureScreenshot(driver, "screenshots/error_getTopArtists.png");
            System.out.println("Artists  NOT displayed fully\n");
        }
      //  return artistsArray.size() > 5 ? artistsArray.subList(0, Math.max(5, artistsArray.size() - 2)) : artistsArray;
        return artistsArray;
    }
    public boolean isFirstPageDisplayed() {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10)); // 10 seconds max wait
        boolean isElementPresent = false;
        try {
            wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//*[@name=\"lineByLineTextContainer\"]")));
            isElementPresent = true;
            System.out.println("First page displayed");
        } catch (TimeoutException ignored) {
            System.out.println("page NOT displayed\n");
        }

        return isElementPresent;
    }

    public boolean isTotalStatsCorrect() {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(60)); // 60 seconds max wait
        boolean isElementPresent = false;
        try {
            wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//XCUIElementTypeStaticText[@name=\"We spent \"]")));
            WebElement element = driver.findElement(By.xpath("//XCUIElementTypeStaticText[contains(@name, 'minutes')]"));
            String labelText = element.getAttribute("name");
            // Deserialize the JSON file to obtain an instance of `Data` with total minutes
            JasonExtraction.Data data = new Gson().fromJson(new FileReader("cnf_prime.json"), JasonExtraction.Data.class);
            Assert.assertEquals(Integer.parseInt(labelText.split(" ")[0]), data.getTotalStats().getTracks().getTotalMinutes());
            System.out.println("We spent " + data.getTotalStats().getTracks().getTotalMinutes() + " minutes");

            isElementPresent = true;

        } catch (TimeoutException | IOException ignored) {
            System.out.println("\nWe spent NOT appeared or error reading JSON\n");
        }

        return isElementPresent;
    }


    public String getDecade() throws InterruptedException {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(30)); // 60 seconds max wait
        wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//*[@name=\"FadeInOut_Transformation_Animation\" and @label=\"Your year was all about the\"]")));
        Thread.sleep(1000);
        List<WebElement> elements = driver.findElements(By.xpath("//*[@name='FadeInOut_Transformation_Animation']"));
        return elements.get(1).getAttribute("label");
    }

    public boolean isLastFrame(){
        return !driver.findElements(By.xpath("//*[@name=\"Button,Recap_StoryView_DeeplinkButton\"]")).isEmpty();
    }
    public int getCountOfDecadeTrack() throws InterruptedException {
        String dynamicXPath = "//XCUIElementTypeOther[contains(@name, 'album-art-view-')]";
        int targetCount = 6;
        Duration timeout = Duration.ofSeconds(20);
        Instant endTime = Instant.now().plus(timeout);
        int count = 0;
        while (Instant.now().isBefore(endTime)) {
            // Find all matching elements
            List<WebElement> elements = driver.findElements(By.xpath(dynamicXPath));
            count = elements.size();

            // If the target count is reached, return the count
            if (elements.size() == targetCount) {
                System.out.println("Target count reached: " + count);
                return elements.size();
            }
            // Pause briefly
            Thread.sleep(1000);
        }
        // If the loop exits due to timeout, log a message
        System.out.println("Timeout reached. Target count not met.");
        return count;
    }
}



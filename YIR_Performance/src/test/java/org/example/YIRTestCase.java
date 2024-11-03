package org.example;

import com.google.gson.Gson;
import io.appium.java_client.AppiumDriver;
import io.appium.java_client.ios.IOSDriver;
import io.appium.java_client.ios.options.XCUITestOptions;
import org.json.JSONObject;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.*;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.net.URL;
import java.time.Duration;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import static org.testng.Assert.assertNotNull;

public class YIRTestCase {
    private WebDriverWait wait;
    private Helper helper;
    public AppiumDriver driver;
    public IOSSongLapseTester iossonglapseTester;
    public ValidateEachFrame validateEachFrame;
    Map<String, String> songs = JasonExtraction.songsMap;
    @BeforeClass(alwaysRun = true)
    public void classSetUp() throws Exception {
        JasonExtraction.extractJson();
    }

    @BeforeMethod(alwaysRun = true)
    public void setup() throws Exception {
        XCUITestOptions options = new XCUITestOptions()
                .setPlatformVersion("18.0.1")
                .setDeviceName("iPhone_13 Pro")
                .setUdid("00008110-001C45D60CC0401E")
                .setApp("com.amazon.mp3.CloudPlayer") // or the app path
                .setAutomationName("XCUITest")
                .setShowXcodeLog(true);

        driver = new IOSDriver(new URL("http://127.0.0.1:4723"), options);

        Thread.sleep(1000);
        driver.findElement(By.id("AMMyMusicNavigationTabIconAccessibilityIdentifier")).click();
        driver.findElement(By.id("2024 Delivered")).click();// Launch App

        wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        helper = new Helper(driver, wait);
        validateEachFrame = new ValidateEachFrame(driver, helper, wait);
        iossonglapseTester = new IOSSongLapseTester(driver, wait, helper, validateEachFrame);
    }

    @Test(priority = 0)
    public void test_S101_FirstPage() throws InterruptedException {
        System.out.println("\n========================================================================================");
        System.out.println("\nCase 1: S1.01 Verify the first page, 'Special Delivery'");
        Assert.assertTrue(validateEachFrame.isFirstPageDisplayed());
    }

    @Test(priority = 1)
    public void test_S106_CountOfTracksIn2024Splash() throws InterruptedException {
        System.out.println("\nCase 2: S.1.06 Verify the track count appears in 2024 splash");
        Assert.assertEquals(6, validateEachFrame.getCountOfDecadeTrack());
    }

    @Test(priority = 2)
    public void test_S201_WeSpentxxxMinutes() throws FileNotFoundException, InterruptedException {
        System.out.println("\nCase 3: S2.01 We Spent xxx minutes");

        JasonExtraction.Data data = new Gson().fromJson(new FileReader("cnf_prime.json"), JasonExtraction.Data.class);
        String totalMinutes = String.valueOf(data.getTotalStats().getTracks().getTotalMinutes());
        totalMinutes = helper.formatWithCommas(totalMinutes);

        wait = new WebDriverWait(driver, Duration.ofSeconds(15));
        wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(helper.weFound_xpath)));
        wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        Assert.assertTrue(validateEachFrame.isPartOfStringFound(totalMinutes));
        System.out.println("We spent " + totalMinutes + " minutes together this year");
    }

    @Test(priority = 3)
    public void test_S401_TopDecade() throws InterruptedException {
        wait = new WebDriverWait(driver, Duration.ofSeconds(60));
        System.out.println("\nCase 4: S4.01 Top decade");
        helper.goNextFrame(2);
        String decade = validateEachFrame.getDecade();
        String decadeFromJson = JasonExtraction.getTopDecadeFromJson();
        Assert.assertEquals(decade, decadeFromJson.substring(2));
        System.out.println("Decade is " + decade);
        wait = new WebDriverWait(driver, Duration.ofSeconds(10));
    }

//    @Test(priority = 4)
//    public void testOpenShareFromTopDecade() throws InterruptedException {
//        System.out.println("\nCase 5: Open Share from Top decade Page");
//        helper.openShareCard("testOpenShareFromTopDecade");
//    }

    @Test(priority = 5)
    public void test_S501_ValidateThereWereSomeGenre() throws InterruptedException {
        System.out.println("\nCase 6: S5.01 Verify There were some genre...");
        helper.goNextFrame(3);
        Assert.assertTrue(validateEachFrame.genreFrameValidation());
    }

    @Test(priority = 6)
    public void test_S502_ValidateTopGenres() throws InterruptedException {
        System.out.println("\nCase 7: S5.02 Verify Top 5 genres");
        helper.goNextFrame(3);
        List<String> topGenresFromJSon = JasonExtraction.getTopGenres();
        String firstGenre = topGenresFromJSon.get(0);

        List<String>  topGenres = validateEachFrame.getTopGenres(firstGenre);
        Assert.assertEquals(topGenres, topGenresFromJSon);
        System.out.println(topGenres.toString());
    }

//    @Test(priority = 7)
//    public void testOpenShareFromTopGenre() throws InterruptedException {
//        System.out.println("\nCase 8: Open Share from Top Genre Page");
//        helper.openShareCard();
//    }

    @Test(priority = 8)
    public void test_S602_FirstHalfObsessedWith() throws InterruptedException {
        System.out.println("\nCase 9: S6.02 First Obsessed With");
        helper.goNextFrame(4);
        HashMap<String, Object> firstHalfFromJSon = JasonExtraction.getFirstHalf();
        String topTrack = (String) firstHalfFromJSon.get("topTrack");
        String peakListeningMonth = "" + firstHalfFromJSon.get("peakMonth");
        peakListeningMonth = helper.getMonth(peakListeningMonth);
        Map<String, String> songs = JasonExtraction.songsMap;
        String trackNameFromJson = songs.get(topTrack);
        wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//*[@name=\"First you were obsessed with \"]")));

        //Track found
        Assert.assertTrue(validateEachFrame.isPartOfStringFound(trackNameFromJson.split(" ")[0]));
        Assert.assertTrue(validateEachFrame.isPartOfStringFound(peakListeningMonth));
        System.out.println(trackNameFromJson);
        System.out.println("Peak Listening Month : " + peakListeningMonth);
    }

    @Test(priority = 9)
    public void test_S603_SecondHalfLaterYouAre() throws InterruptedException {
        System.out.println("\nCase 10: S6.03  Later you were all about");
        helper.goNextFrame(4);
        HashMap<String, Object> secondHalfFromJSon = JasonExtraction.getSecondHalf();
        String topTrack = (String) secondHalfFromJSon.get("topTrack");
        String peakListeningMonth = "" + secondHalfFromJSon.get("peakMonth");
        peakListeningMonth = helper.getMonth(peakListeningMonth);
        String trackNameFromJson = songs.get(topTrack);
        wait = new WebDriverWait(driver, Duration.ofSeconds(20));
        wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//*[@name=\"Later, you were all about \"]")));
        wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        //Track found
        Assert.assertTrue(validateEachFrame.isPartOfStringFound(trackNameFromJson.split(" ")[0]));
        Assert.assertTrue(validateEachFrame.isPartOfStringFound(peakListeningMonth));
        System.out.println(trackNameFromJson);
        System.out.println("Peak Listening Month : " + peakListeningMonth);
    }

    @Test(priority = 10)
    public void test_S701_ButOnlyOneSongDominatedFrame() throws InterruptedException {
        System.out.println("\nCase 11: S7.01 But Only One Song Dominated");
        helper.goNextFrame(5);
        WebElement elem = wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//XCUIElementTypeStaticText[@name=\"dominated your charts\"]")));
        wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        assertNotNull(elem, "But one Song Dominated frame not found on the page");
        System.out.println("But Only One Song Dominated frame appeared");
    }

    @Test(priority = 11)
    public void test_S702_YourTopSongFrame() throws InterruptedException {
        System.out.println("\nCase 12: S7.02 Your Top Song ");
        helper.goNextFrame(5);
        wait = new WebDriverWait(driver, Duration.ofSeconds(20));
        WebElement elem = wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//XCUIElementTypeStaticText[contains(@name, \"You played it\") and contains(@name, \"times. That's commitment!\")]")));
        wait = new WebDriverWait(driver, Duration.ofSeconds(10));

        assertNotNull(elem, "'That's a commitment' not found on the page");
        System.out.println("Your Top Song Frame: 'That's a commitment'  appeared");

        System.out.println("=== Verify 'You played x times' ===");
        int count = JasonExtraction.topTracksArray.getJSONObject(0).getInt("count");
        Assert.assertTrue(validateEachFrame.isPartOfStringFound("" + count));
        System.out.println("You played " + count + " times");
    }


    @Test(priority = 12)
    public void test_S801_TopSongRareCard() throws InterruptedException {
        System.out.println("\nCase 13: S8.01 Your Top Song Rare card");
        helper.goNextFrame(6);
        //-- Extract data from json to verify --//
        String topSongID = JasonExtraction.topTracksArray.getJSONObject(0).getString("id");
        String topSongTrackTitle = JasonExtraction.songsMap.get(topSongID);
        //--------------------------------------//

        wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//*[@name=\"BIG FAN\"] ")));
        wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//*[@name=\"Top Song\"] ")));
        System.out.println("TopSong Share Card Opened");

        System.out.println("=== Verify 'You are xPercentile listener'===");
        int countPercentile = JasonExtraction.topTracksArray.getJSONObject(0).getInt("countPercentile");
        Assert.assertTrue(validateEachFrame.isPartOfStringFound("" + countPercentile));
        System.out.println("You are " + countPercentile + "% listener");

        System.out.println("=== Verify TopSong Title appears w/icon ===");
        Assert.assertTrue(validateEachFrame.isPartOfStringFound(topSongTrackTitle.split(" ")[0]));
        System.out.println(topSongTrackTitle + "appeared");
    }

    @Test(priority = 13)
    public void test_S901_AllYourFavoriteCapturedForever() throws InterruptedException {
        System.out.println("\nCase 14: S9.01  All Your Favorite Captured Forever ");
        helper.goNextFrame(7);
        wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//*[@name=\"All your favorites,\"]")));
        WebElement elem = wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//*[@name=\"captured forever\"]")));
        assertNotNull(elem, "frame not appeared");
        System.out.println("All Your Favorite Captured Forever appeared");
    }

    @Test(priority = 14)
    public void test_S903_MyTopSongs2024() throws InterruptedException {
        System.out.println("\nCase 15: S9.03 My TopSongs 2024");
        helper.goNextFrame(7);
        wait = new WebDriverWait(driver, Duration.ofSeconds(20));
        WebElement elem =  wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//*[contains(@name, \"Your playlist is waiting\")]\n")));
        wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        assertNotNull(elem, "frame not appeared");
        System.out.println("Your playlist is waiting.... appeared");
        System.out.println("My Top Songs 2024 appeared");
    }

    @Test(priority = 15)
    public void test_S11_0_1_YouListenToXXXArtists() throws FileNotFoundException, InterruptedException {
        System.out.println("\nCase 16: S11.01 You listen to xxx artists this year, but someone stole the show");
        helper.goNextFrame(9);

        //--- Extract json data to be verified ---//
        JasonExtraction.Data data = new Gson().fromJson(new FileReader("cnf_prime.json"), JasonExtraction.Data.class);
        String totalDistinctCountFromJson = String.valueOf(data.getTotalStats().getArtists().getDistinctCount());

        wait = new WebDriverWait(driver, Duration.ofSeconds(60));
        WebElement elem =  wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//*[@name=\"You listened to \"]")));
        assertNotNull(elem, "S11.01 frame not appeared");

        elem =  driver.findElement(By.xpath("//*[@name=\"emphasizedText-line1-part2\"]"));
        System.out.println("You listen to " + elem.getAttribute("label") + " artists");

        totalDistinctCountFromJson = helper.formatWithCommas(totalDistinctCountFromJson);
        Assert.assertEquals(totalDistinctCountFromJson, elem.getAttribute("label"));
        System.out.println("You listened to " + totalDistinctCountFromJson + " artists this year, but someone stole the show");
    }


    @Test(priority = 16)
    public void test_S11_02_YourTopArtist() throws InterruptedException {
        System.out.println("\nCase 17: S11.0.2 Your Top Artist");
        helper.goNextFrame(9);
        //-- extract data from json to verify --//
        int minutes = JasonExtraction.topArtistsArray.getJSONObject(0).getInt("minutes");
        String artistID = JasonExtraction.topArtistsArray.getJSONObject(0).getString("id");
        String artistName = JasonExtraction.artistMap.get(artistID);
        //--------------------------------------//
        WebElement elem = wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//*[contains(@name, 'minutes together and trust us, they are honored')]")));
        assertNotNull(elem, "Not found -> You spent xxxxx minutes together and trust us, they are honored");
        System.out.println( " Your Top Artist appeared");

        System.out.println("--- Verify top artist : " + artistName + "---");
        boolean isFound = helper.elementFound("//*[@name=\"" + artistName + "\"]", 10);
        Assert.assertTrue(isFound);
        System.out.println(artistName + " found");

        System.out.println("\n --- Verify 'You spent " + minutes + " minutes' --- ");
        String label = elem.getAttribute("label");
        Assert.assertTrue(label.contains(helper.formatWithCommas("" + minutes)));
        System.out.println(label);
    }

    @Test(priority = 17)
    public void test_S13_01_TopArtistRareCard() throws InterruptedException {
        System.out.println("\nCase 18: S13.0.1 Top Artist Rare Card");
        helper.goNextFrame(10);
        //-- extract data from json to verify --//
        int minutesPercentileFromJson = JasonExtraction.topArtistsArray.getJSONObject(0).getInt("minutesPercentile");
        String artistID = JasonExtraction.topArtistsArray.getJSONObject(0).getString("id");
        String artistName = JasonExtraction.artistMap.get(artistID);
        //--------------------------------------//
        WebElement elem = wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//XCUIElementTypeStaticText[@name=\"Songs they've been loving, picked just for you.\"]")));
        System.out.println( "Top Artist Rare Card appeared");

        System.out.println("--- Verify top artist : " + artistName + "---");
        boolean isFound = helper.elementFound("//*[@name=\"" + artistName + "\"]", 10);
        Assert.assertTrue(isFound, artistName + " not found");
        System.out.println(artistName + " found");

        System.out.println("--- Verify the String :  Songs they've been loving, picked just for you.");
        String label = elem.getAttribute("label");
        Assert.assertTrue(label.contains("Songs they've been loving, picked just for you."));
        System.out.println("Found: " + label);
    }

    @Test(priority = 18)
    public void test_S14_01_TopArtists() throws InterruptedException {
        System.out.println("\nCase 19: S14.0.1 Top Artists");
        helper.goNextFrame(11);
        List<String> topArtistsNamesListFromJson = new ArrayList<>();
        // Loop through each artist JSON object in the JSONArray and extract the ID
        for (int i = 0; i < JasonExtraction.topArtistsArray.length(); i++) {
            JSONObject artist = JasonExtraction.topArtistsArray.getJSONObject(i);
            String artistId = artist.getString("id");
            topArtistsNamesListFromJson.add(JasonExtraction.artistMap.get(artistId));
        }
        // Output the list of artist IDs
        System.out.println("Top Artists IDs: " + topArtistsNamesListFromJson);
        wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//XCUIElementTypeStaticText[contains(@name,   '" + topArtistsNamesListFromJson.get(1) + "')]")));

        // Extract the artists names from the current frame and put them in array
        List<String> topArtistsArray = validateEachFrame.getTopArtists();

        Assert.assertEquals(topArtistsArray, topArtistsNamesListFromJson);
        System.out.println("Top Artists correctly displayed : " + topArtistsArray);
    }


    @AfterMethod
    public void tearDown() {
        if (driver != null) {
            driver.quit(); // Close the browser
        }
    }
}

package org.example;

import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.android.options.UiAutomator2Options;
import org.openqa.selenium.remote.DesiredCapabilities;

import java.net.MalformedURLException;
import java.net.URL;

public class BasicTest {
    public static void main(String[] args) {
        AndroidDriver driver = null;

        try {
            // Set up desired capabilities
            UiAutomator2Options options = new UiAutomator2Options();
            options.setPlatformName("Android");
            options.setDeviceName("OnePlus Nord N200 5G");  // Replace with your Android device name
            options.setUdid("b516d4ae"); // Replace with your Android device UDID
            options.setApp("com.amazon.mp3.CloudPlayer"); // Android package for the app
            options.setAutomationName("UiAutomator2");
            options.setCapability("disableHiddenApiPolicyCheck", true);

            // Initialize the driver
            driver = new AndroidDriver(new URL("http://127.0.0.1:4723"), options);

            // Your test code here

        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (driver != null) {
                driver.quit();
            }
        }
    }
}

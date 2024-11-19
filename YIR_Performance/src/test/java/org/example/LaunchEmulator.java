package org.example;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.IOException;

public class LaunchEmulator {

    public static void main(String[] args) {
        // Name of the emulator as seen in Android Studio AVD Manager
        String emulatorName = "Medium_Phone_API_35"; // Replace with your emulator name

        try {
            // Command to launch the emulator
            Process process = new ProcessBuilder("/Users/marxyang/Library/Android/sdk/emulator/emulator", "-avd", emulatorName).start();

            // Read and print emulator output
            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
            String line;
            while ((line = reader.readLine()) != null) {
                System.out.println(line);
            }

            process.waitFor();
            System.out.println("Emulator launched successfully.");

        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
            System.out.println("Failed to launch emulator.");
        }
    }
}

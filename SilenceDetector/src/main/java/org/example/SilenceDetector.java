package org.example;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.InterruptedIOException;
import java.math.BigDecimal;
import java.math.RoundingMode;

//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
public class SilenceDetector {
    public static void detectSilenceInMp4(String filePath) throws IOException, InterruptedIOException {
        String command = "ffmpeg -i " + filePath + " -af silencedetect=noise=-40dB:d=0.31 -f null -";
        System.out.println(command);
        Process process = Runtime.getRuntime().exec(command);

        BufferedReader reader = new BufferedReader(new InputStreamReader(process.getErrorStream()));
        String line;
        System.out.println("Detecting silence interval");

        int counter = 0;
        while ((line = reader.readLine()) != null) {
            //  System.out.println(line);

            if (line.contains("silence_start")) {
                counter++;
                System.out.println("\n");
                System.out.println("Gap "+ counter);

                System.out.println("Silence starts at: " + roundDownToTwoDecimalPlaces(Double.parseDouble(line.split("silence_start: ")[1] )) + " seconds");
            } else if (line.contains("silence_end")) {

                String[] parts = line.split("silence_end: ")[1].split(" | ");
                //  System.out.println("parts[0] " + parts[0] + "   parts[1] " + parts[1]  + "   parts[2] " + parts[2] + "   parts[3] " + parts[3]);
                System.out.println("Silence ends at: " +  roundDownToTwoDecimalPlaces(Double.parseDouble(parts[0])) + " seconds, Duration: " + roundDownToTwoDecimalPlaces(Double.parseDouble(parts[3])) + " seconds");
            }
        }
    }

    public static double roundDownToTwoDecimalPlaces(double value) {
        BigDecimal bd = new BigDecimal(value);
        bd = bd.setScale(2, RoundingMode.FLOOR); // Round down to 2 decimal places
        return bd.doubleValue();
    }

    public static void main(String[] args) throws IOException {
        String filePath = "/Users/marxyang/Desktop/PERFORMANCE_TEST/C_Android/Wifi_C.mp4";
        detectSilenceInMp4(filePath);
    }

}
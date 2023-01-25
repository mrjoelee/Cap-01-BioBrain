package com.biobrain.app;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class BioBrainApp {

    public void execute() {
        welcome();
    }

    private void welcome() {
        printFile("src/main/images/welcomeRobot.txt");
    }

    private void printFile(String fileName) {
        try{
            System.out.println(Files.readString(Path.of(fileName)));
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }
}

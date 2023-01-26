package com.biobrain.app;

import com.apps.util.Console;
import com.apps.util.Prompter;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Scanner;

public class BioBrainApp {

    private final Prompter prompter = new Prompter(new Scanner(System.in));

    public void execute() {

        welcome();
        Console.pause(1500);
        askIfUserWantToPlay();
    }

    private void welcome() {
        String splashScreen = "src/main/images/welcomeRobot.txt";
        printFile(splashScreen);
    }

    private void askIfUserWantToPlay() {

        String dontWantToPlayBanner = "src/main/images/dontWantToPlayBanner.txt";
        System.out.println("\nWould you like to play Bio Brain? [Y]es or [N]o ");
        String input = prompter.prompt("Enter response: ", "[YyNn]", "\nInvalid input... Please enter [Y]es or [N]o \n");

        if (input.equalsIgnoreCase("y")) {
            // this is where we start the game
            System.out.println("Let's play!");
        } else {
//            System.out.println("Don't be scared! Next time, say yes!");
            printFile(dontWantToPlayBanner);
//            System.exit(0);
        }
    }

    private void printFile(String fileName) {
        try {
            System.out.println(Files.readString(Path.of(fileName)));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

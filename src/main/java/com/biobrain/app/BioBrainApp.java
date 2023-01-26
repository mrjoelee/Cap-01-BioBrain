package com.biobrain.app;

import com.apps.util.Console;
import com.apps.util.Prompter;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Scanner;

public class BioBrainApp {

    private final Prompter prompter = new Prompter(new Scanner(System.in));
    private boolean gameOver = false;

    public void execute() {
        intro();
        Console.pause(1500);
        welcome();
        Console.pause(1500);
        askIfUserWantToPlay();
    }

    public void intro() {
        printFile("src/main/intro/intro.txt");

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

            System.out.println("Let's play!");
            // this is where we start the game
            game();
        } else {
            printFile(dontWantToPlayBanner);
        }
    }

    private void game() {

        Thread inputThread = new Thread(() -> {
            Scanner scanner = new Scanner(System.in);
            while (!gameOver) {
                String quitInput = scanner.nextLine();
                if (quitInput.equalsIgnoreCase("quit")) {
                    gameOver = true;
                    break;
                }
            }
            scanner.close();
        });
        inputThread.start();

        if (!gameOver) {
            printFile("src/main/images/mapBioBrain.txt");
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

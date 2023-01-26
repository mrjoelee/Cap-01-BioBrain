package com.biobrain.app;

import com.apps.util.Prompter;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Scanner;

public class BioBrainApp {

    private final Prompter prompter = new Prompter(new Scanner(System.in));

    public void execute() {

        welcome();
        askIfUserWantToPlay();
    }

    private void welcome() {
        printFile("src/main/images/welcomeRobot.txt");
    }

    private void askIfUserWantToPlay() {

        String input = prompter.prompt("\nWould you like to play Bio Brain? [Y]es or [N]o", "[YyNn]", "\nInvalid input... Please enter [Y]es or [N]o \n");

        if (input.equalsIgnoreCase("y")) {
            // this is where we start the game
            System.out.println("Let's play!");
        } else {
            System.out.println("Don't be a wimp! Next time, say yes!");
            System.exit(0);
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

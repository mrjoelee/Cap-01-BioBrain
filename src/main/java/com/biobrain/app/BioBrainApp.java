package com.biobrain.app;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Scanner;

public class BioBrainApp {

    Scanner input = new Scanner(System.in);
    public void execute() {

        welcome();
        askIfUserWantToPlay();
    }

    private void welcome() {
        printFile("src/main/images/welcomeRobot.txt");
    }

    private void askIfUserWantToPlay() {

        String answer;

        while(true) {
            System.out.println("Do you want to play Bio Brain? (y/n)");
            answer = input.nextLine();

            if(answer.equalsIgnoreCase("y")) {
                System.out.println("Let's play!");
                // game would start here
                break;
            } else if(answer.equalsIgnoreCase("n")) {
                System.out.println("You are missing out!! Bye!!");
                System.exit(0);
            } else {
                System.out.println("Invalid answer. Please type 'y' or 'n'.");
            }
        }
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

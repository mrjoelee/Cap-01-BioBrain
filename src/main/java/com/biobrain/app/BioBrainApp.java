package com.biobrain.app;

import com.apps.util.Console;
import com.apps.util.Prompter;
import com.biobrain.Location;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;


import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.lang.reflect.Type;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Scanner;

public class BioBrainApp {

    private final Prompter prompter = new Prompter(new Scanner(System.in));
    private Location currentLocation;
    private List<Location> locations;
    private boolean gameOver = false;

    public void execute() {
        intro();
        Console.pause(1500);
        welcome();
        Console.pause(1500);
        askIfUserWantToPlay();

//        Gson gson = new Gson();
//        Type locationList = new TypeToken<List<Location>>() {
//        }.getType();
//        try (BufferedReader reader = new BufferedReader(new FileReader("jsonFiles/locations.json"))) {
//            List<Location> locations = gson.fromJson(reader, locationList);
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
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

        Gson gson = new Gson();
        Type locationList = new TypeToken<List<Location>>() {
        }.getType();
        try (BufferedReader reader = new BufferedReader(new FileReader("jsonFiles/locations.json"))) {

            locations = gson.fromJson(reader, locationList);

            if(locations != null && !locations.isEmpty()){

                currentLocation = locations.get(0);

                System.out.printf("You are currently in " + currentLocation.getName());
            } else {
                System.out.println("Error in getting the location");
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

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

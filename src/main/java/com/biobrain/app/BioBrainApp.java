package com.biobrain.app;

import com.apps.util.Console;
import com.apps.util.Prompter;
import com.biobrain.Item;
import com.biobrain.Location;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.util.List;
import java.util.Objects;
import java.util.Scanner;
import java.util.stream.Stream;

public class BioBrainApp {

    private final Prompter prompter = new Prompter(new Scanner(System.in));
    private Location currentLocation;
    private List<Location> locations;
    private boolean gameOver = false;


    public void execute() {
        intro();
        welcome();
        Console.pause(1500);
        askIfUserWantToPlay();
    }

    public void intro() {
        printFile("intro/intro.txt");
        Console.pause(5000);
        Console.clear();
    }

    private void welcome() {
        String splashScreen = "images/welcomeRobot.txt";
        printFile(splashScreen);
    }

    private void askIfUserWantToPlay() {

        String dontWantToPlayBanner = "images/dontWantToPlayBanner.txt";
        System.out.println("\nWould you like to play Bio Brain? [Y]es or [N]o ");
        String input = prompter.prompt("Enter response: ", "[YyNn]", "\nInvalid input... Please enter [Y]es or [N]o \n");

        if (input.equalsIgnoreCase("y")) {

            System.out.println("Let's play!");
            // this is where we start the game
            Console.clear();
            game();
        } else {
            printFile(dontWantToPlayBanner);
        }
    }

    private void game() {
        locationsJsonParsed();

        if (!gameOver) {
//            printFile("images/mapBioBrain.txt");
            askPlayerAction();
        }
    }

    private void locationsJsonParsed() {
        Gson gson = new Gson();
        Type locationList = new TypeToken<List<Location>>() {
        }.getType();
        //noinspection ConstantConditions
        try (InputStream input = getClass().getClassLoader().getResourceAsStream("jsonFiles/locations.json");
             BufferedReader reader = new BufferedReader(new InputStreamReader(input))) {

            locations = gson.fromJson(reader, locationList);

            if (locations != null && !locations.isEmpty()) {

                currentLocation = locations.get(0);

                List<String> itemsInRoom = currentLocation.getItems();

                System.out.printf("\nYou are currently in %s \n", currentLocation.getName());
                System.out.println("\nYou see the following items: ");
                for (String item : itemsInRoom) {
                    System.out.print("\n " + item);
                }
                System.out.printf("\n\nYou can choose to go East to %s ", currentLocation.getDirections().get("east"));
                System.out.printf("\nOr you can go South to %s", currentLocation.getDirections().get("south"));

            } else {
                System.out.println("Error in getting the location");
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void askPlayerAction() {
        System.out.println("\nWhat would you like to do? Look at items or Move to a different location");
        System.out.println("\nType Look to check item or Type Go to the direction you want to move to.");
        String input = prompter.prompt("\nEnter response: ", "[LlMm]", "\nInvalid input... Please enter [L]ook or [M]ove \n");
        if(input.equalsIgnoreCase("l")) {
            System.out.println("\nWhich item would you like to look at?");
            String itemToLookAt = prompter.prompt("Enter item name: ");
            if(currentLocation.getItems().contains(itemToLookAt)) {
                System.out.printf("\nYou are looking at %s", itemToLookAt);
//                String itemDescription = Item.getDescriptions(itemToLookAt);
//                System.out.println("\nItem description: " + itemDescription);

            } else {
                System.out.println("\nItem not found");
            }
        } else {
            System.out.println("You are moving to a different location");
        }
    }

    private void printFile(String fileName) {
        //noinspection ConstantConditions
        try (BufferedReader buffer = new BufferedReader(new InputStreamReader(getClass().getClassLoader().getResourceAsStream(fileName)))) {
            Stream.generate(() -> {
                        try {
                            return buffer.readLine();
                        } catch (IOException e) {
                            throw new RuntimeException(e);
                        }
                    })
                    .takeWhile(Objects::nonNull)
                    .forEach(System.out::println);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

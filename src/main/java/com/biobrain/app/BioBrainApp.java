package com.biobrain.app;

import com.apps.util.Console;
import com.apps.util.Prompter;
import com.biobrain.Item;
import com.biobrain.Location;
import com.biobrain.Player;
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
    private Player player = null;
    private Location currentLocation;
    private List<Location> locations;
    private List<String> itemsInRoom;
    private boolean gameOver = false;

    public void execute() {
        intro();
        welcome();
        Console.pause(1500);
        askIfUserWantToPlay();
        player = Player.create();
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
        System.out.println("\nWould you like to play Bio Brain? Yes or No ");
        String input = prompter.prompt("Enter response: ", "(?i)(Yes|No)", "\nInvalid input... Please type Yes or No \n");

        if (input.equalsIgnoreCase("yes")) {

            System.out.println("Let's play!");
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
            while (!gameOver) {
                askPlayerAction();
            }
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

                itemsInRoom = currentLocation.getItems();

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
        System.out.println("\nWhat would you like to do? Look at items or Move to a different location or quit?");
        System.out.println("\nType Look to check item, Move to a different location, or Quit to exit the game");
        String input = prompter.prompt("\nEnter response: ", "(?i)(Look|Move|Quit)", "\nInvalid input... Please type Look, Move, or Quit \n");
        if (input.equalsIgnoreCase("look")) {
            System.out.println("\nWhich item would you like to look at?");
            String itemToLookAt = prompter.prompt("Enter item name: ");
            if (currentLocation.getItems().contains(itemToLookAt)) {
                String itemDescription = Item.getDescriptions(itemToLookAt);
                int damageValue = Item.getDamageValue(itemToLookAt);
                System.out.printf("\nItem description:  %s it has a damage value of %s", itemDescription, damageValue);

            } else if (itemToLookAt.equalsIgnoreCase("quit")) {
                System.out.println("\nThanks for playing!");
                gameOver = true;
            } else {
                System.out.println("\nItem not found");
            }
        } else if (input.equalsIgnoreCase("move")) {
            System.out.println("\nWhich direction would you like to move to?");
            String direction = prompter.prompt("Enter direction: ");
            if (direction.equalsIgnoreCase("q") || direction.equalsIgnoreCase("quit")) {
                System.out.println("\nThanks for playing!");
                gameOver = true;
            } else {
                movePlayer(direction);
            }

        } else if (input.equalsIgnoreCase("quit")) {
            System.out.println("\nThanks for playing!");
            gameOver = true;
        }
    }

    private void movePlayer(String direction) {
        String nextLocation = currentLocation.getDirections().get(direction.toLowerCase());
        if (nextLocation != null) {
            for (Location location : locations) {
                if (location.getName().equalsIgnoreCase(nextLocation)) {
                    currentLocation = location;
                    break;
                }
            }
            itemsInRoom = currentLocation.getItems();
            System.out.printf("\nYou are currently in %s \n", currentLocation.getName());
            System.out.println("\nYou see the following items: ");
            for (String item : itemsInRoom) {
                System.out.print("\n " + item);
            }
            System.out.printf("\n\nYou can choose to go East to %s ", currentLocation.getDirections().get("east"));
            System.out.printf("\nOr you can go South to %s", currentLocation.getDirections().get("south"));
        } else {
            System.out.println("\nYou can't go that way");
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

package com.biobrain.app;

import com.apps.util.Console;
import com.apps.util.Prompter;
import com.biobrain.Item;
import com.biobrain.Location;
import com.biobrain.Player;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.List;
import java.util.Objects;
import java.util.Scanner;
import java.util.stream.Stream;

public class BioBrainApp {

    private static final String GAME_INTRO = "intro/intro.txt";
    private static final String SPLASH_SCREEN = "images/welcomeRobot.txt";
    private static final String NO_BANNER = "images/dontWantToPlayBanner.txt";
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
        printFile(GAME_INTRO);
        Console.pause(5000);
        Console.clear();
    }

    private void welcome() {
        printFile(SPLASH_SCREEN);
    }

    private void askIfUserWantToPlay() {
        System.out.println("\nWould you like to play Bio Brain? Yes or No ");
        String input = prompter.prompt("Enter response: ", "(?i)(Yes|No)", "\nInvalid input... Please type Yes or No \n");

        if (input.equalsIgnoreCase("yes")) {

            System.out.println("Let's play!");
            Console.clear();
            game();
        } else {
            printFile(NO_BANNER);
        }
    }

    private void game() {
        sector1();

        if (!gameOver) {
//            printFile("images/mapBioBrain.txt");

            while (!gameOver) {
                askPlayerAction();
            }
        }
    }

    private void sector1() {

        locations = Location.parsedLocationsFromJson();

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

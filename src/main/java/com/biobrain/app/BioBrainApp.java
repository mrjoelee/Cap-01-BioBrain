package com.biobrain.app;

import com.apps.util.Console;
import com.apps.util.Prompter;
import com.biobrain.Item;
import com.biobrain.Location;
import com.biobrain.Npc;
import com.biobrain.Player;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Scanner;
import java.util.stream.Stream;

public class BioBrainApp {
    private static final String GAME_INTRO = "intro/intro.txt";
    private static final String SPLASH_SCREEN = "images/welcomeRobot.txt";
    private static final String NO_BANNER = "images/dontWantToPlayBanner.txt";
    private final Prompter prompter = new Prompter(new Scanner(System.in));
    private Player player;
    private Npc npc;
    private Location currentLocation;
    private List<Location> locations;
    private Map<String, String> directions;
    private List<String> itemsInRoom;
    private boolean gameOver = false;

    public void execute() {
        player = Player.create();
        intro();
        welcome();
        Console.pause(1500);
        askIfUserWantToPlay();
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
            System.out.println("\nLet's play!");
            Console.clear();
            game();
        } else {
            printFile(NO_BANNER);
        }
    }

    private void game() {
        System.out.println("\n\nYour plan worked! You reprogrammed the BioBrain and now they know everything!\n" +
                "It's the last day before the director shuts down the Jung_E project. If you're going to escape with\n" +
                "the BioBrain it's now or never. Hurry! Try to make it to the Train Dock! There isn't much time!\n\n");
        Console.pause(8000);
        System.out.println("BEEP BEEP BEEP!\n");
        System.out.println("\nThat's the alarm! Someone must have detected that the BioBrain is missing from the Production Room!\n" +
                "You've got to get moving!");
        Console.clear();
        currentPlayerLocation();
        if (!gameOver) {
//            printFile("images/mapBioBrain.txt");
            while (!gameOver) {
                askPlayerAction();
            }
        }
    }

    private void currentPlayerLocation() {
        System.out.println(player.displayPlayerInfo());
        Console.pause(1000);
        locations = Location.parsedLocationsFromJson();
        if (locations != null && !locations.isEmpty()) {
            currentLocation = locations.get(2);
            itemsInRoom = currentLocation.getItems();
            System.out.println("\n=====================================================");
            System.out.printf("\nYou are currently in %s \n", currentLocation.getName());
            System.out.println("\nYou see the following items: ");
            for (String item : itemsInRoom) {
                System.out.print("\n " + item);
            }
            System.out.println("\n\nYou can choose to go: ");
            directions = currentLocation.getDirections();
            for (Map.Entry<String, String> direction : directions.entrySet()) {
                System.out.printf("\n%s to %s", direction.getKey(), direction.getValue());
            }
            System.out.println("\n===================================================");
        } else {
            System.out.println("Error in getting the location");
        }
    }

    private void askPlayerAction() {
        System.out.println("\nWhat would you like to do? Look at items, Get item, Move to a different location or quit?");
        System.out.println("\nType Look to check item, Get to pick up item, Move to a different location, Show to see inventory or Quit to exit the game");
        String input = prompter.prompt("\nEnter response: ", "(?i)(Look|Get|Show|Drop|Move|Quit)", "\nInvalid input... Please type Go Look, Get Item, Move, or Quit \n").trim();
        switch (input.toLowerCase()) {
            case "look":
                lookAtItem();
                break;
            case "move":
                System.out.println("\nWhich direction would you like to move to?");
                String direction = prompter.prompt("Enter direction: ");
                if (direction.equalsIgnoreCase("quit")) {
                    System.out.println("\nThanks for playing!");
                    gameOver = true;
                } else {
                    Console.clear();
                    movePlayer(direction);
                }
                break;
            case "get":
                getItem();
                break;
            case "show":
                showInventory();
                break;
            case "drop":
                dropItem();
                break;
            case "quit":
                System.out.println("\nThanks for playing!");
                gameOver = true;
                break;
            default:
                System.out.println("\nInvalid input... Please type Go Look, Get Item,  Move, or Quit \n");
                break;
        }
    }

    private void lookAtItem() {
        System.out.println("\nWhich item would you like to look at?");
        String itemToLookAt = prompter.prompt("\nEnter item name: ");
        if (currentLocation.getItems().contains(itemToLookAt)) {
            String itemDescription = Item.getDescriptions(itemToLookAt);
            int damageValue = Item.getDamageValue(itemToLookAt);
            System.out.printf("\nItem description:  %s it has a damage value of %s \n", itemDescription, damageValue);
        } else if (itemToLookAt.equalsIgnoreCase("quit")) {
            System.out.println("\nThanks for playing!");
            gameOver = true;
        } else {
            System.out.println("\nItem not found");
        }
    }

    private void getItem() {
        String itemToPickUp = prompter.prompt("\nEnter which item you want to pick up: ");
        if (currentLocation.getItems().contains(itemToPickUp)) {
            player.addItem(itemToPickUp);
            itemsInRoom.remove(itemToPickUp);
            System.out.printf("\n You picked up %s \n", itemToPickUp);
            System.out.println(player.displayPlayerInfo());
            Console.pause(1000);
        } else if (itemToPickUp.equalsIgnoreCase("quit")) {
            System.out.println("\nThanks for playing!");
            gameOver = true;
        } else {
            System.out.println("\nItem not found");
        }
    }

    private void showInventory() {
        Console.pause(1500);
        if (player.getInventory().isEmpty()) {
            System.out.println("\nYou have no items in your inventory");
        } else {
            Console.clear();
            System.out.println("\n ======================================================================");
            System.out.println("\nYou have the following items in your inventory: " + player.getInventory());
            System.out.println("\n ======================================================================");
            Console.pause(1500);
            currentPlayerLocation();
        }
    }

    private void dropItem() {
        String itemToDrop = prompter.prompt("\nEnter which item you want to drop: ");
        if (player.getInventory().contains(itemToDrop)) {
            player.removeItem(itemToDrop);
            System.out.printf("\n You dropped %s ", itemToDrop);
            System.out.println(player.displayPlayerInfo());
        } else if (itemToDrop.equalsIgnoreCase("quit")) {
            System.out.println("\nThanks for playing!");
            gameOver = true;
        } else {
            System.out.println("\nItem not found");
        }
    }

    private void movePlayer(String direction) {
        String nextLocation = currentLocation.getDirections().get(direction.toLowerCase());
        if (nextLocation != null) {
            for (Location location : locations) {
                if (location.getName().equalsIgnoreCase(nextLocation)) {
                    currentLocation = location;
//                    break;
                }
            }
            itemsInRoom = currentLocation.getItems();
            System.out.println("\n=======================================================");
            System.out.printf("\nYou are currently in %s \n", currentLocation.getName());
            System.out.println("\nYou see the following items: ");
            for (String item : itemsInRoom) {
                System.out.print("\n " + item);
            }
            System.out.println("\n=============================================================");
            System.out.printf("\n\nYou can choose to go East to %s \n", currentLocation.getDirections().get("east"));
            System.out.printf("\nOr you can go South to %s \n", currentLocation.getDirections().get("south"));
        } else {
            System.out.println("\nYou can't go that way");
        }
    }

    private void printFile(String fileName) {
        //noinspection ConstantConditions        t
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

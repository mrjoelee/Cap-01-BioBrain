package com.biobrain.app;

import com.apps.util.Console;
import com.apps.util.Prompter;
import com.biobrain.*;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.*;
import java.util.stream.Stream;

import static com.biobrain.UserInput.WordCommands;

public class BioBrainApp {
    private static final String GAME_INTRO = "intro/intro.txt";
    private static final String SPLASH_SCREEN = "images/welcomeRobot.txt";
    private static final String NO_BANNER = "images/dontWantToPlayBanner.txt";
    private final Prompter prompter = new Prompter(new Scanner(System.in));
    private Player player;
    private String npc;
    private Location currentLocation;
    private List<Location> locations;
    private Map<String, String> directions;
    private List<String> itemsInRoom;
    private boolean gameOver = false;


    public void execute() throws IOException {
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
            printFile("images/mapBioBrain.txt");
            while (!gameOver) {
                askPlayerAction();
            }
        }
    }

    private void currentPlayerLocation() {
        System.out.println(player.displayPlayerInfo());
        Console.pause(1000);
        locations = Location.parsedLocationsFromJson();

        if (locations == null || locations.isEmpty()) {
            System.out.println("Error in getting the location");
            return;
        }

        currentLocation = locations.get(1);
        itemsInRoom = currentLocation.getItems();
        System.out.println("\n=====================================================\n");
        System.out.printf("\nYou are currently in %s \n", currentLocation.getName());
        Console.pause(1000);

        System.out.println("\nYou see the following items: ");
        for (String item : itemsInRoom) {
            System.out.println("\n " + item);
        }

        System.out.println("\n\nYou can choose to go: ");
        directions = currentLocation.getDirections();
        for (Map.Entry<String, String> direction : directions.entrySet()) {
            System.out.printf("\n%s to %s", direction.getKey(), direction.getValue());
        }
        System.out.println("\n===================================================");
    }


    private void askPlayerAction() {
        System.out.println("\nType Look to check item, Get to pick up item, Go to move to a different location, Show Inventory to see inventory or Quit to exit the game");
        UserInput.WordCommands(new ArrayList<>());
        String verb = UserInput.verb.toLowerCase();
        String noun = UserInput.noun;

        switch (verb) {
            case "look":
                lookAtItem(noun);
                break;
            case "go":
                Console.clear();
                movePlayer(noun);
                break;
            case "get":
                getItem(noun);
                break;
            case "show":
                if (noun.equalsIgnoreCase("inventory")) {
                    showInventory();
                }
                break;
            case "drop":
                dropItem(noun);
                break;
        }
    }

    private void lookAtItem(String item) {
        if (!currentLocation.getItems().contains(item)) {
            System.out.println("\nItem not found");
            return;
        }

        String itemDescription = Item.getDescriptions(item);
        int damageValue = Item.getDamageValue(item);
        System.out.println("\n===================================================");
        System.out.printf("\nItem description:  %s it has a damage value of %s \n", itemDescription, damageValue);
        System.out.println("\n===================================================");
        Console.pause(1000);
    }


    private void getItem(String itemToPickup) {
        if (!currentLocation.getItems().contains(itemToPickup)) {
            System.out.println("\nItem not found");
            return;
        }

        player.addItem(itemToPickup);
        itemsInRoom.remove(itemToPickup);
        System.out.printf("\nYou picked up %s \n", itemToPickup);
        System.out.println(player.displayPlayerInfo());
        Console.pause(1000);
    }


    private void showInventory() {
        Console.pause(1500);
        List<String> inventory = player.getInventory();
        if (inventory.isEmpty()) {
            System.out.println("\nYou have no items in your inventory");
            return;
        }

        Console.clear();
        System.out.println("\n ======================================================================");
        System.out.println("\nYou have the following items in your inventory: " + inventory);
        System.out.println("\n ======================================================================");
        Console.pause(1500);
    }


    private void dropItem(String itemToDrop) {
        if (!isItemInInventory(itemToDrop)) {
            System.out.println("\nItem not found");
            return;
        }
        player.removeItem(itemToDrop);
        itemsInRoom.add(itemToDrop);
        System.out.printf("\n You dropped %s ", itemToDrop);
        System.out.println(player.displayPlayerInfo());
    }

    private boolean isItemInInventory(String item) {
        return player.getInventory().contains(item);
    }


    private void movePlayer(String direction) {
        String nextLocation = currentLocation.getDirections().get(direction.toLowerCase());
        if (nextLocation == null) {
            System.out.println("\nYou can't go that way");
            return;
        }

        currentLocation = getLocation(nextLocation);
        itemsInRoom = currentLocation.getItems();
        System.out.println("\n=======================================================");
        System.out.printf("\nYou are currently in %s \n", currentLocation.getName());
        System.out.println("\nYou see the following items: ");
        itemsInRoom.forEach(item -> System.out.println("\n " + item));
        System.out.println("\n=============================================================");
        System.out.println("\nYou can go to the following directions: ");
        currentLocation.getDirections().forEach((key, value) -> System.out.printf("\n %s to %s", key, value));
        System.out.println("\n===================================================");
    }

    private Location getLocation(String locationName) {
        for (Location location : locations) {
            if (location.getName().equalsIgnoreCase(locationName)) {
                return location;
            }
        }
        return null;
    }

}

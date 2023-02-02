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
        if (locations != null && !locations.isEmpty()) {
            currentLocation = locations.get(1);
            itemsInRoom = currentLocation.getItems();
            System.out.println("\n=====================================================\n");
            System.out.printf("\nYou are currently in %s \n", currentLocation.getName());
            Console.pause(1000);

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
        System.out.println("\nType Look to check item, Get to pick up item, Go to move to a different location, Show Inventory to see inventory or Quit to exit the game");
        UserInput.WordCommands(new ArrayList<>());
        String verb = UserInput.verb;
        String noun = UserInput.noun;

        switch (verb.toLowerCase()) {
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
                if(noun.equalsIgnoreCase("inventory")) {
                    showInventory();
                }
                break;
            case "drop":
                dropItem(noun);
                break;
        }
    }
    private void lookAtItem(String item) {
        if (currentLocation.getItems().contains(item)) {
            String itemDescription = Item.getDescriptions(item);
            int damageValue = Item.getDamageValue(item);
            System.out.println("\n===================================================");
            System.out.printf("\nItem description:  %s it has a damage value of %s \n", itemDescription, damageValue);
            System.out.println("\n===================================================");
            Console.pause(1000);
        } else {
            System.out.println("\nItem not found");
        }
    }

    private void getItem(String itemToPickup) {
        if (currentLocation.getItems().contains(itemToPickup)) {
            player.addItem(itemToPickup);
            itemsInRoom.remove(itemToPickup);
            System.out.printf("\n You picked up %s \n", itemToPickup);
            System.out.println(player.displayPlayerInfo());
            Console.pause(1000);
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
        }
    }

    private void dropItem(String itemToDrop) {
        if (player.getInventory().contains(itemToDrop)) {
            player.removeItem(itemToDrop);
            itemsInRoom.add(itemToDrop);
            System.out.printf("\n You dropped %s ", itemToDrop);
            System.out.println(player.displayPlayerInfo());
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
            for (String key : currentLocation.getDirections().keySet()) {
                System.out.printf("\nYou can choose to go %s to %s \n", key, currentLocation.getDirections().get(key));
            }
            System.out.println("\n===================================================");

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

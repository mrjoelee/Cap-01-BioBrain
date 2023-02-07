package com.biobrain.app;

import com.apps.util.Console;
import com.apps.util.Prompter;
import com.biobrain.*;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.*;


public class BioBrainApp {
    private static final String GAME_INTRO = "intro/intro.txt";
    private static final String START_GAME = "intro/startGame.txt";
    private static final String SPLASH_SCREEN = "images/welcomeRobot.txt";
    private static final String NO_BANNER = "images/dontWantToPlayBanner.txt";
    private static final String MAIN_MAP = "images/mapMain.txt";
    private final Prompter prompter = new Prompter(new Scanner(System.in));
    private Player player;
    private String npc;
    private Location currentLocation;
    private List<Location> locations;
    private Map<String, String> directions;
    private List<String> itemsInRoom;
    private boolean gameOver = false;
    private String randomDialogue = Npc.getRandomDialogue();

    public void execute() {
        player = Player.create();
        intro();
        Console.pause(5000L); ///25_000L
        welcome();
        Console.pause(1000);  // 5000
        askIfUserWantToPlay();
    }

    public void intro() {
        printFile(GAME_INTRO);
        // 5000
    }

    private void welcome() {
        printFile(SPLASH_SCREEN);
    }

    private void askIfUserWantToPlay() {
        System.out.println("\n                                                Would you like to play Bio Brain?");
        String input = prompter.prompt("                                                         Enter yes/no: ", "(?i)(Yes|No)", "\nInvalid input... Please type Yes or No \n");
        if (input.equalsIgnoreCase("yes")) {
            System.out.println("\n!");
            Console.clear();
            game();
        } else {
            printFile(NO_BANNER);
        }
    }

    private void game() {
        printFile(START_GAME);
        Console.pause(1000); //8000
        System.out.println("                                            *** BEEP *** BEEP *** BEEP! ***\n");
        System.out.println("\n              That's the alarm! Someone must have detected that the BioBrain is missing from the Production Room!\n" +
                "                                                    You've got to get moving!");
        Console.clear();
        printFile(MAIN_MAP);
        Console.pause(4000);
        currentPlayerLocation();
        if (!gameOver) {
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
        String locationName = currentLocation.getName();
        String mapToPrint = currentLocation.getMap();
        itemsInRoom = currentLocation.getItems();

        System.out.println("\n=====================================================\n");
        System.out.printf("\nYOU ARE CURRENTLY IN -> %s \n", locationName);
        Console.pause(1000);

        System.out.println("\nLooking around you see the following items: ");
        for (String item : itemsInRoom) {
            System.out.println("\n- " + item);
        }

        System.out.println("\n\nTHERE ARE ALSO DOORS THAT LEAD TO");
        directions = currentLocation.getDirections();
        for (Map.Entry<String, String> direction : directions.entrySet()) {
            System.out.printf("\n-> %s to %s", direction.getKey(), direction.getValue());
        }
        System.out.println("\n===================================================");
        printLocationMap(mapToPrint);
    }


    private void askPlayerAction() {
        System.out.println("\nWhat would you like to do?:\n\n- (Look) to check item\n- (Get) to pick up item\n- (Go + direction) to move to a different location\n- (Show Inventory) to see inventory\n- (Show Directions)\n- (Quit) to exit the game.");
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
                } else if (noun.equalsIgnoreCase("directions")) {
                    viewDirections();
                }
                break;
            case "drop":
                dropItem(noun);
                break;
        }
    }

    private void viewDirections() {
        System.out.println("\nYOU CAN CHOOSE FROM THE FOLLOWING DIRECTIONS:");
        currentLocation.getDirections().forEach((key, value) -> System.out.printf("\n-> %s to %s", key, value));
        System.out.println("\n===================================================");
        printLocationMap(currentLocation.getMap());
    }

    private void lookAtItem(String item) {
        if (!currentLocation.getItems().contains(item)) {
            System.out.println("\nItem not found! Please try again.");
            return;
        }

        String itemDescription = Item.getDescriptions(item);
        int damageValue = Item.getDamageValue(item);
        System.out.println("\n===================================================");
        System.out.printf("\n*** Item description:\n- %s. It has a damage value of %s \n", itemDescription, damageValue);
        System.out.println("\n===================================================");
        Console.pause(1000);
    }

    private void getItem(String itemToPickup) {
        if (!currentLocation.getItems().contains(itemToPickup)) {
            System.out.println("\nItem not found! Please try again.");
            return;
        }

        player.addItem(itemToPickup);
        itemsInRoom.remove(itemToPickup);
        System.out.printf("\nAwesome! You've added the %s to your inventory!\n", itemToPickup);
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
            System.out.println("\nItem not found! Please try again.");
            return;
        }
        player.removeItem(itemToDrop);
        itemsInRoom.add(itemToDrop);
        System.out.printf("\n The %s has been removed from your inventory. ", itemToDrop);
        System.out.println(player.displayPlayerInfo());
    }

    private boolean isItemInInventory(String item) {
        return player.getInventory().contains(item);
    }


    private void movePlayer(String direction) {
        String nextLocation = currentLocation.getDirections().get(direction.toLowerCase());
        if (nextLocation == null) {
            System.out.println("\nOh no! Looks like you can't go that way!");
            return;
        }

        currentLocation = getLocation(nextLocation);
        itemsInRoom = currentLocation.getItems();
        System.out.println("\n=======================================================");
        System.out.printf("\nYou're now in -> %s \n", currentLocation.getName());
        System.out.println("\nLooking around you see the following items: ");
        itemsInRoom.forEach(item -> System.out.println("\n- " + item));
        System.out.println("\n=============================================================");
        System.out.println("\nTHERE ARE ALSO DOORS THAT LEAD TO:");
        currentLocation.getDirections().forEach((key, value) -> System.out.printf("\n-> %s to %s", key, value));
        System.out.println("\n===================================================");
        printLocationMap(currentLocation.getMap());
        if (!player.getVisitedLocations().contains(currentLocation.getName())) {
            player.getVisitedLocations().add(currentLocation.getName());
        }

        // printing the location the player have visited.. if need to print map in the future
//        for(String location : player.getVisitedLocations()){
//            System.out.println("Visited locations: " + location);
//        }

    }

    private Location getLocation(String locationName) {
        for (Location location : locations) {
            if (location.getName().equalsIgnoreCase(locationName)) {
                return location;
            }
        }
        return null;
    }

    private void printLocationMap(String mapToPrint) {
        printFile(mapToPrint);
    }

    private void printFile(String fileName) {
        //noinspection ConstantConditions

        try (BufferedReader buffer = new BufferedReader(new InputStreamReader(getClass().getClassLoader().getResourceAsStream(fileName)))) {
            buffer.lines().forEach(System.out::println);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
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
    private List<Location> locations;
    private List<String> itemsInRoom;
    private boolean gameOver = false;

    private Location starterLocation = Location.parsedLocationsFromJson().get(0);

    // after player chooses new direction/position
    private Location updatedLocation;
    private Item updatedItems;
    private Location updatedDirections;

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

            System.out.println("Let's play!\n\n");
            Console.clear();
            game();
        } else {
            printFile(NO_BANNER);
        }
    }

    private void game() {
        if (!gameOver) {
            playerPosition_NewGame();
//            printFile("images/mapBioBrain.txt");
            askPlayerAction(starterLocation);
        }
    }


    private void askPlayerAction(Location starterLocation) {
        // display user options
        System.out.println("\n\nWhat would you like to do?\n\n- Look at items \n- Move to a different location \n- Quit");
        System.out.println("\nType Look to check item, Move to go to a different location, or Quit to exit the game");

        // allow user input
        String input = prompter.prompt("\nEnter response: ", "(?i)(Look|Move|Quit)", "\nInvalid input... Please type Look, Move, or Quit \n");

        // "LOOK" - check user input
        if (input.equalsIgnoreCase("look")) {
            viewItem();

            // "MOVE" - check user input
        } else if (input.equalsIgnoreCase("move")) {

            if (starterLocation != updatedLocation) {
                movePlayer(updatedLocation);
                System.out.printf("Sounds good! Let's leave $s and head to %s. %s.", starterLocation.getDescription(), updatedLocation.getDescription());
            }


        } else if (input.equalsIgnoreCase("quit")) {
            System.out.println("\nThanks for playing!");
            gameOver = true;
        }
    }

    private void viewItem() {
        // "LOOK" - check user input
        System.out.println("\nWhich item would you like to look at?");
        String itemToLookAt = prompter.prompt("Enter item name: ");

        // provide item details
        if (starterLocation.getItems().contains(itemToLookAt)) {
            String itemDescription = Item.getDescriptions(itemToLookAt);
            int damageValue = Item.getDamageValue(itemToLookAt);
            System.out.printf("\nItem description:  %s it has a damage value of %s", itemDescription, damageValue);

            // allow/check for "QUIT"
        } else if (itemToLookAt.equalsIgnoreCase("quit")) {
            System.out.println("\nThanks for playing!");
            gameOver = true;
        } else {
            System.out.println("\nItem not found");
        }

        askPlayerAction(starterLocation);
    }


    private void playerPosition_NewGame() {
        System.out.println("Your plan worked! You reprogrammed the BioBrain and now they know everything!\n" +
                "It's the last day before the director shuts down the Jung_E project. If you're going to escape with\n" +
                "the BioBrain it's now or never. Hurry! Try to make it to the Train Dock! There isn't much time!\n\n");
        Console.pause(8000);


        System.out.println("BEEP BEEP BEEP!");
        System.out.println("That's the alarm! Someone must have detected that the BioBrain is missing from the Production Room!\n" +
                "You've got to get moving!");
        Console.clear();

        System.out.printf("\nYou are currently in %s. %s\n", starterLocation.getName(), starterLocation.getDescription());
        System.out.println("\nTaking a look around, you see the following items: ");
        for (String item : starterLocation.getItems()) {
            System.out.print("\n " + item);
        }

        System.out.printf("\n\nYou can choose to go %s ", starterLocation.getDirections().get("east"));
        System.out.printf("\nOr you can choose to head South to %s ", starterLocation.getDirections().get("south"));
    }

    private void movePlayer(Location starterLocation) {
        System.out.println("\nWhich direction would you like to move to?");

        // prompt for user's direction choice
        String direction = prompter.prompt("Enter direction: \n\n");

        // allow/check for "QUIT"
        if (direction.equalsIgnoreCase("q") || direction.equalsIgnoreCase("quit")) {
            System.out.println("\nThanks for playing!");
            gameOver = true;

            // update position
        } else if (direction.equalsIgnoreCase("east")) {
            updatedLocation = Location.parsedLocationsFromJson().get(1);
            updateAll();

            System.out.printf("You've made it to %s. %s.", updatedLocation.getName(), updatedLocation.getDescription());
            System.out.println("\nTaking a look around, you see the following items: ");
            for (String item : updatedLocation.getItems()) {
                System.out.print("\n " + item);
            }

        } else if (direction.equalsIgnoreCase("south")) {
            updatedLocation = Location.parsedLocationsFromJson().get(3);
            updateAll();

            System.out.printf("You've made it to %s. %s.", updatedLocation.getName(), updatedLocation.getDescription());
            System.out.println("\nTaking a look around, you see the following items: ");
            for (String item : updatedLocation.getItems()) {
                System.out.print("\n " + item);
            }
        } else {
            System.out.println("Whoops! Doesn't look like you can go that way!");
        }
        askPlayerAction(starterLocation);
    }

    private void updateAll() {
        // using as extract method
        updatedLocation.getNpc();
        updatedLocation.getName();
        updatedLocation.getItems();
        updatedLocation.getDirections();
        starterLocation = updatedLocation;
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

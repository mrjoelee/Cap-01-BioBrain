package com.biobrain.app;

import com.biobrain.model.*;
import com.biobrain.util.Console;
import com.biobrain.util.Prompter;
import com.biobrain.view.View;
import com.biobrain.view.locations.LocationManager;

import java.util.*;

import static com.biobrain.util.Printer.printFile;


public class BioBrainApp {
    private static final String GAME_INTRO = "intro/intro.txt";
    private static final String START_GAME = "intro/startGame.txt";
    private static final String SPLASH_SCREEN = "images/welcomeRobot.txt";
    private static final String NO_BANNER = "images/dontWantToPlayBanner.txt";
    private static final String MAIN_MAP = "images/mapMain.txt";
    private static final String PROMPT_TO_CONTINUE = "\n Press [ENTER] to continue...";
    private final Prompter prompter = new Prompter();
    private Player player;
    private String npc;
    private Location currentLocation;
    private Map<String, Location> locations;
    Map<String, Boolean> lockedLocations = new HashMap<>();
    private Map<String, String> directions;
    private List<String> itemsInRoom;
    private boolean isLaser = true;
    private boolean gameOver = false;
    private String randomDialogue = Npc.getRandomDialogue();

    public final View view = new View();

    // APP METHODS
    public void execute() {
        setPlayer(Player.create());
        LocationManager locationManager = new LocationManager(false);
        locations = locationManager.getLocations();
        player = Player.create();
        intro();
        welcome();
       askIfUserWantToPlay();
    }

    public void intro() {
        printFile(GAME_INTRO);
        promptContinue();
        Console.clear();
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
        System.out.println("                                            *** BEEP *** BEEP *** BEEP! ***\n");
        System.out.println("\n              That's the alarm! Someone must have detected that the BioBrain is missing from the Production Room!\n" +
                "                                                    You've got to get moving!");
        promptContinue();
        Console.clear();
        printFile(MAIN_MAP);
        currentPlayerLocation();
        lockDoors();                // sets the map of locked doors via the "isLocked" attribute from locations.json
        if (!gameOver) {
            while (!gameOver) {
                askPlayerAction();
            }
        }
    }

    public void currentPlayerLocation() {
        System.out.println(player.displayPlayerInfo());
        Console.pause(1000);
        setLocations(locations);

        if (locations == null || locations.isEmpty()) {
            System.out.println("Error in getting the location");
            return;
        }

        currentLocation = locations.get("sector2");
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

    // pulls data from locations.json to create a map of locked sectors that cannot be entered
    private void lockDoors() {
        // create a temporary map to hold data
        Map<String,Boolean> tempMap = new HashMap<>();

        // iterate over the locations map using a foreach on the location map's entryset
        for (Map.Entry<String, Location> entry : getLocations().entrySet()) {
            // puts the starting status of all locked sectors into one place for easy reference/manipulation
            tempMap.put(entry.getValue().getName(), entry.getValue().isLocked());
        }

        // set locked room map
        setLockedLocations(tempMap);
    }

    // sets location as unlocked for future access to the location by the player
    private void unlockADoor(String locationName) {
        getLockedLocations().put(locationName, false);
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
                validateThenGetItem(noun);
                break;
            case "use":
                validateThenUseItem(noun);
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
            System.out.printf("\n%s not found! Please try again.", item);
            return;
        }

        String itemDescription = Item.getDescriptions(item);
        int damageValue = Item.getDamageValue(item);
        if (damageValue > 0) {
            System.out.println("\n===================================================");
            System.out.printf("\n*** Weapon Description:\n- %s. It has a Damage value of %s \n", itemDescription, damageValue);
            System.out.println("\n===================================================");
            Console.pause(1000);
        } else {
            System.out.println("\n===================================================");
            System.out.printf("\n*** Item Description:\n- %s.", itemDescription);
            System.out.println("\n===================================================");
            Console.pause(1000);
        }

    }

    public void validateThenGetItem(String itemToPickup) throws IllegalArgumentException{
        if (!currentLocation.getItems().contains(itemToPickup)) {
            throw new RuntimeException(new IllegalArgumentException("\n"+ itemToPickup +" was not found! Please try again."));
        } else {
            getItem(itemToPickup);
        }
    }

    private void getItem(String itemToPickup) {
        addToPlayerInventory(itemToPickup);
        itemsInRoom.remove(itemToPickup);
        System.out.printf("\nAwesome! You've added the %s to your inventory!\n", itemToPickup);
        System.out.println(player.displayPlayerInfo());
        Console.pause(1000);
    }

    private void addToPlayerInventory(String itemToPickup) {
        player.addItem(itemToPickup, player.getInventory().get(itemToPickup));
        itemsInRoom.remove(itemToPickup);
        System.out.printf("\nAwesome! You've added the %s to your inventory!\n", itemToPickup);
    }

    // validates the item used was located in the player's inventory,
    // if not, displays a message that the item is not in possession
    public void validateThenUseItem(String usedItem) throws IllegalArgumentException{
        if (player.getInventory().containsKey(usedItem)) {
            useItem(usedItem);
        } else {
            throw new RuntimeException(new IllegalArgumentException("\nYou're not carrying a " + usedItem+ " to be able to use!"));
        }
    }

    // use an item from the inventory
    private void useItem(String usedItem) {
        Map<String, Item> inv = player.getInventory();// get a reference to the inventory map
        String networkLocation = locations.get("Sector 2 - Control Lab").getName(); //must be in sector 2 to hack the network interface
        String playerLocation = currentLocation.getName(); // get the player current location
        Set<String> itemForSphere = Set.of("memory", "interface", "motherboard"); //using set since we are checking for a certain item (faster)
        String sphere = "A.I. Transfer Sphere";

        // todo use memory + interface + motherbooard = AI Sphere REVIEW LOGIC WITH TEAM
        if (itemForSphere.contains(usedItem) && itemForSphere.stream().allMatch(item -> inv.containsKey(item))) {
            player.addItem("sphere", Item.getAllItems().get("sphere"));

            //once combines to a sphere, it will remove the 3 elements
            for (String item : itemForSphere) {
                player.removeItem(item, inv.get(item));
            }
            System.out.printf("\nAwesome! You've added the %s to your inventory!\n", sphere);
            System.out.println(player.displayPlayerInfo());

        }
        // todo use sphere = download biobrain REVIEW LOGIC WITH TEAM
        // needs a check to ensure laser shield is disabled
        else if (usedItem.equalsIgnoreCase("sphere") ) {
            handleUseSphere(playerLocation);

        } // todo use tablet = to disable laser
        else if (usedItem.equalsIgnoreCase("tablet") && player.getInventory().containsKey(usedItem)) {
            handleUseTablet(playerLocation, networkLocation);
        }
        // todo use weapons
        System.out.println(player.displayPlayerInfo()); // finally, display player info to user again
    }

    //using sphere - checking for location and laser logic
    private void handleUseSphere(String playerLocation) {
        String location = "Sector 1 - Weapons Chamber";
        if (!playerLocation.equalsIgnoreCase(location)) {
            System.out.printf("You must use the sphere inside %s to get the biobrain", location);
        } else if (isLaser) {
            System.out.println("biobrain is protected with laser shield. You must hack the network interface with" +
                    "the table first to disable the laser shield");
        } else {
            player.addItem("biobrain", Item.getAllItems().get("biobrain"));
            System.out.printf("\nAwesome! You've added the %s to your inventory!\n", "BioBrain");
        }
    }

    //using tablet - checking for location and laser logic
    private void handleUseTablet(String playerLocation, String networkLocation) {
        if (!playerLocation.equalsIgnoreCase(networkLocation)) {
            System.out.printf("\nYou can use the tablet in the Network Interface location in %s", networkLocation);
        } else {
            System.out.println("\nYou have successfully disable the laser shield");
            isLaser = false;
        }
    }

    private void showInventory() {
        Console.pause(1500);
        Map<String, Item> inventory = player.getInventory();
        if (inventory.isEmpty()) {
            System.out.println("\nYou have no items in your inventory");
            return;
        }

        Console.clear();
        System.out.println("\n ======================================================================");
        System.out.println("\nYou have the following items in your inventory: " + inventory.keySet());
        System.out.println("\n ======================================================================");
        Console.pause(1500);
    }

    private void dropItem(String itemToDrop) {
        if (!isItemInInventory(itemToDrop)) {
            System.out.println("\nItem not found! Please try again.");
            return;
        }
        player.removeItem(itemToDrop, player.getInventory().get(itemToDrop));
        itemsInRoom.add(itemToDrop);
        System.out.printf("\n The %s has been removed from your inventory. ", itemToDrop);
        System.out.println(player.displayPlayerInfo());
    }

    private boolean isItemInInventory(String item) {
        return player.getInventory().containsKey(item);
    }

    // moves player object to new location
    // redraws
    // objects associated with that room on the map
    private void movePlayer(String direction) {
        String nextLocation = currentLocation.getDirections().get(direction.toLowerCase());
        if (nextLocation == null) {
            System.out.println("\nOh no! Looks like you can't go that way!");
            return;
        }

        // only move player if nextLocation is not in the map of lockedLocations
        if (!getLockedLocations().get(nextLocation)) {
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
        } else {
            System.out.println("\nThe doors to the " + nextLocation + " are LOCKED!\nYou must find a way to UNLOCK them to enter!");
            printLocationMap(currentLocation.getMap());
            return;
        }

        // printing the location the player have visited.. if need to print map in the future
        for(String location : player.getVisitedLocations()){
            System.out.println("Visited locations: " + location);
        }

    }

    // returns the Location object given the String locationName
    private Location getLocation(String locationName) {
        // only return a value if the locations Map contains the locationName
        if (getLocations().containsKey(locationName)) {
            return getLocations().get(locationName);
        }
        return null;
    }

    private void printLocationMap(String mapToPrint) {
        printFile(mapToPrint);
    }


    //pauses & allows user to continue
    private void promptContinue() {
        System.out.println(PROMPT_TO_CONTINUE);
        Scanner scanner = new Scanner(System.in);
        scanner.nextLine();
    }


    // ACCESSOR METHODS
    public Player getPlayer() {
        return player;
    }

    public void setPlayer(Player player) {
        this.player = player;
    }

    public Map<String, Location> getLocations() {
        return locations;
    }

    public void setLocations(Map<String, Location> locations) {
        this.locations = locations;
    }

    public Map<String, Boolean> getLockedLocations() {
        return lockedLocations;
    }

    public void setLockedLocations(Map<String, Boolean> lockedLocations) {
        this.lockedLocations = lockedLocations;
    }
}
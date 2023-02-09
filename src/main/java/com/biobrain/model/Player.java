package com.biobrain.model;

/*
 * Player | Class
 * holds player data
 *
 */

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Player extends Character {

    public Map<String,Item> inventory;
    private List<String> visitedLocations;


    public Player() {
        visitedLocations = new ArrayList<>();
        inventory = new HashMap<>();
    }

    public void addVisitedLocation(String location){
        visitedLocations.add(location);
    }

    public List<String> getVisitedLocations(){
        return visitedLocations;
    }
    public static Player create() {
        return new Player();
    }

    public void addItem(String itemName, Item item) {
        inventory.put(itemName,item);
    }

    public void removeItem(String itemName, Item item) { inventory.remove(itemName, item); }

    public Map<String,Item> getInventory() {
        return inventory;
    }

    public String displayPlayerInfo() {

        if (inventory.isEmpty()) {
            return String.format("\n =========== PLAYER INFO =================\n" +
                    "\n      Your health is at %s", getHealth() +
                    "\n      Your current inventory is empty.\n" +
                    "\n\n =========================================\n");
        }

        return String.format("\n =========== PLAYER INFO =================\n" +
                "\n          Your health is at %s", getHealth() +
                "\n    Your current inventory: " + getInventory().keySet() +
                "\n\n =========================================\n");
    }
}

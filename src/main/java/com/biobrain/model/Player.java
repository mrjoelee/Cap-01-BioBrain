package com.biobrain.model;

import java.util.ArrayList;
import java.util.List;

public class Player extends Character {

    public ArrayList<String> inventory;
    private List<String> visitedLocations;


    public Player() {
        visitedLocations = new ArrayList<>();
        inventory = new ArrayList<String>();
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

    public void addItem(String item) {
        inventory.add(item);
    }

    public void removeItem(String item) {
        inventory.remove(item);
    }

    public ArrayList<String> getInventory() {
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
                "\n    Your current inventory: " + getInventory() +
                "\n\n =========================================\n");
    }
}

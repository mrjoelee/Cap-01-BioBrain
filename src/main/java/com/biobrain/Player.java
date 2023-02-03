package com.biobrain;

import java.util.ArrayList;

public class Player extends Character {

    public ArrayList<String> inventory;

    public Player() {
        inventory = new ArrayList<String>();
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
                "\n      Your health is at %s", getHealth() +
                "\n      Your current inventory: " + getInventory() +
                "\n\n =========================================\n");
    }
}

package com.biobrain;

import java.util.ArrayList;
import java.util.List;

public class Player extends Character {

    public ArrayList<String> inventory;

    public Player() {
        inventory = new ArrayList<String>();
    }
    public static Player create(){
        return new Player();
    }

    public void addItem(String item){
        inventory.add(item);
    }

    public void removeItem(String item){
        inventory.remove(item);
    }

    public ArrayList<String> getInventory(){
        return inventory;
    }

    public String displayPlayerInfo(){
        return String.format("\nYour health is at %s", getHealth());
    }

    public static void main(String[] args) {
        Player p1 = Player.create();
        System.out.println(p1.displayPlayerInfo());
    }
}

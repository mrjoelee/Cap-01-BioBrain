package com.biobrain;

import com.google.gson.Gson;

import java.io.InputStream;
import java.io.InputStreamReader;

public class Item {
    private String name;
    private String description;
    private int damage;

    public Item(String name, String description, int damage) {
        this.name = name;
        this.description = description;
        this.damage = damage;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }


    public String getDescription() {
        return description;
    }

    public int getDamage() {
        return damage;
    }

    public void setDamage(int damage) {
        this.damage = damage;
    }

    public static Item[] getAllItems() {
        Gson gson = new Gson();
        InputStream inputStream = Item.class.getClassLoader().getResourceAsStream("jsonFiles/items.json");
        InputStreamReader reader = new InputStreamReader(inputStream);
        return gson.fromJson(reader, Item[].class);
    }

    public static String getDescriptions(String itemName) {
        Item[] allItems = getAllItems();
        for (Item item : allItems) {
            if (item.getName().equalsIgnoreCase(itemName)) {
                return item.getDescription();
            }
        }
        return "Item description not found";
    }

    public static int getDamageValue(String itemName) {
        Item[] allItems = getAllItems();
        for (Item item : allItems) {
            if (item.getName().equalsIgnoreCase(itemName)) {
                return item.getDamage();
            }
        }
        return 0;
    }
}



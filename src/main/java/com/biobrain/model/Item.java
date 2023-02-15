package com.biobrain.model;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.util.Map;

public class Item implements Inventory{
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

    public static Map<String,Item> getAllItems() {
        Gson gson = new Gson();
        Type itemMap = new TypeToken<Map<String,Item>>() {
        }.getType();
        InputStream inputStream = Item.class.getClassLoader().getResourceAsStream("jsonFiles/items.json");
        InputStreamReader reader = new InputStreamReader(inputStream);
        return gson.fromJson(reader, itemMap);
    }

    public static String getDescriptions(String itemName) {
        if(getAllItems().containsKey(itemName)){
            return getAllItems().get(itemName).getDescription();
        }
        return "Item description not found";
    }

    public static int getDamageValue(String itemName) {
        if(getAllItems().containsKey(itemName)){
            return getAllItems().get(itemName).getDamage();
        }
        return 0;
    }

}



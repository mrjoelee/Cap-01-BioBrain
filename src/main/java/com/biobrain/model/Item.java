package com.biobrain.model;

import com.biobrain.util.JSONParser;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.util.HashMap;
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
        Map<String, Item> items = new HashMap<>();
        JSONParser.parseItemFromJSON().forEach(x -> items.put(x.getName(), x));
        return items;
    }

    public static Item itemName(String name){
        return getAllItems().get(name);
    }
}
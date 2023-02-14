package com.biobrain.items;

import com.biobrain.model.Location;
import com.biobrain.util.FileLoader;
import com.biobrain.view.entities.ItemEntity;
import com.biobrain.view.panels.GamePanel;
import com.biobrain.view.tile.Tile;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.awt.*;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ItemManager {
    private final Map<String, ItemEntity> items;
    public GamePanel gamePanel;

    // CTOR
    public ItemManager(GamePanel gamePanel) {
        this.gamePanel = gamePanel;
        items = new HashMap<>();
        generateItems(parseItemsFromJson());
    }

    public void generateItems(Map<String, ItemEntity> itemMap) {
        itemMap.values().forEach((item) -> {
            Rectangle collider = new Rectangle(item.getX(), item.getY(), 48, 48);
            item.setItemCollider(collider);
            item.setItemImage(item.getImagePath());
            items.put(item.getName(), item);
        });
    }

    public static Map<String, ItemEntity> parseItemsFromJson() {
        Gson gson = new Gson();
        Type itemMap = new TypeToken<Map<String, ItemEntity>>() {
        }.getType();

        //noinspection ConstantConditions
        try (InputStream input = Location.class.getClassLoader().getResourceAsStream("jsonFiles/items.json");
             BufferedReader reader = new BufferedReader(new InputStreamReader(input))) {
            return gson.fromJson(reader, itemMap);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public void draw(Graphics2D g2) {
        for (ItemEntity item : items.values()) {
            if(item.getRoomCode() == gamePanel.currentRoom.getRoomCode()){
                item.draw(g2);
            }
        }
    }


    // ACCESSOR METHODS
    public Map<String, ItemEntity> getItems() {
        return items;
    }

}
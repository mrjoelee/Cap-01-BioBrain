package com.biobrain.items;

import com.biobrain.view.entities.ItemEntity;
import com.biobrain.view.panels.GamePanel;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.awt.*;
import java.io.*;
import java.lang.reflect.Type;
import java.util.*;
import java.util.List;

public class ItemManager {
    private final Map<String, ItemEntity> items;
    public GamePanel gamePanel;

    // CTOR
    public ItemManager(GamePanel gamePanel) {
        this.gamePanel = gamePanel;
        items = new HashMap<>();
        generateItems(parseItemsFromJson());
    }

    public void generateItems(List<ItemEntity> itemList) {
        itemList.forEach((item) -> {
            Rectangle collider = new Rectangle(item.getXin(), item.getYin(), 48, 48);
            item.setItemCollider(collider);
            item.setItemImage(item.getImagePath());
            items.put(item.getCookie(), item);
        });
    }

//    public static List<ItemEntity> parseItemsFromJson() {
//        Gson gson = new Gson();
//        Type itemList = new TypeToken<List<ItemEntity>>() {
//        }.getType();
//
//        //noinspection ConstantConditions
//        try (InputStream input = ItemManager.class.getClassLoader().getResourceAsStream("jsonFiles/items.json");
//             BufferedReader reader = new BufferedReader(new InputStreamReader(input))) {
//            return gson.fromJson(reader, itemList);
//        } catch (Exception e) {
//            e.printStackTrace();
//            return null;
//        }
//    }

    public static List<ItemEntity> parseItemsFromJson() {
        Gson gson = new Gson();
        List<ItemEntity> list = new ArrayList<>();
        try (Reader reader = new InputStreamReader(Objects.requireNonNull(ItemManager.class.getClassLoader().getResourceAsStream("jsonFiles/items.json")))) {
            Type itemType = new TypeToken<ArrayList<ItemEntity>>() {
            }.getType();

            list = gson.fromJson(reader, itemType);

        } catch (IOException e) {
            e.printStackTrace();
        }
        return list;
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
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
import java.util.stream.Collectors;

public class ItemManager {
    private final Map<String, ItemEntity> items;
    public GamePanel gamePanel;

    public ItemManager(GamePanel gamePanel) {
        this.gamePanel = gamePanel;
        items = new HashMap<>();
        parseItemsFromJson();
        List<ItemEntity> test = parseItemsFromJson();

        generateItems(test);
    }

    public void generateItems(List<ItemEntity> itemList) {
        itemList.forEach((item) -> {
            Rectangle collider = new Rectangle(item.getX(), item.getY(), 48, 48);
            item.setItemCollider(collider);
//            item.setItemImage(item.getImagePath());
            items.put(item.getName(), item);
        });
    }


    public static List<ItemEntity> getItemsByRoomCode(int roomCode){
        return parseItemsFromJson().stream().filter(e-> e.getRoomCode() == roomCode).collect(Collectors.toList());
    }

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
            if (item != null && item.getRoomCode() == gamePanel.currentRoom.getRoomCode()) {
                item.draw(g2);
            }
        }
    }

   // ACCESSOR METHODS
    public Map<String, ItemEntity> getItems() {
        return items;
    }
}
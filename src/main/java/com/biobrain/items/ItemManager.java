package com.biobrain.items;

import com.biobrain.util.FileLoader;
import com.biobrain.model.Location;
import com.biobrain.view.entities.ItemEntity;
import com.biobrain.view.locations.LocationManager;
import com.biobrain.view.panels.GamePanel;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.awt.*;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.lang.reflect.Type;
import java.util.List;
import java.util.*;

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
            items.put(item.getName(), item);
        });
    }


    public List<ItemEntity> getItemsByRoomCode(int roomCode) {
        List<ItemEntity> itemsInRoom = new ArrayList<>();

        Location location = gamePanel.locations.getRoomByCode(roomCode);

        if (location != null) {
            location.getItems().forEach(x -> itemsInRoom.add(items.get(x)));
        }

        return itemsInRoom;
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
            if (item != null && item.getName().equalsIgnoreCase("biobrain") && !gamePanel.isLaser && gamePanel.currentRoom.getRoomCode() == item.getRoomCode()) {
                String noLaserBioBrain = "images/items/biobrainNoLaser.png";
                item.draw(g2, FileLoader.loadBuffered(noLaserBioBrain));
                item.setImage(noLaserBioBrain);
            } else if (item != null && gamePanel.currentRoom.getRoomCode() == item.getRoomCode()) {
                item.draw(g2, item.getItemImage());
            }
        }
    }

    // ACCESSOR METHODS
    public Map<String, ItemEntity> getItems() {
        return items;
    }
}
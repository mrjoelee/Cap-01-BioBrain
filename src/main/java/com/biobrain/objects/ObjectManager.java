package com.biobrain.objects;

import com.biobrain.items.ItemManager;
import com.biobrain.util.JSONParser;
import com.biobrain.view.entities.ItemEntity;
import com.biobrain.view.entities.SuperObject;
import com.biobrain.view.panels.GamePanel;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.awt.*;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.lang.reflect.Type;
import java.util.*;
import java.util.List;
import java.util.stream.Collectors;

public class ObjectManager {
    public Map<String, SuperObject> object;
    public GamePanel gamePanel;
    private final List<SuperObject> objects;

    static List<SuperObject> objectsList = JSONParser.parseObjectFromJson();
    public ObjectManager(GamePanel gamePanel){
        this.gamePanel=gamePanel;
        object = new HashMap<>();
        objects = new ArrayList<>();
        generateObjects();
    }

    private void generateObjects() {
        for(SuperObject obj : objectsList){
            Rectangle collider = new Rectangle(obj.getX()*2, obj.getY()*2, (int) (obj.getWidth()*1.5), obj.getHeight()*3);
            obj.setObjectCollider(collider);
            obj.setCollision(true);
            obj.setDefaultX(obj.getX()*2);
            obj.setDefaultY(obj.getY()*2);
            object.put(obj.getName(),obj);
            objects.add(obj);
        }
    }

    public static List<SuperObject> getObjectsByRoomCode(int roomCode){
        return objectsList.stream().filter(e-> e.getRoomCode() == roomCode).collect(Collectors.toList());
    }

    public void draw(Graphics2D g2) {
        for (SuperObject obj : object.values()) {
            if (obj.getRoomCode() == gamePanel.currentRoom.getRoomCode()) {
                obj.draw(g2);
            }
        }
    }

    public List<SuperObject> getObjects(){
        return objects;
    }
}

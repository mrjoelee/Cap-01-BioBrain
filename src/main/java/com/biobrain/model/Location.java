package com.biobrain.model;

import com.biobrain.view.tile.Tile;
import com.biobrain.view.tile.TileSetter;

import java.awt.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Location {
    private String name;
    private int roomCode;
    private String shortName;
    private String guiMap;
    private String map;
    private List<String> items;
    private final boolean isSector;
    private boolean isLocked;
    private String description;
    private String npc;
    private Map<String, String> directions;
    private Rectangle entrance;
    private Rectangle exit;

    public Location(String name, int roomCode, String shortName,String guiMap, String map, List<String> items, boolean isSector, boolean isLocked,
                    String description, String npc, Map<String, String> directions,Rectangle entrance, Rectangle exit) {
        this.name = name;
        this.roomCode = roomCode;
        this.shortName = shortName;
        this.guiMap = guiMap;
        this.map = map;
        this.items = items;
        this.isSector = isSector;
        this.isLocked = isLocked;
        this.description = description;
        this.npc = npc;
        this.directions = directions;
        this.entrance = entrance;
        this.exit = exit;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getRoomCode() {
        return roomCode;
    }

    public void setRoomCode(int roomCode) {
        this.roomCode = roomCode;
    }

    public String getShortName() {
        return shortName;
    }

    public void setShortName(String shortName) {
        this.shortName = shortName;
    }

    public String getGuiMap(){
        return this.guiMap;
    }

    public void setGuiMap(String guiMap) {
        this.guiMap = guiMap;
    }

    public String getMap() {
        return map;
    }

    public void setMap(String map) {
        this.map = map;
    }

    public List<String> getItems() {
        return items;
    }

    public void setItems(List<String> items) {
        this.items = items;
    }

    public boolean isSector() {
        return isSector;
    }

    public boolean isLocked() {
        return isLocked;
    }

    public void setLocked(boolean locked) {
        isLocked = locked;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getNpc() {
        return npc;
    }

    public void setNpc(String npc) {
        this.npc = npc;
    }

    public Map<String, String> getDirections() {
        return directions;
    }

    public void setDirections(Map<String, String> directions) {
        this.directions = directions;
    }

    public Rectangle getEntrance() {
        return entrance;
    }

    public void setEntrance(Rectangle entrance) {
        this.entrance = entrance;
    }

    public Rectangle getExit() {
        return exit;
    }

    public void setExit(Rectangle exit) {
        this.exit = exit;
    }

    public String getLockedMessage(){
        String message = "";
        if(isLocked()) {
            message = String.format("%s seems to be locked. You need to find a way to unlock it", getShortName());
        }
        return message;
    }
    public void draw(Graphics2D g2){
        if(entrance != null){
            g2.drawRect(entrance.x, entrance.y, entrance.width, entrance.height);
        }

        if(exit != null){
            g2.drawRect(exit.x, exit.y, exit.width, exit.height);
        }
    }
}


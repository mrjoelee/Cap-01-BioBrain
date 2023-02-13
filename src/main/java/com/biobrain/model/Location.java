package com.biobrain.model;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.util.List;
import java.util.Map;

public class Location {

    private String name;
    private Boolean isLocked;
    private Map<String, String> directions;
    private Map<String,String> cage;
    private String description;
    private List<String> items;
    private String npc;
    private String map;

    public Location(String name, Boolean isLocked,Map<String,String> cage, Map<String, String> directions, String description, List<String> items, String npc, String map) {
        this.name = name;
        this.isLocked = isLocked;
        this.directions = directions;
        this.cage = cage;
        this.description = description;
        this.items = items;
        this.npc = npc;
        this.map = map;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Boolean getLocked() {
        return isLocked;
    }

    public void setLocked(Boolean locked) {
        isLocked = locked;
    }

    public Map<String, String> getDirections() {
        return directions;
    }

    public void setDirections(Map<String, String> directions) {
        this.directions = directions;
    }

    public Map<String, String> getCage() {
        return cage;
    }

    public void setCage(Map<String, String> cage) {
        this.cage = cage;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public List<String> getItems() {
        return items;
    }

    public void setItems(List<String> items) {
        this.items = items;
    }

    public String getNpc() {
        return npc;
    }

    public void setNpc(String npc) {
        this.npc = npc;
    }

    public String getMap() {
        return map;
    }

   public static Map<String,Location>parsedLocationsFromJson() {
       Gson gson = new Gson();
       Type locationMap = new TypeToken<Map<String,Location>>() {
       }.getType();
       //noinspection ConstantConditions
       try (InputStream input = Location.class.getClassLoader().getResourceAsStream("jsonFiles/locations.json");
            BufferedReader reader = new BufferedReader(new InputStreamReader(input))) {
           return gson.fromJson(reader, locationMap);
       } catch (Exception e) {
           e.printStackTrace();
           return null;
       }
   }
}


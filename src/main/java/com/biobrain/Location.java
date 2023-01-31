package com.biobrain;

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
    private Map<String, String> directions;
    private String description;
    private List<String> items;

    private String npc;

    public Location(String name, Map<String, String> directions, String description, List<String> items, String npc) {
        this.name = name;
        this.directions = directions;
        this.description = description;
        this.items = items;
        this.npc = npc;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Map<String, String> getDirections() {
        return directions;
    }

    public void setDirections(Map<String, String> directions) {
        this.directions = directions;
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

   public static List<Location>parsedLocationsFromJson() {
       Gson gson = new Gson();
       Type locationList = new TypeToken<List<Location>>() {
       }.getType();
       try (InputStream input = Location.class.getClassLoader().getResourceAsStream("jsonFiles/locations.json");
            BufferedReader reader = new BufferedReader(new InputStreamReader(input))) {
           return gson.fromJson(reader, locationList);
       } catch (Exception e) {
           e.printStackTrace();
           return null;
       }
   }
}


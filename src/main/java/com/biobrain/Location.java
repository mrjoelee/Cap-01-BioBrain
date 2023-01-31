package com.biobrain;

import com.google.gson.Gson;

import java.io.InputStream;
import java.io.InputStreamReader;
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

//    public static List<Location> getAllLocations() {
//        Gson gson = new Gson();
//        InputStream inputStream = Location.class.getClassLoader().getResourceAsStream("jsonFiles/locations.json");
//        InputStreamReader reader = new InputStreamReader(inputStream);
//        return gson.fromJson(reader, List.class);
//    }
//
//    public static String getLocationDescription(String locationName) {
//        List<Location> allLocations = getAllLocations();
//        for (Location location : allLocations) {
//            if (location.getName().equalsIgnoreCase(locationName)) {
//                return location.getDescription();
//            }
//        }
//        return "Location description not found";
//    }
//
//    public static Map<String, String> getDirections(String locationName) {
//        List<Location> allLocations = getAllLocations();
//        for (Location location : allLocations) {
//            if (location.getName().equalsIgnoreCase(locationName)) {
//                return location.getDirections();
//            }
//        }
//        return null;
//    }
}


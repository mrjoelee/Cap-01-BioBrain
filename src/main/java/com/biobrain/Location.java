package com.biobrain;

import java.util.List;
import java.util.Map;

public class Location {

    private String name;
    private Map<String, String> directions;
    private String description;
    private List<String> items;

    public Location(String name, Map<String, String> directions, String description, List<String> items) {
        this.name = name;
        this.directions = directions;
        this.description = description;
        this.items = items;
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


}


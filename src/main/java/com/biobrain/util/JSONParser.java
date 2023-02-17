package com.biobrain.util;

import com.biobrain.model.Item;
import com.biobrain.objects.ObjectManager;
import com.biobrain.view.entities.ItemEntity;
import com.biobrain.view.entities.SuperObject;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.biobrain.model.Location;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class JSONParser {
    private static final Gson gson = new Gson();
    private static final String LOCATIONS_PATH = "jsonFiles/locations.json";
    private static final String OBJECTS_PATH = "jsonFiles/objects.json";

    private JSONParser() {
    }

    public static List<Location> mapJSONRoomsToObjects() {
        List<Location> list = new ArrayList<>();
        try (Reader reader = new InputStreamReader(Objects.requireNonNull(JSONParser.class.getClassLoader().getResourceAsStream(LOCATIONS_PATH)))) {
            Type locationListType = new TypeToken<ArrayList<Location>>() {
            }.getType();
            list = gson.fromJson(reader, locationListType);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return list;
    }

    public static List<SuperObject> parseObjectFromJson() {
        Gson gson = new Gson();
        List<SuperObject> objectList = new ArrayList<>();
        try (Reader reader = new InputStreamReader(Objects.requireNonNull(ObjectManager.class.getClassLoader().getResourceAsStream("jsonFiles/objects.json")))) {
            Type objectType = new TypeToken<ArrayList<SuperObject>>() {
            }.getType();
            objectList = gson.fromJson(reader, objectType);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return objectList;
    }

    public static List<Item> parseItemFromJSON(){
        Gson gson = new Gson();
        List<Item> itemList = new ArrayList<>();
        try (Reader reader = new InputStreamReader(Objects.requireNonNull(ObjectManager.class.getClassLoader().getResourceAsStream("jsonFiles/items.json")))) {
            Type objectType = new TypeToken<ArrayList<Item>>() {
            }.getType();
            itemList = gson.fromJson(reader, objectType);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return itemList;
    }
}
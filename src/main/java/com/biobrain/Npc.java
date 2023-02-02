package com.biobrain;

import com.google.gson.Gson;

import java.io.InputStream;
import java.io.InputStreamReader;

public class Npc extends Character {
    private String name;
    private String weapon;
    private String description;

    public Npc(String name, String description, String weapon) {
        this.name = name;
        this.description = description;
        this.weapon = weapon;

    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public  String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getWeapon() {
        return weapon;
    }

    public void setWeapon(String weapon) {
        this.weapon = weapon;
    }

    public static Npc[] getAllNpcs() {

        Gson gson = new Gson();
        InputStream inputStream = Npc.class.getClassLoader().getResourceAsStream("jsonFiles/npc.json");
        InputStreamReader reader = new InputStreamReader(inputStream);
        return gson.fromJson(reader, Npc[].class);
    }

    public static String getDescriptions(String npcName) {
        Npc[] allNpcs = getAllNpcs();
        for (Npc npc : allNpcs) {
            if (npc.getName().equalsIgnoreCase(npcName)) {
                return npc.getDescription();
            }
        }
        return "Npc description not found";
    }

    public static String getWeapon(String npcName) {
        Npc[] allNpcs = getAllNpcs();
        for (Npc npc : allNpcs) {
            if (npc.getName().equalsIgnoreCase(npcName)) {
                return npc.getWeapon();
            }
        }
        return "Npc weapon not found";
    }
}



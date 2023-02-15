package com.biobrain.view.locations;

import com.biobrain.model.Location;
import com.biobrain.util.FileLoader;
import com.biobrain.util.JSONParser;
import com.biobrain.view.panels.GamePanel;
import com.biobrain.view.tile.Tile;
import com.biobrain.view.tile.TileHelper;
import com.biobrain.view.tile.TileSetter;

import java.awt.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/*
*Calls the JSONParser to load all the rooms/sectors then add their exits/entrances by calling AccessManager
*Sets the tiles for the room by calling the tileSetter
*/
public class LocationManager {
    private final Map<String, Location> locations;
    private final List<Location> rooms;
    private boolean isGui;

    public LocationManager(boolean isGui) {
        this.isGui = isGui;
        locations = new HashMap<>();
        rooms = new ArrayList<>();
        generateSectors();
    }

    private void generateSectors(){
        List<Location> allRooms = JSONParser.mapJSONRoomsToObjects();

        for(Location curr : allRooms){
            if(isGui)
            {
                if (curr.isSector()) {
                    curr.setExit(AccessManager.sectorExit());
                    curr.setEntrance(AccessManager.getRoomEntrance(curr.getShortName()));

                }
                TileHelper.loadMap(curr);
            }
            locations.put(curr.getName(), curr);
            locations.put(curr.getShortName(), curr);
            rooms.add(curr);
        }
    }

    public List<Location> getRooms(){
        return rooms;
    }

    public Map<String, Location> getLocations() {
        return locations;
    }
}
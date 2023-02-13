package com.biobrain.view.locations;

import com.biobrain.util.FileLoader;
import com.biobrain.view.panels.GamePanel;
import com.biobrain.view.tile.Tile;

import java.awt.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class LocationManager {
    private final Map<String, Room> locations;
    private final List<Room> rooms;
    public GamePanel gamePanel;


    public LocationManager(GamePanel gamePanel) {
        this.gamePanel = gamePanel;
        locations = new HashMap<>();
        rooms = new ArrayList<>();
        generateSectors();
    }

    public void generateSectors(){
        //TODO :currently same as sector tiles when we get to design we can adjust
        List<Tile> sectorGenericTiles = new ArrayList<>(){{
            add(new Tile(FileLoader.loadBuffered("maps/sectorGeneric/sector_0.png"), true));
            add(new Tile(FileLoader.loadBuffered("maps/sectorGeneric/sector_1.png"), true));
            add(new Tile(FileLoader.loadBuffered("maps/sectorGeneric/sector_2.png"), true));
            add(new Tile(FileLoader.loadBuffered("maps/sectorGeneric/sector_3.png"), true));
            add(new Tile(FileLoader.loadBuffered("maps/sectorGeneric/sector_4.png"), true));
            add(new Tile(FileLoader.loadBuffered("maps/sectorGeneric/sector_5.png"), false));
            add(new Tile(FileLoader.loadBuffered("maps/sectorGeneric/sector_6.png"), true));
            add(new Tile(FileLoader.loadBuffered("maps/sectorGeneric/sector_7.png"), true));
            add(new Tile(FileLoader.loadBuffered("maps/sectorGeneric/sector_8.png"), true));
            add(new Tile(FileLoader.loadBuffered("maps/sectorGeneric/sector_9.png"), true));
        }};

        List<Tile> labTiles = new ArrayList<>(){{
            add(new Tile(FileLoader.loadBuffered("maps/sectorGeneric/sector_0.png"), true));
            add(new Tile(FileLoader.loadBuffered("maps/sectorGeneric/sector_1.png"), true));
            add(new Tile(FileLoader.loadBuffered("maps/sectorGeneric/sector_2.png"), true));
            add(new Tile(FileLoader.loadBuffered("maps/sectorGeneric/sector_3.png"), true));
            add(new Tile(FileLoader.loadBuffered("maps/sectorGeneric/sector_4.png"), true));
            add(new Tile(FileLoader.loadBuffered("maps/sectorGeneric/sector_5.png"), false));
            add(new Tile(FileLoader.loadBuffered("maps/sectorGeneric/sector_6.png"), true));
            add(new Tile(FileLoader.loadBuffered("maps/sectorGeneric/sector_7.png"), true));
            add(new Tile(FileLoader.loadBuffered("maps/sectorGeneric/sector_8.png"), true));
            add(new Tile(FileLoader.loadBuffered("maps/sectorGeneric/sector_9.png"), true));
        }};

        Rectangle sectorExit = new Rectangle(576, 570, 96, 48);

       //ROOMS
        Room lab = new Room("lab", 0, "maps/lab/labMap.txt", labTiles, false, gamePanel, gamePanel.tileHelper, null,null);

        Rectangle sector1Entrance = new Rectangle(720, 672,96 , 48);
        Room sector1 = new Room("sector1", 1, "maps/sectorGeneric/sector.txt", sectorGenericTiles, true, gamePanel, gamePanel.tileHelper, sector1Entrance, sectorExit);

        Rectangle sector2Entrance = new Rectangle(1632, 672, 96, 48);
        Room sector2 = new Room("sector2", 2, "maps/sectorGeneric/sector.txt", sectorGenericTiles, true, gamePanel, gamePanel.tileHelper, sector2Entrance, sectorExit);

        Rectangle sector3Entrance = new Rectangle(2544, 672, 96, 48);
        Room sector3 = new Room("sector3", 3, "maps/sectorGeneric/sector.txt", sectorGenericTiles, true, gamePanel, gamePanel.tileHelper, sector3Entrance, sectorExit);

        Rectangle sector4Entrance = new Rectangle(720, 1344, 96, 48);
        Room sector4 = new Room("sector4", 4, "maps/sectorGeneric/sector.txt", sectorGenericTiles, true, gamePanel, gamePanel.tileHelper, sector4Entrance, sectorExit);

        Rectangle sector5Entrance = new Rectangle(1632, 1344, 96, 48);
        Room sector5 = new Room("sector5", 5, "maps/sectorGeneric/sector.txt", sectorGenericTiles, true, gamePanel, gamePanel.tileHelper, sector5Entrance, sectorExit);

        Rectangle sector6Entrance = new Rectangle(2544, 1344, 96, 48);
        Room sector6 = new Room("sector6", 6, "maps/sectorGeneric/sector.txt", sectorGenericTiles, true, gamePanel, gamePanel.tileHelper, sector6Entrance, sectorExit);

        locations.put(lab.getName(), lab);
        locations.put(sector1.getName(), sector1);
        locations.put(sector2.getName(), sector2);
        locations.put(sector3.getName(), sector3);
        locations.put(sector4.getName(), sector4);
        locations.put(sector5.getName(), sector5);
        locations.put(sector6.getName(), sector6);

        rooms.add(lab);
        rooms.add(sector1);
        rooms.add(sector2);
        rooms.add(sector3);
        rooms.add(sector4);
        rooms.add(sector5);
        rooms.add(sector6);
    }

    public List<Room> getRooms(){
        return rooms;
    }
    public Map<String, Room> getLocations() {
        return locations;
    }
}
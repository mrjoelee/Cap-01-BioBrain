package com.biobrain.view.tile;

import com.biobrain.util.FileLoader;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/*
* Class to set up the tiles for any room/ map
* */
public class TileSetter {
    Map<String, List<Tile>> roomTiles;

    public TileSetter() {
        roomTiles = new HashMap<>();
        loadRoomTiles();
    }
    private void loadRoomTiles(){
        List<Tile> genericTiles = new ArrayList<>(){{
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
            add(new Tile(FileLoader.loadBuffered("maps/lab/lab_0.png"), true));
            add(new Tile(FileLoader.loadBuffered("maps/lab/lab_1.png"), true));
            add(new Tile(FileLoader.loadBuffered("maps/lab/lab_2.png"), true));
            add(new Tile(FileLoader.loadBuffered("maps/lab/lab_3.png"), true));
            add(new Tile(FileLoader.loadBuffered("maps/lab/lab_4.png"), true));
            add(new Tile(FileLoader.loadBuffered("maps/lab/lab_5.png"), false));
            add(new Tile(FileLoader.loadBuffered("maps/lab/lab_6.png"), true));
            add(new Tile(FileLoader.loadBuffered("maps/lab/lab_7.png"), true));
            add(new Tile(FileLoader.loadBuffered("maps/lab/lab_8.png"), true));
            add(new Tile(FileLoader.loadBuffered("maps/lab/lab_9.png"), true));
            add(new Tile(FileLoader.loadBuffered("maps/lab/lab_10.png"), false));
            add(new Tile(FileLoader.loadBuffered("maps/lab/lab_11.png"), false));
            add(new Tile(FileLoader.loadBuffered("maps/lab/lab_12.png"), false));

        }};

        roomTiles.put("lab", labTiles);
        roomTiles.put("sector1", genericTiles);
        roomTiles.put("sector2", genericTiles);
        roomTiles.put("sector3", genericTiles);
        roomTiles.put("sector4", genericTiles);
        roomTiles.put("sector5", genericTiles);
        roomTiles.put("sector6", genericTiles);
    }

    public List<Tile> getRoomTiles(String roomShortName){
        return roomTiles.get(roomShortName);
    }

    public List<Tile> getRoomTiles(int roomCode){
        if(roomCode == 0){
            return  getRoomTiles("lab");
        }
        return getRoomTiles(String.format("sector%s", roomCode));
    }
}
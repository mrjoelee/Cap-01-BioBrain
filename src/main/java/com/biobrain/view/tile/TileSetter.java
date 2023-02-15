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
        //TODO : sector generic is a temporary style used for all sectors. As we start designing sectors we can change the tiles here.
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

        roomTiles.put("lab", genericTiles);
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
}
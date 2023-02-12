package com.biobrain.view.locations;

import com.biobrain.view.panels.GamePanel;
import com.biobrain.view.tile.Tile;
import com.biobrain.view.tile.TileHelper;

import java.awt.*;
import java.util.List;

public class Room {

    private String name;
    private final int roomCode;
    private final String map;
    private List<Tile> tiles;
    private final boolean isSector;
    private GamePanel gamePanel;
    private TileHelper tileHelper;
    private final Rectangle entrance;
    private final Rectangle exit;

    public Room(String name, int roomCode, String map,List<Tile> tiles, boolean isSector, GamePanel gamePanel, TileHelper tileHelper, Rectangle entrance, Rectangle exit) {
        this.name = name;
        this.roomCode = roomCode;
        this.map = map;
        this.tiles = tiles;
        this.isSector = isSector;
        this.gamePanel = gamePanel;
        this.tileHelper = tileHelper;
        this.entrance = entrance;
        this.exit = exit;
        tileHelper.loadMap(this);
    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getMap() {
        return map;
    }

    public int getRoomCode() {
        return roomCode;
    }

    public boolean isSector() {
        return isSector;
    }

    public List<Tile> getTiles() {
        return tiles;
    }

    public void setTiles(List<Tile> tiles) {
        this.tiles = tiles;
    }

    public Rectangle getEntrance(){
        return this.entrance;
    }

    public Rectangle getExit(){
        return this.exit;
    }

    public void draw(Graphics2D g2){
        if(entrance != null){
            g2.drawRect(entrance.x, entrance.y, entrance.width, entrance.height);
        }

        if(exit != null){
            g2.drawRect(exit.x, exit.y, exit.width, exit.height);
        }
    }
}
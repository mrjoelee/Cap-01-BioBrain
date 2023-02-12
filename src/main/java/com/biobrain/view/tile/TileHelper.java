package com.biobrain.view.tile;

import com.biobrain.view.panels.GamePanel;
import com.biobrain.view.locations.Room;

import java.awt.*;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;

public class TileHelper {
    GamePanel gp;
    public Tile[] tile;
    public int[][][] mapTileNum;

    public TileHelper(GamePanel gp) {
        this.gp = gp;
        tile = new Tile[10];
        mapTileNum = new int[gp.maxRooms][gp.maxLabCol][gp.maxLabRow];
    }

    public void loadMap(Room room){
        try {
            InputStream is = getClass().getClassLoader().getResourceAsStream(room.getMap());
            BufferedReader br = new BufferedReader(new InputStreamReader(is));

            int maxCol = room.isSector() ? gp.getMaxSectorCol() : gp.maxLabCol;
            int maxRow = room.isSector()? gp.getMaxSectorRow() : gp.maxLabRow;

            int col =0;
            int row = 0;

            while(col < maxCol  && row < maxRow){
                String line = br.readLine();

                while(col < maxCol){
                    String numbers[] = line.split(" ");
                    int num = Integer.parseInt(numbers[col]);

                    mapTileNum[room.getRoomCode()][col][row] = num;
                    col++;
                }

                if(col == maxCol){
                    col =0;
                    row++;
                }
            }
            br.close();
        }
        catch(Exception e) {
            e.printStackTrace();
        }
    }


    public void draw(Graphics2D g2){

        if(gp.currentRoom.isSector()){
            int col =0;
            int row =0;
            int x = 0;
            int y =0;

            while(col < gp.getMaxSectorCol() && row < gp.getMaxSectorRow()){
            int tileNum = mapTileNum[gp.currentRoom.getRoomCode()][col][row];
                g2.drawImage(gp.currentRoom.getTiles().get(tileNum).image, x, y, gp.getTileSize(), gp.getTileSize(), null);
                col++;
                x += gp.getTileSize();

                if(col == gp.getMaxSectorCol()){
                    col =0;
                    x =0;
                    row++;
                    y += gp.getTileSize();
                }
            }
        }
        else {
            int labCol = 0;
            int labRow = 0;

            int maxCol = gp.currentRoom.isSector() ? gp.getMaxSectorCol() : gp.maxLabCol;
            int maxRow = gp.currentRoom.isSector() ? gp.getMaxSectorRow() : gp.maxLabRow;

            while (labCol < maxCol && labRow < maxRow) {
                int tileNum = mapTileNum[gp.currentRoom.getRoomCode()][labCol][labRow];

                int labX = labCol * gp.getTileSize();
                int labY = labRow * gp.getTileSize();
                int screenX = labX - gp.player.labX + gp.player.screenX;
                int screenY = labY - gp.player.labY + gp.player.screenY;

                if (labX + gp.getTileSize() > gp.player.labX - gp.player.screenX &&
                        labX - gp.getTileSize() < gp.player.labX + gp.player.screenX &&
                        labY + gp.getTileSize() > gp.player.labY - gp.player.screenY &&
                        labY - gp.getTileSize() < gp.player.labY + gp.player.screenY) {

                    g2.drawImage(gp.currentRoom.getTiles().get(tileNum).image, screenX, screenY, gp.getTileSize(), gp.getTileSize(), null);
                }

                labCol++;

                if (labCol == maxCol) {
                    labCol = 0;
                    labRow++;
                }
            }
        }
    }
}
package com.biobrain.view.tile;

import com.biobrain.view.panels.GamePanel;

import java.awt.*;
import java.awt.image.BufferedImage;

public class Map extends TileHelper {
    GamePanel gamePanel;
    BufferedImage labMap[];
    public boolean miniMapOn = false;
    public Map(GamePanel gp) {
        super(gp);
        this.gamePanel = gp;
    }

    public void createLabMap(){
        labMap = new BufferedImage[gamePanel.maxRooms];
        int labMapWidth = gamePanel.labWidth;
        int labMapHegiht = gamePanel.labHeight;

        for(int i =0; i < gamePanel.maxRooms; i++){
            labMap[i] = new BufferedImage(labMapWidth, labMapHegiht, BufferedImage.TYPE_INT_ARGB);
            Graphics2D g2 = (Graphics2D)labMap[i].createGraphics();

            int col =0;
            int row =0;

            while(col < gamePanel.maxLabCol && row < gamePanel.maxLabRow){
                int tileNum = mapTileNum[i][col][row];
                int x = gamePanel.getTileSize() * col;
                int y = gamePanel.getTileSize() * row;
                g2.drawImage(tile[tileNum].image, x, y, null);

                col++;

                if(col == gamePanel.maxLabCol){
                    col =0;
                    row++;
                }
            }
        }
    }

    public void drawFullMapScreen(Graphics2D g2){
        g2.setColor(Color.black);
        g2.fillRect(0,0, gamePanel.screenWidth, gamePanel.screenHeight);

        int width = 500;
        int height = 500;
        int x = gamePanel.screenWidth/2 - width/2;
        int y = gamePanel.screenHeight/2 - height/2;
        g2.drawImage(labMap[gp.currentRoom.getRoomCode()], x, y, width, height, null);

    }
}
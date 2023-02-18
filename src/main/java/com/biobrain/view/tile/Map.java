package com.biobrain.view.tile;

import com.biobrain.items.ItemManager;
import com.biobrain.model.Location;
import com.biobrain.objects.ObjectManager;
import com.biobrain.view.entities.ItemEntity;
import com.biobrain.view.entities.SuperObject;
import com.biobrain.view.panels.GamePanel;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.List;

public class Map {
    GamePanel gamePanel;
    BufferedImage[] roomMaps;
    //game extras
    public boolean miniMapOn = false;
    public Map(GamePanel gp) {
        this.gamePanel = gp;
        createLabMap();
    }

    public void createLabMap() {
        roomMaps = new BufferedImage[gamePanel.maxRooms];
        List<Location> locations = gamePanel.locations.getRooms();

        for (int i = 0; i < locations.size(); i++) {

            Location curr = locations.get(i);
            List<Tile> roomTiles = gamePanel.tileSetter.getRoomTiles(curr.getShortName());
            //items
//            List<ItemEntity> itemList = ItemManager.getItemsByRoomCode(curr.getRoomCode());
//            List<SuperObject> objectList = ObjectManager.getObjectsByRoomCode(curr.getRoomCode());
            int width = curr.isSector() ?
                    gamePanel.getMaxSectorCol() * gamePanel.getTileSize() :
                    gamePanel.maxLabCol * gamePanel.getTileSize();

            int height = curr.isSector() ?
                    gamePanel.getMaxSectorRow() * gamePanel.getTileSize() :
                    gamePanel.maxLabRow * gamePanel.getTileSize();

            int maxCol = curr.isSector() ? gamePanel.getMaxSectorCol() : gamePanel.maxLabCol;
            int maxRow = curr.isSector() ? gamePanel.getMaxSectorRow() : gamePanel.maxLabRow;

            roomMaps[i] = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB); //creating image based on item
            Graphics2D graphics2D = (Graphics2D) roomMaps[i].createGraphics();


            int col = 0;
            int row = 0;

            while (col < maxCol && row < maxRow) {
                int tileNum = TileHelper.mapTileNum[curr.getRoomCode()][col][row];
                int x = gamePanel.getTileSize() * col;
                int y = gamePanel.getTileSize() * row;
                graphics2D.drawImage(roomTiles.get(tileNum).image, x, y, gamePanel.getTileSize(), gamePanel.getTileSize(), null);

                //adding items
//                if(gamePanel.currentRoom.getRoomCode() != 0 && curr.getRoomCode() != 0){
//                    for (ItemEntity item : itemList) {
//                            graphics2D.drawImage(item.getItemImage(), item.getX(), item.getY(), gamePanel.getTileSize(),
//                                    gamePanel.getTileSize(), null);
//                    }
//                }

//                //adding objects
//                if(gamePanel.currentRoom.getRoomCode() != 0 && curr.getRoomCode() != 0){
//                    for (SuperObject obj : objectList) {
//                        graphics2D.drawImage(obj.getObjectImage(), obj.getX()*2, (obj.getY()*2), gamePanel.getTileSize()*2,
//                                gamePanel.getTileSize()*2, null);
//                    }
//                }

                col++;

                if (col == maxCol) {
                    col = 0;
                    row++;
                }
            }
        }
    }

    public void setCurrentMapDisplayed(Graphics2D g2, int roomCode) {
        drawFullMapScreen(g2, roomCode);
    }

    private void drawFullMapScreen(Graphics2D g2, int roomCode) {
//        g2.setColor(Color.black);
        g2.fillRect(0, 0, gamePanel.screenWidth, gamePanel.screenHeight);
        Location roomDisplayed = gamePanel.locations.getRooms()
                .stream().filter(x -> x.getRoomCode() == roomCode)
                .findFirst().orElse(null);

        if (roomDisplayed != null) {

            int width = 400;
            int height = 300;
            int mapX = (gamePanel.screenWidth / 2) - (width / 2);
            int mapY = (gamePanel.screenHeight / 2) - (height / 2);

            g2.drawImage(roomMaps[roomCode], mapX, mapY, width, height, null);

            //only display user on map if they are in the room or if they are looking at the lab map
            if (roomDisplayed.getRoomCode() == gamePanel.currentRoom.getRoomCode() || roomCode == 0) {
                double maxCol = roomDisplayed.isSector() ? gamePanel.getMaxSectorCol() : gamePanel.maxLabCol;
                double maxRow = roomDisplayed.isSector() ? gamePanel.getMaxSectorRow() : gamePanel.maxLabRow;

                double scaleCol = (double) (maxCol * gamePanel.getTileSize()) / width;
                double scaleRow = (maxRow * gamePanel.getTileSize()) / height;
                int xPosition = (int) (mapX + gamePanel.player.labX / scaleCol);
                int yPosition = (int) (mapY + gamePanel.player.labY / scaleRow);
                int playerSize = (int) (gamePanel.getTileSize() / scaleCol);

                if (roomDisplayed.isSector() || gamePanel.currentRoom.getRoomCode() == 0) {
                    g2.drawImage(gamePanel.player.getDirectionImage(), xPosition, yPosition, playerSize, playerSize, null);
                }
            }
            g2.setFont(g2.getFont().deriveFont(32f));
            g2.setColor(Color.white);
            String message = String.format("%s", roomDisplayed.getName());
            int textWidth = g2.getFontMetrics().stringWidth(message);
            int x = (gamePanel.screenWidth / 2) - (textWidth / 2);
            g2.drawString(message, x, 550);
        }
    }
}
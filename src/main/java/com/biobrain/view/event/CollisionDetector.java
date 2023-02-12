package com.biobrain.view.event;

import com.biobrain.view.panels.GamePanel;
import com.biobrain.view.locations.Room;
import com.biobrain.view.entities.Entity;

import java.util.List;

public class CollisionDetector {
    GamePanel gamePanel;

    public CollisionDetector(GamePanel gamePanel) {
        this.gamePanel = gamePanel;
    }

    public void checkTile(Entity entity) {
        int entityLeft = entity.labX + entity.collider.x;
        int entityRight = entity.labX + entity.collider.x + entity.collider.width;
        int entityTop = entity.labY + entity.collider.y;
        int entityBottom = entity.labY + entity.collider.y + entity.collider.height;

        int leftCol = entityLeft / gamePanel.getTileSize();
        int rightCol = entityRight / gamePanel.getTileSize();
        int topRow = entityTop / gamePanel.getTileSize();
        int bottomRow = entityBottom / gamePanel.getTileSize();

        int tileNum1, tileNum2;

        switch (entity.direction) {
            case "up":
                topRow = (entityTop - entity.speed) / gamePanel.getTileSize();
                tileNum1 = gamePanel.tileHelper.mapTileNum[gamePanel.currentRoom.getRoomCode()][leftCol][topRow];
                tileNum2 = gamePanel.tileHelper.mapTileNum[gamePanel.currentRoom.getRoomCode()][rightCol][topRow];

                if (gamePanel.currentRoom.getTiles().get(tileNum1).collision || gamePanel.currentRoom.getTiles().get(tileNum2).collision) {
                    entity.collisionOn = true;
                }
                break;

            case "down":
                bottomRow = (entityBottom + entity.speed) / gamePanel.getTileSize();
                tileNum1 = gamePanel.tileHelper.mapTileNum[gamePanel.currentRoom.getRoomCode()][leftCol][bottomRow];
                tileNum2 = gamePanel.tileHelper.mapTileNum[gamePanel.currentRoom.getRoomCode()][rightCol][bottomRow];

                if (gamePanel.currentRoom.getTiles().get(tileNum1).collision || gamePanel.currentRoom.getTiles().get(tileNum2).collision) {
                    entity.collisionOn = true;
                }
                break;

            case "left":
                leftCol = (entityLeft - entity.speed) / gamePanel.getTileSize();
                tileNum1 = gamePanel.tileHelper.mapTileNum[gamePanel.currentRoom.getRoomCode()][leftCol][topRow];
                tileNum2 = gamePanel.tileHelper.mapTileNum[gamePanel.currentRoom.getRoomCode()][leftCol][bottomRow];

                if (gamePanel.currentRoom.getTiles().get(tileNum1).collision || gamePanel.currentRoom.getTiles().get(tileNum2).collision) {
                    entity.collisionOn = true;
                }
                break;

            case "right":
                rightCol = (entityRight + entity.speed) / gamePanel.getTileSize();
                tileNum1 = gamePanel.tileHelper.mapTileNum[gamePanel.currentRoom.getRoomCode()][rightCol][topRow];
                tileNum2 = gamePanel.tileHelper.mapTileNum[gamePanel.currentRoom.getRoomCode()][rightCol][bottomRow];

                if (gamePanel.currentRoom.getTiles().get(tileNum1).collision || gamePanel.currentRoom.getTiles().get(tileNum2).collision) {
                    entity.collisionOn = true;
                }
                break;
        }

    }

    public void checkExit(Entity entity) {
        entity.collider.x = entity.labX + entity.collider.x;
        entity.collider.y = entity.labY + entity.collider.y;
        Room previous = gamePanel.currentRoom;

        switch (entity.direction) {
            case "up":
            case "down":
            case "left":
            case "right":
                entity.collider.x += entity.speed;
                if(gamePanel.currentRoom.getExit() != null) {
                    if (entity.collider.intersects(gamePanel.currentRoom.getExit())) {
                    gamePanel.currentRoom = gamePanel.locations.getLocations().get("lab");
                    gamePanel.player.labX = previous.getEntrance().x + 24; // get the entrance of the room add 24 so player exits in middle
                    gamePanel.player.labY = previous.getEntrance().y + gamePanel.getTileSize();
                    }
                }
        }
        entity.collider.x = entity.colliderDefaultX;
        entity.collider.y = entity.colliderDefaultY;
    }

    public void checkEntrance(Entity entity) {
        entity.collider.x = entity.labX + entity.collider.x;
        entity.collider.y = entity.labY + entity.collider.y;
        List<Room> rooms = gamePanel.locations.getRooms();

        switch (entity.direction) {
            case "up":
            case "down":
            case "left":
            case "right":
                entity.collider.x += entity.speed;
                for (Room room : rooms) {
                    if (room.getEntrance() != null) {
                        if (entity.collider.intersects(room.getEntrance())) {

                            System.out.println(room.getEntrance());
                            gamePanel.currentRoom = room;
                            gamePanel.player.labX = room.getExit().x + 24;
                            gamePanel.player.labY = room.getExit().y - gamePanel.getTileSize();
                        }
                    }
                }
        }
        entity.collider.x = entity.colliderDefaultX;
        entity.collider.y = entity.colliderDefaultY;
    }
}
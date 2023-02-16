package com.biobrain.view.event;

import com.biobrain.model.Location;
import com.biobrain.objects.ObjectManager;
import com.biobrain.view.entities.Player;
import com.biobrain.view.entities.SuperObject;
import com.biobrain.view.panels.GamePanel;
import com.biobrain.view.entities.Entity;
import com.biobrain.view.tile.Tile;
import com.biobrain.view.tile.TileHelper;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;


public class CollisionDetector {
    GamePanel gamePanel;

    public CollisionDetector(GamePanel gamePanel) {
        this.gamePanel = gamePanel;
    }

    public void checkObject(Entity entity) {
        List<SuperObject> objects = gamePanel.object.getObjects().stream().filter(x -> x.getRoomCode() == gamePanel.currentRoom.getRoomCode()).collect(Collectors.toList());
        for (SuperObject obj : objects) {
            entity.collider.x = entity.labX + entity.collider.x;
            entity.collider.y = entity.labY + entity.collider.y;

            switch (entity.direction) {
                case "up":
                    entity.collider.y -= entity.speed;
                    objectCollisionAndDialogue(entity, obj);
                    break;
                case "down":
                    entity.collider.y += entity.speed;
                    objectCollisionAndDialogue(entity, obj);
                    break;

                case "left":
                    entity.collider.x -= entity.speed;
                    objectCollisionAndDialogue(entity, obj);
                    break;
                case "right":
                    entity.collider.x += entity.speed;
                    objectCollisionAndDialogue(entity, obj);
                    break;
            }
            entity.collider.x = entity.colliderDefaultX;
            entity.collider.y = entity.colliderDefaultY;
        }
    }

    private void objectCollisionAndDialogue(Entity entity, SuperObject obj) {
        if (entity.collider.intersects(obj.getObjectCollider())) {
            if (obj.collision) {
                entity.collisionOn = true;
                if(!obj.getDescription().isEmpty()){
                    gamePanel.ui.setCurrentDialogue(obj.getDescription());
                    gamePanel.gameState = gamePanel.dialogueState;
                }
            }
        }
    }

    public void checkTile(Entity entity) {
        int entityLeft = entity.labX + entity.collider.x;
        int entityRight = entity.labX + entity.collider.x + entity.collider.width;
        int entityTop = entity.labY + entity.collider.y;
        int entityBottom = entity.labY + entity.collider.y + entity.collider.height;
        List<Tile> roomTiles = gamePanel.tileSetter.getRoomTiles(gamePanel.currentRoom.getShortName());

        int leftCol = entityLeft / gamePanel.getTileSize();
        int rightCol = entityRight / gamePanel.getTileSize();
        int topRow = entityTop / gamePanel.getTileSize();
        int bottomRow = entityBottom / gamePanel.getTileSize();

        int tileNum1, tileNum2;

        switch (entity.direction) {

            case "up":
                topRow = (entityTop - entity.speed) / gamePanel.getTileSize();
                tileNum1 = TileHelper.mapTileNum[gamePanel.currentRoom.getRoomCode()][leftCol][topRow];
                tileNum2 = TileHelper.mapTileNum[gamePanel.currentRoom.getRoomCode()][rightCol][topRow];

                if (roomTiles.get(tileNum1).collision || roomTiles.get(tileNum2).collision) {
                    entity.collisionOn = true;
                }
                break;

            case "down":
                bottomRow = (entityBottom + entity.speed) / gamePanel.getTileSize();
                tileNum1 = TileHelper.mapTileNum[gamePanel.currentRoom.getRoomCode()][leftCol][bottomRow];
                tileNum2 = TileHelper.mapTileNum[gamePanel.currentRoom.getRoomCode()][rightCol][bottomRow];

                if (roomTiles.get(tileNum1).collision || roomTiles.get(tileNum2).collision) {
                    entity.collisionOn = true;
                }
                break;

            case "left":
                leftCol = (entityLeft - entity.speed) / gamePanel.getTileSize();
                tileNum1 = TileHelper.mapTileNum[gamePanel.currentRoom.getRoomCode()][leftCol][topRow];
                tileNum2 = TileHelper.mapTileNum[gamePanel.currentRoom.getRoomCode()][leftCol][bottomRow];

                if (roomTiles.get(tileNum1).collision || roomTiles.get(tileNum2).collision) {
                    entity.collisionOn = true;
                }
                break;

            case "right":
                rightCol = (entityRight + entity.speed) / gamePanel.getTileSize();
                tileNum1 = TileHelper.mapTileNum[gamePanel.currentRoom.getRoomCode()][rightCol][topRow];
                tileNum2 = TileHelper.mapTileNum[gamePanel.currentRoom.getRoomCode()][rightCol][bottomRow];

                if (roomTiles.get(tileNum1).collision || roomTiles.get(tileNum2).collision) {
                    entity.collisionOn = true;
                }
                break;
        }
    }

    public void checkExit(Entity entity) {
        entity.collider.x = entity.labX + entity.collider.x;
        entity.collider.y = entity.labY + entity.collider.y;
        Location previous = gamePanel.currentRoom;

        switch (entity.direction) {
            case "up":
            case "down":
            case "left":
            case "right":
                entity.collider.x += entity.speed;
                if (gamePanel.currentRoom.getExit() != null) {
                    if (entity.collider.intersects(gamePanel.currentRoom.getExit())) {
                        Location lab = gamePanel.locations.getLocations().get("lab");
                        gamePanel.currentRoom = lab;
                        gamePanel.player.labX = previous.getEntrance().x + 24; // get the entrance of the room add 24 so player exits in middle
                        gamePanel.player.labY = previous.getEntrance().y + gamePanel.getTileSize();
                        gamePanel.ui.setCurrentDialogue(lab.getDescription());
                        gamePanel.gameState = gamePanel.dialoguePlay;
                    }
                }
        }
        entity.collider.x = entity.colliderDefaultX;
        entity.collider.y = entity.colliderDefaultY;
    }

    public void checkEntrance(Entity entity) {
        entity.collider.x = entity.labX + entity.collider.x;
        entity.collider.y = entity.labY + entity.collider.y;
        List<Location> locations = gamePanel.locations.getRooms();

        if (entity.direction.equals("up")) {
            entity.collider.x += entity.speed;
            for (Location room : locations) {
                if (room.getEntrance() != null) {
                    if (entity.collider.intersects(room.getEntrance())) {
                        if (!room.isLocked()) {
                            gamePanel.currentRoom = room;
                            gamePanel.player.labX = room.getExit().x + 24;
                            gamePanel.player.labY = room.getExit().y - gamePanel.getTileSize();
                            gamePanel.ui.setCurrentDialogue(room.getDescription());
                            gamePanel.gameState = gamePanel.dialoguePlay;
                            break;
                        } else {
                            gamePanel.ui.setCurrentDialogue(room.getLockedMessage());
                            gamePanel.gameState = gamePanel.dialogueState;
                        }
                    }
                }
            }
        }
        entity.collider.x = entity.colliderDefaultX;
        entity.collider.y = entity.colliderDefaultY;
    }
}
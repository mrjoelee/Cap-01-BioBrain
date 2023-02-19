package com.biobrain.view.event;

/*
 * CollisionDetector | Class
 * defines all collision interactions between the player
 * creates dialogue description boxes for collided with objects
 * checks for player entering/exiting rooms
 */

import com.biobrain.model.Location;
import com.biobrain.view.entities.ItemEntity;
import com.biobrain.view.entities.Player;
import com.biobrain.view.entities.NPC.Npc;
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

    // CTOR
    public CollisionDetector(GamePanel gamePanel) {
        this.gamePanel = gamePanel;
    }


    // CLASS METHODS

    // check if game environment object is colliding with player
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

    // creates a dialogue box describing a collided object
    private void objectCollisionAndDialogue(Entity entity, SuperObject obj) {
        if (entity.collider.intersects(obj.getObjectCollider())) {
            if (obj.collision) {
                entity.collisionOn = true;
                if(!(entity instanceof Npc)) {
                    if (!obj.getDescription().isEmpty()) {
                        gamePanel.ui.setCurrentDialogue(obj.getDescription());
                        gamePanel.gameState = gamePanel.dialogueState;
                    }
                }
            }
        }
    }

    // check if an item that may be picked up is colliding with player
    public String checkGrabItem(Player player) {
        String itemName = "none";
        Map<String, ItemEntity> items = gamePanel.getItemManager().getItems();
        for (ItemEntity item : items.values()) {
            if (item != null) {
                if (item.getRoomCode() == gamePanel.currentRoom.getRoomCode()) {
                    player.collider.x = player.labX + player.collider.x;
                    player.collider.y = player.labY + player.collider.y;

                    switch (player.direction) {
                        case "up":
                            player.collider.y -= player.speed;
                            if (player.collider.intersects(item.getItemCollider())) {
                                itemName = item.getName();
                                isBioBrainLocked(itemName, player);
                            }
                            break;
                        case "down":
                            player.collider.y += player.speed;
                            if (player.collider.intersects(item.getItemCollider())) {
                                itemName = item.getName();
                                isBioBrainLocked(itemName, player);
                            }
                            break;

                        case "left":
                            player.collider.x -= player.speed;
                            if (player.collider.intersects(item.getItemCollider())) {
                                itemName = item.getName();
                                isBioBrainLocked(itemName, player);
                            }
                            break;
                        case "right":
                            player.collider.x += player.speed;
                            if (player.collider.intersects(item.getItemCollider())) {
                                itemName = item.getName();
                                isBioBrainLocked(itemName, player);
                            }
                            break;

                    }
                }
            }
            player.collider.x = player.colliderDefaultX;
            player.collider.y = player.colliderDefaultY;
        }
        return itemName;
    }

    // check if BioBrain can be downloaded yet
    private void isBioBrainLocked(String itemName, Player player){
        if (itemName.equals("biobrain") && !gamePanel.getPlayer().getInventory().containsKey("sphere") && gamePanel.isLaser){
            player.collisionOn = true;
            gamePanel.ui.setCurrentDialogue("The LASER SHIELD defenses must be deactivated before I can download the BIOBRAIN!");
            gamePanel.gameState = gamePanel.dialogueState;
        }
        if (gamePanel.getPlayer().getInventory().containsKey("sphere") && !gamePanel.isLaser){
            System.out.println("got sphere");
            gamePanel.ui.setCurrentDialogue("Sector 6 is unlocked!! Use the Sphere to download the BIOBRAIN on it!!");
            gamePanel.gameState = gamePanel.dialogueState;

        } else if (itemName.equalsIgnoreCase("biobrain") && !gamePanel.getPlayer().getInventory().containsKey("sphere") && !gamePanel.isLaser){
            player.collisionOn = true;
            gamePanel.ui.setCurrentDialogue("You must get the sphere first to get the BIOBRAIN");
            gamePanel.gameState = gamePanel.dialogueState;
        } else if (itemName.equalsIgnoreCase("biobrain") && gamePanel.getPlayer().getInventory().containsKey("sphere") && gamePanel.isLaser){
            player.collisionOn = true;
            gamePanel.ui.setCurrentDialogue("The LASER SHIELD defenses must be deactivated before I can download the BIOBRAIN!");
            gamePanel.gameState = gamePanel.dialogueState;
        }
    }

    // check if tile object is colliding with player
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

    // check if exit object is colliding with player
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
                        gamePanel.getBioBrainApp().setCurrentLocation(lab);
                        gamePanel.gameState = gamePanel.dialoguePlay;
                    }
                }
        }
        entity.collider.x = entity.colliderDefaultX;
        entity.collider.y = entity.colliderDefaultY;
    }

    // check if entrance object is colliding with player
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
                            gamePanel.getBioBrainApp().setCurrentLocation(room);
                            gamePanel.gameState = gamePanel.dialoguePlay;
                            break;
                        } else if (room.isLocked() && gamePanel.getPlayer().getInventory().containsKey("biobrain")){
                            gamePanel.ui.setCurrentDialogue("With the BioBrain in hand, the door unlocks!");
                            gamePanel.currentRoom = room;
                            gamePanel.player.labX = room.getExit().x + 24;
                            gamePanel.player.labY = room.getExit().y - gamePanel.getTileSize();
                            gamePanel.getBioBrainApp().setCurrentLocation(room);
                            gamePanel.gameState = gamePanel.dialoguePlay;
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

    //check npc collision
    public void checkNPCCollision(Entity entity, List<Npc> target) {

        for (Npc npc : target) {
            if (npc != null) {
                entity.collider.x = entity.labX + entity.collider.x;
                entity.collider.y = entity.labY + entity.collider.y;

                npc.collider.x = npc.labX + npc.collider.x;
                npc.collider.y = npc.labY + npc.collider.y;

                switch (entity.direction) {
                    case "up":
                        entity.collider.y -= entity.speed;
                        if (entity.collider.intersects(npc.collider)) {
                            entity.collisionOn = true;
                        }
                        break;

                    case "down":
                        entity.collider.y += entity.speed;
                        if (entity.collider.intersects(npc.collider)) {
                            entity.collisionOn = true;
                        }

                        break;

                    case "left":
                        entity.collider.x -= entity.speed;
                        if (entity.collider.intersects(npc.collider)) {
                            entity.collisionOn = true;
                        }

                        break;

                    case "right":
                        entity.collider.x += entity.speed;
                        if (entity.collider.intersects(npc.collider)) {
                            entity.collisionOn = true;
                        }
                        break;
                }
                entity.collider.x = entity.colliderDefaultX;
                entity.collider.y = entity.colliderDefaultY;
                npc.collider.x = npc.colliderDefaultX;
                npc.collider.y = npc.colliderDefaultY;
            }
        }
    }
}
package com.biobrain.view.event;

import com.biobrain.view.panels.GamePanel;

public class EventHandler {
    private final GamePanel gamePanel;
    private final EventRectangle[][][] rectangle;
    private int previousEventX, previousEventY;
    private boolean canTouchEvent = true;
    private int tempMap, tempCol, tempRow;


    public EventHandler(GamePanel gamePanel) {
        this.gamePanel = gamePanel;

        rectangle = new EventRectangle[gamePanel.maxRooms][gamePanel.maxLabCol][gamePanel.maxLabRow];
        int map = 0;
        int col = 0;
        int row = 0;

        while (map < gamePanel.maxRooms && col < gamePanel.maxLabCol && row < gamePanel.maxLabRow) {
            rectangle[map][col][row] = new EventRectangle();
            rectangle[map][col][row].x = 23;
            rectangle[map][col][row].y = 23;
            rectangle[map][col][row].width = 24;
            rectangle[map][col][row].height = 24;
            rectangle[map][col][row].eventRectDefaultX = rectangle[map][col][row].x;
            rectangle[map][col][row].eventRectDefaultY = rectangle[map][col][row].y;

            col++;
            if (col == gamePanel.maxLabCol) {
                col = 0;
                row++;

                if (row == gamePanel.maxLabRow) {
                    row = 0;
                    map++;
                }
            }
        }
    }

    public void checkEvent(){
        int xDistance = Math.abs(gamePanel.player.labX - previousEventX);
        int yDistance = Math.abs(gamePanel.player.labY - previousEventY);
        int distance = Math.max(xDistance, yDistance);

        if(distance > gamePanel.getTileSize()){
            canTouchEvent = true;
        }
        if(canTouchEvent) {
           if(hit(1, 16, 12, "any")){
               teleport(1,13,11);
           }
           else if(hit(0, 15, 12,"up")){
               teleport(1, 12, 11);
           }
            else if (hit(1, 12, 11, "down")) {
                teleport(0, 15, 12);
            } else if (hit(1, 13, 11, "down")) {
                teleport(0, 16, 12);
            }
        }
    }

    //checks event collision
    public boolean hit(int map, int eventCol, int eventRow, String reqDirection){
        boolean hit = false;

        if(map == gamePanel.currentRoom.getRoomCode()) {

            gamePanel.player.collider.x = gamePanel.player.labX + gamePanel.player.collider.x;
            gamePanel.player.collider.y = gamePanel.player.labY + gamePanel.player.collider.y;
            rectangle[map][eventCol][eventRow].x = eventCol * gamePanel.getTileSize() + rectangle[map][eventCol][eventRow].x;
            rectangle[map][eventCol][eventRow].y = eventRow * gamePanel.getTileSize() + rectangle[map][eventCol][eventRow].y;

            if (gamePanel.player.collider.intersects(rectangle[map][eventCol][eventRow])) {
                if (gamePanel.player.direction.contentEquals(reqDirection) || reqDirection.contentEquals("any")) {
                    hit = true;

                    previousEventX = gamePanel.player.labX;
                    previousEventY = gamePanel.player.labY;
                }
            }
            gamePanel.player.collider.x = gamePanel.player.colliderDefaultX;
            gamePanel.player.collider.y = gamePanel.player.colliderDefaultY;
            rectangle[map][eventCol][eventRow].x = rectangle[map][eventCol][eventRow].eventRectDefaultX;
            rectangle[map][eventCol][eventRow].y = rectangle[map][eventCol][eventRow].eventRectDefaultY;
        }
        return hit;
    }

    public void teleport(int map, int col, int row){
        //gamePanel.gameState = gameState;
        System.out.println("HELLOOOOOO");
        gamePanel.currentRoom = gamePanel.locations.getRooms().stream()
                .filter(x -> x.getRoomCode() == map)
                .findFirst()
                .orElse(gamePanel.locations.getLocations().get("lab"));

        gamePanel.player.labX = gamePanel.getTileSize() * col;
        gamePanel.player.labY = gamePanel.getTileSize() * row;
        previousEventX = gamePanel.player.labX;
        previousEventY = gamePanel.player.labY;

        canTouchEvent = false;
    }
}
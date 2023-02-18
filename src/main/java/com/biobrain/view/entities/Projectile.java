package com.biobrain.view.entities;

import com.biobrain.model.Item;
import com.biobrain.util.FileLoader;
import com.biobrain.view.panels.GamePanel;

import java.awt.*;
import java.awt.image.BufferedImage;

public class Projectile{
    GamePanel gamePanel;
    Graphics2D g2;
    Player user;
    String playerDirection;
    Item weapon;
    int speed;
    int x;
    int y;
    boolean alive;
    int life;
    BufferedImage up, down, left, right;
    BufferedImage directionImage;
    int projectileLength;
    int  projectileWidth;

    public Projectile(GamePanel gamePanel, Item weapon, String direction, Player user) {
        this.user = user;
        this.gamePanel = gamePanel;
        this.weapon = weapon;
        figureOutXY(direction);
        this.playerDirection = direction;
        this.alive = true;
        this.life = 80;
        this.speed = 6;
        getImage();
    }

    public void figureOutXY(String direction){
        if(gamePanel.currentRoom.isSector()){
            this.x = user.labX;
            this.y = user.labY;
        }
        else{
            this.x = user.screenX;
            this.y = user.screenY;
        }

        if(direction.equalsIgnoreCase("down")){
            this.x = x + (gamePanel.getTileSize() - 10);
            this.y = y + 20;
        }
        if(direction.equalsIgnoreCase("left") ){
            this.y = y + (gamePanel.getTileSize()/2);
        }
        if(direction.equalsIgnoreCase("right") ){
            this.x = x + (gamePanel.getTileSize()/2);
            this.y = y + (gamePanel.getTileSize()/2);
        }
    }

    public void getImage(){
        String name = weapon == null ? "" : weapon.getName();
        switch(name) {
            case "blaster":
                this.projectileLength = gamePanel.getTileSize()/4;
                this.projectileWidth = gamePanel.getTileSize()/4;
                up = FileLoader.loadBuffered("images/projectiles/blaster/blaster_up.png");
                down = FileLoader.loadBuffered("images/projectiles/blaster/blaster_down.png");
                left = FileLoader.loadBuffered("images/projectiles/blaster/blaster_left.png");
                right =FileLoader.loadBuffered("images/projectiles/blaster/blaster_right.png");
                break;
            case "harpoon":
                if (playerDirection.equalsIgnoreCase("left") || playerDirection.equalsIgnoreCase("right")) {
                    this.projectileWidth = gamePanel.getTileSize();
                    this.projectileLength = gamePanel.getTileSize() / 8;
                } else{
                    this.projectileWidth = gamePanel.getTileSize() / 8;
                    this.projectileLength = gamePanel.getTileSize();
                }
                up = FileLoader.loadBuffered("images/projectiles/harpoon/harpoon_up.png");
                down = FileLoader.loadBuffered("images/projectiles/harpoon/harpoon_down.png");
                left = FileLoader.loadBuffered("images/projectiles/harpoon/harpoon_left.png");
                right =FileLoader.loadBuffered("images/projectiles/harpoon/harpoon_right.png");
                break;
        }
    }

    public void update() {
        switch (playerDirection) {
            case "up":
                this.directionImage = up;
                y -= speed;
                break;
            case "down":
                this.directionImage = down;
                y += speed;
                break;
            case "left":
                this.directionImage = left;
                x -= speed;
                break;
            case "right":
                this.directionImage = right;
                x += speed;
                break;
        }
        life--;

        if (life <= 0) {
            setAlive(false);
        }
    }

    public void draw(Graphics2D g2){
        if(alive) {
            g2.drawImage(directionImage, x, y, projectileWidth, projectileLength, null);
        }
    }

    public boolean isAlive() {
        return alive;
    }

    public void setAlive(boolean alive) {
        this.alive = alive;
    }
}
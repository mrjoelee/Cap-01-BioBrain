package com.biobrain.view.entities;

import com.biobrain.model.Item;
import com.biobrain.util.FileLoader;
import com.biobrain.view.entities.NPC.Npc;
import com.biobrain.view.panels.GamePanel;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;

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
    Rectangle collider;
    int colliderDefaultX;
    int colliderDefaultY;

    public Projectile(GamePanel gamePanel, Item weapon, String direction, Player user) {
        this.user = user;
        this.gamePanel = gamePanel;
        this.weapon = weapon;
        x = user.labX;
        y = user.labY;
        //figureOutXY(direction);
        this.playerDirection = direction;
        this.alive = true;
        this.life = 80;
        this.speed = 6;
        getImage();
        collider = new Rectangle(projectileWidth *2, projectileLength *2, projectileWidth, projectileLength);
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

        List<Npc> npcsHit = new ArrayList<>();

        if(gamePanel.aiRobots.size() > 0 && isAlive()) {
            npcsHit = gamePanel.collisionDetector.checkPlayerProjectileCollision(this, gamePanel.aiRobots);
        }

        if(npcsHit.size() > 0){
            for (Npc target : npcsHit) {
                target.setUpHit();
            }
        }

        life--;

        if (life <= 0) {
            setAlive(false);
        }
    }

    public void draw(Graphics2D g2){
        boolean isSector = gamePanel.locations.getRoomByCode(user.currentLocation).isSector();

        int screenX = x - gamePanel.player.labX + gamePanel.player.screenX;
        int screenY = y - gamePanel.player.labY + gamePanel.player.screenY;

        int drawX = isSector ? x : screenX;
        int drawY = isSector ? y : screenY;

        if(playerDirection.equalsIgnoreCase("down")){
            drawX = drawX + (gamePanel.getTileSize() - 10);
            drawY = drawY + 20;
        }
        if(playerDirection.equalsIgnoreCase("left") ){
            drawY = drawY + (gamePanel.getTileSize()/2);
        }
        if(playerDirection.equalsIgnoreCase("right") ){
            drawX = drawX + (gamePanel.getTileSize()/2);
            drawY = drawY + (gamePanel.getTileSize()/2);
        }

        if(isAlive()) {
            g2.drawImage(directionImage, drawX, drawY, projectileWidth, projectileLength, null);
        }
    }

    public int getX(){
        return this.x;
    }

    public int getY(){
        return this.y;
    }

    public String getPlayerDirection(){
        return this.playerDirection;
    }

    public int getColliderDefaultX() {
        return colliderDefaultX;
    }

    public int getColliderDefaultY() {
        return colliderDefaultY;
    }

    public int getSpeed(){
        return this.speed;
    }

    public void kill(){
        this.alive = false;
    }

    public boolean isAlive() {
        return alive;
    }

    public Rectangle getCollider(){
        return this.collider;
    }

    public void setAlive(boolean alive) {
        this.alive = alive;
    }
}
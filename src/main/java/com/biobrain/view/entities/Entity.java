package com.biobrain.view.entities;

import com.biobrain.model.Location;
import com.biobrain.view.panels.GamePanel;

import java.awt.*;
import java.awt.image.BufferedImage;

public class Entity {
    GamePanel gamePanel;
    public static final int MIN_HEALTH = 0;
    public static final int MAX_HEALTH = 10;
    GamePanel gp;
    public int labX, labY;
    public int speed;
    public BufferedImage up1, up2, down1, down2, left1, left2, right1, right2, itemImage;
    public BufferedImage upAttack, downAttack, leftAttack, rightAttack;
    public String direction = "down";
    public int counter =0;
    public int spriteSelected=1;
    public Rectangle collider = new Rectangle(0,0,48,48);
    private int height;
    private int width;
    public int colliderDefaultX, colliderDefaultY;
    public boolean collisionOn = false;
    public int health;
    private boolean isDead = false;
    public int actionLockCounter =0;
    public int currentLocation;
    public boolean invincible =false;
    public int invincibleCounter = 0;

    public Entity(){
    }

    public Entity(GamePanel gamePanel) {
        this.gp = gamePanel;
    }

    public int getHeight() {
        return height;
    }

    public int getWidth() {
        return width;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public boolean isDead() {
        return isDead;
    }
    public void setDead(boolean dead) {
        this.isDead = dead;
    }

    public void decreaseHealth(){

    }
}
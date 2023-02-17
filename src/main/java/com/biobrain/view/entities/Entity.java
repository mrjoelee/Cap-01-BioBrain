package com.biobrain.view.entities;

import com.biobrain.util.FileLoader;
import com.biobrain.view.panels.GamePanel;

import java.awt.*;
import java.awt.image.BufferedImage;

public class Entity {
    GamePanel gamePanel;
    public static final double MIN_HEALTH = 0;
    public static final double MAX_HEALTH = 100;
    public int labX, labY;
    public int speed;
    public BufferedImage up1, up2, down1, down2, left1, left2, right1, right2, itemImage;
    public BufferedImage upAttack, downAttack, leftAttack, rightAttack;
    public String direction = "down";
    public int counter =0;
    public int spriteSelected=1;
    public Rectangle collider;
    public int colliderDefaultX, colliderDefaultY;
    public boolean collisionOn = false;
    private double health = 100.0;
    private boolean isDead = false;
    public double getHealth() {
        return health;
    }
    public void setHealth(double health){
        if(health < MIN_HEALTH){
            setDead(true);
        } else {
            this.health = health;
        }
    }
    public boolean isDead() {
        return isDead;
    }
    public void setDead(boolean dead) {
        this.isDead = dead;
    }

    public Entity(){
    }
    public Entity(GamePanel gamePanel) {
        this.gamePanel = gamePanel;
    }
}
package com.biobrain.view.entities;

/*
 * ItemEntity | Class
 * item graphics object
 * holds item image data
 */

import com.biobrain.util.FileLoader;
import com.biobrain.view.panels.GamePanel;

import java.awt.*;
import java.awt.image.BufferedImage;

public class ItemEntity extends Entity {
    private final GamePanel gamePanel;
    String name;
    int roomCode;
    String image;
    private final int x;
    private final int y;
    Rectangle itemCollider;

    // CTOR
    public ItemEntity(GamePanel gamePanel, String name,int roomCode, int x, int y, String image) {
        this.gamePanel = gamePanel;
        this.name = name;
        this.roomCode = roomCode;
        this.image = image;
        this.x = x;
        this.y = y;
        setItemImage(image);
    }

    public void setItemImage(String image) {
        //manage sprites loaded
        itemImage = FileLoader.loadBuffered(image);
    }

    // function will be called each frame, only contains logic that needs constant updating
    public void update() {
    }

    // update player graphics
    public void draw(Graphics2D g2) {

        g2.drawImage(getBuffImage(), getX(), getY(), 48, 48, null);
        g2.drawRect(getItemCollider().x, getItemCollider().y, getItemCollider().width, getItemCollider().height);
    }

    public String getName() {
        return name;
    }

    public Rectangle getItemCollider(){
        return this.itemCollider;
    }

    public void setItemCollider(Rectangle collider) {
        this.itemCollider = collider;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public BufferedImage getBuffImage(){
        return this.itemImage;
    }

    public String getImagePath(){
        return image;
    }

    public int getRoomCode() {
        return roomCode;
    }
}
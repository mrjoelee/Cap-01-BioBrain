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

public class ItemEntity{
    String name;
    int roomCode;
    String image;
    String description;
    int damage;
    private int x;
    private int y;
    Rectangle itemCollider;

    public ItemEntity(String name, int roomCode, String image, String description, int damage, int x, int y) {
        this.name = name;
        this.roomCode = roomCode;
        this.image = image;
        this.description = description;
        this.damage = damage;
        this.x = x;
        this.y = y;
    }

    // CTOR
    public ItemEntity(String name,int roomCode, int x, int y, String image) {
        this.name = name;
        this.roomCode = roomCode;
        this.image = image;
        this.x = x;
        this.y = y;
    }

    // function will be called each frame, only contains logic that needs constant updating
    public void update() {
    }

    // update player graphics
    public void draw(Graphics2D g2) {
        g2.drawImage(getItemImage(), getX(), getY(), 48, 48, null);
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


    public String getImagePath(){
        return image;
    }

    public int getRoomCode() {
        return roomCode;
    }

    public BufferedImage getItemImage(){
        return FileLoader.loadBuffered(getImagePath());
    }
}
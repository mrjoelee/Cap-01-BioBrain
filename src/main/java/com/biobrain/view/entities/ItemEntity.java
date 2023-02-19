package com.biobrain.view.entities;

/*
 * ItemEntity | Class
 * item graphics object
 * holds item image data
 */

import com.biobrain.util.FileLoader;

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
    public void draw(Graphics2D g2, BufferedImage image) {
        g2.drawImage(image, getX(), getY(), 48, 48, null);
        g2.setStroke(new BasicStroke(3));
        g2.setColor(new Color(0,0,0,0));
        g2.drawRect(getItemCollider().x, getItemCollider().y, getItemCollider().width, getItemCollider().height);
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
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
    public void setImage(String image){
        this.image = image;
    }
    public int getRoomCode() {
        return roomCode;
    }

    public BufferedImage getItemImage(){
        return FileLoader.loadBuffered(getImagePath());
    }

}
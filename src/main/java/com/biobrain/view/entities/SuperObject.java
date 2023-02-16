package com.biobrain.view.entities;

import com.biobrain.util.FileLoader;

import java.awt.*;
import java.awt.image.BufferedImage;

public class SuperObject {
    String name;
    int roomCode;
    String image;
    String description;
    private int x;
    private int y;
    private int width;
    private int height;
    Rectangle itemCollider;

    public SuperObject(String name, int roomCode, String image, String description, int x, int y, int width, int height, Rectangle itemCollider) {
        this.name = name;
        this.roomCode = roomCode;
        this.image = image;
        this.description = description;
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.itemCollider = itemCollider;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getRoomCode() {
        return roomCode;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public Rectangle getItemCollider() {
        return itemCollider;
    }

    public void setItemCollider(Rectangle itemCollider) {
        this.itemCollider = itemCollider;
    }
    public BufferedImage getItemImage(){
        return FileLoader.loadBuffered(getImagePath());
    }
    public String getImagePath(){
        return image;
    }

    public void draw(Graphics2D g2) {

        g2.drawImage(getItemImage(), getX()*2, getY()*2, (int) (getWidth()*1.5), getHeight()*3, null);
        g2.drawRect(getItemCollider().x, getItemCollider().y, getItemCollider().width, getItemCollider().height);
    }


}

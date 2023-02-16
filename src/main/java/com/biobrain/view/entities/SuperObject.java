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
    public int defaultX;
    public int defaultY;
    public boolean collision;
    Rectangle objectCollider;

    public SuperObject(String name, int roomCode, String image, String description, int x, int y, int width, int height, Rectangle objectCollider, boolean collision) {
        this.name = name;
        this.roomCode = roomCode;
        this.image = image;
        this.description = description;
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.objectCollider = objectCollider;
        this.collision = collision;
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

    public void setCollision(boolean collision) {
        this.collision = collision;
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

    public void setDefaultX(int defaultX) {
        this.defaultX = defaultX;
    }

    public void setDefaultY(int defaultY) {
        this.defaultY = defaultY;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public Rectangle getObjectCollider() {
        return objectCollider;
    }

    public void setObjectCollider(Rectangle objectCollider) {
        this.objectCollider = objectCollider;
    }
    public BufferedImage getObjectImage(){
        return FileLoader.loadBuffered(getImagePath());
    }
    public String getImagePath(){
        return image;
    }

    public void draw(Graphics2D g2) {

        g2.drawImage(getObjectImage(), getX()*2, getY()*2, (int) (getWidth()*1.5), getHeight()*3, null);
        g2.setStroke(new BasicStroke(3));
        g2.setColor(new Color(0,0,0,0));
        g2.drawRect(getObjectCollider().x, getObjectCollider().y, getObjectCollider().width, getObjectCollider().height);
    }
}

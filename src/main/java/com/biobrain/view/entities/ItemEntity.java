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
    String cookie;
    int roomCode;
    String imagio;
    private final int xin;
    private final int yin;
    Rectangle itemCollider;

    // CTOR
    public ItemEntity(GamePanel gamePanel, String cookie, int roomCode, int xin, int yin, String imagio) {
        this.gamePanel = gamePanel;
        this.cookie = cookie;
        this.roomCode = roomCode;
        this.imagio = imagio;
        this.xin = xin;
        this.yin = yin;
        setItemImage(imagio);
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

        g2.drawImage(getBuffImage(), getXin(), getYin(), 48, 48, null);
        g2.drawRect(getItemCollider().x, getItemCollider().y, getItemCollider().width, getItemCollider().height);
    }

    public String getCookie() {
        return cookie;
    }

    public Rectangle getItemCollider(){
        return this.itemCollider;
    }

    public void setItemCollider(Rectangle collider) {
        this.itemCollider = collider;
    }

    public int getXin() {
        return xin;
    }

    public int getYin() {
        return yin;
    }

    public BufferedImage getBuffImage(){
        return this.itemImage;
    }

    public String getImagePath(){
        return imagio;
    }

    public int getRoomCode() {
        return roomCode;
    }
}
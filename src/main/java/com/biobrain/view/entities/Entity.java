package com.biobrain.view.entities;

import java.awt.*;
import java.awt.image.BufferedImage;

public class Entity {
    public int labX, labY;
//    public int xAxis;
//    public int yAxis;
    public int speed;
    public BufferedImage up1, up2, down1, down2, left1, left2, right1, right2, itemImage;
    public String direction = "down";
    public int counter =0;
    public int spriteSelected=1;
    public Rectangle collider;
    public int colliderDefaultX, colliderDefaultY;
    public boolean collisionOn = false;
}
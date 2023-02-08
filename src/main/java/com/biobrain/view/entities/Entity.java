package com.biobrain.view.entities;

import java.awt.image.BufferedImage;

class Entity {
    public int xAxis;
    public int yAxis;
    public int speed;
    public BufferedImage up1, up2, down1, down2, left1, left2, right1, right2;
    public String direction;
    public int counter =0;
    public int spriteSelected=1;

}
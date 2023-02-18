package com.biobrain.objects;

import com.biobrain.util.FileLoader;
import com.biobrain.view.panels.GamePanel;

import java.awt.image.BufferedImage;

public class Health {
    GamePanel gp;
    String name;
    public BufferedImage imageHeart1, imageHeart2, imageHeart3;

    public Health(GamePanel gp){
        this.gp = gp;
        name = "Heart";
        imageHeart1 = FileLoader.loadBuffered("images/health/heart_full.png");
        imageHeart2 = FileLoader.loadBuffered("images/health/heart_half.png");
        imageHeart3 = FileLoader.loadBuffered("images/health/heart_blank.png");
    }
}

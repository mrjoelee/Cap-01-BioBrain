package com.biobrain.view.entities;

import com.biobrain.view.GamePanel;
import com.biobrain.view.KeyHandler;

import java.awt.*;

public class Player extends Entity{
    private GamePanel gamePanel;
    private KeyHandler handler;

    public Player(GamePanel gamePanel, KeyHandler keyHandler) {
        this.gamePanel = gamePanel;
        this.handler = keyHandler;

        setDefaultValues();
    }

    public void setDefaultValues(){
        xAxis = 100;
        yAxis = 100;
        speed = 4;
    }

    public void update(){
        if (handler.isUpPressed()){
            yAxis -= speed;
        }
        if (handler.isDownPressed()){
            yAxis += speed;
        }
        if (handler.isLeftPressed()){
            xAxis -= speed;
        }
        if (handler.isRightPressed()){
            xAxis += speed;
        }
    }

    public void draw(Graphics2D g2){
        g2.setColor(Color.white);
        g2.drawRect(xAxis, yAxis, gamePanel.getTileSize(), gamePanel.getTileSize());
    }
}
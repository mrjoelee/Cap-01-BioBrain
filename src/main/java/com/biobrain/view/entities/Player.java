package com.biobrain.view.entities;

/*
 * Player | Class
 * player object
 * holds player data
 * contains methods to controller player object positioning via keyboard controls
 * contains methods to update player object graphics
 */

import com.biobrain.util.FileLoader;
import com.biobrain.view.GamePanel;
import com.biobrain.view.KeyHandler;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Player extends Entity{
    private GamePanel gamePanel; // reference to GamePanel holding game logic
    private KeyHandler handler;  // instance of input manager for keyboard controls

    // CTOR
    public Player(GamePanel gamePanel, KeyHandler keyHandler) {
        this.gamePanel = gamePanel;
        this.handler = keyHandler;

        setDefaultValues(); // set default player configurations
        getPlayerImage();
    }

    // default player configuration values
    public void setDefaultValues(){
        xAxis = 0; // player x position
        yAxis = 525; // player y position
        speed = 2;   // how fast player moves through positions
    }

    public void getPlayerImage(){
            //manage sprites loaded
            up1 = FileLoader.loadBuffered("images/player_up_1.png");
            up2 =   FileLoader.loadBuffered("images/player_up_2.png");
            down1 = FileLoader.loadBuffered("images/player_down_1.png");
            down2 = FileLoader.loadBuffered("images/player_down_2.png");
            left1 = FileLoader.loadBuffered("images/player_left_1.png");
            left2 = FileLoader.loadBuffered("images/player_left_2.png");
            right1 =FileLoader.loadBuffered("images/player_right_1.png");
            right2 =FileLoader.loadBuffered("images/player_right_2.png");
    }

    // function will be called each frame, only contains logic that needs constant updating
    public void update(){
        playerControls(); // listens for user input each frame
    }

    // a list of user inputs via keyboard
    public void playerControls(){
        if (handler.isUpPressed()){   // if this key is pressed
            yAxis -= speed;           // manipulate player positioning based on speed
        }
        if (handler.isDownPressed()){
            yAxis += speed;
        }
        if (handler.isLeftPressed()){
            xAxis -= speed;
        }
        if (handler.isRightPressed()) {
            xAxis += speed;
        }

        if(handler.isUpPressed() || handler.isDownPressed() || handler.isLeftPressed() || handler.isRightPressed()) {
            if (handler.isUpPressed()) {
                direction = "up";
                yAxis -= speed;
            }
            if (handler.isDownPressed()) {
                direction = "down";
                yAxis += speed;
            }
            if (handler.isLeftPressed()) {
                direction = "left";
                xAxis -= speed;
            }
            if (handler.isRightPressed()) {
                direction = "right";
                xAxis += speed;
            }

            counter++;
            if (counter > 10) {
                if (spriteSelected == 1) {
                    spriteSelected = 2;
                } else if (spriteSelected == 2) {
                    spriteSelected = 1;
                }
                counter = 0;
            }
        }
    }

    // update player graphics
    public void draw(Graphics2D g2){
        BufferedImage image = null;
        switch(direction){
            case "up":
                if(spriteSelected ==1){
                    image = up1;
                }
                if(spriteSelected ==2){
                    image = up2;
                }
                break;
            case "down":
                if(spriteSelected ==1){
                    image = down1;
                }
                if(spriteSelected ==2){
                    image = down2;
                }
                break;
            case "left":
                if(spriteSelected ==1){
                    image = left1;
                }
                if(spriteSelected ==2){
                    image = left2;
                }
                break;
            case "right" :
                if(spriteSelected ==1){
                    image = right1;
                }
                if(spriteSelected ==2){
                    image = right2;
                }
                break;
        }
        g2.drawImage(image, xAxis, yAxis, gamePanel.getTileSize(), gamePanel.getTileSize(), null);
    }
}
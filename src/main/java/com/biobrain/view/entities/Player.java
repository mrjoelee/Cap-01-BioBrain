package com.biobrain.view.entities;

/*
 * Player | Class
 * player object
 * holds player data
 * contains methods to controller player object positioning via keyboard controls
 * contains methods to update player object graphics
 */

import com.biobrain.view.GamePanel;
import com.biobrain.view.KeyHandler;

import java.awt.*;

public class Player extends Entity{
    private GamePanel gamePanel; // reference to GamePanel holding game logic
    private KeyHandler handler;  // instance of input manager for keyboard controls

    // CTOR
    public Player(GamePanel gamePanel, KeyHandler keyHandler) {
        this.gamePanel = gamePanel;
        this.handler = keyHandler;

        setDefaultValues(); // set default player configurations
    }

    // default player configuration values
    public void setDefaultValues(){
        xAxis = 100; // player x position
        yAxis = 100; // player y position
        speed = 4;   // how fast player moves through positions
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
        if (handler.isRightPressed()){
            xAxis += speed;
        }
    }

    // update player graphics
    public void draw(Graphics2D g2){
        g2.setColor(Color.white);
        g2.drawRect(xAxis, yAxis, gamePanel.getTileSize(), gamePanel.getTileSize());
    }
}
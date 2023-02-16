package com.biobrain.view.entities;

/*
 * Player | Class
 * player object
 * holds player data
 * contains methods to controller player object positioning via keyboard controls
 * contains methods to update player object graphics
 */

import com.biobrain.util.FileLoader;
import com.biobrain.view.panels.GamePanel;
import com.biobrain.view.event.KeyHandler;

import java.awt.*;
import java.awt.image.BufferedImage;

public class Player extends Entity {
    private final GamePanel gamePanel; // reference to GamePanel holding game logic
    private final KeyHandler handler;  // instance of input manager for keyboard controls
    public final int screenX;          // x coordinate
    public final int screenY;          // y coordinate

    // CTOR
    public Player(GamePanel gamePanel, KeyHandler keyHandler) {
        this.gamePanel = gamePanel;
        this.handler = keyHandler;
        screenX = gamePanel.screenWidth/2;
        screenY = gamePanel.screenHeight/2;

        collider = new Rectangle(8, 16, 32, 32);
        colliderDefaultX = collider.x;
        colliderDefaultY = collider.y;

        setDefaultValues(); // set default player configurations
        getPlayerImage();
    }


    // CLASS METHODS
    // default player configuration values
    public void setDefaultValues() {
        labX = (gamePanel.getTileSize() * gamePanel.getMaxSectorCol()) /2; // player x position in lab
        labY = (gamePanel.getTileSize() * gamePanel.getMaxSectorRow()) /2; // player y position in lab
        speed = 4;   // how fast player moves through positions
    }

    public void getPlayerImage() {
        //manage sprites loaded
        up1 = FileLoader.loadBuffered("images/player/player_up_1.png");
        up2 = FileLoader.loadBuffered("images/player/player_up_2.png");
        down1 = FileLoader.loadBuffered("images/player/player_down_1.png");
        down2 = FileLoader.loadBuffered("images/player/player_down_2.png");
        left1 = FileLoader.loadBuffered("images/player/player_left_1.png");
        left2 = FileLoader.loadBuffered("images/player/player_left_2.png");
        right1 = FileLoader.loadBuffered("images/player/player_right_1.png");
        right2 = FileLoader.loadBuffered("images/player/player_right_2.png");
    }

    // function will be called each frame, only contains logic that needs constant updating
    public void update() {
        playerControls(); // listens for user input each frame
    }

    // a list of user inputs via keyboard
    public void playerControls() {
        if (handler.isUpPressed() || handler.isDownPressed() || handler.isLeftPressed() || handler.isRightPressed()) {
            if (handler.isUpPressed()) {
                direction = "up";
            }
            if (handler.isDownPressed()) {
                direction = "down";
            }
            if (handler.isLeftPressed()) {
                direction = "left";
            }
            if (handler.isRightPressed()) {
                direction = "right";
            }

            collisionOn = false;

            /* check tile checks if a specific tile has collision on (user cant walk over it)
             * check entrance is constantly checking if the players collider hits a room entrance
             * if player is in the sector have to check if their collider hit the room exit
             */
            gamePanel.collisionDetector.checkTile(this);
            gamePanel.collisionDetector.checkEntrance(this);
            gamePanel.collisionDetector.checkExit(this);
            gamePanel.collisionDetector.checkObject(this);
            //gamePanel.eventHandler.checkEvent();

            if (!collisionOn) {
                switch (direction) {
                    case "up":
                        labY -= speed;
                        break;

                    case "down":
                        labY += speed;
                        break;

                    case "left":
                        labX -= speed;
                        break;

                    case "right":
                        labX += speed;
                        break;
                }
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
    public void draw(Graphics2D g2) {
        BufferedImage image = null;
        switch (direction) {
            case "up":
                if (spriteSelected == 1) {
                    image = up1;
                }
                if (spriteSelected == 2) {
                    image = up2;
                }
                break;
            case "down":
                if (spriteSelected == 1) {
                    image = down1;
                }
                if (spriteSelected == 2) {
                    image = down2;
                }
                break;
            case "left":
                if (spriteSelected == 1) {
                    image = left1;
                }
                if (spriteSelected == 2) {
                    image = left2;
                }
                break;
            case "right":
                if (spriteSelected == 1) {
                    image = right1;
                }
                if (spriteSelected == 2) {
                    image = right2;
                }
                break;
        }
        if (gamePanel.currentRoom.isSector()) {
            g2.drawImage(image, labX, labY, gamePanel.getTileSize(), gamePanel.getTileSize(), null);
        } else {
            g2.drawImage(image, screenX, screenY, gamePanel.getTileSize(), gamePanel.getTileSize(), null);
        }
    }

    public BufferedImage getDirectionImage(){
        BufferedImage directionImage = null;

        switch (direction){
            case "up":
                directionImage = up1;
                break;
                case "right":
                    directionImage = right1;
                break;
            case "left":
                directionImage = left1;
                break;
            default:
                directionImage = down1;
                break;
        }
        return directionImage;
    }
}
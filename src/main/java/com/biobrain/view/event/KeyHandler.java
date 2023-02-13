package com.biobrain.view.event;

import com.biobrain.view.panels.GamePanel;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.image.Kernel;
import java.security.Key;

public class KeyHandler implements KeyListener {
    GamePanel gp;
    public boolean upPressed, downPressed, leftPressed, rightPressed;

    public KeyHandler(GamePanel gp){
        this.gp = gp;
    }

    @Override
    public void keyTyped(KeyEvent e) {

    }

    @Override
    public void keyPressed(KeyEvent e) {
        int code = e.getKeyCode();
        //move the arrow on the menu

        if(gp.gameState == gp.titleState){
            titleState(code);
        }
        else if(gp.gameState == gp.mapState){
            mapState(code);
        }
       else if(gp.gameState == gp.playState){
            playState(code);
        }
    }

    public void titleState(int code){
        if(gp.gameState == gp.titleState){
            //the current substate of titleScreen
            if(gp.ui.titleSubState == 0){
                if(code == KeyEvent.VK_W ||code == KeyEvent.VK_UP){
                    gp.ui.commandNum--;
                    if(gp.ui.commandNum < 0){
                        gp.ui.commandNum = 1;
                    }
                }
                if(code == KeyEvent.VK_S || code == KeyEvent.VK_DOWN){
                    gp.ui.commandNum++;
                    if(gp.ui.commandNum > 1){
                        gp.ui.commandNum = 0;
                    }
                }
                if(code == KeyEvent.VK_ENTER){
                    //new game - changing the gameState transition into the player.update();
                    if(gp.ui.commandNum == 0){
                        gp.ui.titleSubState = 1;
                    }
                    //quit game
                    if(gp.ui.commandNum == 1){
                        System.exit(0);
                    }
                }
            }
            else if(gp.ui.titleSubState == 1){
                if(code == KeyEvent.VK_W ||code == KeyEvent.VK_UP){
                    gp.ui.commandNum--;
                    if(gp.ui.commandNum < 0){
                        gp.ui.commandNum = 2;
                    }
                }
                if(code == KeyEvent.VK_S || code == KeyEvent.VK_DOWN){
                    gp.ui.commandNum++;
                    if(gp.ui.commandNum > 2){
                        gp.ui.commandNum = 0;
                    }
                }
                if(code == KeyEvent.VK_ENTER){
                    if(gp.ui.commandNum == 0){
                        gp.ui.titleSubState = 2;
                    }
                    if(gp.ui.commandNum == 1){
                        gp.gameState=gp.playState;
                    }
                    if(gp.ui.commandNum == 2){
                        gp.ui.titleSubState =0;
                    }
                }
            }
            else if(gp.ui.titleSubState == 2){
                if(code == KeyEvent.VK_ENTER){
                    if(gp.ui.commandNum == 0){
                        gp.ui.titleSubState =1;
                    }
                }
            }
        }
    }
    public void playState(int code){
        if(code == KeyEvent.VK_W ||code == KeyEvent.VK_UP){
            upPressed = true;
        }
        else if(code == KeyEvent.VK_S || code == KeyEvent.VK_DOWN){
            downPressed = true;
        }
        else if(code == KeyEvent.VK_A || code == KeyEvent.VK_LEFT){
            leftPressed = true;
        }
        else if(code == KeyEvent.VK_D || code == KeyEvent.VK_RIGHT){
            rightPressed = true;
        }
        else if(code == KeyEvent.VK_M){
            gp.mapDisplayed = gp.currentRoom.getRoomCode();
            gp.gameState = gp.mapState;
        }
    }

    public void mapState(int code){
        if(code == KeyEvent.VK_M){
          gp.gameState = gp.playState;
        }
        else if (code == KeyEvent.VK_RIGHT || code == KeyEvent.VK_A) {
            int mapToDisplay = gp.mapDisplayed + 1;

            if(mapToDisplay < gp.maxRooms) {
                gp.mapDisplayed = mapToDisplay;
            }
        }
        else if (code == KeyEvent.VK_LEFT || code == KeyEvent.VK_D) {
            int mapToDisplay = gp.mapDisplayed - 1;

            if(mapToDisplay >= 0) {
                gp.mapDisplayed = gp.mapDisplayed - 1;
            }
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
        int code = e.getKeyCode();
        if(code == KeyEvent.VK_W || code == KeyEvent.VK_UP){
            upPressed = false;
        }
        if(code == KeyEvent.VK_S || code == KeyEvent.VK_DOWN){
            downPressed = false;
        }
        if(code == KeyEvent.VK_A || code == KeyEvent.VK_LEFT){
            leftPressed = false;
        }
        if(code == KeyEvent.VK_D || code == KeyEvent.VK_RIGHT){
            rightPressed = false;
        }
    }

    public boolean isUpPressed() {
        return upPressed;
    }

    public boolean isDownPressed() {
        return downPressed;
    }

    public boolean isLeftPressed() {
        return leftPressed;
    }

    public boolean isRightPressed() {
        return rightPressed;
    }
}
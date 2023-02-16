package com.biobrain.view.event;

import com.biobrain.view.panels.GamePanel;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.image.Kernel;
import java.security.Key;

public class KeyHandler implements KeyListener {
    GamePanel gp;
    public boolean upPressed, downPressed, leftPressed, rightPressed, enterPressed;

    public KeyHandler(GamePanel gp) {
        this.gp = gp;
    }

    @Override
    public void keyTyped(KeyEvent e) {

    }

    @Override
    public void keyPressed(KeyEvent e) {
        int code = e.getKeyCode();
        //move the arrow on the menu
        if (gp.gameState == gp.titleState) {
            titleState(code);
        } else if (gp.gameState == gp.mapState) {
            mapState(code);
        } else if (gp.gameState == gp.playState) {
            playState(code);
        } else if (gp.gameState == gp.optionsState) {
            optionState(code);
        } else if (gp.gameState == gp.dialogueState) {
            dialogueState(code);
        } else if (gp.gameState == gp.dialoguePlay) {
            playState(code);
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
        int code = e.getKeyCode();
        if (code == KeyEvent.VK_W || code == KeyEvent.VK_UP) {
            upPressed = false;
        }
        if (code == KeyEvent.VK_S || code == KeyEvent.VK_DOWN) {
            downPressed = false;
        }
        if (code == KeyEvent.VK_A || code == KeyEvent.VK_LEFT) {
            leftPressed = false;
        }
        if (code == KeyEvent.VK_D || code == KeyEvent.VK_RIGHT) {
            rightPressed = false;
        }
        if (code == KeyEvent.VK_ENTER) {
            setEnterPressed(false);
        }
    }

    // controls while options menu is open
    private void optionState(int code) {
        if (code == KeyEvent.VK_ESCAPE) {
            gp.gameState = gp.playState;
        }

        if (code == KeyEvent.VK_ENTER) {
            gp.getKeyHandler().setEnterPressed(true);
        }

        int maxCommandNum = 0;  // resets cursor when moving cursor past the bottom option

        switch (gp.ui.getOptionsSubState()) {
            case 0:
                maxCommandNum = 4;
                break;
            case 2:
                maxCommandNum = 1;
                break;
        }

        if (code == KeyEvent.VK_W || code == KeyEvent.VK_UP) {
            gp.ui.commandNum--;
            gp.playSfx("menuNavigationSound");
            if (gp.ui.commandNum < 0) {
                gp.ui.commandNum = maxCommandNum;
            }
        }
        if (code == KeyEvent.VK_S || code == KeyEvent.VK_DOWN) {
            gp.ui.commandNum++;
            gp.playSfx("menuNavigationSound");
            if (gp.ui.commandNum > maxCommandNum) {
                gp.ui.commandNum = 0;
            }
        }
        if (code == KeyEvent.VK_A || code == KeyEvent.VK_LEFT) {
            if (gp.ui.getOptionsSubState() == 0) {
                if (gp.ui.commandNum == 1 && gp.getMusic().getVolumeScale() > 0) {
                    int temp = gp.getMusic().getVolumeScale() - 1;
                    gp.getMusic().setVolumeScale(temp);
                    gp.getMusic().checkVolume();
                    gp.playSfx("menuNavigationSound");
                }
                if (gp.ui.commandNum == 2 && gp.getSfx().getVolumeScale() > 0) {
                    int temp = gp.getSfx().getVolumeScale() - 1;
                    gp.getSfx().setVolumeScale(temp);
                    gp.getSfx().checkVolume();
                    gp.playSfx("menuNavigationSound");
                }
            }
        }
        if (code == KeyEvent.VK_D || code == KeyEvent.VK_RIGHT) {
            if (gp.ui.getOptionsSubState() == 0) {
                if (gp.ui.commandNum == 1 && gp.getMusic().getVolumeScale() < 5) {
                    int temp = gp.getMusic().getVolumeScale() + 1;
                    gp.getMusic().setVolumeScale(temp);
                    gp.getMusic().checkVolume();
                    gp.playSfx("menuNavigationSound");
                }
                if (gp.ui.commandNum == 2 && gp.getSfx().getVolumeScale() < 5) {
                    int temp = gp.getSfx().getVolumeScale() + 1;
                    gp.getSfx().setVolumeScale(temp);
                    gp.playSfx("menuNavigationSound");
                }
            }
        }
    }

    // allows user to skip past dialogue boxes and descriptions
    private void dialogueState(int code) {
        if (code == KeyEvent.VK_W || code == KeyEvent.VK_UP
                || code == KeyEvent.VK_S || code == KeyEvent.VK_DOWN
                || code == KeyEvent.VK_A || code == KeyEvent.VK_LEFT
                || code == KeyEvent.VK_D || code == KeyEvent.VK_RIGHT)
        {
            gp.gameState = gp.playState;
            playState(code);
        }
        else if (code == KeyEvent.VK_ENTER) {
            enterPressed = true;
        }
    }

    // main title menu controls
    private void titleState(int code) {
        if (gp.gameState == gp.titleState) {        //the current substate of titleScreen
            if (gp.ui.titleSubState == 0) {
                if (code == KeyEvent.VK_W || code == KeyEvent.VK_UP) {
                    gp.playSfx("menuNavigationSound");     // play menu navigation sound
                    gp.ui.commandNum--;
                    if (gp.ui.commandNum < 0) {
                        gp.ui.commandNum = 1;
                    }
                }
                if (code == KeyEvent.VK_S || code == KeyEvent.VK_DOWN) {
                    gp.playSfx("menuNavigationSound");// play menu navigation sound
                    gp.ui.commandNum++;
                    if (gp.ui.commandNum > 1) {
                        gp.ui.commandNum = 0;
                    }
                }
                if (code == KeyEvent.VK_ENTER) {       //new game - changing the gameState transition into the player.update();
                    gp.playSfx("menuSelectSound"); //play menu selection sound
                    if (gp.ui.commandNum == 0) {
                        gp.ui.titleSubState = 1;
                    }                //quit game
                    if (gp.ui.commandNum == 1) {
                        System.exit(0);
                    }
                }
            } else if (gp.ui.titleSubState == 1) {
                if (code == KeyEvent.VK_W || code == KeyEvent.VK_UP) {
                    gp.playSfx("menuNavigationSound"); //play menu navigation sound
                    gp.ui.commandNum--;
                    if (gp.ui.commandNum < 0) {
                        gp.ui.commandNum = 2;
                    }
                }
                if (code == KeyEvent.VK_S || code == KeyEvent.VK_DOWN) {
                    gp.playSfx("menuNavigationSound");     // play menu navigation sound
                    gp.ui.commandNum++;
                    if (gp.ui.commandNum > 2) {
                        gp.ui.commandNum = 0;
                    }
                }
                if (code == KeyEvent.VK_ENTER) {
                    if (gp.ui.commandNum == 0) {
                        gp.playSfx("menuSelectSound");     // play menu navigation sound
                        gp.ui.titleSubState = 2;
                    }
                    if (gp.ui.commandNum == 1) {
                        gp.stopMusic();                          // stop menu music
                        gp.ui.commandNum = 0;                    // reset commandNum for cursor selection
                        gp.playSfx("menuSelectPlaySound");     // play new game selection sound
                        gp.playMusic("mainGameTheme");         // begin gameplay theme music
                        gp.gameState = gp.playState;
                    }
                    if (gp.ui.commandNum == 2) {
                        gp.playSfx("menuSelectSound");     // play menu navigation sound
                        gp.ui.titleSubState = 0;
                    }
                }
            } else if (gp.ui.titleSubState == 2) {
                if (code == KeyEvent.VK_ENTER) {
                    if (gp.ui.commandNum == 0) {
                        gp.ui.titleSubState = 1;
                    }
                }
            }
        }
    }

    private void playState(int code) {
        if (code == KeyEvent.VK_W || code == KeyEvent.VK_UP) {
            upPressed = true;
        } else if (code == KeyEvent.VK_S || code == KeyEvent.VK_DOWN) {
            downPressed = true;
        } else if (code == KeyEvent.VK_A || code == KeyEvent.VK_LEFT) {
            leftPressed = true;
        } else if (code == KeyEvent.VK_D || code == KeyEvent.VK_RIGHT) {
            rightPressed = true;
        } else if (code == KeyEvent.VK_M) {
            gp.mapDisplayed = gp.currentRoom.getRoomCode();
            gp.gameState = gp.mapState;
        } else if (code == KeyEvent.VK_ESCAPE) {
            gp.gameState = gp.optionsState;
        }
    }


    private void mapState(int code) {
        if (code == KeyEvent.VK_M) {
            gp.gameState = gp.playState;
        } else if (code == KeyEvent.VK_RIGHT || code == KeyEvent.VK_A) {
            int mapToDisplay = gp.mapDisplayed + 1;
            if (mapToDisplay < gp.maxRooms) {
                gp.mapDisplayed = mapToDisplay;
            }
        } else if (code == KeyEvent.VK_LEFT || code == KeyEvent.VK_D) {
            int mapToDisplay = gp.mapDisplayed - 1;

            if (mapToDisplay >= 0) {
                gp.mapDisplayed = gp.mapDisplayed - 1;
            }
        }
    }

    // ACCESSOR METHODS
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

    public boolean isEnterPressed() {
        return enterPressed;
    }

    public void setEnterPressed(boolean enterPressed) {
        this.enterPressed = enterPressed;
    }
}
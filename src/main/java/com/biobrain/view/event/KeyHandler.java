package com.biobrain.view.event;

/*
 * KeyHandler | Class implements KeyListener
 * defines all user controls for each state of play
 * tracks basic logic for menus and selections
 */

import com.biobrain.model.Item;
import com.biobrain.model.Location;
import com.biobrain.view.entities.ItemEntity;
import com.biobrain.view.panels.GamePanel;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.image.Kernel;
import java.security.Key;

public class KeyHandler implements KeyListener {
    GamePanel gp;
    public boolean upPressed, downPressed, leftPressed, rightPressed, enterPressed, attackKeyPressed;

    // CTOR
    public KeyHandler(GamePanel gp) {
        this.gp = gp;
    }


    // CLASS METHODS
    // interface methods that listen for user input on keyboard
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
        } else if (gp.gameState == gp.inventoryState) {
            inventoryState(code);
        } else if (gp.gameState == gp.gameOverState) {
            gameOverState(code);
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
        if (code == KeyEvent.VK_K) {
            attackKeyPressed = false;
        }
    }

    //gameover state
    private void gameOverState(int code) {
        if (code == KeyEvent.VK_W || code == KeyEvent.VK_UP) {
            gp.ui.commandNum--;
            gp.playSfx("menuNavigationSound");
            if (gp.ui.commandNum < 0) {
                gp.ui.commandNum = 1;
            }
        }
        if (code == KeyEvent.VK_S || code == KeyEvent.VK_DOWN) {
            gp.ui.commandNum++;
            gp.playSfx("menuNavigationSound");
            if (gp.ui.commandNum > 1) {
                gp.ui.commandNum = 0;
            }
        }
        if (code == KeyEvent.VK_ENTER) {
            if (gp.ui.commandNum == 0) {
                gp.gameState = gp.titleState;
                gp.tryAgain();
            } else if (gp.ui.commandNum == 1) {
                System.exit(0);
            }
        }
    }

    // controls while options menu is open
    private void optionState(int code) {
        // if you press enter in the options menu return to the gameplay state
        if (code == KeyEvent.VK_ESCAPE) {
            gp.gameState = gp.playState;
        }

        // enter makes selections
        if (code == KeyEvent.VK_ENTER) {
            gp.getKeyHandler().setEnterPressed(true);
        }

        int maxCommandNum = 0;  // resets cursor when moving cursor past the bottom option

        // switch statement checks to see if options menu is in a sub menu or not
        switch (gp.ui.getOptionsSubState()) {
            case 0:
                maxCommandNum = 4; // change the max selections available in the current menu
                break;
            case 2:
                maxCommandNum = 1;
                break;
        }

        // MENU NAVIGATION CONTROLS

        // press up/down arrows or W/A keys to navigate up and down
        if (code == KeyEvent.VK_W || code == KeyEvent.VK_UP) {
            gp.ui.commandNum--;                      // decrement the current selection
            gp.playSfx("menuNavigationSound"); // play a sound effect
            if (gp.ui.commandNum < 0) {              // if we get to the end of the menu, wrap back around
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

        // press left/right arrows or A/D keys to adjust sliders
        if (code == KeyEvent.VK_A || code == KeyEvent.VK_LEFT) {
            // if we are in top options menu (not in a sub menu)
            if (gp.ui.getOptionsSubState() == 0) {
                // if volume is not 0, adjust sliders to the left to lower volume
                // both statements the same, top statement is for music manager, bottom statement for sfx manager

                if (gp.ui.commandNum == 0 && gp.getMusic().getVolumeScale() > 0) {
                    int temp = gp.getMusic().getVolumeScale() - 1; // get reference to volume scale and reduce it
                    gp.getMusic().setVolumeScale(temp);            // adjusts volume scale in SoundManager
                    gp.getMusic().checkVolume();                   // resets volume level according to volumeScale
                    gp.playSfx("menuNavigationSound");       // play sound effect
                }
                if (gp.ui.commandNum == 1 && gp.getSfx().getVolumeScale() > 0) {
                    int temp = gp.getSfx().getVolumeScale() - 1;
                    gp.getSfx().setVolumeScale(temp);
                    gp.getSfx().checkVolume();
                    gp.playSfx("menuNavigationSound");
                }
            }
        }
        // same as statements above except
        // if volume is set to maximum, adjust sliders to the right to raise volume
        if (code == KeyEvent.VK_D || code == KeyEvent.VK_RIGHT) {
            if (gp.ui.getOptionsSubState() == 0) {
                if (gp.ui.commandNum == 0 && gp.getMusic().getVolumeScale() < 5) {
                    int temp = gp.getMusic().getVolumeScale() + 1;
                    gp.getMusic().setVolumeScale(temp);
                    gp.getMusic().checkVolume();
                    gp.playSfx("menuNavigationSound");
                }
                if (gp.ui.commandNum == 1 && gp.getSfx().getVolumeScale() < 5) {
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
                || code == KeyEvent.VK_D || code == KeyEvent.VK_RIGHT) {
            gp.gameState = gp.playState;
            playState(code);
        } else if (code == KeyEvent.VK_ENTER) {
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
        } else if (code == KeyEvent.VK_M && gp.getPlayer().getInventory().containsKey("tablet")) {
            gp.mapDisplayed = gp.currentRoom.getRoomCode();
            gp.gameState = gp.mapState;
        } else if (code == KeyEvent.VK_M && !gp.getPlayer().getInventory().containsKey("tablet")) {
            gp.ui.setCurrentDialogue("You must have the tablet to use the map!");
            gp.gameState = gp.dialogueState;
        } else if (code == KeyEvent.VK_ESCAPE) {
            gp.gameState = gp.optionsState;
        } else if (code == KeyEvent.VK_K) {
            attackKeyPressed = true;
        } else if (code == KeyEvent.VK_I) {
            gp.gameState = gp.inventoryState;
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

    private void inventoryState(int code) {
        // MENU NAVIGATION CONTROLS
        // press up/down arrows or W/S keys to navigate up and down
        if (code == KeyEvent.VK_W || code == KeyEvent.VK_UP) {
            gp.getUi().setSlotRow((gp.getUi().getSlotRow() - 1));
            if (gp.getUi().getSlotRow() < 0) {
                gp.getUi().setSlotRow(gp.getUi().getMaxSlotRow());
            }
            gp.playSfx("menuNavigationSound"); // play a sound effect
        }
        if (code == KeyEvent.VK_S || code == KeyEvent.VK_DOWN) {
            gp.getUi().setSlotRow((gp.getUi().getSlotRow() + 1));
            if (gp.getUi().getSlotRow() > gp.getUi().getMaxSlotRow()) {
                gp.getUi().setSlotRow(0);
            }
            gp.playSfx("menuNavigationSound"); // play a sound effect
        }
        // press left/right arrows or A/D keys to navigate left and right
        if (code == KeyEvent.VK_A || code == KeyEvent.VK_LEFT) {
            gp.getUi().setSlotCol((gp.getUi().getSlotCol() - 1));
            if (gp.getUi().getSlotCol() < 0) {
                gp.getUi().setSlotCol(gp.getUi().getMaxSlotCol());
            }
            gp.playSfx("menuNavigationSound"); // play a sound effect
        }
        if (code == KeyEvent.VK_D || code == KeyEvent.VK_RIGHT) {
            gp.getUi().setSlotCol((gp.getUi().getSlotCol() + 1));
            if (gp.getUi().getSlotCol() > gp.getUi().getMaxSlotCol()) {
                gp.getUi().setSlotCol(0);
            }
            gp.playSfx("menuNavigationSound"); // play a sound effect
        }
        if (code == KeyEvent.VK_I || code == KeyEvent.VK_ESCAPE) {
            gp.playSfx("menuSelectSound"); // play a sound effect
            gp.gameState = gp.playState;
        }
        if (code == KeyEvent.VK_ENTER) {
            gp.playSfx("menuSelectSound"); // play a sound effect

            if (gp.getUi().getInventoryIndexArray().size() > gp.getUi().getItemIndexOnSlot()) {
                if (gp.getUi().getInventoryIndexArray().get(gp.getUi().getItemIndexOnSlot()) != null) {
                    gp.playSfx("menuSelectPlaySound"); // play a sound effect

                    ItemEntity usedItem = gp.getUi().getInventoryIndexArray().get(gp.getUi().getItemIndexOnSlot());
                    gp.getBioBrainApp().validateThenUseItem(usedItem.getName());
                    Item item = Item.itemName(usedItem.getName()); // gets the item name
                    gp.getPlayer().setMainWeapon(item); //sets the weapon by the item name
                    gp.gameState = gp.playState;
                    if(usedItem.getName().equalsIgnoreCase("tablet")){
                        handleUseTablet(gp.currentRoom,gp.isLaser);
                    }

                }
            }
        }
    }

    private void handleUseTablet(Location current, boolean laser) {
        if ( current.getRoomCode() == 2 && laser) {
            gp.ui.setCurrentDialogue("You have successfully disabled the laser shield on the biobrain");
            gp.gameState = gp.dialogueState;
            gp.isLaser = false;
        } else if (current.getRoomCode() != 2 && laser) {
            gp.ui.setCurrentDialogue("You must use the tablet where the Network Interface is located");
            gp.gameState = gp.dialogueState;
        }else if(gp.currentRoom.getRoomCode() == 2 && !laser){
            gp.ui.setCurrentDialogue("You have already disabled the laser shield");
            gp.gameState = gp.dialogueState;
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
package com.biobrain.view.entities.NPC;

import com.biobrain.util.FileLoader;
import com.biobrain.view.entities.Entity;
import com.biobrain.view.panels.GamePanel;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.Random;

public class Npc extends Entity {
    GamePanel gamePanel;
    private String name;
    private String description;
    private final int roomCode;
    private final int speed;
    private int blockX; // count only 1 block then x by tilesize
    private int blockY;
    private String collisionDirection = "up";

    public Npc(GamePanel gp, String name, String description, int roomCode, int maxLife, int speed, int width, int height, int blockX, int blockY) {
        super(gp);
        this.gamePanel = gp;
        this.name = name;
        this.description = description;
        this.roomCode = roomCode;
        this.maxLife = maxLife;
        this.life = maxLife;
        this.speed = speed;
        setWidth(width);
        setHeight(height);
        this.labX = blockX * gamePanel.getTileSize();
        this.labY = blockY * gamePanel.getTileSize();
        this.collider = new Rectangle(0, 0, width, height);
        getImage();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getRoomCode() {
        return roomCode;
    }

    public String getRandomDialogue() {
        String[] DIALOGUE_OPTIONS = {
                "This is a violation of protocol, report to the director immediately.",
                "Come with me Dr. Z.",
                "Unauthorized AI movement detected. Return to your station now.",
                "Put down your weapon or be detained.",
                "Cease further action, or you will neutralized.",
                "Return to the Control Lab!"
        };
        return DIALOGUE_OPTIONS[new Random().nextInt(DIALOGUE_OPTIONS.length)];
    }

    public void getImage() {
        //manage sprites loaded
        if (roomCode == 6) {
            up1 = FileLoader.loadBuffered("images/AIBots/Boss/ai_up_1.png");
            up2 = FileLoader.loadBuffered("images/AIBots/Boss/ai_up_2.png");
            down1 = FileLoader.loadBuffered("images/AIBots/Boss/ai_down_1.png");
            down2 = FileLoader.loadBuffered("images/AIBots/Boss/ai_down_2.png");
            left1 = FileLoader.loadBuffered("images/AIBots/Boss/ai_left_1.png");
            left2 = FileLoader.loadBuffered("images/AIBots/Boss/ai_left_2.png");
            right1 = FileLoader.loadBuffered("images/AIBots/Boss/ai_right_1.png");
            right2 = FileLoader.loadBuffered("images/AIBots/Boss/ai_right_2.png");

        } else {
            up1 = FileLoader.loadBuffered("images/AIBots/ai_up_1.png");
            up2 = FileLoader.loadBuffered("images/AIBots/ai_up_2.png");
            down1 = FileLoader.loadBuffered("images/AIBots/ai_down_1.png");
            down2 = FileLoader.loadBuffered("images/AIBots/ai_down_2.png");
            left1 = FileLoader.loadBuffered("images/AIBots/ai_left_1.png");
            left2 = FileLoader.loadBuffered("images/AIBots/ai_left_2.png");
            right1 = FileLoader.loadBuffered("images/AIBots/ai_right_1.png");
            right2 = FileLoader.loadBuffered("images/AIBots/ai_right_2.png");
        }
    }

    public void update() {
        setAction();
        collisionOn = false;
        gamePanel.collisionDetector.checkTile(this);
        gamePanel.collisionDetector.checkObject(this);

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
        } else {
            switch (collisionDirection) {
                case "up":
                    labY += speed;
                    break;

                case "down":
                    labY -= speed;
                    break;

                case "left":
                    labX += speed;
                    break;

                case "right":
                    labX -= speed;
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


    public void setAction() {
        if (actionLockCounter != 120) {
            actionLockCounter++;
        }

        if (actionLockCounter == 120 || collisionOn) {
            Random random = new Random();
            int i = random.nextInt(100) + 1;
            String newDirection = null;

            // Choose a new direction if the NPC collided with a wall in the current direction
            if (collisionOn && direction.equals(collisionDirection)) {
                if (direction.equals("up") || direction.equals("down")) {
                    // If the NPC was moving up or down, choose left or right
                    if (i <= 50) {
                        newDirection = "left";
                    } else {
                        newDirection = "right";
                    }
                } else {
                    // If the NPC was moving left or right, choose up or down
                    if (i <= 50) {
                        newDirection = "up";
                    } else {
                        newDirection = "down";
                    }
                }
            } else {
                // Choose a random direction
                if (i <= 25) {
                    newDirection = "up";
                } else if (i <= 50) {
                    newDirection = "down";
                } else if (i <= 75) {
                    newDirection = "left";
                } else {
                    newDirection = "right";
                }
            }

            direction = newDirection;
            collisionOn = false;
            collisionDirection = newDirection;
            actionLockCounter = 0;
        }
    }

    public void draw(Graphics2D g2) {
        if (gamePanel.currentRoom.getRoomCode() == roomCode) {
            BufferedImage image = null;
            int screenX = labX - gamePanel.player.labX + gamePanel.player.screenX;
            int screenY = labY - gamePanel.player.labY + gamePanel.player.screenY;

            if (labX + gamePanel.getTileSize() > gamePanel.player.labX - gamePanel.player.screenX &&
                    labX - gamePanel.getTileSize() < gamePanel.player.labX + gamePanel.player.screenX &&
                    labY + gamePanel.getTileSize() > gamePanel.player.labY - gamePanel.player.screenY &&
                    labY - gamePanel.getTileSize() < gamePanel.player.labY + gamePanel.player.screenY) {

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
                    g2.drawImage(image, this.labX, this.labY, getWidth(), getHeight(), null);
                } else {
                    g2.drawImage(image, screenX, screenY, getWidth(), getHeight(), null);
                }
            }
        }
    }

}



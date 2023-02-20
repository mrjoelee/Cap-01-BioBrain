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
    public BufferedImage upHit, downHit, leftHit, rightHit;
    public boolean isHit;
    public int waitNextMove =0;

    public Npc(GamePanel gp, String name, String description, int roomCode, int health, int speed, int width, int height, int blockX, int blockY) {
        super(gp);
        this.gamePanel = gp;
        this.name = name;
        this.description = description;
        this.roomCode = roomCode;
        currentLocation = roomCode;
        this.health = health;
        this.speed = speed;
        setWidth(width);
        setHeight(height);
        this.labX = blockX * gamePanel.getTileSize();
        this.labY = blockY * gamePanel.getTileSize();

        collider = new Rectangle(width *2, height *2, width, height);
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

    public static String getRandomDialogue() {
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

            upHit = FileLoader.loadBuffered("images/AIBots/Boss/bossHit/ai_up.png");
            downHit = FileLoader.loadBuffered("images/AIBots/Boss/bossHit/ai_down.png");
            leftHit = FileLoader.loadBuffered("images/AIBots/Boss/bossHit/ai_left.png");
            rightHit = FileLoader.loadBuffered("images/AIBots/Boss/bossHit/ai_right.png");

        } else {
            up1 = FileLoader.loadBuffered("images/AIBots/ai_up_1.png");
            up2 = FileLoader.loadBuffered("images/AIBots/ai_up_2.png");
            down1 = FileLoader.loadBuffered("images/AIBots/ai_down_1.png");
            down2 = FileLoader.loadBuffered("images/AIBots/ai_down_2.png");
            left1 = FileLoader.loadBuffered("images/AIBots/ai_left_1.png");
            left2 = FileLoader.loadBuffered("images/AIBots/ai_left_2.png");
            right1 = FileLoader.loadBuffered("images/AIBots/ai_right_1.png");
            right2 = FileLoader.loadBuffered("images/AIBots/ai_right_2.png");

            upHit = FileLoader.loadBuffered("images/AIBots/botHit/ai_up.png");
            downHit = FileLoader.loadBuffered("images/AIBots/botHit/ai_down.png");
            leftHit = FileLoader.loadBuffered("images/AIBots/botHit/ai_left.png");
            rightHit = FileLoader.loadBuffered("images/AIBots/botHit/ai_right.png");
        }
    }

    public void update() {
        if(isHit && waitNextMove != 120){
            waitNextMove++;
        }
        else {
            waitNextMove = 0;
            setAction();
            collisionOn = false;
            isHit = false;
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

            if (invincible) {
                invincibleCounter++;
                if (invincibleCounter > 40) {
                    invincible = false;
                    invincibleCounter = 0;
                }
            }
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

                if(isHit){
                    switch (direction) {
                        case "up":
                            image = upHit;
                            break;
                        case "down":
                            image = downHit;
                            break;
                        case "left":
                            image = leftHit;
                            break;
                        case "right":
                            image = rightHit;
                            break;
                    }
                }
                else {

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
                }
                if (gamePanel.currentRoom.isSector()) {
                    g2.drawImage(image, this.labX, this.labY, getWidth(), getHeight(), null);
                } else {
                    g2.drawImage(image, screenX, screenY, getWidth(), getHeight(), null);
                }
            }
        }
    }
    public void decreaseHealth(){
        double fractionToDecrease = 1.0 / 60; // decrease health by 1/60th of total health every frame
        int currentHealth = getHealth();
        int newHealth = (int) Math.max(currentHealth - (fractionToDecrease * currentHealth), MIN_HEALTH); // decrease health by fraction and ensure it doesn't fall below MIN_HEALTH
        if (newHealth <= MIN_HEALTH) {
            setDead(true);
            if(currentLocation == 6){
                gamePanel.isBossDead = true;
                gamePanel.gameState = gamePanel.winState;
            }
        } else {
            setHealth(newHealth);
        }
    }

    public int getHealth(){
        return health;
    }
    public void setHealth(int health){
        this.health = health;
    }

    public void setUpHit(){
        if(!invincible){
            decreaseHealth();
            isHit = true;
            invincible = true;
        }
    }
}



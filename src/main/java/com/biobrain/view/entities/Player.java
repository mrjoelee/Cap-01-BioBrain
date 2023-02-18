package com.biobrain.view.entities;

/*
 * Player | Class
 * player object
 * holds player data
 * contains methods to controller player object positioning via keyboard controls
 * contains methods to update player object graphics
 */

import com.biobrain.model.Item;
import com.biobrain.util.FileLoader;
import com.biobrain.view.panels.GamePanel;
import com.biobrain.view.event.KeyHandler;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Player extends Entity {

    public Map<String, Item> inventory;
    public Map<String, ItemEntity> invItemImages;
    private final List<String> visitedLocations;
    private Item mainWeapon;
    private GamePanel gamePanel; // reference to GamePanel holding game logic
    private KeyHandler handler;  // instance of input manager for keyboard controls
    public int screenX;
    public int screenY;
    private long lastAttackTime =0;
    private boolean isAttacking = false;
    public int health = getHealth();
    public int maxHealth;

    public Player() {
        super();
        this.visitedLocations = new ArrayList<>();
        this.inventory = new HashMap<>();
        this.invItemImages  = new HashMap<>();
    }

    // CTOR
    public Player(GamePanel gamePanel, KeyHandler keyHandler) {
        super(gamePanel);
        //TODO Undo for testing purposes
        this.mainWeapon = null;
                //= new Item("neuralyzer", "a blaster test", 5);
        this.visitedLocations = new ArrayList<>();
        this.inventory = new HashMap<>();
        this.invItemImages  = new HashMap<>();
        this.gamePanel = gamePanel;
        this.handler = keyHandler;
        screenX = gamePanel.screenWidth/2;
        screenY = gamePanel.screenHeight/2;

        collider = new Rectangle(8, 16, 32, 32);
        colliderDefaultX = collider.x;
        colliderDefaultY = collider.y;
        getPlayerImage();
        setDefaultValues(); // set default player configurations
    }

    public void setMainWeapon(Item item){
        if(item.getDamage() > 0){
            mainWeapon = item;
            getPlayerImage();
        }
    }

    // CLASS METHODS
    // default player configuration values
    public void setDefaultValues() {
        labX = (gamePanel.getTileSize() * gamePanel.getMaxSectorCol()) /2; // player x position in lab
        labY = (gamePanel.getTileSize() * gamePanel.getMaxSectorRow())/2; // player y position in lab
        setWidth(gamePanel.getTileSize());
        setHeight(gamePanel.getTileSize());
        speed = 4;   // how fast player moves through positions

        //Player Status
        maxHealth = 6; //3 hearts
        health = maxHealth; //1 heart equals a half heart
    }

    //resetting the game if player starts a new game from gameoverState
    public void setDefaultPositions(){
        labX = (gamePanel.getTileSize() * gamePanel.getMaxSectorCol()) /2; // player x position in lab
        labY = (gamePanel.getTileSize() * gamePanel.getMaxSectorRow()) /2; // player y position in lab
    }
    public void restoreLife(){
        maxHealth = MAX_HEALTH;
        health = maxHealth;
    }

    public void addVisitedLocation(String location){
        visitedLocations.add(location);
    }

    public List<String> getVisitedLocations(){
        return visitedLocations;
    }

    public void addItem(String itemName, Item item) {
        inventory.put(itemName,item);
        invItemImages.put(itemName, gamePanel.getItemManager().getItems().get(itemName));
    }

    public void removeItem(String itemName, Item item) {
        inventory.remove(itemName, item);
    }

    public Map<String,Item> getInventory() {
        return inventory;
    }
    // default player configuration values
    public void getPlayerImage() {
        if(mainWeapon == null) {
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
        else if(mainWeapon.getName().equalsIgnoreCase("harpoon")){
            up1 = FileLoader.loadBuffered("images/player/harpoon/player_up_1.png");
            up2 = FileLoader.loadBuffered("images/player/harpoon/player_up_2.png");
            down1 = FileLoader.loadBuffered("images/player/harpoon/player_down_1.png");
            down2 = FileLoader.loadBuffered("images/player/harpoon/player_down_2.png");
            left1 = FileLoader.loadBuffered("images/player/harpoon/player_left_1.png");
            left2 = FileLoader.loadBuffered("images/player/harpoon/player_left_2.png");
            right1 = FileLoader.loadBuffered("images/player/harpoon/player_right_1.png");
            right2 = FileLoader.loadBuffered("images/player/harpoon/player_right_2.png");
        }
        else if(mainWeapon.getName().equalsIgnoreCase("neuralyzer")){
            up1 = FileLoader.loadBuffered("images/player/neuralyzer/player_up_1.png");
            up2 = FileLoader.loadBuffered("images/player/neuralyzer/player_up_2.png");
            down1 = FileLoader.loadBuffered("images/player/neuralyzer/player_down_1.png");
            down2 = FileLoader.loadBuffered("images/player/neuralyzer/player_down_2.png");
            left1 = FileLoader.loadBuffered("images/player/neuralyzer/player_left_1.png");
            left2 = FileLoader.loadBuffered("images/player/neuralyzer/player_left_2.png");
            right1 = FileLoader.loadBuffered("images/player/neuralyzer/player_right_1.png");
            right2 = FileLoader.loadBuffered("images/player/neuralyzer/player_right_2.png");
        }
        else if(mainWeapon.getName().equalsIgnoreCase("blaster")){
            up1 = FileLoader.loadBuffered("images/player/blaster/player_up_1.png");
            up2 = FileLoader.loadBuffered("images/player/blaster/player_up_2.png");
            down1 = FileLoader.loadBuffered("images/player/blaster/player_down_1.png");
            down2 = FileLoader.loadBuffered("images/player/blaster/player_down_2.png");
            left1 = FileLoader.loadBuffered("images/player/blaster/player_left_1.png");
            left2 = FileLoader.loadBuffered("images/player/blaster/player_left_2.png");
            right1 = FileLoader.loadBuffered("images/player/blaster/player_right_1.png");
            right2 = FileLoader.loadBuffered("images/player/blaster/player_right_2.png");
        }
    }

    // function will be called each frame, only contains logic that needs constant updating
    public void update() {
        playerControls(); // listens for user input each frame

        if(health > MAX_HEALTH){
            health = MAX_HEALTH;
        }
        if(health <= 0){
            gamePanel.gameState = gamePanel.gameOverState;
            gamePanel.stopMusic();
            gamePanel.playSfx("gameOverSound");
        }
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
            String itemGrabbed = gamePanel.collisionDetector.checkGrabItem(this);
            pickUpItem(itemGrabbed);
            gamePanel.collisionDetector.checkNPCCollision(this, gamePanel.aiRobots);
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
        if(gamePanel.keyHandler.attackKeyPressed){
            isAttacking = true;
            playerAttack();
        }
    }

    public void pickUpItem(String itemName) {
        if (!itemName.equals("none") && !itemName.equals("biobrain")) {
            System.out.println(itemName);
            gamePanel.getBioBrainApp().validateThenGetItem(itemName);
            gamePanel.getItemManager().getItems().put(itemName, null);
        }
    }

    private void playerAttack(){
        if(mainWeapon == null){
            loadAttackImages();
        }
       else if(mainWeapon.getName().equalsIgnoreCase("blaster") || mainWeapon.getName().equalsIgnoreCase("harpoon")){
            //TODO can we add a attack cool down for each weapon
            loadAttackImages();
            long currentTime = System.currentTimeMillis();
            long attackCoolDown = 250;
            if(currentTime - lastAttackTime > attackCoolDown) {
                gamePanel.projectiles.add(new Projectile(gamePanel, mainWeapon, direction, this));
                lastAttackTime = currentTime;
            }
        }
        else if (mainWeapon.getName().equalsIgnoreCase("neuralyzer")) {
           loadAttackImages();
            long currentTime = System.currentTimeMillis();
            long attackCoolDown = 3000; //timer so the neuralyzer isn't used back to back
            if(currentTime - lastAttackTime > attackCoolDown) {
                gamePanel.setGrayScreen(true);
                neuralyzerEffect();
                lastAttackTime = currentTime;
            }
        }
    }

    public void neuralyzerEffect() {
        Timer timer = new Timer(5, new ActionListener() {
            int alpha = 255;
            boolean fadeIn = true;
            final int step = 10; // adjust this to change the speed of the effect

            @Override
            public void actionPerformed(ActionEvent e) {
                if (fadeIn) {
                    alpha -= step;
                    if (alpha < 0) {
                        alpha = 0;
                        fadeIn = false;
                    }
                } else {
                    alpha += step;
                    if (alpha > 255) {
                        alpha = 255;
                        gamePanel.setGrayScreen(false);
                        ((Timer) e.getSource()).stop();
                    }
                }
                gamePanel.setGrayScreenAlpha(alpha);
            }
        });
        timer.setInitialDelay(0);
        timer.start();
    }

    public void loadAttackImages(){
        String name = mainWeapon == null ? "" : mainWeapon.getName();
        switch(name) {
            case "":
                upAttack = FileLoader.loadBuffered("images/player/attack/hands/player_up.png");
                downAttack = FileLoader.loadBuffered("images/player/attack/hands/player_down.png");
                leftAttack = FileLoader.loadBuffered("images/player/attack/hands/player_left.png");
                rightAttack =FileLoader.loadBuffered("images/player/attack/hands/player_right.png");
                break;
            case "neuralyzer":
                upAttack = FileLoader.loadBuffered("images/player/attack/neuralyzer/player_up.png");
                downAttack = FileLoader.loadBuffered("images/player/attack/neuralyzer/player_down.png");
                leftAttack = FileLoader.loadBuffered("images/player/attack/neuralyzer/player_left.png");
                rightAttack =FileLoader.loadBuffered("images/player/attack/neuralyzer/player_right.png");
                break;
            case "blaster":
                upAttack = FileLoader.loadBuffered("images/player/attack/blaster/player_up.png");
                downAttack = FileLoader.loadBuffered("images/player/attack/blaster/player_down.png");
                leftAttack = FileLoader.loadBuffered("images/player/attack/blaster/player_left.png");
                rightAttack =FileLoader.loadBuffered("images/player/attack/blaster/player_right.png");
                break;
            case "harpoon":
                upAttack = FileLoader.loadBuffered("images/player/attack/harpoon/player_up.png");
                downAttack = FileLoader.loadBuffered("images/player/attack/harpoon/player_down.png");
                leftAttack = FileLoader.loadBuffered("images/player/attack/harpoon/player_left.png");
                rightAttack =FileLoader.loadBuffered("images/player/attack/harpoon/player_right.png");
                break;
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

    public String displayPlayerInfo() {

        if (inventory.isEmpty()) {
            return String.format("\n =========== PLAYER INFO =================\n" +
                    "\n      Your health is at %s", getHealth() +
                    "\n      Your current inventory is empty.\n" +
                    "\n\n =========================================\n");
        }

        return String.format("\n =========== PLAYER INFO =================\n" +
                "\n          Your health is at %s", getHealth() +
                "\n    Your current inventory: " + getInventory().keySet() +
                "\n\n =========================================\n");
    }

    // update player graphics
    public void draw(Graphics2D g2) {
        BufferedImage image = null;
        if (isAttacking) {
            // Handle attack animation
            switch (direction) {
                case "up":
                    image = upAttack;
                    break;
                case "down":
                    image = downAttack;
                    break;
                case "left":
                    image = leftAttack;
                    break;
                case "right":
                    image = rightAttack;
                    break;
            }
            isAttacking = false;
        }else {
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
            g2.drawImage(image, labX, labY, getWidth(), getHeight(), null);
        } else {
            g2.drawImage(image, screenX, screenY, getWidth(), getHeight(), null);
        }
    }


    // ACCESSOR METHODS

    public Map<String, ItemEntity> getInvItemImages() {
        return invItemImages;
    }
}
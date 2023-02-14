package com.biobrain.view.panels;

/*
 * GamePanel | Class
 * creates new thread to handle game logic
 * begins main update loop that keeps gamePanel graphics updated
 */

import com.biobrain.items.ItemManager;
import com.biobrain.util.FileLoader;
import com.biobrain.view.event.KeyHandler;
import com.biobrain.view.locations.LocationManager;
import com.biobrain.view.locations.Room;
import com.biobrain.view.event.UI;
import com.biobrain.view.entities.Player;
import com.biobrain.view.event.CollisionDetector;
import com.biobrain.view.event.EventHandler;
import com.biobrain.view.tile.Map;
import com.biobrain.view.tile.TileHelper;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;


public class GamePanel extends JPanel implements Runnable {
    //World Settings
    final int originalTileSize = 16;                         // define how large tiles for tile grid, 16x16 tiles
    final int scale = 3;                                    // scale graphics by this multiple
    final int tileSize = originalTileSize * scale;          // scale tiles by 3, 48 x 48 in this case
    double FPS = 60;                                        // frames per second (how smoothly game animation runs)
    public final int maxRooms = 7;

    //col in map txt file
    public final int maxLabCol = 60;
    public final int maxLabRow = 32;
    final int maxSectorCol = 16;                            // define how many columns will comprise window
    final int maxSectorRow = 12;                            // define how many rows will comprise window
    public final int screenWidth = tileSize * maxSectorCol;        // window width
    public final int screenHeight = tileSize * maxSectorRow;       // window height

    //Object creation
    public KeyHandler keyHandler = new KeyHandler(this);        // create new instance of input manager for keyboard commands
    public Player player = new Player(this, keyHandler);  // create instance of Player
    public TileHelper tileHelper = new TileHelper(this);
    public Room currentRoom;
    public EventHandler eventHandler = new EventHandler(this);
    public LocationManager locations = new LocationManager(this);
    public ItemManager items = new ItemManager(this);
    public Map playerMap = new Map(this);
    public CollisionDetector collisionDetector = new CollisionDetector(this);
    public UI ui = new UI(this);                   // create new instance of User Interface
    private Thread gameThread;                                       // create a new thread for game logic

    //game extras
    public int mapDisplayed;

    //game state
    public int gameState;
    public final int titleState = 0;
    public final int playState = 1;
    public final int mapState = 2;
    public final int optionsState = 3;
    public final int pauseState = 3;

    public void setupGame() {
        gameState = playState;
        currentRoom = locations.getLocations().get("sector2");
        mapDisplayed = currentRoom.getRoomCode();
    }

    // CTOR
    public GamePanel() {
        this.setPreferredSize(new Dimension(screenWidth, screenHeight));
        this.setBackground(Color.black);
        this.setDoubleBuffered(true);
        this.addKeyListener(keyHandler);
        this.setFocusable(true);
    }

    // starts new thread
    public void startGameThread() {
        gameThread = new Thread(this);
        gameThread.start();
    }

    // runs each frame to allow constant update for animation graphics
    @Override
    public void run() {
        double drawInterval = 1000000000 / FPS;                   // how long to draw for
        double nextDrawTime = System.nanoTime() + drawInterval; // adds draw interval to current time for end time

        while (gameThread != null) {
            update();  // constantly runs the update() method, this is allowing player movement
            repaint(); // this is actually calling paintComponent each frame to update graphics

            try {

                double remainingTime = nextDrawTime - System.nanoTime(); // subtract system time from nextDrawTime
                remainingTime = remainingTime / 1000000;                   // normalize time to 1 digit (seconds)

                // if remaining time drops below 0, reset it to 0 (prevents error in thread.sleep())
                if (remainingTime < 0) {
                    remainingTime = 0;
                }

                //noinspection BusyWait
                Thread.sleep((long) remainingTime);  // put the thread to sleep, preventing it from ending
                nextDrawTime += drawInterval;        // add more time to nextDrawTime to continue the while

            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    // update() runs every frame
    public void update() {
        if (gameState == playState) {
            player.update(); /* listens for player controller for movement */
        }
    }

    // update graphics for player
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g; // defines graphics configurations

        if(gameState == playState) {
            tileHelper.draw(g2);
            items.draw(g2);
            currentRoom.draw(g2);
            player.draw(g2);
        }
        else if(gameState == mapState){
            playerMap.setCurrentMapDisplayed(g2, mapDisplayed);
        }
        ui.draw(g2); // draws the Title screen
        g2.dispose(); // dispose of old configurations, player sprite will update each frame
    }

    private void drawItems() {

    }

    public int getTileSize() {
        return this.tileSize;
    }

    public int getMaxSectorCol(){
        return this.maxSectorCol;
    }

    public int getMaxSectorRow(){
        return this.maxSectorRow;
    }
}
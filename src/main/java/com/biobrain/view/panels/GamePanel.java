package com.biobrain.view.panels;

/*
 * GamePanel | Class
 * creates new thread to handle game logic
 * begins main update loop that keeps gamePanel graphics updated
 */

import com.biobrain.items.ItemManager;
import com.biobrain.model.Location;
import com.biobrain.util.music.SoundManager;
import com.biobrain.objects.ObjectManager;
import com.biobrain.view.entities.Player;
import com.biobrain.view.event.CollisionDetector;
import com.biobrain.view.event.KeyHandler;
import com.biobrain.view.event.UI;
import com.biobrain.view.locations.LocationManager;
import com.biobrain.view.tile.Map;
import com.biobrain.view.tile.TileHelper;
import com.biobrain.view.tile.TileSetter;
import javax.swing.*;
import java.awt.*;

public class GamePanel extends JPanel implements Runnable {
    // TILE MAP Settings
    final int originalTileSize = 16;                         // define how large tiles for tile grid, 16x16 tiles
    final int scale = 3;                                    // scale graphics by this multiple
    final int tileSize = originalTileSize * scale;          // scale tiles by 3, 48 x 48 in this case
    double FPS = 60;                                        // frames per second (how smoothly game animation runs)
    public final int maxRooms = 7;

    // dimensions for map txt file
    public final int maxLabCol = 60;                        // max columns of tiles in the main lab map
    public final int maxLabRow = 32;                        // max rows of tiles in the main lab map
    final int maxSectorCol = 16;                            // define how many columns will comprise window
    final int maxSectorRow = 12;                            // define how many rows will comprise window
    public final int screenWidth = tileSize * maxSectorCol;        // window width
    public final int screenHeight = tileSize * maxSectorRow;       // window height
    public int mapDisplayed;                                       // number for which map should display

    // creation of manager classes
    public KeyHandler keyHandler = new KeyHandler(this);        // create new instance of input manager for keyboard commands
    public Player player = new Player(this, keyHandler);  // create instance of Player
    public TileSetter tileSetter = new TileSetter();                // instance of TileSetter to place tile images
    public TileHelper tileHelper = new TileHelper(this);        // instance of TileHelper to line up tiles
    private SoundManager sfx;                                       // instance of SoundManager used for SFX
    private SoundManager music;                                     // instance of SoundManager used for music
    public LocationManager locations = new LocationManager(true);
    public Location currentRoom = locations.getLocations().get("sector2");
    public ItemManager items = new ItemManager(this);
    public ObjectManager object = new ObjectManager(this);
    public Map playerMap = new Map(this);
    public CollisionDetector collisionDetector = new CollisionDetector(this);

    // GAME SETTINGS
    public UI ui = new UI(this);                   // create new instance of User Interface
    private Thread gameThread;                              // create a new thread for game logic

    // game state variables
    public int gameState;
    public final int titleState = 0;
    public final int playState = 1;
    public final int mapState = 2;
    public final int optionsState = 3;
    public final int dialogueState = 4;
    public final int dialoguePlay = 5;
    public int switchStateCounter = 300;


    // CTOR
    public GamePanel() {
        this.setPreferredSize(new Dimension(screenWidth, screenHeight));
        this.setBackground(Color.black);
        this.setDoubleBuffered(true);
        this.addKeyListener(keyHandler);
        this.setFocusable(true);
        setSfx(new SoundManager(this));
        setMusic(new SoundManager(this));
    }


    // CLASS METHODS
    // define context for game start, starting gameState, starting map, and first song played
    public void setupGame() {
        gameState = playState;
        mapDisplayed = currentRoom.getRoomCode();
        playMusic("mainMenuTheme"); // plays main menu theme from soundsURL map inside SoundManager
    }

    // starts new thread
    public void startGameThread() {
        gameThread = new Thread(this);
        gameThread.start();
    }

    // runs each frame to allow constant updates for animation, graphics, etc
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
        // if game is in a state that allows the player to move
        if (gameState == playState || gameState == dialoguePlay) {
            player.update(); /* listens for player controller for movement */
        }
    }

    // updates all game graphics
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g; // defines graphics configurations

        // game is in the title menu
        if(gameState == titleState) {
            ui.draw(g2); // draws the Title screen
        }
        // displays map of game for player to navigate
        else if (gameState == mapState) {
            playerMap.setCurrentMapDisplayed(g2, mapDisplayed);
        }
        // dialogue state still allows for player movement
        else if (gameState == dialoguePlay) {
            if (switchStateCounter > 0) {
                tileHelper.draw(g2);    // draw tiles
                items.draw(g2);         // draw items to be picked up
                object.draw(g2);        // draw room objects
                currentRoom.draw(g2);   // draw the current room of play
                player.draw(g2);        // draw player graphics
                ui.draw(g2);            // draw any UI that needs to be rendered currently
                switchStateCounter--;   // decrement counter
            }
            // after the switchStateCounter reaches 0, end the dialogue state to dismiss the window
            else{
                switchStateCounter = 300; // reset timer
                gameState = playState;    // return to gameplay state
            }
        // if not in any other state, then game is in play state
        } else{
            tileHelper.draw(g2);
            items.draw(g2);
            object.draw(g2);
            currentRoom.draw(g2);
            player.draw(g2);
            ui.draw(g2);
        }
        g2.dispose(); // dispose of old configurations, player sprite will update each frame
    }

    private void drawItems() {

    }

    // plays music on a loop, made for looped music and sounds
    public void playMusic(String name){
        music.setFile(name); // set which audio file to use
        music.play();
        music.loop();
    }

    // stop soundManager from playing music
    public void stopMusic(){
        music.stop();
    }

    // sound effects only play once without a loop
    public void playSfx(String name){
        sfx.setFile(name); // set which audio file to use
        sfx.play();
    }


    // ACCESSOR METHODS
    public int getTileSize() {
        return this.tileSize;
    }

    public KeyHandler getKeyHandler() {
        return keyHandler;
    }

    public SoundManager getSfx() {
        return sfx;
    }

    public void setSfx(SoundManager sound) {
        this.sfx = sound;
    }

    public SoundManager getMusic() {
        return music;
    }

    public void setMusic(SoundManager sound) {
        this.music = sound;
    }

    public int getMaxSectorCol(){
        return this.maxSectorCol;
    }

    public int getMaxSectorRow(){
        return this.maxSectorRow;
    }
}
package com.biobrain.view;

/*
 * GamePanel | Class
 * creates new thread to handle game logic
 * begins main update loop that keeps gamePanel graphics updated
 */

import com.biobrain.view.entities.Player;

import javax.swing.*;
import java.awt.*;

public class GamePanel extends JPanel implements Runnable {
    final int originalTileSize = 16;                         // define how large tiles for tile grid, 16x16 tiles
    final int scale = 3;                                    // scale graphics by this multiple
    final int tileSize = originalTileSize * scale;          // scale tiles by 3, 48 x 48 in this case
    final int maxScreenCol = 16;                            // define how many columns will comprise window
    final int maxScreenRow = 12;                            // define how many rows will comprise window
    final int screenWidth = tileSize * maxScreenCol;        // window width
    final int screenHeight = tileSize * maxScreenRow;       // window height
    double FPS = 60;                                        // frames per second (how smoothly game animation runs)
    KeyHandler keyHandler = new KeyHandler(this);        // create new instance of input manager for keyboard commands
    Thread gameThread;                                       // create a new thread for game logic
    Player player = new Player(this, keyHandler);  // create instance of Player


    public UI ui = new UI(this);                   // create new instance of User Interface

    //game state
    public int gameState;
    public final int titleState = 0;
    public final int playState = 1;


    public void setupGame() {
        gameState = titleState;
    }

    // CTOR
    public GamePanel() {
        this.setPreferredSize(new Dimension(screenWidth, screenHeight));
        this.setBackground(Color.white);
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

        if (gameState == titleState) {
            ui.draw(g2); // draws the Title screen
        } else {
            player.draw(g2); // draws configuration to player sprite
        }
        g2.dispose(); // dispose of old configurations, player sprite will update each frame
    }

    // todo GamePanel.GetTileSize()
    public int getTileSize() {
        return tileSize;
    }
}
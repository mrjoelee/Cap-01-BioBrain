package com.biobrain.view.panels;

/*
 * GameSetter | Class
 * creates JFrame window and sets configurations
 * adds game logic as GamePanel to the JFrame window
 * displays JFrame window and pop-up instructions to user
 * begins game
 */

import com.biobrain.util.FileLoader;
import com.biobrain.util.WindowInterface;
import com.biobrain.view.panels.GamePanel;

import javax.swing.*;
import java.awt.image.BufferedImage;

public class GameSetter extends JFrame implements WindowInterface {
        JFrame window;
        GamePanel gamePanel;

    // create a window of the game logic and begins play

    public GameSetter() {
        BufferedImage icon = FileLoader.loadBuffered("images/player_down_1.png");
        window = new JFrame();

        // create new JFrame window
        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);  // define what happens upon closing window
        window.setResizable(false);                              // set ability to resize display window
        window.setTitle("BioBrain");                            // set title of window to game title
        window.setIconImage(icon);

        gamePanel = new GamePanel();                  // new instance of GamePanel (contains game logic)
        window.add(gamePanel);                                  // add Game Panel as window display
        window.pack();

        window.setLocationRelativeTo(null);
        window.setVisible(true);                                // make window display visible

        gamePanel.setupGame();
        gamePanel.gameState = gamePanel.titleState;             //sets the state to the title screen
        gamePanel.startGameThread();                            // begin the game thread to start game loop

        WindowInterface.displayPopUpWindow(gamePanel, FileLoader.loadTextFile("Instructions/Instructions.txt")); // call a pop-up window
    }
}
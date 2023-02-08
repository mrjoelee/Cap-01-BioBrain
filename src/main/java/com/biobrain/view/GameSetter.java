package com.biobrain.view;

/*
 * GameSetter | Class
 * creates JFrame window and sets configurations
 * adds game logic as GamePanel to the JFrame window
 * displays JFrame window and pop-up instructions to user
 * begins game
 */

import com.biobrain.app.BioBrainApp;
import com.biobrain.util.FileLoader;
import com.biobrain.util.WindowInterface;

import javax.swing.*;
import java.awt.*;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;

public class GameSetter implements WindowInterface {

    // create a window of the game logic and begins play
    public static void setGame() {
        JFrame window = new JFrame();
        // create new JFrame window
        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);  // define what happens upon closing window
        window.setResizable(false);                              // set ability to resize display window
        window.setTitle("BioBrain");                            // set title of window to game title

        GamePanel gamePanel = new GamePanel();                  // new instance of GamePanel (contains game logic)
        window.add(gamePanel);                                  // add Game Panel as window display
        window.pack();

        window.setLocationRelativeTo(null);
        window.setVisible(true);                                // make window display visible

        gamePanel.startGameThread();                            // begin the game thread to start game loop

        WindowInterface.displayPopUpWindow(gamePanel, FileLoader.loadTextFile("Instructions/Instructions.txt")); // call a pop-up window
    }


}
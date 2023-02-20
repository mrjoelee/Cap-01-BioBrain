package com.biobrain.client;

/*
 * Main | Game Client
 * main game client
 * loads game window
 * gives user choice of play for text game or 2d version
 */

import com.biobrain.app.BioBrainApp;
import com.biobrain.util.FileLoader;
import com.biobrain.view.panels.GameSetter;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;

public class Main {
    public static void main(String[] args) {

        JFrame frame = new JFrame();
        JPanel mainPanel = new JPanel(new BorderLayout());
        JPanel topPnl = new JPanel();
        JPanel midPnl = new JPanel(new FlowLayout(FlowLayout.CENTER));
        JPanel btnPnl = new JPanel(new FlowLayout(FlowLayout.CENTER));
        JLabel question = new JLabel();
        JButton textGame = new JButton("Text Game");
        JButton guiGame = new JButton("2D Game");

        btnPnl.add(textGame);
        btnPnl.add(guiGame);

        btnPnl.setBorder(BorderFactory.createLineBorder(Color.BLACK));

        mainPanel.add(topPnl, BorderLayout.NORTH);
        mainPanel.add(midPnl, BorderLayout.CENTER);
        mainPanel.add(btnPnl, BorderLayout.SOUTH);

        JLabel label = new JLabel();
        label.setIcon(new ImageIcon(new ImageIcon(FileLoader.loadBuffered("images/biobrain.png")).getImage().getScaledInstance(250, 75, Image.SCALE_DEFAULT)));
        topPnl.add(label);
        question.setText("Would you like to play the Text Adventure or 2D Game?");
        midPnl.add(question);

        BufferedImage icon = FileLoader.loadBuffered("images/player/player_down_1.png");
        frame.setIconImage(icon);
        frame.setResizable(false);
        frame.setTitle("BioBrain");
        frame.setSize(350,200);
        frame.add(mainPanel);
        frame.setVisible(true);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLocationRelativeTo(null);

        textGame.addActionListener(e -> {
            if(e.getSource() == textGame) {
                frame.dispose();
                loadTextAdventure();
            }
        });

        guiGame.addActionListener(e -> {
            if(e.getSource() == guiGame) {
                frame.dispose();
                loadGui();
            }
        });
    }

    private static void loadTextAdventure(){
        BioBrainApp app = new BioBrainApp();
        app.execute();
    }

    private static void loadGui(){
        GameSetter.setGame();
    }
}


package com.biobrain.view;

import javax.swing.*;

public class GameSetter {

    public static void setGame() {
        JFrame window = new JFrame();
        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        window.setResizable(true);
        window.setTitle("BioBrain");

        GamePanel gamePanel = new GamePanel();
        window.add(gamePanel);
        window.pack();

        window.setLocationRelativeTo(null);
        window.setVisible(true);

        gamePanel.startGameThread();
        displayHelpWindow(gamePanel);
    }

    static void displayHelpWindow(GamePanel parent) {
        String help = new StringBuffer().append("READ ME").toString();
        JOptionPane.showMessageDialog(parent, help);
    }
}
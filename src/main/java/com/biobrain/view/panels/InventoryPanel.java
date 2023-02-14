package com.biobrain.view.panels;

import com.biobrain.util.FileLoader;

import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;
import java.awt.image.BufferedImage;

public class InventoryPanel extends JPanel {
    public JFrame inventoryFrame;
    public JPanel inventory = new JPanel(new BorderLayout());
    GamePanel gamePanel = new GamePanel();
    ////    public Image image;
    public int screenWith = 216;
    public int screenHeight = gamePanel.screenHeight;
    Boolean isDisplay;

    public InventoryPanel(JFrame inventoryFrame,  GamePanel gamePanel) {
        this.setPreferredSize((new Dimension(216, screenHeight)));
        this.inventoryFrame = inventoryFrame;
        this.setLayout(new BorderLayout());
        this.gamePanel = gamePanel;
        this.setOpaque(false);
    }

    public void checkVisibility() {

    }

//    public InventoryPanel(GamePanel inventoryFrame, GamePanel gamePanel){
//        this.setPreferredSize(new Dimension(screenWith, screenHeight));
//        this.setLayout(new BorderLayout());
//      this.gamePanel = gamePanel;
////      this.image = image;
//        this.inventoryFrame = inventoryFrame;
//        this.setLayout(new BorderLayout());
//        this.setOpaque(false);
//    }

    public void draw(Graphics2D g2) {
        BufferedImage image = FileLoader.loadBuffered("images/inventory.png");
        g2.drawImage(image, 216, 48, null);
    }

}

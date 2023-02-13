package com.biobrain.view.event;

import com.biobrain.util.FileLoader;
import com.biobrain.util.WindowInterface;
import com.biobrain.view.panels.GamePanel;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;

public class UI implements WindowInterface {
    GamePanel gamePanel;
    Graphics2D g2;
    Font thaleahFont;
    public int commandNum =0;

    //substate - probably might be usable later.
    public int titleSubState = 0; //0: the first screen, 1: second screen

    //can create different color using RBG
    public Color color = new Color(255, 255, 255);

    //CTOR
    public UI(GamePanel gamePanel){
        this.gamePanel = gamePanel;
        thaleahFont = loadFont();
    }

    //importing font
    private Font loadFont() {
        try {
            InputStream fontStyle = this.getClass().getResourceAsStream("/font/ThaleahFat.ttf");
            //noinspection ConstantConditions
            thaleahFont = Font.createFont(Font.TRUETYPE_FONT, fontStyle);
            return thaleahFont;
        } catch (IOException | FontFormatException e) {
            e.printStackTrace();
        }
        return null;
    }

    public void draw(Graphics2D g2){
        this.g2 = g2;
        g2.setColor(Color.black);
        g2.setFont(thaleahFont);

        //title state --> other game state implementation can be put there. (pause, inventory,map,etc)??
        if(gamePanel.gameState == gamePanel.titleState){
            drawTitleScreen();
        }
    }

    private Image playerIcon(){
        BufferedImage playerIcon = FileLoader.loadBuffered("images/player_down_1.png");
        return playerIcon.getScaledInstance(25,25,0);
    }

    private void drawTitleScreen() {

        //checking titleScreen substate
        if(titleSubState == 0){
            //Title Name
            BufferedImage title = FileLoader.loadBuffered("images/lab1.png");
            int x = 0; //resolution of png is based on screenWidth and screenHeight
            int y = 0;
            g2.setColor(color);
            g2.drawImage(title, x, y, null);

            g2.setFont(g2.getFont().deriveFont(Font.BOLD,72F));
            String text = "BIO BRAIN";
             x = getXForCenteredText(text);
            y = gamePanel.getTileSize()*2;
            g2.drawString(text,x,y);
            /* --- Menu ---*/
            //New Game
            g2.setFont(g2.getFont().deriveFont(Font.BOLD,36F));
             text = "New Game";
            x = getXForCenteredText(text);
            y += gamePanel.getTileSize()*8;
            g2.drawString(text,x,y);
            playerIconPos(x, y,0);
            //TODO:Load Game?

            //Quit
            text = "Quit";
            x = getXForCenteredText(text);
            y += gamePanel.getTileSize();
            g2.drawString(text,x,y);
            playerIconPos(x,y,1);
        } else if (titleSubState == 1){
            showIntro();
            g2.setColor(color);
            g2.setFont(g2.getFont().deriveFont(Font.BOLD,36F));
            String text = "Instructions";
            int x = getXForCenteredText(text);
            int y = (int) (gamePanel.getTileSize()*10.5);
            g2.drawString(text,x,y);

            playerIconPos(x,y,0);
            //play
            g2.setColor(color);
            g2.setFont(g2.getFont().deriveFont(Font.BOLD, 36F));
            text = "Play";
            x = getXForCenteredText(text);
            y = (int) (gamePanel.getTileSize()*11.1);
            g2.drawString(text, x, y);
            playerIconPos(x,y,1);
            //back
            g2.setColor(color);
            g2.setFont(g2.getFont().deriveFont(Font.BOLD,36F));
            text = "Back";
            x = getXForCenteredText(text);
            y = (int) (gamePanel.getTileSize()*11.6);
            g2.drawString(text,x,y);
            playerIconPos(x,y,2);
        }
        else if (titleSubState == 2) {
            showInstruction();
            //back
            g2.setColor(color);
            g2.setFont(g2.getFont().deriveFont(Font.BOLD,36F));
            String text = "Back";
            int x = getXForCenteredText(text);
            int y = (gamePanel.getTileSize()*11);
            g2.drawString(text,x,y);
            playerIconPos(x,y,0);
        }
    }

    private void playerIconPos(int x, int y,int z) {
        int playerIconWidth = playerIcon().getWidth(null);
        int playerIconHeight = playerIcon().getHeight(null);
        playerIconHeight = y - 25;
        playerIconWidth = x - gamePanel.getTileSize();
        if (commandNum == z){
            //if want to use an image, can use drawImage
            g2.drawImage(playerIcon(),playerIconWidth,playerIconHeight,null);
        }
    }

    public void showInstruction() {
        BufferedImage intro = FileLoader.loadBuffered("images/Instructions1.png");
        int x = 0;
        int y = 0;
        g2.setColor(Color.black);
        g2.drawImage(intro, x, y, null);
        //WindowInterface.displayPopUpWindow(gamePanel,FileLoader.loadTextFile("Instructions/Instructions.txt"));
    }

    private void showIntro() {
        //g2.setFont(g2.getFont().deriveFont(Font.PLAIN,10F));
        BufferedImage intro = FileLoader.loadBuffered("images/intro1.png");
        int x = 0;
        int y = 0;
        g2.setColor(Color.black);
        g2.drawImage(intro, x, y, null);
    }
    //sets the (width)
    public int getXForCenteredText(String text){
        int length =(int)g2.getFontMetrics().getStringBounds(text,g2).getWidth();
        return gamePanel.screenWidth/2 - length/2;
    }

    //TODO:maybe we can use it for later?
    //new-line method
//    private void drawString(Graphics g2, String text, int x, int y){
//        for(String line : text.split("\n")){
//            g2.drawString(line, x,y+=g2.getFontMetrics().getHeight());
//        }
//    }

}

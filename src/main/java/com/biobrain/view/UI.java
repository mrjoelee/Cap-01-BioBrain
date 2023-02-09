package com.biobrain.view;

import com.biobrain.util.FileLoader;
import com.biobrain.util.WindowInterface;

import java.awt.*;
import java.awt.image.BufferedImage;

public class UI implements WindowInterface {
    GamePanel gamePanel;
    Graphics2D g2;
    public int commandNum =0;

    //substate - probably might be usable later.
    public int titleSubState = 0; //0: the first screen, 1: second screen

    //CTOR
    public UI(GamePanel gamePanel){
        this.gamePanel = gamePanel;
    }
    public void draw(Graphics2D g2){
        this.g2 = g2;
        g2.setColor(Color.black);

        //title state --> other game state implementation can be put there. (pause, inventory,map,etc)??
        if(gamePanel.gameState == gamePanel.titleState){
            drawTitleScreen();
        }
    }

    private void drawTitleScreen() {
        //checking titleScreen substate
        if(titleSubState == 0){
            //Title Name
            g2.setFont(g2.getFont().deriveFont(Font.PLAIN,10F));
            BufferedImage title = FileLoader.loadBuffered("images/background.png");
            int x = (gamePanel.getWidth() - title.getWidth()) / 2;
            int y = gamePanel.tileSize*-1;
            g2.setColor(Color.white);
            g2.drawImage(title, x, y, null);

            g2.setFont(g2.getFont().deriveFont(Font.BOLD,48F));
            String text = "BIO BRAIN";
             x = getXForCenteredText(text);
            y = gamePanel.tileSize*3;
            g2.drawString(text,x,y);
            /* --- Menu ---*/
            //New Game
            g2.setFont(g2.getFont().deriveFont(Font.BOLD,48F));
             text = "New Game";
            x = getXForCenteredText(text);
            y += gamePanel.tileSize*7;
            g2.drawString(text,x,y);
            if (commandNum ==0){
                //if want to use an image, can use drawImage
                g2.drawString(">",x-gamePanel.tileSize,y);
            }
            //TODO:Load Game?

            //Quit
            text = "Quit";
            x = getXForCenteredText(text);
            y += gamePanel.tileSize;
            g2.drawString(text,x,y);
            if (commandNum ==1){
                //if want to use an image, can use drawImage
                g2.drawString(">",x-gamePanel.tileSize,y);
            }
        } else if (titleSubState == 1){
            showIntro();
            g2.setColor(Color.blue);
            g2.setFont(g2.getFont().deriveFont(Font.BOLD,18F));
            String text = "Instructions";
            int x = getXForCenteredText(text);
            int y = (int) (gamePanel.tileSize*10.5);
            g2.drawString(text,x,y);

            if(commandNum == 0){
                g2.drawString(">",x-gamePanel.tileSize,y);
            }
            //play
            g2.setColor(Color.blue);
            g2.setFont(g2.getFont().deriveFont(Font.BOLD, 18F));
            text = "Play";
            x = getXForCenteredText(text);
            y = (int) (gamePanel.tileSize*11.1);
            g2.drawString(text, x, y);
            if (commandNum == 1) {
                g2.drawString(">", x-gamePanel.tileSize, y);
            }
            //back
            g2.setColor(Color.blue);
            g2.setFont(g2.getFont().deriveFont(Font.BOLD,18F));
            text = "Back";
            x = getXForCenteredText(text);
            y = (int) (gamePanel.tileSize*11.6);
            g2.drawString(text,x,y);
            if(commandNum == 2){
                g2.drawString(">",x-gamePanel.tileSize,y);
            }
        }
        else if (titleSubState == 2) {
            showInstruction();
            //back
            g2.setColor(Color.blue);
            g2.setFont(g2.getFont().deriveFont(Font.BOLD,18F));
            String text = "Back";
            int x = getXForCenteredText(text);
            int y = (gamePanel.tileSize*7);
            g2.drawString(text,x,y);
            if(commandNum == 0){
                g2.drawString(">",x-gamePanel.tileSize,y);
            }
        }
    }
    public void showInstruction() {
        BufferedImage intro = FileLoader.loadBuffered("images/Instructions.png");
        int x = (gamePanel.getWidth() - intro.getWidth(null)) / 2;
        int y = (gamePanel.tileSize);
        g2.setColor(Color.black);
        g2.drawImage(intro, x, y, null);
        //WindowInterface.displayPopUpWindow(gamePanel,FileLoader.loadTextFile("Instructions/Instructions.txt"));
    }

    private void showIntro() {
        //g2.setFont(g2.getFont().deriveFont(Font.PLAIN,10F));
        BufferedImage intro = FileLoader.loadBuffered("images/intro.png");
        int x = (gamePanel.getWidth() - intro.getWidth(null)) / 2;
        int y = (int) (gamePanel.tileSize*-0.2);
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

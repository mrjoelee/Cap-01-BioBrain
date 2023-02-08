package com.biobrain.view;

import com.biobrain.util.FileLoader;

import java.awt.*;
import java.awt.image.BufferedImage;

public class UI {
    GamePanel gamePanel;
    Graphics2D g2;
    public int commandNum =0;

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
    //TODO: can use drawImage() to bring a png
    private void drawTitleScreen() {
        //Title Name
        g2.setFont(g2.getFont().deriveFont(Font.PLAIN,10F));
        BufferedImage title = FileLoader.loadBuffered("images/biobrain.png");
        int x = (gamePanel.getWidth() - title.getWidth(null)) / 2;
        int y = gamePanel.tileSize;
        g2.setColor(Color.black);
        g2.drawImage(title, x, y, null);

        /* --- Menu ---*/
        //New Game
        g2.setFont(g2.getFont().deriveFont(Font.BOLD,48F));
        String text = "New Game";
        x = getXForCenteredText(text);
        y += gamePanel.tileSize*7;
        g2.drawString(text,x,y);
        if (commandNum ==0){
            //if want to use an image, can use drawImage
            g2.drawString(">",x- gamePanel.tileSize,y);
        }
        //TODO:Load Game?

        //Quit
        text = "Quit";
        x = getXForCenteredText(text);
        y += gamePanel.tileSize;
        g2.drawString(text,x,y);
        if (commandNum ==1){
            //if want to use an image, can use drawImage
            g2.drawString(">",x- gamePanel.tileSize,y);
        }
    }

    //sets the (width)
    public int getXForCenteredText(String text){
        int length =(int)g2.getFontMetrics().getStringBounds(text,g2).getWidth();
        int x = gamePanel.screenWidth/2 - length/2;
        return x;
    }
}

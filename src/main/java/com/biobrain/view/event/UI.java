package com.biobrain.view.event;

/*
 * UI | Class implements WindowInterface
 * defines all graphics settings for each playstate
 * redraws all game assets depending on current state of play
 * draws windows for dialogues and menus
 */

import com.biobrain.util.FileLoader;
import com.biobrain.util.WindowInterface;
import com.biobrain.view.panels.GamePanel;
import com.biobrain.view.panels.GameSetter;
import java.awt.*;
import java.awt.font.FontRenderContext;
import java.awt.font.LineBreakMeasurer;
import java.awt.font.TextAttribute;
import java.awt.font.TextLayout;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.text.AttributedCharacterIterator;
import java.text.AttributedString;

public class UI implements WindowInterface {
    GamePanel gamePanel;
    Graphics2D g2;
    Font thaleahFont;
    public int commandNum = 0;       // tracks which menu selection the cursor is currently highlighting

    //substates for in-game menus
    public int titleSubState = 0;    // 0: the first screen,   1: second screen

    // OptionsSubState dictates condition of options menu
    private int optionsSubState = 0; // 0: main option screen, 2: quit game confirmation

    //can create different color using RBG
    public Color color = new Color(255, 255, 255);
    private String currentDialogue = "";

    //CTOR
    public UI(GamePanel gamePanel) {
        this.gamePanel = gamePanel;
        thaleahFont = loadFont();
    }


    // CLASS METHODS

    // draws the current playstate
    public void draw(Graphics2D g2) {
        this.g2 = g2;
        g2.setColor(Color.black);
        g2.setFont(thaleahFont);

        //title state
        if (gamePanel.gameState == gamePanel.titleState) {
            drawTitleScreen();
        }
        //TODO: will add features such as sound, quit later on.
        //option state
        if (gamePanel.gameState == gamePanel.optionsState) {
            drawOption();
        }
        //adding inventory panel
        if (gamePanel.gameState == gamePanel.playState) {
            GameSetter.manageVisibility();
        }
        //dialogue state
        if (gamePanel.gameState == gamePanel.dialogueState) {
            drawDialogueScreen();
        }
        if(gamePanel.gameState == gamePanel.dialoguePlay){
            drawDialogueScreen();
        }
    }

    private void drawDialogueScreen(){
        if(!currentDialogue.isEmpty()) {
            int x = gamePanel.getTileSize() * 3;
            int y = gamePanel.getTileSize() * 3;
            int width = gamePanel.screenWidth - (gamePanel.getTileSize() * 6);
            int height;

            // Create a TextLayout object for the current dialogue
            AttributedString attributedString = new AttributedString(currentDialogue);
            Font font = thaleahFont.deriveFont(20F);
            attributedString.addAttribute(TextAttribute.FONT, font);
            AttributedCharacterIterator paragraph = attributedString.getIterator();
            FontRenderContext frc = g2.getFontRenderContext();
            LineBreakMeasurer lineMeasurer = new LineBreakMeasurer(paragraph, frc);
            float drawPosY = 0;
            int numLines = 0;

            while (lineMeasurer.getPosition() < paragraph.getEndIndex()) {
                TextLayout layout = lineMeasurer.nextLayout(width - gamePanel.getTileSize());
                numLines++;
                drawPosY += layout.getAscent() + layout.getDescent() + layout.getLeading();
            }
            // Adjust the height of the rectangle based on the number of lines required to display the text
            height = (int) (numLines * (drawPosY / numLines)) + gamePanel.getTileSize();

            drawSubWindow(x, y, width, height);
            x += gamePanel.getTileSize() / 2;
            y += gamePanel.getTileSize() / 2;

            printCurrentDialogue(width, x, y);
        }
    }

    private void drawSubWindow(int x, int y, int width, int height){
        Color opacity = new Color(0, 0, 0, 150);
        g2.setColor(opacity);
        g2.fillRoundRect(x, y, width, height, 35, 35);

        g2.setColor(color);
        g2.setStroke(new BasicStroke(5));
        g2.drawRoundRect(x + 5, y + 5, width - 10, height - 10, 25, 25);
    }

    private void printCurrentDialogue(int width, int x, int y){
        int widthInBox = width - (gamePanel.getTileSize());
        Font font = thaleahFont.deriveFont(20F);
        AttributedString attributedString = new AttributedString(currentDialogue);
        attributedString.addAttribute(TextAttribute.FONT, font);
        AttributedCharacterIterator paragraph = attributedString.getIterator();
        int paragraphStart = paragraph.getBeginIndex();
        int paragraphEnd = paragraph.getEndIndex();

        FontRenderContext frc = g2.getFontRenderContext();
        LineBreakMeasurer lineMeasurer = new LineBreakMeasurer(paragraph, frc);

        float drawPosY = y;
        lineMeasurer.setPosition(paragraphStart);

        while (lineMeasurer.getPosition() < paragraphEnd) {
            TextLayout layout = lineMeasurer.nextLayout(widthInBox);

            float drawPosX = layout.isLeftToRight()
                    ? x : widthInBox - layout.getAdvance();

            drawPosY += layout.getAscent();

            layout.draw(g2, drawPosX, drawPosY);

            drawPosY += layout.getDescent() + layout.getLeading();
        }
    }

    // draws option menu elements
    private void drawOption() {
        g2.setColor(Color.white);
        g2.setFont(g2.getFont().deriveFont(18F));
        //sub window
        int x = gamePanel.getTileSize() * 4;
        int y = gamePanel.getTileSize();
        int width = gamePanel.getTileSize() * 8;
        int height = gamePanel.getTileSize() * 10;
        drawWindow(x, y, width, height);

        // Switch governs which parts of the option menu to draw depending on substate
        switch (optionsSubState) {
            case 0:
                optionTop(x, y);             // draw main options window
                break;
            case 2:
                optionEndGameConfirm(x, y);  // draw confirmation window for quitting game
                break;
        }
    }

    // draws the 'top' of the main options menu, will add substates if necessary
    private void optionTop(int frameX, int frameY) {
        // coordinates for text
        int textX;
        int textY;

        //-------------------------OPTIONS MENU GRAPHICS-----------------------------------------------------//
        // Title of Menu - everything below pertains to the Option menu Title text
        String text = "OPTIONS";
        textX = getXForCenteredText(text);        // center text on screen
        textY = frameY + gamePanel.getTileSize(); // give text a position at the top of the window
        g2.drawString(text, textX, textY);        // draw text with given config

        // Music Settings
        textX = frameX + gamePanel.getTileSize();        // position text to the left side of the window
        textY += gamePanel.getTileSize() * 2;               // give text a position 1 tile below the above text
        g2.drawString("Music", textX, textY);       // draws given string as text with txtX/textY values

        if (commandNum == 0) {                            // draws cursor next to Music
            g2.drawString(">", textX - 25, textY);// draws given string as text with txtX/textY values
        }

        // SFX Settings
        textY += gamePanel.getTileSize();                // give text a position 1 tile below the above text
        g2.drawString("Sound Effects", textX, textY);// draws given string as text with txtX/textY values

        if (commandNum == 1) {                             // draws cursor next to Sound Effects
            g2.drawString(">", textX - 25, textY);  // draws given string as text with txtX/textY values
        }

        // Quit Game
        textY += gamePanel.getTileSize();                          // give text a position 1 tile below the above text
        g2.drawString("Quit Game", textX, textY);              // draws given string as text with txtX/textY values

        if (commandNum == 2) {                                     // draws cursor next to Quit
            g2.drawString(">", textX - 25, textY);          // draws given string as text with txtX/textY values
            if (gamePanel.getKeyHandler().isEnterPressed()) {
                setOptionsSubState(2);                            // set substate to quit game window
                commandNum = 0;                                   // reset commandNum for option slection
                gamePanel.getKeyHandler().setEnterPressed(false); // reset enterPressed to false so enter is not held
            }
        }

        // Back
        textY += gamePanel.getTileSize() * 2;                      // give text a position 1 tile below the above text
        g2.drawString("Back", textX, textY);                   // draws given string as text with txtX/textY values

        if (commandNum == 3) {                                     // draws cursor next to Back
            g2.drawString(">", textX - 25, textY);          // draws given string as text with txtX/textY values
            if(gamePanel.getKeyHandler().isEnterPressed()){
                gamePanel.gameState = gamePanel.playState;        // return to playstate
                commandNum = 0;                                   // reset commandNum for option slection
                gamePanel.getKeyHandler().setEnterPressed(false); // reset enterPressed to false so enter is not held
            }
        }

        // Music Volume Box
        textX = frameX + gamePanel.getTileSize() * 4;                     // give box a position 4 tiles to the right
        textY = frameY + gamePanel.getTileSize() * 2 + 24;                // position box next to "Music" text
        g2.drawRect(textX, textY, 120, 24);                   // draw rectangle
        int volumeBarWidth = 24 * gamePanel.getMusic().getVolumeScale();  // adjusts value of width of volume bar
        g2.fillRect(textX, textY, volumeBarWidth, 24);              // adjusts width of music volume bar in box

        // SFX Volume Box
        textY += gamePanel.getTileSize();                                 // position box next to "Sound Effects" text
        g2.drawRect(textX, textY, 120, 24);                   // draw rectangle
        volumeBarWidth = 24 * gamePanel.getSfx().getVolumeScale();        // adjusts value of width of volume bar
        g2.fillRect(textX, textY, volumeBarWidth, 24);              // adjusts width of SFX volume bar in box
    }

    // draws window and selections to confirm user is ready to quit game
    private void optionEndGameConfirm(int frameX, int frameY) {
        int textX = frameX + gamePanel.getTileSize();                // starting x position
        int textY = frameY + gamePanel.getTileSize();                // starting y position

        g2.drawString("Quit the Game?", textX, textY);           // draw confirmation text

        //CHOICES
        // Return to Title Screen
        String text = "Yes";                                         // Yes text
        textX = getXForCenteredText(text);                           // center this text on the x
        textY += gamePanel.getTileSize() * 3;                        // move this text 3 tiles below confirmation text
        g2.drawString(text, textX, textY);                           // draw text is specified position
        if (commandNum == 0) {                                       // if on the Yes selection,
            g2.drawString(">", textX - 25, textY);               // draw the cursor next to "yes"
            if (gamePanel.getKeyHandler().enterPressed) {              // if enter is pressed,
                setOptionsSubState(0);                                    // reset subState
                System.exit(0);                                     // exit app
            }
        }

        // Don't Quit the Game
        text = "No";                                                 // No text
        textX = getXForCenteredText(text);                           // center this text on the x
        textY += gamePanel.getTileSize();                            // move this text 1 tiles below yes text
        g2.drawString(text, textX, textY);                           // draw text is specified position
        if (commandNum == 1) {                                       // if on the Yes selection,
            g2.drawString(">", textX - 25, textY);               // draw the cursor next to "no"
            if (gamePanel.getKeyHandler().enterPressed) {              // if enter is pressed,
                setOptionsSubState(0);                                    // reset subState
                commandNum = 3;                                           // reset commandNum selection back to "quit game"
                gamePanel.getKeyHandler().setEnterPressed(false);         // reset enterPressed to false so enter is not held down
            }
        }
    }

    private Image playerIcon(){
        BufferedImage playerIcon = FileLoader.loadBuffered("images/player/player_down_1.png");
        return playerIcon.getScaledInstance(25, 25, 0);
    }

    private void drawTitleScreen() {
        //checking titleScreen substate
        if (titleSubState == 0) {
            //Title Name
            BufferedImage title = FileLoader.loadBuffered("images/lab1.png");
            int x = 0; //resolution of png is based on screenWidth and screenHeight
            int y = 0;
            g2.setColor(color);
            g2.drawImage(title, x, y, null);

            g2.setFont(g2.getFont().deriveFont(Font.BOLD, 72F));
            String text = "BIO BRAIN";
            x = getXForCenteredText(text);
            y = gamePanel.getTileSize() * 2;
            g2.drawString(text, x, y);
            /* --- Menu ---*/
            //New Game
            g2.setFont(g2.getFont().deriveFont(Font.BOLD, 36F));
            text = "New Game";
            x = getXForCenteredText(text);
            y += gamePanel.getTileSize() * 8;
            g2.drawString(text, x, y);
            playerIconPos(x, y, 0);
            //TODO:Load Game?

            //Quit
            text = "Quit";
            x = getXForCenteredText(text);
            y += gamePanel.getTileSize();
            g2.drawString(text, x, y);
            playerIconPos(x, y, 1);
        } else if (titleSubState == 1) {
            showIntro();
            g2.setColor(color);
            g2.setFont(g2.getFont().deriveFont(Font.BOLD, 36F));
            String text = "Instructions";
            int x = getXForCenteredText(text);
            int y = (int) (gamePanel.getTileSize() * 10.5);
            g2.drawString(text, x, y);

            playerIconPos(x, y, 0);
            //play
            g2.setColor(color);
            g2.setFont(g2.getFont().deriveFont(Font.BOLD, 36F));
            text = "Play";
            x = getXForCenteredText(text);
            y = (int) (gamePanel.getTileSize() * 11.1);
            g2.drawString(text, x, y);
            playerIconPos(x, y, 1);
            //back
            g2.setColor(color);
            g2.setFont(g2.getFont().deriveFont(Font.BOLD, 36F));
            text = "Back";
            x = getXForCenteredText(text);
            y = (int) (gamePanel.getTileSize() * 11.6);
            g2.drawString(text, x, y);
            playerIconPos(x, y, 2);
        } else if (titleSubState == 2) {
            showInstruction();
            //back
            g2.setColor(color);
            g2.setFont(g2.getFont().deriveFont(Font.BOLD, 36F));
            String text = "Back";
            int x = getXForCenteredText(text);
            int y = (gamePanel.getTileSize() * 11);
            g2.drawString(text, x, y);
            playerIconPos(x, y, 0);
        }
    }

    private void playerIconPos(int x, int y, int z) {
        int playerIconWidth = playerIcon().getWidth(null);
        int playerIconHeight = playerIcon().getHeight(null);
        playerIconHeight = y - 25;
        playerIconWidth = x - gamePanel.getTileSize();
        if (commandNum == z) {
            //if want to use an image, can use drawImage
            g2.drawImage(playerIcon(), playerIconWidth, playerIconHeight, null);
        }
    }

    public void showInstruction() {
        BufferedImage intro = FileLoader.loadBuffered("images/Instructions.png");
        int x = 0;
        int y = 0;
        g2.setColor(Color.white);
        g2.drawImage(intro, x, y, null);
        //WindowInterface.displayPopUpWindow(gamePanel,FileLoader.loadTextFile("Instructions/Instructions.txt"));
    }

    private void showIntro() {
        //g2.setFont(g2.getFont().deriveFont(Font.PLAIN,10F));
        BufferedImage intro = FileLoader.loadBuffered("images/intro.png");
        int x = 0;
        int y = 0;
        g2.setColor(Color.white);
        g2.drawImage(intro, x, y, null);
    }

    //sets the (width)
    public int getXForCenteredText(String text){
        int length =(int)g2.getFontMetrics().getStringBounds(text,g2).getWidth();
        return gamePanel.screenWidth/2 - length/2;
    }

    //creates a inner window
    public void drawWindow(int x, int y, int width, int height) {
        //creates a rectangle
        Color windowColor = new Color(0, 0, 0, 200);  // a is the opacity
        g2.setColor(windowColor);
        g2.fillRoundRect(x, y, width, height, 35, 35);

        //border for the rectangle
        windowColor = new Color(255, 255, 255, 200);
        g2.setColor(windowColor);
        g2.setStroke(new BasicStroke(5)); //defines the width of outlines of graphics
        g2.drawRoundRect(x + 5, y + 5, width - 10, height - 10, 25, 25);
    }

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

    public String getCurrentDialogue() {
        return currentDialogue;
    }

    public void setCurrentDialogue(String currentDialogue) {
        this.currentDialogue = currentDialogue;
    }

    // ACCESSOR METHODS
    // OptionsSubState dictates condition of options menu
    public int getOptionsSubState() {
        return optionsSubState;
    }

    public void setOptionsSubState(int optionsSubState) {
        this.optionsSubState = optionsSubState;
    }

    public Font getFont(){
        return thaleahFont;
    }
}

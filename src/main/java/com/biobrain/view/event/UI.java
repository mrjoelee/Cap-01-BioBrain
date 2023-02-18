package com.biobrain.view.event;

/*
 * UI | Class implements WindowInterface
 * defines all graphics settings for each playstate
 * redraws all game assets depending on current state of play
 * draws windows for dialogues and menus
 */

import com.biobrain.objects.Health;
import com.biobrain.items.ItemManager;
import com.biobrain.model.Item;
import com.biobrain.util.FileLoader;
import com.biobrain.util.WindowInterface;
import com.biobrain.view.entities.ItemEntity;
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
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class UI implements WindowInterface {
    GamePanel gamePanel;
    Graphics2D g2;
    Image fullHeart, halfHeart, blankHeart;
    Font thaleahFont;
    private int slotCol = 0;         // tracks which col of the inventory we are selecting
    private int slotRow = 0;         // tracks which row of the inventory we are selecting
    private int maxSlotRow = 1;     // 0: row1, 1: row2, etc.
    private int maxSlotCol = 3;      // 0: col1, 1: col2, etc.
    public int commandNum = 0;       // tracks which menu selection the cursor is currently highlighting

    //substates for in-game menus
    public int titleSubState = 0;    // 0: the first screen,   1: second screen

    // OptionsSubState dictates condition of options menu
    private int optionsSubState = 0; // 0: main option screen, 2: quit game confirmation

    //can create different color using RBG
    public Color color = new Color(255, 255, 255);
    private String currentDialogue = "";

    // used to store item descriptions in the corresponding index for the inventory
    List<ItemEntity> inventoryIndexArray = new ArrayList<>();

    //CTOR
    public UI(GamePanel gamePanel) {
        this.gamePanel = gamePanel;
        thaleahFont = loadFont();

        //Create Health
        Health heart = new Health(gamePanel);
        fullHeart = heart.imageHeart1.getScaledInstance(50,40,0);
        halfHeart = heart.imageHeart2.getScaledInstance(50,40,0);
        blankHeart = heart.imageHeart3.getScaledInstance(50,40,0);
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
        //option state
        if (gamePanel.gameState == gamePanel.optionsState) {
            drawOption();
            drawPlayerHealth();
        }
        //inventory state
        if (gamePanel.gameState == gamePanel.inventoryState) {
            drawInventory();
        }
        //adding inventory panel
        if (gamePanel.gameState == gamePanel.playState) {
            drawPlayerHealth();
        }
        //dialogue state
        if (gamePanel.gameState == gamePanel.dialogueState) {
            drawDialogueScreen();
            drawPlayerHealth();
        }
        if (gamePanel.gameState == gamePanel.dialoguePlay) {
            drawDialogueScreen();
            drawPlayerHealth();
        }
        if(gamePanel.gameState == gamePanel.gameOverState){
            drawGameOver();
        }
    }

    private void drawGameOver() {
        g2.setColor(new Color(0,0,0,150));
        g2.fillRect(0,0,gamePanel.screenWidth, gamePanel.screenHeight);
        int x;
        int y;
        String text;
        g2.setFont(g2.getFont().deriveFont(Font.BOLD, 110f));
        text = "Game Over";
        //Shadow
        g2.setColor(Color.black);
        x = getXForCenteredText(text);
        y = gamePanel.getTileSize()*4;
        g2.drawString(text,x,y);
        //Main
        g2.setColor(Color.white);
        g2.drawString(text,x-4,y-4);
        //start new game or quit
        g2.setFont(g2.getFont().deriveFont(50f));
        text="Try Again?";
        x = getXForCenteredText(text);
        y += gamePanel.getTileSize()*4;
        g2.drawString(text,x,y);
        playerIconPos(x,y,0);

        //Back to title screen
        text="Quit";
        x=getXForCenteredText(text);
        y +=55;
        g2.drawString(text,x,y);
        playerIconPos(x,y,1);
    }

    private void drawPlayerHealth() {
        int x = gamePanel.getTileSize()*12;  //starting point to place the health heart
        int y = gamePanel.getTileSize()-30;
        int i = 0;

        // Draw max health
        //noinspection AccessStaticViaInstance
        while(i < gamePanel.player.MAX_HEALTH/2){  // 2 lives == 1 heart.
            g2.drawImage(blankHeart,x,y,null);
            i++;
            x += gamePanel.getTileSize();
        }

        //Reset
        x = gamePanel.getTileSize()*12;
        y = gamePanel.getTileSize()-30;
        i=0;

        //Draw current health
        while(i < gamePanel.player.health){
            g2.drawImage(halfHeart, x, y, null);
            i++;
            if(i < gamePanel.player.health){
                g2.drawImage(fullHeart,x,y,null);
            }
            i++;
            x += gamePanel.getTileSize();
        }

    }

    private void drawDialogueScreen() {
        if (!currentDialogue.isEmpty()) {
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

    private void drawSubWindow(int x, int y, int width, int height) {
        Color opacity = new Color(0, 0, 0, 150);
        g2.setColor(opacity);
        g2.fillRoundRect(x, y, width, height, 35, 35);

        g2.setColor(color);
        g2.setStroke(new BasicStroke(5));
        g2.drawRoundRect(x + 5, y + 5, width - 10, height - 10, 25, 25);
    }

    private void printCurrentDialogue(int width, int x, int y) {
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
                optionTop(x, y);                          // draw main options window
                break;
            case 1:
                optionControlsWindow(x, y);               // draw window displaying keyboard controls
                break;
            case 2:
                optionEndGameConfirm(x, y);               // draw confirmation window for quitting game
                break;
        }

        gamePanel.getKeyHandler().setEnterPressed(false); // let KeyHandler know enter was released
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

        // Controls
        textY += gamePanel.getTileSize();                // give text a position 1 tile below the above text
        g2.drawString("Controls", textX, textY);     // draws given string as text with txtX/textY values

        if (commandNum == 2) {                             // draws cursor next to Sound Effects
            g2.drawString(">", textX - 25, textY);  // draws given string as text with txtX/textY values

            if (gamePanel.getKeyHandler().isEnterPressed()) {
                setOptionsSubState(1);                            // set substate to keyboard controls window
                commandNum = 0;                                   // reset commandNum for option selection
                gamePanel.getKeyHandler().setEnterPressed(false); // reset enterPressed to false so enter is not held
            }
        }

        // Quit Game
        textY += gamePanel.getTileSize();                          // give text a position 1 tile below the above text
        g2.drawString("Quit Game", textX, textY);              // draws given string as text with txtX/textY values

        if (commandNum == 3) {                                     // draws cursor next to Quit
            g2.drawString(">", textX - 25, textY);          // draws given string as text with txtX/textY values
            if (gamePanel.getKeyHandler().isEnterPressed()) {
                setOptionsSubState(2);                            // set substate to quit game window
                commandNum = 0;                                   // reset commandNum for option selection
                gamePanel.getKeyHandler().setEnterPressed(false); // reset enterPressed to false so enter is not held
            }
        }

        // Back
        textY += gamePanel.getTileSize() * 2;                      // give text a position 1 tile below the above text
        g2.drawString("Back", textX, textY);                   // draws given string as text with txtX/textY values

        if (commandNum == 4) {                                     // draws cursor next to Back
            g2.drawString(">", textX - 25, textY);          // draws given string as text with txtX/textY values
            if (gamePanel.getKeyHandler().isEnterPressed()) {
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

    // draws the window and text for the controls reminder in the options menu
    private void optionControlsWindow(int frameX, int frameY){
        String text = "Keyboard Controls";            // starting text for title
        int textX = getXForCenteredText(text);        // x pos
        int textY = frameY + gamePanel.getTileSize(); // y pos

        // Draw Title Text of Window
        g2.drawString(text, textX, textY);

        textX = frameX + gamePanel.getTileSize();                    // move text left
        textY = textY + gamePanel.getTileSize();                     // move text down
        g2.drawString("Navigate Menu", textX, textY); // draw next keyboard input
        textY = textY + gamePanel.getTileSize();                     // move text down
        g2.drawString("Movement", textX, textY); // draw next keyboard input
        textY = textY + gamePanel.getTileSize();                     // move text down
        g2.drawString("Attack", textX, textY);                   // draw next keyboard input
        textY = textY + gamePanel.getTileSize();                     // move text down
        g2.drawString("Map", textX, textY);                      // draw next keyboard input
        textY = textY + gamePanel.getTileSize();                     // move text down
        g2.drawString("Inventory", textX, textY);                // draw next keyboard input
        textY = textY + gamePanel.getTileSize();                     // move text down
        g2.drawString("Confirm Selections", textX, textY);       // draw next keyboard input
        textY = textY + gamePanel.getTileSize();                     // move text down
        g2.drawString("Options / Back", textX, textY);           // draw next keyboard input

        textX = frameX + gamePanel.getTileSize() * 5;
        textY = frameY + gamePanel.getTileSize() * 2;
        g2.drawString("W A S D / Arrows", textX, textY); // draw next keyboard input
        textY = textY + gamePanel.getTileSize();                     // move text down
        g2.drawString("W A S D / Arrows", textX, textY); // draw next keyboard input
        textY = textY + gamePanel.getTileSize();                     // move text down
        g2.drawString("K", textX, textY);                   // draw next keyboard input
        textY = textY + gamePanel.getTileSize();                     // move text down
        g2.drawString("M", textX, textY);                      // draw next keyboard input
        textY = textY + gamePanel.getTileSize();                     // move text down
        g2.drawString("I", textX, textY);                // draw next keyboard input
        textY = textY + gamePanel.getTileSize();                     // move text down
        g2.drawString("Enter", textX, textY);       // draw next keyboard input
        textY = textY + gamePanel.getTileSize();                     // move text down
        g2.drawString("Esc", textX, textY);           // draw next keyboard input

        // Draw the Back Selection to Leave the Menu
        textY = textY + gamePanel.getTileSize();

        g2.drawString("Back", textX, textY);

        if(commandNum == 0){
            g2.drawString(">", textX - 25, textY);

            if(gamePanel.getKeyHandler().isEnterPressed()){
                setOptionsSubState(0);
            }
        }
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

    // draws the inventory window, item images, and item descriptions
    private void drawInventory() {
        g2.setColor(Color.yellow);                // set text color
        g2.setFont(g2.getFont().deriveFont(36F)); // set font size
        String text = "INVENTORY";                // create inventory title text
        int titleTextX = getXForCenteredText(text);    // center text on screen
        int titleTextY = gamePanel.getTileSize() * 4;    // give text a position at the top of the window
        g2.drawString(text, titleTextX, titleTextY);        // draw text with given config

        // Draw the item window first
        int frameX = getXForCenteredText(text) - 48;   // x position (matched to text above)
        int frameY = gamePanel.getTileSize() * 5;        // y position
        int frameHeight = gamePanel.getTileSize() * 3;   // height of window
        int frameWidth = gamePanel.getTileSize() * 5;    // width of window

        // draw inventory window
        drawWindow(frameX, frameY, frameWidth, frameHeight);

        // Make the Item Slots
        final int slotXstart = frameX; // distance on x from one slot to the next
        final int slotYstart = frameY; // distance on y from one slot to the next
        int slotX = slotXstart;             // starting x pos for slot 1
        int slotY = slotYstart;             // starting y pos for slot 1

        // Draw Items Currently Held by Player

        int inventoryIndex = 0;

        // using an iterator to go through the map of items in the player's inventory
        Iterator<Map.Entry<String, Item>> itemItr = gamePanel.getPlayer().getInventory().entrySet().iterator();

        // while there is still another item in the inventory,
        while (itemItr.hasNext()) {
            Map.Entry<String, Item> item = itemItr.next();
            if (!inventoryIndexArray.contains(gamePanel.getPlayer().getInvItemImages().get(item.getKey()))) {
                inventoryIndexArray.add(gamePanel.getPlayer().getInvItemImages().get(item.getKey()));
            }
        }

        for (int i = 0; i < inventoryIndexArray.size(); i++) {
            // draw the itemEntity found
            g2.drawImage(inventoryIndexArray.get(i).getItemImage(), slotX + 20, slotY + 20, 48, 48, null);

            inventoryIndex++;                 // increment the index we are focusing on by 1
            slotX += gamePanel.getTileSize(); // add 1 tile of width between the slots

            // if we ever get to the slot at end of the row, start putting item images in the next
            if (inventoryIndex == 4) {
                slotX = slotXstart;                // back to the starting slot of the row
                slotY += gamePanel.getTileSize(); // move the items 1 tile downward to start the next row
            }
        }

        // Dimensions for Item Selection Cursor
        int cursorX = slotXstart + 20 + (gamePanel.getTileSize() * getSlotCol()); // starting x pos
        int cursorY = slotYstart + 20 + (gamePanel.getTileSize() * getSlotRow()); // starting y pos
        int cursorWidth = gamePanel.getTileSize();  // cursor width
        int cursorHeight = gamePanel.getTileSize(); // cursor height

        // Draw Cursor
        g2.setColor(Color.white);               // cursor is white
        g2.setStroke(new BasicStroke(2)); // change thickness of line

        // Draw Cursor as Rounded Square
        g2.drawRoundRect(cursorX, cursorY, cursorWidth, cursorHeight, 10, 10);

        // Item Description Window
        int dFrameX = frameX;                                     // x pos
        int dFrameY = frameY + frameHeight;                       // y pos
        int dFrameWidth = frameWidth;                             // width of window
        int dFrameHeight = gamePanel.getTileSize() * 3;           // height of window
        drawWindow(dFrameX, dFrameY, dFrameWidth, dFrameHeight);  // draw the description text window

        // Item Description Text
        int textX = dFrameX + 20;                       // x pos
        int textY = dFrameY + gamePanel.getTileSize();  // y pos
        g2.setFont(g2.getFont().deriveFont(20F));       // set font size

        // tracks which item index is the cursor sitting on
        int itemIndex = getItemIndexOnSlot();

        // check as long as there are items in the inventory
        if (itemIndex < gamePanel.getPlayer().getInventory().size()) {
            // split the description up by its new lines
            for (String line : inventoryIndexArray.get(itemIndex).getDescription().split("\n")) {
                g2.drawString(line, textX, textY); // draw a line
                textY += 32;                       // move the next line down
            }
        }
    }

    // get the item index of the currently highlighted inventory slot
    private int getItemIndexOnSlot() {
        int itemIndex = getSlotCol() + (getSlotRow() * 4); // get number index of current item in inventory
        return itemIndex;
    }

    private Image playerIcon() {
        BufferedImage playerIcon = FileLoader.loadBuffered("images/player/player_down_1.png");
        //noinspection ConstantConditions
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

            g2.setFont(g2.getFont().deriveFont(Font.BOLD, 80F));
            String text = "BIO BRAIN";
            g2.setColor(Color.black);
            x = getXForCenteredText(text);
            y = gamePanel.getTileSize()*2;
            g2.drawString(text,x,y);

            g2.setColor(color);
            g2.drawString(text, x-4, y-4);
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
    public int getXForCenteredText(String text) {
        int length = (int) g2.getFontMetrics().getStringBounds(text, g2).getWidth();
        return gamePanel.screenWidth / 2 - length / 2;
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

    public Font getFont() {
        return thaleahFont;
    }

    public int getSlotCol() {
        return slotCol;
    }

    public void setSlotCol(int slotCol) {
        this.slotCol = slotCol;
    }

    public int getSlotRow() {
        return slotRow;
    }

    public void setSlotRow(int slotRow) {
        this.slotRow = slotRow;
    }

    public int getMaxSlotRow() {
        return maxSlotRow;
    }

    public int getMaxSlotCol() {
        return maxSlotCol;
    }
}

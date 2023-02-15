package com.biobrain.util;

/*
 * WindowInterface | Interface
 * contains methods that manipulate the game window
 * contains methods that loads pop-up windows as children to the gamePanel
 */

import com.biobrain.view.panels.GamePanel;
import javax.swing.*;

public interface WindowInterface {

    // loads a pop-up window with the given string message.
    // user must click 'ok' or x out of window to proceed
    static void displayPopUpWindow(GamePanel parent, String message) {
        JOptionPane.showMessageDialog(parent, message);
    }

    //default method

}
package com.biobrain.util;

/*
 * WindowInterface | Interface
 * contains methods that manipulate the game window
 * contains methods that loads pop-up windows as children to the gamePanel
 */

import com.biobrain.view.GamePanel;
import javax.swing.*;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public interface WindowInterface {

    // loads a pop-up window with the given string message.
    // user must click 'ok' or x out of window to proceed
    static void displayPopUpWindow(GamePanel parent, String message) {
        JOptionPane.showMessageDialog(parent, message);
    }

    //default method
     static String printFile(String fileName) {
        StringBuilder sb = new StringBuilder();
        //noinspection ConstantConditions
        try (BufferedReader buffer = new BufferedReader(new InputStreamReader(WindowInterface.class.getClassLoader().getResourceAsStream(fileName)))) {
            buffer.lines().forEach(x-> sb.append(x).append("\n"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return sb.toString();
    }
}
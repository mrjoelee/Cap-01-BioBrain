package com.biobrain.util;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import javax.swing.*;

import static org.junit.jupiter.api.Assertions.*;

class WindowInterfaceTest {

    @Test
    void displayPopUpWindow_asExpectedSuccess() {
        WindowInterface.displayPopUpWindow(null, "Test String");

        JOptionPane optionPane = (JOptionPane) JOptionPane.getRootFrame().getComponent(0);
        System.out.println(optionPane.getMessage());
        Assertions.assertNotNull(optionPane, "Pop-up JOptionPane window did not appear.");
        Assertions.assertEquals("Incorrect message in JOptionPane", "Test String");
    }
}
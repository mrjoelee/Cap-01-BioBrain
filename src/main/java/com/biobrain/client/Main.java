package com.biobrain.client;

import com.biobrain.app.BioBrainApp;

import javax.swing.*;
import java.io.IOException;

public class Main {
    public static void main(String[] args) {
        BioBrainApp app = new BioBrainApp();
        app.execute();

        String help = new StringBuffer().append("READ ME").toString();
        JOptionPane.showMessageDialog(null, help);
    }
}


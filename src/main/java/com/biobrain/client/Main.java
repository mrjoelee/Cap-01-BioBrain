package com.biobrain.client;

/*
 * Main | Game Client
 * main game client
 * loads game window
 */

import com.biobrain.app.BioBrainApp;
import com.biobrain.view.GameSetter;

public class Main {
    public static void main(String[] args) {
        BioBrainApp app = new BioBrainApp();
        app.execute();

//        GameSetter.setGame(); // create new game window, start app
    }
}


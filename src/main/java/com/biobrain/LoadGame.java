package com.biobrain;

import com.biobrain.app.BioBrainApp;

import java.io.*;


public class LoadGame {
    private static Object BioBrainApp;

    public static void LoadGame() {
        try {
            FileInputStream fis = new FileInputStream("Bb.sav");
            ObjectInputStream ois = new ObjectInputStream(fis);
            BioBrainApp game = (BioBrainApp) ois.readObject();
            ois.close();
            System.out.println("\n Game loaded \n");
        }catch (Exception e) {
            System.out.println("Error! Can't load data.\n" + e.getMessage());
        }
    }
}

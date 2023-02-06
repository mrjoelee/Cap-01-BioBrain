package com.biobrain;


import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;

public class SaveGame implements java.io.Serializable {
    private static Object BioBrainApp;

    private static void saveGame() {
        try {
            FileOutputStream fos = new FileOutputStream("Bb.sav");
            ObjectOutputStream os = new ObjectOutputStream(fos);
            os.writeObject(BioBrainApp);// game info
            os.flush();
            os.close();
            System.out.println("Game saved\n");
        } catch (IOException e){
            System.out.println("Error! Cant save data.\n" + e.getMessage());
        }
    }
}

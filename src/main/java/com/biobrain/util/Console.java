package com.biobrain.util;

import java.io.IOException;

public class Console {

    public static void clear(){
        ProcessBuilder process = System.getProperty("os.name").contains("Windows") ?
                new ProcessBuilder("cmd", "/c", "cls") :
                new ProcessBuilder("clear");
        try {
            process.inheritIO().start().waitFor();
        } catch (IOException | InterruptedException ex) {
            ex.printStackTrace();
        }
    }

    public static void pause(long time){
        try{
            Thread.sleep(time);
        }
        catch(InterruptedException e){
            e.printStackTrace();
        }

    }
}
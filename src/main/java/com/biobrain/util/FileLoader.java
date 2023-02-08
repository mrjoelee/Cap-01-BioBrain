package com.biobrain.util;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Objects;

public class FileLoader {

    public static BufferedImage loadBuffered(String path){
        BufferedImage bufferedImage = null;
        try {
            bufferedImage = ImageIO.read(Objects.requireNonNull(FileLoader.class.getClassLoader().getResourceAsStream(path)));
        }
        catch (IOException e) {

            e.printStackTrace();
        }
        return bufferedImage;
    }

    public static String loadTextFile(String fileName) {
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
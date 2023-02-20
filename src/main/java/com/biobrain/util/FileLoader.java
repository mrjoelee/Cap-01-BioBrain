package com.biobrain.util;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class FileLoader {

    public static BufferedImage loadBuffered(String path) {
        try (InputStream in = FileLoader.class.getClassLoader().getResourceAsStream(path)) {
            //noinspection ConstantConditions
            return ImageIO.read(in);
        } catch (IOException e) {

            e.printStackTrace();
        }
        return null;
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
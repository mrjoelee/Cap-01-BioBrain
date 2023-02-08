package com.biobrain.util;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;
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
}
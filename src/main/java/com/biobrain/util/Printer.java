package com.biobrain.util;

import com.biobrain.app.BioBrainApp;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Objects;

public class Printer {


    public static void printFile(String fileName) {
        try (BufferedReader buffer = new BufferedReader(new InputStreamReader(Objects.requireNonNull(Printer.class.getClassLoader().getResourceAsStream(fileName))))) {
            buffer.lines().forEach(System.out::println);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
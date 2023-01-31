package com.biobrain;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.*;
import java.lang.reflect.Type;
import java.util.List;
import java.util.Scanner;


public class UserInput {
    private List<String> commands;
    private List<String> objects;

    public static void WordCommands(List<String> wordList) throws IOException {
        String noun;
        String verb;

        Gson gson = new Gson();
        Type userInput = new TypeToken<List<UserInput>>() {
        }.getType();

        try (Reader reader = new FileReader("src/main/resources/jsonFiles/userInput.json")) {
            userInput = gson.<Type>fromJson(reader, userInput);

            if (wordList.size() != 2) {
                System.out.println("Please enter a valid command of two words)");
            } else {
                verb = wordList.get(0);
                noun = wordList.get(1);
                if (!userInput.equals(verb)) {
                    System.out.println(verb + "not on list");
                }
                if (!userInput.equals(noun)) {
                    System.out.println(noun + "not on list");
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) throws IOException {
        try (Reader reader = new InputStreamReader(UserInput.class.getClassLoader().getResourceAsStream("jsonFiles/userInput.json"))) {
            Gson gson = new Gson();
            UserInput input = gson.fromJson(reader, UserInput.class);
            System.out.println("Choose a command: ");
            Scanner scanner = new Scanner(System.in);
            String userInput = scanner.nextLine().trim();
            boolean isValid = false;

            // Validate command
            for (String verb : input.commands) {
                if (userInput.startsWith(verb)) {
                    isValid = true;
                    break;
                }
            }
            if (!isValid) {
                System.out.printf(" Invalid verb: %s", userInput);
            }
            isValid = false;


            for (String noun : input.objects) {
                if (!userInput.endsWith(noun)) {
                    isValid = true;
                    break;
                }
            }
            if (!isValid) {
                System.out.printf("Invalid noun: %s", userInput);
            }
            System.out.println("Command is valid. " + userInput);
        }
    }
}











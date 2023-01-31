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

        try (Reader reader = new InputStreamReader(UserInput.class.getClassLoader().getResourceAsStream("jsonFiles/userInput.json"))) {
            Gson gson = new Gson();
            UserInput input = gson.fromJson(reader, UserInput.class);

            while (true) {
                System.out.println("Choose a command: ");
                Scanner scanner = new Scanner(System.in);
                String userInput = scanner.nextLine().trim();
                String[] words = userInput.split(" ");

                verb = words[0];
                noun = words[1];

                if (words.length != 2) {
                    System.out.println("Please enter a valid command of two words\n");
                } else if (!input.commands.contains(verb)) {
                    System.out.printf("%s not on list of valid commands\n", verb);

            System.out.println("Choose a command: ");
            Scanner scanner = new Scanner(System.in);
            String userInput = scanner.nextLine().trim();
//            boolean isValid = false;
//
//            // Validate command
//                for (String verb : input.commands) {
//                    if (userInput.startsWith(verb)) {
//                        isValid = true;
//                        break;
//                    }
//                }
//                if (!isValid) {
//                    System.out.printf(" Invalid command: %s", userInput);
//                    isValid = false;
//                }
//
//                for (String noun : input.objects) {
//                    if (userInput.endsWith(noun)) {
//                        isValid = true;
////                        break;
//                    }
//                }
//                if (!isValid) {
//                    System.out.printf("Invalid command: %s", userInput);
//                }
//                System.out.println(" Command is valid. " + userInput);

            String[] words = userInput.split(" ");
            if (words.length != 2) {
                System.out.println("Please enter a valid command of two words");
            } else {
                String verb = words[0];
                String noun = words[1];
                if (!input.commands.contains(verb)) {
                    System.out.printf("%s not on list of valid commands", verb);
                }
                if (!input.objects.contains(noun)) {
                    System.out.printf("\n%s not on list of valid commands", noun);
                }
                if (!input.objects.contains(noun)) {
                    System.out.printf("%s not on list of valid commands\n", noun);
                } else
                    break;
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}












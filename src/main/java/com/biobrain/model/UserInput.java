package com.biobrain.model;

import com.google.gson.Gson;


import java.io.*;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Scanner;


public class UserInput {
    private List<String> commands;
    private List<String> objects;
    public static String verb;
    public static String noun;

    public static void WordCommands(List<String> wordList) {

        //noinspection ConstantConditions
        try (Reader reader = new InputStreamReader(UserInput.class.getClassLoader().getResourceAsStream("jsonFiles/userInput.json"))) {
            Gson gson = new Gson();
            UserInput input = gson.fromJson(reader, UserInput.class);


            while (true) {
                System.out.println("\nType one of the commands above: ");
                Scanner scanner = new Scanner(System.in);
                String userInput = scanner.nextLine().trim();

                if(userInput.equalsIgnoreCase("quit")){
                    System.out.println("Thanks for playing!");
                    System.exit(0);
                    break;
                }
                String[] words = userInput.split(" ", 2);


                if(words.length < 2) {
                    System.out.println("Please enter a valid command of at least two words\n");
                } else {
                    verb = VerbSynonymList.getSynonym(words[0]);

                    // todo For Debug, prints user input, DELETE when done
                    for (Iterator<String> iter = Arrays.stream(words).iterator(); iter.hasNext(); ) {
                        System.out.println("Printing the command array: "+ iter.next());
                    }

                    noun = words[1];

                    if (!input.commands.contains(verb)) {
                        System.out.printf("%s not on list of valid commands\n", verb);
                    }
                    if (!input.objects.contains(noun)) {
                        System.out.printf("%s not on list of valid commands\n", noun);
                    } else {
                        break;
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}



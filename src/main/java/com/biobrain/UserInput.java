package com.biobrain;

import com.biobrain.Location;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.List;


public class UserInput {

    public void WordCommands(List<String> wordList) throws IOException {
        String noun;
        String verb;

        Gson gson = new Gson();
        Type userInput = new TypeToken<List<Location>>() {
        }.getType();

        try (BufferedReader reader = new BufferedReader(new FileReader("jsonFiles/userInput.json"))) {
            userInput = gson.fromJson(reader, userInput);

            if (wordList.size() != 2) {
                System.out.println("Please enter a valid command of two words)");
            } else {
                verb = wordList.get(0);
                noun = wordList.get(1);
                if (userInput.equals(verb)) {
                    System.out.println(verb + "not on list");
                }
                if (userInput.equals(noun)) {
                    System.out.println(noun + "not on list");
                }
            }
        }catch (IOException e) {
            e.printStackTrace();
        }
    }
}






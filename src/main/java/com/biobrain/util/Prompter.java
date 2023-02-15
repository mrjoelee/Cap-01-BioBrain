package com.biobrain.util;

import java.util.Scanner;

public class Prompter {
    private final Scanner scanner;

    public Prompter() {
        //self-sufficient creates its own Scanner. how cute.
        this.scanner = new Scanner(System.in);
    }

    public String prompt(String message){
        System.out.println(message);
        System.out.print("> ");
        return getInput();
    }

    public String prompt(String message, String regex, String error){
        String userInput = null;
        boolean isValid = false;

        while(true){
            System.out.println(message);
            System.out.print("> ");
            userInput = getInput();
            isValid = userInput.matches(regex);

            if(isValid){
                break;
            }
            System.out.println(error);
        }
        return userInput;
    }

    private String getInput(){
        return this.scanner.nextLine().toUpperCase().trim();
    }
}
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;

public class UserInput {
    public static void WordCommands(List<String> wordList) {
        String noun;
        String verb;

        List<String> commands = new ArrayList<>(Arrays.asList("pick up", "go", "punch", "kick",
                "use", "run", "walk", "restore"));
        List<String> objects = new ArrayList<>(Arrays.asList("north", "south", "east", "west",
                "m4", "neutralizer", "potion"));

        if (wordList.size() != 2) {
            System.out.println("Please enter a valid command of two words)");
        } else {
            verb = wordList.get(0);
            noun = wordList.get(1);
            if (commands.contains(verb)) {
                System.out.println(verb + "not on list");
            }
            if (commands.contains(noun)) {
                System.out.println(noun + "not on list");
            }
        }
    }
}





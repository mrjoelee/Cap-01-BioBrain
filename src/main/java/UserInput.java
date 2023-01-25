import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class UserInput {
    public static void wordCommands(List<String> wordlist){
        String noun;
        String verb;

        List<String> commands = new ArrayList<>(Arrays.asList("pick up", "go", "punch", "kick",
                "use", "run", "walk", "restore"));
        List<String> objects = new ArrayList<>(Arrays.asList("north", "south", "east", "west",
                "m4", "neutralizer","potion" ));
    }

}


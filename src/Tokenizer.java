import java.util.ArrayList;

/**
 * The Tokenizer maintains all possible symbols and key words in the Boaz language
 *
 * @author Andrei Constantin (ac2042)
 *
 */
public class Tokenizer
{
    public static ArrayList<String> tokens = new ArrayList<String>()
    {{
        add("IDENTIFIER");
        add("BEGIN");
        add("IF");
        add("INT_CONST");
        add("CHAR_CONST");
    }};
}


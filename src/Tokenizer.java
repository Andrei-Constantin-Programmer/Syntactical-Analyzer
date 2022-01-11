import java.util.ArrayList;
import java.util.List;

/**
 * The Tokenizer transforms the input code into a list of tokens in the Boaz language.
 *
 * @author Andrei Constantin (ac2042)
 *
 */
public class Tokenizer
{
    private List<Token> tokens;

    private static List<String> keywords;

    public Tokenizer(List<String> lines)
    {
        keywords = new ArrayList<>(){{add("begin"); add("char"); add("do"); add("else"); add("end"); add("fi"); add("if"); add("int"); add("od"); add("print"); add("program"); add("then"); add("while");}};
        tokenize(lines);
    }


    private void tokenize(List<String> lines)
    {

    }


    /**
     * A single token, containing the token string and the token type.
     *
     * @author Andrei Constantin (ac2042)
     */
    public static class Token
    {
        public static final int IDENTIFIER = 0;
        public static final int KEYWORD = 1;
        public static final int INTEGER = 2;
        public static final int STRING = 3;
        public static final int CHARACTER = 4;
        public static final int SYMBOL = 5;

        private String tokenString;
        private int type;

        /**
         * Constructor for the Token class. The token type must be one of the static values in the class.
         * @param tokenString The token itself
         * @param tokenType The type of token (IDENTIFIER, KEYWORD, SYMBOL, INTEGER, STRING, CHARACTER)
         */
        public Token(String tokenString, int tokenType)
        {
            if(tokenType<0 || tokenType>5)
                throw new IllegalArgumentException("The tokenType must be one of the following: IDENTIFIER, KEYWORD, INTEGER, STRING, CHARACTER, SYMBOL");
            this.tokenString = tokenString;
            this.type = tokenType;
        }

        /**
         * Get the token's type
         * @return The token's type
         */
        public int getTokenType()
        {
            return type;
        }

        /**
         * Get the token string
         * @return The token string
         */
        public String getTokenString()
        {
            return tokenString;
        }

        @Override
        public String toString() {
            return tokenString + " " + type;
        }
    }
}


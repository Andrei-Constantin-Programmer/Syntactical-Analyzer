import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

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
    private static List<String> types;

    /**
     * Constructor for the Tokenizer
     * @param input The input code to tokenize
     */
    public Tokenizer(String input)
    {
        keywords = new ArrayList<>(){{add("begin"); add("do"); add("else"); add("end"); add("fi"); add("if"); add("od"); add("print"); add("program"); add("then"); add("while");}};
        types = new ArrayList<>(){{add("char"); add("int");}};
        tokens = new ArrayList<>();
        tokenize(input);
    }

    /**
     * Tokenize the given lines, putting the results in the 'tokens' list.
     * @param input The input code to tokenize
     */
    private void tokenize(String input)
    {
        String[] splitInput = input.split("\\s+");
        for(var x: splitInput)
        {
            char firstChar = x.charAt(0);
            char lastChar = x.charAt(x.length()-1);
            if(isAlphabeticOrUnderscore(firstChar))
            {
                if(keywords.contains(x))
                    tokens.add(new Token(x, Token.KEYWORD));
                else if(types.contains(x))
                    tokens.add(new Token(x, Token.TYPE));
                else
                    tokens.add(new Token(x, Token.IDENTIFIER));
            }
            else if(isNumeric(firstChar))
            {
                for(int i=1; i<x.length()-1; i++)
                    if(!isNumeric(x.charAt(i)))
                        throw new TokenizeException("Non-numeric value in numeric token.");
                tokens.add(new Token(x, Token.INTEGER));
            }
            else if(firstChar=='\'')
            {
                if(lastChar!='\'')
                    throw new TokenizeException("Missing enclosing ' symbol");
                tokens.add(new Token(x, Token.OPERAND));
            }
            else if(firstChar=='\"')
            {
                if(lastChar!='\"')
                    throw new TokenizeException("Missing enclosing \" symbol");
                tokens.add(new Token(x, Token.CHARACTER));
            }
            else
            {
                tokens.add(new Token(x, Token.SYMBOL));
            }
        }
    }

    /**
     * Get the list of tokens.
     * @return The list of tokens
     */
    public List<Token> getTokens()
    {
        return tokens;
    }


    /**
     * Checks if a character is alphabetic or underscore
     * @param c The character to check
     * @return true, if c is alphabetic or underscore, false otherwise
     */
    private boolean isAlphabeticOrUnderscore(char c)
    {
        return c>='a' && c<='z' || c>='A' && c<='Z' || c=='_';
    }

    /**
     * Checks if a character is numeric
     * @param c The character to check
     * @return true, if c is numeric, false otherwise
     */
    private boolean isNumeric(char c)
    {
        return c>='0' && c<='9';
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
        public static final int CHARACTER = 3;
        public static final int OPERAND = 4;
        public static final int SYMBOL = 5;
        public static final int TYPE = 6;

        private String tokenString;
        private int type;

        /**
         * Constructor for the Token class. The token type must be one of the static values in the class.
         * @param tokenString The token itself
         * @param tokenType The type of token
         */
        public Token(String tokenString, int tokenType)
        {
            if(tokenType<0 || tokenType>6)
                throw new IllegalArgumentException("The tokenType must be one of the static values.");
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

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (!(o instanceof Token)) return false;
            Token token = (Token) o;
            return type == token.type && tokenString.equals(token.tokenString);
        }

        @Override
        public int hashCode() {
            return Objects.hash(tokenString, type);
        }
    }


    /**
     * Exception encountered during tokenizing
     *
     * @author Andrei Constantin (ac2042)
     */
    public static class TokenizeException extends RuntimeException
    {
        public TokenizeException()
        {
            this("Tokenizing Exception encountered.");
        }

        public TokenizeException(String message)
        {
            super(message);
        }
    }
}


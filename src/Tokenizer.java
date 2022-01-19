import java.util.*;

/**
 * The Tokenizer transforms the input code into a list of tokens in the Boaz language.
 *
 * @author Andrei Constantin (ac2042)
 *
 */
public class Tokenizer
{
    private List<Token> tokens;
    private final Map<String, TokenType> keywords;

    public static final List<String> symbols = new ArrayList<>(){{add(";"); add(","); add("+"); add("-"); add("*"); add("/"); add(":="); add("&"); add("|"); add("="); add("!="); add(">"); add("<"); add("<="); add(">="); add("!"); add("("); add(")");}};

    /**
     * Constructor for the Tokenizer
     * @param input The input code to tokenize
     */
    public Tokenizer(String input)
    {
        keywords = new HashMap<>(){{put("begin", TokenType.KEYWORD); put("do", TokenType.KEYWORD); put("else", TokenType.KEYWORD); put("end", TokenType.KEYWORD); put("fi", TokenType.KEYWORD); put("if", TokenType.KEYWORD); put("od", TokenType.KEYWORD);
            put("print", TokenType.KEYWORD); put("program", TokenType.KEYWORD); put("then", TokenType.KEYWORD); put("while", TokenType.KEYWORD); put("int", TokenType.KEYWORD); put("char", TokenType.KEYWORD);}};

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
        for(int i=0; i<splitInput.length; i++)
        {
            var x = splitInput[i];
            if(isNumeric(x.charAt(0)))
                addNumber(x);
            else if(x.charAt(0)=='"')
                addChar(x);
            else if(isAlphabeticOrUnderscore(x.charAt(0)))
            {
                TokenType type = keywords.get(x);
                if(type==null)
                    type=TokenType.IDENTIFIER;
                tokens.add(new Token(x, type));
            }
            else if(symbols.contains(x))
            {
                if(i<splitInput.length-1) {
                    if (x.equals(">") && splitInput[i + 1].equals("=")) {
                        tokens.add(new Token(">=", TokenType.SYMBOL));
                        i++;
                    }
                    else if (x.equals("<") && splitInput[i + 1].equals("=")) {
                        tokens.add(new Token("<=", TokenType.SYMBOL));
                        i++;
                    }
                    else if(x.equals("!") && splitInput[i+1].equals("=")) {
                        tokens.add(new Token("!=", TokenType.SYMBOL));
                        i++;
                    }
                    else
                        tokens.add(new Token(x, TokenType.SYMBOL));
                }
                else
                    tokens.add(new Token(x, TokenType.SYMBOL));
            }
            else
                throw new TokenizeException("Unexpected token " + x);
        }
    }

    /**
     * Adds a character to the list
     * @param tokenString The token string
     */
    private void addChar(String tokenString)
    {
        if(tokenString.length()>3)
            throw new TokenizeException("Invalid character length");
        if(tokenString.charAt(tokenString.length()-1) != '"')
            throw new TokenizeException("No ending \" found for character");
        tokens.add(new Token(tokenString, TokenType.CHAR_CONST));
    }

    /**
     * Adds a number token to the list
     * @param tokenString The token string
     */
    private void addNumber(String tokenString)
    {
        try{
            int x = Integer.parseInt(tokenString);
            tokens.add(new Token(tokenString, TokenType.INT_CONST));
        }catch(Exception ex)
        {
            throw new TokenizeException("Invalid number");
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
        final String tokenString;
        final TokenType type;

        /**
         * Constructor for the Token class. The token type must be one of the static values in the class.
         * @param tokenString The token itself
         * @param tokenType The type of token
         */
        public Token(String tokenString, TokenType tokenType)
        {
            this.tokenString = tokenString;
            this.type = tokenType;
        }

        @Override
        public String toString() {
            return tokenString + " " + type;
        }
    }

    /**
     * Enum that contains all possible token types
     */
    public enum TokenType
    {
        KEYWORD, SYMBOL, IDENTIFIER, INT_CONST, CHAR_CONST
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


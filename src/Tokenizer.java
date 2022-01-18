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

    /**
     * Constructor for the Tokenizer
     * @param input The input code to tokenize
     */
    public Tokenizer(String input)
    {
        keywords = new HashMap<>(){{put("begin", TokenType.BEGIN); put("do", TokenType.DO); put("else", TokenType.ELSE); put("end", TokenType.END); put("fi", TokenType.FI); put("if", TokenType.IF); put("od", TokenType.OD); put("print", TokenType.PRINT);
            put("program", TokenType.PROGRAM); put("then", TokenType.THEN); put("while", TokenType.WHILE);}};

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
            switch (x) {
                case "(" -> tokens.add(new Token(x, TokenType.LEFT_PARENTHESIS, null));
                case ")" -> tokens.add(new Token(x, TokenType.RIGHT_PARENTHESIS, null));
                case "," -> tokens.add(new Token(x, TokenType.COMMA, null));
                case ";" -> tokens.add(new Token(x, TokenType.SEMICOLON, null));
                case "+" -> tokens.add(new Token(x, TokenType.PLUS, null));
                case "-" -> tokens.add(new Token(x, TokenType.MINUS, null));
                case "*" -> tokens.add(new Token(x, TokenType.STAR, null));
                case "/" -> tokens.add(new Token(x, TokenType.SLASH, null));
                case "!" -> tokens.add(new Token(x, TokenType.NOT, null));
                case "!=" -> tokens.add(new Token(x, TokenType.NOT_EQUAL, null));
                case "=" -> tokens.add(new Token(x, TokenType.EQUAL, null));
                case "<" -> tokens.add(new Token(x, TokenType.LESS, null));
                case "<=" -> tokens.add(new Token(x, TokenType.LESS_EQUAL, null));
                case ">" -> tokens.add(new Token(x, TokenType.GREATER, null));
                case ">=" -> tokens.add(new Token(x, TokenType.GREATER_EQUAL, null));
                case ":=" -> tokens.add(new Token(x, TokenType.ASSIGN, null));

                default -> {
                    if(isNumeric(x.charAt(0)))
                        addNumber(x);
                    else if(x.charAt(0)=='"')
                        addChar(x);
                    else if(isAlphabeticOrUnderscore(x.charAt(0)))
                    {
                        TokenType type = keywords.get(x);
                        if(type==null)
                            type=TokenType.IDENTIFIER;
                        tokens.add(new Token(x, type, null));
                    }
                    else
                        throw new TokenizeException("Unexpected token " + x);
                }
            }
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
        tokens.add(new Token(tokenString, TokenType.CHARACTER, tokenString.charAt(1)));
    }

    /**
     * Adds a number token to the list
     * @param tokenString The token string
     */
    private void addNumber(String tokenString)
    {
        try{
            int x = Integer.parseInt(tokenString);
            tokens.add(new Token(tokenString, TokenType.NUMBER, x));
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
        final Object literal;

        /**
         * Constructor for the Token class. The token type must be one of the static values in the class.
         * @param tokenString The token itself
         * @param tokenType The type of token
         */
        public Token(String tokenString, TokenType tokenType, Object literal)
        {
            this.tokenString = tokenString;
            this.type = tokenType;
            this.literal = literal;
        }

        @Override
        public String toString() {
            return tokenString + " " + type;
        }
    }

    public enum TokenType
    {
        LEFT_PARENTHESIS, RIGHT_PARENTHESIS, COMMA, MINUS, PLUS, SEMICOLON, STAR, SLASH,

        NOT, NOT_EQUAL, EQUAL, GREATER, LESS, GREATER_EQUAL, LESS_EQUAL, ASSIGN,

        IDENTIFIER, NUMBER, CHARACTER,

        PROGRAM, BEGIN, END,

        WHILE, PRINT, DO, OD, IF, THEN, ELSE, FI, INT_TYPE, CHAR_TYPE
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


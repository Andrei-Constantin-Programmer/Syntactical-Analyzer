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
    public static List<String> baseKeywords;
    public static List<String> statements;
    public static List<String> statementComponents;
    public static List<String> arithmeticOperators;
    public static List<String> booleanOperators;
    public static List<String> relationalOperators;
    public static List<String> unaryOperators;
    public static List<String> otherSymbols;

    /**
     * Constructor for the Tokenizer
     * @param input The input code to tokenize
     */
    public Tokenizer(String input)
    {
        //keywords = new ArrayList<>(){{add("begin"); add("do"); add("else"); add("end"); add("fi"); add("if"); add("od"); add("print"); add("program"); add("then"); add("while");}};
        baseKeywords = new ArrayList<>(){{add("program"); add("begin"); add("end");}};
        statements = new ArrayList<>(){{add("if"); add("while"); add("print"); }};
        statementComponents = new ArrayList<>(){{add("then"); add("else"); add("fi"); add("do"); add("od");}};
        arithmeticOperators = new ArrayList<>(){{add("+"); add("*"); add("/");}};
        booleanOperators = new ArrayList<>(){{add("&"); add("|");}};
        relationalOperators = new ArrayList<>(){{add("="); add("!="); add("<"); add(">"); add("<="); add(">=");}};
        unaryOperators = new ArrayList<>(){{add("-"); add("!");}};
        otherSymbols = new ArrayList<>(){{add(","); add(";"); add(":="); add("("); add(")"); }};

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
                if(baseKeywords.contains(x))
                    tokens.add(new Token(x, Token.BASE_KEYWORD));
                else if(statements.contains(x))
                    tokens.add(new Token(x, Token.STATEMENT));
                else if(x.equals("int") || x.equals("char"))
                    tokens.add(new Token(x, Token.TYPE));
                else
                    tokens.add(new Token(x, Token.IDENTIFIER));
            }
            else if(isNumeric(firstChar))
            {
                for(int i=1; i<x.length()-1; i++)
                    if(!isNumeric(x.charAt(i)))
                        throw new TokenizeException("Non-numeric value in numeric token");
                tokens.add(new Token(x, Token.INTEGER));
            }
            else if(firstChar=='\"')
            {
                if(lastChar!='\"')
                    throw new TokenizeException("Missing enclosing \" symbol");
                tokens.add(new Token(x, Token.CHARACTER));
            }
            else if(arithmeticOperators.contains(x))
                tokens.add(new Token(x, Token.ARITHMETIC_OPERATOR));
            else if(booleanOperators.contains(x))
                tokens.add(new Token(x, Token.BOOLEAN_OPERATOR));
            else if(relationalOperators.contains(x))
                tokens.add(new Token(x, Token.RELATIONAL_OPERATOR));
            else if(unaryOperators.contains(x))
                tokens.add(new Token(x, Token.UNARY_OPERATOR));
            else
            {
                if(otherSymbols.contains(x))
                    tokens.add(new Token(x, Token.OTHER));
                else
                    throw new TokenizeException("Invalid token " + x);
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
        public static final int BASE_KEYWORD = 1;
        public static final int STATEMENT = 2;
        public static final int INTEGER = 3;
        public static final int CHARACTER = 4;
        public static final int ARITHMETIC_OPERATOR = 5;
        public static final int BOOLEAN_OPERATOR = 6;
        public static final int RELATIONAL_OPERATOR = 7;
        public static final int UNARY_OPERATOR = 8;
        public static final int TYPE = 9;
        public static final int OTHER = 10;

        private String tokenString;
        private int type;

        /**
         * Constructor for the Token class. The token type must be one of the static values in the class.
         * @param tokenString The token itself
         * @param tokenType The type of token
         */
        public Token(String tokenString, int tokenType)
        {
            if(tokenType<0 || tokenType>10)
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

    enum TokenType
    {
        LEFT_PARENT, RIGHT_PARENT, LEFT_BRACE, RIGHT_BRACE, COMMA, MINUS, PLUS, SEMICOLON, STAR, SLASH,

        NOT, NOT_EQUAL, EQUAL, GREATER, LESS, GREATER_EQUAL, LESS_EQUAL, ASSIGN,

        IDENTIFIER, NUMBER, CHARACTER,

        PROGRAM, BEGIN, END,

        WHILE, PRINT, DO, OD, IF, THEN, ELSE, FI, INT, CHAR
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


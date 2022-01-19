import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * The Parser class. It is used for parsing the given lines of code (in the Boaz language).
 *
 * @author Andrei Constantin (ac2042)
 *
 */
public class Parser
{
    private final List<Tokenizer.Token> tokens;

    private Map<Tokenizer.Token, ExpressionType> symbolTable;

    private int currentToken;

    /**
     * The constructor of the Parser.
     * @param tokens The tokens
     */
    public Parser(List<Tokenizer.Token> tokens)
    {
        this.tokens = tokens;
    }

    /**
     * Parse the lines of code
     */
    public void parse()
    {
        parseInit();
        /*while(!getCurrentToken().tokenString.equals("END"))
        {
            var token = getCurrentToken();
            if(token.tokenString.equals("PRINT"))
            {
                currentToken++;
                parseExpression();
            }
            currentToken++;
        }*/
        currentToken++;
    }

    private ExpressionType parseExpression() {
        System.out.println("Parse expression " + getCurrentToken());

        return ExpressionType.INT;
    }

    /**
     * Check for the 'program', program identifier, 'end' and 'begin' tokens.
     * Parse variable declarations.
     */
    private void parseInit() {
        if(!tokens.get(0).tokenString.equals("program"))
        {
            throw new ParseException("The program doesn't start with the keyword 'program'");
        }
        if(!tokens.get(tokens.size()-1).tokenString.equals("end"))
            throw new ParseException("The program doesn't end with the keyword 'end'");

        currentToken = 1;
        if (getCurrentToken().type != Tokenizer.TokenType.IDENTIFIER)
            throw new ParseException("No identifier found for program");
        currentToken++;
        parseDeclarations();
        if(!getCurrentToken().tokenString.equals("begin"))
            throw new ParseException("No begin statement found");
        currentToken++;
    }

    /**
     * Parse all declarations
     */
    private void parseDeclarations()
    {
        while(!getCurrentToken().tokenString.equals("begin"))
        {
            parseDeclaration();
        }
    }

    /**
     * Parse a single declaration
     */
    private void parseDeclaration()
    {
        var token = getCurrentToken();
        if (!token.tokenString.equals("int") && !token.tokenString.equals("char"))
            throw new ParseException("Wrong token type. Expected type: TYPE (int|char)");
        currentToken++;
        if (getCurrentToken().type != Tokenizer.TokenType.IDENTIFIER)
            throw new ParseException("Wrong token type. Expected type: IDENTIFIER");
        currentToken++;
        while(getCurrentToken().tokenString.equals(","))
        {
            currentToken++;
            if (getCurrentToken().type != Tokenizer.TokenType.IDENTIFIER)
                throw new ParseException("Wrong token type. Expected type: IDENTIFIER");
            currentToken++;
        }

        if (!getCurrentToken().tokenString.equals(";"))
            throw new ParseException("Expected ;");

        currentToken++;
    }


    /**
     * Returns the current token based on the token iteration location
     * @return The token
     */
    private Tokenizer.Token getCurrentToken()
    {
        return tokens.get(currentToken);
    }

    /**
     * Exception encountered during tokenizing
     *
     * @author Andrei Constantin (ac2042)
     */
    public static class ParseException extends RuntimeException
    {
        public ParseException()
        {
            this("Parse Exception encountered.");
        }

        public ParseException(String message)
        {
            super(message);
        }
    }

    /**
     * Exception encountered during type checking
     *
     * @author Andrei Constantin (ac2042)
     */
    public static class TypeException extends RuntimeException
    {
        public TypeException()
        {
            this("Type Exception encountered.");
        }

        public TypeException(String message)
        {
            super(message);
        }
    }

    public enum ExpressionType
    {
        INT, CHAR, BOOL
    }
}

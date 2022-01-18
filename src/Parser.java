import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * The Parser class. It is used for parsing the given lines of code (in the Boaz language).
 *
 * @author Andrei Constantin (ac2042)
 *
 */
public class Parser
{
    private List<Tokenizer.Token> tokens;
    private List<Tokenizer.Token> variables;

    private int tokenIterationLocation;

    /**
     * The constructor of the Parser.
     * @param tokens The tokens
     */
    public Parser(List<Tokenizer.Token> tokens)
    {
        this.tokens = tokens;
        variables = new ArrayList<>();
    }

    /**
     * Parse the lines of code
     */
    public void parse()
    {
        if(!tokens.get(0).tokenString.equals("program"))
        {
            throw new ParseException("The program doesn't start with the keyword 'program'");
        }
        if(!tokens.get(tokens.size()-1).tokenString.equals("end"))
            throw new ParseException("The program doesn't end with the keyword 'end'");

        tokenIterationLocation = 1;
        if (currentToken().type != Tokenizer.TokenType.IDENTIFIER)
            throw new ParseException("No identifier found for program");
        tokenIterationLocation++;
        parseDeclarations();
        if(!currentToken().tokenString.equals("begin"))
            throw new ParseException("No begin statement found");
        tokenIterationLocation++;

        for(var x: variables)
            System.out.println(x);
    }

    /**
     * Parse all declarations
     */
    private void parseDeclarations()
    {
        while(!currentToken().tokenString.equals("begin"))
        {
            parseDeclaration();
        }
    }

    /**
     * Parse a single declaration
     */
    private void parseDeclaration()
    {
        var token = currentToken();
        if (token.type != Tokenizer.TokenType.INT_TYPE && token.type != Tokenizer.TokenType.CHAR_TYPE)
            throw new ParseException("Wrong token type. Expected type: TYPE (int|char)");
        tokenIterationLocation++;
        if (currentToken().type != Tokenizer.TokenType.IDENTIFIER)
            throw new ParseException("Wrong token type. Expected type: IDENTIFIER");
        variables.add(new Tokenizer.Token(currentToken().tokenString, token.type, null));
        tokenIterationLocation++;
        while(currentToken().type == Tokenizer.TokenType.COMMA)
        {
            tokenIterationLocation++;
            if (currentToken().type != Tokenizer.TokenType.IDENTIFIER)
                throw new ParseException("Wrong token type. Expected type: IDENTIFIER");
            variables.add(new Tokenizer.Token(currentToken().tokenString, token.type, null));
            tokenIterationLocation++;
        }

        if (currentToken().type != Tokenizer.TokenType.SEMICOLON)
            throw new ParseException("Expected ;");
        tokenIterationLocation++;
    }


    /**
     * Returns the current token based on the token iteration location
     * @return The token
     */
    private Tokenizer.Token currentToken()
    {
        return getToken(tokenIterationLocation);
    }

    /**
     * Returns the token in the list at the specified position
     * @param tokenIterationLocation The token iteration location
     * @return The token
     */
    private Tokenizer.Token getToken(int tokenIterationLocation)
    {
        return tokens.get(tokenIterationLocation);
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
}

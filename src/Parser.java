import java.util.ArrayList;
import java.util.List;

/**
 * The Parser class. It is used for parsing the given lines of code (in the Boaz language).
 *
 * @author Andrei Constantin (ac2042)
 *
 */
public class Parser
{
    private final List<Tokenizer.Token> tokens;

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
        /*parseInit();
        while(getCurrentToken().type != Tokenizer.TokenType.END)
        {
            var token = getCurrentToken();
            if(token.type == Tokenizer.TokenType.PRINT)
            {
                currentToken++;
                parseExpression();
            }
            currentToken++;
        }*/
        currentToken++;
        try {
            var expression = parseExpression();
            System.out.println(expression);
        }catch(Error error)
        {
            System.err.println(error);
        }
    }

    private Expression parseExpression()
    {
        System.out.println("Parse expression " + getCurrentToken());
        return parseEquality();
    }

    private Expression parseEquality()
    {
        System.out.println("Parse equality " + getCurrentToken());
        Expression expression = parseComparison();

        while(checkTypeMatch(Tokenizer.TokenType.NOT_EQUAL, Tokenizer.TokenType.EQUAL))
        {
            Tokenizer.Token operator = tokens.get(currentToken-1);
            Expression right = parseComparison();
            expression = new Expression.BinaryExpression(expression, operator, right);
        }

        return expression;
    }

    private Expression parseComparison()
    {
        System.out.println("Parse comparison " + getCurrentToken());
        Expression expression = parseTerm();

        while(checkTypeMatch(Tokenizer.TokenType.GREATER, Tokenizer.TokenType.GREATER_EQUAL, Tokenizer.TokenType.LESS, Tokenizer.TokenType.LESS_EQUAL))
        {
            Tokenizer.Token operator = tokens.get(currentToken-1);
            Expression right = parseTerm();
            expression = new Expression.BinaryExpression(expression, operator, right);
        }

        return expression;
    }

    private Expression parseTerm()
    {
        System.out.println("Parse term " + getCurrentToken());
        Expression expression = parseFactor();

        while(checkTypeMatch(Tokenizer.TokenType.MINUS, Tokenizer.TokenType.PLUS, Tokenizer.TokenType.AND, Tokenizer.TokenType.OR))
        {
            Tokenizer.Token operator = tokens.get(currentToken-1);
            Expression right = parseFactor();
            expression = new Expression.BinaryExpression(expression, operator, right);
        }

        return expression;
    }

    private Expression parseFactor()
    {
        System.out.println("Parse factor " + getCurrentToken());
        Expression expression = parseUnary();

        while(checkTypeMatch(Tokenizer.TokenType.SLASH, Tokenizer.TokenType.STAR))
        {
            var operator = tokens.get(currentToken-1);
            Expression right = parseUnary();
            expression = new Expression.BinaryExpression(expression, operator, right);
        }

        return expression;
    }

    private Expression parseUnary()
    {
        System.out.println("Parse unary " + getCurrentToken());
        if(checkTypeMatch(Tokenizer.TokenType.NOT, Tokenizer.TokenType.MINUS))
        {
            var operator = tokens.get(currentToken-1);
            Expression right = parseUnary();
            return new Expression.UnaryExpression(operator, right);
        }

        return primaryParse();
    }

    private Expression primaryParse()
    {
        System.out.println("Parse primary " + getCurrentToken());
        if(checkTypeMatch(Tokenizer.TokenType.NUMBER, Tokenizer.TokenType.CHARACTER))
            return new Expression.Literal(tokens.get(currentToken-1).literal);
        if(checkTypeMatch(Tokenizer.TokenType.LEFT_PARENTHESIS))
        {
            Expression expression = parseExpression();
            if(Tokenizer.TokenType.RIGHT_PARENTHESIS == getCurrentToken().type) {
                currentToken++;
            }
            else
                throw new ParseException("Expected ) not found");

            return new Expression.Grouping(expression);
        }

        throw new Error("Expected expression");
    }

    private boolean checkTypeMatch(Tokenizer.TokenType... types)
    {
        for(var type: types)
        {
            if (type == getCurrentToken().type) {
                currentToken++;
                return true;
            }
        }

        return false;
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
        if (token.type != Tokenizer.TokenType.INT_TYPE && token.type != Tokenizer.TokenType.CHAR_TYPE)
            throw new ParseException("Wrong token type. Expected type: TYPE (int|char)");
        currentToken++;
        if (getCurrentToken().type != Tokenizer.TokenType.IDENTIFIER)
            throw new ParseException("Wrong token type. Expected type: IDENTIFIER");
        currentToken++;
        while(getCurrentToken().type == Tokenizer.TokenType.COMMA)
        {
            currentToken++;
            if (getCurrentToken().type != Tokenizer.TokenType.IDENTIFIER)
                throw new ParseException("Wrong token type. Expected type: IDENTIFIER");
            currentToken++;
        }

        if (getCurrentToken().type != Tokenizer.TokenType.SEMICOLON)
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
}

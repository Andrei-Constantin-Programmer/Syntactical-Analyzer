import java.util.ArrayList;
import java.util.HashMap;
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

    private Map<String, Expression.ExpressionType> variableTable;
    public static final Map<String, Expression.ExpressionType> binaryOperators = new HashMap<>(){{
        put("+", Expression.ExpressionType.INT);
        put("-", Expression.ExpressionType.INT);
        put("*", Expression.ExpressionType.INT);
        put("/", Expression.ExpressionType.INT);

        put("&", Expression.ExpressionType.BOOL);
        put("|", Expression.ExpressionType.BOOL);

        put("=", Expression.ExpressionType.BOOL);
        put("!=", Expression.ExpressionType.BOOL);
        put(">", Expression.ExpressionType.BOOL);
        put(">=", Expression.ExpressionType.BOOL);
        put("<", Expression.ExpressionType.BOOL);
        put("<=", Expression.ExpressionType.BOOL);
    }};
    public static final Map<String, Expression.ExpressionType> unaryOperators = new HashMap<>(){{
        put("!", Expression.ExpressionType.BOOL);
        put("-", Expression.ExpressionType.INT);
    }};

    private int currentToken;

    /**
     * The constructor of the Parser.
     * @param tokens The tokens
     */
    public Parser(List<Tokenizer.Token> tokens)
    {
        variableTable = new HashMap<>();
        this.tokens = tokens;
    }

    /**
     * Parse the lines of code
     */
    public void parse()
    {
        parseInit();
        while(!getCurrentToken().tokenString.equals("end"))
        {
            var token = getCurrentToken();
            if(token.tokenString.equals("print"))
            {
                currentToken++;
                var x = createExpression(currentToken);
                parseExpression(createExpression(currentToken));
            }
            currentToken++;
        }
        currentToken++;
    }

    private Expression createExpression(int tokenPos)
    {
        while(!tokens.get(tokenPos).tokenString.equals(";"))
        {
            if(tokens.get(tokenPos).type == Tokenizer.TokenType.KEYWORD)
                throw new ParseException("Expected ;");
            tokenPos++;
        }
        List<Tokenizer.Token> newTokens = new ArrayList<>();
        for(int i=currentToken; i<tokenPos; i++)
        {
            newTokens.add(tokens.get(i));
        }

        return new Expression(newTokens, Expression.ExpressionType.UNKNOWN);
    }

    private Expression parseExpression(Expression expression)
    {
        if(expression.isConstant())
            return expression;
        if(expression.isVariable(variableTable))
            return expression;
        /*if(expression.hasBinaryOperator())
        {
            var l = parseExpression(expression.left);
            currentToken++;
            var symbol = getCurrentToken().tokenString;
            currentToken++;
            var r = parseExpression(expression.right);

            if(l.type!=r.type)
                throw new TypeException("Expressions have different types");
            return new Expression(expression.tokens, l, symbol, r, l.type);
        }
        if(expression.hasUnaryOperator())
        {
            var operand = parseExpression(expression.right);
            var symbol = expression.symbol;
            return new Expression(expression.expressionString, null, symbol, operand, operand.type);
        }*/

        System.out.println(expression.tokens);
        throw new Parser.ParseException("Error parsing expression");
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
        variableTable.put(getCurrentToken().tokenString, Expression.getExpressionTypeFromToken(token));
        currentToken++;
        while(getCurrentToken().tokenString.equals(","))
        {
            currentToken++;
            if (getCurrentToken().type != Tokenizer.TokenType.IDENTIFIER)
                throw new ParseException("Wrong token type. Expected type: IDENTIFIER");
            variableTable.put(getCurrentToken().tokenString, Expression.getExpressionTypeFromToken(token));
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
}

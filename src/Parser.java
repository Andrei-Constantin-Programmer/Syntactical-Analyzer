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

    private Map<String, ExpressionType> variableTable;
    public static final Map<String, ExpressionType> symbolTableBinary = new HashMap<>(){{
        put("+", ExpressionType.INT);
        put("-", ExpressionType.INT);
        put("*", ExpressionType.INT);
        put("/", ExpressionType.INT);

        put("&", ExpressionType.BOOL);
        put("|", ExpressionType.BOOL);

        put("=", ExpressionType.BOOL);
        put("!=", ExpressionType.BOOL);
        put(">", ExpressionType.BOOL);
        put(">=", ExpressionType.BOOL);
        put("<", ExpressionType.BOOL);
        put("<=", ExpressionType.BOOL);
    }};
    public static final Map<String, ExpressionType> symbolTableUnary = new HashMap<>(){{
        put("!", ExpressionType.BOOL);
        put("-", ExpressionType.NONE);
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
            System.out.println(token);
            currentToken++;

            if(token.tokenString.equals("print"))
            {
                parsePrintStatement();
            }
            else if(token.tokenString.equals("if"))
            {
                parseIfStatement();
            }
            else if(token.tokenString.equals("while"))
            {
                parseWhileStatement();
            }
            else if(variableTable.containsKey(token.tokenString))
            {
                var type = variableTable.get(token.tokenString);
                parseAssignStatement(type);
            }
            /*else
                throw new ParseException("Unknown statement " + token.tokenString);*/
        }
    }


    /**
     * Parse a print statement
     */
    private void parsePrintStatement()
    {
        var expressionType = parseExpression(currentToken, ExpressionType.NONE, true, 0, ";");
        if(expressionType==ExpressionType.NONE)
            throw new ParseException("Expression expected");
        currentToken = jumpExpression(currentToken, ";");
    }

    /**
     * Parse an assign statement
     * @param type The type of the variable
     */
    private void parseAssignStatement(ExpressionType type)
    {
        if(!tokens.get(currentToken).tokenString.equals(":="))
            throw new ParseException("Assign statement expected");
        currentToken++;
        var exprType = parseExpression(currentToken, ExpressionType.NONE, true, 0, ";");
        if(exprType != type)
            throw new TypeException("Assignment mismatching expression types");
        currentToken = jumpExpression(currentToken, ";");
    }

    /**
     * Parse a while statement
     */
    private void parseWhileStatement()
    {

    }

    /**
     * Parse an if statement
     */
    private void parseIfStatement()
    {

    }

    /**
     * Get the token position following the expression starting at token position.
     * @param tokenPosition The position of the first token in the expression
     * @param endCase Where to stop the expression
     */
    private int jumpExpression(int tokenPosition, String endCase)
    {
        while(!tokens.get(tokenPosition).tokenString.equals(endCase))
            tokenPosition++;
        return tokenPosition+1;
    }


    /**
     * Parse expression
     * @param tokenPosition The position of the first token in the expression
     * @param type The type of the expression
     * @param expectVariable true if a variable (or constant) is expected, false if a binary symbol is expected
     * @param parenthesesOpen The amount of parentheses opened
     * @param endCase Where to stop parsing the expression
     * @return The type of the expression
     */
    private ExpressionType parseExpression(int tokenPosition, ExpressionType type, boolean expectVariable, int parenthesesOpen, String endCase)
    {
        var token = tokens.get(tokenPosition);

        if(expectVariable)
        {
            if(token.tokenString.equals("("))
                return parseExpression(tokenPosition+1, type, true, parenthesesOpen+1, endCase);
            if(symbolTableUnary.containsKey(token.tokenString))
                return parseExpression(tokenPosition + 1, getBestType(type, symbolTableUnary.get(token.tokenString)), true, parenthesesOpen, endCase);

            try {
                return parseExpression(tokenPosition+1, getBestType(type, getExpressionTypeFromToken(token)), false, parenthesesOpen, endCase);
            }catch(Exception ex)
            {
                throw new ParseException("Variable " + token.tokenString + " not declared");
            }
        }
        else
        {
            if(token.tokenString.equals(endCase))
                return finishParseExpression(type, parenthesesOpen);
            if(token.tokenString.equals(")"))
                return parseExpression(tokenPosition+1, type, false, parenthesesOpen-1, endCase);
            if(symbolTableBinary.containsKey(token.tokenString))
                return parseExpression(tokenPosition + 1, getBestType(type, symbolTableBinary.get(token.tokenString)), true, parenthesesOpen, endCase);
            throw new ParseException("Expected binary symbol expected");
        }
    }

    private ExpressionType finishParseExpression(ExpressionType type, int parenthesesClose) {
        if(parenthesesClose !=0)
            throw new ParseException("Missing parenthesis");
        return getBestType(type, ExpressionType.NONE);
    }

    /**
     * Return the appropriate expression type.
     * @param baseType The base type to be overridden (or not)
     * @param type The new type that will override (or not)
     * @return The expression type
     */
    private ExpressionType getBestType(ExpressionType baseType, ExpressionType type)
    {
        if(baseType == ExpressionType.NONE)
            return type;
        if(baseType == ExpressionType.INT)
            return type != ExpressionType.NONE?type:baseType;
        if(baseType == ExpressionType.CHAR)
            return type == ExpressionType.BOOL?type:baseType;
        return baseType;
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
        variableTable.put(getCurrentToken().tokenString, getExpressionTypeFromToken(token));
        currentToken++;
        while(getCurrentToken().tokenString.equals(","))
        {
            currentToken++;
            if (getCurrentToken().type != Tokenizer.TokenType.IDENTIFIER)
                throw new ParseException("Wrong token type. Expected type: IDENTIFIER");
            variableTable.put(getCurrentToken().tokenString, getExpressionTypeFromToken(token));
            currentToken++;
        }

        if (!getCurrentToken().tokenString.equals(";"))
            throw new ParseException("Expected ;");

        currentToken++;
    }

    /**
     * Return the expression type that correlates to the token type
     * @param token The token
     * @return The expression type corresponding to the token
     */
    private ExpressionType getExpressionTypeFromToken(Tokenizer.Token token)
    {
        if(variableTable.containsKey(token.tokenString))
            return variableTable.get(token.tokenString);
        if(token.tokenString.equals("char"))
            return ExpressionType.CHAR;
        if(token.tokenString.equals("int"))
            return ExpressionType.INT;
        if(token.type == Tokenizer.TokenType.INT_CONST)
            return ExpressionType.INT;
        if(token.type == Tokenizer.TokenType.CHAR_CONST)
            return ExpressionType.CHAR;
        throw new ParseException("The given token has no corresponding expression type");
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

    /**
     * Enum that contains all possible expression types
     */
    private enum ExpressionType
    {
        INT, BOOL, CHAR, NONE
    }
}

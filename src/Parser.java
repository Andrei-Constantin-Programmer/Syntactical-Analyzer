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

    private List<Variable> variables;

    private int tokenIterationLocation;

    private static final int TYPE_INT = 0;
    private static final int TYPE_CHAR = 1;
    private static final int TYPE_BOOL = 2;
    private static final int TYPE_UNKNOWN = 3;

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
        if(!tokens.get(0).getTokenString().equals("program"))
        {
            throw new ParseException("The program doesn't start with the keyword 'program'");
        }
        if(!tokens.get(tokens.size()-1).getTokenString().equals("end"))
            throw new ParseException("The program doesn't end with the keyword 'end'");

        tokenIterationLocation = 0;

        while(tokenIterationLocation<tokens.size())
        {
            var token = currentToken();
            if(token.getTokenString().equals("program"))
            {
                tokenIterationLocation++;
                if (currentToken().getTokenType() != Tokenizer.Token.IDENTIFIER)
                    throw new ParseException("No identifier found for program");
                tokenIterationLocation++;
                parseDeclarations();
                if(!currentToken().getTokenString().equals("begin"))
                    throw new ParseException("No begin statement found");
                tokenIterationLocation++;
            }
            else if(token.getTokenString().equals("print"))
            {
                tokenIterationLocation++;
                //parseExpression(tokenIterationLocation);
            }
            else tokenIterationLocation++;
        }

    }

    //region Main Parsers
    /**
     * Parse all declarations
     */
    private void parseDeclarations()
    {
        while(!currentToken().getTokenString().equals("begin"))
        {
            parseDeclaration();
        }
    }

    /**
     * Parse a single declaration
     */
    private void parseDeclaration()
    {
        var type = currentToken();
        if (type.getTokenType() != Tokenizer.Token.TYPE)
            throw new ParseException("Wrong token type. Expected type: TYPE (int|char)");
        tokenIterationLocation++;
        if (currentToken().getTokenType() != Tokenizer.Token.IDENTIFIER)
            throw new ParseException("Wrong token type. Expected type: IDENTIFIER");
        variables.add(new Variable(currentToken().getTokenString(), type.getTokenString().equals("int")?TYPE_INT:TYPE_CHAR));
        tokenIterationLocation++;
        while(currentToken().getTokenString().equals(","))
        {
            tokenIterationLocation++;
            if (currentToken().getTokenType() != Tokenizer.Token.IDENTIFIER)
                throw new ParseException("Wrong token type. Expected type: IDENTIFIER");
            variables.add(new Variable(currentToken().getTokenString(), type.getTokenString().equals("int")?TYPE_INT:TYPE_CHAR));
            tokenIterationLocation++;
        }

        if (!currentToken().getTokenString().equals(";"))
            throw new ParseException("Expected ;");
        tokenIterationLocation++;
    }





    /**
     * Parse an exception
     *
     * @param tokenPosition The iteration location
     * @return The type of the expression
     */
    /*private int parseExpression(int tokenPosition)
    {
        int secondTokenPosition = getTerm(tokenPosition);
        if(!isBinaryOperator(tokens.get(secondTokenPosition+1)))
            return parseTerm(tokenPosition);
        else
        {
            var type1 = parseTerm(tokenPosition);
            var type2 = parseBinaryOp(secondTokenPosition+1);
            var type3 = parseTerm(secondTokenPosition+2);

            if(type1 == type2 && type2 == type3)
                return type1;
            throw new TypeException("Incompatible types");
        }
    }*/

    /**
     * Parse a term.
     * @param tokenPosition The token iteration location
     * @return The type of the term
     */
    /*private int parseTerm(int tokenPosition)
    {
        if(getToken(tokenPosition).getTokenString().equals("("))
        {
            return parseExpression(tokenPosition);
        }
        else if(isUnaryOperator(getToken(tokenPosition)))
        {
            var type1 = parseUnaryOp(tokenPosition);
            var type2 = parseTerm(tokenPosition+1);
            if(type1 != type2)
                throw new TypeException("Unary operator and term have different types");
            return type1;
        }
        else
            return convertTokenType(getToken(tokenPosition));
    }*/
    //endregion

    //region Secondary Parsers
    /**
     * Parse a binary operator
     * @param tokenPosition The token iteration location
     *
     * @return The type of the operator
     */
    /*private int parseBinaryOp(int tokenPosition)
    {
        var token = getToken(tokenPosition);
        if(!isBinaryOperator(token))
            throw new ParseException("Binary operator expected");
        if(token.getTokenType()== Tokenizer.Token.BOOLEAN_OPERATOR)
            return TYPE_BOOL;
        else if(token.getTokenType() == Tokenizer.Token.ARITHMETIC_OPERATOR)
            return TYPE_INT;
        else
            return TYPE_UNKNOWN;
    }*/

    /**
     * Parse a unary operator
     * @param tokenPosition The token iteration location
     *
     * @return The type of the operator
     */
    /*private int parseUnaryOp(int tokenPosition)
    {
        var token = getToken(tokenPosition);
        if(!isUnaryOperator(token))
            throw new ParseException("Binary operator expected");
        if(token.getTokenString().equals("!"))
            return TYPE_BOOL;
        else
            return TYPE_INT;
    }*/
    //endregion

    /**
     * Convert from token type to type.
     * @param token The token
     * @return The new type
     */
    /*private int convertTokenType(Tokenizer.Token token)
    {
        var tokenType = token.getTokenType();
        if(tokenType == Tokenizer.Token.INTEGER)
            return TYPE_INT;
        else if (tokenType == Tokenizer.Token.CHARACTER)
            return TYPE_CHAR;
        else if(tokenType == Tokenizer.Token.IDENTIFIER)
        {
            Variable found = null;
            for(int i=0; i<variables.size() && found==null; i++)
            {
                Variable var = variables.get(i);
                if(var.getIdentifier().equals(token.getTokenString()))
                    found = var;
            }

            if(found == null)
                throw new ParseException("Undeclared identifier: " + token.getTokenString());

            return found.varType;
        }
        else
            throw new TypeException("Could not convert from token type");
    }*/

    /**
     * Returns the end of the term starting at the given position
     * @param tokenPosition The iteration location
     * @return The position of the final token in the term
     */
    /*private int getTerm(int tokenPosition)
    {
        if(!getToken(tokenPosition).getTokenString().equals("("))
        {
            if(!isUnaryOperator(getToken(tokenPosition)))
                return tokenPosition;
            else
                return tokenPosition+1;
        }
        while(!getToken(tokenPosition).getTokenString().equals(")"))
            tokenPosition++;
        return tokenPosition;
    }*/

    /**
     * Checks whether the token is a binary operator or not
     * @param token The token to be checked
     * @return true if the token is a binary operator, false otherwise
     */
    /*private boolean isBinaryOperator(Tokenizer.Token token)
    {
        if(Tokenizer.arithmeticOperators.contains(token.getTokenString()))
            return true;
        if(Tokenizer.booleanOperators.contains(token.getTokenString()))
            return true;
        if(Tokenizer.relationalOperators.contains(token.getTokenString()))
            return true;
        return false;
    }*/

    /**
     * Checks whether the token is an unary operator or not
     * @param token The token to be checked
     * @return true if the token is an unary operator, false otherwise
     */
    /*private boolean isUnaryOperator(Tokenizer.Token token)
    {
        return Tokenizer.unaryOperators.contains(token.getTokenString());
    }*/


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
     * A single variable, containing its identifier and its type (INT or CHAR)
     */
    private static class Variable
    {
        private String identifier;
        private int varType;

        /**
         * Constructor for the Variable class. The variable type must be one of the static values in the class.
         * @param identifier The identifier
         * @param varType The variable type
         */
        public Variable(String identifier, int varType)
        {
            if(varType<0 || varType>1)
                throw new IllegalArgumentException("The varType must be one of the static values.");
            this.identifier = identifier;
            this.varType = varType;
        }

        public String getIdentifier()
        {
            return identifier;
        }

        public int getVarType()
        {
            return varType;
        }

        @Override
        public String toString() {
            return identifier + " " + varType;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (!(o instanceof Variable)) return false;
            Variable variable = (Variable) o;
            return varType == variable.varType && identifier.equals(variable.identifier);
        }

        @Override
        public int hashCode() {
            return Objects.hash(identifier, varType);
        }
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

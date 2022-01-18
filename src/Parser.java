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
    /*public void parse()
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

    }*/

    //region Main Parsers
    /**
     * Parse all declarations
     */
    /*private void parseDeclarations()
    {
        while(!currentToken().getTokenString().equals("begin"))
        {
            parseDeclaration();
        }
    }*/

    /**
     * Parse a single declaration
     */
    /*private void parseDeclaration()
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

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
        if(!tokens.get(0).getTokenString().equals("program"))
        {
            throw new ParseException("The program doesn't start with the keyword 'program'");
        }
        if(!tokens.get(tokens.size()-1).getTokenString().equals("end"))
            throw new ParseException("The program doesn't end with the keyword 'end'");

        tokenIterationLocation = 0;

        /*while(tokenIterationLocation<tokens.size())
        {
            System.out.println("While tokenIterationLocation<tokens.size()");*/
            if(tokens.get(tokenIterationLocation).getTokenString().equals("program"))
            {
                if (tokens.get(tokenIterationLocation + 1).getTokenType() != Tokenizer.Token.IDENTIFIER)
                    throw new ParseException("No identifier found for program");
                tokenIterationLocation+=2;
                parseDeclarations();
                if(!tokens.get(tokenIterationLocation).getTokenString().equals("begin"))
                    throw new ParseException("No begin statement found");
            }
        //}
    }

    /**
     * Parse all declarations
     */
    private void parseDeclarations()
    {
        while(!tokens.get(tokenIterationLocation).getTokenString().equals("begin"))
        {
            parseDeclaration();
        }
    }

    /**
     * Parse a single declaration
     */
    private void parseDeclaration()
    {
        if (tokens.get(tokenIterationLocation).getTokenType() != Tokenizer.Token.TYPE)
            throw new ParseException("Wrong token type. Expected type: TYPE (int|char)");
        tokenIterationLocation++;
        if (tokens.get(tokenIterationLocation).getTokenType() != Tokenizer.Token.IDENTIFIER)
            throw new ParseException("Wrong token type. Expected type: IDENTIFIER");
        tokenIterationLocation++;
        while(tokens.get(tokenIterationLocation).getTokenString().equals(","))
        {
            tokenIterationLocation++;
            if (tokens.get(tokenIterationLocation).getTokenType() != Tokenizer.Token.IDENTIFIER)
                throw new ParseException("Wrong token type. Expected type: IDENTIFIER");
            tokenIterationLocation++;
        }

        if (!tokens.get(tokenIterationLocation).getTokenString().equals(";"))
            throw new ParseException("Expected ;");
        tokenIterationLocation++;
    }


    /**
     * A single variable, containing its identifier and its type (INT or CHAR)
     */
    private static class Variable
    {
        private String identifier;
        private int varType;

        public static final int INT = 0;
        public static final int CHAR = 1;

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
}

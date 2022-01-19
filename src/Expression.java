import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Expression class, containing the left-side and right-side terms and a symbol
 *
 * @author Andrei Constantin (ac2042)
 */
public class Expression
{
    public final List<Tokenizer.Token> tokens;
    public Expression left, right;
    public Tokenizer.Token symbol;
    public ExpressionType type;

    private Expression(List<Tokenizer.Token> tokens, Expression left, Tokenizer.Token symbol, Expression right, ExpressionType type)
    {
        this.tokens = tokens;
        this.left = left;
        this.symbol = symbol;
        this.right = right;
        this.type = type;
    }

    public Expression(List<Tokenizer.Token> tokens, ExpressionType type)
    {
        this.tokens = tokens;
        this.type = type;
        splitExpression();
    }

    public boolean isConstant()
    {
        if(!isSingle())
            return false;

        var type = tokens.get(0).type;
        return type == Tokenizer.TokenType.INT_CONST || type == Tokenizer.TokenType.CHAR_CONST;
    }

    public boolean isVariable(Map<String, ExpressionType> variableTable)
    {
        if(!isSingle())
            return false;
        if(tokens.get(0).type == Tokenizer.TokenType.IDENTIFIER)
        {
            return variableTable.containsKey(tokens.get(0).tokenString);
        }
        return false;
    }

    private boolean isSingle()
    {
        return tokens.size()==1;
    }

    private void splitExpression()
    {
        if(isSingle()) {
            left = new Expression(tokens.subList(0, 1), null, null, null, getExpressionTypeFromToken(tokens.get(0)));
            right = null;
            symbol = null;
            type = getExpressionTypeFromToken(tokens.get(0));
        }

        var index = 0;
        var token = tokens.get(index);
        if(token.type == Tokenizer.TokenType.IDENTIFIER || token.type == Tokenizer.TokenType.CHAR_CONST || token.type == Tokenizer.TokenType.INT_CONST)
        {
            left = new Expression(tokens.subList(index, 1), getExpressionTypeFromToken(tokens.get(index)));
            index++;
            if(!Parser.binaryOperators.containsKey(tokens.get(index).tokenString))
                throw new Parser.ParseException("Expected binary operator");
            this.symbol = tokens.get(index);

            index++;
            consume(index);
        }
        else if(token.tokenString.equals("("))
        {
            var newTokens = new ArrayList<Tokenizer.Token>();
            while(!tokens.get(index).tokenString.equals(")"))
            {
                var nextToken = tokens.get(index);
                if(nextToken.type!= Tokenizer.TokenType.INT_CONST && nextToken.type!= Tokenizer.TokenType.CHAR_CONST && nextToken.type!= Tokenizer.TokenType.IDENTIFIER && !Parser.binaryOperators.containsKey(nextToken.tokenString) && !Parser.unaryOperators.containsKey(nextToken.tokenString))
                    throw new Parser.ParseException("Expected ) not found");
                newTokens.add(tokens.get(index));
                index++;
            }
            left = new Expression(newTokens, ExpressionType.UNKNOWN);
            index++;
            if(!Parser.binaryOperators.containsKey(tokens.get(index).tokenString))
                throw new Parser.ParseException("Expected binary operator");
            this.symbol = tokens.get(index);

            index++;
            consume(index);
        }
        else if(Parser.unaryOperators.containsKey(token.tokenString))
        {
            symbol = tokens.get(index);

        }

        throw new Parser.ParseException("Expression could not be parsed");
    }

    private void consume(int index) {
        var token = tokens.get(index);
        if(token.type == Tokenizer.TokenType.IDENTIFIER || token.type == Tokenizer.TokenType.CHAR_CONST || token.type == Tokenizer.TokenType.INT_CONST)
            right = new Expression(tokens.subList(index, 1), null, null, null, getExpressionTypeFromToken(tokens.get(index)));
        else if(token.tokenString.equals("("))
        {
            var newTokens = new ArrayList<Tokenizer.Token>();
            while(!tokens.get(index).tokenString.equals(")"))
            {
                var nextToken = tokens.get(index);
                if(nextToken.type!= Tokenizer.TokenType.INT_CONST && nextToken.type!= Tokenizer.TokenType.CHAR_CONST && nextToken.type!= Tokenizer.TokenType.IDENTIFIER && !Parser.binaryOperators.containsKey(nextToken.tokenString) && !Parser.unaryOperators.containsKey(nextToken.tokenString))
                    throw new Parser.ParseException("Expected ) not found");
                newTokens.add(tokens.get(index));
                index++;
            }

            right = new Expression(newTokens, ExpressionType.UNKNOWN);
        }
    }

    public enum ExpressionType
    {
        INT, CHAR, BOOL, UNKNOWN
    }

    public static Expression.ExpressionType getExpressionTypeFromToken(Tokenizer.Token token) {

        return token.tokenString.equals("int") ? Expression.ExpressionType.INT : Expression.ExpressionType.CHAR;
    }

    public void print()
    {
        inorder(this);
    }

    private void inorder(Expression expression)
    {
        if(expression==null)
            return;
        inorder(expression.left);
        System.out.print(expression.symbol+" ");
        inorder(expression.right);
    }

    @Override
    public String toString() {
        return "Expression: " + tokens;
    }
}

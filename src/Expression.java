/**
 * The expression abstract class contains all tokens in the expression
 *
 * @author Andrei Constantin (ac2042)
 */
abstract class Expression {
    interface Visitor<R> {
        R visitBinaryExpressionExpression(BinaryExpression expression);
        R visitGroupingExpression(Grouping expression);
        R visitLiteralExpression(Literal expression);
        R visitUnaryExpressionExpression(UnaryExpression expression);
    }
    static class BinaryExpression extends Expression {
        BinaryExpression(Expression left, Tokenizer.Token operator, Expression right) {
            this.left = left;
            this.operator = operator;
            this.right = right;
        }

        @Override
        <R> R accept(Visitor<R> visitor) {
            return visitor.visitBinaryExpressionExpression(this);
        }

        final Expression left;
        final Tokenizer.Token operator;
        final Expression right;
    }
    static class Grouping extends Expression {
        Grouping(Expression expression) {
            this.expression = expression;
        }

        @Override
        <R> R accept(Visitor<R> visitor) {
            return visitor.visitGroupingExpression(this);
        }

        final Expression expression;
    }
    static class Literal extends Expression {
        Literal(Object value) {
            this.value = value;
        }

        @Override
        <R> R accept(Visitor<R> visitor) {
            return visitor.visitLiteralExpression(this);
        }

        final Object value;
    }
    static class UnaryExpression extends Expression {
        UnaryExpression(Tokenizer.Token operator, Expression right) {
            this.operator = operator;
            this.right = right;
        }

        @Override
        <R> R accept(Visitor<R> visitor) {
            return visitor.visitUnaryExpressionExpression(this);
        }

        final Tokenizer.Token operator;
        final Expression right;
    }

    abstract <R> R accept(Visitor<R> visitor);
}

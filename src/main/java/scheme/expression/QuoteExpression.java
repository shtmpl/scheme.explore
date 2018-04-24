package scheme.expression;

import scheme.Environment;
import scheme.Expression;

public class QuoteExpression implements Expression {
    public static QuoteExpression make(Expression expression) {
        return new QuoteExpression(expression);
    }


    private final Expression expression;

    private QuoteExpression(Expression expression) {
        this.expression = expression;
    }

    public Expression expression() {
        return expression;
    }

    @Override
    public Expression eval(Environment environment) {
        return expression;
    }

    @Override
    public String toString() {
        return expression.toString();
    }
}

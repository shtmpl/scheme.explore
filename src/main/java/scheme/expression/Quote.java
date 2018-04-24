package scheme.expression;

import scheme.Environment;
import scheme.Expression;

public class Quote implements Expression {
    private final Expression expression;

    public Quote(Expression expression) {
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

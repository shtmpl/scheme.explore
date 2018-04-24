package scheme.expression;

import scheme.Environment;
import scheme.Expression;

public class IntegralExpression implements Expression {
    private final Long value;

    public IntegralExpression(Long value) {
        this.value = value;
    }

    public Long value() {
        return value;
    }

    @Override
    public Expression eval(Environment environment) {
        return this;
    }

    @Override
    public String toString() {
        return String.format("%s", value);
    }
}

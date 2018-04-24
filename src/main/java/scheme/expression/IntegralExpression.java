package scheme.expression;

import scheme.Environment;
import scheme.Expression;

public class IntegralExpression implements Expression {
    public static IntegralExpression make(Long value) {
        return new IntegralExpression(value);
    }


    private final Long value;

    private IntegralExpression(Long value) {
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

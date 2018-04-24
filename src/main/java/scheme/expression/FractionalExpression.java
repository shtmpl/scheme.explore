package scheme.expression;

import scheme.Environment;
import scheme.Expression;

public class FractionalExpression implements Expression {
    public static FractionalExpression make(Double value) {
        return new FractionalExpression(value);
    }


    private final Double value;

    private FractionalExpression(Double value) {
        this.value = value;
    }

    public Double value() {
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
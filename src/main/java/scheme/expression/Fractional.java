package scheme.expression;

import scheme.Environment;
import scheme.Expression;

public class Fractional implements Expression {
    private final Double value;

    public Fractional(Double value) {
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

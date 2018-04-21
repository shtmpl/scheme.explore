package scheme.expression;

import scheme.Environment;
import scheme.Expression;

public class LiteralNumber implements Expression {
    private final Number value;

    public LiteralNumber(Number value) {
        this.value = value;
    }

    public Number value() {
        return value;
    }

    @Override
    public LiteralNumber eval(Environment environment) {
        return this;
    }

    @Override
    public String toString() {
        return String.format("%s", value);
    }
}

package scheme.expression;

import scheme.Environment;
import scheme.Expression;

public class LiteralString implements Expression {
    private final String value;

    public LiteralString(String value) {
        this.value = value;
    }

    public String value() {
        return value;
    }

    public Expression eval(Environment environment) {
        return this;
    }

    @Override
    public String toString() {
        return String.format("\"%s\"", value);
    }
}

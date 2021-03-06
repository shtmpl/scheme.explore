package scheme.expression;

import scheme.Environment;
import scheme.Expression;

public class StringExpression implements Expression {
    public static StringExpression make(String value) {
        return new StringExpression(value);
    }


    private final String value;

    private StringExpression(String value) {
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

package scheme.expression;

import scheme.Environment;
import scheme.Expression;

public class Text implements Expression {
    private final String value;

    public Text(String value) {
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

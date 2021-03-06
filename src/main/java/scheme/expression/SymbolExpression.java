package scheme.expression;

import scheme.Environment;
import scheme.Expression;

import java.util.Objects;

public class SymbolExpression implements Expression {
    public static SymbolExpression make(String name) {
        return new SymbolExpression(name);
    }


    private final String name;

    private SymbolExpression(String name) {
        this.name = name;
    }

    public String name() {
        return name;
    }

    @Override
    public Expression eval(Environment environment) {
        return environment.lookup(this);
    }

    @Override
    public boolean equals(Object other) {
        if (this == other) {
            return true;
        }

        if (other == null || getClass() != other.getClass()) {
            return false;
        }

        SymbolExpression that = (SymbolExpression) other;
        return Objects.equals(name, that.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name);
    }

    @Override
    public String toString() {
        return String.format("%s", name);
    }
}

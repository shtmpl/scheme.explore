package scheme.expression;

import scheme.Environment;
import scheme.Expression;

import java.util.Objects;

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
    public boolean equals(Object other) {
        if (this == other) return true;

        if (other == null || getClass() != other.getClass()) return false;

        FractionalExpression that = (FractionalExpression) other;
        return Objects.equals(value, that.value);
    }

    @Override
    public int hashCode() {
        return Objects.hash(value);
    }

    @Override
    public String toString() {
        return String.format("%s", value);
    }
}

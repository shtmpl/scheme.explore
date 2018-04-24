package scheme.expression;

import scheme.Environment;
import scheme.Expression;

import java.util.Objects;

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
    public boolean equals(Object other) {
        if (this == other) return true;

        if (other == null || getClass() != other.getClass()) return false;

        IntegralExpression that = (IntegralExpression) other;
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

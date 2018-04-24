package scheme.expression;

import scheme.Environment;
import scheme.Expression;

public class UnitExpression implements Expression {
    @Override
    public Expression eval(Environment environment) {
        return this;
    }

    @Override
    public String toString() {
        return "()";
    }
}

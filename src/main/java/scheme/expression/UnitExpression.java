package scheme.expression;

import scheme.Environment;
import scheme.Expression;

public class UnitExpression implements Expression {
    private static class Single {
        private static final UnitExpression INSTANCE = new UnitExpression();
    }

    public static UnitExpression make() {
        return Single.INSTANCE;
    }


    private UnitExpression() {
    }

    @Override
    public Expression eval(Environment environment) {
        return this;
    }

    @Override
    public String toString() {
        return "()";
    }
}

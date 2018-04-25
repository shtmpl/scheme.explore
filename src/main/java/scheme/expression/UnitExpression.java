package scheme.expression;

import scheme.CombinationExpression;
import scheme.Environment;
import scheme.Expression;

public class UnitExpression extends CombinationExpression {
    private static class Single {
        private static final UnitExpression INSTANCE = new UnitExpression();
    }

    public static UnitExpression make() {
        return Single.INSTANCE;
    }


    private UnitExpression() {
        super(null, null);
    }

    @Override
    public Expression car() {
        throw new UnsupportedOperationException("car is not supported");
    }

    @Override
    public CombinationExpression cdr() {
        throw new UnsupportedOperationException("cdr is not supported");
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

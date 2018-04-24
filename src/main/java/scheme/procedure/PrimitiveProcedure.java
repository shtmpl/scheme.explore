package scheme.procedure;

import scheme.Environment;
import scheme.Expression;
import scheme.Procedure;
import scheme.CombinationExpression;

public class PrimitiveProcedure implements Procedure {
    public interface Implementation {
        Expression $(CombinationExpression arguments);
    }


    private final Implementation implementation;

    public PrimitiveProcedure(Implementation implementation) {
        this.implementation = implementation;
    }

    @Override
    public Expression eval(Environment environment) {
        return this;
    }

    @Override
    public Expression apply(CombinationExpression arguments) {
        return implementation.$(arguments);
    }
}

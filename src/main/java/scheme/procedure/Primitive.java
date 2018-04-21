package scheme.procedure;

import scheme.Environment;
import scheme.Expression;
import scheme.Procedure;
import scheme.Combination;

public class Primitive implements Procedure {
    public interface Implementation {
        Expression $(Combination arguments);
    }


    private final Implementation implementation;

    public Primitive(Implementation implementation) {
        this.implementation = implementation;
    }

    @Override
    public Expression eval(Environment environment) {
        return this;
    }

    @Override
    public Expression apply(Combination arguments) {
        return implementation.$(arguments);
    }
}

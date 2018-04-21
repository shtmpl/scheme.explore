package scheme.structure;

import scheme.Core;
import scheme.Environment;
import scheme.Expression;

public class Pair implements Expression {
    private final Expression car;
    private final Expression cdr;

    public Pair(Expression car, Expression cdr) {
        this.car = car;
        this.cdr = cdr;
    }

    public Expression car() {
        return car;
    }

    public Expression cdr() {
        return cdr;
    }

    @Override
    public Expression eval(Environment environment) {
        return this;
    }

    @Override
    public String toString() {
        return String.format("(%s . %s)", car, cdr);
    }
}

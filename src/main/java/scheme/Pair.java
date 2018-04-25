package scheme;

import java.util.List;

public class Pair implements Expression {
    public static Pair make(List<Expression> expressions) {
        return new Pair(expressions.get(0), expressions.get(1));
    }


    private Expression car;
    private Expression cdr;

    protected Pair(Expression car, Expression cdr) {
        this.car = car;
        this.cdr = cdr;
    }

    public Expression car() {
        return car;
    }

    public void car(Expression expression) {
        car = expression;
    }

    public Expression cdr() {
        return cdr;
    }

    public void cdr(Expression expression) {
        cdr = expression;
    }

    @Override
    public Expression eval(Environment environment) {
        throw new UnsupportedOperationException("Evaluation is not supported");
    }

    @Override
    public String toString() {
        return String.format("(%s . %s)", car, cdr);
    }
}

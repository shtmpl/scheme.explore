package scheme.expression;

import scheme.Environment;
import scheme.Expression;
import scheme.Utilities;

public class IfExpression implements Expression {
    public static IfExpression make(Expression predicate, Expression consequent, Expression alternative) {
        return new IfExpression(predicate, consequent, alternative);
    }


    private final Expression predicate;
    private final Expression consequent;
    private final Expression alternative;

    private IfExpression(Expression predicate, Expression consequent, Expression alternative) {
        this.predicate = predicate;
        this.consequent = consequent;
        this.alternative = alternative;
    }

    public Expression predicate() {
        return predicate;
    }

    public Expression consequent() {
        return consequent;
    }

    public Expression alternative() {
        return alternative;
    }

    @Override
    public Expression eval(Environment environment) {
        return Utilities.isTrue(predicate.eval(environment))
                ? consequent.eval(environment)
                : alternative.eval(environment);
    }

    @Override
    public String toString() {
        return String.format("(if %s %s %s)", predicate, consequent, alternative);
    }
}

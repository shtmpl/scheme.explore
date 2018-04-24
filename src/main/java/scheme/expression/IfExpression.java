package scheme.expression;

import scheme.Environment;
import scheme.Expression;
import scheme.Utilities;

public class IfExpression implements Expression {
    public static class Builder {
        private Expression predicate;
        private Expression consequent;
        private Expression alternative;

        public Builder predicate(Expression predicate) {
            this.predicate = predicate;
            return this;
        }

        public Builder consequent(Expression consequent) {
            this.consequent = consequent;
            return this;
        }

        public Builder alternative(Expression alternative) {
            this.alternative = alternative;
            return this;
        }

        public IfExpression build() {
            return new IfExpression(this);
        }
    }


    private final Expression predicate;
    private final Expression consequent;
    private final Expression alternative;

    public IfExpression(Builder builder) {
        this.predicate = builder.predicate;
        this.consequent = builder.consequent;
        this.alternative = builder.alternative;
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

package scheme.expression;

import scheme.Environment;
import scheme.Expression;
import scheme.Utilities;

public class If implements Expression {
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

        public If build() {
            return new If(this);
        }
    }


    private final Expression predicate;
    private final Expression consequent;
    private final Expression alternative;

    public If(Builder builder) {
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
}

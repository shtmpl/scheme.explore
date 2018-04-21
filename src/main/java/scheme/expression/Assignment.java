package scheme.expression;

import scheme.Core;
import scheme.Environment;
import scheme.Expression;

public class Assignment implements Expression {
    public static class Builder {
        private Symbol variable;
        private Expression value;

        public Builder variable(Symbol variable) {
            this.variable = variable;
            return this;
        }

        public Builder value(Expression value) {
            this.value = value;
            return this;
        }

        public Assignment build() {
            return new Assignment(this);
        }
    }

    private final Symbol variable;
    private final Expression value;

    public Assignment(Builder builder) {
        this.variable = builder.variable;
        this.value = builder.value;
    }

    public Symbol variable() {
        return variable;
    }

    public Expression value() {
        return value;
    }

    @Override
    public Expression eval(Environment environment) {
        environment.set(variable, value.eval(environment));
        return Core.UNIT;
    }
}

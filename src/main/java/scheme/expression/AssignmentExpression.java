package scheme.expression;

import scheme.Core;
import scheme.Environment;
import scheme.Expression;

public class AssignmentExpression implements Expression {
    public static class Builder {
        private SymbolExpression variable;
        private Expression value;

        public Builder variable(SymbolExpression variable) {
            this.variable = variable;
            return this;
        }

        public Builder value(Expression value) {
            this.value = value;
            return this;
        }

        public AssignmentExpression build() {
            return new AssignmentExpression(this);
        }
    }

    private final SymbolExpression variable;
    private final Expression value;

    public AssignmentExpression(Builder builder) {
        this.variable = builder.variable;
        this.value = builder.value;
    }

    public SymbolExpression variable() {
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

    @Override
    public String toString() {
        return String.format("(set! %s %s)", variable, value);
    }
}

package scheme.expression;

import scheme.Core;
import scheme.Environment;
import scheme.Expression;

public class AssignmentExpression implements Expression {
    public static AssignmentExpression make(SymbolExpression variable, Expression value) {
        return new AssignmentExpression(variable, value);
    }


    private final SymbolExpression variable;
    private final Expression value;

    private AssignmentExpression(SymbolExpression variable, Expression value) {
        this.variable = variable;
        this.value = value;
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

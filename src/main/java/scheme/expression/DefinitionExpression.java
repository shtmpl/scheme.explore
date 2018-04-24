package scheme.expression;

import scheme.Core;
import scheme.Environment;
import scheme.Expression;

public class DefinitionExpression implements Expression {
    public static DefinitionExpression make(SymbolExpression variable, Expression value) {
        return new DefinitionExpression(variable, value);
    }


    private final SymbolExpression variable;
    private final Expression value;

    private DefinitionExpression(SymbolExpression variable, Expression value) {
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
        environment.define(variable, value.eval(environment));
        return Core.UNIT;
    }

    @Override
    public String toString() {
        return String.format("(define %s %s)", variable, value);
    }
}

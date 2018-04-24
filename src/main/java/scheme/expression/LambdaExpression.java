package scheme.expression;

import scheme.Environment;
import scheme.Expression;
import scheme.Strings;
import scheme.procedure.CompoundProcedure;

import java.util.List;

public class LambdaExpression implements Expression {
    public static LambdaExpression make(List<SymbolExpression> parameters, List<Expression> expressions) {
        return new LambdaExpression(parameters, expressions);
    }


    private final List<SymbolExpression> parameters;
    private final List<Expression> expressions;

    private LambdaExpression(List<SymbolExpression> parameters, List<Expression> expressions) {
        this.parameters = parameters;
        this.expressions = expressions;
    }

    public List<SymbolExpression> parameters() {
        return parameters;
    }

    public List<Expression> expressions() {
        return expressions;
    }

    @Override
    public Expression eval(Environment environment) {
        return CompoundProcedure.make(environment, parameters, expressions);
    }

    @Override
    public String toString() {
        return String.format("(lambda (%s) %s)", Strings.join(" ", parameters), Strings.join(" ", expressions));
    }
}

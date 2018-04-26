package scheme.expression;

import scheme.*;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class LetExpression implements Expression {
    private static <X> List<X> cons(X x, List<X> rest) {
        List<X> result = new LinkedList<>();
        result.add(x);
        result.addAll(rest);

        return result;
    }

    public static LetExpression make(List<CombinationExpression> bindings, List<Expression> expressions) {
        return new LetExpression(bindings, expressions);
    }


    private final List<CombinationExpression> bindings;
    private final List<Expression> expressions;

    private LetExpression(List<CombinationExpression> bindings, List<Expression> expressions) {
        this.bindings = bindings;
        this.expressions = expressions;
    }

    private Expression toCombinationExpression() {
        List<SymbolExpression> bindingVariables = new ArrayList<>(bindings.size());
        List<Expression> bindingExpressions = new ArrayList<>(bindings.size());
        for (CombinationExpression expression : bindings) {
            bindingVariables.add(Utilities.asSymbol(expression.car()));
            bindingExpressions.add(expression.cdr().car());
        }

        return CombinationExpression.make(cons(LambdaExpression.make(bindingVariables, expressions), bindingExpressions));
    }

    @Override
    public Expression eval(Environment environment) {
        return toCombinationExpression().eval(environment);
    }

    @Override
    public String toString() {
        return String.format("(let (%s) %s)",
                Strings.join(" ", bindings),
                Strings.join(" ", expressions));
    }
}

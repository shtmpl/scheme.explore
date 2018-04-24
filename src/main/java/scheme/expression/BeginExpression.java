package scheme.expression;

import scheme.Environment;
import scheme.Expression;
import scheme.Strings;
import scheme.Utilities;

import java.util.ArrayList;
import java.util.List;

public class BeginExpression implements Expression {
    public static BeginExpression make(List<Expression> expressions) {
        return new BeginExpression(expressions);
    }


    private final List<Expression> expressions;

    private BeginExpression(List<Expression> expressions) {
        this.expressions = expressions;
    }

    @Override
    public Expression eval(Environment environment) {
        return Utilities.mapEval(expressions, environment).get(expressions.size() - 1);
    }

    @Override
    public String toString() {
        return String.format("(begin %s)", Strings.join(" ", expressions));
    }
}

package scheme.expression;

import scheme.Environment;
import scheme.Expression;
import scheme.Strings;
import scheme.Utilities;

import java.util.ArrayList;
import java.util.List;

public class BeginExpression implements Expression {
    public static class Builder {
        private List<Expression> expressions = new ArrayList<>();

        public Builder expression(Expression expression) {
            expressions.add(expression);
            return this;
        }

        public BeginExpression build() {
            return new BeginExpression(this);
        }
    }


    private final List<Expression> expressions;

    private BeginExpression(Builder builder) {
        this.expressions = builder.expressions;
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

package scheme.expression;

import scheme.Environment;
import scheme.Expression;
import scheme.Utilities;

import java.util.ArrayList;
import java.util.List;

public class Begin implements Expression {
    public static class Builder {
        private List<Expression> expressions = new ArrayList<>();

        public Builder expression(Expression expression) {
            expressions.add(expression);
            return this;
        }

        public Begin build() {
            return new Begin(this);
        }
    }


    private final List<Expression> expressions;

    private Begin(Builder builder) {
        this.expressions = builder.expressions;
    }

    @Override
    public Expression eval(Environment environment) {
        return Utilities.mapEval(expressions, environment).get(expressions.size() - 1);
    }
}

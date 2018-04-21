package scheme;

import java.util.ArrayList;
import java.util.List;

public class Combination implements Expression {
    public static class Builder {
        private List<Expression> expressions = new ArrayList<>();

        public Builder expression(Expression expression) {
            expressions.add(expression);
            return this;
        }

        public Combination build() {
            return new Combination(this);
        }
    }


    private final List<Expression> expressions;

    private Combination(Builder builder) {
        this.expressions = builder.expressions;
    }

    public List<Expression> expressions() {
        return expressions;
    }

    @Override
    public Expression eval(Environment environment) {
        Procedure operator = Utilities.downcastToProcedure(expressions.get(0).eval(environment));

        Builder builder = new Builder();
        for (Expression expression : expressions.subList(1, expressions.size())) {
            builder.expression(expression.eval(environment));
        }

        Combination operands = builder.build();

        return operator.apply(operands);
    }

    @Override
    public String toString() {
        return String.format("(%s)", Strings.join(" ", expressions));
    }
}

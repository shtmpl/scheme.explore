package scheme;

import scheme.expression.UnitExpression;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class CombinationExpression implements Expression {
    public static CombinationExpression make(List<Expression> expressions) {
        return new CombinationExpression(expressions);
    }


    private final List<Expression> expressions;

    private CombinationExpression(List<Expression> expressions) {
        this.expressions = expressions;
    }

    public List<Expression> expressions() {
        return expressions;
    }

    public Expression car() {
        return expressions.get(0);
    }

    public Expression cdr() {
        return expressions.size() < 2
                ? UnitExpression.make()
                : CombinationExpression.make(expressions.subList(1, expressions.size()));
    }

    @Override
    public Expression eval(Environment environment) {
        Procedure operator = Utilities.asProcedure(expressions.get(0).eval(environment));

        List<Expression> evaluated = new ArrayList<>();
        for (Expression expression : expressions.subList(1, expressions.size())) {
            evaluated.add(expression.eval(environment));
        }

        return operator.apply(CombinationExpression.make(evaluated));
    }

    @Override
    public String toString() {
        return String.format("(%s)", Strings.join(" ", expressions));
    }
}

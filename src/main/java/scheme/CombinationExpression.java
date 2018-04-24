package scheme;

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

    @Override
    public Expression eval(Environment environment) {
        Procedure operator = Utilities.asProcedure(expressions.get(0).eval(environment));
        CombinationExpression operands = CombinationExpression.make(expressions
                .stream()
                .skip(1)
                .map((Expression expression) -> expression.eval(environment))
                .collect(Collectors.toList()));

        return operator.apply(operands);
    }

    @Override
    public String toString() {
        return String.format("(%s)", Strings.join(" ", expressions));
    }
}

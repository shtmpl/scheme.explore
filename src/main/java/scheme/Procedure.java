package scheme;

import scheme.expression.CombinationExpression;

public interface Procedure extends Expression {
    Expression apply(CombinationExpression arguments);
}

package scheme;

import scheme.structure.Pair;

import java.util.ArrayList;
import java.util.List;

public final class Utilities {
    public static Procedure downcastToProcedure(Expression expression) {
        if (expression instanceof Procedure) {
            return (Procedure) expression;
        }

        throw new RuntimeException(String.format("Not a procedure: %s", expression));
    }

    public static boolean isFalse(Expression expression) {
        return Core.FALSE.equals(expression);
    }

    public static boolean isTrue(Expression expression) {
        return !isFalse(expression);
    }

    public static boolean isNull(Expression expression) {
        return Core.UNIT.equals(expression);
    }

    public static boolean isPair(Expression expression) {
        return expression instanceof Pair;
    }

    public static List<Expression> mapEval(List<Expression> expressions, Environment environment) {
        List<Expression> result = new ArrayList<>(expressions.size());
        for (Expression expression : expressions) {
            result.add(expression.eval(environment));
        }

        return result;
    }
}

package scheme;

import scheme.expression.*;
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


    public static Expression newIntegral(String x) {
        return new Integral(Long.valueOf(x));
    }

    public static Expression newFractional(String x) {
        return new Fractional(Double.valueOf(x));
    }

    public static Expression newString(String x) {
        return new Text(x);
    }

    public static Expression newSymbol(String x) {
        return new Symbol(x);
    }

    public static Expression newUnit(String x) {
        return Core.UNIT;
    }

    public static Expression newCombination(List<Expression> expressions) {
        Combination.Builder result = new Combination.Builder();
        for (Expression expression : expressions) {
            result.expression(expression);
        }

        return result.build();
    }

    public static Expression newQuote(Expression expression) {
        return new Quote(expression);
    }

    public static Expression newLambda(List<Expression> expressions) {
        Lambda.Builder result = new Lambda.Builder();

        for (Expression expression : ((Combination) expressions.get(0)).expressions()) {
            result.parameter((Symbol) expression);
        }

        for (Expression expression : ((Combination) expressions.get(1)).expressions()) {
            result.expression(expression);
        }

        return result.build();
    }

    public static Expression newDefinition(List<Expression> expressions) {
        Definition.Builder result = new Definition.Builder();

        Expression variable = expressions.get(0);
        if (variable instanceof Symbol) {
            result.variable((Symbol) variable);
            result.value(expressions.get(1));
        } else if (variable instanceof Combination) {
            List<Symbol> variables = new ArrayList<>();
            for (Expression expression : ((Combination) variable).expressions()) {
                variables.add((Symbol) expression);
            }

            Lambda.Builder lambda = new Lambda.Builder();
            for (Symbol x : variables.subList(1, variables.size())) {
                lambda.parameter(x);
            }

            for (Expression expression : ((Combination) expressions.get(1)).expressions()) {
                lambda.expression(expression);
            }

            result.variable(variables.get(0));
            result.value(lambda.build());
        }

        return result.build();
    }

    public static Expression newAssignment(List<Expression> expressions) {
        Assignment.Builder result = new Assignment.Builder();
        result.variable((Symbol) expressions.get(0));
        result.value(expressions.get(1));

        return result.build();
    }

    public static Expression newIf(List<Expression> expressions) {
        If.Builder result = new If.Builder();
        result.predicate(expressions.get(0));
        result.consequent(expressions.get(1));
        result.alternative(expressions.get(2));

        return result.build();
    }

    public static Expression newBegin(List<Expression> expressions) {
        Begin.Builder result = new Begin.Builder();
        for (Expression expression : expressions) {
            result.expression(expression);
        }

        return result.build();
    }
}

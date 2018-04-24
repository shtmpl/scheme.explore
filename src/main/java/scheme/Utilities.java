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


    public static Expression newIntegralExpression(String x) {
        return new IntegralExpression(Long.valueOf(x));
    }

    public static Expression newFractionalExpression(String x) {
        return new FractionalExpression(Double.valueOf(x));
    }

    public static Expression newStringExpression(String x) {
        return new StringExpression(x);
    }

    public static Expression newSymbolExpression(String x) {
        return new SymbolExpression(x);
    }

    public static Expression newUnitExpression(String x) {
        return Core.UNIT;
    }

    public static Expression newCombinationExpression(List<Expression> expressions) {
        CombinationExpression.Builder result = new CombinationExpression.Builder();
        for (Expression expression : expressions) {
            result.expression(expression);
        }

        return result.build();
    }

    public static Expression newQuoteExpression(Expression expression) {
        return new QuoteExpression(expression);
    }

    public static Expression newLambdaExpression(List<Expression> expressions) {
        LambdaExpression.Builder result = new LambdaExpression.Builder();

        for (Expression expression : ((CombinationExpression) expressions.get(0)).expressions()) {
            result.parameter((SymbolExpression) expression);
        }

        for (Expression expression : ((CombinationExpression) expressions.get(1)).expressions()) {
            result.expression(expression);
        }

        return result.build();
    }

    public static Expression newDefinitionExpression(List<Expression> expressions) {
        DefinitionExpression.Builder result = new DefinitionExpression.Builder();

        Expression variable = expressions.get(0);
        if (variable instanceof SymbolExpression) {
            result.variable((SymbolExpression) variable);
            result.value(expressions.get(1));
        } else if (variable instanceof CombinationExpression) {
            List<SymbolExpression> variables = new ArrayList<>();
            for (Expression expression : ((CombinationExpression) variable).expressions()) {
                variables.add((SymbolExpression) expression);
            }

            LambdaExpression.Builder lambda = new LambdaExpression.Builder();
            for (SymbolExpression x : variables.subList(1, variables.size())) {
                lambda.parameter(x);
            }

            for (Expression expression : ((CombinationExpression) expressions.get(1)).expressions()) {
                lambda.expression(expression);
            }

            result.variable(variables.get(0));
            result.value(lambda.build());
        }

        return result.build();
    }

    public static Expression newAssignmentExpression(List<Expression> expressions) {
        AssignmentExpression.Builder result = new AssignmentExpression.Builder();
        result.variable((SymbolExpression) expressions.get(0));
        result.value(expressions.get(1));

        return result.build();
    }

    public static Expression newIfExpression(List<Expression> expressions) {
        IfExpression.Builder result = new IfExpression.Builder();
        result.predicate(expressions.get(0));
        result.consequent(expressions.get(1));
        result.alternative(expressions.get(2));

        return result.build();
    }

    public static Expression newBeginExpression(List<Expression> expressions) {
        BeginExpression.Builder result = new BeginExpression.Builder();
        for (Expression expression : expressions) {
            result.expression(expression);
        }

        return result.build();
    }
}

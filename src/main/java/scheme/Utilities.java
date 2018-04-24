package scheme;

import scheme.expression.*;
import scheme.structure.Pair;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public final class Utilities {
    public static SymbolExpression asSymbolExpression(Expression expression) {
        if (expression instanceof SymbolExpression) {
            return (SymbolExpression) expression;
        }

        throw new RuntimeException(String.format("Not a symbol: %s", expression));
    }

    public static CombinationExpression asCombinationExpression(Expression expression) {
        if (expression instanceof CombinationExpression) {
            return (CombinationExpression) expression;
        }

        throw new RuntimeException(String.format("Not a combination: %s", expression));
    }

    public static Procedure asProcedure(Expression expression) {
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
        return expression instanceof CombinationExpression;
    }

    public static List<Expression> mapEval(List<Expression> expressions, Environment environment) {
        List<Expression> result = new ArrayList<>(expressions.size());
        for (Expression expression : expressions) {
            result.add(expression.eval(environment));
        }

        return result;
    }


    public static Expression makeUnitExpression(String x) {
        return UnitExpression.make();
    }

    public static Expression makeIntegralExpression(String x) {
        return IntegralExpression.make(Long.valueOf(x));
    }

    public static Expression makeFractionalExpression(String x) {
        return FractionalExpression.make(Double.valueOf(x));
    }

    public static Expression makeStringExpression(String x) {
        return StringExpression.make(x);
    }

    public static Expression makeSymbolExpression(String x) {
        return SymbolExpression.make(x);
    }

    public static Expression makeCombinationExpression(List<Expression> expressions) {
        return CombinationExpression.make(expressions);
    }

    public static Expression makeQuoteExpression(Expression expression) {
        return QuoteExpression.make(expression);
    }

    public static Expression makeLambdaExpression(List<Expression> expressions) {
        return LambdaExpression.make(
                asCombinationExpression(expressions.get(0))
                        .expressions()
                        .stream()
                        .map(Utilities::asSymbolExpression)
                        .collect(Collectors.toList()),
                asCombinationExpression(expressions.get(1)).expressions());
    }

    public static Expression makeDefinitionExpression(List<Expression> expressions) {
        Expression variable = expressions.get(0);
        if (variable instanceof SymbolExpression) {
            return DefinitionExpression.make(
                    asSymbolExpression(variable),
                    expressions.get(1));
        } else if (variable instanceof CombinationExpression) {
            return DefinitionExpression.make(
                    asSymbolExpression(asCombinationExpression(variable).expressions().get(0)),
                    LambdaExpression.make(
                            asCombinationExpression(variable)
                                    .expressions()
                                    .stream()
                                    .skip(1)
                                    .map(Utilities::asSymbolExpression)
                                    .collect(Collectors.toList()),
                            asCombinationExpression(expressions.get(1)).expressions()));
        }

        throw new RuntimeException("Malformed definition");
    }

    public static Expression makeAssignmentExpression(List<Expression> expressions) {
        return AssignmentExpression.make(asSymbolExpression(expressions.get(0)), expressions.get(1));
    }

    public static Expression makeIfExpression(List<Expression> expressions) {
        return IfExpression.make(expressions.get(0), expressions.get(1), expressions.get(2));
    }

    public static Expression makeBeginExpression(List<Expression> expressions) {
        return BeginExpression.make(expressions);
    }
}

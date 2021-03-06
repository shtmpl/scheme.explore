package scheme;

import scheme.expression.*;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static java.util.Arrays.asList;

public final class Utilities {
    public static SymbolExpression asSymbol(Expression expression) {
        if (expression instanceof SymbolExpression) {
            return (SymbolExpression) expression;
        }

        throw new RuntimeException(String.format("Not a symbol: %s", expression));
    }

    public static List<SymbolExpression> mapAsSymbol(List<Expression> expressions) {
        List<SymbolExpression> result = new ArrayList<>(expressions.size());
        for (Expression expression : expressions) {
            result.add(asSymbol(expression));
        }

        return result;
    }

    public static Pair asPair(Expression expression) {
        if (expression instanceof Pair) {
            return (Pair) expression;
        }

        throw new RuntimeException(String.format("Not a pair: %s", expression));
    }

    public static CombinationExpression asCombination(Expression expression) {
        if (expression instanceof CombinationExpression) {
            return (CombinationExpression) expression;
        }

        throw new RuntimeException(String.format("Not a combination: %s", expression));
    }

    public static List<CombinationExpression> mapAsCombination(List<Expression> expressions) {
        List<CombinationExpression> result = new ArrayList<>(expressions.size());
        for (Expression expression : expressions) {
            result.add(asCombination(expression));
        }

        return result;
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

    public static boolean isList(Expression expression) {
        return expression instanceof CombinationExpression;
    }

    public static boolean isPair(Expression expression) {
        return expression instanceof CombinationExpression || expression instanceof Pair;
    }

    public static boolean isNumber(Expression expression) {
        return expression instanceof IntegralExpression || expression instanceof FractionalExpression;
    }

    public static boolean isString(Expression expression) {
        return expression instanceof StringExpression;
    }

    public static boolean isSymbol(Expression expression) {
        return expression instanceof SymbolExpression;
    }


    public static List<Expression> mapEval(List<Expression> expressions, Environment environment) {
        List<Expression> result = new ArrayList<>(expressions.size());
        for (Expression expression : expressions) {
            result.add(expression.eval(environment));
        }

        return result;
    }


    public static Expression makeUnit(String x) {
        return UnitExpression.make();
    }

    public static Expression makeIntegral(String x) {
        return IntegralExpression.make(Long.valueOf(x));
    }

    public static Expression makeFractional(String x) {
        return FractionalExpression.make(Double.valueOf(x));
    }

    public static Expression makeString(String x) {
        return StringExpression.make(x);
    }

    public static Expression makeSymbol(String x) {
        return SymbolExpression.make(x);
    }

    public static Expression makeCombination(List<Expression> expressions) {
        return CombinationExpression.make(expressions);
    }

    public static Expression makeQuote(Expression expression) {
        return QuoteExpression.make(expression);
    }

    public static Expression makeQuoteCombination(Expression expression) {
        return CombinationExpression.make(asList(SymbolExpression.make("quote"), expression));
    }

    public static Expression makeLambda(List<Expression> expressions) {
        return LambdaExpression.make(
                asCombination(expressions.get(0))
                        .expressions()
                        .stream()
                        .map(Utilities::asSymbol)
                        .collect(Collectors.toList()),
                asCombination(expressions.get(1)).expressions());
    }

    public static Expression makeDefinition(List<Expression> expressions) {
        Expression variable = expressions.get(0);
        if (variable instanceof SymbolExpression) {
            return DefinitionExpression.make(
                    asSymbol(variable),
                    expressions.get(1));
        } else if (variable instanceof CombinationExpression) {
            return DefinitionExpression.make(
                    asSymbol(asCombination(variable).expressions().get(0)),
                    LambdaExpression.make(
                            asCombination(variable)
                                    .expressions()
                                    .stream()
                                    .skip(1)
                                    .map(Utilities::asSymbol)
                                    .collect(Collectors.toList()),
                            asCombination(expressions.get(1)).expressions()));
        }

        throw new RuntimeException("Malformed definition");
    }

    public static Expression makeAssignment(List<Expression> expressions) {
        return AssignmentExpression.make(asSymbol(expressions.get(0)), expressions.get(1));
    }

    public static Expression makeBegin(List<Expression> expressions) {
        return BeginExpression.make(expressions);
    }

    public static Expression makeIf(List<Expression> expressions) {
        return IfExpression.make(expressions.get(0), expressions.get(1), expressions.get(2));
    }

    public static Expression makeCond(List<Expression> expressions) {
        return CondExpression.make(expressions.stream().map(Utilities::asCombination).collect(Collectors.toList()));
    }

    public static Expression makeLet(List<Expression> expressions) {
        return LetExpression.make(
                mapAsCombination(asCombination(expressions.get(0)).expressions()),
                asCombination(expressions.get(1)).expressions());
    }
}

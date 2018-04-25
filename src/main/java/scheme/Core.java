package scheme;

import scheme.expression.*;
import scheme.procedure.PrimitiveProcedure;

import java.util.*;

public final class Core {
    public static final Expression UNIT = UnitExpression.make();

    public static final Expression FALSE = SymbolExpression.make("false");
    public static final Expression TRUE = SymbolExpression.make("true");




    private static String toStringUnquoted(Expression expression) {
        if (expression instanceof StringExpression) {
            return ((StringExpression) expression).value();
        }

        return expression.toString();
    }

    private static Expression error(List<Expression> expressions) {
        StringBuilder result = new StringBuilder("Error: ");

        String delimiter = "";
        for (Expression expression : expressions) {
            result.append(delimiter).append(toStringUnquoted(expression));
            delimiter = " ";
        }

        throw new RuntimeException(result.toString());
    }

    public static final Procedure ERROR = PrimitiveProcedure.make(
            (CombinationExpression arguments) -> error(arguments.expressions()));


    public static final Procedure APPLY = PrimitiveProcedure.make(
            (CombinationExpression arguments) -> {
                Procedure procedure = Utilities.asProcedure(arguments.expressions().get(0));
                CombinationExpression args = (CombinationExpression) arguments.expressions().get(1);

                return procedure.apply(args);
            });


    public static final Procedure IS_NULL = PrimitiveProcedure.make(
            (CombinationExpression arguments) -> Utilities.isNull(arguments.expressions().get(0)) ? TRUE : FALSE);


    public static final Procedure IS_PAIR = PrimitiveProcedure.make(
            (CombinationExpression arguments) -> Utilities.isPair(arguments.expressions().get(0)) ? TRUE : FALSE);

    private static <X> List<X> cons(X x, List<X> rest) {
        List<X> result = new LinkedList<>();
        result.add(x);
        result.addAll(rest);

        return result;
    }

    public static final Procedure CONS = PrimitiveProcedure.make(
            (CombinationExpression arguments) -> {
                List<Expression> expressions = arguments.expressions();

                Expression car = expressions.get(0);
                Expression cdr = expressions.get(1);
                if (Utilities.isNull(cdr)) {
                    return CombinationExpression.make(cons(car, Collections.emptyList()));
                } else if (Utilities.isList(cdr)) {
                    return CombinationExpression.make(cons(car, Utilities.asCombination(cdr).expressions()));
                }

                return Pair.make(expressions);
            });

    public static final Procedure CAR = PrimitiveProcedure.make(
            (CombinationExpression arguments) -> Utilities.asPair(arguments.expressions().get(0)).car());

    public static final Procedure CDR = PrimitiveProcedure.make(
            (CombinationExpression arguments) -> Utilities.asPair(arguments.expressions().get(0)).cdr());

    public static final Procedure SET_CAR = PrimitiveProcedure.make(
            (CombinationExpression arguments) -> {
                Utilities.asPair(arguments.expressions().get(0)).car(arguments.expressions().get(1));
                return UnitExpression.make();
            });

    public static final Procedure SET_CDR = PrimitiveProcedure.make(
            (CombinationExpression arguments) -> {
                Utilities.asPair(arguments.expressions().get(0)).cdr(arguments.expressions().get(1));
                return UnitExpression.make();
            });



    public static final Procedure IS_LIST = PrimitiveProcedure.make(
            (CombinationExpression arguments) -> Utilities.isList(arguments.expressions().get(0)) ? TRUE : FALSE);

    public static final Procedure LIST = PrimitiveProcedure.make(
            (CombinationExpression arguments) -> arguments);


    private static IntegralExpression length(Expression expression) {
        if (Utilities.isNull(expression)) {
            return IntegralExpression.make((long) 0);
        } else if (Utilities.isList(expression)) {
            return IntegralExpression.make((long) Utilities.asCombination(expression).expressions().size());
        }

        throw new RuntimeException("Malformed list");
    }

    public static final Procedure LENGTH = PrimitiveProcedure.make(
            (CombinationExpression arguments) -> length(arguments.expressions().get(0)));


    private static boolean isEq(Expression first, Expression other) {
        return Objects.equals(first, other);
    }

    public static final Procedure IS_EQ = PrimitiveProcedure.make(
            (CombinationExpression arguments) -> {
                List<Expression> expressions = arguments.expressions();

                return isEq(expressions.get(0), expressions.get(1)) ? TRUE : FALSE;
            });


    public static final Procedure IS_NUMBER = PrimitiveProcedure.make(
            (CombinationExpression arguments) -> Utilities.isNumber(arguments.expressions().get(0)) ? TRUE : FALSE);

    private static Number unwrapNumber(Expression expression) {
        if (expression instanceof IntegralExpression) {
            return ((IntegralExpression) expression).value();
        } else if (expression instanceof FractionalExpression) {
            return ((FractionalExpression) expression).value();
        }

        throw new RuntimeException(String.format("Expression is not a number: %s", expression));
    }

    private static List<Number> mapUnwrapNumber(List<Expression> expressions) {
        List<Number> result = new ArrayList<>(expressions.size());
        for (Expression expression : expressions) {
            result.add(unwrapNumber(expression));
        }

        return result;
    }


    private static int compareNumbers(Expression first, Expression other) {
        Number x = unwrapNumber(first), y = unwrapNumber(other);
        if (x instanceof Long && y instanceof Long) {
            return Long.compare(x.longValue(), y.longValue());
        } else {
            return Double.compare(x.doubleValue(), y.doubleValue());
        }
    }

    public static final Procedure LESS_THAN = PrimitiveProcedure.make(
            (CombinationExpression arguments) -> {
                List<Expression> expressions = arguments.expressions();

                return compareNumbers(expressions.get(0), expressions.get(1)) < 0 ? TRUE : FALSE;
            });

    public static final Procedure LESS_THAN_OR_EQUAL_TO = PrimitiveProcedure.make(
            (CombinationExpression arguments) -> {
                List<Expression> expressions = arguments.expressions();

                return compareNumbers(expressions.get(0), expressions.get(1)) <= 0 ? TRUE : FALSE;
            });

    public static final Procedure EQUAL_TO = PrimitiveProcedure.make(
            (CombinationExpression arguments) -> {
                List<Expression> expressions = arguments.expressions();

                return compareNumbers(expressions.get(0), expressions.get(1)) == 0 ? TRUE : FALSE;
            });

    public static final Procedure GREATER_THAN_OR_EQUAL_TO = PrimitiveProcedure.make(
            (CombinationExpression arguments) -> {
                List<Expression> expressions = arguments.expressions();

                return compareNumbers(expressions.get(0), expressions.get(1)) >= 0 ? TRUE : FALSE;
            });

    public static final Procedure GREATER_THAN = PrimitiveProcedure.make(
            (CombinationExpression arguments) -> {
                List<Expression> expressions = arguments.expressions();

                return compareNumbers(expressions.get(0), expressions.get(1)) > 0 ? TRUE : FALSE;
            });


    private static Expression addNumbers(List<Expression> expressions) {
        Number result = 0L;
        for (Number number : mapUnwrapNumber(expressions)) {
            if (result instanceof Long && number instanceof Long) {
                result = result.longValue() + number.longValue();
            } else {
                result = result.doubleValue() + number.doubleValue();
            }
        }

        return result instanceof Long
                ? IntegralExpression.make(result.longValue())
                : FractionalExpression.make(result.doubleValue());
    }

    public static final Procedure ADD = PrimitiveProcedure.make(
            (CombinationExpression arguments) -> addNumbers(arguments.expressions()));

    private static Expression subtractNumbers(List<Expression> expressions) {
        if (expressions.size() < 1) {
            throw new RuntimeException("Invalid number of arguments");
        } else if (expressions.size() == 1) {
            Number result = unwrapNumber(expressions.get(0));
            return result instanceof Long
                    ? IntegralExpression.make(-result.longValue())
                    : FractionalExpression.make(-result.doubleValue());
        }

        Number result = unwrapNumber(expressions.get(0));
        for (Number number : mapUnwrapNumber(expressions.subList(1, expressions.size()))) {
            if (result instanceof Long && number instanceof Long) {
                result = result.longValue() - number.longValue();
            } else {
                result = result.doubleValue() - number.doubleValue();
            }
        }

        return result instanceof Long
                ? IntegralExpression.make(result.longValue())
                : FractionalExpression.make(result.doubleValue());
    }

    public static final Procedure SUBTRACT = PrimitiveProcedure.make(
            (CombinationExpression arguments) -> subtractNumbers(arguments.expressions()));

    private static Expression multiplyNumbers(List<Expression> expressions) {
        Number result = 1L;
        for (Number number : mapUnwrapNumber(expressions)) {
            if (result instanceof Long && number instanceof Long) {
                result = result.longValue() * number.longValue();
            } else {
                result = result.doubleValue() * number.doubleValue();
            }
        }

        return result instanceof Long
                ? IntegralExpression.make(result.longValue())
                : FractionalExpression.make(result.doubleValue());
    }

    public static final Procedure MULTIPLY = PrimitiveProcedure.make(
            (CombinationExpression arguments) -> multiplyNumbers(arguments.expressions()));

    private static Expression divideNumbers(List<Expression> expressions) {
        if (expressions.size() < 1) {
            throw new RuntimeException("Invalid number of arguments");
        }

        Number result = unwrapNumber(expressions.get(0));
        for (Number number : mapUnwrapNumber(expressions.subList(1, expressions.size()))) {
            if (number.longValue() == 0L || number.doubleValue() == 0.) {
                throw new RuntimeException("Division by zero");
            }

            result = result.doubleValue() / number.doubleValue();
        }

        return result instanceof Long
                ? IntegralExpression.make(result.longValue())
                : FractionalExpression.make(result.doubleValue());
    }

    public static final Procedure DIVIDE = PrimitiveProcedure.make(
            (CombinationExpression arguments) -> divideNumbers(arguments.expressions()));

    private static Expression sqrt(Expression expression) {
        return FractionalExpression.make(Math.sqrt(unwrapNumber(expression).doubleValue()));
    }

    public static final Procedure SQRT = PrimitiveProcedure.make(
            (CombinationExpression arguments) -> sqrt(arguments.expressions().get(0)));


    public static final Procedure IS_STRING = PrimitiveProcedure.make(
            (CombinationExpression arguments) -> Utilities.isString(arguments.expressions().get(0)) ? TRUE : FALSE);

    public static final Procedure DISPLAY = PrimitiveProcedure.make(
            (CombinationExpression arguments) -> {
                System.out.print(toStringUnquoted(arguments.expressions().get(0)));
                return UNIT;
            });

    public static final Procedure NEWLINE = PrimitiveProcedure.make(
            (CombinationExpression arguments) -> {
                System.out.println();
                return UNIT;
            });


    public static Expression eval(Expression expression, Environment environment) {
        return expression.eval(environment);
    }

    public static Expression apply(Procedure procedure, CombinationExpression args) {
        return procedure.apply(args);
    }
}

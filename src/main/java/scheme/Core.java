package scheme;

import scheme.expression.*;
import scheme.procedure.Primitive;
import scheme.structure.Pair;

import java.util.ArrayList;
import java.util.List;

public final class Core {
    public static final Expression UNIT = new Unit();

    public static final Expression FALSE = new Symbol("false");
    public static final Expression TRUE = new Symbol("true");

    public static final Procedure IS_NULL = new Primitive(new Primitive.Implementation() {
        @Override
        public Expression $(Combination arguments) {
            if (Utilities.isNull(arguments.expressions().get(0))) {
                return TRUE;
            }

            return FALSE;
        }
    });


    private static String toStringUnquoted(Expression expression) {
        if (expression instanceof Text) {
            return ((Text) expression).value();
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

    public static final Procedure ERROR = new Primitive(new Primitive.Implementation() {
        @Override
        public Expression $(Combination arguments) {
            return error(arguments.expressions());
        }
    });


    public static final Procedure APPLY = new Primitive(new Primitive.Implementation() {
        @Override
        public Expression $(Combination arguments) {
            Procedure procedure = Utilities.downcastToProcedure(arguments.expressions().get(0));
            Combination args = (Combination) arguments.expressions().get(1);

            return procedure.apply(args);
        }
    });


    private static Number unwrapNumber(Expression expression) {
        if (expression instanceof Integral) {
            return ((Integral) expression).value();
        } else if (expression instanceof Fractional) {
            return ((Fractional) expression).value();
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
                ? new Integral(result.longValue())
                : new Fractional(result.doubleValue());
    }

    public static final Procedure ADD = new Primitive(new Primitive.Implementation() {
        @Override
        public Expression $(Combination arguments) {
            return addNumbers(arguments.expressions());
        }
    });

    private static Expression subtractNumbers(List<Expression> expressions) {
        Number result = 0L;
        for (Number number : mapUnwrapNumber(expressions)) {
            if (result instanceof Long && number instanceof Long) {
                result = result.longValue() - number.longValue();
            } else {
                result = result.doubleValue() - number.doubleValue();
            }
        }

        return result instanceof Long
                ? new Integral(result.longValue())
                : new Fractional(result.doubleValue());
    }

    public static final Procedure SUBTRACT = new Primitive(new Primitive.Implementation() {
        @Override
        public Expression $(Combination arguments) {
            return subtractNumbers(arguments.expressions());
        }
    });

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
                ? new Integral(result.longValue())
                : new Fractional(result.doubleValue());
    }

    public static final Procedure MULTIPLY = new Primitive(new Primitive.Implementation() {
        @Override
        public Expression $(Combination arguments) {
            return multiplyNumbers(arguments.expressions());
        }
    });

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
//            if (result instanceof Long && number instanceof Long) {
//                result = result.longValue() / number.longValue();
//            } else {
//
//            }
        }

        return result instanceof Long
                ? new Integral(result.longValue())
                : new Fractional(result.doubleValue());
    }

    public static final Procedure DIVIDE = new Primitive(new Primitive.Implementation() {
        @Override
        public Expression $(Combination arguments) {
            return divideNumbers(arguments.expressions());
        }
    });

    private static Expression sqrt(Expression expression) {
        return new Fractional(Math.sqrt(unwrapNumber(expression).doubleValue()));
    }

    public static final Procedure SQRT = new Primitive(new Primitive.Implementation() {
        @Override
        public Expression $(Combination arguments) {
            return sqrt(arguments.expressions().get(0));
        }
    });


    public static final Procedure CONS = new Primitive(new Primitive.Implementation() {
        @Override
        public Expression $(Combination arguments) {
            Expression car = arguments.expressions().get(0);
            Expression cdr = arguments.expressions().get(1);

            return new Pair(car, cdr);
        }
    });

    public static final Procedure CAR = new Primitive(new Primitive.Implementation() {
        @Override
        public Expression $(Combination arguments) {
            Pair pair = (Pair) arguments.expressions().get(0);

            return pair.car();
        }
    });

    public static final Procedure CDR = new Primitive(new Primitive.Implementation() {
        @Override
        public Expression $(Combination arguments) {
            Pair pair = (Pair) arguments.expressions().get(0);

            return pair.cdr();
        }
    });

    public static final Procedure IS_PAIR = new Primitive(new Primitive.Implementation() {
        @Override
        public Expression $(Combination arguments) {
            if (Utilities.isPair(arguments.expressions().get(0))) {
                return TRUE;
            }

            return FALSE;
        }
    });


    public static final Procedure DISPLAY = new Primitive(new Primitive.Implementation() {
        @Override
        public Expression $(Combination arguments) {
            System.out.print(toStringUnquoted(arguments.expressions().get(0)));
            return UNIT;
        }
    });

    public static final Procedure NEWLINE = new Primitive(new Primitive.Implementation() {
        @Override
        public Expression $(Combination arguments) {
            System.out.println();
            return UNIT;
        }
    });


    public static Expression eval(Expression expression, Environment environment) {
        return expression.eval(environment);
    }

    public static Expression apply(Procedure procedure, Combination args) {
        return procedure.apply(args);
    }
}

package scheme;

import scheme.expression.LiteralNumber;
import scheme.expression.Symbol;
import scheme.expression.Unit;
import scheme.procedure.Primitive;

import java.util.ArrayList;
import java.util.List;

public final class Core {
    public static final Expression UNIT = new Unit();

    public static final Expression FALSE = new Symbol("false");
    public static final Expression TRUE = new Symbol("true");


    public static final Procedure APPLY = new Primitive(new Primitive.Implementation() {
        @Override
        public Expression $(Combination arguments) {
            Procedure procedure = Utilities.downcastToProcedure(arguments.expressions().get(0));
            Combination args = (Combination) arguments.expressions().get(1);

            return procedure.apply(args);
        }
    });


    private static boolean allLiteralNumbers(List<Expression> expressions) {
        for (Expression expression : expressions) {
            if (expression instanceof LiteralNumber) {
                continue;
            }

            return false;
        }

        return true;
    }

    private static List<Number> asNumbers(List<Expression> expressions) {
        List<Number> result = new ArrayList<>(expressions.size());
        for (Expression expression : expressions) {
            result.add(((LiteralNumber) expression).value());
        }

        return result;
    }

    private static Number addNumbers(List<Number> numbers) {
        double result = 0.;
        for (Number number : numbers) {
            result += number.doubleValue();
        }

        return result;
    }

    public static final Procedure ADD = new Primitive(new Primitive.Implementation() {
        @Override
        public Expression $(Combination arguments) {
            if (allLiteralNumbers(arguments.expressions())) {
                return new LiteralNumber(addNumbers(asNumbers(arguments.expressions())));
            }

            throw new RuntimeException(String.format("Invalid argument types: %s", arguments));
        }
    });

    private static Number subtractNumbers(List<Number> numbers) {
        double result = 0.;
        for (Number number : numbers) {
            result -= number.doubleValue();
        }

        return result;
    }

    public static final Procedure SUBTRACT = new Primitive(new Primitive.Implementation() {
        @Override
        public Expression $(Combination arguments) {
            if (allLiteralNumbers(arguments.expressions())) {
                return new LiteralNumber(subtractNumbers(asNumbers(arguments.expressions())));
            }

            throw new RuntimeException(String.format("Invalid argument types: %s", arguments));
        }
    });

    private static Number multiplyNumbers(List<Number> numbers) {
        double result = 1.;
        for (Number number : numbers) {
            result *= number.doubleValue();
        }

        return result;
    }

    public static final Procedure MULTIPLY = new Primitive(new Primitive.Implementation() {
        @Override
        public Expression $(Combination arguments) {
            if (allLiteralNumbers(arguments.expressions())) {
                return new LiteralNumber(multiplyNumbers(asNumbers(arguments.expressions())));
            }

            throw new RuntimeException(String.format("Invalid argument types: %s", arguments));
        }
    });

    private static Number divideNumbers(List<Number> numbers) {
        if (numbers.size() < 1) {
            throw new RuntimeException(String.format("Invalid number of arguments: %s", numbers));
        }

        double result = numbers.get(0).doubleValue();
        for (Number number : numbers.subList(1, numbers.size())) {
            if (number.doubleValue() == 0.) {
                throw new RuntimeException(String.format("Division by zero: %s", numbers));
            }

            result /= number.doubleValue();
        }

        return result;
    }

    public static final Procedure DIVIDE = new Primitive(new Primitive.Implementation() {
        @Override
        public Expression $(Combination arguments) {
            if (allLiteralNumbers(arguments.expressions())) {
                return new LiteralNumber(divideNumbers(asNumbers(arguments.expressions())));
            }

            throw new RuntimeException(String.format("Invalid argument types: %s", arguments));
        }
    });

    public static final Procedure SQRT = new Primitive(new Primitive.Implementation() {
        @Override
        public Expression $(Combination arguments) {
            if (allLiteralNumbers(arguments.expressions())) {
                LiteralNumber x = (LiteralNumber) arguments.expressions().get(0);
                return new LiteralNumber(Math.sqrt(x.value().doubleValue()));
            }

            throw new RuntimeException(String.format("Invalid argument types: %s", arguments));
        }
    });


    public static Expression eval(Expression expression, Environment environment) {
        return expression.eval(environment);
    }

    public static Expression apply(Procedure procedure, Combination args) {
        return procedure.apply(args);
    }
}

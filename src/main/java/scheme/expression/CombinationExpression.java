package scheme.expression;

import scheme.*;

import java.util.*;

public class CombinationExpression extends Pair {
    private static CombinationExpression makeRecursively(List<Expression> expressions) {
        if (expressions.isEmpty()) {
            return UnitExpression.make();
        }

        return new CombinationExpression(expressions.get(0), makeRecursively(expressions.subList(1, expressions.size())));
    }

    private static CombinationExpression makeIteratively(List<Expression> expressions) {
        CombinationExpression result = UnitExpression.make();

        if (expressions.isEmpty()) {
            return result;
        }

        ListIterator<Expression> it = expressions.listIterator(expressions.size());
        while (it.hasPrevious()) {
            Expression expression = it.previous();
            result = new CombinationExpression(expression, result);
        }

        return result;
    }

    public static CombinationExpression make(List<Expression> expressions) {
        return makeIteratively(expressions);
    }


    protected CombinationExpression(Expression car, CombinationExpression cdr) {
        super(car, cdr);
    }

    @Override
    public Expression car() {
        return super.car();
    }

    @Override
    public CombinationExpression cdr() {
        return (CombinationExpression) super.cdr();
    }

    public List<Expression> expressions() {
        List<Expression> result = new LinkedList<>();
        result.add(car());

        CombinationExpression rest = cdr();
        while (!Utilities.isNull(rest)) {
            result.add(rest.car());
            rest = rest.cdr();
        }

        return result;
    }

    @Override
    public Expression eval(Environment environment) {
        Procedure operator = Utilities.asProcedure(car().eval(environment));

        List<Expression> evaluated = new ArrayList<>();
        for (Expression expression : cdr().expressions()) {
            evaluated.add(expression.eval(environment));
        }

        return operator.apply(make(evaluated));
    }

    @Override
    public String toString() {
        return String.format("(%s)", Strings.join(" ", expressions()));
    }
}

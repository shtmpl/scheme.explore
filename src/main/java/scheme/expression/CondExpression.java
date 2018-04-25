package scheme.expression;

import scheme.CombinationExpression;
import scheme.Environment;
import scheme.Expression;
import scheme.Strings;

import java.util.List;

import static java.util.Arrays.asList;
import static scheme.Core.FALSE;

public class CondExpression implements Expression {
    public static CondExpression make(List<CombinationExpression> clauses) {
        return new CondExpression(clauses);
    }


    private final List<CombinationExpression> clauses;

    private CondExpression(List<CombinationExpression> clauses) {
        this.clauses = clauses;
    }

    public List<CombinationExpression> clauses() {
        return clauses;
    }

    private static boolean isElseClause(CombinationExpression clause) {
        return SymbolExpression.make("else").equals(clause.car());
    }

    private static Expression expand(List<CombinationExpression> clauses) {
        if (clauses.isEmpty()) {
           return FALSE;
        }

        CombinationExpression first = clauses.get(0);
        if (isElseClause(first)) {
            return BeginExpression.make(first.cdr().expressions());
        }

        List<CombinationExpression> rest = clauses.subList(1, clauses.size());
        return IfExpression.make(
                first.car(),
                BeginExpression.make(first.cdr().expressions()),
                expand(rest));
    }

    public Expression toIfExpression() {
        return expand(clauses);
    }

    @Override
    public Expression eval(Environment environment) {
        return toIfExpression().eval(environment);
    }

    @Override
    public String toString() {
        return String.format("(cond %s)", Strings.join(" ", clauses));
    }
}

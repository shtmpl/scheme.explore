package scheme.procedure;

import scheme.*;
import scheme.expression.CombinationExpression;
import scheme.expression.SymbolExpression;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CompoundProcedure implements Procedure {
    public static CompoundProcedure make(Environment environment,
                                         List<SymbolExpression> parameters,
                                         List<Expression> expressions) {
        return new CompoundProcedure(environment, parameters, expressions);
    }


    private final Environment environment;

    private final List<SymbolExpression> parameters;
    private final List<Expression> expressions;

    private CompoundProcedure(Environment environment,
                             List<SymbolExpression> parameters,
                             List<Expression> expressions) {
        this.environment = environment;
        this.parameters = parameters;
        this.expressions = expressions;
    }

    public Environment environment() {
        return environment;
    }

    public List<SymbolExpression> parameters() {
        return parameters;
    }

    public List<Expression> expressions() {
        return expressions;
    }

    @Override
    public Expression eval(Environment environment) {
        return null; // FIXME
    }

    private static Map<SymbolExpression, Expression> bindings(List<SymbolExpression> variables,
                                                              List<Expression> values) {
        if (variables.size() < values.size()) {
            throw new RuntimeException(String.format("Too many arguments supplied: %s %s", variables, values));
        } else if (variables.size() > values.size()) {
            throw new RuntimeException(String.format("Too few arguments supplied: %s %s", variables, values));
        }

        Map<SymbolExpression, Expression> result = new HashMap<>();
        for (int index = 0; index < variables.size(); ++index) {
            result.put(variables.get(index), values.get(index));
        }

        return result;
    }

    @Override
    public Expression apply(CombinationExpression arguments) {
        Environment extended = environment.extend(bindings(parameters, arguments.expressions()));

        Expression result = Core.UNIT;
        for (Expression expression : expressions) {
            result = expression.eval(extended);
        }

        return result;
    }
}

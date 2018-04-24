package scheme.procedure;

import scheme.*;
import scheme.expression.SymbolExpression;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CompoundProcedure implements Procedure {
    private final Environment environment;

    private final List<SymbolExpression> parameters;
    private final List<? extends Expression> body;

    public CompoundProcedure(Environment environment,
                             List<SymbolExpression> parameters,
                             List<? extends Expression> body) {
        this.environment = environment;
        this.parameters = parameters;
        this.body = body;
    }

    public Environment environment() {
        return environment;
    }

    public List<SymbolExpression> parameters() {
        return parameters;
    }

    public List<? extends Expression> body() {
        return body;
    }

    @Override
    public Expression eval(Environment environment) {
        return null; // FIXME
    }

    private static Map<SymbolExpression, Expression> bindings(List<SymbolExpression> variables,
                                                              List<? extends Expression> values) {
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
        for (Expression expression : body) {
            result = expression.eval(extended);
        }

        return result;
    }
}

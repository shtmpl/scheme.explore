package scheme.procedure;

import scheme.*;
import scheme.expression.Symbol;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Compound implements Procedure {
    private final Environment environment;

    private final List<Symbol> parameters;
    private final List<? extends Expression> body;

    public Compound(Environment environment,
                    List<Symbol> parameters,
                    List<? extends Expression> body) {
        this.environment = environment;
        this.parameters = parameters;
        this.body = body;
    }

    public Environment environment() {
        return environment;
    }

    public List<Symbol> parameters() {
        return parameters;
    }

    public List<? extends Expression> body() {
        return body;
    }

    @Override
    public Expression eval(Environment environment) {
        return null; // FIXME
    }

    private static Map<Symbol, Expression> bindings(List<Symbol> variables,
                                                    List<? extends Expression> values) {
        if (variables.size() < values.size()) {
            throw new RuntimeException(String.format("Too many arguments supplied: %s %s", variables, values));
        } else if (variables.size() > values.size()) {
            throw new RuntimeException(String.format("Too few arguments supplied: %s %s", variables, values));
        }

        Map<Symbol, Expression> result = new HashMap<>();
        for (int index = 0; index < variables.size(); ++index) {
            result.put(variables.get(index), values.get(index));
        }

        return result;
    }

    @Override
    public Expression apply(Combination arguments) {
        Environment extended = environment.extend(bindings(parameters, arguments.expressions()));

        Expression result = Core.UNIT;
        for (Expression expression : body) {
            result = expression.eval(extended);
        }

        return result;
    }
}

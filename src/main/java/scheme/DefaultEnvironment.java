package scheme;

import scheme.expression.Symbol;

import java.util.Map;

public class DefaultEnvironment implements Environment {
    private final Environment enclosing;

    private final Map<Symbol, Expression> bindings;

    public DefaultEnvironment(Environment enclosing, Map<Symbol, Expression> bindings) {
        this.enclosing = enclosing;
        this.bindings = bindings;
    }

    @Override
    public Environment extend(Map<Symbol, Expression> bindings) {
        return new DefaultEnvironment(this, bindings);
    }

    @Override
    public Expression lookup(Symbol variable) {
        if (bindings.containsKey(variable)) {
            return bindings.get(variable);
        }

        return enclosing.lookup(variable);
    }

    @Override
    public void define(Symbol variable, Expression value) {
        bindings.put(variable, value);
    }

    @Override
    public void set(Symbol variable, Expression value) {
        if (bindings.containsKey(variable)) {
            bindings.put(variable, value);
            return;
        }

        enclosing.set(variable, value);
    }
}

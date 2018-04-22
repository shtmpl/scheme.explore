package scheme;

import scheme.expression.Symbol;

import java.util.HashMap;
import java.util.Map;

public class DefaultEnvironment implements Environment {
    private static final Map<Symbol, Expression> PRIMITIVES = new HashMap<>();

    static {
        PRIMITIVES.put(new Symbol("null?"), Core.IS_NULL);

        PRIMITIVES.put(new Symbol("true"), Core.TRUE);
        PRIMITIVES.put(new Symbol("false"), Core.FALSE);

        PRIMITIVES.put(new Symbol("error"), Core.ERROR);

        PRIMITIVES.put(new Symbol("apply"), Core.APPLY);

        PRIMITIVES.put(new Symbol("cons"), Core.CONS);
        PRIMITIVES.put(new Symbol("car"), Core.CAR);
        PRIMITIVES.put(new Symbol("cdr"), Core.CDR);
        PRIMITIVES.put(new Symbol("pair?"), Core.IS_PAIR);

        PRIMITIVES.put(new Symbol("+"), Core.ADD);
        PRIMITIVES.put(new Symbol("-"), Core.SUBTRACT);
        PRIMITIVES.put(new Symbol("*"), Core.MULTIPLY);
        PRIMITIVES.put(new Symbol("/"), Core.DIVIDE);

        PRIMITIVES.put(new Symbol("sqrt"), Core.SQRT);

        PRIMITIVES.put(new Symbol("display"), Core.DISPLAY);
        PRIMITIVES.put(new Symbol("newline"), Core.NEWLINE);
    }


    private final Environment enclosing;

    private final Map<Symbol, Expression> bindings;

    public DefaultEnvironment(Environment enclosing, Map<Symbol, Expression> bindings) {
        this.enclosing = enclosing;
        this.bindings = bindings;
    }

    public DefaultEnvironment(Environment enclosing) {
        this(enclosing, PRIMITIVES);
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

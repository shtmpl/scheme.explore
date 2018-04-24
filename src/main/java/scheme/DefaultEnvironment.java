package scheme;

import scheme.expression.SymbolExpression;

import java.util.HashMap;
import java.util.Map;

public class DefaultEnvironment implements Environment {
    private static final Map<SymbolExpression, Expression> PRIMITIVES = new HashMap<>();

    static {
        PRIMITIVES.put(new SymbolExpression("null?"), Core.IS_NULL);

        PRIMITIVES.put(new SymbolExpression("true"), Core.TRUE);
        PRIMITIVES.put(new SymbolExpression("false"), Core.FALSE);

        PRIMITIVES.put(new SymbolExpression("error"), Core.ERROR);

        PRIMITIVES.put(new SymbolExpression("apply"), Core.APPLY);

        PRIMITIVES.put(new SymbolExpression("cons"), Core.CONS);
        PRIMITIVES.put(new SymbolExpression("car"), Core.CAR);
        PRIMITIVES.put(new SymbolExpression("cdr"), Core.CDR);
        PRIMITIVES.put(new SymbolExpression("pair?"), Core.IS_PAIR);

        PRIMITIVES.put(new SymbolExpression("+"), Core.ADD);
        PRIMITIVES.put(new SymbolExpression("-"), Core.SUBTRACT);
        PRIMITIVES.put(new SymbolExpression("*"), Core.MULTIPLY);
        PRIMITIVES.put(new SymbolExpression("/"), Core.DIVIDE);

        PRIMITIVES.put(new SymbolExpression("sqrt"), Core.SQRT);

        PRIMITIVES.put(new SymbolExpression("display"), Core.DISPLAY);
        PRIMITIVES.put(new SymbolExpression("newline"), Core.NEWLINE);
    }


    private final Environment enclosing;

    private final Map<SymbolExpression, Expression> bindings;

    public DefaultEnvironment(Environment enclosing, Map<SymbolExpression, Expression> bindings) {
        this.enclosing = enclosing;
        this.bindings = bindings;
    }

    public DefaultEnvironment(Environment enclosing) {
        this(enclosing, PRIMITIVES);
    }

    @Override
    public Environment extend(Map<SymbolExpression, Expression> bindings) {
        return new DefaultEnvironment(this, bindings);
    }

    @Override
    public Expression lookup(SymbolExpression variable) {
        if (bindings.containsKey(variable)) {
            return bindings.get(variable);
        }

        return enclosing.lookup(variable);
    }

    @Override
    public void define(SymbolExpression variable, Expression value) {
        bindings.put(variable, value);
    }

    @Override
    public void set(SymbolExpression variable, Expression value) {
        if (bindings.containsKey(variable)) {
            bindings.put(variable, value);
            return;
        }

        enclosing.set(variable, value);
    }
}

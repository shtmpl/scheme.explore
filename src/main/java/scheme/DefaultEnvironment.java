package scheme;

import scheme.expression.SymbolExpression;

import java.util.HashMap;
import java.util.Map;

public class DefaultEnvironment implements Environment {
    private static final Map<SymbolExpression, Expression> PRIMITIVES = new HashMap<>();

    static {
        PRIMITIVES.put(SymbolExpression.make("null?"), Core.IS_NULL);

        PRIMITIVES.put(SymbolExpression.make("true"), Core.TRUE);
        PRIMITIVES.put(SymbolExpression.make("false"), Core.FALSE);

        PRIMITIVES.put(SymbolExpression.make("error"), Core.ERROR);

        PRIMITIVES.put(SymbolExpression.make("apply"), Core.APPLY);

        PRIMITIVES.put(SymbolExpression.make("cons"), Core.CONS);
        PRIMITIVES.put(SymbolExpression.make("car"), Core.CAR);
        PRIMITIVES.put(SymbolExpression.make("cdr"), Core.CDR);
        PRIMITIVES.put(SymbolExpression.make("pair?"), Core.IS_PAIR);
        PRIMITIVES.put(SymbolExpression.make("list"), Core.LIST);

        PRIMITIVES.put(SymbolExpression.make("eq?"), Core.IS_EQ);

        PRIMITIVES.put(SymbolExpression.make("<"), Core.LESS_THAN);
        PRIMITIVES.put(SymbolExpression.make("<="), Core.LESS_THAN_OR_EQUAL_TO);
        PRIMITIVES.put(SymbolExpression.make("="), Core.EQUAL_TO);
        PRIMITIVES.put(SymbolExpression.make(">="), Core.GREATER_THAN_OR_EQUAL_TO);
        PRIMITIVES.put(SymbolExpression.make(">"), Core.GREATER_THAN);

        PRIMITIVES.put(SymbolExpression.make("+"), Core.ADD);
        PRIMITIVES.put(SymbolExpression.make("-"), Core.SUBTRACT);
        PRIMITIVES.put(SymbolExpression.make("*"), Core.MULTIPLY);
        PRIMITIVES.put(SymbolExpression.make("/"), Core.DIVIDE);

        PRIMITIVES.put(SymbolExpression.make("sqrt"), Core.SQRT);

        PRIMITIVES.put(SymbolExpression.make("display"), Core.DISPLAY);
        PRIMITIVES.put(SymbolExpression.make("newline"), Core.NEWLINE);
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

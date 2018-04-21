package scheme;

import scheme.expression.Symbol;

import java.util.Map;

public interface Environment {
    Environment EMPTY = new Environment() {
        @Override
        public Environment extend(Map<Symbol, Expression> bindings) {
            return this;
        }

        @Override
        public Expression lookup(Symbol variable) {
            throw new RuntimeException(String.format("Unbound variable: `%s`", variable));
        }

        @Override
        public void define(Symbol variable, Expression value) {
            /*NOP*/
        }

        @Override
        public void set(Symbol variable, Expression value) {
            throw new RuntimeException(String.format("Unbound variable: `%s`", variable));
        }
    };

    Environment extend(Map<Symbol, Expression> bindings);

    Expression lookup(Symbol variable);

    void define(Symbol variable, Expression value);

    void set(Symbol variable, Expression value);
}

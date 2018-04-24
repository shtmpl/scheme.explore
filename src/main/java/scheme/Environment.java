package scheme;

import scheme.expression.SymbolExpression;

import java.util.Map;

public interface Environment {
    Environment EMPTY = new Environment() {
        @Override
        public Environment extend(Map<SymbolExpression, Expression> bindings) {
            return this;
        }

        @Override
        public Expression lookup(SymbolExpression variable) {
            throw new RuntimeException(String.format("Unbound variable: `%s`", variable));
        }

        @Override
        public void define(SymbolExpression variable, Expression value) {
            /*NOP*/
        }

        @Override
        public void set(SymbolExpression variable, Expression value) {
            throw new RuntimeException(String.format("Unbound variable: `%s`", variable));
        }
    };

    Environment extend(Map<SymbolExpression, Expression> bindings);

    Expression lookup(SymbolExpression variable);

    void define(SymbolExpression variable, Expression value);

    void set(SymbolExpression variable, Expression value);
}

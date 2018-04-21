package scheme.expression;

import scheme.Environment;
import scheme.Expression;
import scheme.procedure.Compound;

import java.util.ArrayList;
import java.util.List;

public class Lambda implements Expression {
    public static class Builder {
        private List<Symbol> parameters = new ArrayList<>();
        private List<Expression> expressions = new ArrayList<>();

        public Builder parameter(Symbol parameter) {
            parameters.add(parameter);
            return this;
        }

        public Builder expression(Expression expression) {
            expressions.add(expression);
            return this;
        }

        public Lambda build() {
            return new Lambda(this);
        }
    }


    private final List<Symbol> parameters;
    private final List<? extends Expression> body;

    private Lambda(Builder builder) {
        this.parameters = builder.parameters;
        this.body = builder.expressions;
    }

    public List<Symbol> parameters() {
        return parameters;
    }

    public List<? extends Expression> body() {
        return body;
    }

    @Override
    public Expression eval(Environment environment) {
        return new Compound(environment, parameters, body);
    }
}

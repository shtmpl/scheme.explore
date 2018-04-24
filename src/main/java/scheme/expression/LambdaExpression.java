package scheme.expression;

import scheme.Environment;
import scheme.Expression;
import scheme.Strings;
import scheme.procedure.CompoundProcedure;

import java.util.ArrayList;
import java.util.List;

public class LambdaExpression implements Expression {
    public static class Builder {
        private List<SymbolExpression> parameters = new ArrayList<>();
        private List<Expression> expressions = new ArrayList<>();

        public Builder parameter(SymbolExpression parameter) {
            parameters.add(parameter);
            return this;
        }

        public Builder expression(Expression expression) {
            expressions.add(expression);
            return this;
        }

        public LambdaExpression build() {
            return new LambdaExpression(this);
        }
    }


    private final List<SymbolExpression> parameters;
    private final List<? extends Expression> body;

    private LambdaExpression(Builder builder) {
        this.parameters = builder.parameters;
        this.body = builder.expressions;
    }

    public List<SymbolExpression> parameters() {
        return parameters;
    }

    public List<? extends Expression> body() {
        return body;
    }

    @Override
    public Expression eval(Environment environment) {
        return new CompoundProcedure(environment, parameters, body);
    }

    @Override
    public String toString() {
        return String.format("(lambda (%s) %s)", Strings.join(" ", parameters), Strings.join(" ", body));
    }
}

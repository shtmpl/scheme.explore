package scheme.syntax;

import scheme.Combination;
import scheme.Core;
import scheme.Expression;
import scheme.Strings;
import scheme.expression.*;

import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.regex.Pattern;

public class Parser implements Iterable<Expression> {
    private static Number numberValueOf(String string) {
        return Double.valueOf(string);
    }

    private static String requireNext(Iterator<String> it) {
        if (!it.hasNext()) {
            throw new RuntimeException(String.format("Unexpected: `%s`", "EOF"));
        }

        return it.next();
    }

    private static String requireNext(Iterator<String> it, String required) {
        if (!it.hasNext()) {
            throw new RuntimeException(String.format("Unexpected: `%s`", "EOF"));
        }

        String x = it.next();

        if (required.equals(x)) {
            return x;
        }

        throw new RuntimeException(String.format("Unexpected: `%s`", x));
    }


    private static final Pattern PATTERN_FRACTIONAL = Pattern.compile("(\\d+\\.|\\.\\d+|\\d+\\.\\d+)");

    private static Expression parseFractional(PushbackIterator<String> it) {
        String x = requireNext(it);

        if (PATTERN_FRACTIONAL.matcher(x).matches()) {
            return new Fractional(Double.valueOf(x));
        } else {
            it.stash(x);
        }

        return null;
    }

    private static final Pattern PATTERN_INTEGRAL = Pattern.compile("\\d+");

    private static Expression parseIntegral(PushbackIterator<String> it) {
        String x = requireNext(it);

        if (PATTERN_INTEGRAL.matcher(x).matches()) {
            return new Integral(Long.valueOf(x));
        } else {
            it.stash(x);
        }

        return null;
    }

    private static final Pattern PATTERN_STRING = Pattern.compile("\".*\"", Pattern.DOTALL);

    private static Expression parseText(PushbackIterator<String> it) {
        if (it.hasNext()) {
            String x = it.next();

            if (PATTERN_STRING.matcher(x).matches()) {
                return new Text(Strings.trimAffix(x, "\""));
            } else {
                it.stash(x);
            }
        }

        return null;
    }

    private static final Pattern PATTERN_SYMBOL = Pattern.compile("[A-Za-z0-9<=>!?+\\-*/]+");

    private static Expression parseSymbol(PushbackIterator<String> it) {
        if (it.hasNext()) {
            String x = it.next();

            if (PATTERN_SYMBOL.matcher(x).matches()) {
                return new Symbol(x);
            } else {
                it.stash(x);
            }
        }

        return null;
    }

    private static Expression parseUnit(PushbackIterator<String> it) {
        return Core.UNIT;
    }

    private static Expression parseQuote(PushbackIterator<String> it) {
        if (it.hasNext()) {
            String x = it.next();

            if ("'".equals(x)) {
                if (it.hasNext()) {
                    Expression expression = parseExpression(it);

                    return new Quote(expression);
                } else {
                    throw new SyntaxException("Unexpected EOF");
                }
            } else {
                it.stash(x);
            }
        }

        return null;
    }

    private static Expression parseQuoteExpanded(PushbackIterator<String> it) {
        Expression result = parseExpression(it);

        if (it.hasNext()) {
            String x = it.next();

            if (")".equals(x)) {
                return new Quote(result);
            }
        }

        throw new RuntimeException("Unexpected EOF");
    }

    private static Expression parseLambda(PushbackIterator<String> it) {
        Lambda.Builder result = new Lambda.Builder();

        if (it.hasNext()) {
            String x = it.next();

            if ("(".equals(x)) {
                while (it.hasNext() && !")".equals(x = it.next())) {
                    it.stash(x);
                    Symbol arg = (Symbol) parseExpression(it);
                    result.parameter(arg);
                }
            }
        }

        while (it.hasNext()) {
            String x = it.next();

            if (")".equals(x)) {
                return result.build();
            } else {
                it.stash(x);
                result.expression(parseExpression(it));
            }
        }

        throw new RuntimeException("Unexpected EOF");
    }

    private static Expression parseDefinition(PushbackIterator<String> it) {
        Definition.Builder result = new Definition.Builder();

        Symbol variable = (Symbol) parseSymbol(it);
        result.variable(variable);

        Expression value = parseExpression(it);
        result.value(value);

        if (it.hasNext()) {
            String x = it.next();

            if (")".equals(x)) {
                return result.build();
            } else {
                throw new RuntimeException("Invalid syntax: define");
            }
        } else {
            throw new RuntimeException("Unexpected EOF");
        }
    }

    private static Expression parseAssignment(PushbackIterator<String> it) {
        Assignment.Builder result = new Assignment.Builder();

        Symbol variable = (Symbol) parseSymbol(it);
        result.variable(variable);

        Expression value = parseExpression(it);
        result.value(value);

        if (it.hasNext()) {
            String x = it.next();

            if (")".equals(x)) {
                return result.build();
            } else {
                throw new RuntimeException("Invalid syntax: define");
            }
        } else {
            throw new RuntimeException("Unexpected EOF");
        }
    }

    private static Expression parseIf(PushbackIterator<String> it) {
        If.Builder result = new If.Builder();

        Expression expression = parseExpression(it);
        if (expression != null) {
            result.predicate(expression);
        }

        expression = parseExpression(it);
        if (expression != null) {
            result.consequent(expression);
        }

        expression = parseExpression(it);
        if (expression != null) {
            result.alternative(expression);
        }

        if (it.hasNext()) {
            String x = it.next();

            if (")".equals(x)) {
                return result.build();
            } else {
                throw new RuntimeException("Invalid syntax: if");
            }
        } else {
            throw new RuntimeException("Unexpected EOF");
        }
    }

    private static Expression parseApplication(PushbackIterator<String> it) {
        Combination.Builder result = new Combination.Builder();

        while (it.hasNext()) {
            String x = it.next();

            if (")".equals(x)) {
                return result.build();
            } else {
                it.stash(x);
                result.expression(parseExpression(it));
            }
        }

        throw new RuntimeException("Unexpected EOF");
    }

    private static Expression parseCombination(PushbackIterator<String> it) {
        requireNext(it, "(");

        if (it.hasNext()) {
            String f = it.next();
            switch (f) {
                case ")":
                    return parseUnit(it);
                case "quote":
                    return parseQuoteExpanded(it);
                case "lambda":
                    return parseLambda(it);
                case "define":
                    return parseDefinition(it);
                case "set!":
                    return parseAssignment(it);
                case "if":
                    return parseIf(it);
                default:
                    it.stash(f);
                    return parseApplication(it);
            }
        } else {
            throw new RuntimeException("Unexpected EOF");
        }
    }

    private static Expression parseExpression(PushbackIterator<String> it) {
        if (!it.hasNext()) {
            return null;
        }

        Expression result;

        result = parseFractional(it);
        if (result != null) {
            return result;
        }

        result = parseIntegral(it);
        if (result != null) {
            return result;
        }

        result = parseText(it);
        if (result != null) {
            return result;
        }

        result = parseSymbol(it);
        if (result != null) {
            return result;
        }

        result = parseQuote(it);
        if (result != null) {
            return result;
        }

        result = parseCombination(it);
        if (result != null) {
            return result;
        }

        throw new RuntimeException(String.format("Unexpected: `%s`", "EOF"));
    }

    private final Iterable<String> lexemes;

    public Parser(Iterable<String> lexemes) {
        this.lexemes = lexemes;
    }

    private Expression nextExpression(PushbackIterator<String> it) {
        return parseExpression(it);
    }

    @Override
    public Iterator<Expression> iterator() {
        final PushbackIterator<String> it = new PushbackIterator<>(lexemes.iterator());

        return new Iterator<Expression>() {
            private Expression next;
            boolean nextDeployed = true;

            @Override
            public boolean hasNext() {
                if (nextDeployed) {
                    next = nextExpression(it);
                    nextDeployed = false;
                }

                return next != null;
            }

            @Override
            public Expression next() {
                if (nextDeployed) {
                    if (hasNext()) {
                        return next();
                    }

                    throw new NoSuchElementException();
                }

                nextDeployed = true;
                return next;
            }

            @Override
            public void remove() {
                throw new UnsupportedOperationException();
            }
        };
    }
}

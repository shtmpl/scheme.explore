package program.repl;

import scheme.Core;
import scheme.DefaultEnvironment;
import scheme.Environment;
import scheme.Expression;
import scheme.expression.Symbol;
import scheme.syntax.Lexer;
import scheme.syntax.Parser;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PushbackReader;
import java.util.HashMap;
import java.util.Map;

public final class Main {
    private static final int MAX_IDENTIFIER_LENGTH = 1024;

    private static final Map<Symbol, Expression> PRIMITIVES = new HashMap<>();

    static {
        PRIMITIVES.put(new Symbol("null?"), Core.IS_NULL);

        PRIMITIVES.put(new Symbol("true"), Core.TRUE);
        PRIMITIVES.put(new Symbol("false"), Core.FALSE);

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
    }

    private static final Environment ENVIRONMENT_GLOBAL =
            new DefaultEnvironment(Environment.EMPTY, PRIMITIVES);


    private static final String PROMPT_IN = "λ> ";
    private static final String PROMPT_OUT = "=> ";
    private static final String PROMPT_ERR = "~> ";

    public static void main(String[] args) {
        try (PushbackReader reader =
                     new PushbackReader(
                             new BufferedReader(
                                     new InputStreamReader(System.in)), MAX_IDENTIFIER_LENGTH)) {
            System.out.print(PROMPT_IN);
            for (Expression expression : new Parser(new Lexer(reader))) {
                try {
                    Expression evaluated = expression.eval(ENVIRONMENT_GLOBAL);
                    System.out.printf("%s%s%n", PROMPT_OUT, evaluated);
                } catch (RuntimeException exception) {
                    System.out.printf("%s%s%n", PROMPT_ERR, exception.getMessage());
                }

                System.out.print(PROMPT_IN);
            }
        } catch (IOException exception) {
            throw new RuntimeException(exception);
        }

        System.out.println("Done!");
    }
}

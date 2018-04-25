package program.interpreter.interactive;

import scheme.*;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class Main {
    private static final Environment ENVIRONMENT_GLOBAL = new DefaultEnvironment(Environment.EMPTY);

    private static final String PROMPT_IN = "λ> ";
    private static final String PROMPT_OUT = "=> ";
    private static final String PROMPT_ERR = "~> ";

    private static BufferedReader createBufferedStdinReader() {
        return new BufferedReader(new InputStreamReader(System.in));
    }

    public static void main(String[] args) {
        try (ExpressionReader reader = new ExpressionReader(createBufferedStdinReader())) {
            System.out.print(PROMPT_IN);

            Expression expression;
            while ((expression = reader.nextExpression()) != null) {
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

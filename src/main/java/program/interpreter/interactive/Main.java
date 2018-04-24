package program.interpreter.interactive;

import scheme.*;
import scheme.syntax.Result;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.List;

import static java.util.Arrays.asList;

public class Main {
    private static final Environment ENVIRONMENT_GLOBAL = new DefaultEnvironment(Environment.EMPTY);

    private static final String PROMPT_IN = "λ> ";
    private static final String PROMPT_OUT = "=> ";
    private static final String PROMPT_ERR = "~> ";

    private static BufferedReader createBufferedStdinReader() {
        return new BufferedReader(new InputStreamReader(System.in));
    }

    public static void main(String[] args) {
        try (BufferedReader reader = createBufferedStdinReader()) {
            System.out.print(PROMPT_IN);

            String line, remaining = "";
            while ((line = reader.readLine()) != null) {
                if (!remaining.isEmpty()) {
                    line = Strings.join(" ", asList(remaining, line));
                }

                Result<List<Expression>> result;
                while ((result = Syntax.program().apply(line)).isSuccess()) {
                    for (Expression expression : result.value()) {
                        try {
                            Expression evaluated = expression.eval(ENVIRONMENT_GLOBAL);
                            if (Utilities.isNull(evaluated)) {
                                /*NOP*/
                            } else {
                                System.out.printf("%s%s%n", PROMPT_OUT, evaluated);
                            }
                        } catch (RuntimeException exception) {
                            System.out.printf("%s%s%n", PROMPT_ERR, exception.getMessage());
                        }
                    }

                    line = result.remaining();
                }

                remaining = result.remaining();

                System.out.print(PROMPT_IN);
            }
        } catch (IOException exception) {
            throw new RuntimeException(exception);
        }
    }
}

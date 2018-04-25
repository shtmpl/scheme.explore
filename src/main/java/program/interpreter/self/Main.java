package program.interpreter.self;

import scheme.*;
import scheme.syntax.Result;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

import static java.util.Arrays.asList;

public final class Main {
    private static final String SRC_INTERPRETER = "applicative.scm";

    private static final Environment ENVIRONMENT_GLOBAL = new DefaultEnvironment(Environment.EMPTY);

    public static void main(String... args) {
        try (ExpressionReader reader =
                     new ExpressionReader(
                             new BufferedReader(
                                     new InputStreamReader(
                                             Thread.currentThread()
                                                     .getContextClassLoader()
                                                     .getResourceAsStream(SRC_INTERPRETER))),
                             Syntax.PROGRAM)) {
            Expression expression;
            while ((expression = reader.nextExpression()) != null) {
                try {
                    Expression evaluated = expression.eval(ENVIRONMENT_GLOBAL);
                    if (Utilities.isNull(evaluated)) {
                        /*NOP*/
                    } else {
                        System.out.printf("%s%n", evaluated);
                    }
                } catch (RuntimeException exception) {
                    System.out.printf("%s%n", exception.getMessage());
                }
            }
        } catch (IOException exception) {
            throw new RuntimeException(exception);
        }

//        System.out.println("Done!");
    }
}

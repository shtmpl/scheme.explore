package interpreter.self;

import scheme.*;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;

public final class Main {
    private static final String SRC_INTERPRETER = "src/main/scheme/interpreter/applicative.scm";

    private static final Environment ENVIRONMENT_GLOBAL = new DefaultEnvironment(Environment.EMPTY);

    public static void main(String... args) {
        try (ExpressionReader reader =
                     new ExpressionReader(
                             Files.newBufferedReader(
                                     Paths.get(SRC_INTERPRETER),
                                     StandardCharsets.UTF_8),
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

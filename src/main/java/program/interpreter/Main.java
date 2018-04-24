package program.interpreter;

import scheme.*;
import scheme.syntax.Result;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

import static java.util.Arrays.asList;

public final class Main {
    private static final Environment ENVIRONMENT_GLOBAL = new DefaultEnvironment(Environment.EMPTY);

    private static BufferedReader createBufferedFileReader(String path) {
        try {
            return Files.newBufferedReader(Paths.get(path), StandardCharsets.UTF_8);
        } catch (IOException exception) {
            throw new RuntimeException(exception);
        }
    }

    public static void main(String... args) {
        if (args.length < 1) {
            System.err.println("File path should be specified");
            System.exit(1);
        }

        try (ExpressionReader reader = new ExpressionReader(createBufferedFileReader(args[0]))) {
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
                    System.out.printf("%s%s%n", exception.getMessage());
                }
            }
        } catch (IOException exception) {
            throw new RuntimeException(exception);
        }

        System.out.println("Done!");
    }
}

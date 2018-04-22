package program.interpreter;

import scheme.*;
import scheme.expression.Symbol;
import scheme.syntax.Lexer;
import scheme.syntax.Parser;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PushbackReader;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;

public final class Main {
    private static final Environment ENVIRONMENT_GLOBAL = new DefaultEnvironment(Environment.EMPTY);

    private static final int MAX_IDENTIFIER_LENGTH = 1024;

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

        try (PushbackReader reader = new PushbackReader(createBufferedFileReader(args[0]), MAX_IDENTIFIER_LENGTH)) {
            for (Expression expression : new Parser(new Lexer(reader))) {
                try {
                    Expression evaluated = expression.eval(ENVIRONMENT_GLOBAL);
                    if (Utilities.isNull(evaluated)) {
                        continue;
                    }

                    System.out.printf("%s%n", evaluated);
                } catch (RuntimeException exception) {
                    System.out.printf("%s%n", exception.getMessage());
                }
            }
        } catch (IOException exception) {
            throw new RuntimeException(exception);
        }

        System.out.println("Done!");
    }
}

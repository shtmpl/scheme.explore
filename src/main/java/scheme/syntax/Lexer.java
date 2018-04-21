package scheme.syntax;

import java.io.IOException;
import java.io.PushbackReader;
import java.util.Iterator;
import java.util.NoSuchElementException;

public class Lexer implements Iterable<String> {
    private static String findWhitespaces(PushbackReader input) throws IOException {
        StringBuilder result = new StringBuilder();

        int x;
        while ((x = input.read()) != -1) {
            if (Character.isWhitespace(x)) {
                result.append((char) x);
                continue;
            }

            input.unread(x);
            break;
        }

        return result.toString();
    }

    private static String findOneCharacterLexeme(PushbackReader input) throws IOException {
        StringBuilder result = new StringBuilder();

        int x;
        if ((x = input.read()) != -1) {
            switch (x) {
                case '(':
                case ')':
                case '\'':
                    result.append((char) x);
                    break;
                default:
                    input.unread(x);
                    break;
            }
        }

        return result.toString();
    }

    private static String findNumberLiteral(PushbackReader input) throws IOException {
        StringBuilder result = new StringBuilder();

        int x;
        while ((x = input.read()) != -1) {
            if (Character.isDigit(x)) {
                result.append((char) x);
                continue;
            }

            input.unread(x);
            break;
        }

        return result.toString();
    }

    private static String findStringLiteral(PushbackReader input) throws IOException {
        StringBuilder result = new StringBuilder();

        int x;
        if ((x = input.read()) != -1) {
            if (x == '"') {
                result.append((char) x);
            } else {
                input.unread(x);
                return "";
            }

            while ((x = input.read()) != -1 && x != '"') {
                result.append((char) x);
            }

            if (x == '"') {
                result.append((char) x);
            } else {
                throw new RuntimeException("Unexpected EOF");
            }
        }

        return result.toString();
    }

    private static String findIdentifier(PushbackReader input) throws IOException {
        StringBuilder result = new StringBuilder();

        int x;
        while ((x = input.read()) != -1) {
            if (Character.isLetterOrDigit(x)
                    || '+' == x || '-' == x || '*' == x || '/' == x
                    || '<' == x || '=' == x || '>' == x
                    || '!' == x || '?' == x) {
                result.append((char) x);
                continue;
            }

            input.unread(x);
            break;
        }

        return result.toString();
    }

    private static String findLexeme(PushbackReader input) throws IOException {
        String result = findWhitespaces(input);

        result = findOneCharacterLexeme(input);
        if (!result.isEmpty()) {
            return result;
        }

        result = findNumberLiteral(input);
        if (!result.isEmpty()) {
            return result;
        }

        result = findStringLiteral(input);
        if (!result.isEmpty()) {
            return result;
        }

        result = findIdentifier(input);
        if (!result.isEmpty()) {
            return result;
        }

        return result;
    }

    private final PushbackReader input;

    public Lexer(PushbackReader input) {
        this.input = new PushbackReader(input);
    }

    public String nextLexeme() {
        try {
            return findLexeme(input);
        } catch (IOException exception) {
            throw new RuntimeException(exception);
        }
    }

    @Override
    public Iterator<String> iterator() {
        return new Iterator<String>() {
            private String next;
            boolean nextDeployed = true;

            @Override
            public boolean hasNext() {
                if (nextDeployed) {
                    next = nextLexeme();
                    nextDeployed = false;
                }

                return !next.isEmpty();
            }

            @Override
            public String next() {
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

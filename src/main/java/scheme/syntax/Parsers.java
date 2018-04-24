package scheme.syntax;

import scheme.Strings;

import java.util.LinkedList;
import java.util.List;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static java.util.Arrays.asList;

public final class Parsers {


    // Primitives

    public static <X> Parser<X> unexpected() {
        return (String input) -> Result.failure(input, "Unexpected");
    }


    public static Parser<Character> character(char character) {
        return (String input) -> {
            if (input.isEmpty()) {
                return Result.failure(input, String.format("Unexpected: `%s`", "EOF"));
            }

            char x = input.charAt(0);
            return character == x
                    ? Result.success(input.substring(1), character)
                    : Result.failure(input, String.format("Expected: `%s`, Actual: `%s`", character, x));
        };
    }

    public static Parser<Character> characterSatisfying(Predicate<Character> predicate) {
        return (String input) -> {
            if (input.isEmpty()) {
                return Result.failure(input, String.format("Unexpected: `%s`", "EOF"));
            }

            char x = input.charAt(0);
            return predicate.test(x)
                    ? Result.success(input.substring(1), x)
                    : Result.failure(input, String.format("Unexpected: `%s`", x));
        };
    }


    public static Parser<Character> whitespace() {
        return characterSatisfying(Character::isWhitespace);
    }

    public static Parser<Character> digit() {
        return characterSatisfying(Character::isDigit);
    }

    public static Parser<Character> letter() {
        return characterSatisfying(Character::isLetter);
    }

    public static Parser<Character> characterOf(String characters) {
        return characterSatisfying((Character x) -> characters.contains(x.toString()));
    }

    public static Parser<Character> characterExcept(String characters) {
        return characterSatisfying((Character x) -> !characters.contains(x.toString()));
    }


    public static Parser<String> string(String string) {
        return (String input) -> {
            if (input.isEmpty()) {
                return Result.failure(input, "Unexpected: `EOF`");
            }

            if (input.startsWith(string)) {
                return Result.success(input.substring(string.length()), string);
            }

            return Result.failure(input, String.format("Expected: `%s`", string));
        };
    }

    public static Parser<String> whitespaces() {
        return asString(oneOrMore(whitespace()));
    }

    public static Parser<String> digits() {
        return asString(oneOrMore(digit()));
    }

    public static Parser<String> letters() {
        return asString(oneOrMore(letter()));
    }


    public static Parser<String> pattern(Pattern pattern) {
        return (String input) -> {
            if (input.isEmpty()) {
                return Result.failure(input, "Unexpected: `EOF`");
            }

            Matcher matcher = pattern.matcher(input);
            if (matcher.find()) {
                return Result.success(input.substring(matcher.end()), matcher.group());
            }

            return Result.failure(input, String.format("Expected: `%s`", pattern));
        };
    }


    // Combinators

    private static <X> List<X> cons(X first, List<X> rest) {
        List<X> result = new LinkedList<>();
        result.add(first);
        result.addAll(rest);

        return result;
    }


    public static <X> Parser<X> named(String name, Parser<X> parser) {
        return (String input) -> {
            Result<X> result = parser.apply(input);
            if (result.isSuccess()) {
                return result;
            }

            return Result.failure(result.remaining(), String.format("`%s`. %s", name, result.error()));
        };
    }


    @SafeVarargs
    public static <X> Parser<List<X>> allOf(Parser<X> first, Parser<X>... rest) {
        return (String input) -> {
            List<X> parsed = new LinkedList<>();

            for (Parser<X> parser : cons(first, asList(rest))) {
                Result<X> result = parser.apply(input);
                if (result.isFailure()) {
                    return Result.failure(input, result.error());
                }

                parsed.add(result.value());
                input = result.remaining();
            }

            return Result.success(input, parsed);
        };
    }


    @SafeVarargs
    public static <X> Parser<X> anyOf(Parser<X> first, Parser<X>... rest) {
        return (String input) -> {
            for (Parser<X> parser : cons(first, asList(rest))) {
                Result<X> result = parser.apply(input);
                if (result.isSuccess()) {
                    return result;
                }
            }

            return Result.failure(input, "Nothing"); // FIXME
        };
    }


    public static <X, F> Parser<F> as(Function<X, F> function, Parser<X> parser) {
        return (String input) -> {
            Result<X> result = parser.apply(input);
            if (result.isSuccess()) {
                return Result.success(result.remaining(), function.apply(result.value()));
            }

            return Result.failure(input, result.error());
        };
    }

    public static Parser<String> asString(Parser<List<Character>> parser) {
        return as((List<Character> characters) -> Strings.join("", characters), parser);
    }


    @SafeVarargs
    public static <X, S> Parser<List<X>> separatedBy(Parser<S> separator, Parser<X> first, Parser<X>... rest) {
        List<Parser<X>> parsers = cons(first, asList(rest));
        return (String input) -> {
            List<X> parsed = new LinkedList<>();

            String remaining = input;
            for (int index = 0; index < parsers.size() - 1; index++) {
                Parser<X> parser = parsers.get(index);

                Result<X> result = parser.apply(remaining);
                if (result.isFailure()) {
                    return Result.failure(input, result.error());
                }

                Result<S> ignore = separator.apply(result.remaining());
                if (ignore.isFailure()) {
                    return Result.failure(input, ignore.error());
                }

                parsed.add(result.value());
                remaining = ignore.remaining();
            }

            Result<X> result = parsers.get(parsers.size() - 1).apply(remaining);
            if (result.isSuccess()) {
                parsed.add(result.value());
                return Result.success(result.remaining(), parsed);
            }

            return Result.failure(input, result.error());
        };
    }


    public static <X> Parser<Void> ignore(Parser<X> parser) {
        return (String input) -> {
            Result<X> ignore = parser.apply(input);
            if (ignore.isFailure()) {
                return Result.failure(input, ignore.error());
            }

            return Result.success(ignore.remaining(), null);
        };
    }


    public static <X> Parser<X> optional(Parser<X> parser) { // FIXME
        return (String input) -> {
            Result<X> result = parser.apply(input);
            if (result.isSuccess()) {
                return result;
            }

            return Result.success(input, null);
        };
    }


    public static <X> Parser<List<X>> zeroOrMore(Parser<X> parser) {
        return (String input) -> {
            List<X> parsed = new LinkedList<>();

            Result<X> result;
            while ((result = parser.apply(input)).isSuccess()) {
                parsed.add(result.value());
                input = result.remaining();
            }

            return Result.success(input, parsed);
        };
    }

    public static <X, S> Parser<List<X>> zeroOrMoreSeparatedBy(Parser<S> separator, Parser<X> parser) {
        return (String input) -> {
            List<X> parsed = new LinkedList<>();

            Result<X> result;
            String remaining = input;
            while ((result = parser.apply(remaining)).isSuccess()) {
                parsed.add(result.value());
                input = result.remaining();

                Result<S> ignore = separator.apply(input);
                if (ignore.isFailure()) {
                    return Result.success(input, parsed);
                }

                remaining = ignore.remaining();
            }

            return Result.success(input, parsed);
        };
    }


    public static <X> Parser<List<X>> oneOrMore(Parser<X> parser) {
        return (String input) -> {
            List<X> parsed = new LinkedList<>();

            Result<X> result = parser.apply(input);
            if (result.isFailure()) {
                return Result.failure(input, result.error());
            }

            do {
                parsed.add(result.value());
                input = result.remaining();
            } while ((result = parser.apply(input)).isSuccess());

            return Result.success(input, parsed);
        };
    }

    public static <X, S> Parser<List<X>> oneOrMoreSeparatedBy(Parser<S> separator, Parser<X> parser) {
        return (String input) -> {
            List<X> parsed = new LinkedList<>();

            Result<X> result = parser.apply(input);
            if (result.isFailure()) {
                return Result.failure(input, result.error());
            }

            String remaining = result.remaining();
            do {
                parsed.add(result.value());
                input = result.remaining();

                Result<S> ignore = separator.apply(result.remaining());
                if (ignore.isFailure()) {
                    break;
                }

                remaining = ignore.remaining();
            } while ((result = parser.apply(remaining)).isSuccess());

            return Result.success(input, parsed);
        };
    }


    public static <X, A> Parser<X> before(Parser<X> before, Parser<A> parser) {
        return (String input) -> {
            Result<X> result = before.apply(input);
            if (result.isFailure()) {
                return Result.failure(input, result.error());
            }

            Result<A> ignore = parser.apply(result.remaining());
            if (ignore.isFailure()) {
                return Result.failure(input, ignore.error());
            }

            return Result.success(ignore.remaining(), result.value());
        };
    }

    public static <B, X, A> Parser<X> between(Parser<B> before, Parser<X> between, Parser<A> after) {
        return (String input) -> {
            Result<B> ignoreBefore = before.apply(input);
            if (ignoreBefore.isFailure()) {
                return Result.failure(input, ignoreBefore.error());
            }

            Result<X> result = between.apply(ignoreBefore.remaining());
            if (result.isFailure()) {
                return Result.failure(input, result.error());
            }

            Result<A> ignoreAfter = after.apply(result.remaining());
            if (ignoreAfter.isFailure()) {
                return Result.failure(input, ignoreAfter.error());
            }

            return Result.success(ignoreAfter.remaining(), result.value());
        };
    }

    public static <B, X> Parser<X> after(Parser<B> parser, Parser<X> after) {
        return (String input) -> {
            Result<B> ignore = parser.apply(input);
            if (ignore.isFailure()) {
                return Result.failure(input, ignore.error());
            }

            Result<X> result = after.apply(ignore.remaining());
            if (result.isFailure()) {
                return Result.failure(input, result.error());
            }

            return Result.success(result.remaining(), result.value());
        };
    }


    private static <B, X, A> Parser<X> nested(Parser<B> before, Parser<X> parser, Parser<A> after) {
        return new Parser<X>() {
            private final Parser<X> recursive = anyOf(this, parser);

            @Override
            public Result<X> apply(String input) {
                Result<X> result = recursive.apply(input);
                if (result.isSuccess()) {
                    return result;
                }

                Result<B> rb = before.apply(input);
                if (rb.isFailure()) {
                    return Result.failure(input, rb.error());
                }

                Result<X> rr = recursive.apply(rb.remaining());
                if (rr.isFailure()) {
                    return Result.failure(input, rr.error());
                }

                Result<A> ra = after.apply(rr.remaining());
                if (ra.isFailure()) {
                    return Result.failure(input, ra.error());
                }

                return Result.success(ra.remaining(), rr.value());
            }
        };
    }

    public static <X> Parser<X> parenthesised(Parser<X> parser) {
        return between(string("("), parser, string(")"));
    }




    public static void main(String[] args) {
        String program = "(x (x (x)))";

        Parser<String> symbol = letters();

        Parser.Reference<String> ref = Parser.reference();
        Parser<String> list = as(Object::toString, parenthesised(oneOrMoreSeparatedBy(whitespace(), ref.parser())));
        Parser<String> expression = anyOf(
                symbol,
                list);
        ref.set(expression);

        System.out.println(expression.apply(program));
    }
}

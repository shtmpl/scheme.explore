package scheme.syntax.parsing;

import org.junit.Test;
import scheme.syntax.Parsers;
import scheme.syntax.Result;

import java.util.regex.Pattern;

import static java.util.Arrays.asList;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

public class ParsersTest {
    private static <X> void assertFailure(Result<X> result, String remaining) {
        assertThat("Result", result.isFailure(), is(true));
        assertThat("Remaining", result.remaining(), is(remaining));
    }

    private static <X> void assertSuccess(Result<X> result, String remaining, X x) {
        assertThat("Result", result.isSuccess(), is(true));
        assertThat("Remaining", result.remaining(), is(remaining));
        assertThat("Parsed", result.value(), is(x));
    }


    @Test
    public void shouldAllowToParseCharacter() throws Exception {
        assertFailure(Parsers.character('x').apply(""), "");
        assertSuccess(Parsers.character('x').apply("x"), "", 'x');
        assertSuccess(Parsers.character('x').apply("xx"), "x", 'x');
    }

    @Test
    public void shouldAllowToParseCharacterWhenPredicateIsSatisfied() throws Exception {
        System.out.println(
                Parsers.characterSatisfying(
                        Character::isLetter).apply("abcd"));
    }

    @Test
    public void shouldAllowToParseAnyCharacterOfTheSuppliedSet() throws Exception {
        assertSuccess(
                Parsers.characterOf("abcd").apply("abcd"),
                "bcd",
                'a');
    }

    @Test
    public void shouldAllowToParseStringAccordingToTheGivenPattern() throws Exception {
        System.out.println(
                Parsers.pattern(Pattern.compile("\\d+")).apply("0123foo"));
    }

    @Test
    public void shouldAllowToParseAllOf() throws Exception {
        System.out.println(
                Parsers.allOf(
                        Parsers.character('a'),
                        Parsers.character('b'),
                        Parsers.character('c')).apply("abcd"));
    }

    @Test
    public void shouldAllowToParseAnyOf() throws Exception {
        System.out.println(
                Parsers.anyOf(
                        Parsers.character('a'),
                        Parsers.character('b'),
                        Parsers.character('c')).apply("abcd"));
    }

    @Test
    public void shouldAllowToParseAs() throws Exception {
        System.out.println(
                Parsers.as(
                        (Character x) -> Long.parseLong(Character.toString(x)), Parsers.character('1')
                ).apply("1bcd"));
    }

    @Test
    public void shouldAllowToParseSeparatedBy() throws Exception {
        assertFailure(
                Parsers.separatedBy(
                        Parsers.whitespace(),
                        Parsers.character('a')).apply(""),
                "");
        assertSuccess(
                Parsers.separatedBy(
                        Parsers.whitespace(),
                        Parsers.character('a')).apply("a"),
                "",
                asList('a'));
        assertSuccess(
                Parsers.separatedBy(
                        Parsers.whitespace(),
                        Parsers.character('a')).apply("ax"),
                "x",
                asList('a'));
        assertSuccess(
                Parsers.separatedBy(
                        Parsers.whitespace(),
                        Parsers.character('a')).apply("a "),
                " ",
                asList('a'));
        assertFailure(
                Parsers.separatedBy(
                        Parsers.whitespace(),
                        Parsers.character('a'),
                        Parsers.character('b')).apply("a "),
                "a ");
        assertSuccess(
                Parsers.separatedBy(
                        Parsers.whitespace(),
                        Parsers.character('a'),
                        Parsers.character('b')).apply("a b"),
                "",
                asList('a', 'b'));
    }

    @Test
    public void shouldAllowToParseOptional() throws Exception {
        assertSuccess(
                Parsers.optional(
                        Parsers.character('x')).apply(""),
                "",
                null);
        assertSuccess(
                Parsers.optional(
                        Parsers.character('x')).apply("x"),
                "",
                'x');
    }

    @Test
    public void shouldAllowToParseZeroOrMore() throws Exception {
        System.out.println(
                Parsers.zeroOrMore(
                        Parsers.character('x')).apply(""));
        System.out.println(
                Parsers.zeroOrMore(
                        Parsers.character('x')).apply("x"));
        System.out.println(
                Parsers.zeroOrMore(
                        Parsers.character('x')).apply("xx"));
    }

    @Test
    public void shouldAllowToParseZeroOrMoreSeparatedBy() throws Exception {
        assertSuccess(
                Parsers.zeroOrMoreSeparatedBy(
                        Parsers.whitespace(),
                        Parsers.letter()).apply(""),
                "",
                asList());
        assertSuccess(
                Parsers.zeroOrMoreSeparatedBy(
                        Parsers.whitespace(),
                        Parsers.letter()).apply("x"),
                "",
                asList('x'));
        assertSuccess(
                Parsers.zeroOrMoreSeparatedBy(
                        Parsers.whitespace(),
                        Parsers.letter()).apply("x "),
                " ",
                asList('x'));
        assertSuccess(
                Parsers.zeroOrMoreSeparatedBy(
                        Parsers.whitespace(),
                        Parsers.letter()).apply("x x"),
                "",
                asList('x', 'x'));
        assertSuccess(
                Parsers.zeroOrMoreSeparatedBy(
                        Parsers.whitespace(),
                        Parsers.letter()).apply("x x "),
                " ",
                asList('x', 'x'));
    }

    @Test
    public void shouldAllowToParseOneOrMore() throws Exception {
        assertFailure(
                Parsers.oneOrMore(
                        Parsers.character('x')).apply(""),
                "");
        assertSuccess(
                Parsers.oneOrMore(
                        Parsers.character('x')).apply("x"),
                "",
                asList('x'));
        assertSuccess(
                Parsers.oneOrMore(
                        Parsers.character('x')).apply("xx"),
                "",
                asList('x', 'x'));
    }

    @Test
    public void shouldAllowToParseOneOrMoreSeparatedBy() throws Exception {
        assertFailure(
                Parsers.oneOrMoreSeparatedBy(
                        Parsers.whitespace(),
                        Parsers.letter()).apply(""),
                "");
        assertSuccess(
                Parsers.oneOrMoreSeparatedBy(
                        Parsers.whitespace(),
                        Parsers.letter()).apply("x"),
                "",
                asList('x'));
        assertSuccess(
                Parsers.oneOrMoreSeparatedBy(
                        Parsers.whitespace(),
                        Parsers.letter()).apply("x "),
                " ",
                asList('x'));
        assertSuccess(
                Parsers.oneOrMoreSeparatedBy(
                        Parsers.whitespace(),
                        Parsers.letter()).apply("x x"),
                "",
                asList('x', 'x'));
        assertSuccess(
                Parsers.oneOrMoreSeparatedBy(
                        Parsers.whitespace(),
                        Parsers.letter()).apply("x x "),
                " ",
                asList('x', 'x'));
    }

    @Test
    public void shouldAllowToParseBefore() throws Exception {
        System.out.println(
                Parsers.before(
                        Parsers.character('x'),
                        Parsers.character('b')).apply("xb"));
    }

    @Test
    public void shouldAllowToParseBetween() throws Exception {
        System.out.println(
                Parsers.between(
                        Parsers.character('a'),
                        Parsers.character('x'),
                        Parsers.character('b')).apply("axb"));
    }

    @Test
    public void shouldAllowToParseAfter() throws Exception {
        System.out.println(
                Parsers.after(
                        Parsers.character('a'),
                        Parsers.character('x')).apply("ax"));
    }

    @Test
    public void shouldAllowToParseIgnore() throws Exception {
        System.out.println(
                Parsers.ignore(
                        Parsers.character('x')).apply("xab"));
    }
}
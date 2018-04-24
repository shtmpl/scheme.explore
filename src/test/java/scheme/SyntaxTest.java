package scheme;

import org.junit.Test;
import scheme.syntax.Result;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.*;

public class SyntaxTest {
    private static <X> void assertFailure(Result<X> result, String remaining) {
        assertThat("Result", result.isFailure(), is(true));
        assertThat("Remaining", result.remaining(), is(remaining));
    }

    private static <X> void assertSuccess(Result<X> result, String remaining, String form) {
        assertThat("Result", result.isSuccess(), is(true));
        assertThat("Remaining", result.remaining(), is(remaining));
        assertThat("Parsed", result.value().toString(), is(form)); // FIXME
    }

    @Test
    public void shouldAllowToParseCombination() throws Exception {
        assertSuccess(
                Syntax.combination().apply("(0)"),
                "",
                "(0)");
        assertSuccess(
                Syntax.combination().apply("(0 1 2)"),
                "",
                "(0 1 2)");
    }

    @Test
    public void shouldAllowToParseQuote() throws Exception {
        assertSuccess(
                Syntax.quote().apply("'0"),
                "",
                "0");
        assertSuccess(
                Syntax.quote().apply("'x"),
                "",
                "x");
        assertSuccess(
                Syntax.quote().apply("'()"),
                "",
                "()");
        assertSuccess(
                Syntax.quote().apply("'(0 1 2)"),
                "",
                "(0 1 2)");

        assertSuccess(
                Syntax.quote().apply("(quote 0)"),
                "",
                "0");
        assertSuccess(
                Syntax.quote().apply("(quote x)"),
                "",
                "x");
        assertSuccess(
                Syntax.quote().apply("(quote ())"),
                "",
                "()");
        assertSuccess(
                Syntax.quote().apply("(quote (0 1 2))"),
                "",
                "(0 1 2)");
    }

    @Test
    public void shouldAllowToParseLambda() throws Exception {
        assertSuccess(
                Syntax.lambda().apply("(lambda () 42)"),
                "",
                "(lambda () 42)");
        assertSuccess(
                Syntax.lambda().apply("(lambda (x) x)"),
                "",
                "(lambda (x) x)");
        assertSuccess(
                Syntax.lambda().apply("(lambda (x y) (+ x y))"),
                "",
                "(lambda (x y) (+ x y))");
        assertSuccess(
                Syntax.lambda().apply("(lambda (x y) (set! x 0) (set! y 1) (+ x y))"),
                "",
                "(lambda (x y) (set! x 0) (set! y 1) (+ x y))");
    }

    @Test
    public void shouldAllowToParseDefinition() throws Exception {
        assertSuccess(
                Syntax.definition().apply("(define x 42)"),
                "",
                "(define x 42)");

        assertSuccess(
                Syntax.definition().apply("(define (x) 42)"),
                "",
                "(define x (lambda () 42))");
        assertSuccess(
                Syntax.definition().apply("(define (identity x) x)"),
                "",
                "(define identity (lambda (x) x))");
        assertSuccess(
                Syntax.definition().apply("(define (add x y) (+ x y))"),
                "",
                "(define add (lambda (x y) (+ x y)))");
    }

    @Test
    public void shouldAllowToParseAssignment() throws Exception {
        assertSuccess(
                Syntax.assignment().apply("(set! x 42)"),
                "",
                "(set! x 42)");
        assertSuccess(
                Syntax.assignment().apply("(set! x (+ 0 1 2))"),
                "",
                "(set! x (+ 0 1 2))");
    }

    @Test
    public void shouldAllowToParseIf() throws Exception {
        assertSuccess(
                Syntax.if_().apply("(if 42 x y)"),
                "",
                "(if 42 x y)");
        assertSuccess(
                Syntax.if_().apply("(if (< x 1) 1 (* x (factorial (- x 1))))"),
                "",
                "(if (< x 1) 1 (* x (factorial (- x 1))))");
    }

    @Test
    public void shouldAllowToParseBegin() throws Exception {
        assertSuccess(
                Syntax.begin().apply("(begin 42)"),
                "",
                "(begin 42)");
        assertSuccess(
                Syntax.begin().apply("(begin (set! x 42) x)"),
                "",
                "(begin (set! x 42) x)");
    }
}
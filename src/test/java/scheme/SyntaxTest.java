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
    public void shouldAllowToParseUnit() throws Exception {
        assertSuccess(
                Syntax.unit().apply("()"),
                "",
                "()");
    }

    @Test
    public void shouldAllowToParseIntegral() throws Exception {
        assertSuccess(
                Syntax.integral().apply("42"),
                "",
                "42");
    }

    @Test
    public void shouldAllowToParseFractional() throws Exception {
        assertSuccess(
                Syntax.fractional().apply("42."),
                "",
                "42.0");
        assertSuccess(
                Syntax.fractional().apply(".42"),
                "",
                "0.42");
        assertSuccess(
                Syntax.fractional().apply("42.0"),
                "",
                "42.0");
    }

    @Test
    public void shouldAllowToParseString() throws Exception {
        assertSuccess(
                Syntax.string().apply("\"\""),
                "",
                "\"\"");
        assertSuccess(
                Syntax.string().apply("\"  \""),
                "",
                "\"  \"");
        assertSuccess(
                Syntax.string().apply("\"\n\n\""),
                "",
                "\"\n\n\"");
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
        assertSuccess(
                Syntax.combination().apply("(  0  1  2  )"),
                "",
                "(0 1 2)");
        assertSuccess(
                Syntax.combination().apply("(\n0\n1\n2\n)"),
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
                Syntax.quote().apply("'(  0  1  2  )"),
                "",
                "(0 1 2)");
        assertSuccess(
                Syntax.quote().apply("'(\n0\n1\n2\n)"),
                "",
                "(0 1 2)");

        assertSuccess(
                Syntax.quote().apply("(quote 0)"),
                "",
                "0");
        assertSuccess(
                Syntax.quote().apply("(quote ())"),
                "",
                "()");
        assertSuccess(
                Syntax.quote().apply("(quote x)"),
                "",
                "x");
        assertSuccess(
                Syntax.quote().apply("(  quote  x  )"),
                "",
                "x");
        assertSuccess(
                Syntax.quote().apply("(\nquote\nx\n)"),
                "",
                "x");

        assertSuccess(
                Syntax.quote().apply("(quote (0 1 2))"),
                "",
                "(0 1 2)");
        assertSuccess(
                Syntax.quote().apply("(  quote  (  0  1  2  )  )"),
                "",
                "(0 1 2)");
        assertSuccess(
                Syntax.quote().apply("(\nquote\n(\n0\n1\n2\n)\n)"),
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
                Syntax.lambda().apply("(  lambda  ()  42  )"),
                "",
                "(lambda () 42)");
        assertSuccess(
                Syntax.lambda().apply("(\nlambda\n()\n42\n)"),
                "",
                "(lambda () 42)");

        assertSuccess(
                Syntax.lambda().apply("(lambda (x) x)"),
                "",
                "(lambda (x) x)");
        assertSuccess(
                Syntax.lambda().apply("(  lambda  (  x  )  x  )"),
                "",
                "(lambda (x) x)");
        assertSuccess(
                Syntax.lambda().apply("(\nlambda\n(\nx\n)\nx\n)"),
                "",
                "(lambda (x) x)");

        assertSuccess(
                Syntax.lambda().apply("(lambda (x y) (+ x y))"),
                "",
                "(lambda (x y) (+ x y))");
        assertSuccess(
                Syntax.lambda().apply("(  lambda  (  x  y  )  (  +  x  y  )  )"),
                "",
                "(lambda (x y) (+ x y))");
        assertSuccess(
                Syntax.lambda().apply("(\nlambda\n(\nx\ny\n)\n(\n+\nx\ny\n)\n)"),
                "",
                "(lambda (x y) (+ x y))");

        assertSuccess(
                Syntax.lambda().apply("(lambda (x y) (set! x 0) (set! y 1) (+ x y))"),
                "",
                "(lambda (x y) (set! x 0) (set! y 1) (+ x y))");
        assertSuccess(
                Syntax.lambda().apply("(  lambda  (  x  y  )  (  set!  x  0  )  (  set!  y  1  )  (  +  x  y  )  )"),
                "",
                "(lambda (x y) (set! x 0) (set! y 1) (+ x y))");
        assertSuccess(
                Syntax.lambda().apply("(\nlambda\n(\nx\ny\n)\n(\nset!\nx\n0\n)\n(\nset!\ny\n1\n)\n(\n+\nx\ny\n)\n)"),
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
                Syntax.definition().apply("(  define  x  42  )"),
                "",
                "(define x 42)");
        assertSuccess(
                Syntax.definition().apply("(\ndefine\nx\n42\n)"),
                "",
                "(define x 42)");

        assertSuccess(
                Syntax.definition().apply("(define (x) 42)"),
                "",
                "(define x (lambda () 42))");
        assertSuccess(
                Syntax.definition().apply("(  define  (  x  )  42  )"),
                "",
                "(define x (lambda () 42))");
        assertSuccess(
                Syntax.definition().apply("(\ndefine\n(\nx\n)\n42\n)"),
                "",
                "(define x (lambda () 42))");

        assertSuccess(
                Syntax.definition().apply("(define (identity x) x)"),
                "",
                "(define identity (lambda (x) x))");
        assertSuccess(
                Syntax.definition().apply("(  define  (  identity  x  )  x  )"),
                "",
                "(define identity (lambda (x) x))");
        assertSuccess(
                Syntax.definition().apply("(\ndefine\n(\nidentity\nx\n)\nx\n)"),
                "",
                "(define identity (lambda (x) x))");

        assertSuccess(
                Syntax.definition().apply("(define (add x y) (set! x 0) (set! y 1) (+ x y))"),
                "",
                "(define add (lambda (x y) (set! x 0) (set! y 1) (+ x y)))");
        assertSuccess(
                Syntax.definition().apply("(  define  (  add  x  y  )  (  set!  x  0  )  (  set!  y  1  )  (  +  x  y  )  )"),
                "",
                "(define add (lambda (x y) (set! x 0) (set! y 1) (+ x y)))");
        assertSuccess(
                Syntax.definition().apply("(\ndefine\n(\nadd\nx\ny\n)\n(\nset!\nx\n0\n)\n(\nset!\ny\n1\n)\n(\n+\nx\ny\n)\n)"),
                "",
                "(define add (lambda (x y) (set! x 0) (set! y 1) (+ x y)))");
    }

    @Test
    public void shouldAllowToParseAssignment() throws Exception {
        assertSuccess(
                Syntax.assignment().apply("(set! x 42)"),
                "",
                "(set! x 42)");
        assertSuccess(
                Syntax.assignment().apply("(  set!  x  42  )"),
                "",
                "(set! x 42)");
        assertSuccess(
                Syntax.assignment().apply("(\nset!\nx\n42\n)"),
                "",
                "(set! x 42)");

        assertSuccess(
                Syntax.assignment().apply("(set! x (+ 0 1 2))"),
                "",
                "(set! x (+ 0 1 2))");
        assertSuccess(
                Syntax.assignment().apply("(  set!  x  (  +  0  1  2  )  )"),
                "",
                "(set! x (+ 0 1 2))");
        assertSuccess(
                Syntax.assignment().apply("(\nset!\nx\n(\n+\n0\n1\n2\n)\n)"),
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
                Syntax.if_().apply("(  if  42  x  y  )"),
                "",
                "(if 42 x y)");
        assertSuccess(
                Syntax.if_().apply("(\nif\n42\nx\ny\n)"),
                "",
                "(if 42 x y)");

        assertSuccess(
                Syntax.if_().apply("(if (< x 1) 1 (* x (factorial (- x 1))))"),
                "",
                "(if (< x 1) 1 (* x (factorial (- x 1))))");
        assertSuccess(
                Syntax.if_().apply("(  if  (  <  x  1  )  1  (  *  x  (  factorial  (  -  x  1  )  )  )  )"),
                "",
                "(if (< x 1) 1 (* x (factorial (- x 1))))");
        assertSuccess(
                Syntax.if_().apply("(\nif\n(\n<\nx\n1\n)\n1\n(\n*\nx\n(\nfactorial\n(\n-\nx\n1\n)\n)\n)\n)"),
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
                Syntax.begin().apply("(  begin  42  )"),
                "",
                "(begin 42)");
        assertSuccess(
                Syntax.begin().apply("(\nbegin\n42\n)"),
                "",
                "(begin 42)");

        assertSuccess(
                Syntax.begin().apply("(begin (set! x 42) x)"),
                "",
                "(begin (set! x 42) x)");
        assertSuccess(
                Syntax.begin().apply("(  begin  (  set!  x  42  )  x  )"),
                "",
                "(begin (set! x 42) x)");
        assertSuccess(
                Syntax.begin().apply("(\nbegin\n(\nset!\nx\n42\n)\nx\n)"),
                "",
                "(begin (set! x 42) x)");
    }
}
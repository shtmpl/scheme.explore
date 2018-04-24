package scheme;

import scheme.syntax.Parser;
import scheme.syntax.Parsers;

import java.util.List;
import java.util.regex.Pattern;

public final class Syntax {
    private static final Parser<Expression> UNIT =
            Parsers.as(
                    Utilities::newUnit,
                    Parsers.string("()"));

    public static Parser<Expression> unit() {
        return UNIT;
    }

    private static final Pattern PATTERN_INTEGRAL = Pattern.compile("^\\d+");
    private static final Parser<Expression> INTEGRAL =
            Parsers.as(
                    Utilities::newIntegral,
                    Parsers.pattern(PATTERN_INTEGRAL));

    public static Parser<Expression> integral() {
        return INTEGRAL;
    }

    private static final Pattern PATTERN_FRACTIONAL = Pattern.compile("^(\\d+\\.\\d+|\\d+\\.|\\.\\d+)");
    private static final Parser<Expression> FRACTIONAL =
            Parsers.as(
                    Utilities::newFractional,
                    Parsers.pattern(PATTERN_FRACTIONAL));

    public static Parser<Expression> fractional() {
        return FRACTIONAL;
    }

    private static final Parser<Expression> STRING =
            Parsers.as(
                    Utilities::newString,
                    Parsers.between(
                            Parsers.character('"'),
                            Parsers.asString(Parsers.zeroOrMore(Parsers.characterExcept("\""))),
                            Parsers.character('"')));

    public static Parser<Expression> string() {
        return STRING;
    }

    private static final Parser<Expression> SYMBOL =
            Parsers.as(
                    Utilities::newSymbol,
                    Parsers.asString(
                            Parsers.oneOrMore(
                                    Parsers.anyOf(
                                            Parsers.letter(),
                                            Parsers.digit(),
                                            Parsers.characterOf("+-*/"),
                                            Parsers.characterOf("<=>"),
                                            Parsers.characterOf("?!")))));

    public static Parser<Expression> symbol() {
        return SYMBOL;
    }


    private static final Parser.Reference<Expression> REF_EXPRESSION = Parser.reference();

    private static final Parser<Expression> COMBINATION =
            Parsers.as(Utilities::newCombination,
                    Parsers.parenthesised(
                            Parsers.between(
                                    Parsers.optional(Parsers.whitespaces()),
                                    Parsers.oneOrMoreSeparatedBy(
                                            Parsers.whitespaces(),
                                            REF_EXPRESSION.parser()),
                                    Parsers.optional(Parsers.whitespaces()))));

    public static Parser<Expression> combination() {
        return COMBINATION;
    }

    private static final Parser<Expression> KEYWORD_QUOTE =
            Parsers.as(
                    Utilities::newSymbol,
                    Parsers.string("quote"));
    private static final Parser<Expression> QUOTE =
            Parsers.as(
                    Utilities::newQuote,
                    Parsers.anyOf(
                            Parsers.after(
                                    Parsers.string("'"),
                                    REF_EXPRESSION.parser()),
                            Parsers.parenthesised(
                                    Parsers.between(
                                            Parsers.optional(Parsers.whitespaces()),
                                            Parsers.as(
                                                    (List<Expression> expressions) -> expressions.get(1),
                                                    Parsers.separatedBy(
                                                            Parsers.whitespaces(),
                                                            KEYWORD_QUOTE,
                                                            REF_EXPRESSION.parser())),
                                            Parsers.optional(Parsers.whitespaces())))));

    public static Parser<Expression> quote() {
        return QUOTE;
    }

    private static final Parser<Expression> LAMBDA =
            Parsers.as(
                    Utilities::newLambda,
                    Parsers.parenthesised(
                            Parsers.between(
                                    Parsers.optional(Parsers.whitespaces()),
                                    Parsers.after(
                                            Parsers.string("lambda"),
                                            Parsers.after(
                                                    Parsers.whitespaces(),
                                                    Parsers.separatedBy(
                                                            Parsers.whitespaces(),
                                                            Parsers.parenthesised(
                                                                    Parsers.between(
                                                                            Parsers.optional(Parsers.whitespaces()),
                                                                            Parsers.as(
                                                                                    Utilities::newCombination,
                                                                                    Parsers.zeroOrMoreSeparatedBy(
                                                                                            Parsers.whitespaces(),
                                                                                            SYMBOL)),
                                                                            Parsers.optional(Parsers.whitespaces()))),
                                                            Parsers.as(
                                                                    Utilities::newCombination,
                                                                    Parsers.oneOrMoreSeparatedBy(
                                                                            Parsers.whitespaces(),
                                                                            REF_EXPRESSION.parser()))))),
                                    Parsers.optional(Parsers.whitespaces()))));

    public static Parser<Expression> lambda() {
        return LAMBDA;
    }

    private static final Parser<Expression> DEFINITION =
            Parsers.as(
                    Utilities::newDefinition,
                    Parsers.parenthesised(
                            Parsers.between(
                                    Parsers.optional(Parsers.whitespaces()),
                                    Parsers.after(
                                            Parsers.string("define"),
                                            Parsers.after(
                                                    Parsers.whitespaces(),
                                                    Parsers.anyOf(
                                                            Parsers.separatedBy(
                                                                    Parsers.whitespaces(),
                                                                    SYMBOL,
                                                                    REF_EXPRESSION.parser()),
                                                            Parsers.separatedBy(
                                                                    Parsers.whitespaces(),
                                                                    COMBINATION,
                                                                    Parsers.as(
                                                                            Utilities::newCombination,
                                                                            Parsers.oneOrMoreSeparatedBy(
                                                                                    Parsers.whitespaces(),
                                                                                    REF_EXPRESSION.parser())))))),
                                    Parsers.optional(Parsers.whitespaces()))));

    public static Parser<Expression> definition() {
        return DEFINITION;
    }

    private static final Parser<Expression> ASSIGNMENT =
            Parsers.as(
                    Utilities::newAssignment,
                    Parsers.parenthesised(
                            Parsers.between(
                                    Parsers.optional(Parsers.whitespaces()),
                                    Parsers.after(
                                            Parsers.string("set!"),
                                            Parsers.after(
                                                    Parsers.whitespaces(),
                                                    Parsers.separatedBy(
                                                            Parsers.whitespaces(),
                                                            SYMBOL,
                                                            REF_EXPRESSION.parser()))),
                                    Parsers.optional(Parsers.whitespaces()))));

    public static Parser<Expression> assignment() {
        return ASSIGNMENT;
    }

    private static final Parser<Expression> IF =
            Parsers.as(
                    Utilities::newIf,
                    Parsers.parenthesised(
                            Parsers.between(
                                    Parsers.optional(Parsers.whitespaces()),
                                    Parsers.after(
                                            Parsers.string("if"),
                                            Parsers.after(
                                                    Parsers.whitespaces(),
                                                    Parsers.separatedBy(
                                                            Parsers.whitespaces(),
                                                            REF_EXPRESSION.parser(),
                                                            REF_EXPRESSION.parser(),
                                                            REF_EXPRESSION.parser()))),
                                    Parsers.optional(Parsers.whitespaces()))));

    public static Parser<Expression> if_() {
        return IF;
    }

    private static final Parser<Expression> BEGIN =
            Parsers.as(
                    Utilities::newBegin,
                    Parsers.parenthesised(
                            Parsers.between(
                                    Parsers.optional(Parsers.whitespaces()),
                                    Parsers.after(
                                            Parsers.string("begin"),
                                            Parsers.after(
                                                    Parsers.whitespaces(),
                                                    Parsers.oneOrMoreSeparatedBy(
                                                            Parsers.whitespaces(),
                                                            REF_EXPRESSION.parser()))),
                                    Parsers.optional(Parsers.whitespaces()))));

    public static Parser<Expression> begin() {
        return BEGIN;
    }

    private static final Parser<Expression> EXPRESSION =
            Parsers.anyOf(
                    FRACTIONAL,
                    INTEGRAL,
                    STRING,
                    SYMBOL,
                    UNIT,
                    QUOTE,
                    LAMBDA,
                    DEFINITION,
                    ASSIGNMENT,
                    IF,
                    BEGIN,
                    COMBINATION);

    static {
        REF_EXPRESSION.set(EXPRESSION);
    }


    private static final Parser<List<Expression>> PROGRAM =
            Parsers.between(
                    Parsers.optional(Parsers.whitespaces()),
                    Parsers.oneOrMoreSeparatedBy(
                            Parsers.whitespaces(),
                            EXPRESSION),
                    Parsers.optional(Parsers.whitespaces()));

    public static Parser<List<Expression>> program() {
        return PROGRAM;
    }
}

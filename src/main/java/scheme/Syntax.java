package scheme;

import scheme.syntax.Parser;
import scheme.syntax.Parsers;

import java.util.List;
import java.util.regex.Pattern;

public final class Syntax {
    static final Parser<Expression> EXPRESSION_UNIT =
            Parsers.as(
                    Utilities::newUnitExpression,
                    Parsers.string("()"));

    private static final Pattern PATTERN_INTEGRAL = Pattern.compile("^\\d+");
    static final Parser<Expression> EXPRESSION_INTEGRAL =
            Parsers.as(
                    Utilities::newIntegralExpression,
                    Parsers.pattern(PATTERN_INTEGRAL));

    private static final Pattern PATTERN_FRACTIONAL = Pattern.compile("^(\\d+\\.\\d+|\\d+\\.|\\.\\d+)");
    static final Parser<Expression> EXPRESSION_FRACTIONAL =
            Parsers.as(
                    Utilities::newFractionalExpression,
                    Parsers.pattern(PATTERN_FRACTIONAL));

    static final Parser<Expression> EXPRESSION_STRING =
            Parsers.as(
                    Utilities::newStringExpression,
                    Parsers.between(
                            Parsers.character('"'),
                            Parsers.asString(Parsers.zeroOrMore(Parsers.characterExcept("\""))),
                            Parsers.character('"')));

    static final Parser<Expression> EXPRESSION_SYMBOL =
            Parsers.as(
                    Utilities::newSymbolExpression,
                    Parsers.asString(
                            Parsers.oneOrMore(
                                    Parsers.anyOf(
                                            Parsers.letter(),
                                            Parsers.digit(),
                                            Parsers.characterOf("+-*/"),
                                            Parsers.characterOf("<=>"),
                                            Parsers.characterOf("?!")))));

    private static final Parser.Reference<Expression> REF_EXPRESSION = Parser.reference();

    static final Parser<Expression> EXPRESSION_COMBINATION =
            Parsers.as(Utilities::newCombinationExpression,
                    Parsers.parenthesised(
                            Parsers.between(
                                    Parsers.optional(Parsers.whitespaces()),
                                    Parsers.oneOrMoreSeparatedBy(
                                            Parsers.whitespaces(),
                                            REF_EXPRESSION.parser()),
                                    Parsers.optional(Parsers.whitespaces()))));

    static final Parser<Expression> EXPRESSION_QUOTE =
            Parsers.as(
                    Utilities::newQuoteExpression,
                    Parsers.anyOf(
                            Parsers.after(
                                    Parsers.string("'"),
                                    REF_EXPRESSION.parser()),
                            Parsers.parenthesised(
                                    Parsers.between(
                                            Parsers.optional(Parsers.whitespaces()),
                                            Parsers.after(
                                                    Parsers.string("quote"),
                                                    Parsers.after(
                                                            Parsers.whitespaces(),
                                                            REF_EXPRESSION.parser())),
                                            Parsers.optional(Parsers.whitespaces())))));

    static final Parser<Expression> EXPRESSION_LAMBDA =
            Parsers.as(
                    Utilities::newLambdaExpression,
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
                                                                                    Utilities::newCombinationExpression,
                                                                                    Parsers.zeroOrMoreSeparatedBy(
                                                                                            Parsers.whitespaces(),
                                                                                            EXPRESSION_SYMBOL)),
                                                                            Parsers.optional(Parsers.whitespaces()))),
                                                            Parsers.as(
                                                                    Utilities::newCombinationExpression,
                                                                    Parsers.oneOrMoreSeparatedBy(
                                                                            Parsers.whitespaces(),
                                                                            REF_EXPRESSION.parser()))))),
                                    Parsers.optional(Parsers.whitespaces()))));

    static final Parser<Expression> EXPRESSION_DEFINITION =
            Parsers.as(
                    Utilities::newDefinitionExpression,
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
                                                                    EXPRESSION_SYMBOL,
                                                                    REF_EXPRESSION.parser()),
                                                            Parsers.separatedBy(
                                                                    Parsers.whitespaces(),
                                                                    EXPRESSION_COMBINATION,
                                                                    Parsers.as(
                                                                            Utilities::newCombinationExpression,
                                                                            Parsers.oneOrMoreSeparatedBy(
                                                                                    Parsers.whitespaces(),
                                                                                    REF_EXPRESSION.parser())))))),
                                    Parsers.optional(Parsers.whitespaces()))));

    static final Parser<Expression> EXPRESSION_ASSIGNMENT =
            Parsers.as(
                    Utilities::newAssignmentExpression,
                    Parsers.parenthesised(
                            Parsers.between(
                                    Parsers.optional(Parsers.whitespaces()),
                                    Parsers.after(
                                            Parsers.string("set!"),
                                            Parsers.after(
                                                    Parsers.whitespaces(),
                                                    Parsers.separatedBy(
                                                            Parsers.whitespaces(),
                                                            EXPRESSION_SYMBOL,
                                                            REF_EXPRESSION.parser()))),
                                    Parsers.optional(Parsers.whitespaces()))));

    static final Parser<Expression> EXPRESSION_IF =
            Parsers.as(
                    Utilities::newIfExpression,
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

    static final Parser<Expression> EXPRESSION_BEGIN =
            Parsers.as(
                    Utilities::newBeginExpression,
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

    static final Parser<Expression> EXPRESSION =
            Parsers.anyOf(
                    EXPRESSION_FRACTIONAL,
                    EXPRESSION_INTEGRAL,
                    EXPRESSION_STRING,
                    EXPRESSION_SYMBOL,
                    EXPRESSION_UNIT,
                    EXPRESSION_QUOTE,
                    EXPRESSION_LAMBDA,
                    EXPRESSION_DEFINITION,
                    EXPRESSION_ASSIGNMENT,
                    EXPRESSION_IF,
                    EXPRESSION_BEGIN,
                    EXPRESSION_COMBINATION);

    static {
        REF_EXPRESSION.set(EXPRESSION);
    }


    public static final Parser<List<Expression>> PROGRAM =
            Parsers.between(
                    Parsers.optional(Parsers.whitespaces()),
                    Parsers.oneOrMoreSeparatedBy(
                            Parsers.whitespaces(),
                            EXPRESSION),
                    Parsers.optional(Parsers.whitespaces()));
}

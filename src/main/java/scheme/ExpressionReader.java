package scheme;

import scheme.syntax.Parser;
import scheme.syntax.Result;

import java.io.BufferedReader;
import java.io.FilterReader;
import java.io.IOException;
import java.util.Deque;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

import static java.util.Arrays.asList;

public class ExpressionReader extends FilterReader {
    private final Parser<List<Expression>> syntax;
    private final Queue<Expression> queue = new LinkedList<>();

    private String remaining = "";

    public ExpressionReader(BufferedReader in, Parser<List<Expression>> syntax) {
        super(in);
        this.syntax = syntax;
    }

    public Expression nextExpression() throws IOException {
        if (!queue.isEmpty()) {
            return queue.poll();
        }

        String line;
        while ((line = ((BufferedReader) in).readLine()) != null) {
            if (!remaining.isEmpty()) {
                line = Strings.join(" ", asList(remaining, line));
            }

            Result<List<Expression>> result;
            while ((result = syntax.apply(line)).isSuccess()) {
                for (Expression expression : result.value()) {
                    queue.offer(expression);
                }

                line = result.remaining();
            }

            remaining = result.remaining();

            if (!queue.isEmpty()) {
                return queue.poll();
            }
        }

        return queue.poll();
    }
}

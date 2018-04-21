package scheme;

public interface Procedure extends Expression {
    Expression apply(Combination arguments);
}

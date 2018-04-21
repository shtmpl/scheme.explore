package scheme;

public interface Expression {
    Expression eval(Environment environment);
}

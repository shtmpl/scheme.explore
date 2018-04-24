package scheme.syntax;

public final class Result<X> {
    public static <X> Result<X> success(String remaining, X value) {
        return new Result<>(remaining, value);
    }

    public static <X> Result<X> failure(String remaining, String message) {
        return new Result<>(remaining, null, message);
    }


    private final String remaining;

    private final X value;
    private final String error;


    private Result(String remaining, X value, String... error) {
        this.remaining = remaining;
        this.value = value;
        this.error = error.length == 0 ? null : error[0];
    }

    public String remaining() {
        return remaining;
    }

    public X value() {
        return value;
    }

    public String error() {
        return error;
    }

    public boolean isSuccess() {
        return error == null;
    }

    public boolean isFailure() {
        return !isSuccess();
    }

    @Override
    public String toString() {
        if (error == null) {
            return String.format("Success: `%s` (Remaining: `%s`)", value, remaining);
        }

        return String.format("Failure: %s (Remaining: `%s`)", error, remaining);
    }
}

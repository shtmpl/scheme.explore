package scheme.syntax;

import java.util.concurrent.atomic.AtomicReference;

@FunctionalInterface
public interface Parser<X> {
    Result<X> apply(String input);

    final class Reference<X> extends AtomicReference<Parser<X>> {
        private final Parser<X> lazy = (String input) -> deref().apply(input);

        private Parser<X> deref() {
            return get();
        }

        public Parser<X> parser() {
            return lazy;
        }
    }

    static <X> Reference<X> reference() {
        return new Reference<>();
    }
}

package scheme;

public final class Strings {
    public static String trimPrefix(String string, String prefix) {
        if (string.startsWith(prefix)) {
            string = string.substring(prefix.length());
        }

        return string;
    }

    private static String trimSuffix(String string, String suffix) {
        if (string.endsWith(suffix)) {
            string = string.substring(0, string.length() - suffix.length());
        }

        return string;
    }

    public static String trimAffix(String string, String affix) {
        string = trimPrefix(string, affix);
        string = trimSuffix(string, affix);

        return string;
    }

    public static <X> String join(String delimiter, Iterable<X> xs) {
        StringBuilder result = new StringBuilder();
        String d = "";
        for (X x : xs) {
            result.append(d).append(x);
            d = delimiter;
        }

        return result.toString();
    }
}

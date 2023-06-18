package hexlet.code;

import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

public class Differ {

    private static final String PREFIX_EMPTY = "   ";
    private static final String PREFIX_MINUS = " - ";
    private static final String PREFIX_PLUS = " + ";

    public static String generate(Map<String, String> data1, Map<String, String> data2) {
        List<String> keys = Stream.concat(data1.keySet().stream(), data2.keySet().stream())
                .distinct()
                .sorted()
                .toList();

        StringBuilder sb = new StringBuilder();
        sb.append("{").append("\n");

        keys.forEach(key -> {
            if (data1.containsKey(key) && data2.containsKey(key)) {
                if (data1.get(key).equals(data2.get(key))) {
                    appendLine(sb, PREFIX_EMPTY, key, data1.get(key));
                } else {
                    appendLine(sb, PREFIX_MINUS, key, data1.get(key));
                    appendLine(sb, PREFIX_PLUS, key, data2.get(key));
                }
            } else if (!data2.containsKey(key) && data1.containsKey(key)) {
                appendLine(sb, PREFIX_MINUS, key, data1.get(key));
            } else {
                appendLine(sb, PREFIX_PLUS, key, data2.get(key));
            }
        });

        sb.append("}");
        return sb.toString();
    }

    private static void appendLine(StringBuilder sb, String prefix, String key, String value) {
        sb.append(String.format("%s%s: %s", prefix, key, value))
                .append("\n");
    }
}

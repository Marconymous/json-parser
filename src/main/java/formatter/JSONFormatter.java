package formatter;

public class JSONFormatter {
    public static String format(String json) {
        StringBuilder sb = new StringBuilder();
        int tabIndex = 0;
        boolean inString = false;

        for (char c : json.toCharArray()) {
            if (!inString) {
                if (c == '{' || c == '[') {
                    sb.append(c);
                    tabIndex++;
                    newLine(sb, tabIndex);
                } else if (c == '}' || c == ']') {
                    tabIndex--;
                    newLine(sb, tabIndex);
                    sb.append(c);
                } else if (c == ',') {
                    sb.append(c);
                    newLine(sb, tabIndex);
                } else if (c == '"') {
                    inString = true;
                } else {
                    sb.append(c);
                }
            } else {
                if (c == '"') inString = false;

                sb.append(c);
            }
        }
        return sb.toString();
    }

    private static void newLine(StringBuilder s, int tabIndex) {
        s.append("\n").append("\t".repeat(tabIndex));
    }
}

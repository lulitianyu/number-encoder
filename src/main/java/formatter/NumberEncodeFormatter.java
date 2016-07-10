package formatter;

/**
 * {@link NumberEncodeFormatter} formats the phone number and matching words
 * into a appropriate string.
 */
public class NumberEncodeFormatter {
    private static final String NEW_LINE = System.getProperty("line.separator");

    public static String format(String phoneNumber, String[] words) {
        StringBuilder builder = new StringBuilder();

        for (String w : words) {
            builder.append(phoneNumber);
            builder.append(": ");
            builder.append(w);
            builder.append(NEW_LINE);
        }
        return builder.toString();
    }
}

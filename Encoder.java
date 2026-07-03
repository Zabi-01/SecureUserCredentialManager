import java.util.Base64;

public class Encoder {
    public static String encode(String s) {
        return s == null ? "" : Base64.getEncoder().encodeToString(s.getBytes());
    }

    public static String decode(String s) {
        if (s == null || s.isEmpty()) return "";
        try {
            return new String(Base64.getDecoder().decode(s));
        } catch (Exception e) {
            return "[DECODE ERROR]";
        }
    }
}
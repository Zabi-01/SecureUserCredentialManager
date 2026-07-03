import java.util.Arrays;

public class Validator {
    private static final String[] COMMON = {"123456", "password", "qwerty", "abc123"};

    public static boolean isStrongPassword(String p) {
        if (p == null || p.length() < 8) return false;
        if (Arrays.stream(COMMON).anyMatch(c -> c.equalsIgnoreCase(p))) return false;

        boolean up = false, low = false, dig = false, spec = false;
        for (char c : p.toCharArray()) {
            if (Character.isUpperCase(c)) up = true;
            if (Character.isLowerCase(c)) low = true;
            if (Character.isDigit(c)) dig = true;
            if ("!@#$%^&*()_+-=".indexOf(c) >= 0) spec = true;
        }
        return up && low && dig && spec;
    }
}
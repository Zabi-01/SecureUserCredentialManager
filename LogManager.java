import java.io.*;
import java.text.SimpleDateFormat;
import java.util.Date;

public class LogManager {
    private static final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    public static void log(String user, String action) {
        String time = sdf.format(new Date());
        String line = "[" + time + "] " + user + " → " + action + "\n";
        try (FileWriter fw = new FileWriter("logs/system.log", true)) {
            fw.write(line);
        } catch (Exception ignored) {}
    }
}
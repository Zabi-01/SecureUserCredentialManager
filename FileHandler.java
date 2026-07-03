import java.io.*;
import java.util.*;

public class FileHandler {

    public static void initializeDirectories() {
        new File("users").mkdirs();
        new File("logs").mkdirs();

        try {
            new File("logs/system.log").createNewFile();
        } catch (IOException e) {
            // error handled silently as per project style
        }
    }

    public static List<PasswordEntry> loadVault(String username) {
        List<PasswordEntry> list = new ArrayList<>();
        File file = new File("users/" + username + "/vault.dat");
        if (!file.exists()) return list;

        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = br.readLine()) != null) {
                if (line.trim().isEmpty()) continue;
                String[] p = line.split("\\|", 4);
                String notes = p.length == 4 ? p[3] : "";
                list.add(new PasswordEntry(p[0], p[1], p[2], notes));
            }
        } catch (Exception e) {
            LogManager.log(username, "Vault load failed");
        }
        return list;
    }

    public static void saveVault(String username, List<PasswordEntry> entries) {
        try (PrintWriter pw = new PrintWriter(
                new FileWriter("users/" + username + "/vault.dat"))) {
            for (PasswordEntry e : entries) {
                pw.println(
                    e.site + "|" +
                    e.username + "|" +
                    e.encodedPassword + "|" +
                    e.notes
                );
            }
        } catch (Exception e) {
            LogManager.log(username, "Vault save failed");
        }
    }
}

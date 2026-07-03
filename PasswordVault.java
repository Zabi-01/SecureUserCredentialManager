import java.util.*;

public class PasswordVault {
    private List<PasswordEntry> entries = new ArrayList<>();

    public void addEntry(PasswordEntry e) { entries.add(e); }
    public void deleteEntry(int index) { if (index >= 0 && index < entries.size()) entries.remove(index); }
    public List<PasswordEntry> getAll() { return new ArrayList<>(entries); }

    public void loadFromFile(String username) {
        entries = FileHandler.loadVault(username);
    }

    public void saveToFile(String username) {
        FileHandler.saveVault(username, entries);
    }
}
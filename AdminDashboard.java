import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.io.*;

public class AdminDashboard {
    private JPanel panel;

    public AdminDashboard(SecurePassManager app) {
        panel = new JPanel(new BorderLayout());
        JTabbedPane tabs = new JTabbedPane();

        // Tab 1: All Users
        DefaultTableModel model = new DefaultTableModel(
                new Object[]{"Username", "Email", "Passwords Saved"}, 0);
        JTable table = new JTable(model);
        loadUsers(model);
        tabs.addTab("All Users", new JScrollPane(table));

        // Tab 2: System Logs
        JTextArea logArea = new JTextArea(20, 60);
        logArea.setEditable(false);
        logArea.setFont(new Font("Monospaced", Font.PLAIN, 12));
        logArea.setText(readFile("logs/system.log"));
        tabs.addTab("System Logs", new JScrollPane(logArea));

        // Tab 3: View Any User's Vault
        JPanel vaultPanel = new JPanel(new BorderLayout());
        JTextArea vaultArea = new JTextArea(20, 70);
        vaultArea.setEditable(false);
        vaultArea.setFont(new Font("Monospaced", Font.PLAIN, 12));

        JComboBox<String> userBox = new JComboBox<>();
        JButton loadBtn = new JButton("Load Vault");

        // Load usernames using simple File (NO NIO!)
        File usersFolder = new File("users");
        if (usersFolder.exists() && usersFolder.isDirectory()) {
            File[] userDirs = usersFolder.listFiles();
            if (userDirs != null) {
                for (File dir : userDirs) {
                    if (dir.isDirectory()) {
                        userBox.addItem(dir.getName());
                    }
                }
            }
        }

        loadBtn.addActionListener(e -> {
            String selected = (String) userBox.getSelectedItem();
            if (selected != null) {
                String content = readFile("users/" + selected + "/vault.dat");
                vaultArea.setText("=== ENCODED VAULT: " + selected + " ===\n\n" + content);
                LogManager.log("ADMIN", "Viewed vault of " + selected);
            }
        });

        JPanel top = new JPanel();
        top.add(new JLabel("Select User:"));
        top.add(userBox);
        top.add(loadBtn);

        vaultPanel.add(top, BorderLayout.NORTH);
        vaultPanel.add(new JScrollPane(vaultArea), BorderLayout.CENTER);
        tabs.addTab("View Vaults", vaultPanel);

        panel.add(tabs, BorderLayout.CENTER);

        JButton logout = new JButton("Logout");
        logout.setFont(new Font("Arial", Font.BOLD, 16));
        logout.addActionListener(e -> app.showScreen("Login"));
        panel.add(logout, BorderLayout.SOUTH);
    }

    private void loadUsers(DefaultTableModel model) {
        model.setRowCount(0);
        File usersFolder = new File("users");
        if (!usersFolder.exists()) return;

        File[] dirs = usersFolder.listFiles();
        if (dirs == null) return;

        for (File dir : dirs) {
            if (dir.isDirectory()) {
                String username = dir.getName();

                // Read email
                String email = "N/A";
                File profile = new File(dir, "profile.dat");
                if (profile.exists()) {
                    try (BufferedReader br = new BufferedReader(new FileReader(profile))) {
                        email = br.readLine();
                        if (email == null) email = "No email";
                    } catch (Exception ignored) {}
                }

                // Count passwords
                int count = 0;
                File vault = new File(dir, "vault.dat");
                if (vault.exists()) {
                    try (BufferedReader br = new BufferedReader(new FileReader(vault))) {
                        while (br.readLine() != null) count++;
                    } catch (Exception ignored) {}
                }

                model.addRow(new Object[]{username, email, count});
            }
        }
    }

    private String readFile(String path) {
        File file = new File(path);
        if (!file.exists()) return "File not found: " + path;

        StringBuilder sb = new StringBuilder();
        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = br.readLine()) != null) {
                sb.append(line).append("\n");
            }
        } catch (Exception e) {
            return "Error reading file: " + path;
        }
        return sb.toString();
    }

    public JPanel getPanel() {
        return panel;
    }
}
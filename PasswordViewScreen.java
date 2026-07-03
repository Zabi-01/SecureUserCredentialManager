import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class PasswordViewScreen {
    private JPanel panel;
    private JTable table;
    private DefaultTableModel model;
    private boolean showingPlain = false;
    private SecurePassManager app;
    private PasswordVault vault;
    private UserDashboard dashboard;

    public PasswordViewScreen(SecurePassManager app, PasswordVault vault, UserDashboard dashboard) {
        this.app = app;
        this.vault = vault;
        this.dashboard = dashboard;

        panel = new JPanel(new BorderLayout());

        // Table setup
        model = new DefaultTableModel(new Object[]{"Site", "Username", "Password", "Notes"}, 0);
        table = new JTable(model);
        table.setFont(new Font("Arial", Font.PLAIN, 14));
        table.getTableHeader().setFont(new Font("Arial", Font.BOLD, 14));
        panel.add(new JScrollPane(table), BorderLayout.CENTER);

        // Buttons
        JButton toggleBtn = new JButton("Show Passwords");
        JButton deleteBtn = new JButton("Delete Selected");
        JButton backBtn = new JButton("Back to Dashboard");

        JPanel bottom = new JPanel();
        bottom.add(toggleBtn);
        bottom.add(deleteBtn);
        bottom.add(backBtn);
        panel.add(bottom, BorderLayout.SOUTH);

        refreshTable();  // Load passwords

        // Toggle show/hide passwords
        toggleBtn.addActionListener(e -> {
            showingPlain = !showingPlain;
            toggleBtn.setText(showingPlain ? "Hide Passwords" : "Show Passwords");
            refreshTable();
        });

        // Delete selected row
        deleteBtn.addActionListener(e -> {
            int row = table.getSelectedRow();
            if (row == -1) {
                JOptionPane.showMessageDialog(panel, "Please select a password to delete!");
                return;
            }
            if (JOptionPane.showConfirmDialog(panel, "Delete this password permanently?") == JOptionPane.YES_OPTION) {
                vault.deleteEntry(row);
                vault.saveToFile(app.getCurrentUser());
                refreshTable();
                LogManager.log(app.getCurrentUser(), "Deleted a password entry");
                JOptionPane.showMessageDialog(panel, "Password deleted!");
            }
        });

        // Back button
        backBtn.addActionListener(e -> {
            dashboard.refresh();
            app.showScreen("Dashboard");
        });
    }

    private void refreshTable() {
        model.setRowCount(0);
        List<PasswordEntry> entries = vault.getAll();
        for (PasswordEntry e : entries) {
            String password = showingPlain ? e.getPlainPassword() : "••••••••";
            model.addRow(new Object[]{e.site, e.username, password, e.notes});
        }
    }

    public JPanel getPanel() {
        return panel;
    }
}
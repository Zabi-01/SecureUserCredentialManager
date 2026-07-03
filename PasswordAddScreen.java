import javax.swing.*;
import java.awt.*;

public class PasswordAddScreen {
    private JPanel panel;
    private SecurePassManager app;
    private PasswordVault vault;
    private UserDashboard dashboard;

    public PasswordAddScreen(SecurePassManager app, PasswordVault vault, UserDashboard dashboard) {
        this.app = app;
        this.vault = vault;
        this.dashboard = dashboard;

        panel = new JPanel(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints();
        c.insets = new Insets(10, 10, 10, 10);
        c.fill = GridBagConstraints.HORIZONTAL;

        JTextField siteField = new JTextField(25);
        JTextField userField = new JTextField(25);
        JPasswordField passField = new JPasswordField(25);
        JTextField notesField = new JTextField(25);

        c.gridx = 0; c.gridy = 0; panel.add(new JLabel("Site/App:"), c);
        c.gridx = 1; panel.add(siteField, c);

        c.gridx = 0; c.gridy++; panel.add(new JLabel("Username/Email:"), c);
        c.gridx = 1; panel.add(userField, c);

        c.gridx = 0; c.gridy++; panel.add(new JLabel("Password:"), c);
        c.gridx = 1; panel.add(passField, c);

        c.gridx = 0; c.gridy++; panel.add(new JLabel("Notes:"), c);
        c.gridx = 1; panel.add(notesField, c);

        JButton saveBtn = new JButton("Save");
        JButton cancelBtn = new JButton("Cancel");

        c.gridx = 0; c.gridy++; c.gridwidth = 2;
        JPanel btns = new JPanel();
        btns.add(cancelBtn);
        btns.add(saveBtn);
        panel.add(btns, c);

        saveBtn.addActionListener(e -> {
            String site = siteField.getText().trim();
            String user = userField.getText().trim();
            String pass = new String(passField.getPassword());
            String notes = notesField.getText();

            if (site.isEmpty() || pass.isEmpty()) {
                JOptionPane.showMessageDialog(panel, "Site and Password required!");
                return;
            }

            PasswordEntry entry = new PasswordEntry(site, user, Encoder.encode(pass), notes);
            vault.addEntry(entry);
            vault.saveToFile(app.getCurrentUser());
            LogManager.log(app.getCurrentUser(), "Added password for " + site);

            JOptionPane.showMessageDialog(panel, "Password saved!");
            dashboard.refresh();
            app.showScreen("Dashboard");
        });

        cancelBtn.addActionListener(e -> app.showScreen("Dashboard"));
    }

    public JPanel getPanel() { return panel; }
}
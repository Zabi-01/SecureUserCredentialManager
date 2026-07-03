import javax.swing.*;
import java.awt.*;

public class UserDashboard {
    private JPanel panel;
    private SecurePassManager app;
    private PasswordVault vault;

    public UserDashboard(SecurePassManager app) {
        this.app = app;
        this.vault = new PasswordVault();
        vault.loadFromFile(app.getCurrentUser());

        panel = new JPanel(new BorderLayout());

        JLabel welcome = new JLabel("Welcome, " + app.getCurrentUser() + "!", SwingConstants.CENTER);
        welcome.setFont(new Font("Arial", Font.BOLD, 28));
        welcome.setBorder(BorderFactory.createEmptyBorder(30, 0, 30, 0));
        panel.add(welcome, BorderLayout.NORTH);

        JButton addBtn = new JButton("Add New Password");
        JButton viewBtn = new JButton("View Saved Passwords");
        JButton logoutBtn = new JButton("Logout");

        JPanel buttons = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 20));
        buttons.add(addBtn);
        buttons.add(viewBtn);
        buttons.add(logoutBtn);
        panel.add(buttons, BorderLayout.CENTER);

        // ADD PASSWORD SCREEN
        addBtn.addActionListener(e -> {
            app.getMainPanel().add(new PasswordAddScreen(app, vault, this).getPanel(), "Add");
            app.showScreen("Add");
        });

       
        viewBtn.addActionListener(e -> {
            app.getMainPanel().add(new PasswordViewScreen(app, vault, this).getPanel(), "View");
            app.showScreen("View");
        });

        // LOGOUT
        logoutBtn.addActionListener(e -> {
            app.setCurrentUser(null);
            app.showScreen("Login");
            LogManager.log("Guest", "User logged out");
        });
    }

    public void refresh() {
        vault.loadFromFile(app.getCurrentUser());
    }

    public JPanel getPanel() {
        return panel;
    }
}
import javax.swing.*;
import java.awt.*;
import java.io.*;

public class LoginScreen {
    private JPanel panel;
    private SecurePassManager app;

    public LoginScreen(SecurePassManager app) {
        this.app = app;
        panel = new JPanel(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints();
        c.insets = new Insets(15, 15, 15, 15);
        c.fill = GridBagConstraints.HORIZONTAL;

        JLabel title = new JLabel("SecurePass Manager", SwingConstants.CENTER);
        title.setFont(new Font("Arial", Font.BOLD, 30));
        c.gridx = 0; c.gridy = 0; c.gridwidth = 2;
        panel.add(title, c);

        JTextField userField = new JTextField(20);
        JPasswordField passField = new JPasswordField(20);

        c.gridwidth = 1; c.gridy++;
        panel.add(new JLabel("Username:"), c);
        c.gridx = 1; panel.add(userField, c);

        c.gridx = 0; c.gridy++;
        panel.add(new JLabel("Password:"), c);
        c.gridx = 1; panel.add(passField, c);

        JButton loginBtn = new JButton("User Login");
        JButton signupBtn = new JButton("Sign Up");
        JButton adminBtn = new JButton("Admin Login");

        c.gridx = 0; c.gridy++; c.gridwidth = 2;
        JPanel buttons = new JPanel();
        buttons.add(loginBtn); buttons.add(signupBtn); buttons.add(adminBtn);
        panel.add(buttons, c);

        loginBtn.addActionListener(e -> {
            String username = userField.getText().trim();
            String password = new String(passField.getPassword());

            if (username.isEmpty() || password.isEmpty()) {
                JOptionPane.showMessageDialog(panel, "Fill all fields!");
                return;
            }

            File file = new File("users/" + username + "/profile.dat");
            if (!file.exists()) {
                JOptionPane.showMessageDialog(panel, "User not found!");
                return;
            }

            try (BufferedReader br = new BufferedReader(new FileReader(file))) {
                br.readLine(); // email
                String storedPass = br.readLine();
                String mfaCode = br.readLine();

                if (!Encoder.decode(storedPass).equals(password)) {
                    JOptionPane.showMessageDialog(panel, "Wrong password!");
                    return;
                }

                String input = JOptionPane.showInputDialog("Enter your 6-digit MFA code:");
                if (input == null || !input.equals(mfaCode)) {
                    JOptionPane.showMessageDialog(panel, "Invalid MFA Code!");
                    return;
                }

                app.setCurrentUser(username);
                app.getMainPanel().add(new UserDashboard(app).getPanel(), "Dashboard");
                app.showScreen("Dashboard");
                LogManager.log(username, "Login successful");

            } catch (Exception ex) {
                JOptionPane.showMessageDialog(panel, "Login error!");
            }
        });

        signupBtn.addActionListener(e -> app.showScreen("Signup"));

        adminBtn.addActionListener(e -> {
            String pass = JOptionPane.showInputDialog("Admin Password:");
            if ("admin123".equals(pass)) {
                String code = JOptionPane.showInputDialog("Admin MFA Code:");
                if ("123456".equals(code)) {
                    app.setCurrentUser("ADMIN");
                    app.getMainPanel().add(new AdminDashboard(app).getPanel(), "AdminDashboard");
                    app.showScreen("AdminDashboard");
                    LogManager.log("ADMIN", "Admin login successful");
                } else {
                    JOptionPane.showMessageDialog(panel, "Wrong MFA Code!");
                }
            } else if (pass != null) {
                JOptionPane.showMessageDialog(panel, "Wrong admin password!");
            }
        });
    }

    public JPanel getPanel() { return panel; }
}
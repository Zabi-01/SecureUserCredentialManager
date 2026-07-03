import javax.swing.*;
import java.awt.*;
import java.io.*;

public class SignupScreen {
    private JPanel panel;
    private SecurePassManager app;

    public SignupScreen(SecurePassManager app) {
        this.app = app;
        panel = new JPanel(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints();
        c.insets = new Insets(10, 10, 10, 10);
        c.fill = GridBagConstraints.HORIZONTAL;

        JLabel title = new JLabel("Create Account", SwingConstants.CENTER);
        title.setFont(new Font("Arial", Font.BOLD, 28));
        c.gridx = 0; c.gridy = 0; c.gridwidth = 2;
        panel.add(title, c);

        JTextField userField = new JTextField(20);
        JTextField emailField = new JTextField(20);
        JPasswordField passField = new JPasswordField(20);
        JPasswordField confirmField = new JPasswordField(20);

        c.gridwidth = 1; c.gridy++;
        panel.add(new JLabel("Username:"), c);
        c.gridx = 1; panel.add(userField, c);

        c.gridx = 0; c.gridy++;
        panel.add(new JLabel("Email:"), c);
        c.gridx = 1; panel.add(emailField, c);

        c.gridx = 0; c.gridy++;
        panel.add(new JLabel("Password:"), c);
        c.gridx = 1; panel.add(passField, c);

        c.gridx = 0; c.gridy++;
        panel.add(new JLabel("Confirm:"), c);
        c.gridx = 1; panel.add(confirmField, c);

        JButton signupBtn = new JButton("Create Account");
        JButton backBtn = new JButton("Back");

        c.gridx = 0; c.gridy++; c.gridwidth = 2;
        JPanel btns = new JPanel();
        btns.add(backBtn); btns.add(signupBtn);
        panel.add(btns, c);

        signupBtn.addActionListener(e -> {
            String username = userField.getText().trim();
            String email = emailField.getText().trim();
            String p1 = new String(passField.getPassword());
            String p2 = new String(confirmField.getPassword());

            if (username.isEmpty() || p1.isEmpty()) {
                JOptionPane.showMessageDialog(panel, "Fill required fields!");
                return;
            }
            if (!email.toLowerCase().contains("@gmail.com")) {
                JOptionPane.showMessageDialog(panel, "Only Gmail accounts allowed!\nUse an email ending with @gmail.com");
                return;
            }
            if (!p1.equals(p2)) {
                JOptionPane.showMessageDialog(panel, "Passwords don't match!");
                return;
            }
            if (!Validator.isStrongPassword(p1)) {
                JOptionPane.showMessageDialog(panel, "Weak password! Need 8+ chars, upper, lower, digit, special.");
                return;
            }

            File dir = new File("users/" + username);
            if (dir.exists()) {
                JOptionPane.showMessageDialog(panel, "Username taken!");
                return;
            }

            String mfa = String.format("%06d", (int)(Math.random() * 1000000));

            try {
                dir.mkdirs();
                PrintWriter pw = new PrintWriter("users/" + username + "/profile.dat");
                pw.println(email);
                pw.println(Encoder.encode(p1));
                pw.println(mfa);
                pw.close();

                JOptionPane.showMessageDialog(panel,
                        "Account Created!\n\nYour MFA Code: " + mfa +
                                "\n\nSAVE IT — you need it every login!", "MFA Code", JOptionPane.INFORMATION_MESSAGE);

                app.showScreen("Login");
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(panel, "Error creating account!");
            }
        });

        backBtn.addActionListener(e -> app.showScreen("Login"));
    }

    public JPanel getPanel() { return panel; }
}
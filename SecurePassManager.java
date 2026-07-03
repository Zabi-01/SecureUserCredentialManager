import javax.swing.*;
import java.awt.*;

public class SecurePassManager {
    private JFrame frame;
    private CardLayout cardLayout;
    private JPanel mainPanel;
    private String currentUser = null;

    public SecurePassManager() {
        FileHandler.initializeDirectories();
        setupGUI();
    }

    private void setupGUI() {
        frame = new JFrame("SecurePass Manager");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(900, 600);
        frame.setLocationRelativeTo(null);

        cardLayout = new CardLayout();
        mainPanel = new JPanel(cardLayout);

        mainPanel.add(new LoginScreen(this).getPanel(), "Login");
        mainPanel.add(new SignupScreen(this).getPanel(), "Signup");

        frame.add(mainPanel);
        frame.setVisible(true);
    }

    public void showScreen(String name) {
        cardLayout.show(mainPanel, name); 
    }
    public void setCurrentUser(String username) {
        this.currentUser = username; 
    }
    public String getCurrentUser() { 
        return currentUser; }
    public JPanel getMainPanel() { return mainPanel; }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(SecurePassManager::new);
    }
}
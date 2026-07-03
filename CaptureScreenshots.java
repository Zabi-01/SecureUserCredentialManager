import java.awt.*;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;
import javax.swing.*;
import java.io.File;

/**
 * Utility to capture screenshots of the GUI screens for README documentation.
 * Run this AFTER compiling all source files.
 * Usage: java CaptureScreenshots
 */
public class CaptureScreenshots {

    public static void main(String[] args) throws Exception {
        File screenshotsDir = new File("screenshots");
        screenshotsDir.mkdirs();

        SwingUtilities.invokeAndWait(() -> {
            try {
                FileHandler.initializeDirectories();
                // --- Login Screen ---
                captureScreen("Login", screenshotsDir);
                System.out.println("[OK] Captured Login screen");
            } catch (Exception e) {
                e.printStackTrace();
            }
        });

        System.out.println("\nScreenshots saved to: screenshots/");
        System.out.println("Upload these to your GitHub repo under screenshots/ folder.");
        System.exit(0);
    }

    private static void captureScreen(String screenName, File outDir) throws Exception {
        JFrame frame = new JFrame("SecurePass Manager - " + screenName);
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setSize(900, 600);
        frame.setLocationRelativeTo(null);

        CardLayout cardLayout = new CardLayout();
        JPanel mainPanel = new JPanel(cardLayout);

        SecurePassManager dummyApp = new SecurePassManager() {
            { /* prevent full init */ }
        };

        if (screenName.equals("Login")) {
            mainPanel.add(new LoginScreen(dummyApp).getPanel(), "Login");
        } else if (screenName.equals("Signup")) {
            mainPanel.add(new SignupScreen(dummyApp).getPanel(), "Signup");
        }

        frame.add(mainPanel);
        frame.setVisible(true);

        Thread.sleep(500); // let it render

        BufferedImage img = new BufferedImage(
                frame.getWidth(), frame.getHeight(), BufferedImage.TYPE_INT_RGB);
        frame.paint(img.getGraphics());

        File out = new File(outDir, screenName.toLowerCase() + "_screen.png");
        ImageIO.write(img, "png", out);
        frame.dispose();
    }
}

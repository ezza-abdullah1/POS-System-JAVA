package view;

import javax.swing.*;
import java.awt.*;
import java.util.Timer;
import java.util.TimerTask;

public class SplashScreenView {

    public static void main(String[] args) {
        // Show the splash screen
        SwingUtilities.invokeLater(() -> showSplashScreen());
    }

    public static void showSplashScreen() {
        // Create a JWindow (Splash Screen Window)
        JWindow splash = new JWindow();

        // Get screen size to adjust the splash screen size dynamically
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        splash.setSize(screenSize.width / 2, screenSize.height / 2); // Set the splash size to half of screen size
        splash.setLocationRelativeTo(null); // Center the window

        // Create a JPanel for the splash screen content
        JPanel content = new JPanel() {
            // Override paintComponent for custom gradient background
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g;
                
                // Set gradient background from #00296b to #003f88
                GradientPaint gradient = new GradientPaint(0, 0, new Color(0x00296b), getWidth(), getHeight(), new Color(0x003f88));
                g2d.setPaint(gradient);
                g2d.fillRect(0, 0, getWidth(), getHeight());

                // Set the font for the text
                Font font = new Font("Segoe UI", Font.BOLD, 60); // Use Segoe UI font, bold, 60px
                g2d.setFont(font);
                g2d.setColor(Color.WHITE); // White text color
                String text = "Metro Pakistan";

                // Get the size of the text and calculate the position to center it
                FontMetrics metrics = g2d.getFontMetrics(font);
                int x = (getWidth() - metrics.stringWidth(text)) / 2;
                int y = (getHeight() + metrics.getHeight()) / 2 - metrics.getDescent();
                
                // Draw the text at the calculated position
                g2d.drawString(text, x, y);
            }
        };

        content.setLayout(new BorderLayout());
        content.setBorder(BorderFactory.createEmptyBorder(30, 30, 30, 30)); // Add padding to content

        // Add a modern-looking progress bar
        JProgressBar progressBar = new JProgressBar();
        progressBar.setIndeterminate(true); // Indeterminate progress bar (no percentage shown)
        progressBar.setPreferredSize(new Dimension(400, 10)); // Adjust size for aesthetics
        progressBar.setBackground(new Color(240, 240, 240));
        progressBar.setForeground(Color.decode("#ffd500")); // Set the color using hexadecimal code // Light blue color for progress bar
        content.add(progressBar, BorderLayout.SOUTH);

        // Set the content for the splash window
        splash.setContentPane(content);

        // Smoothly adjust the splash screen to fit the screen size
        splash.setOpacity(0f);
        Timer fadeInTimer = new Timer();
        fadeInTimer.schedule(new TimerTask() {
            private int opacity = 0;

            @Override
            public void run() {
                if (opacity < 100) {
                    float currentOpacity = opacity / 100f;
                    SwingUtilities.invokeLater(() -> splash.setOpacity(currentOpacity)); // Fade-in effect
                    opacity++;
                } else {
                    fadeInTimer.cancel(); // Stop the timer once fully visible
                }
            }
        }, 0, 10);

        // Show the splash screen with a timer for a few seconds (simulate loading)
        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                // Hide the splash screen with a fade-out effect
                fadeOut(splash);
                // Launch the main login screen
                SwingUtilities.invokeLater(SplashScreenView::launchLoginScreen);
            }
        }, 5000); // Show for 5 seconds (5000 ms)

        // Show the splash window
        splash.setVisible(true);
    }

    // Smooth fade-out transition effect
    public static void fadeOut(JWindow window) {
        Timer timer = new Timer();
        timer.scheduleAtFixedRate(new TimerTask() {
            private int opacity = 100; // opacity variable to control fading

            @Override
            public void run() {
                if (opacity > 0) {
                    final float currentOpacity = opacity / 100f; // Convert to float between 0 and 1
                    SwingUtilities.invokeLater(() -> {
                        window.setOpacity(currentOpacity); // Update window opacity
                    });
                    opacity--; // Decrease opacity by 1
                } else {
                    window.setVisible(false); // Hide the window once opacity reaches 0
                    timer.cancel(); // Stop the timer
                }
            }
        }, 0, 10); // Run the task immediately, repeat every 10 ms
    }

    // Method to launch the main login screen
    public static void launchLoginScreen() {
        JFrame loginFrame = new JFrame("POS System - Login");
        loginFrame.setSize(400, 300);
        loginFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        loginFrame.setLocationRelativeTo(null); // Center the frame

        // Add simple login form
        JPanel loginPanel = new JPanel();
        loginPanel.setLayout(new GridLayout(3, 2));

        loginPanel.add(new JLabel("Username:"));
        JTextField usernameField = new JTextField();
        loginPanel.add(usernameField);

        loginPanel.add(new JLabel("Password:"));
        JPasswordField passwordField = new JPasswordField();
        loginPanel.add(passwordField);

        JButton loginButton = new JButton("Login");
        loginButton.addActionListener(e -> {
            // Placeholder login logic (you can replace this with actual login logic)
            JOptionPane.showMessageDialog(loginFrame, "Login successful!");
        });
        loginPanel.add(new JLabel());
        loginPanel.add(loginButton);

        loginFrame.add(loginPanel, BorderLayout.CENTER);
        loginFrame.setVisible(true);
    }
}

package view;

import BranchManager.view.DashboardBR;
import utils.RoundedButton;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionListener;

public class LoginView extends JFrame {
    private JTextField emailField;
    private JPasswordField passwordField;
    private String role;
    private Color primaryColor = new Color(63, 81, 181);
    private Color hoverColor = new Color(92, 107, 192);
    private RoundedButton loginButton;

    public LoginView(String selectedRole) {
        this.role = selectedRole; // Set the role from the SplashScreenView
        setTitle("POS System - Login");
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        setSize(screenSize.width / 2, screenSize.height / 2); // Half screen size

        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BorderLayout());
        mainPanel.setBackground(new Color(245, 245, 245)); // Light gray background

        // Create panels for the left and right sides
        JPanel leftPanel = createLogoPanel(); // Metro-style logo on the left
        JPanel rightPanel = createLoginPanel(); // Login credentials panel on the right

        // Add the panels to the main panel (left and right split)
        mainPanel.add(leftPanel, BorderLayout.WEST);
        mainPanel.add(rightPanel, BorderLayout.EAST);

        setContentPane(mainPanel);
    }

    // Left side: Metro-style logo panel with vertical "M E T R O"
    private JPanel createLogoPanel() {
        JPanel panel = new JPanel();
        panel.setBackground(new Color(0x003566));
        panel.setLayout(new GridBagLayout()); // Use GridBagLayout for better control

        JLabel logoLabel = new JLabel("M E T R O ", JLabel.CENTER);
        logoLabel.setFont(new Font("Segoe UI", Font.BOLD, 50));
        logoLabel.setForeground(Color.WHITE);

        // Align label vertically in the center
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        panel.add(logoLabel, gbc);
        panel.setPreferredSize(new Dimension(getWidth() / 2, getHeight()));
        return panel;
    }

    private JPanel createLoginPanel() {
        // Main panel for the login screen
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS)); // Use BoxLayout for vertical arrangement
        panel.setOpaque(true);
        panel.setBorder(new EmptyBorder(20, 40, 40, 40));

        // Back Button
        RoundedButton backButton = new RoundedButton("<<");
        backButton.setFont(new Font("Segoe UI", Font.BOLD, 16));
        backButton.setPreferredSize(new Dimension(40, 10)); // Make the back button small
        backButton.setBorder(BorderFactory.createLineBorder(Color.white));
        backButton.addActionListener(e -> navigateBackToSplash());

        // Panel for the back button (top-left)
        JPanel backButtonPanel = new JPanel();
        backButtonPanel.setLayout(new BorderLayout());
        backButtonPanel.add(backButton, BorderLayout.WEST);
        backButtonPanel.setOpaque(false); // Make it transparent

        // Add back button panel to the top of the main panel
        panel.add(backButtonPanel);

        // Create the login form panel with GridBagLayout
        JPanel loginFormPanel = new JPanel();
        loginFormPanel.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(15, 10, 15, 10); // Adds space between components

        // Login Title
        JLabel titleLabel = new JLabel("         Login");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 34));
        gbc.gridwidth = 2;
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.anchor = GridBagConstraints.CENTER; // Center the title
        loginFormPanel.add(titleLabel, gbc);

        // Email Label and TextField
        JLabel emailLabel = new JLabel("Email:");
        emailLabel.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        gbc.gridwidth = 1;
        gbc.gridx = 0;
        gbc.gridy = 2;
        loginFormPanel.add(emailLabel, gbc);

        emailField = new JTextField();
        emailField.setPreferredSize(new Dimension(200, 30));
        emailField.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        emailField.setBorder(BorderFactory.createLineBorder(primaryColor));
        gbc.gridx = 1;
        loginFormPanel.add(emailField, gbc);

        // Password Label and PasswordField
        JLabel passwordLabel = new JLabel("Password:");
        passwordLabel.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        gbc.gridx = 0;
        gbc.gridy = 3;
        loginFormPanel.add(passwordLabel, gbc);

        passwordField = new JPasswordField();
        passwordField.setPreferredSize(new Dimension(200, 30));
        passwordField.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        passwordField.setBorder(BorderFactory.createLineBorder(primaryColor));
        gbc.gridx = 1;
        loginFormPanel.add(passwordField, gbc);

        // Login Button (Centered)
        loginButton = new RoundedButton("Login");
        loginButton.setPreferredSize(new Dimension(100, 40));
        gbc.gridwidth = 2;
        gbc.gridx = 0;
        gbc.gridy = 4;
        gbc.anchor = GridBagConstraints.CENTER; // Center the button
        loginFormPanel.add(loginButton, gbc);

        // Add login form panel to the main panel
        panel.add(loginFormPanel);

        return panel;
    }

    public String getEmail() {
        return emailField.getText();
    }

    public String getPassword() {
        return new String(passwordField.getPassword());
    }

    public String getRole() {
        return role;
    }

    public void addLoginButtonListener(ActionListener listener) {
        loginButton.addActionListener(listener);
    }

    private void navigateBackToSplash() {
        // Close the login screen
        dispose();
        // Reopen the splash screen
        SplashScreenView.reopenSplashScreen();
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            // Instantiate the LoginView with a role (Example: "BRANCHMANAGER")
            new LoginView("BranchManager").setVisible(true);
   });
}
}

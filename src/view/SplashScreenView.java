package view;

import javax.swing.*;
import controller.LoginController;
import dao.LoginDAO;
import java.awt.*;
import utils.RoundedButton;
import java.util.Timer;
import java.util.TimerTask;

public class SplashScreenView {
    static JWindow splash;
    static String selectedRole;

    public static void main(String[] args) {
        // Show the splash screen
        SwingUtilities.invokeLater(() -> showSplashScreen());
    }

    public static void showSplashScreen() {
        splash = new JWindow();
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        splash.setSize(screenSize.width / 2, screenSize.height / 2);
        splash.setLocationRelativeTo(null);

        JPanel content = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g;
                GradientPaint gradient = new GradientPaint(0, 0, new Color(0x00296b), getWidth(), getHeight(), new Color(0x003f88));
                g2d.setPaint(gradient);
                g2d.fillRect(0, 0, getWidth(), getHeight());

                Font font = new Font("Segoe UI", Font.BOLD, 60);
                g2d.setFont(font);
                g2d.setColor(Color.WHITE);
                String text = "Metro Pakistan";

                FontMetrics metrics = g2d.getFontMetrics(font);
                int x = (getWidth() - metrics.stringWidth(text)) / 2;
                int y = (getHeight() + metrics.getHeight()) / 2 - metrics.getDescent();

                g2d.drawString(text, x, y);
            }
        };

        content.setLayout(new BorderLayout());
        content.setBorder(BorderFactory.createEmptyBorder(30, 30, 30, 30));

        JProgressBar progressBar = new JProgressBar();
        progressBar.setIndeterminate(true);
        progressBar.setPreferredSize(new Dimension(400, 10));
        progressBar.setBackground(new Color(240, 240, 240));
        progressBar.setForeground(Color.decode("#ffd500"));
        content.add(progressBar, BorderLayout.SOUTH);

        splash.setContentPane(content);
        splash.setVisible(true);

        // Show the splash screen for 5 seconds before transitioning to role selection
        Timer timer = new Timer();
        timer.schedule(new java.util.TimerTask() {
            @Override
            public void run() {
                fadeOut(splash);
            }
        }, 5000);
    }

    public static void fadeOut(JWindow window) {
        Timer fadeOutTimer = new Timer();
        fadeOutTimer.scheduleAtFixedRate(new java.util.TimerTask() {
            private int opacity = 100;

            @Override
            public void run() {
                if (opacity > 0) {
                    final float currentOpacity = opacity / 100f;
                    SwingUtilities.invokeLater(() -> window.setOpacity(currentOpacity));
                    opacity--;
                } else {
                    window.setVisible(false);
                    fadeOutTimer.cancel();
                    SwingUtilities.invokeLater(SplashScreenView::launchRoleSelectionScreen);
                }
            }
        }, 0, 10);
    }

    public static void launchRoleSelectionScreen() {
        JFrame roleFrame = new JFrame("Select Your Role");
        roleFrame.setSize(splash.getWidth(), splash.getHeight());
        roleFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        roleFrame.setLocationRelativeTo(null);

        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(4, 1, 10, 10));
        panel.setBackground(new Color(245, 245, 245));

        RoundedButton superAdminButton = new RoundedButton("Super Admin");
        superAdminButton.addActionListener(e -> {
            selectedRole = "SuperAdmin";
            SwingUtilities.invokeLater(() -> launchLoginScreen(roleFrame));
        });

        RoundedButton branchManagerButton = new RoundedButton("Branch Manager");
        branchManagerButton.addActionListener(e -> {
            selectedRole = "BranchManager";
            SwingUtilities.invokeLater(() -> launchLoginScreen(roleFrame));
        });

        RoundedButton cashierButton = new RoundedButton("Cashier");
        cashierButton.addActionListener(e -> {
            selectedRole = "Cashier";
            SwingUtilities.invokeLater(() -> launchLoginScreen(roleFrame));
        });

        RoundedButton deoButton = new RoundedButton("Data Entry Operator");
        deoButton.addActionListener(e -> {
            selectedRole = "DataEntryOperator";
            SwingUtilities.invokeLater(() -> launchLoginScreen(roleFrame));
        });

        panel.add(superAdminButton);
        panel.add(branchManagerButton);
        panel.add(cashierButton);
        panel.add(deoButton);

        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        roleFrame.add(panel);
        roleFrame.setVisible(true);
    }

    public static void launchLoginScreen(JFrame roleFrame) {
        roleFrame.dispose(); // Close the role selection frame
        LoginView loginView = new LoginView(selectedRole);
        LoginDAO loginDAO = new LoginDAO();
        new LoginController(loginView, loginDAO);
        loginView.setVisible(true);
    }

    // Method to reopen splash screen
    public static void reopenSplashScreen() {
        SwingUtilities.invokeLater(SplashScreenView::showSplashScreen);
}
}

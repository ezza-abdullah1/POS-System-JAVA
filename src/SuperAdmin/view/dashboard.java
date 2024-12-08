package SuperAdmin.view;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.border.EmptyBorder;
import java.awt.geom.RoundRectangle2D;
import utils.RoundedButton;

public class dashboard {

    private JFrame frame;
    private JPanel contentPanel;
    private Color primaryColor = new Color(0, 86, 179);
    private Color hoverColor = new Color(0, 106, 199);
    private Color backgroundColor = new Color(245, 245, 245);

    public dashboard() {
        initializeFrame();
        createContentPanel();
        frame.setVisible(true);
    }

    private void initializeFrame() {
        frame = new JFrame("Super Admin Dashboard");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(1200, 800);
        frame.setLocationRelativeTo(null);
        frame.setContentPane(createBackgroundPanel());
    }

    private JPanel createBackgroundPanel() {
        return new JPanel() {
            private Image backgroundImage = new ImageIcon("src\\imgs\\new.jpg").getImage();

            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);

                // Draw background image with reduced transparency
                if (backgroundImage != null) {
                    Graphics2D g2d = (Graphics2D) g.create();
                    g2d.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
                    g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.5f)); // Reduced transparency
                    g2d.drawImage(backgroundImage, 0, 0, getWidth(), getHeight(), this);
                    g2d.dispose();
                }

                // Create semi-transparent overlay
                Graphics2D g2d = (Graphics2D) g.create();
                g2d.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);

                // White semi-transparent overlay
                Color overlayColor = new Color(255, 255, 255, 110);
                g2d.setColor(overlayColor);
                g2d.fillRect(0, 0, getWidth(), getHeight());
                g2d.dispose();
            }
        };
    }

    private void createContentPanel() {
        contentPanel = new JPanel(new BorderLayout());
        contentPanel.setOpaque(false);
        contentPanel.setBorder(new EmptyBorder(20, 20, 20, 20));

        // Create header panel
        JPanel headerPanel = createHeaderPanel();
        contentPanel.add(headerPanel, BorderLayout.NORTH);

        // Create main panel with buttons
        JPanel mainPanel = createMainPanel();
        contentPanel.add(mainPanel, BorderLayout.CENTER);

        frame.add(contentPanel);
    }

    private JPanel createHeaderPanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setOpaque(false);
        panel.setBorder(new EmptyBorder(30, 0, 30, 0));

        JLabel headerLabel = new JLabel("Super Admin Dashboard");
        headerLabel.setFont(new Font("Segoe UI", Font.BOLD, 32));
        headerLabel.setForeground(primaryColor);
        headerLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        panel.add(headerLabel);
        return panel;
    }

    private JPanel createMainPanel() {
        JPanel panel = new JPanel(new GridBagLayout()); // Changed from FlowLayout to GridBagLayout
        GridBagConstraints gbc = new GridBagConstraints();
        panel.setOpaque(false);

        // Create action buttons with hover effects
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.insets = new Insets(10, 10, 10, 10);
        panel.add(createActionCard(
                "Manage Branch",
                "Manage your branches effectively",
                e -> new branchView() // Implement functionality
        ), gbc);

        gbc.gridx = 1;
        gbc.gridy = 0;
        panel.add(createActionCard(
                "Manage Branch Manager",
                "Manage branch manager accounts",
                e -> new BranchManagerView() // Implement functionality
        ), gbc);

        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.gridwidth = 2; // Spanning both columns
        panel.add(createActionCard(
                "View Reports",
                "View detailed system reports",
                e -> new reportsView() // Implement functionality
        ), gbc);

        return panel;
    }

    private JPanel createActionCard(String title, String description, ActionListener action) {
        JPanel card = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2d = (Graphics2D) g;
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                // Draw rounded rectangle background
                g2d.setColor(Color.WHITE);
                RoundRectangle2D rect = new RoundRectangle2D.Float(0, 0, getWidth(), getHeight(), 20, 20);
                g2d.fill(rect);

                // Draw subtle shadow
                g2d.setColor(new Color(0, 0, 0, 20));
                g2d.setStroke(new BasicStroke(1));
                g2d.draw(rect);
            }
        };

        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
        card.setPreferredSize(new Dimension(300, 200));
        card.setBorder(new EmptyBorder(20, 20, 20, 20));
        card.setOpaque(false);

        JLabel iconLabel = new JLabel("●");
        iconLabel.setFont(new Font("Segoe UI", Font.PLAIN, 48));
        iconLabel.setForeground(primaryColor);
        iconLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        card.add(iconLabel);

        card.add(Box.createVerticalStrut(15));

        JLabel titleLabel = new JLabel(title);
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 18));
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        card.add(titleLabel);

        card.add(Box.createVerticalStrut(10));

        JLabel descLabel = new JLabel("<html><center> " + description + "</center></html>");
        descLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        descLabel.setForeground(Color.GRAY);
        descLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        card.add(descLabel);

        card.add(Box.createVerticalStrut(15));

        JButton actionButton = createStyledButton("Open");
        actionButton.addActionListener(action);
        actionButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        card.add(actionButton);

        card.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                card.setCursor(new Cursor(Cursor.HAND_CURSOR));
                card.repaint();
            }

            @Override
            public void mouseExited(MouseEvent e) {
                card.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
                card.repaint();
            }
        });

        return card;
    }

    private JButton createStyledButton(String text) {
        JButton button = new JButton(text) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2d = (Graphics2D) g;
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                if (getModel().isPressed()) {
                    g2d.setColor(hoverColor.darker());
                } else if (getModel().isRollover()) {
                    g2d.setColor(hoverColor);
                } else {
                    g2d.setColor(primaryColor);
                }

                g2d.fill(new RoundRectangle2D.Float(0, 0, getWidth(), getHeight(), 10, 10));

                g2d.setColor(Color.WHITE);
                FontMetrics fm = g2d.getFontMetrics();
                int x = (getWidth() - fm.stringWidth(getText())) / 2;
                int y = ((getHeight() - fm.getHeight()) / 2) + fm.getAscent();
                g2d.drawString(getText(), x, y);
            }
        };

        button.setFont(new Font("Segoe UI", Font.BOLD, 14));
        button.setForeground(Color.WHITE);
        button.setPreferredSize(new Dimension(120, 35));
        button.setMaximumSize(new Dimension(120, 35));
        button.setBorderPainted(false);
        button.setContentAreaFilled(false);
        button.setFocusPainted(false);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));

        return button;
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(dashboard::new);
    }
}

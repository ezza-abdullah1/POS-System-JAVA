package SuperAdmin.view;

import SuperAdmin.controller.BranchController;
import utils.RoundedButton;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.border.AbstractBorder;
import javax.swing.border.EmptyBorder;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class addBranchView extends JDialog {
    private JTextField branchCode;
    private JTextField nameField;
    private JTextField cityField;
    private JTextField addressField;
    private JTextField phoneField;
    private RoundedButton saveButton;
    private branchView parent;

    public addBranchView(branchView parent) {
        super(parent, "Add New Branch", true);
        this.parent = parent;

        initializeUI();
    }

    private void initializeUI() {
        setTitle("Add New Branch");
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setSize(600, 500); // Adjusted size
        setLocationRelativeTo(parent);

        // Set up the layered pane for background image and components
        JLayeredPane layeredPane = new JLayeredPane();
        layeredPane.setPreferredSize(new Dimension(600, 500)); // Match the frame size

        // Add custom background panel
        BackgroundPanel backgroundPanel = new BackgroundPanel();
        backgroundPanel.setBounds(0, 0, 600, 500); // Set the background to cover the entire frame
        layeredPane.add(backgroundPanel, JLayeredPane.DEFAULT_LAYER);

        // Create the content panel
        JPanel contentPanel = new JPanel(new GridBagLayout());
        contentPanel.setOpaque(false); // Make it transparent to show the background
        contentPanel.setBounds(50, 50, 500, 400); // Set position and size for contentPanel
        contentPanel.setBorder(new EmptyBorder(20, 20, 20, 20));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Create and add labels and fields
        Color foregroundColor = new Color(0, 0, 0); // Black text
        JLabel branchCodeLabel = createLabel("Branch Code:", foregroundColor);
        JLabel nameLabel = createLabel("Branch Name:", foregroundColor);
        JLabel cityLabel = createLabel("City:", foregroundColor);
        JLabel addressLabel = createLabel("Address:", foregroundColor);
        JLabel phoneLabel = createLabel("Phone Number:", foregroundColor);

        branchCode = createTextField(foregroundColor, new Color(240, 240, 240));
        nameField = createTextField(foregroundColor, new Color(240, 240, 240));
        cityField = createTextField(foregroundColor, new Color(240, 240, 240));
        addressField = createTextField(foregroundColor, new Color(240, 240, 240));
        phoneField = createTextField(foregroundColor, new Color(240, 240, 240));

        saveButton = new RoundedButton("Save Branch");
        saveButton.setPreferredSize(new Dimension(150, 40));
        saveButton.setBackground(new Color(50, 90, 120));
        saveButton.setForeground(Color.WHITE);
        saveButton.setFont(new Font("Siege UI", Font.BOLD, 18));
        saveButton.addActionListener(new SaveButtonListener());

        // Add components to the content panel
        gbc.gridx = 0;
        gbc.gridy = 0;
        contentPanel.add(branchCodeLabel, gbc);
        gbc.gridx = 1;
        contentPanel.add(branchCode, gbc);

        gbc.gridx = 0;
        gbc.gridy = 1;
        contentPanel.add(nameLabel, gbc);
        gbc.gridx = 1;
        contentPanel.add(nameField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 2;
        contentPanel.add(cityLabel, gbc);
        gbc.gridx = 1;
        contentPanel.add(cityField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 3;
        contentPanel.add(addressLabel, gbc);
        gbc.gridx = 1;
        contentPanel.add(addressField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 4;
        contentPanel.add(phoneLabel, gbc);
        gbc.gridx = 1;
        contentPanel.add(phoneField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 5;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        contentPanel.add(saveButton, gbc);

        layeredPane.add(contentPanel, JLayeredPane.PALETTE_LAYER);

        setContentPane(layeredPane);
        setVisible(true);
    }

    private JLabel createLabel(String text, Color foregroundColor) {
        JLabel label = new JLabel(text);
        label.setForeground(foregroundColor);
        label.setFont(new Font("Siege UI", Font.BOLD, 15));
        label.setOpaque(false);
        label.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        return label;
    }

    private JTextField createTextField(Color foregroundColor, Color backgroundColor) {
        JTextField textField = new JTextField() {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2d = (Graphics2D) g.create();
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2d.setColor(getBackground());
                g2d.fillRoundRect(0, 0, getWidth(), getHeight(), 15, 15);
                g2d.setClip(new java.awt.geom.RoundRectangle2D.Double(0, 0, getWidth(), getHeight(), 15, 15));
                super.paintComponent(g2d);
                g2d.dispose();
            }
        };
        textField.setForeground(foregroundColor);
        textField.setCaretColor(foregroundColor);
        textField.setFont(new Font("Siege UI", Font.PLAIN, 14));
        textField.setPreferredSize(new Dimension(250, 30));
        textField.setBackground(backgroundColor);
        textField.setOpaque(false);
        textField.setBorder(new RoundedBorder(15, new Color(169, 169, 169), 1));
        return textField;
    }

    class RoundedBorder extends AbstractBorder {
        private int radius;
        private Color borderColor;
        private int thickness;

        public RoundedBorder(int radius, Color borderColor, int thickness) {
            this.radius = radius;
            this.borderColor = borderColor;
            this.thickness = thickness;
        }

        @Override
        public void paintBorder(Component c, Graphics g, int x, int y, int width, int height) {
            // Clip the graphics context to create a rounded border area
            Graphics2D g2d = (Graphics2D) g;
            g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

            // Set the border color to gray
            g2d.setColor(borderColor);
            g2d.setStroke(new BasicStroke(thickness)); // Set the border thickness

            // Draw the rounded border
            g2d.drawRoundRect(x, y, width - 1, height - 1, radius, radius);
        }

        @Override
        public Insets getBorderInsets(Component c) {
            // Return the appropriate insets for the border
            return new Insets(thickness, thickness, thickness, thickness);
        }
    }

    public class BackgroundPanel extends JPanel {
        private Image backgroundImage;

        public BackgroundPanel() {
            try {
                // Load image from the classpath (ensure it's in the resources folder)
                backgroundImage = ImageIO.read(getClass().getResource("bg.jpg"));
            } catch (Exception e) {
                e.printStackTrace();
                JOptionPane.showMessageDialog(addBranchView.this,
                        "Error loading background image: " + e.getMessage(),
                        "Image Load Error",
                        JOptionPane.ERROR_MESSAGE);
            }
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            if (backgroundImage != null) {
                Graphics2D g2d = (Graphics2D) g;

                // Draw the background image first
                g2d.drawImage(backgroundImage, 0, 0, getWidth(), getHeight(), this);

                // Create a darker, semi-transparent blue overlay
                Color transparentWhite = new Color(255, 255, 255, 180); // White with 180 opacity (Alpha = 180)

                // Set the color and transparency
                g2d.setColor(transparentWhite);

                // Fill the entire panel with the darker, semi-transparent blue
                g2d.fillRect(0, 0, getWidth(), getHeight());
            }
        }
    }

    private class SaveButtonListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            String code = branchCode.getText();
            String name = nameField.getText();
            String city = cityField.getText();
            String address = addressField.getText();
            String phone = phoneField.getText();

            // Perform validation checks
            if (code.isEmpty()) {
                JOptionPane.showMessageDialog(parent, "Branch Code cannot be empty.", "Error",
                        JOptionPane.ERROR_MESSAGE);
                return;
            }

            if (name.isEmpty()) {
                JOptionPane.showMessageDialog(parent, "Branch Name cannot be empty.", "Error",
                        JOptionPane.ERROR_MESSAGE);
                return;
            }

            if (city.isEmpty()) {
                JOptionPane.showMessageDialog(parent, "City cannot be empty.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            if (address.isEmpty()) {
                JOptionPane.showMessageDialog(parent, "Address cannot be empty.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            if (phone.isEmpty()) {
                JOptionPane.showMessageDialog(parent, "Phone Number cannot be empty.", "Error",
                        JOptionPane.ERROR_MESSAGE);
                return;
            }

            // Validate phone number format
            if (!phone.matches("\\d{10,15}")) {
                JOptionPane.showMessageDialog(parent, "Invalid phone number. It should be between 10 to 15 digits.",
                        "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            try {
                BranchController branchController = new BranchController();

                // Pass the values to the controller and set numEmployees to 0, isActive to true
                String resultMessage = branchController.saveBranch(code, name, city, address, phone, 0, true);

                if (resultMessage.startsWith("Branch saved successfully:")) {
                    parent.loadBranchData(); // Refresh data
                    dispose();
                    JOptionPane.showMessageDialog(parent, resultMessage);
                } else {
                    // Show the error message if it's not a success
                    JOptionPane.showMessageDialog(parent, resultMessage, "Error", JOptionPane.ERROR_MESSAGE);
                }

            } catch (Exception ex) {
                // Show error dialog with exception message
                JOptionPane.showMessageDialog(parent, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
}

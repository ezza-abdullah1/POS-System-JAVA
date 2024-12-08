package view;

import controller.UserController;
import model.UserModel;
import utils.RoundedButton;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.border.AbstractBorder;
import javax.swing.border.EmptyBorder;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class UpdateUserView extends JFrame {
    private JTextField nameField;
    private JTextField emailField;
    private JTextField branchCodeField;
    private JTextField passwordField;
    private JTextField salaryField;
    private RoundedButton updateButton;
    private String email;
    private String role;
    private UserController userController;
    private Runnable refreshTable;
    private UserTableView parent;

    public UpdateUserView(UserTableView parent, UserController controller, String email, String role,
            Runnable refreshTable) {
        this.parent = parent;
        this.role = role;
        this.email = email;
        this.userController = controller;
        this.refreshTable = refreshTable;
        initializeUI();
        populateFields();
    }

    private void initializeUI() {
        setTitle("Update User");
        setSize(600, 500);
        setLocationRelativeTo(parent);

        // Layered pane for background and components
        JLayeredPane layeredPane = new JLayeredPane();
        layeredPane.setPreferredSize(new Dimension(600, 500));

        // Add background panel
        BackgroundPanel backgroundPanel = new BackgroundPanel();
        backgroundPanel.setBounds(0, 0, 600, 500);
        layeredPane.add(backgroundPanel, JLayeredPane.DEFAULT_LAYER);

        // Create the content panel with GridBagLayout
        JPanel contentPanel = new JPanel(new GridBagLayout());
        contentPanel.setOpaque(false); // Transparent background
        contentPanel.setBounds(50, 50, 500, 400); // Adjust positioning and size

        contentPanel.setBorder(new EmptyBorder(20, 20, 20, 20));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        Color foregroundColor = new Color(0, 0, 0); // Light gray text

        // Labels for the fields
        JLabel nameLabel = createLabel("Name:", foregroundColor);
        JLabel emailLabel = createLabel("Email:", foregroundColor);
        JLabel passwordLabel = createLabel("Password:", foregroundColor);
        JLabel branchCodeLabel = createLabel("Branch Code:", foregroundColor);
        JLabel salaryLabel = createLabel("Salary:", foregroundColor);

        // Text fields
        nameField = createTextField(foregroundColor, new Color(240, 240, 240));
        emailField = createTextField(foregroundColor, new Color(240, 240, 240));
        passwordField = createTextField(foregroundColor, new Color(240, 240, 240));
        branchCodeField = createTextField(foregroundColor, new Color(240, 240, 240));
        branchCodeField.setEditable(false);
        salaryField = createTextField(foregroundColor, new Color(240, 240, 240));

        // Update button
        updateButton = new RoundedButton("Update");
        updateButton.setPreferredSize(new Dimension(150, 40));
        updateButton.setForeground(Color.WHITE);
        updateButton.setFont(new Font("Siege UI", Font.BOLD, 18));
        updateButton.addActionListener(e -> updateUser());

        // Add components to the content panel
        gbc.gridx = 0;
        gbc.gridy = 0;
        contentPanel.add(nameLabel, gbc);
        gbc.gridx = 1;
        contentPanel.add(nameField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 1;
        contentPanel.add(emailLabel, gbc);
        gbc.gridx = 1;
        contentPanel.add(emailField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 2;
        contentPanel.add(passwordLabel, gbc);
        gbc.gridx = 1;
        contentPanel.add(passwordField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 3;
        contentPanel.add(branchCodeLabel, gbc);
        gbc.gridx = 1;
        contentPanel.add(branchCodeField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 4;
        contentPanel.add(salaryLabel, gbc);
        gbc.gridx = 1;
        contentPanel.add(salaryField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 5;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        contentPanel.add(updateButton, gbc);

        // Add content panel to layered pane
        layeredPane.add(contentPanel, JLayeredPane.PALETTE_LAYER);

        // Set content pane and show window
        setContentPane(layeredPane);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setVisible(true);
    }

    // Helper methods for creating labels, text fields, and background panel
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

    private class BackgroundPanel extends JPanel {
        private Image backgroundImage;

        public BackgroundPanel() {
            try {
                // Load background image
                backgroundImage = ImageIO.read(getClass().getResource("/imgs/bg.jpg"));
            } catch (Exception e) {
                e.printStackTrace();
                JOptionPane.showMessageDialog(UpdateUserView.this,
                        "Error loading background image: " + e.getMessage(),
                        "Image Load Error", JOptionPane.ERROR_MESSAGE);
            }
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            if (backgroundImage != null) {
                Graphics2D g2d = (Graphics2D) g;
                g2d.drawImage(backgroundImage, 0, 0, getWidth(), getHeight(), this);
                Color transparentWhite = new Color(255, 255, 255, 180);
                g2d.setColor(transparentWhite);
                g2d.fillRect(0, 0, getWidth(), getHeight());
            }
        }
    }

    // Rounded Border class for styling text fields
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
            Graphics2D g2d = (Graphics2D) g;
            g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2d.setColor(borderColor);
            g2d.setStroke(new BasicStroke(thickness));
            g2d.drawRoundRect(x, y, width - 1, height - 1, radius, radius);
        }

        @Override
        public Insets getBorderInsets(Component c) {
            return new Insets(thickness, thickness, thickness, thickness);
        }
    }

    private void populateFields() {
        String[] userData = userController.getUserByEmailAndRole(email, role);

        nameField.setText(userData[1]);
        emailField.setText(userData[2]);
        passwordField.setText(userData[3]);
        branchCodeField.setText(userData[5]);
        salaryField.setText(userData[6]);
    }

    private void updateUser() {
        String name = nameField.getText();
        String email = emailField.getText();
        String empNumberStr = passwordField.getText();
        String salaryStr = salaryField.getText();

        try {
            if (name.isEmpty() || email.isEmpty() || empNumberStr.isEmpty() || salaryStr.isEmpty()) {
                JOptionPane.showMessageDialog(this, "All fields must be filled out.", "Validation Error",
                        JOptionPane.ERROR_MESSAGE);
                return;
            }

            int empNumber = Integer.parseInt(empNumberStr);
            double salary = Double.parseDouble(salaryStr);

            UserModel updatedUser = new UserModel();
            updatedUser.setName(name);
            updatedUser.setEmail(email);
            updatedUser.setBranchCode(Integer.parseInt(branchCodeField.getText()));
            updatedUser.setEmpNumber(empNumber);
            updatedUser.setSalary(salary);

            userController.updateUser(updatedUser);
            refreshTable.run();
            dispose();
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Invalid number format for Employee Number or Salary.",
                    "Validation Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}

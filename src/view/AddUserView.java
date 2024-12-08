package view;

import SuperAdmin.controller.BranchController;
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
import java.util.ArrayList;
import java.util.List;

public class AddUserView extends JFrame {
    private JComboBox<String> branchComboBox;
    private JTextField nameField;
    private JTextField emailField;
    private JTextField salaryField;
    private JTextField empNumberField;
    private JButton addButton;
    private List<Integer> activeBranchCodes;
    private UserTableView parent;
    private String role;

    public AddUserView(UserTableView parent) {
        this.parent = parent;
        this.role = parent.getRole();
        setTitle("Add " + role);
        setSize(600, 500);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

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
        JLabel branchLabel = createLabel("Select Branch:", foregroundColor);
        JLabel nameLabel = createLabel("Name:", foregroundColor);
        JLabel emailLabel = createLabel("Email:", foregroundColor);
        JLabel salaryLabel = createLabel("Salary:", foregroundColor);
        JLabel empNumberLabel = createLabel("Employee Number:", foregroundColor);
        activeBranchCodes = new ArrayList<>();

        branchComboBox = new JComboBox<>();
        branchComboBox.setFont(new Font("Siege UI", Font.PLAIN, 14));
        branchComboBox.setBackground(Color.WHITE);

        nameField = createTextField(foregroundColor, new Color(240, 240, 240));
        emailField = createTextField(foregroundColor, new Color(240, 240, 240));
        salaryField = createTextField(foregroundColor, new Color(240, 240, 240));
        empNumberField = createTextField(foregroundColor, new Color(240, 240, 240));

        addButton = new RoundedButton("Add " + role);
        addButton.setPreferredSize(new Dimension(150, 40));
        addButton.setForeground(Color.WHITE);
        addButton.setFont(new Font("Siege UI", Font.BOLD, 18));
        addButton.addActionListener(e -> addUser());

        // Add components to the content panel
        gbc.gridx = 0;
        gbc.gridy = 0;
        contentPanel.add(branchLabel, gbc);
        gbc.gridx = 1;
        contentPanel.add(branchComboBox, gbc);

        gbc.gridx = 0;
        gbc.gridy = 1;
        contentPanel.add(nameLabel, gbc);
        gbc.gridx = 1;
        contentPanel.add(nameField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 2;
        contentPanel.add(emailLabel, gbc);
        gbc.gridx = 1;
        contentPanel.add(emailField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 3;
        contentPanel.add(empNumberLabel, gbc);
        gbc.gridx = 1;
        contentPanel.add(empNumberField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 4;
        contentPanel.add(salaryLabel, gbc);
        gbc.gridx = 1;
        contentPanel.add(salaryField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 5;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        contentPanel.add(addButton, gbc);

        layeredPane.add(contentPanel, JLayeredPane.PALETTE_LAYER);

        setContentPane(layeredPane);
        setVisible(true);

        // Load active branches into the JComboBox
        BranchController userController = new BranchController();
        userController.loadActiveBranchesToView(this);
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

    // Custom Rounded Border Class with thickness and color
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
                backgroundImage = ImageIO.read(getClass().getResource("/imgs/bg.jpg"));
            } catch (Exception e) {
                e.printStackTrace();
                JOptionPane.showMessageDialog(AddUserView.this,
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

    public void addBranchToComboBox(String branch, int branchCode) {
        branchComboBox.addItem(branch);
        activeBranchCodes.add(branchCode);
    }

    private void addUser() {
        String name = nameField.getText().trim();
        String email = emailField.getText().trim();
        String salaryText = salaryField.getText().trim();
        String empNumberText = empNumberField.getText().trim();

        if (name.isEmpty() || email.isEmpty() || salaryText.isEmpty() || empNumberText.isEmpty()) {
            JOptionPane.showMessageDialog(this, "All fields must be filled out.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        if (!email.matches("^[A-Za-z0-9+_.-]+@(.+)$")) {
            JOptionPane.showMessageDialog(this, "Invalid email format.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        try {
            double salary = Double.parseDouble(salaryText);
            if (salary <= 0) {
                JOptionPane.showMessageDialog(this, "Salary must be a positive number.", "Error",
                        JOptionPane.ERROR_MESSAGE);
                return;
            }

            int empNumber = Integer.parseInt(empNumberText);
            if (empNumber <= 0) {
                JOptionPane.showMessageDialog(this, "Employee number must be a positive integer.", "Error",
                        JOptionPane.ERROR_MESSAGE);
                return;
            }

            int selectedBranchCode = activeBranchCodes.get(branchComboBox.getSelectedIndex());

            int confirm = JOptionPane.showConfirmDialog(this,
                    "Are you sure you want to add this " + role + "?\n\n"
                            + "Name: " + name + "\n"
                            + "Email: " + email + "\n"
                            + "Salary: " + salary + "\n"
                            + "Employee Number: " + empNumber + "\n"
                            + "Branch: " + branchComboBox.getSelectedItem(),
                    "Confirm Addition", JOptionPane.YES_NO_OPTION);

            if (confirm == JOptionPane.YES_OPTION) {
                UserModel newUser = new UserModel();
                newUser.setName(name);
                newUser.setEmail(email);
                newUser.setPassword("123");
                newUser.setRole(role);
                newUser.setBranchCode(selectedBranchCode);
                newUser.setSalary(salary);
                newUser.setEmpNumber(empNumber);

                UserController userController = new UserController();
                userController.addUser(newUser);
                parent.loadUserData(role);
                // dispose();
            }
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Invalid salary or employee number.", "Error",
                    JOptionPane.ERROR_MESSAGE);
        } catch (IndexOutOfBoundsException ex) {
            JOptionPane.showMessageDialog(this, "Please select a valid branch.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}

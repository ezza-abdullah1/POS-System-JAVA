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

public class updateBranchView extends JFrame {
    private RoundedButton updateButton;
    private JTextField nameField;
    private JTextField addressField;
    private JTextField cityField;
    private JTextField phoneField;
    private JComboBox<String> activeField;
    private JSpinner numEmpSpinner;
    private String branchcode;
    private BranchController branchController;
    private branchView parent;
    private Runnable refreshTable;

    public updateBranchView(branchView parent, BranchController controller, String branchcode,
            Runnable refreshTable) {
        this.parent = parent;
        this.branchcode = branchcode;
        this.branchController = controller;
        this.refreshTable = refreshTable;
        initializeUI();
        populateFields();
    }

    private void initializeUI() {
        setTitle("Update Branch");
        setSize(600, 500); // Adjusted size
        setLocationRelativeTo(parent);

        // Set up the layered pane for background image and components
        JLayeredPane layeredPane = new JLayeredPane();
        layeredPane.setPreferredSize(new Dimension(600, 500));

        // Add custom background panel
        BackgroundPanel backgroundPanel = new BackgroundPanel();
        backgroundPanel.setBounds(0, 0, 600, 500);
        layeredPane.add(backgroundPanel, JLayeredPane.DEFAULT_LAYER);

        // Create the content panel with GridBagLayout
        JPanel contentPanel = new JPanel(new GridBagLayout());
        contentPanel.setOpaque(false); // Transparent to show background
        contentPanel.setBounds(50, 50, 500, 400); // Adjust positioning and size

        contentPanel.setBorder(new EmptyBorder(20, 20, 20, 20));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        Color foregroundColor = new Color(0, 0, 0); // Light gray text

        // Labels for the fields
        JLabel nameLabel = createLabel("Name:", foregroundColor);
        JLabel cityLabel = createLabel("City:", foregroundColor);
        JLabel addressLabel = createLabel("Address:", foregroundColor);
        JLabel phoneLabel = createLabel("Phone:", foregroundColor);
        JLabel activeLabel = createLabel("Is Active:", foregroundColor);
        JLabel numEmpLabel = createLabel("Number of Employees:", foregroundColor);

        // Text fields and spinner
        nameField = createTextField(foregroundColor, new Color(240, 240, 240));
        cityField = createTextField(foregroundColor, new Color(240, 240, 240));
        addressField = createTextField(foregroundColor, new Color(240, 240, 240));
        phoneField = createTextField(foregroundColor, new Color(240, 240, 240));
        activeField = new JComboBox<>(new String[] { "", "true", "false" });
        numEmpSpinner = new JSpinner(new SpinnerNumberModel(0, 0, Integer.MAX_VALUE, 1));

        // Update button
        updateButton = new RoundedButton("Update Branch");
        updateButton.setPreferredSize(new Dimension(150, 40));
        updateButton.setForeground(Color.WHITE);
        updateButton.setFont(new Font("Siege UI", Font.BOLD, 18));
        updateButton.addActionListener(e -> updateBranch());

        // Add components to the content panel
        gbc.gridx = 0;
        gbc.gridy = 0;
        contentPanel.add(nameLabel, gbc);
        gbc.gridx = 1;
        contentPanel.add(nameField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 1;
        contentPanel.add(cityLabel, gbc);
        gbc.gridx = 1;
        contentPanel.add(cityField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 2;
        contentPanel.add(addressLabel, gbc);
        gbc.gridx = 1;
        contentPanel.add(addressField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 3;
        contentPanel.add(phoneLabel, gbc);
        gbc.gridx = 1;
        contentPanel.add(phoneField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 4;
        contentPanel.add(activeLabel, gbc);
        gbc.gridx = 1;
        contentPanel.add(activeField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 5;
        contentPanel.add(numEmpLabel, gbc);
        gbc.gridx = 1;
        contentPanel.add(numEmpSpinner, gbc);

        gbc.gridx = 0;
        gbc.gridy = 6;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        contentPanel.add(updateButton, gbc);

        layeredPane.add(contentPanel, JLayeredPane.PALETTE_LAYER);

        setContentPane(layeredPane);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
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
        textField.setFont(new Font("Siege UI", Font.PLAIN, 13));
        textField.setPreferredSize(new Dimension(350, 35));
        textField.setBackground(backgroundColor);
        textField.setOpaque(false);
        textField.setBorder(new RoundedBorder(15, new Color(169, 169, 169), 1));

        return textField;
    }

    public class BackgroundPanel extends JPanel {
        private Image backgroundImage;

        public BackgroundPanel() {
            try {
                // Load background image
                backgroundImage = ImageIO.read(getClass().getResource("/imgs/bg.jpg"));
            } catch (Exception e) {
                e.printStackTrace();
                JOptionPane.showMessageDialog(updateBranchView.this,
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
        String[] branchData = branchController.getBranchByCode(branchcode);

        nameField.setText(branchData[1]);
        cityField.setText(branchData[2]);
        addressField.setText(branchData[3]);
        phoneField.setText(branchData[4]);
        numEmpSpinner.setValue(Integer.parseInt(branchData[5])); // Set spinner value
        activeField.setSelectedItem(branchData[6]);
    }

    private void updateBranch() {
        String name = nameField.getText();
        String city = cityField.getText();
        String address = addressField.getText();
        String phone = phoneField.getText();
        int numEmp = (int) numEmpSpinner.getValue(); // Retrieve value from spinner
        String item = (String) activeField.getSelectedItem();
        Boolean isactive = Boolean.parseBoolean(item);

        // Validation
        if (name.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Branch Name cannot be empty.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        if (city.isEmpty()) {
            JOptionPane.showMessageDialog(this, "City cannot be empty.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        if (address.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Address cannot be empty.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        if (phone.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Phone Number cannot be empty.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        if (!phone.matches("\\d{10,15}")) {
            JOptionPane.showMessageDialog(this, "Phone number must be between 10 to 15 digits.", "Error",
                    JOptionPane.ERROR_MESSAGE);
            return;
        }

        if (item.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please select the status (Active/Inactive).", "Error",
                    JOptionPane.ERROR_MESSAGE);
            return;
        }

        // Confirmation before updating
        int confirmation = JOptionPane.showConfirmDialog(this,
                "Are you sure you want to update this branch?", "Confirm Update", JOptionPane.YES_NO_OPTION);

        if (confirmation == JOptionPane.YES_OPTION) {
            try {
                branchController.updateBranch(branchcode, name, city, address, phone, numEmp, isactive);

                JOptionPane.showMessageDialog(this, "Branch updated successfully.");
                refreshTable.run(); // Refresh table data
                dispose();
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
}

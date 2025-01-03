package DataEntryOperator.view;

import java.awt.*;
import java.awt.event.ActionListener;
import javax.swing.*;

public class ProductPanel extends JDialog {
    private JTextField productIdField, nameField, categoryField,
            originalPriceField, salePriceField, unitPriceField,
            cartonPriceField, taxField, weightField, quantityField;
    private JButton saveButton;

    public ProductPanel(int vendorId) {
        super((Frame) null, "Add Product for Vendor ID: " + vendorId, true);
        setSize(1000, 800);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout());

        // Create a panel with semi-transparent background
        JPanel backgroundPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                // Load the background image
                ImageIcon bgImage = new ImageIcon("src\\imgs\\Screenshot 2024-12-09 032241.jpg");
                Image image = bgImage.getImage();
                g.drawImage(image, 0, 0, getWidth(), getHeight(), this);

                // Add semi-transparent overlay
                Graphics2D g2d = (Graphics2D) g.create();
                g2d.setColor(new Color(255, 255, 255, 170)); // Semi-transparent white
                g2d.fillRect(0, 0, getWidth(), getHeight());
                g2d.dispose();
            }
        };
        backgroundPanel.setLayout(new GridBagLayout());
        add(backgroundPanel);

        // Form panel
        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setOpaque(false); // Transparent background
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(15, 15, 15, 15); // Spacing
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Add fields to form panel
        addField(formPanel, gbc, 0, "Product ID:", productIdField = createTextField());
        addField(formPanel, gbc, 1, "Product Name:", nameField = createTextField());
        addField(formPanel, gbc, 2, "Category:", categoryField = createTextField());
        addField(formPanel, gbc, 3, "Original Price:", originalPriceField = createTextField());
        addField(formPanel, gbc, 4, "Sale Price:", salePriceField = createTextField());
        addField(formPanel, gbc, 5, "Price by Unit:", unitPriceField = createTextField());
        addField(formPanel, gbc, 6, "Price by Carton:", cartonPriceField = createTextField());
        addField(formPanel, gbc, 7, "Tax (%):", taxField = createTextField());
        addField(formPanel, gbc, 8, "Weight:", weightField = createTextField());
        addField(formPanel, gbc, 9, "Quantity:", quantityField = createTextField());

        // Save button
        gbc.gridx = 1;
        gbc.gridy = 10;
        saveButton = createButton("Save Product");
        formPanel.add(saveButton, gbc);

        backgroundPanel.add(formPanel);
    }

    private void addField(JPanel panel, GridBagConstraints gbc, int y, String labelText, JTextField field) {
        gbc.gridx = 0;
        gbc.gridy = y;
        panel.add(createLabel(labelText), gbc);

        gbc.gridx = 1;
        panel.add(field, gbc);
    }

    private JLabel createLabel(String text) {
        JLabel label = new JLabel(text);
        label.setFont(new Font("Segoe UI", Font.BOLD, 26)); // Larger font size
        return label;
    }

    private JTextField createTextField() {
        JTextField textField = new JTextField(25); // Larger text field
        textField.setFont(new Font("Segoe UI", Font.PLAIN, 24));
        return textField;
    }

    private JButton createButton(String text) {
        JButton button = new JButton(text);
        button.setFocusPainted(false);
        button.setFont(new Font("Segoe UI", Font.BOLD, 23));
        button.setBackground(new Color(0, 102, 204));
        button.setForeground(Color.WHITE);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        return button;
    }

    // Getters for all fields
    public String getProductId() {
        return productIdField.getText().trim();
    }

    public String getProductName() {
        return nameField.getText().trim();
    }

    public String getCategory() {
        return categoryField.getText().trim();
    }

    public double getOriginalPrice() {
        return Double.parseDouble(originalPriceField.getText().trim());
    }

    public double getSalePrice() {
        return Double.parseDouble(salePriceField.getText().trim());
    }

    public double getPriceByUnit() {
        return Double.parseDouble(unitPriceField.getText().trim());
    }

    public double getPriceByCarton() {
        return Double.parseDouble(cartonPriceField.getText().trim());
    }

    public double getTax() {
        return Double.parseDouble(taxField.getText().trim());
    }

    public double getWeight() {
        return Double.parseDouble(weightField.getText().trim());
    }

    public int getQuantity() {
        return Integer.parseInt(quantityField.getText().trim());
    }

    // Validation method
    public boolean validateInputs() {
        try {
            if (getProductId().isEmpty() || getProductName().isEmpty() || getCategory().isEmpty()) {
                JOptionPane.showMessageDialog(this, "Please fill in all mandatory fields.", "Validation Error",
                        JOptionPane.ERROR_MESSAGE);
                return false;
            }
            if (getOriginalPrice() < 0 || getSalePrice() < 0 || getPriceByUnit() < 0 || getPriceByCarton() < 0
                    || getTax() < 0 || getWeight() < 0 || getQuantity() < 0) {
                JOptionPane.showMessageDialog(this, "Numeric fields cannot have negative values.", "Validation Error",
                        JOptionPane.ERROR_MESSAGE);
                return false;
            }
            if (!getProductId().matches("^[A-Za-z0-9_-]+$")) {
                JOptionPane.showMessageDialog(this,
                        "Product ID can only contain alphanumeric characters, underscores, or hyphens.",
                        "Validation Error", JOptionPane.ERROR_MESSAGE);
                return false;
            }
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Please ensure all numeric fields contain valid numbers.",
                    "Validation Error", JOptionPane.ERROR_MESSAGE);
            return false;
        }
        return true;
    }

    // Add listener for the Save button
    public void addSaveListener(ActionListener listener) {
        saveButton.addActionListener(e -> {
            if (validateInputs()) {
                listener.actionPerformed(e);
            }
        });
    }
}
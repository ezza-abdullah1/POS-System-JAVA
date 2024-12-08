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
        setSize(500, 500);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new GridLayout(12, 2)); // Adjusted for all fields

        add(new JLabel("Product ID:"));
        productIdField = new JTextField();
        add(productIdField);

        add(new JLabel("Product Name:"));
        nameField = new JTextField();
        add(nameField);

        add(new JLabel("Category:"));
        categoryField = new JTextField();
        add(categoryField);

        add(new JLabel("Original Price:"));
        originalPriceField = new JTextField();
        add(originalPriceField);

        add(new JLabel("Sale Price:"));
        salePriceField = new JTextField();
        add(salePriceField);

        add(new JLabel("Price by Unit:"));
        unitPriceField = new JTextField();
        add(unitPriceField);

        add(new JLabel("Price by Carton:"));
        cartonPriceField = new JTextField();
        add(cartonPriceField);

        add(new JLabel("Tax (%):"));
        taxField = new JTextField();
        add(taxField);

        add(new JLabel("Weight:"));
        weightField = new JTextField();
        add(weightField);

        add(new JLabel("Quantity:"));
        quantityField = new JTextField();
        add(quantityField);

        saveButton = new JButton("Save Product");
        add(saveButton);
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

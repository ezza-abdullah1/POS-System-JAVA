package DataEntryOperator.view;

import java.awt.*;
import java.awt.event.ActionListener;

import javax.swing.*;

public class ProductPanel extends JDialog {
    private JTextField nameField, categoryField, originalPriceField, salePriceField, unitPriceField, cartonPriceField;
    private JButton saveButton;

    public ProductPanel(int vendorId) {
        super((Frame) null, "Add Product for Vendor ID: " + vendorId, true);
        setSize(400, 400);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new GridLayout(7, 2));

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

        saveButton = new JButton("Save Product");
        add(saveButton);
    }

    public String getProductName() {
        return nameField.getText();
    }

    public String getCategory() {
        return categoryField.getText();
    }

    public double getOriginalPrice() {
        return Double.parseDouble(originalPriceField.getText());
    }

    public double getSalePrice() {
        return Double.parseDouble(salePriceField.getText());
    }

    public double getPriceByUnit() {
        return Double.parseDouble(unitPriceField.getText());
    }

    public double getPriceByCarton() {
        return Double.parseDouble(cartonPriceField.getText());
    }

    public void addSaveListener(ActionListener listener) {
        saveButton.addActionListener(listener);
    }
}
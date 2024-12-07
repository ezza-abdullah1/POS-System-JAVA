package DataEntryOperator.view;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;

import DataEntryOperator.controller.ProductController;
import DataEntryOperator.model.Vendor;
import db.DatabaseConnection;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.util.List;

public class VendorTablePanel extends JPanel {
    JTable vendorTable;
    private DefaultTableModel tableModel;
    private JButton addVendorButton;

    public VendorTablePanel() {
        setLayout(new BorderLayout());

        // Panel for heading and button
        JPanel topPanel = new JPanel(new BorderLayout());

        // Heading label
        JLabel headingLabel = new JLabel("Vendor and Product Management", JLabel.CENTER);
        headingLabel.setFont(new Font("Arial", Font.BOLD, 16));
        headingLabel.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0)); // Padding for better appearance
        topPanel.add(headingLabel, BorderLayout.NORTH);

        // Add Vendor button
        addVendorButton = new JButton("Add New Vendor");
        topPanel.add(addVendorButton, BorderLayout.SOUTH);

        // Add topPanel to the main panel
        add(topPanel, BorderLayout.NORTH);

        // Table model
        tableModel = new DefaultTableModel(
                new Object[] { "Vendor ID", "Vendor Name", "Vendor Address", "Contact No", "Add Product" }, 0);
        vendorTable = new JTable(tableModel) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return column == 4; // Make only the "Add Product" column editable
            }
        };

        // Adding a button renderer for "Add Product" column
        vendorTable.getColumn("Add Product").setCellRenderer(new ButtonRenderer());
        vendorTable.getColumn("Add Product").setCellEditor(new ButtonEditor(new JCheckBox(), this));

        JScrollPane scrollPane = new JScrollPane(vendorTable);
        add(scrollPane, BorderLayout.CENTER);
    }

    public void addVendorActionListener(ActionListener listener) {
        addVendorButton.addActionListener(listener);
    }

    public void setVendors(List<Vendor> vendors) {
        tableModel.setRowCount(0); // Clear existing rows
        for (Vendor vendor : vendors) {
            tableModel.addRow(new Object[] {
                    vendor.getVendorId(),
                    vendor.getVendorName(),
                    vendor.getVendorAddress(),
                    vendor.getContactNo(),
                    "+"
            });
        }
    }

    public int getSelectedVendorId(int row) {
        return (int) tableModel.getValueAt(row, 0);
    }
}

class ButtonRenderer extends JButton implements TableCellRenderer {
    public ButtonRenderer() {
        setText("+");
    }

    @Override
    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus,
            int row, int column) {
        return this;
    }
}

class ButtonEditor extends DefaultCellEditor {
    private JButton button;
    private VendorTablePanel vendorPanel;

    public ButtonEditor(JCheckBox checkBox, VendorTablePanel vendorPanel) {
        super(checkBox);
        this.vendorPanel = vendorPanel;
        button = new JButton("+");
        button.addActionListener(this::handleClick);
    }

    private void handleClick(ActionEvent event) {
        int row = vendorPanel.vendorTable.getSelectedRow();
        int vendorId = vendorPanel.getSelectedVendorId(row);

        ProductPanel productPanel = new ProductPanel(vendorId);
        productPanel.addSaveListener(e -> {
            ProductController productController = new ProductController();

            String name = productPanel.getProductName();
            String category = productPanel.getCategory();
            double originalPrice = productPanel.getOriginalPrice();
            double salePrice = productPanel.getSalePrice();
            double priceByUnit = productPanel.getPriceByUnit();
            double priceByCarton = productPanel.getPriceByCarton();

            productController.addProduct(vendorId, name, category, originalPrice, salePrice, priceByUnit,
                    priceByCarton);

            JOptionPane.showMessageDialog(productPanel, "Product added successfully!");
            productPanel.dispose();
        });

        productPanel.setVisible(true);
    }

    @Override
    public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
        return button;
    }
}

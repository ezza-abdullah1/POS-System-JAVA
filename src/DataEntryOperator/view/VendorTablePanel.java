package DataEntryOperator.view;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableCellRenderer;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

import DataEntryOperator.controller.ProductController;
import DataEntryOperator.model.Vendor;

public class VendorTablePanel extends JPanel {
    JTable vendorTable;
    private DefaultTableModel tableModel;
    private JButton addVendorButton;

    public VendorTablePanel() {
        setLayout(new BorderLayout());

        // Top Panel with heading and button
        JPanel topPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g;
                Color color1 = new Color(30, 144, 255); // Blue
                Color color2 = new Color(135, 206, 250); // Lighter Blue
                GradientPaint gradient = new GradientPaint(0, 0, color1, getWidth(), getHeight(), color2);
                g2d.setPaint(gradient);
                g2d.fillRect(0, 0, getWidth(), getHeight());
            }
        };
        topPanel.setLayout(new BorderLayout());
        topPanel.setPreferredSize(new Dimension(getWidth(), 100));

        // Heading label
        JLabel headingLabel = new JLabel("Vendor and Product Management", JLabel.CENTER);
        headingLabel.setFont(new Font("SansSerif", Font.BOLD, 24));
        headingLabel.setForeground(Color.WHITE);
        headingLabel.setBorder(BorderFactory.createEmptyBorder(20, 0, 10, 0));
        topPanel.add(headingLabel, BorderLayout.CENTER);

        // Add Vendor Button
        addVendorButton = new JButton("Add New Vendor");
        styleButton(addVendorButton);
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        buttonPanel.setOpaque(false);
        buttonPanel.add(addVendorButton);
        topPanel.add(buttonPanel, BorderLayout.SOUTH);

        add(topPanel, BorderLayout.NORTH);

        // Table model
        tableModel = new DefaultTableModel(
                new Object[] { "Vendor ID", "Vendor Name", "Vendor Address", "Contact No", "Add Product" }, 0);
        vendorTable = new JTable(tableModel) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return column == 4; // Only "Add Product" column is editable
            }
        };

        // Table Header Styling
        JTableHeader header = vendorTable.getTableHeader();
        header.setFont(new Font("SansSerif", Font.BOLD, 14));
        header.setBackground(new Color(30, 144, 255));
        header.setForeground(Color.WHITE);
        header.setReorderingAllowed(false);

        // Table Row Styling
        vendorTable.setRowHeight(30);
        vendorTable.setFont(new Font("SansSerif", Font.PLAIN, 14));
        vendorTable.setSelectionBackground(new Color(135, 206, 250));
        vendorTable.setSelectionForeground(Color.BLACK);

        // Alternating row colors
        vendorTable.setDefaultRenderer(Object.class, new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected,
                    boolean hasFocus, int row, int column) {
                Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
                if (!isSelected) {
                    c.setBackground(row % 2 == 0 ? Color.WHITE : new Color(240, 248, 255)); // Light blue for alternate
                                                                                            // rows
                }
                return c;
            }
        });

        // Add Product Button Styling
        vendorTable.getColumn("Add Product").setCellRenderer(new ButtonRenderer());
        vendorTable.getColumn("Add Product").setCellEditor(new ButtonEditor(new JCheckBox(), this));

        // Scroll Pane
        JScrollPane scrollPane = new JScrollPane(vendorTable);
        scrollPane.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        add(scrollPane, BorderLayout.CENTER);

        // Panel Background
        setBackground(Color.WHITE);
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

    private void styleButton(JButton button) {
        button.setFont(new Font("SansSerif", Font.BOLD, 14));
        button.setBackground(new Color(30, 144, 255)); // Blue
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        button.setBorder(BorderFactory.createLineBorder(new Color(0, 102, 204), 2));
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));

        // Hover effect
        button.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                button.setBackground(new Color(70, 130, 180)); // Darker blue on hover
            }

            @Override
            public void mouseExited(java.awt.event.MouseEvent evt) {
                button.setBackground(new Color(30, 144, 255)); // Original blue
            }
        });
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

            String productId = productPanel.getProductId();
            String name = productPanel.getProductName();
            String category = productPanel.getCategory();
            double originalPrice = productPanel.getOriginalPrice();
            double salePrice = productPanel.getSalePrice();
            double priceByUnit = productPanel.getPriceByUnit();
            double priceByCarton = productPanel.getPriceByCarton();
            double tax = productPanel.getTax();
            double weight = productPanel.getWeight();
            int quantity = productPanel.getQuantity();

            productController.addProduct(productId, vendorId, name, category, originalPrice, salePrice,
                    priceByUnit, priceByCarton, tax, weight, quantity);

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

package view;
import javax.swing.*;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.DefaultTableModel;

import dao.ProductDAO;
import utils.ButtonUtils;
import utils.MetroCardPaymentCallback;

import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyListener;

public class CashierView extends JFrame {
     private JTextField productIdField, quantityField, cashField;
    private JLabel cashAmountLabel;
    private MetroCardPaymentCallback metroCardPaymentCallback;
    private JButton addItemButton, updateButton, deleteButton, checkoutButton;
    private JTable cartTable;
    private DefaultTableModel cartTableModel;
    private JLabel subtotalLabel, discountLabel, taxLabel, totalLabel, changeLabel;
    private JPanel rightPanel;
    private static final String[] COLUMN_NAMES = {
        "Product ID", "Product Name", "Price", "Weight", 
        "Discount Amount", "Tax Amount", "Quantity", "Subtotal"
    };

    public CashierView() {
        setTitle("POS System - Cashier");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(900, 600);
        setLayout(new BorderLayout());
        initializeComponents();
    }
    public void setMetroCardPaymentCallback(MetroCardPaymentCallback callback) {
        this.metroCardPaymentCallback = callback;
    }

    private void initializeComponents() {
        // Initialize input fields
        productIdField = new JTextField(10);
        quantityField = new JTextField(3);
        quantityField.setText("1");
        cashField = new JTextField(5);

        // Initialize buttons
        addItemButton = new JButton("Add Item");
        updateButton = new JButton("Update Item");
        deleteButton = new JButton("Delete Item");
        checkoutButton = new JButton("Checkout");

        // Initialize table with a more robust table model
        cartTableModel = new DefaultTableModel(COLUMN_NAMES, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return column == 6; // Only quantity column is editable
            }
            
            @Override
            public Class<?> getColumnClass(int column) {
                if (column == 6) return Integer.class; // Quantity column
                return super.getColumnClass(column);
            }
        };
        
        cartTable = new JTable(cartTableModel);
        cartTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        cartTable.getColumnModel().getColumn(6).setCellEditor(new DefaultCellEditor(new JTextField()) {
            @Override
            public boolean stopCellEditing() {
                try {
                    int value = Integer.parseInt(getCellEditorValue().toString());
                    if (value <= 0) throw new NumberFormatException("Quantity must be positive");
                    return super.stopCellEditing();
                } catch (NumberFormatException e) {
                    JOptionPane.showMessageDialog(CashierView.this, 
                        "Please enter a valid positive number", 
                        "Invalid Input", 
                        JOptionPane.ERROR_MESSAGE);
                    return false;
                }
            }
        });
         
        // Initialize labels
        subtotalLabel = new JLabel("Subtotal: Rs0.00");
        discountLabel = new JLabel("Discount: Rs0.00");
        taxLabel = new JLabel("Tax: Rs0.00");
        totalLabel = new JLabel("Total: Rs0.00");
        changeLabel = new JLabel("Change: Rs0.00");

        setupLayout();
    }

    private void setupLayout() {
        // Top panel
        JPanel topPanel = new JPanel(new FlowLayout());
        topPanel.add(new JLabel("Product ID:"));
        topPanel.add(productIdField);
        topPanel.add(new JLabel("Quantity:"));
        topPanel.add(quantityField);
        topPanel.add(addItemButton);
        topPanel.add(updateButton);
        topPanel.add(deleteButton);
        add(topPanel, BorderLayout.NORTH);

        // Center panel (cart table)
        add(new JScrollPane(cartTable), BorderLayout.CENTER);

        // Right panel
        rightPanel = new JPanel(new GridLayout(11, 1));
        rightPanel.add(subtotalLabel);
        rightPanel.add(discountLabel);
        rightPanel.add(taxLabel);
        rightPanel.add(totalLabel);
        // rightPanel.add(new JLabel("Cash:"));
        cashAmountLabel = new JLabel("Cash Amount: Rs 0.00");
        rightPanel.add(cashAmountLabel);
        rightPanel.add(cashField);
        rightPanel.add(changeLabel);
        rightPanel.add(checkoutButton);
        add(rightPanel, BorderLayout.EAST);
    }
    public void clearCart() {
        cartTableModel.setRowCount(0);
    }
    public void resetTotals() {
        updateTotals("0.00", "0.00", "0.00", "0.00");
        setChange("0.00");
        cashAmountLabel.setText("Cash Amount: Rs0.00");
    }

    // Getters for input values
    public String getProductId() { return productIdField.getText(); }
    public int getQuantity() { return Integer.parseInt(quantityField.getText()); }
    public String getCashAmount() { return cashField.getText(); }
    public int getSelectedRow() { return cartTable.getSelectedRow(); }

    // Setters for listeners
    public void setAddItemListener(ActionListener listener) { addItemButton.addActionListener(listener); }
    public void setUpdateItemListener(ActionListener listener) { updateButton.addActionListener(listener); }
    public void setDeleteItemListener(ActionListener listener) { deleteButton.addActionListener(listener); }
    public void setCheckoutListener(ActionListener listener) { checkoutButton.addActionListener(listener); }
    public void setPaymentListener(ActionListener listener) { 
        JButton paymentButton = ButtonUtils.createButton("Payment", listener);
        ((JPanel)getContentPane().getComponent(2)).add(paymentButton); // Add to right panel
    }
    @FunctionalInterface
    public interface CellEditListener {
        void onCellEdit(int row, String newValue);
    }
    // Modify the key listener setter to handle both fields
    public void setAddItemKeyListener(KeyListener listener) {
        productIdField.addKeyListener(listener);
        quantityField.addKeyListener(listener);
    }

    
    public void setCartTableCellEditListener(CellEditListener listener) {
        cartTableModel.addTableModelListener(e -> {
            if (e.getType() == TableModelEvent.UPDATE && e.getColumn() == 6) { // Quantity column
                int row = e.getFirstRow();
                try {
                    String newValue = cartTableModel.getValueAt(row, e.getColumn()).toString();
                    int quantity = Integer.parseInt(newValue);
                    if (quantity > 0) {
                        listener.onCellEdit(row, newValue);
                    } else {
                        throw new NumberFormatException("Quantity must be positive");
                    }
                } catch (NumberFormatException ex) {
                    SwingUtilities.invokeLater(() -> {
                        JOptionPane.showMessageDialog(this, 
                            "Please enter a valid positive number", 
                            "Invalid Input", 
                            JOptionPane.ERROR_MESSAGE);
                        listener.onCellEdit(row, "1"); // Reset to 1 if invalid
                    });
                }
            }
        });
    }
    
    public void revertTableCell(int row, int column) {
        cartTableModel.fireTableCellUpdated(row, column);
    }
    // Table operations
    public void addTableRow(Object[] row) {
        cartTableModel.addRow(row);
    }

    public void updateTableRow(int row, Object[] newData) {
        // Disable table model listeners temporarily
        TableModelListener[] listeners = cartTableModel.getTableModelListeners();
        for (TableModelListener listener : listeners) {
            cartTableModel.removeTableModelListener(listener);
        }
        
        try {
            // Update the data
            for (int i = 0; i < newData.length; i++) {
                cartTableModel.setValueAt(newData[i], row, i);
            }
        } finally {
            // Re-enable listeners
            for (TableModelListener listener : listeners) {
                cartTableModel.addTableModelListener(listener);
            }
        }
        
        // Refresh the table
        cartTableModel.fireTableRowsUpdated(row, row);
    }
    public void removeTableRow(int row) {
        cartTableModel.removeRow(row);
    }

    // Update UI elements
    public void updateTotals(String subtotal, String discount, String tax, String total) {
        subtotalLabel.setText("Subtotal: Rs" + subtotal);
        discountLabel.setText("Discount: Rs" + discount);
        taxLabel.setText("Tax: Rs" + tax);
        totalLabel.setText("Total: Rs" + total);
    }

    public void setChange(String change) {
        changeLabel.setText("Change: Rs" + change);
    }

    public void clearProductInput() {
        productIdField.setText("");
    }

    public void showPaymentDialog(double total, ProductDAO productDAO) {
        JDialog paymentDialog = new JDialog(this, "Payment Options", true);
        paymentDialog.setSize(400, 300);
        paymentDialog.setLayout(new BorderLayout());

        // Payment options panel
        JPanel optionsPanel = new JPanel(new GridLayout(3, 1));
        JButton cashButton = ButtonUtils.createButton("Cash", null);
        JButton metroCardButton = ButtonUtils.createButton("Metro Card", null);
        JButton cardButton = ButtonUtils.createButton("Credit/Debit Card", null);

        optionsPanel.add(cashButton);
        optionsPanel.add(metroCardButton);
        optionsPanel.add(cardButton);
        paymentDialog.add(optionsPanel, BorderLayout.WEST);

        // Card payment panel
        JPanel cardPanel = createCardPanel(total, paymentDialog, productDAO);
        cardPanel.setVisible(false);
        paymentDialog.add(cardPanel, BorderLayout.CENTER);

        // Metro Card payment panel
        JPanel metroCardPanel = createMetroCardPanel(total, paymentDialog, productDAO);
        metroCardPanel.setVisible(false);
        paymentDialog.add(metroCardPanel, BorderLayout.EAST);

        // Cash payment setup
        setupCashPayment(cashButton, paymentDialog, total);

        // Button actions for showing respective panels
        cardButton.addActionListener(e -> {
            cardPanel.setVisible(true);
            metroCardPanel.setVisible(false);
        });

        metroCardButton.addActionListener(e -> {
            metroCardPanel.setVisible(true);
            cardPanel.setVisible(false);
        });

        paymentDialog.setLocationRelativeTo(this);
        paymentDialog.setVisible(true);
    }

    private JPanel createCardPanel(double total, JDialog paymentDialog, ProductDAO productDAO) {
        JPanel cardPanel = new JPanel(new GridLayout(4, 2));
        JLabel totalAmountLabel = new JLabel("Total: Rs" + String.format("%.2f", total));
        JTextField cardNumberField = new JTextField();
        JTextField securityCodeField = new JTextField();
        JButton confirmButton = ButtonUtils.createButton("Confirm", e -> {
            try {
                boolean success = productDAO.processCardPayment(
                    cardNumberField.getText(),
                    securityCodeField.getText(),
                    total
                );
                if (success) {
                    JOptionPane.showMessageDialog(paymentDialog, "Transaction Successful!");
                    paymentDialog.dispose();
                } else {
                    JOptionPane.showMessageDialog(paymentDialog, "Transaction failed! Insufficient balance.");
                }
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(paymentDialog, "Error processing payment: " + ex.getMessage());
            }
        });

        cardPanel.add(new JLabel("Card Number:"));
        cardPanel.add(cardNumberField);
        cardPanel.add(new JLabel("Security Code:"));
        cardPanel.add(securityCodeField);
        cardPanel.add(totalAmountLabel);
        cardPanel.add(new JLabel());
        cardPanel.add(confirmButton);

        return cardPanel;
    }

    private JPanel createMetroCardPanel(double total, JDialog paymentDialog, ProductDAO productDAO) {
        JPanel metroCardPanel = new JPanel(new GridLayout(4, 2));
        JLabel totalAmountLabel = new JLabel("Total: Rs" + String.format("%.2f", total));
        JTextField metroCardNumberField = new JTextField();
        JButton confirmButton = ButtonUtils.createButton("Confirm", e -> {
            try {
                double deductedAmount = productDAO.processMetroCardPayment(
                    metroCardNumberField.getText(),
                    total
                );
                if (deductedAmount > 0) {
                    // Update the UI
                    cashAmountLabel.setText("Points Used: Rs" + String.format("%.2f", deductedAmount));
                    
                    // Notify the controller through callback
                    if (metroCardPaymentCallback != null) {
                        metroCardPaymentCallback.onMetroCardPayment(
                            metroCardNumberField.getText(),
                            deductedAmount
                        );
                    }
                    
                    JOptionPane.showMessageDialog(paymentDialog, 
                        String.format("Payment Successful!\nPoints used: Rs%.2f", deductedAmount));
                    paymentDialog.dispose();
                } else {
                    JOptionPane.showMessageDialog(paymentDialog, "Invalid Metro Card details or insufficient points.");
                }
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(paymentDialog, "Error processing payment: " + ex.getMessage());
            }
        });

        metroCardPanel.add(new JLabel("Metro Card Number:"));
        metroCardPanel.add(metroCardNumberField);
        metroCardPanel.add(totalAmountLabel);
        metroCardPanel.add(new JLabel());
        metroCardPanel.add(confirmButton);

        return metroCardPanel;
    }

    private void setupCashPayment(JButton cashButton, JDialog paymentDialog, double total) {
        cashButton.addActionListener(e -> {
            JDialog cashDialog = new JDialog(paymentDialog, "Enter Cash Payment", true);
            cashDialog.setSize(300, 200);
            cashDialog.setLayout(new FlowLayout());

            JTextField cashAmountField = new JTextField(10);
            JButton okButton = ButtonUtils.createButton("OK", okEvent -> {
                try {
                    double cashAmount = Double.parseDouble(cashAmountField.getText());
                    if (cashAmount < total) {
                        JOptionPane.showMessageDialog(cashDialog, 
                            "Insufficient amount. Please enter a valid cash amount.");
                    } else {
                        double change = cashAmount - total;
                        setChange(String.format("%.2f", change));
                        cashAmountLabel.setText("Cash Amount: Rs" + String.format("%.2f", cashAmount));
                        cashDialog.dispose();
                        paymentDialog.dispose();
                    }
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(cashDialog, 
                        "Invalid cash amount. Please enter a valid number.");
                }
            });

            cashDialog.add(new JLabel("Enter Cash Amount:"));
            cashDialog.add(cashAmountField);
            cashDialog.add(new JLabel("Total: Rs" + String.format("%.2f", total)));
            cashDialog.add(okButton);

            cashDialog.setLocationRelativeTo(paymentDialog);
            cashDialog.setVisible(true);
        });
    }
}

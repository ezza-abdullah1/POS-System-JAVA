//updated cashier ui
package view;
import javax.swing.*;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;

import dao.ProductDAO;
import utils.ButtonUtils;
import utils.MetroCardPaymentCallback;

import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.event.KeyListener;
import java.text.SimpleDateFormat;
import java.util.Date;

public class CashierView extends JFrame {
    private JTextField productIdField, quantityField, cashField;
    private JLabel cashAmountLabel;
    private JLabel branchNTNLabel, cashierIdLabel, billingIdLabel, dateTimeLabel;
    private Timer dateTimeTimer;
    private MetroCardPaymentCallback metroCardPaymentCallback;
    private JButton addItemButton, updateButton, deleteButton, checkoutButton;
    private JTable cartTable;
    private DefaultTableModel cartTableModel;
    private JLabel subtotalLabel;
    private JLabel discountLabel;
    private JLabel taxLabel;
    public JLabel totalLabel;
    private JLabel changeLabel;
    private JPanel rightPanel;
    private JLabel remainingBalanceLabel;
    private static final String[] COLUMN_NAMES = {
            "Product ID", "Product Name", "Price", "Weight",
            "Discount Amount", "Tax Amount", "Quantity", "Subtotal"
    };

    // Custom colors
    private static final Color ACCENT_COLOR = new Color(41, 128, 185);
    private static final Color BG_COLOR = new Color(236, 240, 241);
    private static final Color BUTTON_COLOR = new Color(52, 152, 219);
    private static final Color TEXT_COLOR = new Color(44, 62, 80);
    private static final Color HEADER_BG_COLOR = new Color(41, 128, 185, 50);



    public CashierView() {
        setTitle("Modern POS System");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1200, 800);
        setLayout(new BorderLayout(10, 10));
        setBackground(BG_COLOR);

        setMinimumSize(new Dimension(1000, 600));
        setContentPane(createBackgroundPanel());

        initializeHeaderComponents();
        initializeComponents();
        startDateTimeTimer();

        getRootPane().setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
    }
    private void initializeHeaderComponents() {
        // Initialize header labels with modern styling
        branchNTNLabel = createHeaderLabel("Branch NTN: 1234567890");
        cashierIdLabel = createHeaderLabel("Cashier ID: CASH001");
        billingIdLabel = createHeaderLabel("Bill #: INV" + generateBillNumber());
        dateTimeLabel = createHeaderLabel(getCurrentDateTime());
    }

    private JLabel createHeaderLabel(String text) {
        JLabel label = new JLabel(text);
        label.setFont(new Font("Segoe UI", Font.BOLD, 14));
        label.setForeground(TEXT_COLOR);
        label.setHorizontalAlignment(SwingConstants.CENTER);
        return label;
    }

    private String generateBillNumber() {
        // Simple bill number generation - can be modified later
        return String.format("%06d", (int)(Math.random() * 999999));
    }

    private String getCurrentDateTime() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return sdf.format(new Date());
    }

    private void startDateTimeTimer() {
        dateTimeTimer = new Timer(1000, e -> {
            dateTimeLabel.setText(getCurrentDateTime());
        });
        dateTimeTimer.start();
    }


    @Override
    public void dispose() {
        if (dateTimeTimer != null) {
            dateTimeTimer.stop();
        }
        super.dispose();
    }
    private JPanel createBackgroundPanel() {
        return new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                // Create a gradient background
                Graphics2D g2d = (Graphics2D) g.create();
                g2d.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);

                // Create semi-transparent overlay
                Color startColor = new Color(255, 255, 255, 230);
                Color endColor = new Color(255, 255, 255, 200);
                GradientPaint gp = new GradientPaint(0, 0, startColor, 0, getHeight(), endColor);
                g2d.setPaint(gp);
                g2d.fillRect(0, 0, getWidth(), getHeight());
                g2d.dispose();
            }
        };
    }

    private void initializeComponents() {
        // Initialize input fields with modern styling
        productIdField = createStyledTextField(10);
        quantityField = createStyledTextField(3);
        quantityField.setText("1");
        cashField = createStyledTextField(5);

        // Initialize buttons with modern styling
        addItemButton = createStyledButton("Add Item", BUTTON_COLOR);
        updateButton = createStyledButton("Update Item", BUTTON_COLOR);
        deleteButton = createStyledButton("Delete Item", new Color(231, 76, 60));
        checkoutButton = createStyledButton("Checkout", new Color(46, 204, 113));

        // Initialize table with modern styling
        initializeTable();

        // Initialize labels with modern styling
        subtotalLabel = createStyledLabel("Subtotal: Rs0.00");
        discountLabel = createStyledLabel("Discount: Rs0.00");
        taxLabel = createStyledLabel("Tax: Rs0.00");
        totalLabel = createStyledLabel("Total: Rs0.00");
        remainingBalanceLabel = createStyledLabel("Remaining Balance: Rs0.00");

        changeLabel = createStyledLabel("Change: Rs0.00");
        cashAmountLabel = createStyledLabel("Cash Amount: Rs0.00");


        setupLayout();
    }
    public void updateFinalTotal(String total) {
        remainingBalanceLabel.setText("Remaining Balance: Rs" + total);
        remainingBalanceLabel.setVisible(true);
    }

    private JTextField createStyledTextField(int columns) {
        JTextField field = new JTextField(columns);
        field.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        field.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(ACCENT_COLOR),
                BorderFactory.createEmptyBorder(5, 5, 5, 5)
        ));
        return field;
    }

    private JButton createStyledButton(String text, Color bgColor) {
        JButton button = new JButton(text);
        button.setFont(new Font("Segoe UI", Font.BOLD, 14));
        button.setForeground(Color.WHITE);
        button.setBackground(bgColor);
        button.setBorderPainted(false);
        button.setFocusPainted(false);
        button.setOpaque(true);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));

        // Add hover effect
        button.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                button.setBackground(bgColor.darker());
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                button.setBackground(bgColor);
            }
        });

        return button;
    }

    private JLabel createStyledLabel(String text) {
        JLabel label = new JLabel(text);
        label.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        label.setForeground(TEXT_COLOR);
        return label;
    }

    private void initializeTable() {
        cartTableModel = new DefaultTableModel(COLUMN_NAMES, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return column == 6;
            }

            @Override
            public Class<?> getColumnClass(int column) {
                if (column == 6) return Integer.class;
                return super.getColumnClass(column);
            }
        };

        cartTable = new JTable(cartTableModel) {
            @Override
            public Component prepareRenderer(javax.swing.table.TableCellRenderer renderer, int row, int column) {
                Component comp = super.prepareRenderer(renderer, row, column);
                if (comp instanceof JComponent) {
                    ((JComponent) comp).setOpaque(true);
                }
                if (isCellSelected(row, column)) {
                    comp.setBackground(ACCENT_COLOR.brighter());
                    comp.setForeground(Color.WHITE);
                } else {
                    comp.setBackground(row % 2 == 0 ? Color.WHITE : new Color(245, 245, 245));
                    comp.setForeground(TEXT_COLOR);
                }
                return comp;
            }
        };

        // Style the table
        cartTable.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        cartTable.setRowHeight(30);
        cartTable.setIntercellSpacing(new Dimension(10, 5));
        cartTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        cartTable.setShowGrid(true);
        cartTable.setGridColor(new Color(189, 195, 199));

        // Style the header
        JTableHeader header = cartTable.getTableHeader();
        header.setFont(new Font("Segoe UI", Font.BOLD, 14));
        header.setBackground(ACCENT_COLOR);
        header.setForeground(Color.WHITE);
        header.setBorder(BorderFactory.createLineBorder(ACCENT_COLOR.darker()));
    }
    public void setMetroCardPaymentCallback(MetroCardPaymentCallback callback) {
        this.metroCardPaymentCallback = callback;
    }

    private void setupLayout() {
        // Header panel with store information
        JPanel headerPanel = new JPanel();
        headerPanel.setLayout(new GridBagLayout());
        headerPanel.setOpaque(true);
        headerPanel.setBackground(HEADER_BG_COLOR);
        headerPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 4;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(5, 5, 5, 5);

        // Create centered panel for header information
        JPanel centerHeaderPanel = new JPanel(new GridLayout(2, 2, 20, 5));
        centerHeaderPanel.setOpaque(false);

        // Add header components to the centered panel
        centerHeaderPanel.add(branchNTNLabel);
        centerHeaderPanel.add(cashierIdLabel);
        centerHeaderPanel.add(billingIdLabel);
        centerHeaderPanel.add(dateTimeLabel);

        // Add centered panel to header
        headerPanel.add(centerHeaderPanel, gbc);

        // Top panel with product input fields and buttons
        JPanel topPanel = new JPanel();
        topPanel.setLayout(new FlowLayout(FlowLayout.LEFT, 10, 10));
        topPanel.setOpaque(false);
        topPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // Add components to top panel with labels
        topPanel.add(createStyledLabel("Product ID:"));
        topPanel.add(productIdField);
        topPanel.add(createStyledLabel("Quantity:"));
        topPanel.add(quantityField);
        topPanel.add(Box.createHorizontalStrut(10));
        topPanel.add(addItemButton);
        topPanel.add(updateButton);
        topPanel.add(deleteButton);

        // Combine header and top panel
        JPanel combinedTopPanel = new JPanel(new BorderLayout());
        combinedTopPanel.setOpaque(false);
        combinedTopPanel.add(headerPanel, BorderLayout.NORTH);
        combinedTopPanel.add(topPanel, BorderLayout.CENTER);

        add(combinedTopPanel, BorderLayout.NORTH);

        // Rest of the layout remains the same
        JScrollPane scrollPane = new JScrollPane(cartTable);
        scrollPane.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createEmptyBorder(0, 10, 10, 10),
                BorderFactory.createLineBorder(ACCENT_COLOR)
        ));
        scrollPane.setOpaque(false);
        scrollPane.getViewport().setOpaque(false);
        add(scrollPane, BorderLayout.CENTER);

        // Right panel setup remains the same
        rightPanel = new JPanel();
        rightPanel.setLayout(new BoxLayout(rightPanel, BoxLayout.Y_AXIS));
        rightPanel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createEmptyBorder(0, 10, 10, 10),
                BorderFactory.createLineBorder(ACCENT_COLOR)
        ));
        rightPanel.setOpaque(false);

        Component[] labels = {
                subtotalLabel, Box.createVerticalStrut(10),
                discountLabel, Box.createVerticalStrut(10),
                taxLabel, Box.createVerticalStrut(10),
                totalLabel, Box.createVerticalStrut(20),
                remainingBalanceLabel, Box.createVerticalStrut(10),
                cashAmountLabel, Box.createVerticalStrut(10),
                cashField, Box.createVerticalStrut(10),
                changeLabel, Box.createVerticalStrut(20),
                checkoutButton
        };

        for (Component comp : labels) {
            rightPanel.add(comp);
            if (comp instanceof JLabel) {
                ((JLabel) comp).setAlignmentX(Component.LEFT_ALIGNMENT);
            } else if (comp instanceof JTextField || comp instanceof JButton) {
                comp.setMaximumSize(new Dimension(Integer.MAX_VALUE, comp.getPreferredSize().height));
                ((JComponent) comp).setAlignmentX(Component.LEFT_ALIGNMENT);
            }
        }

        add(rightPanel, BorderLayout.EAST);
    }
    public void clearCart() {
        cartTableModel.setRowCount(0);
    }

    public void resetTotals() {


        remainingBalanceLabel.setText("Remaining Balance: Rs0.00");
        remainingBalanceLabel.setVisible(false);
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
        paymentDialog.setSize(500, 400);
        paymentDialog.setLayout(new BorderLayout(10, 10));

        // Style the dialog
        paymentDialog.getRootPane().setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        ((JPanel)paymentDialog.getContentPane()).setBackground(BG_COLOR);

        // Payment options panel with modern styling
        JPanel optionsPanel = new JPanel(new GridLayout(3, 1, 10, 10));
        optionsPanel.setOpaque(false);

        JButton cashButton = createStyledButton("Cash", BUTTON_COLOR);
        JButton metroCardButton = createStyledButton("Metro Card", BUTTON_COLOR);
        JButton cardButton = createStyledButton("Credit/Debit Card", BUTTON_COLOR);

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


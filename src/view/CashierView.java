package view;
import javax.swing.*;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;

import controller.CashierController;
import dao.ProductDAO;
import model.UserModel;
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
    private JButton addItemButton, updateButton, deleteButton, checkoutButton,paymentButton;
    private  int brnchlbl;
    private JTable cartTable;
    private DefaultTableModel cartTableModel;
    private JLabel subtotalLabel;
    private JLabel discountLabel;
    private JLabel taxLabel;
    private JButton metroCardButton;
    public JLabel totalLabel;
    private JLabel changeLabel;
    private JPanel rightPanel;
    private JLabel remainingBalanceLabel;
    public static int code;
    public static  int id;    
    private static final String[] COLUMN_NAMES = {
            "Product ID", "Product Name", "Price", "Weight",
            "Discount Amount", "Tax Amount", "Quantity", "Subtotal"
    };

    private static final Color ACCENT_COLOR = new Color(79, 70, 229);    // Modern indigo
    private static final Color BG_COLOR = new Color(249, 250, 251);      // Light gray background
    private static final Color BUTTON_COLOR = new Color(99, 102, 241);   // Indigo button
    private static final Color TEXT_COLOR = new Color(17, 24, 39);       // Dark text
    private static final Color HEADER_BG_COLOR = new Color(255, 255, 255); // White header


    public CashierView() {
        setTitle("Cashier Screen");
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
        branchNTNLabel = createHeaderLabel("Branch Code: "+ UserModel.getLoggedInBranchCode());
        cashierIdLabel = createHeaderLabel("Cashier ID: "+ UserModel.getLoggedInUserId());
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
        return String.format("%06d", (int) (Math.random() * 999999));
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
    public int getBranchCode()
    {
        return UserModel.getLoggedInBranchCode();
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
            private Image backgroundImage = new ImageIcon("src\\imgs\\Logo_METRO.svg.png").getImage();

            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);

                // Draw background image
                if (backgroundImage != null) {
                    g.drawImage(backgroundImage, 0, 0, getWidth(), getHeight(), this);
                }

                // Create semi-transparent overlay
                Graphics2D g2d = (Graphics2D) g.create();
                g2d.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);

                // White semi-transparent overlay
                Color overlayColor = new Color(255, 255, 255, 110);
                g2d.setColor(overlayColor);
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
        // Initialize Metro Card button
        metroCardButton = createStyledButton("Pay with Metro Card", new Color(79, 70, 229));
        metroCardButton.setVisible(true); // Initially hidden until checkout
        checkoutButton = createStyledButton("Checkout", new Color(46, 204, 113));
        paymentButton=createStyledButton("Card Payment",new Color(230, 179, 10));

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
        };

        cartTable = new JTable(cartTableModel) {
            @Override
            public Component prepareRenderer(javax.swing.table.TableCellRenderer renderer, int row, int column) {
                Component comp = super.prepareRenderer(renderer, row, column);
                if (comp instanceof JComponent) {
                    ((JComponent) comp).setOpaque(true);
                }
                if (isCellSelected(row, column)) {
                    comp.setBackground(new Color(ACCENT_COLOR.getRed(), ACCENT_COLOR.getGreen(), ACCENT_COLOR.getBlue(), 200));
                    comp.setForeground(Color.WHITE);
                } else {
                    // Semi-transparent alternating rows
                    comp.setBackground(row % 2 == 0 ?
                            new Color(255, 255, 255, 200) :
                            new Color(255, 253, 112, 200));
                    comp.setForeground(TEXT_COLOR);
                }
                return comp;
            }
        };

         // Set preferred column widths (1 cm ≈ 38 pixels)
        int[] columnWidths = {50, 70, 50, 50, 50, 50, 50, 50};
        for (int i = 0; i < columnWidths.length; i++) {
            cartTable.getColumnModel().getColumn(i).setPreferredWidth(columnWidths[i]);
            cartTable.getColumnModel().getColumn(i).setMinWidth(columnWidths[i]);
        }

        cartTable.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        cartTable.setRowHeight(30);
        cartTable.setIntercellSpacing(new Dimension(10, 10));
        cartTable.setShowGrid(true);
        cartTable.setShowHorizontalLines(false);
        cartTable.setGridColor(new Color(229, 231, 235));
        cartTable.setOpaque(false);

        JTableHeader header = cartTable.getTableHeader();
        header.setFont(new Font("Segoe UI Semibold", Font.PLAIN, 10));
        header.setBackground(new Color(255, 255, 255, 200));
        header.setForeground(TEXT_COLOR);
        header.setBorder(BorderFactory.createMatteBorder(0, 0, 2, 0, ACCENT_COLOR));
        header.setPreferredSize(new Dimension(header.getPreferredSize().width, 45));
        header.setOpaque(false);
    }

    public void setMetroCardPaymentCallback(MetroCardPaymentCallback callback) {
        this.metroCardPaymentCallback = callback;
    }

    public void showPaymentDialog(double total, ProductDAO productDAO) {
        JDialog paymentDialog = new JDialog(this, "Payment Options", true);
        paymentDialog.setSize(800, 500);
        paymentDialog.setLayout(new BorderLayout());
        paymentDialog.getContentPane().setBackground(BG_COLOR);

        JPanel mainPanel = new JPanel(new CardLayout());
        mainPanel.setBackground(Color.WHITE);

        // Payment options panel - now with only cash and card options
        JPanel optionsPanel = new JPanel(new GridLayout(2, 1, 0, 20)); // Changed to 2 rows
        optionsPanel.setBorder(BorderFactory.createEmptyBorder(30, 30, 30, 30));
        optionsPanel.setBackground(Color.WHITE);

        String[] options = {"Cash Payment", "Card Payment"}; // Removed Metro Card
        JButton[] optionButtons = new JButton[options.length];
        ButtonGroup buttonGroup = new ButtonGroup();

        for (int i = 0; i < options.length; i++) {
            optionButtons[i] = createPaymentOptionButton(options[i]);
            optionsPanel.add(optionButtons[i]);
            buttonGroup.add(optionButtons[i]);
        }

        // Create payment panels
        JPanel cashPanel = createModernCashPanel(total, paymentDialog);
        JPanel cardPanel = createCardPanel(total, paymentDialog, productDAO);

        mainPanel.add(cashPanel, "CASH");
        mainPanel.add(cardPanel, "CARD");

        CardLayout cardLayout = (CardLayout) mainPanel.getLayout();

        // Add action listeners for panel switching
        optionButtons[0].addActionListener(e -> cardLayout.show(mainPanel, "CASH"));
        optionButtons[1].addActionListener(e -> cardLayout.show(mainPanel, "CARD"));

        paymentDialog.add(optionsPanel, BorderLayout.WEST);
        paymentDialog.add(mainPanel, BorderLayout.CENTER);

        paymentDialog.setLocationRelativeTo(this);
        paymentDialog.setVisible(true);
    }

    // Add new method for Metro Card button listener
    public void setMetroCardButtonListener(ActionListener listener) {
        metroCardButton.addActionListener(listener);
    }

    // Add method to show/hide Metro Card button
    public void setMetroCardButtonVisible(boolean visible) {
        metroCardButton.setVisible(visible);
    }
    private JButton createPaymentOptionButton(String text) {
        JButton button = new JButton(text);
        button.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        button.setForeground(TEXT_COLOR);
        button.setBackground(Color.WHITE);
        button.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(209, 213, 219)),
                BorderFactory.createEmptyBorder(15, 20, 15, 20)
        ));
        button.setFocusPainted(false);

        button.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                button.setBackground(new Color(243, 244, 246));
            }

            public void mouseExited(java.awt.event.MouseEvent evt) {
                button.setBackground(Color.WHITE);
            }
        });

        return button;
    }
    private void setupLayout() {
        // Header panel with store information


        JPanel headerPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2d = (Graphics2D) g.create();
                g2d.setColor(new Color(255, 255, 255, 180));
                g2d.fillRect(0, 0, getWidth(), getHeight());
                super.paintComponent(g);
            }
        };
        headerPanel.setOpaque(false);
        JPanel topPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2d = (Graphics2D) g.create();
                g2d.setColor(new Color(255, 255, 255, 180));
                g2d.fillRect(0, 0, getWidth(), getHeight());
                super.paintComponent(g);
            }
        };
        topPanel.setOpaque(false);

        rightPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2d = (Graphics2D) g.create();
                g2d.setColor(new Color(255, 255, 255, 180));
                g2d.fillRect(0, 0, getWidth(), getHeight());
                super.paintComponent(g);
            }
        };
        rightPanel.setOpaque(false);

        headerPanel.setLayout(new GridBagLayout());
        headerPanel.setOpaque(true);
        headerPanel.setBackground(HEADER_BG_COLOR);
        headerPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // Add header image
        ImageIcon headerIcon = new ImageIcon("src/imgs/Logo_METRO.svg.png"); // Replace with actual path
        JLabel headerImageLabel = new JLabel(headerIcon);
        headerImageLabel.setPreferredSize(new Dimension(50, 50)); // Adjust size as needed
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.weightx = 0.2;
        headerPanel.add(headerImageLabel, gbc);

        // Create centered panel for header information
        JPanel centerHeaderPanel = new JPanel(new GridLayout(2, 2, 20, 5));
        centerHeaderPanel.setOpaque(false);

        // Add header components to the centered panel
        centerHeaderPanel.add(branchNTNLabel);
        centerHeaderPanel.add(cashierIdLabel);
        centerHeaderPanel.add(billingIdLabel);
        centerHeaderPanel.add(dateTimeLabel);

        // Add centered panel to header
        gbc.gridx = 1;
        gbc.weightx = 0.8;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        headerPanel.add(centerHeaderPanel, gbc);

        // Top panel with product input fields and buttons

        topPanel.setLayout(new FlowLayout(FlowLayout.LEFT, 10, 10));

        topPanel.setBorder(BorderFactory.createEmptyBorder(10, 30, 10, 30));
        // Add components to top panel with labels
        topPanel.add(createStyledLabel("Product ID:"));
        topPanel.add(productIdField);
        topPanel.add(createStyledLabel("Quantity:"));
        topPanel.add(quantityField);
        topPanel.add(Box.createHorizontalStrut(10));
        topPanel.add(addItemButton);
        topPanel.add(updateButton);
        topPanel.add(deleteButton);
        topPanel.add(paymentButton);

        // Combine header and top panel
        JPanel combinedTopPanel = new JPanel(new BorderLayout());
        combinedTopPanel.setOpaque(false);
        combinedTopPanel.add(headerPanel, BorderLayout.NORTH);
        combinedTopPanel.add(topPanel, BorderLayout.CENTER);
        add(combinedTopPanel, BorderLayout.NORTH);
        JScrollPane scrollPane = new JScrollPane(cartTable) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2d = (Graphics2D) g.create();
                g2d.setColor(new Color(255, 255, 255, 180));
                g2d.fillRect(0, 0, getWidth(), getHeight());
                super.paintComponent(g);
            }
        };
        scrollPane.setOpaque(false);
        scrollPane.getViewport().setOpaque(false);
        scrollPane.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createEmptyBorder(0, 30, 10, 30),
                BorderFactory.createLineBorder(ACCENT_COLOR)
        ));
        add(scrollPane, BorderLayout.CENTER);

        // Right panel setup remains the same

        rightPanel.setLayout(new BoxLayout(rightPanel, BoxLayout.Y_AXIS));
        rightPanel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createEmptyBorder(0, 30, 10, 30),
                BorderFactory.createLineBorder(ACCENT_COLOR)
        ));

// Modify the right panel setup to include Metro Card button
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
                metroCardButton, Box.createVerticalStrut(10), // Added Metro Card button
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
    public String getProductId() {
        return productIdField.getText();
    }

    public int getQuantity() {
        return Integer.parseInt(quantityField.getText());
    }

    public String getCashAmount() {
        return cashField.getText();
    }

    public int getSelectedRow() {
        return cartTable.getSelectedRow();
    }

    // Setters for listeners
    public void setAddItemListener(ActionListener listener) {
        addItemButton.addActionListener(listener);
    }

    public void setUpdateItemListener(ActionListener listener) {
        updateButton.addActionListener(listener);
    }

    public void setDeleteItemListener(ActionListener listener) {
        deleteButton.addActionListener(listener);
    }

    public void setCheckoutListener(ActionListener listener) {
        checkoutButton.addActionListener(listener);
    }

    public void setPaymentListener(ActionListener listener) {
        paymentButton.addActionListener(listener);
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

    private JPanel createModernCashPanel(double total, JDialog paymentDialog) {
        JPanel cashPanel = new JPanel();
        cashPanel.setLayout(new BoxLayout(cashPanel, BoxLayout.Y_AXIS));
        cashPanel.setBackground(Color.WHITE);
        cashPanel.setBorder(BorderFactory.createEmptyBorder(30, 30, 30, 30));

        // Title
        JLabel titleLabel = new JLabel("Cash Payment");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 24));
        titleLabel.setAlignmentX(Component.LEFT_ALIGNMENT);

        // Amount panel
        JPanel amountPanel = new JPanel();
        amountPanel.setLayout(new BoxLayout(amountPanel, BoxLayout.Y_AXIS));
        amountPanel.setBackground(Color.WHITE);
        amountPanel.setAlignmentX(Component.LEFT_ALIGNMENT);
        amountPanel.setBorder(BorderFactory.createEmptyBorder(20, 0, 20, 0));

        JLabel totalLabel = new JLabel(String.format("Total Amount: Rs%.2f", total));
        totalLabel.setFont(new Font("Segoe UI", Font.PLAIN, 18));

        JLabel cashLabel = new JLabel("Enter Cash Amount:");
        cashLabel.setFont(new Font("Segoe UI", Font.PLAIN, 16));

        JTextField cashAmountField = new JTextField();
        cashAmountField.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        cashAmountField.setMaximumSize(new Dimension(300, 40));
        cashAmountField.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(209, 213, 219)),
                BorderFactory.createEmptyBorder(5, 10, 5, 10)
        ));

        JButton confirmButton = new JButton("Confirm Payment");
        confirmButton.setFont(new Font("Segoe UI", Font.BOLD, 16));
        confirmButton.setBackground(ACCENT_COLOR);
        confirmButton.setForeground(Color.WHITE);
        confirmButton.setBorderPainted(false);
        confirmButton.setFocusPainted(false);
        confirmButton.setMaximumSize(new Dimension(300, 50));
        confirmButton.setAlignmentX(Component.LEFT_ALIGNMENT);

        confirmButton.addActionListener(e -> {
            try {
                double cashAmount = Double.parseDouble(cashAmountField.getText());
                if (cashAmount < total) {
                    JOptionPane.showMessageDialog(cashPanel,
                            "Insufficient amount. Please enter at least Rs" + String.format("%.2f", total),
                            "Invalid Amount",
                            JOptionPane.WARNING_MESSAGE);
                } else {
                    double change = cashAmount - total;
                    setChange(String.format("%.2f", change));
                    cashAmountLabel.setText("Cash Amount: Rs" + String.format("%.2f", cashAmount));
                    paymentDialog.dispose();
                }
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(cashPanel,
                        "Please enter a valid amount",
                        "Invalid Input",
                        JOptionPane.ERROR_MESSAGE);
            }
        });

        // Add components with spacing
        amountPanel.add(totalLabel);
        amountPanel.add(Box.createVerticalStrut(20));
        amountPanel.add(cashLabel);
        amountPanel.add(Box.createVerticalStrut(10));
        amountPanel.add(cashAmountField);
        amountPanel.add(Box.createVerticalStrut(30));
        amountPanel.add(confirmButton);

        cashPanel.add(titleLabel);
        cashPanel.add(Box.createVerticalStrut(20));
        cashPanel.add(amountPanel);
        cashPanel.add(Box.createVerticalGlue());

        return cashPanel;
    }

    private JPanel createCardPanel(double total, JDialog paymentDialog, ProductDAO productDAO) {
        JPanel cardPanel = new JPanel();
        cardPanel.setLayout(new BoxLayout(cardPanel, BoxLayout.Y_AXIS));
        cardPanel.setBackground(Color.WHITE);
        cardPanel.setBorder(BorderFactory.createEmptyBorder(30, 30, 30, 30));

        // Title
        JLabel titleLabel = new JLabel("Card Payment");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 24));
        titleLabel.setAlignmentX(Component.LEFT_ALIGNMENT);

        // Form panel
        JPanel formPanel = new JPanel();
        formPanel.setLayout(new BoxLayout(formPanel, BoxLayout.Y_AXIS));
        formPanel.setBackground(Color.WHITE);
        formPanel.setAlignmentX(Component.LEFT_ALIGNMENT);
        formPanel.setBorder(BorderFactory.createEmptyBorder(20, 0, 20, 0));

        JLabel totalLabel = new JLabel(String.format("Total Amount: Rs%.2f", total));
        totalLabel.setFont(new Font("Segoe UI", Font.PLAIN, 18));

        // Card number field
        JLabel cardLabel = new JLabel("Card Number:");
        cardLabel.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        JTextField cardNumberField = createStyledTextField();

        // Security code field
        JLabel securityLabel = new JLabel("Security Code:");
        securityLabel.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        JTextField securityCodeField = createStyledTextField();

        JButton confirmButton = new JButton("Process Payment");
        confirmButton.setFont(new Font("Segoe UI", Font.BOLD, 16));
        confirmButton.setBackground(ACCENT_COLOR);
        confirmButton.setForeground(Color.WHITE);
        confirmButton.setBorderPainted(false);
        confirmButton.setFocusPainted(false);
        confirmButton.setMaximumSize(new Dimension(300, 50));
        confirmButton.setAlignmentX(Component.LEFT_ALIGNMENT);

        confirmButton.addActionListener(e -> {
            try {
                boolean success = productDAO.processCardPayment(
                        cardNumberField.getText(),
                        securityCodeField.getText(),
                        total
                );
                if (success) {
                    JOptionPane.showMessageDialog(cardPanel,
                            "Payment Successful!",
                            "Success",
                            JOptionPane.INFORMATION_MESSAGE);
                    paymentDialog.dispose();
                } else {
                    JOptionPane.showMessageDialog(cardPanel,
                            "Transaction failed! Insufficient balance.",
                            "Payment Failed",
                            JOptionPane.ERROR_MESSAGE);
                }
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(cardPanel,
                        "Error processing payment: " + ex.getMessage(),
                        "Error",
                        JOptionPane.ERROR_MESSAGE);
            }
        });

        // Add components with spacing
        formPanel.add(totalLabel);
        formPanel.add(Box.createVerticalStrut(20));
        formPanel.add(cardLabel);
        formPanel.add(Box.createVerticalStrut(5));
        formPanel.add(cardNumberField);
        formPanel.add(Box.createVerticalStrut(15));
        formPanel.add(securityLabel);
        formPanel.add(Box.createVerticalStrut(5));
        formPanel.add(securityCodeField);
        formPanel.add(Box.createVerticalStrut(30));
        formPanel.add(confirmButton);

        cardPanel.add(titleLabel);
        cardPanel.add(Box.createVerticalStrut(20));
        cardPanel.add(formPanel);
        cardPanel.add(Box.createVerticalGlue());

        return cardPanel;
    }

    private JPanel createModernMetroPanel(double total, JDialog paymentDialog, ProductDAO productDAO) {
        JPanel metroPanel = new JPanel();
        metroPanel.setLayout(new BoxLayout(metroPanel, BoxLayout.Y_AXIS));
        metroPanel.setBackground(Color.WHITE);
        metroPanel.setBorder(BorderFactory.createEmptyBorder(30, 30, 30, 30));

        // Title
        JLabel titleLabel = new JLabel("Metro Card Payment");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 24));
        titleLabel.setAlignmentX(Component.LEFT_ALIGNMENT);

        // Form panel
        JPanel formPanel = new JPanel();
        formPanel.setLayout(new BoxLayout(formPanel, BoxLayout.Y_AXIS));
        formPanel.setBackground(Color.WHITE);
        formPanel.setAlignmentX(Component.LEFT_ALIGNMENT);
        formPanel.setBorder(BorderFactory.createEmptyBorder(20, 0, 20, 0));

        JLabel totalLabel = new JLabel(String.format("Total Amount: Rs%.2f", total));
        totalLabel.setFont(new Font("Segoe UI", Font.PLAIN, 18));

        JLabel cardLabel = new JLabel("Metro Card Number:");
        cardLabel.setFont(new Font("Segoe UI", Font.PLAIN, 16));

        JTextField metroCardField = createStyledTextField();

        JButton confirmButton = new JButton("Process Payment");
        confirmButton.setFont(new Font("Segoe UI", Font.BOLD, 16));
        confirmButton.setBackground(ACCENT_COLOR);
        confirmButton.setForeground(Color.WHITE);
        confirmButton.setBorderPainted(false);
        confirmButton.setFocusPainted(false);
        confirmButton.setMaximumSize(new Dimension(300, 50));
        confirmButton.setAlignmentX(Component.LEFT_ALIGNMENT);

        confirmButton.addActionListener(e -> {
            try {
                double deductedAmount = productDAO.processMetroCardPayment(
                        metroCardField.getText(),
                        total
                );
                if (deductedAmount > 0) {
                    cashAmountLabel.setText("Points Used: Rs" + String.format("%.2f", deductedAmount));

                    if (metroCardPaymentCallback != null) {
                        metroCardPaymentCallback.onMetroCardPayment(
                                metroCardField.getText(),
                                deductedAmount
                        );
                    }

                    JOptionPane.showMessageDialog(metroPanel,
                            String.format("Payment Successful!\nPoints used: Rs%.2f", deductedAmount),
                            "Success",
                            JOptionPane.INFORMATION_MESSAGE);
                    paymentDialog.dispose();
                } else {
                    JOptionPane.showMessageDialog(metroPanel,
                            "Invalid Metro Card details or insufficient points.",
                            "Payment Failed",
                            JOptionPane.ERROR_MESSAGE);
                }
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(metroPanel,
                        "Error processing payment: " + ex.getMessage(),
                        "Error",
                        JOptionPane.ERROR_MESSAGE);
            }
        });

        // Add components with spacing
        formPanel.add(totalLabel);
        formPanel.add(Box.createVerticalStrut(20));
        formPanel.add(cardLabel);
        formPanel.add(Box.createVerticalStrut(5));
        formPanel.add(metroCardField);
        formPanel.add(Box.createVerticalStrut(30));
        formPanel.add(confirmButton);

        metroPanel.add(titleLabel);
        metroPanel.add(Box.createVerticalStrut(20));
        metroPanel.add(formPanel);
        metroPanel.add(Box.createVerticalGlue());

        return metroPanel;
    }

    // Helper method to create consistently styled text fields
    private JTextField createStyledTextField() {
        JTextField field = new JTextField();
        field.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        field.setMaximumSize(new Dimension(300, 40));
        field.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(209, 213, 219)),
                BorderFactory.createEmptyBorder(5, 10, 5, 10)
        ));
        return field;
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
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            CashierView view = new CashierView();
            CashierController controller = new CashierController(view);
            view.setVisible(true);

        });
    }

}


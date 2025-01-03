package DataEntryOperator.view;

import DataEntryOperator.controller.VendorController;
import DataEntryOperator.controller.ProductController;
import DataEntryOperator.model.Vendor;
import SuperAdmin.view.updateBranchView;
import utils.RoundedButton;

import javax.swing.*;
import javax.swing.table.*;
import java.awt.*;
import java.awt.event.*;
import java.util.List;

public class MainDashboard extends JFrame {
    private JTable vendorTable;
    private DefaultTableModel tableModel;
    private VendorController vendorController;
    private JTextField searchField;

    public MainDashboard() {
        setTitle("Vendor and Product Management");
        setSize(1200, 800);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        vendorController = new VendorController();

        // Header Panel
        JPanel headerPanel = new JPanel();
        headerPanel.setLayout(new BorderLayout());
        headerPanel.setBackground(new Color(70, 130, 180));

        ImageIcon headerIcon = new ImageIcon("src\\imgs\\Logo_METRO.svg.png");
        Image scaledImage = headerIcon.getImage().getScaledInstance(getWidth(), 100, Image.SCALE_SMOOTH);
        JLabel imageLabel = new JLabel(new ImageIcon(scaledImage));
        imageLabel.setHorizontalAlignment(SwingConstants.CENTER);

        headerPanel.add(imageLabel, BorderLayout.NORTH);

        // Side Panel
        JPanel sidePanel = new JPanel();
        sidePanel.setLayout(new GridLayout(4, 1, 10, 10));
        sidePanel.setBackground(new Color(70, 130, 160));
        sidePanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        RoundedButton addVendorButton = new RoundedButton("Add New Vendor");
        RoundedButton updateVendorButton = new RoundedButton("Update Vendor");
        RoundedButton deleteVendorButton = new RoundedButton("Delete Vendor");
        RoundedButton viewVendorButton = new RoundedButton("View All Vendors");

        RoundedButton[] buttons = { addVendorButton, updateVendorButton, deleteVendorButton, viewVendorButton };
        for (RoundedButton button : buttons) {
            button.setFont(new Font("SansSerif", Font.BOLD, 14));
            button.setBackground(new Color(0x003366));
            button.setForeground(Color.WHITE);
            button.setFocusPainted(false);
            sidePanel.add(button);
        }

        // Table Panel
        JPanel tablePanel = new JPanel(new BorderLayout());
        tablePanel.setBackground(Color.WHITE);

        // Search Panel (Below the heading)
        JPanel searchPanel = new JPanel();
        searchPanel.setLayout(new FlowLayout(FlowLayout.RIGHT)); // Align to the right
        searchPanel.setBackground(new Color(70, 130, 180));
        searchPanel.setPreferredSize(new Dimension(250, 50)); // Increase size

        JLabel searchLabel = new JLabel("Search:");
        searchLabel.setForeground(Color.WHITE);
        searchLabel.setFont(new Font("SansSerif", Font.BOLD, 14));

        searchField = new JTextField(20);
        searchField.setToolTipText("Search vendors...");
        searchField.setFont(new Font("SansSerif", Font.PLAIN, 16)); // Make the font modern
        searchField.setPreferredSize(new Dimension(200, 35)); // Increase width and height
        searchField.setBorder(BorderFactory.createLineBorder(new Color(70, 130, 180), 2)); // Modern border
        searchField.addKeyListener(new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent e) {
                filterTable(searchField.getText());
            }
        });

        searchPanel.add(searchLabel);
        searchPanel.add(searchField);

        // Heading Label
        JLabel headingLabel = new JLabel("Vendor and Product Management", JLabel.CENTER);
        headingLabel.setFont(new Font("Segoe UI", Font.BOLD, 24));
        headingLabel.setForeground(Color.WHITE);

        // Combine heading and search panel
        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.setBackground(new Color(70, 130, 180));
        topPanel.add(headingLabel, BorderLayout.NORTH);
        topPanel.add(searchPanel, BorderLayout.SOUTH);

        tablePanel.add(topPanel, BorderLayout.NORTH);

        // Vendor Table
        tableModel = new DefaultTableModel(
                new Object[] { "Vendor ID", "Vendor Name", "Vendor Address", "Contact No", "Add Product" }, 0);
        vendorTable = new JTable(tableModel) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return column == 4; // Allow editing only for "Add Product" column
            }
        };

        JTableHeader header = vendorTable.getTableHeader();
        header.setFont(new Font("Segoe UI", Font.BOLD, 16));
        header.setBackground(new Color(70, 130, 180));
        header.setForeground(Color.WHITE);

        vendorTable.setRowHeight(35);
        vendorTable.setFont(new Font("Segoe UI", Font.PLAIN, 14));

        vendorTable.getColumn("Add Product").setCellRenderer(new ButtonRenderer());
        vendorTable.getColumn("Add Product").setCellEditor(new ButtonEditor(new JCheckBox()));

        JScrollPane scrollPane = new JScrollPane(vendorTable);
        tablePanel.add(scrollPane, BorderLayout.CENTER);

        // Button Listeners
        addVendorButton.addActionListener(e -> openAddVendorDialog());
        updateVendorButton.addActionListener(e -> openUpdateVendorScreen());
        deleteVendorButton.addActionListener(e -> deleteSelectedVendor());
        viewVendorButton.addActionListener(e -> fetchAndDisplayVendors());

        // Add panels to frame
        add(headerPanel, BorderLayout.NORTH);
        add(sidePanel, BorderLayout.WEST);
        add(tablePanel, BorderLayout.CENTER);
        setVisible(true);
        setLocationRelativeTo(null);
        fetchAndDisplayVendors(); // Load data initially
    }

    private void fetchAndDisplayVendors() {
        List<Vendor> vendors = vendorController.fetchVendors();
        tableModel.setRowCount(0); // Clear table
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

    private void openAddVendorDialog() {
        AddVendorPanel addVendorPanel = new AddVendorPanel();
        addVendorPanel.addSaveListener(event -> {
            String name = addVendorPanel.getVendorName();
            String address = addVendorPanel.getVendorAddress();
            String contact = addVendorPanel.getContactNo();

            vendorController.addVendor(name, address, contact);
            JOptionPane.showMessageDialog(this, "Vendor added successfully.");

            fetchAndDisplayVendors();
            addVendorPanel.dispose();
        });

        addVendorPanel.setVisible(true);
    }

    private void deleteSelectedVendor() {
        int selectedRow = vendorTable.getSelectedRow();
        if (selectedRow >= 0) {
            int vendorId = (int) tableModel.getValueAt(selectedRow, 0);

            int confirmation = JOptionPane.showConfirmDialog(
                    this,
                    "Are you sure you want to delete this vendor?",
                    "Confirm Deletion",
                    JOptionPane.YES_NO_OPTION,
                    JOptionPane.WARNING_MESSAGE);

            if (confirmation == JOptionPane.YES_OPTION) {
                vendorController.deleteVendor(vendorId);
                fetchAndDisplayVendors();
            }
        } else {
            JOptionPane.showMessageDialog(this, "Please select a vendor to delete.");
        }
    }

    private void openUpdateVendorScreen() {
        int selectedRow = vendorTable.getSelectedRow();
        if (selectedRow != -1) {

            int id = (int) vendorTable.getValueAt(selectedRow, 0);
            new updateVendorView(this, vendorController, id, this::refreshTable);
        } else {
            JOptionPane.showMessageDialog(this, "Please select a branch to update.");
        }
    }

    public void refreshTable() {
        fetchAndDisplayVendors();
    }

    private void filterTable(String searchText) {
        TableRowSorter<DefaultTableModel> sorter = new TableRowSorter<>(tableModel);
        vendorTable.setRowSorter(sorter);

        sorter.setRowFilter(RowFilter.regexFilter("(?i)" + searchText));
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new MainDashboard().setVisible(true));
    }

    class ButtonRenderer extends JButton implements TableCellRenderer {
        public ButtonRenderer() {
            setText("+");
            setBackground(Color.WHITE);
        }

        @Override
        public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus,
                int row, int column) {
            return this;
        }
    }

    class ButtonEditor extends DefaultCellEditor {
        private JButton button;

        public ButtonEditor(JCheckBox checkBox) {
            super(checkBox);
            button = new JButton("+");
            button.setBackground(Color.WHITE);
            button.addActionListener(e -> handleAddProduct());
        }

        private void handleAddProduct() {
            int row = vendorTable.getSelectedRow();
            if (row >= 0) {
                int vendorId = (int) tableModel.getValueAt(row, 0);
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
        }

        @Override
        public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row,
                int column) {
            return button;
        }
    }
}

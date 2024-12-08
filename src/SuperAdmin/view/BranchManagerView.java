package SuperAdmin.view;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableRowSorter;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

import SuperAdmin.model.UserModel;
import utils.RoundedButton;
import SuperAdmin.controller.UserController;

public class BranchManagerView extends JFrame {
    private RoundedButton addButton;
    private RoundedButton updateButton;
    private RoundedButton deleteButton;
    private RoundedButton viewButton;
    private JTable branchManagerTable;
    private DefaultTableModel tableModel;
    private UserController userController = new UserController();
    private JTextField searchField;

    public BranchManagerView() {
        setTitle("Manage Branch Managers");
        setSize(900, 600);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout());

        // Header with Image
        JPanel headerPanel = new JPanel();
        headerPanel.setLayout(new BorderLayout());
        headerPanel.setBackground(new Color(70, 130, 180));

        // Full-Width Image
        ImageIcon headerIcon = new ImageIcon("src\\imgs\\Logo_METRO.svg.png");
        Image scaledImage = headerIcon.getImage().getScaledInstance(getWidth(), 100, Image.SCALE_SMOOTH);
        JLabel imageLabel = new JLabel(new ImageIcon(scaledImage));
        imageLabel.setHorizontalAlignment(SwingConstants.CENTER);

        // Title Label
        JLabel titleLabel = new JLabel("Manage Branch Managers", SwingConstants.CENTER);
        titleLabel.setFont(new Font("SansSerif", Font.BOLD, 24));
        titleLabel.setForeground(Color.WHITE);

        // Add components to the header
        headerPanel.add(imageLabel, BorderLayout.NORTH);
        headerPanel.add(titleLabel, BorderLayout.SOUTH);

        // Side Panel (Button Panel)
        JPanel sidePanel = new JPanel();
        sidePanel.setLayout(new GridLayout(4, 1, 10, 10));
        sidePanel.setBackground(new Color(70, 130, 160));
        sidePanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // Search Panel
        JPanel searchPanel = new JPanel();
        searchPanel.setLayout(new FlowLayout());
        searchPanel.setBackground(new Color(70, 130, 160));

        JLabel searchLabel = new JLabel("Search:");
        searchLabel.setForeground(Color.WHITE);
        searchLabel.setFont(new Font("SansSerif", Font.BOLD, 14));
        searchField = new JTextField(20);
        searchField.setToolTipText("Search branch managers...");
        searchField.addKeyListener(new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent e) {
                filterTable(searchField.getText());
            }
        });

        searchPanel.add(searchLabel);
        searchPanel.add(searchField);

        // Buttons
        addButton = new RoundedButton("Add New Branch Manager");
        updateButton = new RoundedButton("Update Branch Manager");
        deleteButton = new RoundedButton("Delete Branch Manager");
        viewButton = new RoundedButton("View All Branch Managers");

        RoundedButton[] buttons = {addButton, updateButton, deleteButton, viewButton};
        setButtonProperties(buttons);

        for (RoundedButton button : buttons) {
            sidePanel.add(button);
        }

        // Table
        tableModel = new DefaultTableModel(
                new String[]{"Name", "Email", "BranchCode", "Salary", "EmpNumber", "AppointedOn", "UpdatedAt"},
                0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        
        branchManagerTable = new JTable(tableModel);
        branchManagerTable.setFillsViewportHeight(true);
        branchManagerTable.setRowHeight(25);
        
        JTableHeader tableHeader = branchManagerTable.getTableHeader();
        tableHeader.setFont(new Font("SansSerif", Font.BOLD, 20));
        tableHeader.setBackground(new Color(25, 25, 130));
        tableHeader.setForeground(Color.WHITE);
        branchManagerTable.setTableHeader(tableHeader);
        
        JScrollPane scrollPane = new JScrollPane(branchManagerTable);

        // Add components to the frame
        add(headerPanel, BorderLayout.NORTH);
        add(sidePanel, BorderLayout.WEST);
        add(scrollPane, BorderLayout.CENTER);

        // Event Listeners
        addButton.addActionListener(e -> openAddBranchManagerScreen());
        updateButton.addActionListener(e -> openUpdateBranchManagerScreen());
        deleteButton.addActionListener(e -> deleteSelectedBranchManager());
        viewButton.addActionListener(e -> loadBranchManagerData());

        setLocationRelativeTo(null);
        setVisible(true);
    }

    private void setButtonProperties(RoundedButton[] buttons) {
        for (RoundedButton button : buttons) {
            button.setFont(new Font("SansSerif", Font.BOLD, 14));
            button.setBackground(new Color(70, 130, 180));
            button.setForeground(Color.WHITE);
            button.setFocusPainted(false);
            button.setPreferredSize(new Dimension(button.getPreferredSize().width, 
                button.getPreferredSize().height - 28));
        }
    }

    public void addBranchManagerToTable(UserModel user) {
        tableModel.addRow(new Object[] {
                user.getName(),
                user.getEmail(),
                user.getBranchCode(),
                user.getSalary(),
                user.getEmpNumber(),
                user.getCreatedAt(), // Changed from user.getCreatedAt() to user.getAppointedOn()
                user.getUpdatedAt()
        });
    }

    public void setUserController(UserController userController) {
        this.userController = userController;
    }

    public void loadBranchManagerData() {
        tableModel.setRowCount(0);

        userController.loadBranchManagerDataToView(this);
    }

    private void openAddBranchManagerScreen() {
        try {
            new AddBranchManagerView(this);
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error opening Add Branch Manager screen: " + e.getMessage());
        }
    }

    private void openUpdateBranchManagerScreen() {
        int selectedRow = branchManagerTable.getSelectedRow();
        if (selectedRow != -1) {
            int branchCode = (int) branchManagerTable.getValueAt(selectedRow, 2);
            new UpdateBranchManagerView(this, userController, branchCode, this::refreshTable);
        } else {
            JOptionPane.showMessageDialog(this, "Please select a branch manager to update.");
        }
    }

    public void refreshTable() {
        loadBranchManagerData();
    }

    private void deleteSelectedBranchManager() {
        int selectedRow = branchManagerTable.getSelectedRow();
        if (selectedRow >= 0) {
            int branchCode = (int) branchManagerTable.getValueAt(selectedRow, 2);

            int confirmation = JOptionPane.showConfirmDialog(
                    this,
                    "Are you sure you want to delete this branch manager?",
                    "Confirm Deletion",
                    JOptionPane.YES_NO_OPTION,
                    JOptionPane.WARNING_MESSAGE);

            if (confirmation == JOptionPane.YES_OPTION) {
                try {
                    String resultMessage = userController.deleteBranchManager(branchCode);

                    if (resultMessage.equals("Branch Manager deleted successfully.")) {
                        tableModel.removeRow(selectedRow);
                        JOptionPane.showMessageDialog(this, resultMessage);
                    } else {
                        JOptionPane.showMessageDialog(this, resultMessage, "Error", JOptionPane.ERROR_MESSAGE);
                    }

                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage(), "Error",
                            JOptionPane.ERROR_MESSAGE);
                }
            } else {
                // User cancelled the deletion
                JOptionPane.showMessageDialog(this, "Deletion cancelled.");
            }
        } else {
            JOptionPane.showMessageDialog(this, "Please select a branch manager to delete.");
        }
    }

    private void filterTable(String searchText) {
        TableRowSorter<DefaultTableModel> sorter = new TableRowSorter<>(tableModel);
        branchManagerTable.setRowSorter(sorter);

        sorter.setRowFilter(RowFilter.regexFilter("(?i)" + searchText));
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(BranchManagerView::new);
    }
}

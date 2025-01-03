package view;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableRowSorter;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

import model.UserModel;
import controller.UserController;
import utils.RoundedButton;

public class UserTableView extends JFrame {
    private RoundedButton addButton;
    private RoundedButton updateButton;
    private RoundedButton deleteButton;
    private RoundedButton viewButton;
    private JTable userTable;
    private DefaultTableModel tableModel;
    private UserController userController = new UserController();
    private String role;
    private int branchCode;

    public UserTableView(int branchCode,String role) {
        this.role = role;
        this.branchCode = branchCode;
        String readableRole = getReadableRole(role);
        setTitle("Manage " + readableRole + "s");
        setSize(900, 600);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout());

        // Header with Image
        JPanel headerPanel = new JPanel();
        headerPanel.setLayout(new BorderLayout());
        headerPanel.setBackground(new Color(70, 130, 180)); // Previous steel blue background

        // Full-Width Image
        ImageIcon headerIcon = new ImageIcon("src\\imgs\\Logo_METRO.svg.png"); // Replace with the path to your image
        Image scaledImage = headerIcon.getImage().getScaledInstance(getWidth(), 100, Image.SCALE_SMOOTH); // Scaled to
        // full width
        JLabel imageLabel = new JLabel(new ImageIcon(scaledImage));
        imageLabel.setHorizontalAlignment(SwingConstants.CENTER); // Center the image within the panel

        headerPanel.add(imageLabel, BorderLayout.CENTER);

        // Title Label
        JLabel titleLabel = new JLabel("Manage " + readableRole + "s", SwingConstants.CENTER);
        titleLabel.setFont(new Font("SansSerif", Font.BOLD, 24));
        titleLabel.setForeground(Color.WHITE);

        // Set the image to fill the top area of the header
        imageLabel.setHorizontalAlignment(SwingConstants.CENTER); // Align image to the center

        // Add components to the header
        headerPanel.add(imageLabel, BorderLayout.NORTH);
        headerPanel.add(titleLabel, BorderLayout.SOUTH);

        // Side Panel (Button Panel)
        JPanel sidePanel = new JPanel();
        sidePanel.setLayout(new GridLayout(4, 1, 10, 10)); // Adjusts button layout
        sidePanel.setBackground(new Color(70, 130, 160)); // Previous steel blue background
        sidePanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        addButton = new RoundedButton("Add New " + readableRole);
        updateButton = new RoundedButton("Update " + readableRole);
        deleteButton = new RoundedButton("Delete " + readableRole);
        viewButton = new RoundedButton("View All " + readableRole + "s");

        RoundedButton[] buttons = { addButton, updateButton, deleteButton, viewButton };
        setButtonProperties(buttons);

        for (RoundedButton button : buttons) {
            sidePanel.add(button);
        }

        // Table
        tableModel = new DefaultTableModel(
                new String[] { "Name", "Email", "BranchCode", "Salary", "EmpNumber", "AppointedOn", "UpdatedAt" },
                0);
        userTable = new JTable(tableModel);
        userTable.setFillsViewportHeight(true);
        userTable.setRowHeight(25);

        JTableHeader tableHeader = userTable.getTableHeader();
        tableHeader.setFont(new Font("SansSerif", Font.BOLD, 20));

        // Set the background and foreground of the table header to ensure they retain
        // their color
        tableHeader.setBackground(new Color(25, 25, 130)); // Set header background color (dark blue)
        tableHeader.setForeground(new Color(25, 25, 130)); // Set header text color to white
        userTable.setTableHeader(tableHeader); // Apply header customization

        JScrollPane scrollPane = new JScrollPane(userTable);

        // Add components to the frame
        add(headerPanel, BorderLayout.NORTH);
        add(sidePanel, BorderLayout.WEST);
        add(scrollPane, BorderLayout.CENTER);

        // Event Listeners
        addButton.addActionListener(e -> openAddUserScreen(role));
        updateButton.addActionListener(e -> openUpdateUserScreen(role));
        deleteButton.addActionListener(e -> deleteSelectedUser());
        viewButton.addActionListener(e -> loadUserData(branchCode,role));

        setLocationRelativeTo(null);
        setVisible(true);
    }

    private void setButtonProperties(RoundedButton[] buttons) {
        for (RoundedButton button : buttons) {
            button.setFont(new Font("SansSerif", Font.BOLD, 14));
            button.setBackground(new Color(70, 130, 180)); // Steel blue

            button.setForeground(Color.WHITE);
            button.setFocusPainted(false);
            // Adjust button height
            button.setPreferredSize(
                    new Dimension(button.getPreferredSize().width, button.getPreferredSize().height - 28));
        }
    }

    public void loadUserData(int branchCode,String role) {
        tableModel.setRowCount(0);
        userController.loadUserDataToView(userTable, branchCode,role);
    }

    public String getRole() {
        return this.role;
    }
    public int getBranchCode() {
        return this.branchCode;
    }


    public void setUserController(UserController userController) {
        this.userController = userController;
    }

    private void openAddUserScreen(String role) {
        try {
            new AddUserView(this);
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error opening Add Branch Manager screen: " + e.getMessage());
        }
    }

    private void openUpdateUserScreen(String role) {
        int selectedRow = userTable.getSelectedRow();
        if (selectedRow != -1) {
            String email = (String) userTable.getValueAt(selectedRow, 1);
            try {
                new UpdateUserView(this, userController, email, role, this::refreshTable);
            } catch (Exception e) {
                e.printStackTrace();
                JOptionPane.showMessageDialog(this, "Error opening Update User screen: " + e.getMessage());
            }
        } else {
            JOptionPane.showMessageDialog(this, "Please select a user to update.");
        }
    }

    private void deleteSelectedUser() {
        int selectedRow = userTable.getSelectedRow();
        if (selectedRow >= 0) {
            String email = (String) userTable.getValueAt(selectedRow, 1);
            Object empNumberObject = userTable.getValueAt(selectedRow, 4);
            int empNumber = 0;

            if (empNumberObject instanceof Number) {
                empNumber = ((Number) empNumberObject).intValue();
            } else {
                JOptionPane.showMessageDialog(this, "Invalid EmpNumber format.");
                return;
            }

            int confirmation = JOptionPane.showConfirmDialog(this,
                    "Are you sure you want to delete the user with\n EmpNumber:" + empNumber + ", Name: "
                            + userTable.getValueAt(selectedRow, 0) + "?",
                    "Confirm Deletion",
                    JOptionPane.YES_NO_OPTION,
                    JOptionPane.WARNING_MESSAGE);

            if (confirmation == JOptionPane.YES_OPTION) {
                try {
                    String resultMessage = userController.deleteUserByEmailAndEmpNumber(email, empNumber);

                    if (resultMessage.equals("User deleted successfully.")) {
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
                JOptionPane.showMessageDialog(this, "Deletion cancelled.");
            }
        } else {
            JOptionPane.showMessageDialog(this, "Please select a user to delete.");
        }
    }

    private void filterTable(String searchText) {
        TableRowSorter<DefaultTableModel> sorter = (TableRowSorter<DefaultTableModel>) userTable.getRowSorter();
        if (sorter == null) {
            sorter = new TableRowSorter<>(tableModel);
            userTable.setRowSorter(sorter);
        }
        sorter.setRowFilter(RowFilter.regexFilter("(?i)" + searchText));
    }

    private void refreshTable() {
        loadUserData(branchCode,role);
    }

    private String getReadableRole(String role) {
        String[] parts = role.split("(?<!^)(?=[A-Z])");
        StringBuilder readableRole = new StringBuilder();
        for (String part : parts) {
            readableRole.append(part.substring(0, 1).toUpperCase()).append(part.substring(1).toLowerCase()).append(" ");
        }
        return readableRole.toString().trim();
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new UserTableView(101,"Cashier"));
    }
}
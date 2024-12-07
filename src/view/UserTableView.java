package view;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableRowSorter;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

import model.UserModel;
import controller.UserController;
import view.UpdateUserView;

public class UserTableView extends JFrame {
    private JButton addButton;
    private JButton updateButton;
    private JButton deleteButton;
    private JButton viewButton;
    private JTable userTable;
    private DefaultTableModel tableModel;
    private UserController userController = new UserController();
    private JTextField searchField;
    private String role;

    public UserTableView(String role) {
        this.role = role;
        String readableRole = getReadableRole(role);
        setTitle("Manage " + readableRole + "s");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout());

        JLabel titleLabel = new JLabel("Manage " + readableRole + "s", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Serif", Font.BOLD, 20));

        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));

        JPanel searchPanel = new JPanel();
        searchPanel.setLayout(new FlowLayout());

        JLabel searchLabel = new JLabel("Search:");
        searchField = new JTextField(20);
        searchField.setToolTipText("Search " + role + "s...");
        searchField.addKeyListener(new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent e) {
                filterTable(searchField.getText());
            }
        });

        searchPanel.add(searchLabel);
        searchPanel.add(searchField);

        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new FlowLayout());

        addButton = new JButton("Add New " + readableRole);
        updateButton = new JButton("Update " + readableRole);
        deleteButton = new JButton("Delete " + readableRole);
        viewButton = new JButton("View All " + readableRole + "s");

        buttonPanel.add(addButton);
        buttonPanel.add(updateButton);
        buttonPanel.add(deleteButton);
        buttonPanel.add(viewButton);

        mainPanel.add(searchPanel);
        mainPanel.add(buttonPanel);

        tableModel = new DefaultTableModel(
                new String[] { "Name", "Email", "BranchCode", "Salary", "EmpNumber", "AppointedOn", "UpdatedAt" },
                0);
        userTable = new JTable(tableModel);
        JScrollPane scrollPane = new JScrollPane(userTable);

        add(titleLabel, BorderLayout.NORTH);
        add(mainPanel, BorderLayout.CENTER);
        add(scrollPane, BorderLayout.SOUTH);

        addButton.addActionListener(e -> openAddUserScreen(role));
        updateButton.addActionListener(e -> openUpdateUserScreen(role));
        deleteButton.addActionListener(e -> deleteSelectedUser());
        viewButton.addActionListener(e -> loadUserData(role));

        setLocationRelativeTo(null);
        setVisible(true);
    }

    public void addUserToTable(UserModel user) {
        tableModel.addRow(new Object[] {
                user.getUserID(),
                user.getName(),
                user.getEmail(),
                user.getBranchCode(),
                user.getSalary(),
                user.getEmpNumber(),
                user.getCreatedAt(),
                user.getUpdatedAt()
        });
    }

    public String getRole() {
        return this.role;
    }

    public void setUserController(UserController userController) {
        this.userController = userController;
    }

    public void loadUserData(String role) {
        tableModel.setRowCount(0);
        userController.loadUserDataToView(userTable, role);
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
                    "Are you sure you want to delete the user with\n EmpNumber:" + empNumber + ", Name: " + userTable.getValueAt(selectedRow, 0) + "?",
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
        loadUserData(role);
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
        SwingUtilities.invokeLater(() -> {
            try {
                new UserTableView("DataEntryOperator");
            } catch (Exception e) {
                JOptionPane.showMessageDialog(null, "Error initializing the application: " + e.getMessage(),
                        "Application Error", JOptionPane.ERROR_MESSAGE);
            }
        });
    }
}

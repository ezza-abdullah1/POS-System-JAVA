package SuperAdmin.view;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
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
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout());

        JLabel titleLabel = new JLabel("Manage Branch Managers", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Serif", Font.BOLD, 20));

        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));

        JPanel searchPanel = new JPanel();
        searchPanel.setLayout(new FlowLayout());

        JLabel searchLabel = new JLabel("Search:");
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

        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new FlowLayout());

        addButton = new RoundedButton("Add New Branch Manager");
        updateButton = new RoundedButton("Update Branch Manager");
        deleteButton = new RoundedButton("Delete Branch Manager");
        viewButton = new RoundedButton("View All Branch Managers");
        RoundedButton[] buttons = { addButton, updateButton, deleteButton, viewButton };

        Dimension buttonSize = new Dimension(190, 30);
        Font buttonFont = new Font("Serif", Font.BOLD, 12);
        setButtonProperties(buttons, buttonSize, buttonFont);

        buttonPanel.add(addButton);
        buttonPanel.add(updateButton);
        buttonPanel.add(deleteButton);
        buttonPanel.add(viewButton);

        mainPanel.add(searchPanel);
        mainPanel.add(buttonPanel);

        tableModel = new DefaultTableModel(
                new String[] { "Name", "Email", "BranchCode", "Salary", "EmpNumber", "AppointedOn", "UpdatedAt" }, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // This makes all cells in the table uneditable
            }
        };
        branchManagerTable = new JTable(tableModel);
        JScrollPane scrollPane = new JScrollPane(branchManagerTable);
        add(titleLabel, BorderLayout.NORTH);
        add(mainPanel, BorderLayout.CENTER);
        add(scrollPane, BorderLayout.SOUTH);

        addButton.addActionListener(e -> openAddBranchManagerScreen());
        updateButton.addActionListener(e -> openUpdateBranchManagerScreen());
        deleteButton.addActionListener(e -> deleteSelectedBranchManager());
        viewButton.addActionListener(e -> loadBranchManagerData());

        setLocationRelativeTo(null);
        setVisible(true);
    }

    private void setButtonProperties(RoundedButton[] buttons, Dimension size, Font font) {
        for (RoundedButton button : buttons) {
            button.setPreferredSize(size);
            button.setFont(font);
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

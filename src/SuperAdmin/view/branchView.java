package SuperAdmin.view;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableRowSorter;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

import SuperAdmin.model.BranchModel;
import utils.RoundedButton;
import SuperAdmin.controller.BranchController;

public class branchView extends JFrame {
    private RoundedButton addButton;
    private RoundedButton updateButton;
    private RoundedButton deleteButton;
    private RoundedButton viewButton;
    private RoundedButton activateButton;
    private JTable branchTable;
    private DefaultTableModel tableModel;
    private BranchController branchController = new BranchController();
    private JTextField searchField;

    public branchView() {
        setTitle("Branch Management");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout());

        JLabel titleLabel = new JLabel("Branch Management", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Serif", Font.BOLD, 20));

        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));

        JPanel searchPanel = new JPanel();
        searchPanel.setLayout(new FlowLayout());

        JLabel searchLabel = new JLabel("Search:");
        searchField = new JTextField(20);
        searchField.setToolTipText("Search customers...");
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

        addButton = new RoundedButton("Add New Branch");
        updateButton = new RoundedButton("Update Branch");
        activateButton = new RoundedButton("Activate Branch");
        deleteButton = new RoundedButton("Delete Branch");
        viewButton = new RoundedButton("View All Branches");
        RoundedButton[] buttons = { addButton, updateButton, activateButton, deleteButton, viewButton };

        Dimension buttonSize = new Dimension(150, 30);
        Font buttonFont = new Font("Serif", Font.BOLD, 12);
        setButtonProperties(buttons, buttonSize, buttonFont);

        buttonPanel.add(addButton);
        buttonPanel.add(updateButton);
        buttonPanel.add(activateButton);
        buttonPanel.add(deleteButton);
        buttonPanel.add(viewButton);

        mainPanel.add(searchPanel);
        mainPanel.add(buttonPanel);

        tableModel = new DefaultTableModel(
                new String[] { "BranchCode", "BranchName", "City", "Address", "Phone", "NumEmployees", "IsActive" }, 0);
        branchTable = new JTable(tableModel);
        JScrollPane scrollPane = new JScrollPane(branchTable);

        add(titleLabel, BorderLayout.NORTH);
        add(mainPanel, BorderLayout.CENTER);
        add(scrollPane, BorderLayout.SOUTH);

        branchTable.getSelectionModel().addListSelectionListener(e -> updateActivateButtonText());
        addButton.addActionListener(e -> openAddbranchScreen());
        updateButton.addActionListener(e -> openUpdateBranchScreen());
        deleteButton.addActionListener(e -> deleteSelectedBranch());
        viewButton.addActionListener(e -> loadBranchData());
        activateButton.addActionListener(e -> activateBranch());

        setLocationRelativeTo(null);
        setVisible(true);
    }

    private void setButtonProperties(RoundedButton[] buttons, Dimension size, Font font) {
        for (RoundedButton button : buttons) {
            button.setPreferredSize(size);
            button.setFont(font);
        }
    }

    public void addbranchToTable(BranchModel branch) {
        tableModel.addRow(new Object[] {
                branch.getBranchCode(),
                branch.getBranchName(),
                branch.getCity(),
                branch.getAddress(),
                branch.getPhone(),
                branch.getNumEmployees(),
                branch.isActive(),
        });
    }

    public void setCustomerController(BranchController branchController) {
        this.branchController = branchController;
    }

    public void loadBranchData() {
        tableModel.setRowCount(0);

        branchController.loadbranchDataToView(this);
    }

    private void openAddbranchScreen() {
        addBranchView nbv = new addBranchView(this);
        // nbv.setCustomerController(branchController);
    }

    private void openUpdateBranchScreen() {
        int selectedRow = branchTable.getSelectedRow();
        if (selectedRow != -1) {
            Object statusObj = tableModel.getValueAt(selectedRow, 6);

            boolean isActive = false;
            if (statusObj instanceof Boolean) {
                isActive = (Boolean) statusObj;
            } else if (statusObj instanceof String) {
                isActive = Boolean.parseBoolean((String) statusObj);
            }

            if (!isActive) {
                JOptionPane.showMessageDialog(this,
                        "Cannot update an inactive branch. Please activate the branch first.",
                        "Error",
                        JOptionPane.ERROR_MESSAGE);
                return; // Prevent further execution
            }

            String branchcode = (String) branchTable.getValueAt(selectedRow, 0);
            new updateBranchView(this, branchController, branchcode, this::refreshTable);
        } else {
            JOptionPane.showMessageDialog(this, "Please select a branch to update.");
        }
    }

    public void refreshTable() {
        loadBranchData();
    }

    private void updateActivateButtonText() {
        int selectedRow = branchTable.getSelectedRow();

        if (selectedRow >= 0) {
            Object statusObj = tableModel.getValueAt(selectedRow, 6);

            boolean isActive = false;
            if (statusObj instanceof Boolean) {
                isActive = (Boolean) statusObj;
            } else if (statusObj instanceof String) {
                isActive = Boolean.parseBoolean((String) statusObj);
            }

            if (isActive) {
                activateButton.setText("Deactivate Branch");
            } else {
                activateButton.setText("Activate Branch");
            }
        }
    }

    private void activateBranch() {
        int selectedRow = branchTable.getSelectedRow();

        if (selectedRow >= 0) {
            String branchCodeStr = (String) tableModel.getValueAt(selectedRow, 0);
            int branchCode = Integer.parseInt(branchCodeStr);

            Object statusObj = tableModel.getValueAt(selectedRow, 6);

            boolean isActive = false;
            if (statusObj instanceof Boolean) {
                isActive = (Boolean) statusObj;
            } else if (statusObj instanceof String) {
                isActive = Boolean.parseBoolean((String) statusObj);
            }

            String action = isActive ? "deactivate" : "activate";
            int confirmation = JOptionPane.showConfirmDialog(
                    this,
                    "Are you sure you want to " + action + " this branch?",
                    "Confirm " + action.substring(0, 1).toUpperCase() + action.substring(1),
                    JOptionPane.YES_NO_OPTION,
                    JOptionPane.WARNING_MESSAGE);

            if (confirmation == JOptionPane.YES_OPTION) {
                boolean newStatus = !isActive;

                try {
                    String resultMessage = branchController.updateBranchStatus(branchCode, newStatus);

                    if (resultMessage.equals("Branch status updated successfully.")) {
                        tableModel.setValueAt(newStatus, selectedRow, 6);

                        if (newStatus) {
                            activateButton.setText("Deactivate Branch");
                        } else {
                            activateButton.setText("Activate Branch");
                        }

                        JOptionPane.showMessageDialog(this, resultMessage);
                    } else {
                        JOptionPane.showMessageDialog(this, resultMessage, "Error", JOptionPane.ERROR_MESSAGE);
                    }

                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage(), "Error",
                            JOptionPane.ERROR_MESSAGE);
                }
            } else {
                JOptionPane.showMessageDialog(this, "Action canceled.");
            }
        } else {
            JOptionPane.showMessageDialog(this, "Please select a branch to change its activation status.");
        }
    }

    private void deleteSelectedBranch() {
        int selectedRow = branchTable.getSelectedRow();
        if (selectedRow >= 0) {
            String branchCodeStr = (String) tableModel.getValueAt(selectedRow, 0);
            int branchCode = Integer.parseInt(branchCodeStr);

            int confirmation = JOptionPane.showConfirmDialog(
                    this,
                    "Are you sure you want to delete this branch?",
                    "Confirm Deletion",
                    JOptionPane.YES_NO_OPTION,
                    JOptionPane.WARNING_MESSAGE);

            if (confirmation == JOptionPane.YES_OPTION) {
                try {
                    String resultMessage = branchController.deleteBranch(branchCode);

                    if (resultMessage.equals("Branch deleted successfully.")) {
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
            JOptionPane.showMessageDialog(this, "Please select a branch to delete.");
        }
    }

    private void filterTable(String searchText) {
        TableRowSorter<DefaultTableModel> sorter = new TableRowSorter<>(tableModel);
        branchTable.setRowSorter(sorter);

        sorter.setRowFilter(RowFilter.regexFilter("(?i)" + searchText));
    }

    public static void main(String[] args) {

        SwingUtilities.invokeLater(branchView::new);
    }
}

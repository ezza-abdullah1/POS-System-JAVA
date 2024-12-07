package SuperAdmin.view;

import SuperAdmin.controller.UserController;
import SuperAdmin.model.UserModel;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class UpdateBranchManagerView extends JFrame {
    private JTextField nameField;
    private JTextField emailField;
    private JTextField branchCodeField;
    private JTextField empNumberField;
    private JTextField salaryField;
    private JButton updateButton;
    private int branchCode;
    private UserController branchManagerController;
    private BranchManagerView parent;
    private Runnable refreshTable;

    public UpdateBranchManagerView(BranchManagerView parent, UserController controller, int branchCode,
            Runnable refreshTable) {
        this.parent = parent;
        this.branchCode = branchCode;
        this.branchManagerController = controller;
        this.refreshTable = refreshTable;
        initializeUI();
        populateFields();
    }

    private void initializeUI() {
        setTitle("Update Branch Manager");
        setLayout(new GridLayout(6, 2)); // Adjusted grid layout to 6 rows

        JLabel nameLabel = new JLabel("Name:");
        nameField = new JTextField();
        JLabel emailLabel = new JLabel("Email:");
        emailField = new JTextField();
        JLabel branchCodeLabel = new JLabel("Branch Code:");
        branchCodeField = new JTextField(String.valueOf(branchCode));
        branchCodeField.setEditable(false); // Branch Code is fixed
        JLabel empNumberLabel = new JLabel("Employee Number:");
        empNumberField = new JTextField();
        JLabel salaryLabel = new JLabel("Salary:");
        salaryField = new JTextField();

        updateButton = new JButton("Update");
        updateButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                updateBranchManager();
            }
        });

        add(nameLabel);
        add(nameField);
        add(emailLabel);
        add(emailField);
        add(branchCodeLabel);
        add(branchCodeField);
        add(empNumberLabel);
        add(empNumberField);
        add(salaryLabel);
        add(salaryField);
        add(new JLabel()); // Empty label for proper grid layout
        add(updateButton);

        setSize(400, 300);
        setLocationRelativeTo(parent);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setVisible(true);
    }

    private void populateFields() {
        String[] managerData = branchManagerController.getBranchManagerByBranchCode(branchCode);

        nameField.setText(managerData[1]);
        emailField.setText(managerData[2]);
        empNumberField.setText(managerData[6]);
        salaryField.setText(managerData[7]);
    }

    private void updateBranchManager() {
        String name = nameField.getText();
        String email = emailField.getText();
        String empNumberStr = empNumberField.getText();
        String salaryStr = salaryField.getText();
        // Validation
        if (name.isEmpty() || email.isEmpty() || empNumberStr.isEmpty() || salaryStr.isEmpty()) {
            JOptionPane.showMessageDialog(this, "All fields are required.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        if (!email.matches("^[^@\\s]+@[^@\\s]+\\.[^@\\s]+$")) {
            JOptionPane.showMessageDialog(this, "Invalid email format.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        double salaryValue;
        try {
            salaryValue = Double.parseDouble(salaryStr);
            if (salaryValue <= 0) {
                JOptionPane.showMessageDialog(this, "Salary must be a positive number.", "Error",
                        JOptionPane.ERROR_MESSAGE);
                return;
            }
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Salary must be a valid number.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        int empNumberInt;

        try {
            empNumberInt = Integer.parseInt(empNumberStr);

            int confirmation = JOptionPane.showConfirmDialog(this,
                    "Are you sure you want to update this branch manager?", "Confirm Update",
                    JOptionPane.YES_NO_OPTION);

            if (confirmation == JOptionPane.YES_OPTION) {
                UserModel updatedBranchManager = new UserModel();

                // Set the individual fields
                updatedBranchManager.setBranchCode(branchCode);
                updatedBranchManager.setName(name);
                updatedBranchManager.setEmail(email);
                updatedBranchManager.setEmpNumber(empNumberInt);
                updatedBranchManager.setSalary(salaryValue);
                updatedBranchManager.setRole("BranchManager");
                // Call the update method in the UserModel
                branchManagerController.updateBranchManager(updatedBranchManager);

                refreshTable.run(); // Refresh table data
                dispose();
            }
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Employee Number must be a valid number.", "Error",
                    JOptionPane.ERROR_MESSAGE);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}

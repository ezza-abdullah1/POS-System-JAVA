package view;

import controller.UserController;
import model.UserModel;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class UpdateUserView extends JFrame {
    private JTextField nameField;
    private JTextField emailField;
    private JTextField branchCodeField;
    private JTextField passwordField;
    private JTextField salaryField;
    private JButton updateButton;
    private String email;
    private String role;
    private UserController userController;
    private Runnable refreshTable;
    private UserTableView parent;

    public UpdateUserView(UserTableView parent, UserController controller, String email, String role, Runnable refreshTable) {
        this.parent = parent;
        this.role = role;
        this.email = email;
        this.userController = controller;
        this.refreshTable = refreshTable;
        initializeUI();
        populateFields();
    }

    private void initializeUI() {
        setTitle("Update User");
        setLayout(new GridLayout(6, 2));

        JLabel nameLabel = new JLabel("Name:");
        nameField = new JTextField();
        JLabel emailLabel = new JLabel("Email:");
        emailField = new JTextField();
        JLabel passwordLabel = new JLabel("Password:");
        passwordField = new JTextField();
        JLabel branchCodeLabel = new JLabel("Branch Code:");
        branchCodeField = new JTextField();
        branchCodeField.setEditable(false);
        JLabel salaryLabel = new JLabel("Salary:");
        salaryField = new JTextField();

        updateButton = new JButton("Update");
        updateButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                updateUser();
            }
        });

        add(nameLabel);
        add(nameField);
        add(emailLabel);
        add(emailField);
        add(branchCodeLabel);
        add(branchCodeField);
        add(passwordLabel);
        add(passwordField);
        add(salaryLabel);
        add(salaryField);
        add(new JLabel());
        add(updateButton);

        setSize(400, 300);
        setLocationRelativeTo(parent);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setVisible(true);
    }

    private void populateFields() {
        String[] userData = userController.getUserByEmailAndRole(email, role);

        nameField.setText(userData[1]);
        emailField.setText(userData[2]);
        passwordField.setText(userData[3]);
        branchCodeField.setText(userData[5]);
        salaryField.setText(userData[6]);
    }

    private void updateUser() {
        String name = nameField.getText();
        String email = emailField.getText();
        String empNumberStr = passwordField.getText();
        String salaryStr = salaryField.getText();

        try {
            if (name.isEmpty() || email.isEmpty() || empNumberStr.isEmpty() || salaryStr.isEmpty()) {
                JOptionPane.showMessageDialog(this, "All fields must be filled out.", "Validation Error",
                        JOptionPane.ERROR_MESSAGE);
                return;
            }

            int empNumber = Integer.parseInt(empNumberStr);
            double salary = Double.parseDouble(salaryStr);

            UserModel updatedUser = new UserModel();
            updatedUser.setName(name);
            updatedUser.setEmail(email);
            updatedUser.setBranchCode(Integer.parseInt(branchCodeField.getText()));
            updatedUser.setEmpNumber(empNumber);
            updatedUser.setSalary(salary);

            userController.updateUser(updatedUser);
            refreshTable.run();
            dispose();
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Invalid number format for Employee Number or Salary.",
                    "Validation Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}

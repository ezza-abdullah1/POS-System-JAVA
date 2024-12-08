package SuperAdmin.view;

import SuperAdmin.controller.BranchController;
import SuperAdmin.controller.UserController;
import SuperAdmin.model.UserModel;
import utils.RoundedButton;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

public class AddBranchManagerView extends JFrame {
    private JComboBox<String> branchComboBox;
    private JTextField nameField;
    private JTextField emailField;
    private JTextField salaryField;
    private JTextField empNumberField;
    private RoundedButton addButton;
    private List<Integer> activeBranchCodes;
    private BranchManagerView parent;

    public AddBranchManagerView(BranchManagerView parent) {
        this.parent = parent;
        setTitle("Add Branch Manager");
        setSize(600, 500); // Increased window size
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new GridBagLayout()); // Using GridBagLayout for more control over the layout

        // Initialize the activeBranchCodes list
        activeBranchCodes = new ArrayList<>();
        // Create constraints for the GridBagLayout
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10); // Add padding between components
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Initialize components
        branchComboBox = new JComboBox<>();
        nameField = new JTextField(20);
        emailField = new JTextField(20);
        salaryField = new JTextField(20);
        empNumberField = new JTextField(20);
        addButton = new RoundedButton("Add Branch Manager");

        // Customize button and fields with a better font
        Font font = new Font("Arial", Font.PLAIN, 14);
        addButton.setFont(new Font("Arial", Font.BOLD, 16));
        nameField.setFont(font);
        emailField.setFont(font);
        salaryField.setFont(font);
        empNumberField.setFont(font);
        addButton.setBackground(new Color(76, 175, 80)); // Green background
        addButton.setForeground(Color.WHITE);
        addButton.setPreferredSize(new Dimension(140, 40));
        // Add components to the frame with constraints
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        add(new JLabel("Add Branch Manager", JLabel.CENTER), gbc);

        gbc.gridwidth = 1;
        gbc.gridy++;
        add(new JLabel("Select Branch:"), gbc);

        gbc.gridx = 1;
        add(branchComboBox, gbc);

        gbc.gridx = 0;
        gbc.gridy++;
        add(new JLabel("Name:"), gbc);

        gbc.gridx = 1;
        add(nameField, gbc);

        gbc.gridx = 0;
        gbc.gridy++;
        add(new JLabel("Email:"), gbc);

        gbc.gridx = 1;
        add(emailField, gbc);

        gbc.gridx = 0;
        gbc.gridy++;
        add(new JLabel("Salary:"), gbc);

        gbc.gridx = 1;
        add(salaryField, gbc);

        gbc.gridx = 0;
        gbc.gridy++;
        add(new JLabel("Employee Number:"), gbc);

        gbc.gridx = 1;
        add(empNumberField, gbc);

        gbc.gridx = 1;
        gbc.gridy++;
        gbc.gridwidth = 2;
        add(addButton, gbc);
        setVisible(true);

        // Call the controller to load active branches into the JComboBox
        BranchController userController = new BranchController();
        userController.loadActiveBranchesToView(this);

        // Action listener for the Add button
        addButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                addBranchManager();
            }
        });

    }

    // Method to add branch to combo box
    public void addBranchToComboBox(String branch, int branchCode) {
        branchComboBox.addItem(branch);
        activeBranchCodes.add(branchCode);
    }

    private void addBranchManager() {
        String name = nameField.getText().trim();
        String email = emailField.getText().trim();
        String salaryText = salaryField.getText().trim();
        String empNumberText = empNumberField.getText().trim();

        if (name.isEmpty() || email.isEmpty() || salaryText.isEmpty() || empNumberText.isEmpty()) {
            JOptionPane.showMessageDialog(this, "All fields must be filled out.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        if (!email.matches("^[A-Za-z0-9+_.-]+@(.+)$")) {
            JOptionPane.showMessageDialog(this, "Invalid email format.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        try {
            double salary = Double.parseDouble(salaryText);
            if (salary <= 0) {
                JOptionPane.showMessageDialog(this, "Salary must be a positive number.", "Error",
                        JOptionPane.ERROR_MESSAGE);
                return;
            }

            int empNumber = Integer.parseInt(empNumberText);
            if (empNumber <= 0) {
                JOptionPane.showMessageDialog(this, "Employee number must be a positive integer.", "Error",
                        JOptionPane.ERROR_MESSAGE);
                return;
            }

            int selectedBranchCode = activeBranchCodes.get(branchComboBox.getSelectedIndex());

            int confirm = JOptionPane.showConfirmDialog(this,
                    "Are you sure you want to add this Branch Manager?\n\n"
                            + "Name: " + name + "\n"
                            + "Email: " + email + "\n"
                            + "Salary: " + salary + "\n"
                            + "Employee Number: " + empNumber + "\n"
                            + "Branch: " + branchComboBox.getSelectedItem(),
                    "Confirm Addition", JOptionPane.YES_NO_OPTION);

            if (confirm == JOptionPane.YES_OPTION) {
                UserModel newBranchManager = new UserModel();
                newBranchManager.setName(name);
                newBranchManager.setEmail(email);
                newBranchManager.setPassword("123"); // Default password
                newBranchManager.setRole("BranchManager");
                newBranchManager.setBranchCode(selectedBranchCode);
                newBranchManager.setSalary(salary);
                newBranchManager.setEmpNumber(empNumber);

                // Call the controller
                UserController userController = new UserController();
                userController.addBranchManager(newBranchManager);
                parent.loadBranchManagerData(); // Refresh data
                dispose();
            }
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Invalid salary or employee number.", "Error",
                    JOptionPane.ERROR_MESSAGE);
        } catch (IndexOutOfBoundsException ex) {
            JOptionPane.showMessageDialog(this, "Please select a valid branch.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                // new AddBranchManagerView().setVisible(true);
            }
        });
    }
}

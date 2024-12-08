package SuperAdmin.view;

import SuperAdmin.controller.BranchController;
import utils.RoundedButton;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class addBranchView extends JDialog {
    private JTextField branchCode;
    private JTextField nameField;
    private JTextField cityField;
    private JTextField addressField;
    private JTextField phoneField;
    private RoundedButton saveButton;
    private branchView parent;

    public addBranchView(branchView parent) {
        super(parent, "Add New Branch", true);
        this.parent = parent;

        setLayout(new GridLayout(7, 2)); // Updated layout to account for 7 fields
        setSize(400, 300); // Adjusted size to fit 7 fields

        add(new JLabel("Branch Code:"));
        branchCode = new JTextField();
        add(branchCode);

        add(new JLabel("Branch Name:"));
        nameField = new JTextField();
        add(nameField);

        add(new JLabel("City:"));
        cityField = new JTextField();
        add(cityField);

        add(new JLabel("Address:"));
        addressField = new JTextField();
        add(addressField);

        add(new JLabel("Phone Number:"));
        phoneField = new JTextField();
        add(phoneField);

        // Removed numEmployeesField and isActiveField

        saveButton = new RoundedButton("Save Branch");
        saveButton.setPreferredSize(new Dimension(140, 40));

        saveButton.addActionListener(new SaveButtonListener());
        add(saveButton);

        setLocationRelativeTo(parent);
        setVisible(true);
    }

    private class SaveButtonListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            String code = branchCode.getText();
            String name = nameField.getText();
            String city = cityField.getText();
            String address = addressField.getText();
            String phone = phoneField.getText();

            // Perform validation checks
            if (code.isEmpty()) {
                JOptionPane.showMessageDialog(parent, "Branch Code cannot be empty.", "Error",
                        JOptionPane.ERROR_MESSAGE);
                return;
            }

            if (name.isEmpty()) {
                JOptionPane.showMessageDialog(parent, "Branch Name cannot be empty.", "Error",
                        JOptionPane.ERROR_MESSAGE);
                return;
            }

            if (city.isEmpty()) {
                JOptionPane.showMessageDialog(parent, "City cannot be empty.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            if (address.isEmpty()) {
                JOptionPane.showMessageDialog(parent, "Address cannot be empty.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            if (phone.isEmpty()) {
                JOptionPane.showMessageDialog(parent, "Phone Number cannot be empty.", "Error",
                        JOptionPane.ERROR_MESSAGE);
                return;
            }

            // Validate phone number format
            if (!phone.matches("\\d{10,15}")) {
                JOptionPane.showMessageDialog(parent, "Invalid phone number. It should be between 10 to 15 digits.",
                        "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            try {
                BranchController branchController = new BranchController();

                // Pass the values to the controller and set numEmployees to 0, isActive to true
                String resultMessage = branchController.saveBranch(code, name, city, address, phone, 0, true);

                if (resultMessage.startsWith("Branch saved successfully:")) {
                    parent.loadBranchData(); // Refresh data
                    dispose();
                    JOptionPane.showMessageDialog(parent, resultMessage);
                } else {
                    // Show the error message if it's not a success
                    JOptionPane.showMessageDialog(parent, resultMessage, "Error", JOptionPane.ERROR_MESSAGE);
                }

            } catch (Exception ex) {
                // Show error dialog with exception message
                JOptionPane.showMessageDialog(parent, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
}

package SuperAdmin.view;

import SuperAdmin.controller.BranchController;
import utils.RoundedButton;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class updateBranchView extends JFrame {
    private RoundedButton updateButton;
    private JTextField nameField;
    private JTextField addressField;
    private JTextField cityField;
    private JTextField phoneField;
    private JComboBox<String> activeField;
    private JSpinner numEmpSpinner;
    private String branchcode;
    private BranchController branchController;
    private branchView parent;
    private Runnable refreshTable;

    public updateBranchView(branchView parent, BranchController controller, String branchcode,
            Runnable refreshTable) {
        this.parent = parent;
        this.branchcode = branchcode;
        this.branchController = controller;
        this.refreshTable = refreshTable;
        initializeUI();
        populateFields();
    }

    private void initializeUI() {
        setTitle("Update Branch");
        setLayout(new GridLayout(8, 2));

        JLabel nameLabel = new JLabel("Name:");
        nameField = new JTextField();
        JLabel cityLabel = new JLabel("City:");
        cityField = new JTextField();
        JLabel addressLabel = new JLabel("Address:");
        addressField = new JTextField();
        JLabel phoneLabel = new JLabel("Phone:");
        phoneField = new JTextField();
        JLabel activeLabel = new JLabel("Is Active:");
        activeField = new JComboBox<>(new String[] { "", "true", "false" });
        JLabel numEmpLabel = new JLabel("Number of Employees:");

        // Using JSpinner for number of employees
        numEmpSpinner = new JSpinner(new SpinnerNumberModel(0, 0, Integer.MAX_VALUE, 1));

        updateButton = new RoundedButton("Update");
        updateButton.setPreferredSize(new Dimension(80, 15));

        updateButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                updateBranch();
            }
        });

        add(nameLabel);
        add(nameField);
        add(cityLabel);
        add(cityField);
        add(addressLabel);
        add(addressField);
        add(phoneLabel);
        add(phoneField);
        add(activeLabel);
        add(activeField);
        add(numEmpLabel);
        add(numEmpSpinner);
        add(new JLabel());
        add(updateButton);

        setSize(400, 300);
        setLocationRelativeTo(parent);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setVisible(true);
    }

    private void populateFields() {
        String[] branchData = branchController.getBranchByCode(branchcode);

        nameField.setText(branchData[1]);
        cityField.setText(branchData[2]);
        addressField.setText(branchData[3]);
        phoneField.setText(branchData[4]);
        numEmpSpinner.setValue(Integer.parseInt(branchData[5])); // Set spinner value
        activeField.setSelectedItem(branchData[6]);
    }

    private void updateBranch() {
        String name = nameField.getText();
        String city = cityField.getText();
        String address = addressField.getText();
        String phone = phoneField.getText();
        int numEmp = (int) numEmpSpinner.getValue(); // Retrieve value from spinner
        String item = (String) activeField.getSelectedItem();
        Boolean isactive = Boolean.parseBoolean(item);

        // Validation
        if (name.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Branch Name cannot be empty.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        if (city.isEmpty()) {
            JOptionPane.showMessageDialog(this, "City cannot be empty.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        if (address.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Address cannot be empty.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        if (phone.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Phone Number cannot be empty.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        if (!phone.matches("\\d{10,15}")) {
            JOptionPane.showMessageDialog(this, "Phone number must be between 10 to 15 digits.", "Error",
                    JOptionPane.ERROR_MESSAGE);
            return;
        }

        if (item.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please select the status (Active/Inactive).", "Error",
                    JOptionPane.ERROR_MESSAGE);
            return;
        }

        // Confirmation before updating
        int confirmation = JOptionPane.showConfirmDialog(this,
                "Are you sure you want to update this branch?", "Confirm Update", JOptionPane.YES_NO_OPTION);

        if (confirmation == JOptionPane.YES_OPTION) {
            try {
                branchController.updateBranch(branchcode, name, city, address, phone, numEmp, isactive);

                JOptionPane.showMessageDialog(this, "Branch updated successfully.");
                refreshTable.run(); // Refresh table data
                dispose();
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
}

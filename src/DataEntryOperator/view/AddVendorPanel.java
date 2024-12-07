package DataEntryOperator.view;

import java.awt.*;
import java.awt.event.ActionListener;
import javax.swing.*;

public class AddVendorPanel extends JFrame {
    private JTextField nameField, addressField, contactField;
    private JButton saveButton;

    public AddVendorPanel() {
        setTitle("Add New Vendor");
        setSize(400, 300);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new GridLayout(4, 2));

        // Vendor Name field
        add(new JLabel("Vendor Name:"));
        nameField = new JTextField();
        add(nameField);

        // Vendor Address field
        add(new JLabel("Vendor Address:"));
        addressField = new JTextField();
        add(addressField);

        // Contact No field
        add(new JLabel("Contact No:"));
        contactField = new JTextField();
        add(contactField);

        // Save Button
        saveButton = new JButton("Save");
        add(saveButton);
    }

    public String getVendorName() {
        return nameField.getText().trim();
    }

    public String getVendorAddress() {
        return addressField.getText().trim();
    }

    public String getContactNo() {
        return contactField.getText().trim();
    }

    public void addSaveListener(ActionListener listener) {
        saveButton.addActionListener(e -> {
            if (validateInputs()) {
                listener.actionPerformed(e);
            }
        });
    }

    // Validation logic
    private boolean validateInputs() {
        // Vendor Name validation: Cannot be empty
        if (getVendorName().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Vendor Name cannot be empty.", "Validation Error",
                    JOptionPane.ERROR_MESSAGE);
            return false;
        }

        // Vendor Address validation: Cannot be empty
        if (getVendorAddress().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Vendor Address cannot be empty.", "Validation Error",
                    JOptionPane.ERROR_MESSAGE);
            return false;
        }

        // Contact Number validation: Must be numeric and 10-15 digits long
        String contact = getContactNo();
        if (contact.isEmpty() || !contact.matches("\\d{10,15}")) {
            JOptionPane.showMessageDialog(this, "Contact Number must be numeric and 10 to 15 digits long.",
                    "Validation Error", JOptionPane.ERROR_MESSAGE);
            return false;
        }

        return true;
    }
}

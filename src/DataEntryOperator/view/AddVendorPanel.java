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

        add(new JLabel("Vendor Name:"));
        nameField = new JTextField();
        add(nameField);

        add(new JLabel("Vendor Address:"));
        addressField = new JTextField();
        add(addressField);

        add(new JLabel("Contact No:"));
        contactField = new JTextField();
        add(contactField);

        saveButton = new JButton("Save");
        add(saveButton);
    }

    public String getVendorName() {
        return nameField.getText();
    }

    public String getVendorAddress() {
        return addressField.getText();
    }

    public String getContactNo() {
        return contactField.getText();
    }

    public void addSaveListener(ActionListener listener) {
        saveButton.addActionListener(listener);
    }
}

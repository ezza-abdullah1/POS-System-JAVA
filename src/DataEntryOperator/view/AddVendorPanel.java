package DataEntryOperator.view;

import java.awt.*;
import java.awt.event.ActionListener;
import javax.swing.*;

public class AddVendorPanel extends JFrame {
    private JTextField nameField, addressField, contactField;
    private JButton saveButton;

    public AddVendorPanel() {
        // Apply Nimbus Look and Feel
        try {
            for (UIManager.LookAndFeelInfo info : UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        setTitle("Add New Vendor");
        setSize(1000, 800); // Standard size
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        // Create a panel with semi-transparent background
        JPanel backgroundPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                // Load the background image
                ImageIcon bgImage = new ImageIcon("src\\imgs\\Screenshot 2024-12-09 032241.jpg");
                Image image = bgImage.getImage();
                g.drawImage(image, 0, 0, getWidth(), getHeight(), this);

                // Add semi-transparent overlay
                Graphics2D g2d = (Graphics2D) g.create();
                g2d.setColor(new Color(255, 255, 255, 170)); // Semi-transparent white
                g2d.fillRect(0, 0, getWidth(), getHeight());
                g2d.dispose();
            }
        };
        backgroundPanel.setLayout(new GridBagLayout());
        add(backgroundPanel);

        // Create the form panel
        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setOpaque(false); // Transparent background
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(15, 15, 15, 15); // Increased spacing
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Vendor Name field
        gbc.gridx = 0;
        gbc.gridy = 0;
        formPanel.add(createLabel("Vendor Name:"), gbc);

        gbc.gridx = 1;
        nameField = createTextField();
        formPanel.add(nameField, gbc);

        // Vendor Address field
        gbc.gridx = 0;
        gbc.gridy = 1;
        formPanel.add(createLabel("Vendor Address:"), gbc);

        gbc.gridx = 1;
        addressField = createTextField();
        formPanel.add(addressField, gbc);

        // Contact No field
        gbc.gridx = 0;
        gbc.gridy = 2;
        formPanel.add(createLabel("Contact No:"), gbc);

        gbc.gridx = 1;
        contactField = createTextField();
        formPanel.add(contactField, gbc);

        // Save Button
        gbc.gridx = 1;
        gbc.gridy = 3;
        saveButton = createButton("Save");
        formPanel.add(saveButton, gbc);

        backgroundPanel.add(formPanel);
    }

    private JLabel createLabel(String text) {
        JLabel label = new JLabel(text);
        label.setFont(new Font("Segoe UI", Font.BOLD, 26)); // Larger font size
        return label;
    }

    private JTextField createTextField() {
        JTextField textField = new JTextField(25); // Larger text field
        textField.setFont(new Font("Segoe UI", Font.PLAIN, 24));
        return textField;
    }

    private JButton createButton(String text) {
        JButton button = new JButton(text);
        button.setFocusPainted(false);
        button.setFont(new Font("Segoe UI", Font.BOLD, 23));
        button.setBackground(new Color(0, 102, 204));
        button.setForeground(Color.WHITE);
        return button;
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

    private boolean validateInputs() {
        if (getVendorName().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Vendor Name cannot be empty.", "Validation Error",
                    JOptionPane.ERROR_MESSAGE);
            return false;
        }

        if (getVendorAddress().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Vendor Address cannot be empty.", "Validation Error",
                    JOptionPane.ERROR_MESSAGE);
            return false;
        }

        String contact = getContactNo();
        if (contact.isEmpty() || !contact.matches("\\d{10,15}")) {
            JOptionPane.showMessageDialog(this, "Contact Number must be numeric and 10 to 15 digits long.",
                    "Validation Error", JOptionPane.ERROR_MESSAGE);
            return false;
        }

        return true;
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            AddVendorPanel panel = new AddVendorPanel();
            panel.setVisible(true);
        });
    }
}
// package view;


// import SuperAdmin.controller.BranchController;
// import controller.UserController;
// import model.UserModel;

// import javax.swing.*;
// import java.awt.*;
// import java.awt.event.ActionEvent;
// import java.awt.event.ActionListener;
// import java.util.ArrayList;
// import java.util.List;

// public class AddUserView extends JFrame {
//     private JComboBox<String> branchComboBox;
//     private JTextField nameField;
//     private JTextField emailField;
//     private JTextField salaryField;
//     private JTextField empNumberField;
//     private JButton addButton;
//     private List<Integer> activeBranchCodes;
//     private UserTableView parent;
//     private String role;

//     public AddUserView(UserTableView parent) {
//         this.parent = parent;
//         this.role = parent.getRole();
//         setTitle("Add " + role);
//         setSize(600, 500);
//         setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
//         setLocationRelativeTo(null);
//         setLayout(new GridBagLayout());

//         activeBranchCodes = new ArrayList<>();
//         GridBagConstraints gbc = new GridBagConstraints();
//         gbc.insets = new Insets(10, 10, 10, 10);
//         gbc.fill = GridBagConstraints.HORIZONTAL;

//         branchComboBox = new JComboBox<>();
//         nameField = new JTextField(20);
//         emailField = new JTextField(20);
//         salaryField = new JTextField(20);
//         empNumberField = new JTextField(20);
//         addButton = new JButton("Add " + role);

//         Font font = new Font("Arial", Font.PLAIN, 14);
//         addButton.setFont(new Font("Arial", Font.BOLD, 16));
//         nameField.setFont(font);
//         emailField.setFont(font);
//         salaryField.setFont(font);
//         empNumberField.setFont(font);
//         addButton.setBackground(new Color(76, 175, 80));
//         addButton.setForeground(Color.WHITE);

//         gbc.gridx = 0;
//         gbc.gridy = 0;
//         gbc.gridwidth = 2;
//         add(new JLabel("Add " + role, JLabel.CENTER), gbc);

//         gbc.gridwidth = 1;
//         gbc.gridy++;
//         add(new JLabel("Select Branch:"), gbc);

//         gbc.gridx = 1;
//         add(branchComboBox, gbc);

//         gbc.gridx = 0;
//         gbc.gridy++;
//         add(new JLabel("Name:"), gbc);

//         gbc.gridx = 1;
//         add(nameField, gbc);

//         gbc.gridx = 0;
//         gbc.gridy++;
//         add(new JLabel("Email:"), gbc);

//         gbc.gridx = 1;
//         add(emailField, gbc);

//         gbc.gridx = 0;
//         gbc.gridy++;
//         add(new JLabel("Salary:"), gbc);

//         gbc.gridx = 1;
//         add(salaryField, gbc);

//         gbc.gridx = 0;
//         gbc.gridy++;
//         add(new JLabel("Employee Number:"), gbc);

//         gbc.gridx = 1;
//         add(empNumberField, gbc);

//         gbc.gridx = 1;
//         gbc.gridy++;
//         gbc.gridwidth = 2;
//         add(addButton, gbc);
//         setVisible(true);

//          // Call the controller to load active branches into the JComboBox
//         BranchController userController = new BranchController();
//         userController.loadActiveBranchesToView(this);


//         addButton.addActionListener(new ActionListener() {
//             @Override
//             public void actionPerformed(ActionEvent e) {
//                 addUser();
//             }
//         });
//     }

//     public void addBranchToComboBox(String branch, int branchCode) {
//         branchComboBox.addItem(branch);
//         activeBranchCodes.add(branchCode);
//     }

//     private void addUser() {
//         String name = nameField.getText().trim();
//         String email = emailField.getText().trim();
//         String salaryText = salaryField.getText().trim();
//         String empNumberText = empNumberField.getText().trim();

//         if (name.isEmpty() || email.isEmpty() || salaryText.isEmpty() || empNumberText.isEmpty()) {
//             JOptionPane.showMessageDialog(this, "All fields must be filled out.", "Error", JOptionPane.ERROR_MESSAGE);
//             return;
//         }

//         if (!email.matches("^[A-Za-z0-9+_.-]+@(.+)$")) {
//             JOptionPane.showMessageDialog(this, "Invalid email format.", "Error", JOptionPane.ERROR_MESSAGE);
//             return;
//         }

//         try {
//             double salary = Double.parseDouble(salaryText);
//             if (salary <= 0) {
//                 JOptionPane.showMessageDialog(this, "Salary must be a positive number.", "Error",
//                         JOptionPane.ERROR_MESSAGE);
//                 return;
//             }

//             int empNumber = Integer.parseInt(empNumberText);
//             if (empNumber <= 0) {
//                 JOptionPane.showMessageDialog(this, "Employee number must be a positive integer.", "Error",
//                         JOptionPane.ERROR_MESSAGE);
//                 return;
//             }

//             int selectedBranchCode = activeBranchCodes.get(branchComboBox.getSelectedIndex());

//             int confirm = JOptionPane.showConfirmDialog(this,
//                     "Are you sure you want to add this " + role + "?\n\n"
//                             + "Name: " + name + "\n"
//                             + "Email: " + email + "\n"
//                             + "Salary: " + salary + "\n"
//                             + "Employee Number: " + empNumber + "\n"
//                             + "Branch: " + branchComboBox.getSelectedItem(),
//                     "Confirm Addition", JOptionPane.YES_NO_OPTION);

//             if (confirm == JOptionPane.YES_OPTION) {
//                 UserModel newUser = new UserModel();
//                 newUser.setName(name);
//                 newUser.setEmail(email);
//                 newUser.setPassword("123");
//                 newUser.setRole(role);
//                 newUser.setBranchCode(selectedBranchCode);
//                 newUser.setSalary(salary);
//                 newUser.setEmpNumber(empNumber);

//                 UserController userController = new UserController();
//                 userController.addUser(newUser);
//                 parent.loadUserData(role);
//                 dispose();
//             }
//         } catch (NumberFormatException ex) {
//             JOptionPane.showMessageDialog(this, "Invalid salary or employee number.", "Error",
//                     JOptionPane.ERROR_MESSAGE);
//         } catch (IndexOutOfBoundsException ex) {
//             JOptionPane.showMessageDialog(this, "Please select a valid branch.", "Error", JOptionPane.ERROR_MESSAGE);
//         }
//     }
// }


package view;

import SuperAdmin.controller.BranchController;
import controller.UserController;
import model.UserModel;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

public class AddUserView extends JFrame {
    private JComboBox<String> branchComboBox;
    private JTextField nameField;
    private JTextField emailField;
    private JTextField salaryField;
    private JTextField empNumberField;
    private JButton addButton;
    private List<Integer> activeBranchCodes;
    private UserTableView parent;
    private String role;

    public AddUserView(UserTableView parent) {
        this.parent = parent;
        this.role = parent.getRole();
        setTitle("Add " + role);
        setSize(600, 500);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new GridBagLayout());

        activeBranchCodes = new ArrayList<>();
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // UI Components
        branchComboBox = new JComboBox<>();
        nameField = new JTextField(20);
        emailField = new JTextField(20);
        salaryField = new JTextField(20);
        empNumberField = new JTextField(20);
        addButton = new JButton("Add " + role);

        // Set Modern Font
        Font font = new Font("Segoe UI", Font.PLAIN, 14);
        addButton.setFont(new Font("Segoe UI", Font.BOLD, 16));
        nameField.setFont(font);
        emailField.setFont(font);
        salaryField.setFont(font);
        empNumberField.setFont(font);

        // Set Button Color
        addButton.setBackground(new Color(76, 175, 80)); // Modern green
        addButton.setForeground(Color.WHITE);
        addButton.setFocusPainted(false);
        addButton.setBorderPainted(false);
        addButton.setCursor(new Cursor(Cursor.HAND_CURSOR));

        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        add(new JLabel("Add " + role, JLabel.CENTER), gbc);

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

        // Set background panel with semi-transparent image
        setContentPane(createBackgroundPanel());

        // Load branches into the ComboBox
        BranchController userController = new BranchController();
        userController.loadActiveBranchesToView(this);

        addButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                addUser();
            }
        });
    }

    private JPanel createBackgroundPanel() {
        return new JPanel() {
            private Image backgroundImage = new ImageIcon("src\\imgs\\your-image.jpg").getImage();

            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);

                // Draw background image
                if (backgroundImage != null) {
                    g.drawImage(backgroundImage, 0, 0, getWidth(), getHeight(), this);
                }

                // Semi-transparent overlay
                Graphics2D g2d = (Graphics2D) g.create();
                g2d.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);

                Color overlayColor = new Color(0, 0, 0, 50); // Darker, subtle overlay
                g2d.setColor(overlayColor);
                g2d.fillRect(0, 0, getWidth(), getHeight());
                g2d.dispose();
            }
        };
    }

    public void addBranchToComboBox(String branch, int branchCode) {
        branchComboBox.addItem(branch);
        activeBranchCodes.add(branchCode);
    }

    private void addUser() {
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
                    "Are you sure you want to add this " + role + "?\n\n"
                            + "Name: " + name + "\n"
                            + "Email: " + email + "\n"
                            + "Salary: " + salary + "\n"
                            + "Employee Number: " + empNumber + "\n"
                            + "Branch: " + branchComboBox.getSelectedItem(),
                    "Confirm Addition", JOptionPane.YES_NO_OPTION);

            if (confirm == JOptionPane.YES_OPTION) {
                UserModel newUser = new UserModel();
                newUser.setName(name);
                newUser.setEmail(email);
                newUser.setPassword("123");
                newUser.setRole(role);
                newUser.setBranchCode(selectedBranchCode);
                newUser.setSalary(salary);
                newUser.setEmpNumber(empNumber);

                UserController userController = new UserController();
                userController.addUser(newUser);
                parent.loadUserData(role);
                dispose();
            }
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Invalid salary or employee number.", "Error",
                    JOptionPane.ERROR_MESSAGE);
        } catch (IndexOutOfBoundsException ex) {
            JOptionPane.showMessageDialog(this, "Please select a valid branch.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}

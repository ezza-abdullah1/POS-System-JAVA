package SuperAdmin.view;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import utils.RoundedButton;

public class dashboard {

    public dashboard() {

        // Create the main frame
        JFrame frame = new JFrame("Super Admin Dashboard");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(600, 400);
        frame.setLayout(new BorderLayout());

        // Header Label
        JLabel headerLabel = new JLabel("Super Admin Dashboard", SwingConstants.CENTER);
        headerLabel.setFont(new Font("Serif", Font.BOLD, 28)); // Classy, larger font
        headerLabel.setOpaque(true);
        headerLabel.setBackground(new Color(0x001d3d)); // Blue background for header
        headerLabel.setForeground(Color.WHITE); // White text
        headerLabel.setBorder(BorderFactory.createEmptyBorder(20, 0, 20, 0)); // Add padding
        frame.add(headerLabel, BorderLayout.NORTH);
        // Buttons Panel
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new GridLayout(3, 1, 10, 10));

        // Main Panel with GridBagLayout
        JPanel mainPanel = new JPanel(new GridBagLayout());
        mainPanel.setOpaque(false); // Transparent to show frame background
        GridBagConstraints gbc = new GridBagConstraints();

        RoundedButton createBranchButton = new RoundedButton("Manage Branch");
        RoundedButton createBranchManagerButton = new RoundedButton("Manage Branch Manager");
        RoundedButton viewReportsButton = new RoundedButton("View Reports");

        JButton[] buttons = {
                createBranchButton,
                createBranchManagerButton,
                viewReportsButton
        };

        // Style and Add Buttons
        gbc.gridwidth = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(20, 10, 10, 10);
        int row = 0;
        for (JButton button : buttons) {
            gbc.gridx = 0;
            gbc.gridy = row;
            gbc.gridwidth = 2; // Full-width buttons
            mainPanel.add(button, gbc);
            row++;
        }

        // Add Main Panel to Frame
        frame.add(mainPanel, BorderLayout.CENTER);
        // Add Action Listeners
        createBranchButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new branchView();
            }
        });

        createBranchManagerButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new BranchManagerView();
            }
        });

        viewReportsButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new reportsView();
            }
        });

        // Set frame visibility
        frame.setVisible(true);

    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new dashboard();
        });
    }

}

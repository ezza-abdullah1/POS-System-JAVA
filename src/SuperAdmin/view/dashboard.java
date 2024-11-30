package SuperAdmin.view;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class dashboard {

    public dashboard() {

        // Create the main frame
        JFrame frame = new JFrame("Super Admin Dashboard");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(600, 400);
        frame.setLayout(new BorderLayout());

        // Header Label
        JLabel headerLabel = new JLabel("Super Admin Dashboard", SwingConstants.CENTER);
        headerLabel.setFont(new Font("Arial", Font.BOLD, 20));
        frame.add(headerLabel, BorderLayout.NORTH);

        // Buttons Panel
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new GridLayout(3, 1, 10, 10));

        JButton createBranchButton = new JButton("Manage Branch");
        JButton createBranchManagerButton = new JButton("Manage Branch Manager");
        JButton viewReportsButton = new JButton("View Reports");

        buttonPanel.add(createBranchButton);
        buttonPanel.add(createBranchManagerButton);
        buttonPanel.add(viewReportsButton);

        frame.add(buttonPanel, BorderLayout.CENTER);

        // Add Action Listeners
        createBranchButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new BranchesView();
            }
        });

        createBranchManagerButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // showCreateBranchManagerDialog(frame);
            }
        });

        viewReportsButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // showReportsDialog(frame);
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

package BranchManager.view;

import utils.RoundedButton;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class DashboardBR {

    private JButton manageCashiersButton;
    private JButton manageDEOButton;

    public DashboardBR() {
        // Create the main frame
        JFrame frame = new JFrame("Branch Manager Dashboard");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(700, 600); // Adjusted size
        frame.setLocationRelativeTo(null); // Center the frame
        frame.setLayout(new BorderLayout());
        frame.getContentPane().setBackground(new Color(240, 248, 255)); // Light blue background

        // Header Label
        JLabel headerLabel = new JLabel("Branch Manager Dashboard", SwingConstants.CENTER);
        headerLabel.setFont(new Font("Serif", Font.BOLD, 28)); // Classy, larger font
        headerLabel.setOpaque(true);
        headerLabel.setBackground(new Color(0x001d3d)); // Blue background for header
        headerLabel.setForeground(Color.WHITE); // White text
        headerLabel.setBorder(BorderFactory.createEmptyBorder(20, 0, 20, 0)); // Add padding
        frame.add(headerLabel, BorderLayout.NORTH);

        // Main Panel with GridBagLayout
        JPanel mainPanel = new JPanel(new GridBagLayout());
        mainPanel.setOpaque(false); // Transparent to show frame background
        GridBagConstraints gbc = new GridBagConstraints();

        // Initialize Buttons as rounded buttons
        manageCashiersButton = new RoundedButton("Manage Cashiers");
        manageDEOButton = new RoundedButton("Manage Data Entry Operators");

        JButton[] buttons = {
                manageCashiersButton,
                manageDEOButton
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
        manageCashiersButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new CashierView();
            }
        });

        manageDEOButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new DEO_View();
            }
        });

        // Set frame visibility
        frame.setVisible(true);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(DashboardBR::new);
    }
}

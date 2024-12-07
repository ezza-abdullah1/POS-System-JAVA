package BranchManager.view;

import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;

import view.UserTableView;

public class CashierView {

    CashierView()
    {
        SwingUtilities.invokeLater(() -> {
            try {
                new UserTableView("Cashier");
            } catch (Exception e) {
                JOptionPane.showMessageDialog(null, "Error initializing the application: " + e.getMessage(),
                        "Application Error", JOptionPane.ERROR_MESSAGE);
            }
        });
    }
}

package BranchManager.view;

import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;

import view.UserTableView;

public class DEO_View {
    private int branchCode;

    DEO_View(int branchCode)
    {
        SwingUtilities.invokeLater(() -> {
            try {
                new UserTableView(branchCode,"DataEntryOperator");
            } catch (Exception e) {
                JOptionPane.showMessageDialog(null, "Error initializing the application: " + e.getMessage(),
                        "Application Error", JOptionPane.ERROR_MESSAGE);
            }
    });

}
}

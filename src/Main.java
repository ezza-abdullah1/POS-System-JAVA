import javax.swing.SwingUtilities;

import controller.CashierController;
import view.CashierView;

public class Main {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            CashierView view = new CashierView();
            CashierController controller = new CashierController(view);
            view.setVisible(true);
        });
    }
}
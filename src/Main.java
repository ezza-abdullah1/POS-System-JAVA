import javax.swing.SwingUtilities;

import controller.CashierController;
import view.CashierView;
import view.SplashScreenView;

public class Main {
    public static void main(String[] args) {
        // Invoke the splash screen
        SwingUtilities.invokeLater(SplashScreenView::showSplashScreen);
    }
}
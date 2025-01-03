import javax.swing.SwingUtilities;

import view.SplashScreenView;

public class Main {
    public static void main(String[] args) {
        // Invoke the splash screen
        SwingUtilities.invokeLater(SplashScreenView::showSplashScreen);
    }
}
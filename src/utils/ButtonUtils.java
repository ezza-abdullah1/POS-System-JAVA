import javax.swing.*;
import java.awt.event.ActionListener;

public class ButtonUtils  {
    public static JButton createButton(String text, ActionListener listener) {
        JButton button = new JButton(text);
        if (listener != null) {
            button.addActionListener(listener);
        }
        return button;
    }
}
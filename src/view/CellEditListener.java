package view;

import javafx.scene.input.KeyEvent;

@FunctionalInterface
public interface CellEditListener {
    void onCellEdit(int row, int column, String newValue);
}
@FunctionalInterface
 interface KeyPressListener {
    void onKeyPress(KeyEvent e);
}
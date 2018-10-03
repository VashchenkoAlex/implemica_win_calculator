package win_calculator;

import javafx.scene.input.KeyCode;

import java.awt.event.KeyEvent;

public class ButtonForTest {

    private String id;
    private /*KeyCode*/int keyCode;
    private boolean shiftPressed;

    ButtonForTest(String id, /*KeyCode*/int keyCode, boolean shiftPressed) {
        this.id = id;
        this.keyCode = keyCode;
        this.shiftPressed = shiftPressed;
    }

    public String getId() {
        return id;
    }

    public /*KeyCode*/int getKeyCode() {
        return keyCode;
    }

    public boolean isShiftPressed() {
        return shiftPressed;
    }
}

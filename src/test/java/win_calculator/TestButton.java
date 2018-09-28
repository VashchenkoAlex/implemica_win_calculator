package win_calculator;

import javafx.scene.input.KeyCode;

public class TestButton {

    private String id;
    private KeyCode keyCode;
    private boolean shiftPressed;

    public TestButton(String id, KeyCode keyCode, boolean shiftPressed) {
        this.id = id;
        this.keyCode = keyCode;
        this.shiftPressed = shiftPressed;
    }

    public String getId() {
        return id;
    }

    public KeyCode getKeyCode() {
        return keyCode;
    }

    public boolean isShiftPressed() {
        return shiftPressed;
    }
}

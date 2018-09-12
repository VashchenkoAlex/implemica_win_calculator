package win_calculator.GUITests;

import javafx.scene.input.KeyCode;

class TestButton {

    private String id;
    private KeyCode keyCode;
    private boolean shiftPressed;

    TestButton(String id, KeyCode keyCode, boolean shiftPressed) {
        this.id = id;
        this.keyCode = keyCode;
        this.shiftPressed = shiftPressed;
    }

    String getId() {
        return id;
    }

    KeyCode getKeyCode() {
        return keyCode;
    }

    boolean isShiftPressed() {
        return shiftPressed;
    }
}

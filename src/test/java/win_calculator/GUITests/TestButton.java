package win_calculator.GUITests;

import javafx.scene.input.KeyCode;

public class TestButton {

    private String id;
    private KeyCode keyCode;

    public TestButton(String id, KeyCode keyCode) {
        this.id = id;
        this.keyCode = keyCode;
    }

    public String getId() {
        return id;
    }

    public KeyCode getKeyCode() {
        return keyCode;
    }
}

package win_calculator;

/**
 * Entity Button that contains required fields for tests
 */
class ButtonForTest {

    /**
     * String id of button using in test's expression
     */
    private String id;
    /**
     * Keyboard keycode for awt.Robot using in tests
     */
    private int keyCode;
    /**
     * Field stores info does robot have to press shift before button
     */
    private boolean shiftPressed;

    /**
     * Initialize {@link ButtonForTest}
     * @param id - key id;
     * @param keyCode - keyboard keycode;
     * @param shiftPressed - is shift pressed before button;
     */
    ButtonForTest(String id, int keyCode, boolean shiftPressed) {
        this.id = id;
        this.keyCode = keyCode;
        this.shiftPressed = shiftPressed;
    }

    /**
     * Getter for id
     * @return id
     */
    String getId() {
        return id;
    }

    /**
     * Getter for keycode
     * @return keycode
     */
    int getKeyCode() {
        return keyCode;
    }

    /**
     * Getter for shiftPressed
     * @return true if shift has to be pressed before button
     */
    boolean isShiftPressed() {
        return shiftPressed;
    }
}

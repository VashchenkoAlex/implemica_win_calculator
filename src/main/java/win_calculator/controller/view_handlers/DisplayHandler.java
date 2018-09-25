package win_calculator.controller.view_handlers;

import javafx.scene.control.Label;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import win_calculator.controller.digits.Digit;
import win_calculator.model.nodes.actions.Action;
import win_calculator.model.nodes.actions.ActionType;

import static win_calculator.controller.utils.ControllerUtils.addCapacity;
import static win_calculator.controller.utils.ControllerUtils.isComaAbsent;
import static win_calculator.model.nodes.actions.ActionType.BACKSPACE;
import static win_calculator.model.nodes.actions.ActionType.DIGIT;

public class DisplayHandler {

    private Label display;
    private static final String COMA = ",";
    private static final String ZERO = "0";
    private static final int MAX_DIGITS = 16;
    private static final double DEFAULT_FONT_SIZE = 47.0;
    private static final String DISPLAY_TEXT_ID = ".text";

    public void setDisplay(Label display) {

        this.display = display;
    }

    private void clearDisplay() {

        display.setText(ZERO);
    }

    private void setDisplayedText(String string) {

        if (!"".equals(string)) {
            display.setText(string);
            fixFontSize();
        } else {
            clearDisplay();
        }
    }

    private void addZero(){

        setDisplayedText(addCapacity(display.getText()+ZERO));

    }

    public void addComa() {

        String text = display.getText();
        if (!"".equals(text) && isComaAbsent(text)) {
            setDisplayedText(text + COMA);
        }
    }

    private void fixFontSize() {

        display.setFont(new Font(display.getFont().getName(), DEFAULT_FONT_SIZE));
        display.layout();
        while (isOverrun()) {
            display.setFont(new Font(display.getFont().getName(), display.getFont().getSize() - 1));
            display.layout();
        }
    }

    private boolean isOverrun() {

        return !display.getText().equals(((Text) display.lookup(DISPLAY_TEXT_ID)).getText());
    }

    private boolean isNotMax(){

        return display.getText().replace("0,","").replaceAll("[ ,]","").length() < MAX_DIGITS;
    }

    public void sendToDisplay(Action action, String number, ActionType previousActionType){

        if (DIGIT.equals(action.getType())){
            if (isNotMax()) {
                if (ZERO.equals(((Digit)action).getValue()) && DIGIT.equals(previousActionType) || BACKSPACE.equals(previousActionType)) {
                    addZero();
                }else {
                    setDisplayedText(number);
                }
            }else if (!DIGIT.equals(previousActionType)){
                setDisplayedText(number);
            }
        }else {
            setDisplayedText(number);
        }
    }
}

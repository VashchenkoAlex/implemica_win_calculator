package win_calculator.controller.view_handlers;

import javafx.scene.control.Label;
import javafx.scene.text.Font;
import javafx.scene.text.Text;

import static win_calculator.utils.StringUtils.*;

public class DisplayHandler {

    private Label display;
    private static final String COMA = ",";

    public void setDisplay(Label display) {

        this.display = display;
    }

    private void clearDisplay() {

        display.setText("0");
    }

    public void setDisplayedText(String string) {

        if (!"".equals(string)) {
            if (!string.contains("e")){
                //string = prepareCapacity(string);
            }
            display.setText(string);
            fixFontSize();
        } else {
            clearDisplay();
        }
    }

    public void addComa() {

        String text = display.getText();
        if (!"".equals(text) && isComaAbsent(text)) {
            display.setText(text + COMA);
        }
    }

    private void fixFontSize() {

        display.setFont(new Font(display.getFont().getName(), 47.0));
        display.layout();
        while (isOverrun()) {
            display.setFont(new Font(display.getFont().getName(), display.getFont().getSize() - 1));
            display.layout();
        }
    }

    private boolean isOverrun() {

        return !display.getText().equals(((Text) display.lookup(".text")).getText());
    }

}

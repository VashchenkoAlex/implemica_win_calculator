package win_calculator.controller.view_handlers;

import javafx.scene.control.Label;
import javafx.scene.text.Font;
import javafx.scene.text.Text;

import java.math.BigDecimal;

import static win_calculator.utils.AppUtils.*;

public class DisplayHandler {

    private Label display;
    private static final String COMA = ",";
    private static final String ZERO = "0";
    private static final int MAX_DIGITS = 16;
    private static final double DEFAULT_FONT_SIZE = 47.0;
    private static final String DISPLAY_TEXT_ID = ".text";
    private static final String DISPLAY_PATTERN = "#############,###.################";

    public void setDisplay(Label display) {

        this.display = display;
    }

    private void clearDisplay() {

        display.setText(ZERO);
    }

    public void sendNumberToDisplay(BigDecimal number){

        setDisplayedText(convertToString(number,DISPLAY_PATTERN));
    }
    public void setDisplayedText(String string) {

        if (!"".equals(string)) {
            display.setText(string);
            fixFontSize();
        } else {
            clearDisplay();
        }
    }

    public void addZero(){

        setDisplayedText(addCapacity(display.getText()+ZERO));

    }

    public void addComa() {

        String text = display.getText();
        if (!"".equals(text) && isComaAbsent(text)) {
            display.setText(text + COMA);
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

    public boolean isNotMax(){

        return display.getText().replace("0,","").replaceAll("[ ,]","").length() < MAX_DIGITS;
    }

    public String getDisplayText(){

        return display.getText();
    }
}

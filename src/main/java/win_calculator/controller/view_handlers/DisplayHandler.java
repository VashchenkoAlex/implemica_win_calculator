package win_calculator.controller.view_handlers;

import javafx.scene.control.Label;
import javafx.scene.text.Font;
import javafx.scene.text.Text;

import static win_calculator.utils.StringUtils.*;

public class DisplayHandler {

    private Label display;

    public void setDisplay(Label display) {

        this.display = display;
    }

    public void clearDisplay(){

        display.setText("0");
    }

    public void setDisplayedText(String string){

        if (!"".equals(string)){
            display.setText(string);
            fixFontSize();
        }else {
            clearDisplay();
        }
    }

    public void addComa(){

        String text = display.getText();
        if (!"".equals(text)&& isComaAbsent(text)){
            display.setText(optimizeStringWithComaAndZero(text+","));
        }
    }

    private void fixFontSize(){
        display.layout();
        while (overrun()){
            double x = display.getFont().getSize();
            display.setFont(new Font(x-10.0));

        }

    }
    private boolean overrun(){

        return !display.getText().equals(((Text)display.lookup(".text")).getText());
    }
}

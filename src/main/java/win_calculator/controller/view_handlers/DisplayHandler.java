package win_calculator.controller.view_handlers;

import javafx.scene.control.TextField;

import static win_calculator.utils.StringUtils.*;

public class DisplayHandler {

    private TextField display;

    public void setDisplay(TextField display) {

        this.display = display;
    }

    public void clearDisplay(){

        display.setText("0");
    }

    public void setDisplayedText(String string){

        if (!"".equals(string)){
            display.setText(cutLastComa(cutLastZeros(optimizeString(string))));
        }
    }

    public void addComa(){

        String text = display.getText();
        if (!"".equals(text)&& isComaAbsent(text)){
            display.setText(optimizeStringWithComaAndZero(text+","));
        }
    }

    public void setDisplayedTextWithZero(String string){

        display.setText(optimizeStringWithComaAndZero(string));
    }


}

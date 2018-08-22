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

        String currentStr = replaceDotToComa(string);
        if (!"".equals(currentStr)){
            if (currentStr.startsWith("0,")){
                display.setText(currentStr);
            }else {
                display.setText(cutLastComa(cutLastZeros(optimizeString(string))));
            }
        }else {
            clearDisplay();
        }
    }

    public void setEnteredText(String string){

        if (!"".equals(string)){
            String currentStr = addCapacity(replaceDotToComa(string));
            display.setText(currentStr);
        }
    }

    public void addComa(){

        String text = display.getText();
        if (!"".equals(text)&& isComaAbsent(text)){
            display.setText(optimizeStringWithComaAndZero(text+","));
        }
    }


}

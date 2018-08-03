package win_calculator;

import javafx.fxml.FXML;
import javafx.scene.control.*;

import java.util.Optional;
import static win_calculator.MainOperation.*;
import static win_calculator.MainOperations.*;
import static win_calculator.StringUtils.*;

public class MainController
{

    @FXML
    private Button menu;

    @FXML
    private TextField display;

    @FXML
    private Label historyText;

    private static boolean wasNotAction = true;
    private static final String ZERO = "0";
    private static final String ZERO_REGEX = "^0";
    private static final String COMA = ",";

    public void buttonMenuClick(){

        Dialog<String> dialog = new TextInputDialog("Enter new text here");
        dialog.setTitle("Change button text");
        Optional<String> optional = dialog.showAndWait();
        optional.ifPresent(menu::setText);
    }

    public void buttonHistoryClick(){

        Dialog<String> dialog = new TextInputDialog("Enter new text here");
        dialog.setTitle("Change button text");
        Optional<String> optional = dialog.showAndWait();
        optional.ifPresent(menu::setText);
    }

    public void buttonPercentClick(){

        setWasNotAction(false);
    }

    public void buttonSqrtClick(){

        setWasNotAction(false);
    }

    public void buttonSqrClick(){

        setWasNotAction(false);
    }

    public void buttonFractionOneClick(){

        setWasNotAction(false);
    }

    public void buttonClearEnteredClick(){

        setDisplayZero();
        setWasNotAction(true);
    }

    public void buttonClearClick(){

        setDisplayZero();
        historyText.setText("");
        setEmptyVariables();
        setWasNotAction(true);
    }

    public void buttonBackSpaceClick(){

        String currentText = display.getText();
        setWasNotAction(true);
        if (currentText.length()>1){
            display.setText(deleteOneSymbolFromTheEnd(currentText));
        }
        if (currentText.length()==1){
            setDisplayZero();
        }
    }

    public void buttonOneClick(){

        setTextForDisplay("1");
        setWasNotAction(true);
    }

    public void buttonTwoClick(){

        setTextForDisplay("2");
        setWasNotAction(true);
    }

    public void buttonThreeClick(){

        setTextForDisplay("3");
        setWasNotAction(true);
    }

    public void buttonFourClick(){

        setTextForDisplay("4");
        setWasNotAction(true);
    }

    public void buttonFiveClick(){

        setTextForDisplay("5");
        setWasNotAction(true);
    }

    public void buttonSixClick(){

        setTextForDisplay("6");
        setWasNotAction(true);
    }

    public void buttonSevenClick(){

        setTextForDisplay("7");
        setWasNotAction(true);
    }

    public void buttonEightClick(){

        setTextForDisplay("8");
        setWasNotAction(true);
    }

    public void buttonNineClick(){

        setTextForDisplay("9");
        setWasNotAction(true);
    }

    public void buttonZeroClick(){

        if (isZeroValidPosition()){
            setTextForDisplay(ZERO);
            setWasNotAction(true);
        }
    }

    public void buttonDivideClick(){

        setWasNotAction(false);
    }

    public void buttonMultiplyClick(){

        setWasNotAction(false);
    }

    public void buttonMinusClick(){

        if (wasNotAction){
            String varStr = removeSpaces(display.getText());
            //doPlus(varStr);
            if (isResult()){
                display.setText(getResultString());
            }
        }else {
            changeOperationAtHistory(MINUS);
        }
        setWasNotAction(false);
    }

    public void buttonPlusClick(){

        if (wasNotAction){
            String displayValue = removeSpaces(display.getText());
            setHistoryText(displayValue+PLUS.getValue());
            doPlus(displayValue);
            if (isResult()){
                display.setText(getResultString());
            }
        }else {
            changeOperationAtHistory(PLUS);
        }
        setWasNotAction(false);
    }

    public void buttonNegateClick(){

        setWasNotAction(true);
    }

    public void buttonComaClick(){

        if (isComaAbsent(display.getText())){
            setTextForDisplay(COMA);
            setWasNotAction(true);
        }
    }

    public void buttonEnterClick(){

    }

    public void buttonClearAllMemoryClick(){

    }

    public void buttonMemoryRecallClick(){

    }

    public void buttonMemoryAddClick(){

    }

    public void buttonMemorySubtractClick(){

    }

    public void buttonMemoryStoreClick(){

    }

    public void buttonMemoryClick(){

    }

    private void setTextForDisplay(String value){
        String currentStr;
        if (wasNotAction){
            currentStr = display.getText();
            if (ZERO.equals(currentStr)){
                if (COMA.equals(value)){
                    display.setText(ZERO+COMA);
                }
                display.setText(value);
                return;
            }
            if (currentStr.length()<21){
                if (currentStr.length()>2 && isComaAbsent(currentStr) && !COMA.equals(value)) {
                    currentStr = removeSpaces(currentStr);
                    currentStr = addSpaces(currentStr,1);
                }
                display.setText(currentStr+value);
            }
        }else {
            display.setText(value);
        }
    }

    private void setWasNotAction(boolean val){
        wasNotAction = val;
    }

    private boolean isZeroValidPosition(){

        return !display.getText().matches(ZERO_REGEX);
    }

    private void setDisplayZero(){

        display.setText("0");
    }

    private void setHistoryText(String string){
        historyText.setText(historyText.getText()+string);
    }

    private void changeOperationAtHistory(MainOperation operation){
        String historyString = historyText.getText();
        historyText.setText(historyString.substring(0,historyString.length()-3)+operation.getValue());
    }
}

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

    private static boolean wasOperationBefore = false;
    private static boolean wasNumber = true;
    private static MainOperation lastOperation;
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

        setWasNumber(false);
    } //TO DO

    public void buttonSqrtClick(){

        setWasNumber(false);
    } //TO DO

    public void buttonSqrClick(){

        setWasNumber(false);
    } //TO DO

    public void buttonFractionOneClick(){

        setWasNumber(false);
    } //TO DO

    // -------- CLEAR BUTTONS ------------------
    public void buttonClearEnteredClick(){

        setDisplay(ZERO);
        setWasNumber(true);
    } //DONE

    public void buttonClearClick(){

        setDisplay(ZERO);
        clearHistory();
        setEmptyVariables();
        setWasNumber(true);
    } //DONE

    public void buttonBackSpaceClick(){

        String currentText = display.getText();
        if (currentText.length()>1){
            display.setText(deleteOneSymbolFromTheEnd(currentText));
        }
        if (currentText.length()==1){
            setDisplay(ZERO);
        }
        setWasNumber(true);
    } //DONE

    // -------- NUMBER BUTTONS -----------------
    public void buttonOneClick(){

        addNumberToDisplay("1");
        setWasNumber(true);
    } //DONE

    public void buttonTwoClick(){

        addNumberToDisplay("2");
        setWasNumber(true);
    } //DONE

    public void buttonThreeClick(){

        addNumberToDisplay("3");
        setWasNumber(true);
    } //DONE

    public void buttonFourClick(){

        addNumberToDisplay("4");
        setWasNumber(true);
    } //DONE

    public void buttonFiveClick(){

        addNumberToDisplay("5");
        setWasNumber(true);
    } //DONE

    public void buttonSixClick(){

        addNumberToDisplay("6");
        setWasNumber(true);
    } //DONE

    public void buttonSevenClick(){

        addNumberToDisplay("7");
        setWasNumber(true);
    } //DONE

    public void buttonEightClick(){

        addNumberToDisplay("8");
        setWasNumber(true);
    } //DONE

    public void buttonNineClick(){

        addNumberToDisplay("9");
        setWasNumber(true);
    } //DONE

    public void buttonZeroClick(){

        if (isZeroNotFirst()){
            putZeroToDisplay();
            setWasNumber(true);
        }
    } //DONE

    public void buttonComaClick(){

        putComaToDisplay();
        setWasNumber(true);
    } //DONE

    // -------- MAIN OPERATIONS BUTTONS ----------
    public void buttonDivideClick(){

        setLastOperation(DIVIDE);

    }

    public void buttonMultiplyClick(){

        setLastOperation(MULTIPLY);

    }

    public void buttonMinusClick(){

        setLastOperation(MINUS);

    }

    public void buttonPlusClick(){

        setLastOperation(PLUS);
        addOperationToHistory(PLUS);
        setNextNumber();
        doOperation(PLUS);
        setWasNumber(false);
        setWasOperationBefore(true);
    }

    public void buttonEnterClick(){

        if (wasOperationBefore){
            finalizeCalc(lastOperation);
            setDisplay(getResultString());
        }
        setWasNumber(false);
    }

    public void buttonNegateClick(){

        setWasNumber(true);
    }

    public void buttonClearAllMemoryClick(){

    } //TO DO

    public void buttonMemoryRecallClick(){

    } //TO DO

    public void buttonMemoryAddClick(){

    } //TO DO

    public void buttonMemorySubtractClick(){

    } //TO DO

    public void buttonMemoryStoreClick(){

    } //TO DO

    public void buttonMemoryClick(){

    } //TO DO

    // -------- SUPPORT METHODS -----------------------
    private void addNumberToDisplay(String value){

        if (wasNumber && isZeroNotFirst()){
            String currentStr = display.getText();
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

    private void putZeroToDisplay(){

        if (wasNumber){
            String currentStr = display.getText();
            if (!ZERO.equals(currentStr)){
                setDisplay(currentStr+ZERO);
            }
        }else {
            setDisplay(ZERO);
        }

    }

    private void putComaToDisplay(){

        String currentStr = display.getText();
        if (!ZERO.equals(currentStr)){
            if (isComaAbsent(currentStr)){
                setDisplay(currentStr+COMA);
                return;
            }
        }
        setDisplay(ZERO+COMA);
    }

    private void setWasNumber(boolean val){
        wasNumber = val;
    }

    private void setWasOperationBefore(boolean val){
        wasOperationBefore = val;
    }

    private boolean isZeroNotFirst(){

        return !display.getText().matches(ZERO_REGEX);
    }

    private void setDisplay(String string){

        display.setText(string);
    }

    private void setHistoryText(String string){
        historyText.setText(historyText.getText()+string);
    }

    private void doOperation(MainOperation currentOperation){

        selectOperation(currentOperation); //+

        setWasOperationBefore(true);
    }

    private void clearHistory(){
        historyText.setText("");
    }

    private void setLastOperation(MainOperation operation){
        lastOperation = operation;
    }

    private void selectOperation(MainOperation operation){
        switch (operation){
            case PLUS: {
                doPlus();
                break;
            }
            case MINUS:{
                doMinus();
                break;
            }
            case MULTIPLY:{
                doMultiply();
                break;
            }
            case DIVIDE:{
                doDivide();
                break;
            }
        }
    }

    private void addOperationToHistory(MainOperation operation){
        if (wasNumber){
            setHistoryText(removeSpaces(display.getText())+operation.getValue());
        }else {
            changeOperationAtHistory(operation);
        }
    }

    private void changeOperationAtHistory(MainOperation operation){
        String historyString = historyText.getText();
        historyText.setText(historyString.substring(0,historyString.length()-3)+operation.getValue());
    }

    private void setNextNumber(){
        addNumber(removeSpaces(display.getText()));
    }
}

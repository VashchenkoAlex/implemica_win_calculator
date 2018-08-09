package win_calculator;

import javafx.application.Platform;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.RowConstraints;
import javafx.stage.Stage;
import win_calculator.exceptions.MyException;
import win_calculator.main_operations.*;

import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;

import static win_calculator.MainOperations.*;
import static win_calculator.StringUtils.*;

public class Controller implements Initializable
{

    @FXML
    private AnchorPane rootPane;

    @FXML
    private Button menu;

    @FXML
    private GridPane mainTable;

    @FXML
    private TextField display;

    @FXML
    private Label historyText;

    @FXML
    private Button fullScreenBtn;

    private static final String FULL_SCREEN_CSS = "/styles/full_screen_style.css";
    private static final String MAIN_CSS = "/styles/styles.css";
    private static boolean wasOperationBefore = false;
    private static boolean wasNumber = true;
    private static boolean isEnterRepeated = false;
    private static boolean isOperationRepeated = false;
    private static final String ZERO = "0";
    private static final String COMA = ",";

    public void closeBtn(){

        Platform.exit();
    } //DONE

    public void hideBtn(){

        Stage stage = (Stage) rootPane.getScene().getWindow();
        stage.toBack();
    } //DONE

    public void fullScreenBtnClick(){

        Stage stage = (Stage) rootPane.getScene().getWindow();
        if (stage.isFullScreen()){
            stage.setFullScreen(false);
            changeStyleTo(MAIN_CSS);
            fullScreenBtn.setText("\uE922");
        }else {
            stage.setFullScreen(true);
            changeStyleTo(FULL_SCREEN_CSS);
            fullScreenBtn.setText("\uE923");
        }
    }

    public void menuBtn(){

        Dialog<String> dialog = new TextInputDialog("...");
        dialog.setTitle("Change button text");
        Optional<String> optional = dialog.showAndWait();
        optional.ifPresent(menu::setText);
    } //TO DO

    public void historyBtn(){

        Dialog<String> dialog = new TextInputDialog("...");
        dialog.setTitle("Change button text");
        Optional<String> optional = dialog.showAndWait();
        optional.ifPresent(menu::setText);
    } //TO DO

    // -------- CLEAR BUTTONS ------------------
    public void clearEnteredBtnClick(){

        setDisplay(ZERO);
        setWasNumber(true);
    } //DONE

    public void clearBtnClick(){

        setDisplay(ZERO);
        clearHistory();
        resetValues();
        setWasNumber(true);
        setWasOperationBefore(false);
    }

    public void backspaceBtnClick(){

        String currentText = display.getText();
        if (currentText.length()>1&&wasNumber){
            display.setText(deleteOneSymbolFromTheEnd(currentText));
        }
        if (currentText.length()==1){
            setDisplay(ZERO);
        }
    }

    // -------- NUMBER BUTTONS -----------------
    public void oneBtnClick(){

        addNumberToDisplay("1");
        setWasNumber(true);
    }

    public void twoBtnClick(){

        addNumberToDisplay("2");
        setWasNumber(true);

    }

    public void threeBtnClick(){

        addNumberToDisplay("3");
        setWasNumber(true);
    }

    public void fourBtnClick(){

        addNumberToDisplay("4");
        setWasNumber(true);
    }

    public void fiveBtnClick(){

        addNumberToDisplay("5");
        setWasNumber(true);
    }

    public void sixBtnClick(){

        addNumberToDisplay("6");
        setWasNumber(true);
    }

    public void sevenBtnClick(){

        addNumberToDisplay("7");
        setWasNumber(true);
    }

    public void eightBtnClick(){

        addNumberToDisplay("8");
        setWasNumber(true);
    }

    public void nineBtnClick(){

        addNumberToDisplay("9");
        setWasNumber(true);
    }

    public void zeroBtnClick(){

        if (isZeroNotFirst()){
            putZeroToDisplay();
            setWasNumber(true);
        }
    }

    public void comaBtnClick(){

        putComaToDisplay();
    }

    // -------- MAIN OPERATIONS BUTTONS ----------
    public void divideBtnClick(){

        Divide divide = new Divide();
        addOperationToHistory(divide);
        String result = doMainOperation(divide,takeDisplayedNumber());
        if (!("".equals(result))){
            setDisplay(result);
        }
        optimizeDisplay();//?
    }

    public void multiplyBtnClick(){

        Multiply multiply = new Multiply();
        addOperationToHistory(multiply);
        String result = doMainOperation(multiply,takeDisplayedNumber());
        if (!("".equals(result))){
            setDisplay(result);
        }
        optimizeDisplay();
    }

    public void minusBtnClick(){

        Minus minus = new Minus();
        addOperationToHistory(minus);
        String result = doMainOperation(minus,takeDisplayedNumber());
        if (!("".equals(result))){
            setDisplay(result);
        }
        optimizeDisplay();
    }

    public void plusBtnClick(){

        Plus plus = new Plus();
        addOperationToHistory(plus);
        String result = doMainOperation(plus,takeDisplayedNumber());
        if (!("".equals(result))){
            setDisplay(result);
        }
        optimizeDisplay();
    }

    public void enterBtnClick(){

        String result = doEnter(takeDisplayedNumber());
        if (!"".equals(result)){
            clearHistory();
            setDisplay(result);
        }
    }

    //---------- ADVANCED OPERATIONS BUTTONS ----------------

    public void percentBtnClick(){

        setWasNumber(false);
    } //TO DO

    public void sqrtBtnClick(){

        setWasNumber(false);
    } //TO DO

    public void sqrBtnClick(){

        setWasNumber(false);
    } //TO DO

    public void fractionBtnOneClick(){

        setWasNumber(false);
    } //TO DO

    public void negateBtnClick(){

        setWasNumber(true);
    } //TO DO

    public void clearAllMemoryBtnClick(){

    } //TO DO

    public void memoryRecallBtnClick(){

    } //TO DO

    public void memoryAddBtnClick(){

    } //TO DO

    public void memorySubtractBtnClick(){

    } //TO DO

    public void memoryStoreBtnClick(){

    } //TO DO

    public void memoryBtnClick(){

    } //TO DO

    // -------- SUPPORT METHODS -----------------------
    void addNumberToDisplay(String value){

        if (wasNumber && isZeroNotFirst()){
            String current = display.getText();
            if (current.length()<21){
                if (current.length()>2 && isComaAbsent(current) && !COMA.equals(value)) {
                    current = removeSpaces(current);
                    current = addSpaces(current,0);
                }
                display.setText(current+value);
            }
        }else {
            display.setText(value);
        }
    }

    void putZeroToDisplay(){

        if (wasNumber){
            String currentStr = display.getText();
            if (!ZERO.equals(currentStr)){
                setDisplay(currentStr+ZERO);
            }
        }else {
            setDisplay(ZERO);
        }

    }

    void putComaToDisplay(){

            String currentStr = display.getText();
            if (isComaAbsent(currentStr)&&wasNumber){
                if (ZERO.equals(currentStr)){
                    setDisplay(ZERO+COMA);
                }else {
                    setDisplay(currentStr+COMA);
                }
            }else if (wasOperationBefore){
                setDisplay(ZERO+COMA);
            }
            setWasNumber(true);
    }

    void setWasNumber(boolean val){

        wasNumber = val;
    }

    void setWasOperationBefore(boolean val){
        wasOperationBefore = val;
    }

    boolean isZeroNotFirst(){

        return !display.getText().matches("0");
    }

    void setDisplay(String string){

        display.setText(string);
    }

    void setHistoryText(String string){
        historyText.setText(historyText.getText()+string);
    }

    void clearHistory(){
        historyText.setText("");
    }

    void addOperationToHistory(MainOperation operation){
        if (wasNumber){
            setHistoryText(optimizeString(display.getText())+operation.getValue());
        }else {
            changeOperationAtHistory(operation);
        }
    }

    void changeOperationAtHistory(MainOperation operation){
        String historyString = historyText.getText();
        if (historyString.length()>0){
            historyText.setText(historyString.substring(0,historyString.length()-3)+operation.getValue());
        }
    }

    String takeDisplayedNumber(){
        return removeSpaces(display.getText());
    }


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        setSizeMainTableColumns();
    }

    void setSizeMainTableColumns(){
        ObservableList<RowConstraints> rows = mainTable.getRowConstraints();
        rows.get(0).setPercentHeight(10);
        rows.get(1).setPercentHeight(10);
        rows.get(2).setPercentHeight(8);
        rows.get(3).setPercentHeight(72);
    }

    void changeStyleTo(String style){

        String object = getClass().getResource(style).toExternalForm();
        rootPane.getStylesheets().add(object);
    }

    String doMainOperation(MainOperation operation,String displayedNumber){

        setLastOperation(operation);
        String result = "";
        if (wasOperationBefore&&wasNumber){
            setSecondNumber(displayedNumber);
            try {
                calculate(isOperationRepeated);
                result = optimizeString(getResult().toString());
            } catch (MyException e) {
                result = e.getMessage();
            }
            setOperationRepeated(true);
        }else {
            setFirstNumber(displayedNumber);
        }
        setEnterRepeated(false);
        setWasNumber(false);
        setWasOperationBefore(true);
        return result;
    }

    String doEnter(String displayedNumber){

        String result = "";
        if (wasOperationBefore){
            if (wasNumber||isOperationRepeated){
                setSecondNumber(displayedNumber);
            }
            try {
                calculate(isEnterRepeated||isOperationRepeated);
                result = optimizeString(getResult().toString());
            } catch (MyException e) {
                result = e.getMessage();
            }
            setEnterRepeated(true);
            setOperationRepeated(false);
            setWasNumber(false);
            setWasOperationBefore(true);
        }else {
            setResult(displayedNumber);
        }
        return result;
    }

    void setEnterRepeated(boolean val){

        isEnterRepeated = val;
    }

    void setOperationRepeated(boolean val){

        isOperationRepeated = val;
    }

    void optimizeDisplay(){

        display.setText(optimizeString(display.getText()));
    }
}

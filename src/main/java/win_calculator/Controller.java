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
import win_calculator.buttons_handlers.ExtraActionButtonsHandler;
import win_calculator.buttons_handlers.MainActionButtonsHandler;
import win_calculator.extra_operations.Percent;
import win_calculator.main_operations.*;

import java.math.BigDecimal;
import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;

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
    private static boolean operationBefore = false;
    private static boolean inputtedNumber = true;
    private static final String ZERO = "0";
    private static final String COMA = ",";
    private final MainActionButtonsHandler mainActnBtnsHandler = new MainActionButtonsHandler();
    private final ExtraActionButtonsHandler extraActnBtnsHandler = new ExtraActionButtonsHandler();
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

        doClearEnteredBtn();
    } //TO DO TESTS

    public void clearBtnClick(){

        doClearBtn();
    }

    public void backspaceBtnClick(){

        doBackspaceBtn();
    } //TO DO TESTS

    // -------- NUMBER BUTTONS -----------------
    public void oneBtnClick(){

        addNumberToDisplay("1");
    }

    public void twoBtnClick(){

        addNumberToDisplay("2");
    }

    public void threeBtnClick(){

        addNumberToDisplay("3");
    }

    public void fourBtnClick(){

        addNumberToDisplay("4");
    }

    public void fiveBtnClick(){

        addNumberToDisplay("5");
    }

    public void sixBtnClick(){

        addNumberToDisplay("6");
    }

    public void sevenBtnClick(){

        addNumberToDisplay("7");
    }

    public void eightBtnClick(){

        addNumberToDisplay("8");
    }

    public void nineBtnClick(){

        addNumberToDisplay("9");
    }

    public void zeroBtnClick(){

        if (isZeroNotFirst()){
            putZeroToDisplay();
        }
    }

    public void comaBtnClick(){

        putComaToDisplay();
    }

    // -------- MAIN OPERATIONS BUTTONS ----------
    public void divideBtnClick(){

        doMainOperationBtn(new Divide());
    }

    public void multiplyBtnClick(){

        doMainOperationBtn(new Multiply());

    }

    public void minusBtnClick(){

        doMainOperationBtn(new Minus());
    }

    public void plusBtnClick(){

        doMainOperationBtn(new Plus());
    }

    public void enterBtnClick(){

        doEnterBtn();
    }

    //---------- ADVANCED OPERATIONS BUTTONS ----------------

    public void percentBtnClick(){

        doPercentBtn();
        setInputtedNumber(false);
    } //TO DO

    public void sqrtBtnClick(){

        setInputtedNumber(false);
    } //TO DO

    public void sqrBtnClick(){

        setInputtedNumber(false);
    } //TO DO

    public void fractionBtnOneClick(){

        setInputtedNumber(false);
    } //TO DO

    public void negateBtnClick(){

        setInputtedNumber(true);
    } //TO DO

    //---------- MEMORY BUTTONS ------------------
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

        if (inputtedNumber && isZeroNotFirst()){
            String current = display.getText();
            if (current.length()<21){
                if (current.length()>2 && isComaAbsent(current) && !COMA.equals(value)) {
                    current = removeCapacity(current);
                    current = addCapacity(current,0);
                }
                display.setText(current+value);
            }
        }else {
            display.setText(value);
        }
        setInputtedNumber(true);
    }

    private void putZeroToDisplay(){

        if (inputtedNumber){
            String currentStr = display.getText();
            if (!ZERO.equals(currentStr)){
                setDisplay(currentStr+ZERO);
            }
        }else {
            setDisplay(ZERO);
        }
        setInputtedNumber(true);
    }

    void putComaToDisplay(){

            String currentStr = display.getText();
            if (isComaAbsent(currentStr)&& inputtedNumber){
                if (ZERO.equals(currentStr)){
                    setDisplay(ZERO+COMA);
                }else {
                    setDisplay(currentStr+COMA);
                }
            }else if (operationBefore){
                setDisplay(ZERO+COMA);
            }
            setInputtedNumber(true);
    }

    public static void setInputtedNumber(boolean val){

        inputtedNumber = val;
    }

    public static boolean wasInputtedNumber(){

        return inputtedNumber;
    }

    public static void setOperationBefore(boolean val){

        operationBefore = val;
    }

    public static boolean wasOperationBefore(){

        return operationBefore;
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

        if (inputtedNumber){
            setHistoryText(removeCapacity(display.getText())+operation.getValue());
        }else {
            changeOperationAtHistory(operation);
        }
    }

    void addExtraOperationToHistory(){

        setHistoryText(removeCapacity(display.getText()));
    }

    void changeOperationAtHistory(MainOperation operation){

        String historyString = historyText.getText();
        if (historyString.length()>0){
            historyText.setText(historyString.substring(0,historyString.length()-3)+operation.getValue());
        }
    }

    private BigDecimal takeDisplayedNumber(){

        return new BigDecimal(replaceComaToDot(removeCapacity(display.getText())));
    }


    @Override
    public void initialize(URL location, ResourceBundle resources) {

        setSizeMainTableColumns();
    }

    private void setSizeMainTableColumns(){

        ObservableList<RowConstraints> rows = mainTable.getRowConstraints();
        rows.get(0).setPercentHeight(10);
        rows.get(1).setPercentHeight(10);
        rows.get(2).setPercentHeight(8);
        rows.get(3).setPercentHeight(72);
    }

    private void changeStyleTo(String style){

        String object = getClass().getResource(style).toExternalForm();
        rootPane.getStylesheets().add(object);
    }

    private void optimizeDisplay(){

        display.setText(optimizeString(display.getText()));
    } //TO DO TESTS

    private void doMainOperationBtn(MainOperation operation){

        addOperationToHistory(operation);
        String result = mainActnBtnsHandler.doOperation(operation,takeDisplayedNumber());
        if (!("".equals(result))){
            setDisplay(result);
        }
        optimizeDisplay();
    }

    private void doEnterBtn(){

        String result = mainActnBtnsHandler.doEnter(takeDisplayedNumber());
        if (!"".equals(result)){
            clearHistory();
            setDisplay(result);
        }
    }

    private void doBackspaceBtn(){

        String currentText = display.getText();
        if (inputtedNumber){
            if (currentText.length()>1){
                display.setText(deleteOneSymbolFromTheEndOf(currentText));
            }else {
                setDisplay(ZERO);
            }
        }
    }

    private void doClearEnteredBtn(){

        setDisplay(ZERO);
        setInputtedNumber(true);
    }

    private void doClearBtn(){

        setDisplay(ZERO);
        clearHistory();
        mainActnBtnsHandler.resetValues();
        setInputtedNumber(true);
        setOperationBefore(false);
    }

    private void doPercentBtn(){

        String result = extraActnBtnsHandler.doOperation(new Percent(), mainActnBtnsHandler.getFirstNumber(),takeDisplayedNumber());
        mainActnBtnsHandler.setSecondNumber(new BigDecimal(result));
        if (!("".equals(result))){
            addExtraOperationToHistory();
            setDisplay(result);
        }
        optimizeDisplay();
    }
}

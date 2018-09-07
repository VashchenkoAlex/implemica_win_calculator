package win_calculator.controller;

import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.RowConstraints;
import javafx.util.Callback;
import win_calculator.DTOs.ResponseDTO;
import win_calculator.controller.nodes.digits.*;
import win_calculator.controller.nodes.digits.Number;
import win_calculator.controller.view_handlers.*;
import win_calculator.exceptions.MyException;
import win_calculator.model.AppModel;
import win_calculator.model.nodes.actions.Action;
import win_calculator.model.nodes.actions.clear.BaskSpace;
import win_calculator.model.nodes.actions.clear.ClearDisplay;
import win_calculator.model.nodes.actions.clear.LastExtraCleaner;
import win_calculator.model.nodes.actions.enter.Enter;
import win_calculator.model.nodes.actions.clear.Clear;
import win_calculator.model.nodes.actions.memory.*;
import win_calculator.model.nodes.actions.extra_operations.*;
import win_calculator.model.nodes.actions.main_operations.*;
import win_calculator.utils.ActionType;
import win_calculator.utils.ComboBoxOption;

import java.math.BigDecimal;
import java.net.URL;
import java.util.ResourceBundle;

import static win_calculator.utils.ActionType.*;

public class FXMLViewController implements Initializable
{

    @FXML
    private AnchorPane rootPane;

    @FXML
    private ComboBox<ComboBoxOption> menuBox;

    @FXML
    private void trimLabelPosition(){
    }

    @FXML
    private GridPane mainTable;

    @FXML
    private Label display;

    @FXML
    private Label historyField;

    @FXML
    private Button fullScreenBtn;

    private AppModel model = new AppModel();
    private HistoryFieldHandler historyFieldHandler = new HistoryFieldHandler();
    private CaptionHandler captionHandler = new CaptionHandler();
    //private StylesHandler stylesHandler = new StylesHandler();
    private DisplayHandler displayHandler = new DisplayHandler();
    private NumberBuilder numberBuilder = new NumberBuilder();
    private Action lastAction;
    private static final String ZERO = "0";

    public void closeBtn(){

        captionHandler.close();
    }

    public void hideBtn(){

        captionHandler.hide();
    }

    public void fullScreenBtnClick(){

        captionHandler.fullScreen();
    }

    public void menuBox(){

    } //TO DO

    public void historyBtn(){

    } //TO DO

    // -------- CLEAR_ENTERED BUTTONS ------------------
    public void clearEnteredBtnClick(){

        makeAction(new ClearDisplay());
    } //TO DO TESTS

    public void clearBtnClick(){

        makeAction(new Clear());
    }

    public void backspaceBtnClick(){

        makeAction(new BaskSpace());
    } //TO DO TESTS

    // -------- DIGIT BUTTONS -----------------
    public void oneBtnClick(){

        makeAction(new OneDigit());
    }

    public void twoBtnClick(){

        makeAction(new TwoDigit());
    }

    public void threeBtnClick(){

        makeAction(new ThreeDigit());
    }

    public void fourBtnClick(){

        makeAction(new FourDigit());
    }

    public void fiveBtnClick(){

        makeAction(new FiveDigit());
    }

    public void sixBtnClick(){

        makeAction(new SixDigit());
    }

    public void sevenBtnClick(){

        makeAction(new SevenDigit());
    }

    public void eightBtnClick(){

        makeAction(new EightDigit());
    }

    public void nineBtnClick(){

        makeAction(new NineDigit());
    }

    public void zeroBtnClick(){

        makeAction(new ZeroDigit());
    }

    public void comaBtnClick(){

        makeAction(new Coma());
        displayHandler.addComa();
    }

    // -------- MAIN OPERATIONS BUTTONS ----------
    public void divideBtnClick(){

        makeAction(new Divide());
    }

    public void multiplyBtnClick(){

        makeAction(new Multiply());
    }

    public void minusBtnClick(){

        makeAction(new Minus());
    }

    public void plusBtnClick(){

        makeAction(new Plus());
    }

    public void enterBtnClick(){

        makeAction(new Enter());
    }

    //---------- ADVANCED OPERATIONS BUTTONS ----------------

    public void percentBtnClick(){

        makeAction(new Percent());
    }

    public void sqrtBtnClick(){

        makeAction(new Sqrt());
    }

    public void sqrBtnClick(){

        makeAction(new Sqr());
    } //TO DO

    public void fractionBtnOneClick(){

        makeAction(new Fraction());
    } //TO DO

    public void negateBtnClick(){

        makeAction(new Negate());
    } //TO DO

    //---------- MEMORY BUTTONS ------------------
    public void clearMemoryBtnClick(){

        makeAction(new ClearMemoryAction());
    } //TO DO

    public void memoryRecallBtnClick(){

        makeAction(new RecallMemory());
    } //TO DO

    public void memoryAddBtnClick(){

        makeAction(new AddToMemoryAction());
    } //TO DO

    public void memorySubtractBtnClick(){

        makeAction(new SubtractMemoryAction());
    } //TO DO

    public void memoryStoreBtnClick(){

        makeAction(new MemoryStoreAction());
    } //TO DO

    public void memoryShowBtnClick(){

        //makeAction(new ShowMemoryAction());
    } //TO DO

    // -------- SUPPORT METHODS -----------------------


    @Override
    public void initialize(URL location, ResourceBundle resources) {

        setSizeMainTableColumns();
        setCellFactoryMenuBox();
        displayHandler.setDisplay(display);
        historyFieldHandler.setHistoryField(historyField);
        captionHandler.setFullScreenBtn(fullScreenBtn);
        captionHandler.setStage(rootPane);
    }

    private void setCellFactoryMenuBox(){
        menuBox.setCellFactory(new Callback<ListView<ComboBoxOption>, ListCell<ComboBoxOption>>() {
            @Override
            public ListCell<ComboBoxOption> call(ListView<ComboBoxOption> param) {
                return new ListCell<ComboBoxOption>(){
                    @Override
                    protected void updateItem(ComboBoxOption item,boolean empty){
                        super.updateItem(item,empty);
                        if (empty){
                            setText("");
                            setGraphic(null);
                        }else {
                            setText(item.getLabel());
                            if (item.isOption().equals("NOT_OPTION")){
                                setId("notOption");
                                setDisable(true);
                            }else{
                                setId("option");
                            }
                        }
                    }
                };
            }
        });
    }

    private void setSizeMainTableColumns(){

        ObservableList<RowConstraints> rows = mainTable.getRowConstraints();
        rows.get(0).setPercentHeight(5);
        rows.get(1).setPercentHeight(5);
        rows.get(2).setPercentHeight(10);
        rows.get(3).setPercentHeight(8);
        rows.get(4).setPercentHeight(77);
    }

    private void makeAction(Action action){

        String history = null;
        try {
            ActionType lastActionType = getLastActionType();
            ResponseDTO response = handleAction(action);
            if (DIGIT.equals(action.getType())){
                if (displayHandler.isNotMax()) {
                    if (ZERO.equals(action.getValue()) && DIGIT.equals(lastActionType)) {
                        displayHandler.addZero();
                    }else {
                        displayHandler.sendNumberToDisplay(response.getDisplayNumber());
                    }
                }
            }else {
                displayHandler.sendNumberToDisplay(response.getDisplayNumber());
            }
            history = response.getHistory();
        }catch (MyException e){
            displayHandler.setDisplayedText(e.getMessage());
        }
        if (history==null){
            history = "";
        }
        historyFieldHandler.setHistoryText(history);
    }

    public ResponseDTO handleAction(Action action) throws MyException {

        ResponseDTO response;
        ActionType type = action.getType();
        if (DIGIT.equals(type)||BACKSPACE.equals(type)|| CLEAR_ENTERED.equals(type)){
            response = handleDigit(action);
        }else {
            response = handleOperation(action);
        }
        return response;
    }

    private ResponseDTO handleOperation(Action action) throws MyException {

        ResponseDTO response;
        Number currentNum = numberBuilder.finish();
        response = model.toDo(action,currentNum);
        if (response.getDisplayNumber() == null){
            response.setDisplayNumber(BigDecimal.ZERO);
        }
        lastAction = action;
        return response;
    }

    private ResponseDTO handleDigit(Action digit){

        ResponseDTO response;
        String historyText = historyFieldHandler.getLastValue();
        if (lastAction!=null && EXTRA_OPERATION.equals(lastAction.getType())){
            try {
                response = model.toDo(new LastExtraCleaner(),null);
                historyText = response.getHistory();
            } catch (MyException e) {
                System.out.println(e.getMessage());
            }
        }
        response = new ResponseDTO(numberBuilder.toDo(digit),historyText);
        lastAction = digit;
        return response;
    }

    private ActionType getLastActionType(){
        ActionType result = CLEAR;
        if (lastAction!=null){
            result = lastAction.getType();
        }
        return result;
    }

    public String getDisplayText() {

        return displayHandler.getDisplayText();
    }
}

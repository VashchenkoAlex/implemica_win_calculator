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
import win_calculator.utils.MemoryType;

import java.math.BigDecimal;
import java.net.URL;
import java.util.ResourceBundle;

import static win_calculator.utils.ActionType.*;
import static win_calculator.utils.MemoryType.STORE;

public class FXMLViewController implements Initializable {

    @FXML
    private AnchorPane rootPane;

    @FXML
    private ComboBox<ComboBoxOption> menuBox;

    @FXML
    private void trimLabelPosition() {
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
    private MemoryHandler memoryHandler = new MemoryHandler();
    private NumberBuilder numberBuilder = new NumberBuilder();
    private ActionType lastActionType;
    private String lastHistoryText;

    public void closeBtn() {

        captionHandler.close();
    }

    public void hideBtn() {

        captionHandler.hide();
    }

    public void fullScreenBtnClick() {

        captionHandler.fullScreen();
    }

    public void menuBox() {

    } //TO DO

    public void historyBtn() {

    } //TO DO

    // -------- CLEAR_ENTERED BUTTONS ------------------
    public void clearEnteredBtnClick() {

        makeAction(new ClearDisplay());
    } //TO DO TESTS

    public void clearBtnClick() {

        makeAction(new Clear());
    }

    public void backspaceBtnClick() {

        makeAction(new BaskSpace());
    } //TO DO TESTS

    // -------- DIGIT BUTTONS -----------------
    public void oneBtnClick() {

        makeAction(new OneDigit());
    }

    public void twoBtnClick() {

        makeAction(new TwoDigit());
    }

    public void threeBtnClick() {

        makeAction(new ThreeDigit());
    }

    public void fourBtnClick() {

        makeAction(new FourDigit());
    }

    public void fiveBtnClick() {

        makeAction(new FiveDigit());
    }

    public void sixBtnClick() {

        makeAction(new SixDigit());
    }

    public void sevenBtnClick() {

        makeAction(new SevenDigit());
    }

    public void eightBtnClick() {

        makeAction(new EightDigit());
    }

    public void nineBtnClick() {

        makeAction(new NineDigit());
    }

    public void zeroBtnClick() {

        makeAction(new ZeroDigit());
    }

    public void comaBtnClick() {

        makeAction(new Coma());
        displayHandler.addComa();
    }

    // -------- MAIN OPERATIONS BUTTONS ----------
    public void divideBtnClick() {

        makeAction(new Divide());
    }

    public void multiplyBtnClick() {

        makeAction(new Multiply());
    }

    public void subtractBtnClick() {

        makeAction(new Subtract());
    }

    public void addBtnClick() {

        makeAction(new Add());
    }

    public void equalsBtnClick() {

        makeAction(new Enter());
    }

    //---------- ADVANCED OPERATIONS BUTTONS ----------------

    public void percentBtnClick() {

        makeAction(new Percent());
    }

    public void sqrtBtnClick() {

        makeAction(new Sqrt());
    }

    public void sqrBtnClick() {

        makeAction(new Sqr());
    }

    public void fractionBtnOneClick() {

        makeAction(new Fraction());
    }

    public void negateBtnClick() {

        makeAction(new Negate());
    }

    //---------- MEMORY BUTTONS ------------------
    public void clearMemoryBtnClick() {

        makeAction(new ClearMemory());
    } //TO DO

    public void memoryRecallBtnClick() {

        makeAction(new RecallMemory());
    } //TO DO

    public void memoryAddBtnClick() {

        makeAction(new AddToMemory());
    } //TO DO

    public void memorySubtractBtnClick() {

        makeAction(new SubtractMemory());
    } //TO DO

    public void memoryStoreBtnClick() {

        makeAction(new StoreMemory());
    } //TO DO

    public void memoryShowBtnClick() {

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

    private void setCellFactoryMenuBox() {
        menuBox.setCellFactory(new Callback<ListView<ComboBoxOption>, ListCell<ComboBoxOption>>() {
            @Override
            public ListCell<ComboBoxOption> call(ListView<ComboBoxOption> param) {
                return new ListCell<ComboBoxOption>() {
                    @Override
                    protected void updateItem(ComboBoxOption item, boolean empty) {
                        super.updateItem(item, empty);
                        if (empty) {
                            setText("");
                            setGraphic(null);
                        } else {
                            setText(item.getLabel());
                            if (item.isOption().equals("NOT_OPTION")) {
                                setId("notOption");
                                setDisable(true);
                            } else {
                                setId("option");
                            }
                        }
                    }
                };
            }
        });
    }

    private void setSizeMainTableColumns() {

        ObservableList<RowConstraints> rows = mainTable.getRowConstraints();
        rows.get(0).setPercentHeight(5);
        rows.get(1).setPercentHeight(5);
        rows.get(2).setPercentHeight(10);
        rows.get(3).setPercentHeight(8);
        rows.get(4).setPercentHeight(77);
    }

    private void makeAction(Action action) {

        String history = null;
        try {
            ActionType previousActionType = lastActionType;
            ResponseDTO response = handleAction(action);
            displayHandler.sendToDisplay(action, response.getDisplayNumber(), previousActionType);
            history = response.getHistory();
        } catch (MyException e) {
            displayHandler.setDisplayedText(e.getMessage());
        }
        historyFieldHandler.setHistoryText(history);
    }

    public ResponseDTO handleAction(Action action) throws MyException {

        ResponseDTO response;
        ActionType type = action.getType();
        if (DIGIT.equals(type)) {
            response = handleDigit(action);
        } else if (CLEAR_ENTERED.equals(type)) {
            response = handleClearEntered(action);
        } else if (BACKSPACE.equals(type)) {
            response = handleBackSpace(action);
        } else if (NEGATE.equals(type) && numberBuilder.containsNumber()) {
            response = handleNegate();
        } else if (MEMORY.equals(type)) {
            response = handleMemory((MemoryAction) action);
        } else {
            response = handleOperation(action);
        }
        lastHistoryText = response.getHistory();
        return response;
    }

    private ResponseDTO handleOperation(Action action) throws MyException {

        Number currentNum;
        if (MEMORY.equals(lastActionType) && numberBuilder.containsNumber()){
            currentNum = numberBuilder.getNumber();
        }else {
            currentNum = numberBuilder.finish();
        }
        ResponseDTO response = model.toDo(action, currentNum);
        if (!NEGATE.equals(action.getType())) {
            numberBuilder.clear();
        }
        if (response.getDisplayNumber() == null) {
            response.setDisplayNumber(BigDecimal.ZERO);
        }
        lastActionType = action.getType();
        return response;
    }

    private ResponseDTO handleDigit(Action digit) {

        ResponseDTO response;
        String historyText = historyFieldHandler.getLastValue();
        if (EXTRA_OPERATION.equals(lastActionType)) {
            try {
                response = model.toDo(new LastExtraCleaner(), null);
                historyText = response.getHistory();
            } catch (MyException e) {
                System.out.println(e.getMessage());
            }
        }
        response = new ResponseDTO(numberBuilder.toDo(digit), historyText);
        lastActionType = digit.getType();
        return response;
    }

    private boolean isBackSpacePossible() {

        return !MAIN_OPERATION.equals(lastActionType) && !EXTRA_OPERATION.equals(lastActionType);
    }

    private ResponseDTO handleMemory(MemoryAction action) {

        MemoryType memoryType = action.getMemoryType();
        Number currentNum = new Number(BigDecimal.ZERO);
        if (STORE.equals(memoryType)) {
            currentNum = numberBuilder.finish();
        }
        Number result = memoryHandler.doAction(action, currentNum);
        if (result != null) {
            currentNum = result;
            numberBuilder.setNumber(result);
        }
        return new ResponseDTO(currentNum.getBigDecimalValue(), lastHistoryText);
    }

    private ResponseDTO handleNegate() {

        numberBuilder.negate(MEMORY.equals(lastActionType));
        return new ResponseDTO(numberBuilder.finish().getBigDecimalValue(), null);
    }

    private ResponseDTO handleBackSpace(Action action) {

        ResponseDTO response;
        if (isBackSpacePossible()) {
            response = handleDigit(action);
        } else {
            response = new ResponseDTO(null, null);
        }
        return response;
    }

    private ResponseDTO handleClearEntered(Action action) throws MyException {

        numberBuilder.clear();
        return model.toDo(action, null);
    }
}

package win_calculator.controller;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.RowConstraints;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.util.Callback;
import win_calculator.DTOs.ResponseDTO;
import win_calculator.controller.view_handlers.*;
import win_calculator.exceptions.MyException;
import win_calculator.model.AppModel;
import win_calculator.model.nodes.actions.Action;
import win_calculator.model.nodes.actions.clear.BaskSpace;
import win_calculator.model.nodes.actions.clear.ClearDisplay;
import win_calculator.model.nodes.actions.clear.LastExtraCleaner;
import win_calculator.model.nodes.actions.digits.Number;
import win_calculator.model.nodes.actions.enter.Enter;
import win_calculator.model.nodes.actions.clear.Clear;
import win_calculator.model.nodes.actions.memory.*;
import win_calculator.model.nodes.actions.digits.*;
import win_calculator.model.nodes.actions.extra_operations.*;
import win_calculator.model.nodes.actions.main_operations.*;
import win_calculator.utils.ActionType;
import win_calculator.utils.ComboBoxOption;

import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;

import static win_calculator.utils.ActionType.*;
import static win_calculator.utils.StringUtils.*;

public class FXMLViewController implements Initializable
{

    @FXML
    private Text menuBoxText;

    @FXML
    private AnchorPane rootPane;

    @FXML
    private ComboBox<ComboBoxOption> menuBox;

    @FXML
    private void trimLabelPosition(ActionEvent event){
    }

    @FXML
    private Pane menuBoxPane;

    @FXML
    private Button menu;

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
    private StylesHandler stylesHandler = new StylesHandler();
    private DisplayHandler displayHandler = new DisplayHandler();
    private NumberBuilder numberBuilder = new NumberBuilder();
    private Action lastAction;

    public void closeBtn(){

        captionHandler.close();
    } //DONE

    public void hideBtn(){

        captionHandler.hide();
    } //DONE

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

    } //TO DO

    public void sqrtBtnClick(){

        makeAction(new Sqrt());
    } //TO DO

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

        String[] results = handleAction(action);
        displayHandler.setDisplayedText(results[0]);
        if (results[1]!=null){
            historyFieldHandler.setHistoryText(results[1]);
        }
    }

    public String[] handleAction(Action action){

        String[] results;
        ActionType type = action.getType();
        if (DIGIT.equals(type)||BACKSPACE.equals(type)|| CLEAR_ENTERED.equals(type)){
            results = handleDigit(action);
        }else {
            results = handleOperation(action);
        }
        return results;
    }

    private String[] handleOperation(Action action){

        String[] results = new String[2];
        Number currentNum = numberBuilder.finish();
        try {
            ResponseDTO response = model.toDo(action,currentNum);
            String responseNumber = response.getDisplayNumber();
            if (responseNumber == null){
                results[0] = "0";
            }else {
                results[0] = cutLastComa(cutLastZeros(replaceDotToComa(responseNumber)));
            }
            results[1] = response.getHistory();
        } catch (MyException e) {
            results[0] = e.getMessage();
        }
        lastAction = action;
        return results;
    }

    private String[] handleDigit(Action action){

        String[] results = new String[2];
        if (lastAction!=null && EXTRA_OPERATION.equals(lastAction.getType())){
            try {
                ResponseDTO responseDTO = model.toDo(new LastExtraCleaner(),null);
                results[1] = responseDTO.getHistory();
            } catch (MyException e) {
                System.out.println(e.getMessage());
            }
        }
        results[0] = addCapacity(replaceDotToComa(numberBuilder.toDo(action)));
        lastAction = action;
        return results;
    }
}

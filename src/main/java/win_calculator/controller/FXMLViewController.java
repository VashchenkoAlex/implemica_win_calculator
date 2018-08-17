package win_calculator.controller;

import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.RowConstraints;
import win_calculator.DTOs.ResponseDTO;
import win_calculator.controller.view_handlers.HistoryFieldHandler;
import win_calculator.exceptions.MyException;
import win_calculator.model.AppModel;
import win_calculator.model.button_handlers.CaptionHandler;
import win_calculator.controller.view_handlers.DisplayHandler;
import win_calculator.model.nodes.actions.Action;
import win_calculator.model.nodes.actions.clear.BaskSpace;
import win_calculator.model.nodes.actions.enter.Enter;
import win_calculator.model.nodes.actions.clear.Clear;
import win_calculator.model.nodes.actions.memory.*;
import win_calculator.model.nodes.actions.numbers.*;
import win_calculator.controller.view_handlers.StylesHandler;
import win_calculator.model.nodes.actions.extra_operations.*;
import win_calculator.model.nodes.actions.main_operations.*;

import java.math.BigDecimal;
import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;

import static win_calculator.utils.StringUtils.optimizeString;

public class FXMLViewController implements Initializable
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
    private Label historyField;

    @FXML
    private Button fullScreenBtn;

    private AppModel model = new AppModel();
    private HistoryFieldHandler historyFieldHandler = new HistoryFieldHandler();
    private CaptionHandler captionHandler = new CaptionHandler();
    private StylesHandler stylesHandler = new StylesHandler();
    private DisplayHandler displayHandler = new DisplayHandler();

    public void closeBtn(){

        captionHandler.close();
    } //DONE

    public void hideBtn(){

        captionHandler.hide();
    } //DONE

    public void fullScreenBtnClick(){

        captionHandler.fullScreen();
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

        displayHandler.clearDisplay();
    } //TO DO TESTS

    public void clearBtnClick(){

        displayHandler.clearDisplay();
        handleAction(new Clear());
    }

    public void backspaceBtnClick(){

        handleAction(new BaskSpace());
    } //TO DO TESTS

    // -------- NUMBER BUTTONS -----------------
    public void oneBtnClick(){

        handleAction(new OneNumber());
    }

    public void twoBtnClick(){

        handleAction(new TwoNumber());
    }

    public void threeBtnClick(){

        handleAction(new ThreeNumber());
    }

    public void fourBtnClick(){

        handleAction(new FourNumber());
    }

    public void fiveBtnClick(){

        handleAction(new FiveNumber());
    }

    public void sixBtnClick(){

        handleAction(new SixNumber());
    }

    public void sevenBtnClick(){

        handleAction(new SevenNumber());
    }

    public void eightBtnClick(){

        handleAction(new EightNumber());
    }

    public void nineBtnClick(){

        handleAction(new NineNumber());
    }

    public void zeroBtnClick(){

        handleAction(new ZeroNumber());
    }

    public void comaBtnClick(){

        handleAction(new Coma());
        displayHandler.addComa();
    }

    // -------- MAIN OPERATIONS BUTTONS ----------
    public void divideBtnClick(){

        handleAction(new Divide());
    }

    public void multiplyBtnClick(){

        handleAction(new Multiply());
    }

    public void minusBtnClick(){

        handleAction(new Minus());
    }

    public void plusBtnClick(){

        handleAction(new Plus());
    }

    public void enterBtnClick(){

        handleAction(new Enter());
    }

    //---------- ADVANCED OPERATIONS BUTTONS ----------------

    public void percentBtnClick(){

        handleAction(new Percent());

    } //TO DO

    public void sqrtBtnClick(){

        handleAction(new Sqrt());
    } //TO DO

    public void sqrBtnClick(){

        handleAction(new Sqr());
    } //TO DO

    public void fractionBtnOneClick(){

        handleAction(new Fraction());
    } //TO DO

    public void negateBtnClick(){

        handleAction(new Negate());
    } //TO DO

    //---------- MEMORY BUTTONS ------------------
    public void clearMemoryBtnClick(){

        handleAction(new ClearMemoryAction());
    } //TO DO

    public void memoryRecallBtnClick(){

        handleAction(new RecallMemory());
    } //TO DO

    public void memoryAddBtnClick(){

        handleAction(new AddToMemoryAction());
    } //TO DO

    public void memorySubtractBtnClick(){

        handleAction(new SubtractMemoryAction());
    } //TO DO

    public void memoryStoreBtnClick(){

        handleAction(new SubtractMemoryAction());
    } //TO DO

    public void memoryShowBtnClick(){

        handleAction(new ShowMemoryAction());
    } //TO DO

    // -------- SUPPORT METHODS -----------------------


    @Override
    public void initialize(URL location, ResourceBundle resources) {

        setSizeMainTableColumns();
        displayHandler.setDisplay(display);
        historyFieldHandler.setHistoryField(historyField);
        captionHandler.setFullScreenBtn(fullScreenBtn);
        captionHandler.setStage(rootPane);
    }

    private void setSizeMainTableColumns(){

        ObservableList<RowConstraints> rows = mainTable.getRowConstraints();
        rows.get(0).setPercentHeight(10);
        rows.get(1).setPercentHeight(10);
        rows.get(2).setPercentHeight(8);
        rows.get(3).setPercentHeight(72);
    }

    private void handleAction(Action action){

        String display;
        try {
            ResponseDTO response = model.toDo(action);
            BigDecimal responseNumber = response.getDisplayNumber();
            if (responseNumber == null){
                display = "0";
            }else {
                display = optimizeString(responseNumber.toString());
            }
            historyFieldHandler.setHistoryText(response.getHistory());
        } catch (MyException e) {
            display = e.getMessage();
        }
        displayHandler.setDisplayedText(display);
    }
}

package win_calculator.controller;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.RowConstraints;
import javafx.util.Callback;
import javafx.util.Duration;
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
import win_calculator.utils.MenuListOption;
import win_calculator.utils.MemoryType;

import java.math.BigDecimal;
import java.net.URL;
import java.util.Arrays;
import java.util.List;
import java.util.ResourceBundle;

import static win_calculator.utils.ActionType.*;
import static win_calculator.utils.MemoryType.ADD_TO_MEMORY;
import static win_calculator.utils.MemoryType.STORE;
import static win_calculator.utils.MemoryType.SUBTRACT_FROM_MEMORY;
import static win_calculator.utils.MenuListOption.*;

public class FXMLViewController implements Initializable {

    @FXML
    private AnchorPane rootPane;

    @FXML
    private GridPane mainTable;

    @FXML
    private Label display;

    @FXML
    private Label historyField;

    @FXML
    private Button fullScreenBtn;

    @FXML
    private Button memoryRecallBtn;

    @FXML
    private Button clearAllMemoryBtn;

    @FXML
    private Button memoryShowBtn;

    @FXML
    private AnchorPane menuListContainer;

    private AppModel model = new AppModel();
    private HistoryFieldHandler historyFieldHandler = new HistoryFieldHandler();
    private CaptionHandler captionHandler = new CaptionHandler();
    private DisplayHandler displayHandler = new DisplayHandler();
    private MemoryHandler memoryHandler = new MemoryHandler();
    private NumberBuilder numberBuilder = new NumberBuilder();
    private ActionType lastActionType;
    private String lastHistoryText;
    private BigDecimal lastDisplayNumber;
    private static final double MENU_LIST_WIDTH = 260;

    @FXML
    private void dropMenu() {

        ListView<MenuListOption> menuListView = prepareMenuListView();
        ObservableList<Node> menuNodes = menuListContainer.getChildren();
        menuNodes.add(prepareBackground());
        menuNodes.add(menuListView);
        menuNodes.add(prepareMenuBtn());
        Button aboutBtn = prepareAboutBtn();
        menuNodes.add(aboutBtn);
        setEmergentList(menuListView,aboutBtn);
    }

    public void closeBtn() {

        captionHandler.close();
    }

    public void hideBtn() {

        captionHandler.hide();
    }

    public void fullScreenBtnClick() {

        captionHandler.fullScreen();
    }

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
        disableMemoryButtons();
    }

    public void memoryRecallBtnClick() {

        makeAction(new RecallMemory());
    }

    public void memoryAddBtnClick() {

        makeAction(new AddToMemory());
        activateMemoryButtons();
    }

    public void memorySubtractBtnClick() {

        makeAction(new SubtractMemory());
        activateMemoryButtons();
    }

    public void memoryStoreBtnClick() {

        makeAction(new StoreMemory());
        activateMemoryButtons();
    }

    public void memoryShowBtnClick() {

        //makeAction(new ShowMemoryAction());
    }

    // -------- SUPPORT METHODS -----------------------


    @Override
    public void initialize(URL location, ResourceBundle resources) {

        disableMemoryButtons();
        setSizeMainTableColumns();
        displayHandler.setDisplay(display);
        historyFieldHandler.setHistoryField(historyField);
        captionHandler.setFullScreenBtn(fullScreenBtn);
        captionHandler.setStage(rootPane);
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
            response = handleMemory((MemoryAction) action,lastDisplayNumber);
        } else {
            response = handleOperation(action);
        }
        lastDisplayNumber = response.getBigdecimalNumber();
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
        lastActionType = action.getType();
        return response;
    }

    private ResponseDTO handleDigit(Action digit) {

        ResponseDTO response;
        String historyText = lastHistoryText;
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

        return !MAIN_OPERATION.equals(lastActionType) && !EXTRA_OPERATION.equals(lastActionType) && !ENTER.equals(lastActionType);
    }

    private ResponseDTO handleMemory(MemoryAction action, BigDecimal number) {

        MemoryType memoryType = action.getMemoryType();
        BigDecimal currentNum = BigDecimal.ZERO;
        if (number!=null){
            currentNum = number;
        }
        if ((STORE.equals(memoryType) || SUBTRACT_FROM_MEMORY.equals(memoryType) || ADD_TO_MEMORY.equals(memoryType))&& numberBuilder.containsNumber()) {
            currentNum = numberBuilder.finish().getBigDecimalValue();
        }
        BigDecimal result = memoryHandler.doAction(action, currentNum);
        if (result != null) {
            currentNum = result;
            numberBuilder.setNumber(new Number(result));
        } else {
            currentNum = lastDisplayNumber;
        }
        lastActionType = action.getType();
        return new ResponseDTO(currentNum, lastHistoryText);
    }

    private ResponseDTO handleNegate() {

        numberBuilder.negate(MEMORY.equals(lastActionType));
        return new ResponseDTO(numberBuilder.finish().getBigDecimalValue(), lastHistoryText);
    }

    private ResponseDTO handleBackSpace(Action action) {

        ResponseDTO response;
        if (isBackSpacePossible()) {
            response = handleDigit(action);
        } else {
            response = new ResponseDTO(lastDisplayNumber, lastHistoryText);
        }
        return response;
    }

    private ResponseDTO handleClearEntered(Action action) throws MyException {

        numberBuilder.clear();
        return model.toDo(action, null);
    }

    private void activateMemoryButtons(){

        memoryRecallBtn.setDisable(false);
        clearAllMemoryBtn.setDisable(false);
        memoryShowBtn.setDisable(false);
    }

    private void disableMemoryButtons(){

        memoryRecallBtn.setDisable(true);
        clearAllMemoryBtn.setDisable(true);
        memoryShowBtn.setDisable(true);
    }

    private ListView<MenuListOption> prepareMenuListView(){

        List<MenuListOption> listOptions = Arrays.asList(CALCULATOR,STANDARD,SCIENTIFIC,PROGRAMMER,
                DATE_CALCULATION,CONVERTER,CURRENCY,VOLUME,LENGTH,WEIGHT_AND_MASS,TEMPERATURE,ENERGY,AREA,
                SPEED,TIME,POWER,DATA,PRESSURE,ANGLE);
        ObservableList<MenuListOption> listOpt = FXCollections.observableList(listOptions);
        ListView<MenuListOption> menuList = new ListView<>(listOpt);
        menuList.setId("droppedList");
        menuList.setCellFactory(new Callback<ListView<MenuListOption>, ListCell<MenuListOption>>() {
            @Override
            public ListCell<MenuListOption> call(ListView<MenuListOption> param) {
                return new ListCell<MenuListOption>() {
                    @Override
                    protected void updateItem(MenuListOption item, boolean empty) {
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
        menuList.setPrefHeight(rootPane.getScene().getHeight() - 33);
        AnchorPane.setTopAnchor(menuList,32.0);
        AnchorPane.setLeftAnchor(menuList,2.0);

        return menuList;
    }

    private void setEmergentList(ListView<MenuListOption> menuListView,Button button){

        Timeline timeline = new Timeline(new KeyFrame(Duration.millis(2), new EventHandler<ActionEvent>() {
            private double i = 1;

            @Override
            public void handle(ActionEvent event) {
                button.setMinWidth(i);
                button.setPrefWidth(i);
                menuListView.setMinWidth(i);
                menuListView.setPrefWidth(i);
                i += 4;
            }
        }));
        timeline.setOnFinished((actionEvent) -> {
            button.setPrefWidth(MENU_LIST_WIDTH);
            menuListView.setPrefWidth(MENU_LIST_WIDTH);
        });
        timeline.setCycleCount(65);
        timeline.play();
    }

    private Button prepareMenuBtn(){

        Button menuBtn = new Button("\uE700");
        menuBtn.setId("menuBtnPressed");
        menuBtn.setPrefSize(44,44);
        menuBtn.setOnAction((actionEvent)-> hideMenuList());
        AnchorPane.setTopAnchor(menuBtn,36.0);
        AnchorPane.setLeftAnchor(menuBtn,1.0);
        return menuBtn;
    }

    private Button prepareAboutBtn(){

        Button aboutBtn = new Button("\uE946   About");
        aboutBtn.setId("aboutBtn");
        aboutBtn.setPrefSize(MENU_LIST_WIDTH,40);
        aboutBtn.setAlignment(Pos.CENTER_LEFT);
        AnchorPane.setBottomAnchor(aboutBtn,10.0);
        AnchorPane.setLeftAnchor(aboutBtn,2.0);
        return aboutBtn;
    }
    private Pane prepareBackground(){

        double height = rootPane.getHeight();
        double width = rootPane.getWidth();
        Pane pane = new Pane();
        pane.setPrefSize(width,height);
        pane.setOpacity(0.01);
        pane.setOnMouseClicked((event)-> hideMenuList());
        return pane;
    }

    private void hideMenuList(){

        ObservableList<Node> nodes = menuListContainer.getChildren();
        for (int i = nodes.size()-1; i >= 0; i--) {
            nodes.remove(i);
        }
    }
}

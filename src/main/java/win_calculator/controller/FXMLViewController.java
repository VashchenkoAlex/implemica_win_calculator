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
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.RowConstraints;
import javafx.stage.Window;
import javafx.util.Callback;
import javafx.util.Duration;
import win_calculator.controller.memory.*;
import win_calculator.model.DTOs.ResponseDTO;
import win_calculator.controller.digits.*;
import win_calculator.model.nodes.actions.Number;
import win_calculator.controller.view_handlers.*;
import win_calculator.model.AppModel;
import win_calculator.model.nodes.actions.Action;
import win_calculator.model.nodes.actions.clear.BaskSpace;
import win_calculator.model.nodes.actions.clear.ClearDisplay;
import win_calculator.model.nodes.actions.clear.LastExtraCleaner;
import win_calculator.model.nodes.actions.enter.Enter;
import win_calculator.model.nodes.actions.clear.Clear;
import win_calculator.model.nodes.actions.extra_operations.*;
import win_calculator.model.nodes.actions.main_operations.*;
import win_calculator.model.nodes.actions.ActionType;
import win_calculator.controller.enums.MenuListOption;

import java.math.BigDecimal;
import java.net.URL;
import java.util.Arrays;
import java.util.List;
import java.util.ResourceBundle;

import static win_calculator.controller.utils.ControllerUtils.isNotNumber;
import static win_calculator.controller.utils.ControllerUtils.replaceCapacity;
import static win_calculator.controller.utils.ControllerUtils.replaceComaToDot;
import static win_calculator.model.nodes.actions.ActionType.*;
import static win_calculator.model.utils.ModelUtils.*;
import static win_calculator.controller.memory.MemoryType.ADD_TO_MEMORY;
import static win_calculator.controller.memory.MemoryType.STORE;
import static win_calculator.controller.memory.MemoryType.SUBTRACT_FROM_MEMORY;
import static win_calculator.controller.enums.MenuListOption.*;

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
    private Button memorySubtractBtn;

    @FXML
    private Button memoryAddBtn;

    @FXML
    private Button memoryStoreBtn;

    @FXML
    private Button memoryShowBtn;

    @FXML
    private AnchorPane dropDownContainer;

    @FXML
    private ScrollPane historyScroll;

    @FXML
    private Button percentBtn;

    @FXML
    private Button sqrtBtn;

    @FXML
    private Button sqrBtn;

    @FXML
    private Button fractionBtn;

    @FXML
    private Button divideBtn;

    @FXML
    private Button multiplyBtn;

    @FXML
    private Button subtractBtn;

    @FXML
    private Button addBtn;

    @FXML
    private Button negateBtn;

    @FXML
    private Button comaBtn;

    @FXML
    private Button dragBtn;

    @FXML
    private GridPane mainButtonsGrid;

    private AppModel model = new AppModel();
    private HistoryFieldHandler historyFieldHandler = new HistoryFieldHandler();
    private CaptionHandler captionHandler = new CaptionHandler();
    private DisplayHandler displayHandler = new DisplayHandler();
    private MemoryHandler memoryHandler = new MemoryHandler();
    private NumberBuilder numberBuilder = new NumberBuilder();
    private ActionType lastActionType;
    private String lastHistoryText;
    private String lastDisplay;
    private double xOffset = 0;
    private double yOffset = 0;
    private static final double MENU_LIST_WIDTH = 260;
    private static final String DISPLAY_PATTERN = "#############,###.################";

    @FXML
    private void dropMenu() {

        ListView<MenuListOption> menuListView = prepareMenuListView();
        ObservableList<Node> menuNodes = dropDownContainer.getChildren();
        menuNodes.add(prepareBackground());
        menuNodes.add(menuListView);
        menuNodes.add(prepareMenuBtn());
        Button aboutBtn = prepareAboutBtn();
        menuNodes.add(aboutBtn);
        setEmergentList(menuListView, aboutBtn);
    }

    public void closeBtn() {

        CaptionHandler.getInstance().close();
    }

    public void hideBtn() {

        captionHandler.hide();
    }

    public void fullScreenBtnClick() {

        captionHandler.fullScreen();
    }

    public void historyBtn() {

        ObservableList<Node> menuNodes = dropDownContainer.getChildren();
        menuNodes.add(prepareBackground());
        menuNodes.add(prepareDropDownLabel("History Label", "historyPane"));
    }

    public void clearEnteredBtnClick() {

        makeAction(new ClearDisplay());
    }

    public void clearBtnClick() {

        makeAction(new Clear());
    }

    public void backspaceBtnClick() {

        makeAction(new BaskSpace());
    }

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

    public void clearMemoryBtnClick() {

        makeAction(new ClearMemory());
        setDisableMemoryButtons(true);
    }

    public void memoryRecallBtnClick() {

        makeAction(new RecallMemory());
    }

    public void memoryAddBtnClick() {

        makeAction(new AddToMemory());
        setDisableMemoryButtons(false);
    }

    public void memorySubtractBtnClick() {

        makeAction(new SubtractMemory());
        setDisableMemoryButtons(false);
    }

    public void memoryStoreBtnClick() {

        makeAction(new StoreMemory());
        setDisableMemoryButtons(false);
    }

    public void memoryShowBtnClick() {

        ObservableList<Node> menuNodes = dropDownContainer.getChildren();
        menuNodes.add(prepareBackground());
        menuNodes.add(prepareDropDownLabel(memoryHandler.doAction(new RecallMemory(),null).toString(), "memoryPane"));
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        setDragEvent();
        setDisableMemoryButtons(true);
        setSizeMainTableColumns();
        displayHandler.setDisplay(display);
        historyFieldHandler.setHistoryField(historyField, historyScroll);
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

        setDisableOperationButtons(false);
        ActionType previousActionType = lastActionType;
        ResponseDTO response = handleAction(action);
        displayHandler.sendToDisplay(action, response.getDisplay(), previousActionType);
        if (isNotNumber(response.getDisplay())) {
            setDisableOperationButtons(true);
            historyFieldHandler.setHistoryText(response.getHistory());

        } else if (CLEAR.equals(lastActionType) || ENTER.equals(lastActionType)) {
            historyFieldHandler.clear();
        } else {
            historyFieldHandler.setHistoryText(response.getHistory());
        }
    }

    public ResponseDTO handleAction(Action action) {

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
            response = handleMemory((MemoryAction) action, lastDisplay);
        } else {
            response = handleOperation(action);
        }
        lastDisplay = response.getDisplay();
        lastHistoryText = response.getHistory();
        return response;
    }

    private ResponseDTO handleOperation(Action action) {

        Number currentNum;
        if (MEMORY.equals(lastActionType) && numberBuilder.containsNumber()) {
            currentNum = numberBuilder.getNumber();
        } else {
            currentNum = numberBuilder.finish();
        }
        ResponseDTO response = model.toDo(currentNum, action);
        if (!NEGATE.equals(action.getType())) {
            numberBuilder.clear();
        }
        lastActionType = action.getType();
        return response;
    }

    private ResponseDTO handleDigit(Action digit) {

        ResponseDTO response;
        if (lastDisplay != null && isNotNumber(lastDisplay)) {
            lastHistoryText = "";
        }
        String historyText = lastHistoryText;
        if (EXTRA_OPERATION.equals(lastActionType)) {
            response = model.toDo(null, new LastExtraCleaner());
            historyText = response.getHistory();
        }
        response = new ResponseDTO(numberBuilder.toDo(digit), historyText);
        lastActionType = digit.getType();
        return response;
    }

    private boolean isBackSpacePossible() {

        return !MAIN_OPERATION.equals(lastActionType)
                && !EXTRA_OPERATION.equals(lastActionType)
                && !ENTER.equals(lastActionType)
                && numberBuilder.containsNumber();
    }

    private ResponseDTO handleMemory(MemoryAction action, String number) {

        MemoryType memoryType = action.getMemoryType();
        String currentNum = "";
        if ((STORE.equals(memoryType) || SUBTRACT_FROM_MEMORY.equals(memoryType) || ADD_TO_MEMORY.equals(memoryType)) && numberBuilder.containsNumber()) {
            currentNum = numberBuilder.convertChainToString();
            numberBuilder.finish();
        }
        if ("".equals(currentNum) && number != null) {
            currentNum = number;
        }
        if ("".equals(currentNum) && numberBuilder.containsNumber()) {
            currentNum = numberBuilder.getNumber().getValue();
        }
        if ("".equals(currentNum)) {
            currentNum = "0";
        }
        BigDecimal result = memoryHandler.doAction(action, new BigDecimal(replaceCapacity(replaceComaToDot(currentNum))));
        if (result != null) {
            currentNum = convertToString(result, DISPLAY_PATTERN);
            numberBuilder.setNumber(new Number(result));
        } else {
            currentNum = lastDisplay;
        }
        lastActionType = action.getType();
        return new ResponseDTO(currentNum, lastHistoryText);
    }

    private ResponseDTO handleNegate() {

        return new ResponseDTO(numberBuilder.negate(MEMORY.equals(lastActionType)), lastHistoryText);
    }

    private ResponseDTO handleBackSpace(Action action) {

        ResponseDTO response;
        if (isBackSpacePossible()) {
            response = handleDigit(action);
        } else {
            if (lastDisplay == null || isNotNumber(lastDisplay)) {
                lastDisplay = "0";
            }
            response = new ResponseDTO(lastDisplay, lastHistoryText);
        }
        return response;
    }

    private ResponseDTO handleClearEntered(Action action) {

        numberBuilder.clear();
        return model.toDo(null, action);
    }

    private void setDisableMemoryButtons(boolean val) {

        memoryRecallBtn.setDisable(val);
        clearAllMemoryBtn.setDisable(val);
        memoryShowBtn.setDisable(val);
    }

    private void setDisableOperationButtons(boolean value) {

        memoryAddBtn.setDisable(value);
        memorySubtractBtn.setDisable(value);
        memoryStoreBtn.setDisable(value);
        percentBtn.setDisable(value);
        sqrtBtn.setDisable(value);
        sqrBtn.setDisable(value);
        fractionBtn.setDisable(value);
        divideBtn.setDisable(value);
        multiplyBtn.setDisable(value);
        subtractBtn.setDisable(value);
        addBtn.setDisable(value);
        negateBtn.setDisable(value);
        comaBtn.setDisable(value);
    }

    private ListView<MenuListOption> prepareMenuListView() {

        List<MenuListOption> listOptions = Arrays.asList(CALCULATOR, STANDARD, SCIENTIFIC, PROGRAMMER,
                DATE_CALCULATION, CONVERTER, CURRENCY, VOLUME, LENGTH, WEIGHT_AND_MASS, TEMPERATURE, ENERGY, AREA,
                SPEED, TIME, POWER, DATA, PRESSURE, ANGLE);
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
        AnchorPane.setTopAnchor(menuList, 32.0);
        AnchorPane.setLeftAnchor(menuList, 2.0);

        return menuList;
    }

    private void setEmergentList(ListView<MenuListOption> menuListView, Button button) {

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

    private Button prepareMenuBtn() {

        Button menuBtn = new Button("\uE700");
        menuBtn.setId("menuBtnPressed");
        menuBtn.setPrefSize(42, 42);
        menuBtn.setOnAction((actionEvent) -> hideDropDown());
        AnchorPane.setTopAnchor(menuBtn, 36.0);
        AnchorPane.setLeftAnchor(menuBtn, 1.0);
        return menuBtn;
    }

    private Button prepareAboutBtn() {

        Button aboutBtn = new Button("\uE946   About");
        aboutBtn.setId("aboutBtn");
        aboutBtn.setPrefSize(MENU_LIST_WIDTH, 40);
        aboutBtn.setAlignment(Pos.CENTER_LEFT);
        AnchorPane.setBottomAnchor(aboutBtn, 10.0);
        AnchorPane.setLeftAnchor(aboutBtn, 2.0);
        return aboutBtn;
    }

    private Pane prepareBackground() {

        double height = rootPane.getHeight();
        double width = rootPane.getWidth();
        Pane pane = new Pane();
        pane.setPrefSize(width, height);
        pane.setOpacity(0.01);
        pane.setOnMouseClicked((event) -> hideDropDown());
        return pane;
    }

    private void hideDropDown() {

        ObservableList<Node> nodes = dropDownContainer.getChildren();
        for (int i = nodes.size() - 1; i >= 0; i--) {
            nodes.remove(i);
        }
    }

    private void setDragEvent() {

        dragBtn.setOnMousePressed(event -> {
            xOffset = event.getSceneX();
            yOffset = event.getSceneY();
        });

        dragBtn.setOnMouseDragged(event -> {
            Window window = rootPane.getScene().getWindow();
            window.setX(event.getScreenX() - xOffset);
            window.setY(event.getScreenY() - yOffset);

        });

        dragBtn.setOnMouseReleased(event -> {
            if (checkOnOverScreen(event)) {
                captionHandler.fullScreen();
            }
        });
    }

    private boolean checkOnOverScreen(MouseEvent event) {

        double x = event.getScreenY();
        double y = event.getScreenX();
        boolean result = false;
        if (x < 1 || y < 1) {
            result = true;
        }
        return result;
    }

    private Label prepareDropDownLabel(String text, String id) {

        Label label = new Label(text);
        label.setId(id);
        AnchorPane.setBottomAnchor(label, 0.0);
        AnchorPane.setLeftAnchor(label, 2.0);
        label.setPrefSize(rootPane.getWidth() - 4, mainButtonsGrid.getHeight());
        return label;
    }
}

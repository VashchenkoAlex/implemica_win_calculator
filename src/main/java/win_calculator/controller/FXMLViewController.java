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
import win_calculator.model.exceptions.*;
import win_calculator.controller.digits.*;
import win_calculator.controller.view_handlers.*;
import win_calculator.model.CalcModel;
import win_calculator.model.memory.*;
import win_calculator.model.nodes.events.Event;
import win_calculator.model.nodes.events.clear.BaskSpace;
import win_calculator.model.nodes.events.clear.ClearDisplay;
import win_calculator.model.nodes.events.clear.LastExtraCleaner;
import win_calculator.model.nodes.events.enter.Enter;
import win_calculator.model.nodes.events.clear.Clear;
import win_calculator.model.nodes.events.extra_operations.*;
import win_calculator.model.nodes.events.main_operations.*;
import win_calculator.model.nodes.events.EventType;
import win_calculator.controller.enums.MenuListOption;

import java.math.BigDecimal;
import java.net.URL;
import java.util.Arrays;
import java.util.List;
import java.util.ResourceBundle;

import static win_calculator.controller.utils.CalculatorUtils.*;
import static win_calculator.model.memory.MemoryType.STORE;
import static win_calculator.model.nodes.events.EventType.*;
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

    private CalcModel model = new CalcModel();
    private HistoryFieldHandler historyFieldHandler = new HistoryFieldHandler();
    private CaptionHandler captionHandler = new CaptionHandler();
    private DisplayHandler displayHandler = new DisplayHandler();
    private NumberBuilder numberBuilder = new NumberBuilder();
    private EventType lastEventType;
    private String lastHistoryText;
    private String lastDisplay;
    private double xOffset = 0;
    private double yOffset = 0;
    private boolean wasExeption = false;
    private static final double MENU_LIST_WIDTH = 260;
    private static final String DISPLAY_PATTERN = "#############,###.################";
    private static final String NEGATIVE_VALUE_FOR_SQRT_MSG = "Invalid input";
    private static final String OVERFLOW_MSG = "Overflow";
    private static final String DIVIDE_BY_ZERO_EXCEPTION_MSG = "Cannot divide by zero";
    private static final String UNDEFINED_EXCEPTION_MSG = "Result is undefined";


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

        makeEvent(new ClearDisplay());
    }

    public void clearBtnClick() {

        makeEvent(new Clear());
    }

    public void backspaceBtnClick() {

        makeEvent(new BaskSpace());
    }

    public void oneBtnClick() {

        makeEvent(new OneDigit());
    }

    public void twoBtnClick() {

        makeEvent(new TwoDigit());
    }

    public void threeBtnClick() {

        makeEvent(new ThreeDigit());
    }

    public void fourBtnClick() {

        makeEvent(new FourDigit());
    }

    public void fiveBtnClick() {

        makeEvent(new FiveDigit());
    }

    public void sixBtnClick() {

        makeEvent(new SixDigit());
    }

    public void sevenBtnClick() {

        makeEvent(new SevenDigit());
    }

    public void eightBtnClick() {

        makeEvent(new EightDigit());
    }

    public void nineBtnClick() {

        makeEvent(new NineDigit());
    }

    public void zeroBtnClick() {

        makeEvent(new ZeroDigit());
    }

    public void comaBtnClick() {

        makeEvent(new Coma());
        displayHandler.addComa();
    }

    public void divideBtnClick() {

        makeEvent(new Divide());
    }

    public void multiplyBtnClick() {

        makeEvent(new Multiply());
    }

    public void subtractBtnClick() {

        makeEvent(new Subtract());
    }

    public void addBtnClick() {

        makeEvent(new Add());
    }

    public void equalsBtnClick() {

        makeEvent(new Enter());
    }

    public void percentBtnClick() {

        makeEvent(new Percent());
    }

    public void sqrtBtnClick() {

        makeEvent(new Sqrt());
    }

    public void sqrBtnClick() {

        makeEvent(new Sqr());
    }

    public void fractionBtnOneClick() {

        makeEvent(new Fraction());
    }

    public void negateBtnClick() {

        makeEvent(new Negate());
    }

    public void clearMemoryBtnClick() {

        makeEvent(new ClearMemory());
        setDisableMemoryButtons(true);
    }

    public void memoryRecallBtnClick() {

        makeEvent(new RecallMemory());
    }

    public void memoryAddBtnClick() {

        makeEvent(new AddToMemory());
        setDisableMemoryButtons(false);
    }

    public void memorySubtractBtnClick() {

        makeEvent(new SubtractMemory());
        setDisableMemoryButtons(false);
    }

    public void memoryStoreBtnClick() {

        makeEvent(new StoreMemory());
        setDisableMemoryButtons(false);
    }

    public void memoryShowBtnClick() {

        ObservableList<Node> menuNodes = dropDownContainer.getChildren();
        menuNodes.add(prepareBackground());
        String[] response = handleEvent(new RecallMemory());
        menuNodes.add(prepareDropDownLabel(response[0] , "memoryPane"));
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

    private void makeEvent(Event event) {

        setDisableOperationButtons(false);
        EventType previousEventType = lastEventType;
        String[] response = handleEvent(event);
        displayHandler.sendToDisplay(event, response[0], previousEventType);
        if (isNotNumber(response[0])) {
            setDisableOperationButtons(true);
            historyFieldHandler.setHistoryText(response[1]);

        } else if (CLEAR.equals(lastEventType) || ENTER.equals(lastEventType)) {
            historyFieldHandler.clear();
        } else {
            historyFieldHandler.setHistoryText(response[1]);
        }
    }

    public String[] handleEvent(Event event) {

        EventType type = event.getType();
        try {
            if (DIGIT.equals(type)) {
                lastDisplay = handleDigit(event);
            } else if (CLEAR_ENTERED.equals(type)) {
                handleClearEntered(event);
                lastDisplay = "0";
            } else if (BACKSPACE.equals(type)) {
                lastDisplay = handleBackSpace(event);
            } else if (NEGATE.equals(type) && numberBuilder.containsNumber()) {
                lastDisplay = handleNegate();
            } else {
                lastDisplay = convertToString(handleOperation(event), DISPLAY_PATTERN);
            }
        } catch (OperationException e) {
            lastDisplay = selectMessageAfterException(e.getType());
            wasExeption = true;
        }
        if (wasExeption && isEventTypeResettingOverflow(type)) {
            lastHistoryText = "";
            wasExeption = false;
        } else {
            lastHistoryText = getHistoryString(model.getHistory());
        }

        lastEventType = event.getType();
        return new String[]{lastDisplay, lastHistoryText};
    }

    private BigDecimal handleOperation(Event event) throws OperationException {

        BigDecimal currentNum = numberBuilder.finish();
        BigDecimal result = model.toDo(currentNum, event);
        if (MEMORY.equals(event.getType())) {
            MemoryType memoryType = ((MemoryEvent) event).getMemoryType();
            if (!STORE.equals(memoryType)){
                numberBuilder.clear();
                numberBuilder.setNumber(result);
            } else if (currentNum != null){
                numberBuilder.setNumber(currentNum);
                result = currentNum;
            }
        } else if (!NEGATE.equals(event.getType()) && !MEMORY.equals(event.getType())) {
            numberBuilder.clear();
        }
        if (ENTER.equals(event.getType())) {
            lastHistoryText = "";
        }
        return result;
    }

    private String handleDigit(Event digit) throws OperationException {

        String response;
        if (lastDisplay != null && isNotNumber(lastDisplay)) {
            lastHistoryText = "";
            numberBuilder.clear();
        }
        if (EXTRA_OPERATION.equals(lastEventType)) {
            model.toDo(null, new LastExtraCleaner());
            lastHistoryText = getHistoryString(model.getHistory());
        }
        response = numberBuilder.toDo(digit);
        return response;
    }

    private boolean isBackSpacePossible() {

        return !MAIN_OPERATION.equals(lastEventType)
                && !EXTRA_OPERATION.equals(lastEventType)
                && !ENTER.equals(lastEventType)
                && numberBuilder.containsNumber();
    }

    private String handleNegate() {

        return numberBuilder.negate(MEMORY.equals(lastEventType));
    }

    private String handleBackSpace(Event event) throws OperationException {

        String response;
        if (isBackSpacePossible()) {
            response = handleDigit(event);
        } else {
            if (lastDisplay == null || isNotNumber(lastDisplay)) {
                lastDisplay = "0";
            }
            response = lastDisplay;
        }
        return response;
    }

    private void handleClearEntered(Event event) throws OperationException {

        numberBuilder.clear();
        model.toDo(null, event);
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

    private boolean isEventTypeResettingOverflow(EventType type) {

        return BACKSPACE.equals(type) || CLEAR.equals(type) || DIGIT.equals(type) || CLEAR_ENTERED.equals(type) || (ENTER.equals(lastEventType) && ENTER.equals(type));
    }

    private String selectMessageAfterException(ExceptionType type){

        String message = "";
        switch (type){
            case OVERFLOW: {
                message = OVERFLOW_MSG;
                break;
            }
            case DIVIDE_BY_ZERO: {
                message = DIVIDE_BY_ZERO_EXCEPTION_MSG;
                break;
            }
            case UNDEFINED_RESULT: {
                message = UNDEFINED_EXCEPTION_MSG;
                break;
            }
            case NEGATIVE_VALUE_FOR_SQRT: {
                message = NEGATIVE_VALUE_FOR_SQRT_MSG;
                break;
            }
        }
        return message;
    }
}

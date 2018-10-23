package win_calculator.view;

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
import win_calculator.controller.CalcController;
import win_calculator.controller.entities.NumberSymbol;
import win_calculator.model.operations.percent.Percent;
import win_calculator.model.operations.extra_operations.*;
import win_calculator.model.operations.binary_operations.Add;
import win_calculator.model.operations.binary_operations.Divide;
import win_calculator.model.operations.binary_operations.Multiply;
import win_calculator.model.operations.binary_operations.Subtract;
import win_calculator.model.operations.Operation;
import win_calculator.model.operations.backspace.BaskSpace;
import win_calculator.model.operations.clear.ClearEntered;
import win_calculator.model.operations.equal.Equal;
import win_calculator.model.operations.clear.Clear;
import win_calculator.model.operations.OperationType;
import win_calculator.view.enums.MenuListOption;
import win_calculator.model.operations.memory_operations.*;
import win_calculator.view.containers.WindowContainer;
import win_calculator.view.containers.DisplayFieldContainer;
import win_calculator.view.containers.HistoryFieldContainer;

import java.net.URL;
import java.util.Arrays;
import java.util.List;
import java.util.ResourceBundle;

import static win_calculator.controller.entities.Symbol.*;
import static win_calculator.controller.utils.ControllerUtils.*;
import static win_calculator.model.operations.OperationType.*;
import static win_calculator.model.operations.memory_operations.MemoryOperationType.*;
import static win_calculator.view.enums.MenuListOption.*;

/**
 * Initializes FXML view, {@link CalcController}, {@link HistoryFieldContainer},
 * {@link WindowContainer} and {@link DisplayFieldContainer}
 * Sets up app display parameters
 */
public class FXMLView implements Initializable {

   /**
    * Constant: default string value for dropped history label
    */
   private static final String DROPPED_HISTORY_TEXT = "History Label";
   /**
    * Constant: id for dropped history label at FXApp
    */
   private static final String DROPPED_HISTORY_ID = "historyPane";
   /**
    * Constant: id for dropped memory label at FXApp
    */
   private static final String DROPPED_MEMORY_ID = "memoryPane";
   /**
    * Constant: id for dropped menu list at FXApp
    */
   private static final String DROPPED_LIST_ID = "droppedList";
   /**
    * Constant: id for menu button after press event on shown menu button at FXApp
    */
   private static final String PRESSED_MENU_BTN_ID = "menuBtnPressed";
   /**
    * Constant: id for About button at dropped menu list at FXApp
    */
   private static final String ABOUT_BTN_ID = "aboutBtn";
   /**
    * Constant: string value for About button at dropped menu list at FXApp
    */
   private static final String ABOUT_BTN_TEXT = "   About";
   /**
    * Constant: class id for clickable option at dropped menu list at FXApp
    */
   private static final String OPTION_ITEM_ID = "option";
   /**
    * Constant: class id for not clickable option at dropped menu list at FXApp
    */
   private static final String NOT_OPTION_ITEM_ID = "notOption";

   /**
    * Constant: symbol on About button at dropdown list
    */
   private static final String ABOUT_BTN_SYMBOL = "\uE946";
   /**
    * Constant: symbol on Menu button at dropdown list
    */
   private static final String MENU_BTN_SYMBOL = "\uE700";

   /**
    * Constant: double value of dropdown menu width
    */
   private static final double MENU_LIST_WIDTH = 260;
   /**
    * Constant: double value of difference between dropdown menu height
    * and FXApp window
    */
   private static final double MENU_LIST_HEIGHT_DIFFERENCE = 33;
   /**
    * Constant: double value of About button height at dropdown menu
    */
   private static final double ABOUT_BTN_HEIGHT = 40;
   /**
    * Constant: double value of top indent for dropdown menu from border
    */
   private static final double MENU_LIST_TOP_ANCHOR = 32.0;
   /**
    * Constant: double value of top indent for menu button from border
    */
   private static final double MENU_BTN_TOP_ANCHOR = 36.0;
   /**
    * Constant: double value of left indent for dropdown menu from border
    */
   private static final double MENU_LIST_LEFT_ANCHOR = 2.0;
   /**
    * Constant: double value of left indent for menu button from border
    */
   private static final double MENU_BTN_LEFT_ANCHOR = 1.0;
   /**
    * Constant: double value of bottom indent for About button from border
    */
   private static final double ABOUT_BTN_BOTTOM_ANCHOR = 10.0;
   /**
    * Constant: double value of Menu button height and width
    */
   private static final double MENU_BTN_SIZE = 42;
   /**
    * Constant: double value of default opacity for transparent background
    */
   private static final double BACKGROUND_OPACITY = 0.01;
   /**
    * Constant: cycle count for emergent label
    */
   private static final int CYCLE_COUNT = 65;
   /**
    * Constant: Increase size step for emergent menu
    */
   private static final int STEP = 4;

   /**
    * Instance of {@link CalcController}
    */
   private CalcController calcController = new CalcController();
   /**
    * Instance of {@link HistoryFieldContainer}
    */
   private HistoryFieldContainer historyContainer = new HistoryFieldContainer();
   /**
    * Instance of {@link WindowContainer}
    */
   private WindowContainer windowContainer = new WindowContainer();
   /**
    * Instance of {@link DisplayFieldContainer}
    */
   private DisplayFieldContainer displayContainer = new DisplayFieldContainer();

   /**
    * Instance of root pane at FXApp
    */
   @FXML
   private AnchorPane rootPane;
   /**
    * Instance of table with all fields at FXApp
    */
   @FXML
   private GridPane mainTable;
   /**
    * Instance of display number field at FXApp
    */
   @FXML
   private Label display;
   /**
    * Instance of history string label  at FXApp
    */
   @FXML
   private Label historyField;
   /**
    * Instance of full screen button at FXApp
    */
   @FXML
   private Button fullScreenBtn;
   /**
    * Instance of MR button at FXApp
    */
   @FXML
   private Button memoryRecallBtn;
   /**
    * Instance of MC button at FXApp
    */
   @FXML
   private Button clearAllMemoryBtn;
   /**
    * Instance of M- button at FXApp
    */
   @FXML
   private Button memorySubtractBtn;
   /**
    * Instance of M+ button at FXApp
    */
   @FXML
   private Button memoryAddBtn;
   /**
    * Instance of MS button at FXApp
    */
   @FXML
   private Button memoryStoreBtn;
   /**
    * Instance of M button at FXApp
    */
   @FXML
   private Button memoryShowBtn;
   /**
    * Instance of container for dropdown lists and labels at FXApp
    */
   @FXML
   private AnchorPane dropDownContainer;
   /**
    * Instance of history label scroll
    */
   @FXML
   private ScrollPane historyScroll;
   /**
    * Instance of % button at FXApp
    */
   @FXML
   private Button percentBtn;
   /**
    * Instance of √ button at FXApp
    */
   @FXML
   private Button sqrtBtn;
   /**
    * Instance of 𝑥² button at FXApp
    */
   @FXML
   private Button sqrBtn;
   /**
    * Instance of ⅟𝑥 button at FXApp
    */
   @FXML
   private Button fractionBtn;
   /**
    * Instance of  button at FXApp
    */
   @FXML
   private Button divideBtn;
   /**
    * Instance of  button at FXApp
    */
   @FXML
   private Button multiplyBtn;
   /**
    * Instance of - button at FXApp
    */
   @FXML
   private Button subtractBtn;
   /**
    * Instance of + button at FXApp
    */
   @FXML
   private Button addBtn;
   /**
    * Instance of  button at FXApp
    */
   @FXML
   private Button negateBtn;
   /**
    * Instance of , button at FXApp
    */
   @FXML
   private Button comaBtn;
   /**
    * Instance of transparent drag button at FXApp
    */
   @FXML
   private Button dragBtn;
   /**
    * Instance of table with operation and digit buttons at FXApp
    */
   @FXML
   private GridPane mainButtonsGrid;

   /**
    * Flag: was digit entered before
    */
   private boolean wasDigitLast = false;

   /**
    * Field X of offset
    */
   private double xOffset = 0;
   /**
    * Field X of offset
    */
   private double yOffset = 0;

   /**
    * Initializes and adds menu list to the view
    */
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

   /**
    * Closes current FXApp
    */
   @FXML
   private void closeBtn() {

      WindowContainer.getInstance().close();
   }

   /**
    * Hide current window
    */
   @FXML
   private void hideBtn() {

      windowContainer.hide();
   }

   /**
    * Maximizes or minimizes current window depends on status
    */
   @FXML
   private void fullScreenBtnClick() {

      windowContainer.fullScreen();
   }

   /**
    * Initializes and adds history pane to the view
    */
   @FXML
   private void historyBtn() {

      ObservableList<Node> menuNodes = dropDownContainer.getChildren();
      menuNodes.add(prepareBackground());
      menuNodes.add(prepareDropDownLabel(DROPPED_HISTORY_TEXT, DROPPED_HISTORY_ID));
   }

   /**
    * Cleans display label
    */
   @FXML
   private void clearEnteredBtnClick() {

      handleOperation(new ClearEntered());
   }

   /**
    * Cleans display label and history label
    */
   @FXML
   private void clearBtnClick() {

      handleOperation(new Clear());
   }

   /**
    * Cuts last digit at display field
    */
   @FXML
   private void backspaceBtnClick() {

      handleOperation(new BaskSpace());
   }

   /**
    * Adds digit one to the display field
    */
   @FXML
   private void oneBtnClick() {

      handleDigit(new NumberSymbol(ONE));
   }

   /**
    * Adds digit two to the display field
    */
   @FXML
   private void twoBtnClick() {

      handleDigit(new NumberSymbol(TWO));
   }

   /**
    * Adds digit three to the display field
    */
   @FXML
   private void threeBtnClick() {

      handleDigit(new NumberSymbol(THREE));
   }

   /**
    * Adds digit four to the display field
    */
   @FXML
   private void fourBtnClick() {

      handleDigit(new NumberSymbol(FOUR));
   }

   /**
    * Adds digit five to the display field
    */
   @FXML
   private void fiveBtnClick() {

      handleDigit(new NumberSymbol(FIVE));
   }

   /**
    * Adds digit six to the display field
    */
   @FXML
   private void sixBtnClick() {

      handleDigit(new NumberSymbol(SIX));
   }

   /**
    * Adds digit seven to the display field
    */
   @FXML
   private void sevenBtnClick() {

      handleDigit(new NumberSymbol(SEVEN));
   }

   /**
    * Adds digit eight to the display field
    */
   @FXML
   private void eightBtnClick() {

      handleDigit(new NumberSymbol(EIGHT));
   }

   /**
    * Adds digit nine to the display field
    */
   @FXML
   private void nineBtnClick() {

      handleDigit(new NumberSymbol(NINE));
   }

   /**
    * Adds digit zero to the display field
    */
   @FXML
   private void zeroBtnClick() {

      handleDigit(new NumberSymbol(ZERO));
   }

   /**
    * Adds coma to the display field
    */
   @FXML
   private void comaBtnClick() {

      handleDigit(new NumberSymbol(SEPARATOR));
      displayContainer.addComa();
   }

   /**
    * Calls divide operation
    */
   @FXML
   private void divideBtnClick() {

      handleOperation(new Divide());
   }

   /**
    * Calls multiply operation
    */
   @FXML
   private void multiplyBtnClick() {

      handleOperation(new Multiply());
   }

   /**
    * Calls subtract operation
    */
   @FXML
   private void subtractBtnClick() {

      handleOperation(new Subtract());
   }

   /**
    * Calls add operation
    */
   @FXML
   private void addBtnClick() {

      handleOperation(new Add());
   }

   /**
    * Calls equal operation
    */
   @FXML
   private void equalsBtnClick() {

      handleOperation(new Equal());
   }

   /**
    * Calls percent operation
    */
   @FXML
   private void percentBtnClick() {

      handleOperation(new Percent());
   }

   /**
    * Calls sqrt operation
    */
   @FXML
   private void sqrtBtnClick() {

      handleOperation(new Sqrt());
   }

   /**
    * Calls sqr operation
    */
   @FXML
   private void sqrBtnClick() {

      handleOperation(new Sqr());
   }

   /**
    * Calls fraction operation
    */
   @FXML
   private void fractionBtnOneClick() {

      handleOperation(new Fraction());
   }

   /**
    * Calls negate operation
    */
   @FXML
   private void negateBtnClick() {

      handleOperation(new Negate());
   }

   /**
    * Calls clear memory operation
    */
   @FXML
   private void clearMemoryBtnClick() {

      handleOperation(new MemoryOperation(CLEAR_MEMORY));
      setDisableMemoryButtons(true);
   }

   /**
    * Calls memory recall operation
    */
   @FXML
   private void memoryRecallBtnClick() {

      handleOperation(new MemoryOperation(RECALL));
   }

   /**
    * Calls add to the memory operation
    */
   @FXML
   private void memoryAddBtnClick() {

      handleOperation(new MemoryOperation(ADD_TO_MEMORY));
      setDisableMemoryButtons(false);
   }

   /**
    * Calls subtract from the memory operation
    */
   @FXML
   private void memorySubtractBtnClick() {

      handleOperation(new MemoryOperation(SUBTRACT_FROM_MEMORY));
      setDisableMemoryButtons(false);
   }

   /**
    * Calls store to the memory operation
    */
   @FXML
   private void memoryStoreBtnClick() {

      handleOperation(new MemoryOperation(STORE));
      setDisableMemoryButtons(false);
   }

   /**
    * Initializes and adds history pane to the view
    */
   @FXML
   private void memoryShowBtnClick() {

      ObservableList<Node> menuNodes = dropDownContainer.getChildren();
      menuNodes.add(prepareBackground());
      String[] response = calcController.handleOperation(new MemoryOperation(RECALL));
      menuNodes.add(prepareDropDownLabel(response[0], DROPPED_MEMORY_ID));
   }

   @Override
   public void initialize(URL location, ResourceBundle resources) {

      setDragButton();
      setDisableMemoryButtons(true);
      setSizeMainTableColumns();
      displayContainer.setDisplay(display);
      historyContainer.setHistoryField(historyField, historyScroll);
      windowContainer.setFullScreenBtn(fullScreenBtn);
      windowContainer.setStage(rootPane);
   }

   /**
    * Sets up rows relations at buttons table
    */
   private void setSizeMainTableColumns() {

      ObservableList<RowConstraints> rows = mainTable.getRowConstraints();
      rows.get(0).setPercentHeight(5);
      rows.get(1).setPercentHeight(5);
      rows.get(2).setPercentHeight(10);
      rows.get(3).setPercentHeight(8);
      rows.get(4).setPercentHeight(77);
   }

   /**
    * Handle given numberSymbol and sets up response from the {@link CalcController}
    * on the display and history labels
    *
    * @param numberSymbol - given numberSymbol
    */
   private void handleDigit(NumberSymbol numberSymbol) {

      String[] response = calcController.handleDigit(numberSymbol);
      displayContainer.sendDigitToDisplay(response[0], wasDigitLast);
      handleDataFromResponse(response);
      wasDigitLast = true;
   }

   /**
    * Handle given digit and sets up response from the {@link CalcController}
    * on the display and history labels
    *
    * @param operation - given operation
    */
   private void handleOperation(Operation operation) {

      String[] response = calcController.handleOperation(operation);
      displayContainer.setDisplayedText(response[0]);
      handleDataFromResponse(response);
      wasDigitLast = false;
   }

   /**
    * Sets up view in depends on given response
    *
    * @param response - given response
    */
   private void handleDataFromResponse(String[] response) {

      setDisableOperationButtons(false);
      OperationType lastOperationType = calcController.getLastOperationType();

      if (isNotNumber(response[0])) {
         setDisableOperationButtons(true);
         historyContainer.setHistoryText(response[1]);

      } else if (CLEAR == lastOperationType || EQUAL == lastOperationType) {
         historyContainer.clear();

      } else {
         historyContainer.setHistoryText(response[1]);

      }

   }

   /**
    * Sets up disabling of memory buttons by given boolean value
    *
    * @param val - given boolean value
    */
   private void setDisableMemoryButtons(boolean val) {

      memoryRecallBtn.setDisable(val);
      clearAllMemoryBtn.setDisable(val);
      memoryShowBtn.setDisable(val);
   }

   /**
    * Sets up disabling of operation buttons by given boolean value
    *
    * @param value - given boolean value
    */
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

   /**
    * Initializes options list for menu
    *
    * @return - initialized ListView<MenuListOption> for menu
    */
   private ListView<MenuListOption> prepareMenuListView() {

      List<MenuListOption> listOptions = Arrays.asList(CALCULATOR, STANDARD, SCIENTIFIC, PROGRAMMER,
              DATE_CALCULATION, CONVERTER, CURRENCY, VOLUME, LENGTH,
              WEIGHT_AND_MASS, TEMPERATURE, ENERGY, AREA,
              SPEED, TIME, POWER, DATA, PRESSURE, ANGLE);
      ObservableList<MenuListOption> listOpt = FXCollections.observableList(listOptions);
      ListView<MenuListOption> menuList = new ListView<>(listOpt);

      menuList.setId(DROPPED_LIST_ID);
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

                     if (!item.isOption()) {
                        setId(NOT_OPTION_ITEM_ID);
                        setDisable(true);
                     } else {
                        setId(OPTION_ITEM_ID);
                     }

                  }

               }
            };
         }

      });

      menuList.setPrefHeight(rootPane.getScene().getHeight() - MENU_LIST_HEIGHT_DIFFERENCE);
      AnchorPane.setTopAnchor(menuList, MENU_LIST_TOP_ANCHOR);
      AnchorPane.setLeftAnchor(menuList, MENU_LIST_LEFT_ANCHOR);

      return menuList;
   }

   /**
    * Sets up given ListView as emergent
    *
    * @param menuListView - given ListView
    * @param button       - given button for ListView
    */
   private void setEmergentList(ListView<MenuListOption> menuListView, Button button) {

      Timeline timeline = new Timeline(new KeyFrame(Duration.millis(2), new EventHandler<ActionEvent>() {
         private double i = 1;

         @Override
         public void handle(ActionEvent event) {

            button.setMinWidth(i);
            button.setPrefWidth(i);
            menuListView.setMinWidth(i);
            menuListView.setPrefWidth(i);
            i += STEP;
         }
      }));

      timeline.setOnFinished((actionEvent) -> {
         button.setPrefWidth(MENU_LIST_WIDTH);
         menuListView.setPrefWidth(MENU_LIST_WIDTH);
      });

      timeline.setCycleCount(CYCLE_COUNT);
      timeline.play();
   }

   /**
    * Initializes menu button
    *
    * @return menu button
    */
   private Button prepareMenuBtn() {

      Button menuBtn = new Button(MENU_BTN_SYMBOL);
      menuBtn.setId(PRESSED_MENU_BTN_ID);
      menuBtn.setPrefSize(MENU_BTN_SIZE, MENU_BTN_SIZE);
      menuBtn.setOnAction((actionEvent) -> hideDropDown());
      AnchorPane.setTopAnchor(menuBtn, MENU_BTN_TOP_ANCHOR);
      AnchorPane.setLeftAnchor(menuBtn, MENU_BTN_LEFT_ANCHOR);

      return menuBtn;
   }

   /**
    * Initializes About button
    *
    * @return About button
    */
   private Button prepareAboutBtn() {

      Button aboutBtn = new Button(ABOUT_BTN_SYMBOL + ABOUT_BTN_TEXT);
      aboutBtn.setId(ABOUT_BTN_ID);
      aboutBtn.setPrefSize(MENU_LIST_WIDTH, ABOUT_BTN_HEIGHT);
      aboutBtn.setAlignment(Pos.CENTER_LEFT);
      AnchorPane.setBottomAnchor(aboutBtn, ABOUT_BTN_BOTTOM_ANCHOR);
      AnchorPane.setLeftAnchor(aboutBtn, MENU_LIST_LEFT_ANCHOR);

      return aboutBtn;
   }

   /**
    * Initializes transparent background pane
    *
    * @return transparent background pane
    */
   private Pane prepareBackground() {

      double height = rootPane.getHeight();
      double width = rootPane.getWidth();

      Pane pane = new Pane();
      pane.setPrefSize(width, height);
      pane.setOpacity(BACKGROUND_OPACITY);
      pane.setOnMouseClicked((event) -> hideDropDown());

      return pane;
   }

   /**
    * Removes children from dropDownContainer
    */
   private void hideDropDown() {

      ObservableList<Node> nodes = dropDownContainer.getChildren();

      for (int i = nodes.size() - 1; i >= 0; i--) {
         nodes.remove(i);
      }
   }

   /**
    * Initializes drag button behavior
    */
   private void setDragButton() {

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
            windowContainer.fullScreen();
         }

      });
   }

   /**
    * Verifies is current location of mouse at visible side of screen
    *
    * @param event - current mouse event
    * @return true if location is not correct
    */
   private boolean checkOnOverScreen(MouseEvent event) {

      double x = event.getScreenY();
      double y = event.getScreenX();
      boolean result = false;

      if (x < 1 || y < 1) {
         result = true;
      }

      return result;
   }

   /**
    * Initializes dropdown label with given text and id
    *
    * @param text - given text
    * @param id   - given id
    * @return initialized label
    */
   private Label prepareDropDownLabel(String text, String id) {

      Label label = new Label(text);
      label.setId(id);
      AnchorPane.setBottomAnchor(label, 0.0);
      AnchorPane.setLeftAnchor(label, MENU_LIST_LEFT_ANCHOR);
      label.setPrefSize(rootPane.getWidth() - 4, mainButtonsGrid.getHeight());

      return label;
   }

}















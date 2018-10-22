package win_calculator.view.listeners;

import javafx.scene.control.Button;
import javafx.stage.Stage;

import java.util.HashMap;

import static org.loadui.testfx.GuiTest.find;

/**
 * Listener of keyboard buttons events
 * Binds keys with buttons on FXApp
 */
public class ButtonPressListener {

   /**
    * Constants: map with binds for simple keys
    */
   private static final HashMap<String, String> buttons = createButtonsMap();
   /**
    * Constants: map with binds for key combinations
    */
   private static final HashMap<String, String> combos = createComboMap();
   /**
    * Constant: string representation of shift button
    */
   private static final String SHIFT_STR = "Shift";

   /**
    * Flag: is Shift button pressed on the keyboard
    */
   private static boolean shiftPressed = false;

   /**
    * Initializes map with keyboard key and button's id at FXApp
    *
    * @return initialized HashMap
    */
   private static HashMap<String, String> createButtonsMap() {

      HashMap<String, String> map = new HashMap<>();
      map.put("0", "#zeroBtn");
      map.put("1", "#oneBtn");
      map.put("2", "#twoBtn");
      map.put("3", "#threeBtn");
      map.put("4", "#fourBtn");
      map.put("5", "#fiveBtn");
      map.put("6", "#sixBtn");
      map.put("7", "#sevenBtn");
      map.put("8", "#eightBtn");
      map.put("9", "#nineBtn");
      map.put("Comma", "#comaBtn");
      map.put("Add", "#addBtn");
      map.put("Subtract", "#subtractBtn");
      map.put("Minus", "#subtractBtn");
      map.put("Multiply", "#multiplyBtn");
      map.put("Divide", "#divideBtn");
      map.put("Slash", "#divideBtn");
      map.put("Q", "#sqrBtn");
      map.put("R", "#fractionBtn");
      map.put("Delete", "#clearEnteredBtn");
      map.put("C", "#clearBtn");
      map.put("Backspace", "#backSpaceBtn");
      map.put("Equals", "#equalsBtn");
      map.put("Equal", "#equalsBtn");
      map.put("F9", "#negateBtn");
      map.put("L", "#clearAllMemoryBtn");
      map.put("M", "#memoryStoreBtn");
      map.put("O", "#memoryRecallBtn");
      map.put("P", "#memoryAddBtn");
      map.put("S", "#memorySubtractBtn");
      return map;
   }

   /**
    * Initializes map for buttons accepts combinated key presses
    *
    * @return initialized HashMap
    */
   private static HashMap<String, String> createComboMap() {

      HashMap<String, String> map = new HashMap<>();
      map.put("2", "#sqrtBtn");
      map.put("5", "#percentBtn");
      map.put("8", "#multiplyBtn");
      map.put("Equals", "#addBtn");
      return map;
   }

   /**
    * Binds keyboard events with buttons on FXApp
    *
    * @param stage - window of current FXApp
    */
   public static void addButtonPressListener(Stage stage) {
      stage.getScene().setOnKeyPressed(event -> {

         String name = event.getCode().getName();
         Button button;

         if (SHIFT_STR.equals(name)) {
            shiftPressed = true;
         } else {

            if (shiftPressed) {
               button = find(combos.get(name));
               shiftPressed = false;
            } else {
               button = find(buttons.get(name));
            }

            button.fire();
         }
      });
   }

}
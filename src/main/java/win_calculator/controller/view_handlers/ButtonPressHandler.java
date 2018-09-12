package win_calculator.controller.view_handlers;

import javafx.scene.control.Button;
import javafx.stage.Stage;

import java.util.HashMap;

import static org.loadui.testfx.GuiTest.find;

public class ButtonPressHandler {

    private static final HashMap<String,String> buttons = createButtonsMap();
    private static final HashMap<String,String> combos = createComboMap();
    private static boolean shiftPressed = false;
    private static HashMap<String,String> createButtonsMap(){

        HashMap<String,String> map = new HashMap<>();
        map.put("0","#buttonZero");
        map.put("1","#buttonOne");
        map.put("2","#buttonTwo");
        map.put("3","#buttonThree");
        map.put("4","#buttonFour");
        map.put("5","#buttonFive");
        map.put("6","#buttonSix");
        map.put("7","#buttonSeven");
        map.put("8","#buttonEight");
        map.put("9","#buttonNine");
        map.put("Comma","#buttonComa");
        map.put("Add","#addBtn");
        map.put("Subtract","#buttonMinus");
        map.put("Subtract","#buttonMinus");
        map.put("Multiply","#multiplyBtn");
        map.put("Divide","#buttonDivide");
        map.put("Slash","#buttonDivide");
//        map.put("%",);
//        map.put("@","#buttonSqrt");
        map.put("Q","#buttonSqr");
        map.put("R","#buttonFraction");
        map.put("Delete","#buttonClearEntered");
        map.put("C","#buttonClear");
        map.put("Backspace","#buttonBackSpace");
        map.put("Equals","#equalsBtn");
        map.put("Enter","#equalsBtn");
        map.put("F9","#buttonNegate");
        return map;
    }

    private static HashMap<String,String> createComboMap(){

        HashMap<String,String> map = new HashMap<>();
        map.put("2","#sqrtBtn");
        map.put("5","#percentBtn");
        map.put("8","#multiplyBtn");
        map.put("Equals","#addBtn");
        return map;
    }
    public static void addButtonPressListener(Stage stage){
        stage.getScene().setOnKeyPressed(event -> {
            try{
                String name = event.getCode().getName();
                if ("Shift".equals(name)){
                    shiftPressed = true;
                }
                Button button;
                if (shiftPressed){
                    button = find(combos.get(name));
                    shiftPressed = false;
                }else {
                    button = find(buttons.get(name));
                }
                button.fire();
            }catch (NullPointerException e){
                System.out.println("key is undefined");
            }
        });
    }

}

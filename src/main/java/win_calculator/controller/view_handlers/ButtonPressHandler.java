package win_calculator.controller.view_handlers;

import javafx.scene.control.Button;
import javafx.stage.Stage;

import java.util.HashMap;

import static org.loadui.testfx.GuiTest.find;

public class ButtonPressHandler {

    private static final HashMap<String,String> actions = createMap();
    private static HashMap<String,String> createMap(){

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
        map.put("Add","#buttonPlus");
        map.put("Minus","#buttonMinus");
        map.put("Subtract","#buttonMinus");
        map.put("Multiply","#buttonMultiply");
        map.put("Divide","#buttonDivide");
        map.put("Slash","#buttonDivide");
        map.put("%","#buttonPercent");
        map.put("@","#buttonSqrt");
        map.put("Q","#buttonSqr");
        map.put("R","#buttonFraction");
        map.put("Delete","#buttonClearEntered");
        map.put("C","#buttonClear");
        map.put("Backspace","#buttonBackSpace");
        map.put("Equals","#buttonEnter");
        map.put("Enter","#buttonEnter");
        map.put("F9","#buttonNegate");
        return map;
    }
    public static void addButtonPressListener(Stage stage){
        stage.getScene().setOnKeyPressed(event -> {
            try{
                Button button = find(actions.get(event.getCode().getName()));
                button.fire();
            }catch (NullPointerException e){
                System.out.println("key is undefined");
            }
        });
    }

}

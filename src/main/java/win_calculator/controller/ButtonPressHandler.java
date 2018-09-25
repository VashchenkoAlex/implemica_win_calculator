package win_calculator.controller;

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
        map.put("0","#zeroBtn");
        map.put("1","#oneBtn");
        map.put("2","#twoBtn");
        map.put("3","#threeBtn");
        map.put("4","#fourBtn");
        map.put("5","#fiveBtn");
        map.put("6","#sixBtn");
        map.put("7","#sevenBtn");
        map.put("8","#eightBtn");
        map.put("9","#nineBtn");
        map.put("Comma","#comaBtn");
        map.put("Add","#addBtn");
        map.put("Subtract","#subtractBtn");
        map.put("Multiply","#multiplyBtn");
        map.put("Divide","#divideBtn");
        map.put("Slash","#divideBtn");
        map.put("Q","#sqrBtn");
        map.put("R","#fractionBtn");
        map.put("Delete","#clearEnteredBtn");
        map.put("C","#clearBtn");
        map.put("Backspace","#backSpaceBtn");
        map.put("Equals","#equalsBtn");
        map.put("Enter","#equalsBtn");
        map.put("F9","#negateBtn");
        map.put("L","#clearAllMemoryBtn");
        map.put("M","#memoryStoreBtn");
        map.put("O","#memoryRecallBtn");
        map.put("P","#memoryAddBtn");
        map.put("S","#memorySubtractBtn");
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

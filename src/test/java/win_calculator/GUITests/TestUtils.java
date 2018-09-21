package win_calculator.GUITests;

import javafx.scene.input.KeyCode;

import java.util.HashMap;

class TestUtils {

    static HashMap<String,TestButton> createButtonsMap(){

        HashMap<String, TestButton> map = new HashMap<>();
        map.put("0",new TestButton("#zeroBtn",KeyCode.DIGIT0,false));
        map.put("1",new TestButton("#oneBtn",KeyCode.DIGIT1,false));
        map.put("2",new TestButton("#twoBtn",KeyCode.DIGIT2,false));
        map.put("3",new TestButton("#threeBtn",KeyCode.DIGIT3,false));
        map.put("4",new TestButton("#fourBtn",KeyCode.DIGIT4,false));
        map.put("5",new TestButton("#fiveBtn",KeyCode.DIGIT5,false));
        map.put("6",new TestButton("#sixBtn",KeyCode.DIGIT6,false));
        map.put("7",new TestButton("#sevenBtn",KeyCode.DIGIT7,false));
        map.put("8",new TestButton("#eightBtn",KeyCode.DIGIT8,false));
        map.put("9",new TestButton("#nineBtn",KeyCode.DIGIT9,false));
        map.put(",",new TestButton("#comaBtn",KeyCode.COMMA,false));
        map.put("+",new TestButton("#addBtn",KeyCode.ADD,false));
        map.put("-",new TestButton("#subtractBtn",KeyCode.SUBTRACT,false));
        map.put("*",new TestButton("#multiplyBtn",KeyCode.DIGIT8,true));
        map.put("n*",new TestButton("#multiplyBtn",KeyCode.MULTIPLY,false));
        map.put("/",new TestButton("#divideBtn",KeyCode.DIVIDE,false));
        map.put("%",new TestButton("#percentBtn",KeyCode.DIGIT5,true));
        map.put("sqrt",new TestButton("#sqrtBtn",KeyCode.DIGIT2,true));
        map.put("sqr",new TestButton("#sqrBtn",KeyCode.Q,false));
        map.put("1/x",new TestButton("#fractionBtn",KeyCode.R,false));
        map.put("CE",new TestButton("#clearEnteredBtn",KeyCode.DELETE,false));
        map.put("C",new TestButton("#clearBtn",KeyCode.C,false));
        map.put("<-",new TestButton("#backSpaceBtn",KeyCode.BACK_SPACE,false));
        map.put("=",new TestButton("#equalsBtn",KeyCode.ENTER,false));
        map.put("±",new TestButton("#negateBtn",KeyCode.F9,false));
        map.put("MC",new TestButton("#clearAllMemoryBtn",KeyCode.L,false));
        map.put("MS",new TestButton("#memoryStoreBtn",KeyCode.M,false));
        map.put("MR",new TestButton("#memoryRecallBtn",KeyCode.O,false));
        map.put("M+",new TestButton("#memoryAddBtn",KeyCode.P,false));
        map.put("M-",new TestButton("#memorySubtractBtn",KeyCode.S,false));
        map.put("MENU",new TestButton("#menuBtn",KeyCode.M,true));
        return map;
    }


}

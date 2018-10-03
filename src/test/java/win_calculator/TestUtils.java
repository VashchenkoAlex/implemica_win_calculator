package win_calculator;

import javafx.scene.input.KeyCode;
import win_calculator.controller.digits.*;
//import win_calculator.controller.memory.*;
import win_calculator.model.memory.*;
import win_calculator.model.nodes.events.Event;
import win_calculator.model.nodes.events.clear.BaskSpace;
import win_calculator.model.nodes.events.clear.Clear;
import win_calculator.model.nodes.events.clear.ClearDisplay;
import win_calculator.model.nodes.events.enter.Enter;
import win_calculator.model.nodes.events.extra_operations.*;
import win_calculator.model.nodes.events.main_operations.Add;
import win_calculator.model.nodes.events.main_operations.Divide;
import win_calculator.model.nodes.events.main_operations.Multiply;
import win_calculator.model.nodes.events.main_operations.Subtract;

import java.awt.event.KeyEvent;
import java.util.HashMap;

public class TestUtils {

    public static HashMap<String, ButtonForTest> createButtonsMap(){

        HashMap<String, ButtonForTest> map = new HashMap<>();
        map.put("0",new ButtonForTest("#zeroBtn",/*KeyCode.DIGIT0*/KeyEvent.VK_0,false));
        map.put("1",new ButtonForTest("#oneBtn",/*KeyCode.DIGIT1*/KeyEvent.VK_1,false));
        map.put("2",new ButtonForTest("#twoBtn",/*KeyCode.DIGIT2*/KeyEvent.VK_2,false));
        map.put("3",new ButtonForTest("#threeBtn",/*KeyCode.DIGIT3*/KeyEvent.VK_3,false));
        map.put("4",new ButtonForTest("#fourBtn",/*KeyCode.DIGIT4*/KeyEvent.VK_4,false));
        map.put("5",new ButtonForTest("#fiveBtn",/*KeyCode.DIGIT5*/KeyEvent.VK_5,false));
        map.put("6",new ButtonForTest("#sixBtn",/*KeyCode.DIGIT6*/KeyEvent.VK_6,false));
        map.put("7",new ButtonForTest("#sevenBtn",/*KeyCode.DIGIT7*/KeyEvent.VK_7,false));
        map.put("8",new ButtonForTest("#eightBtn",/*KeyCode.DIGIT8*/KeyEvent.VK_8,false));
        map.put("9",new ButtonForTest("#nineBtn",/*KeyCode.DIGIT9*/KeyEvent.VK_9,false));
        map.put(",",new ButtonForTest("#comaBtn",/*KeyCode.COMMA*/KeyEvent.VK_COMMA,false));
        map.put("+",new ButtonForTest("#addBtn",/*KeyCode.ADD*/KeyEvent.VK_ADD,false));
        map.put("-",new ButtonForTest("#subtractBtn",/*KeyCode.SUBTRACT*/KeyEvent.VK_SUBTRACT,false));
        map.put("*",new ButtonForTest("#multiplyBtn",/*KeyCode.DIGIT8*/KeyEvent.VK_8,true));
        map.put("n*",new ButtonForTest("#multiplyBtn",/*KeyCode.MULTIPLY*/KeyEvent.VK_MULTIPLY,false));
        map.put("/",new ButtonForTest("#divideBtn",/*KeyCode.DIVIDE*/KeyEvent.VK_DIVIDE,false));
        map.put("%",new ButtonForTest("#percentBtn",/*KeyCode.DIGIT5*/KeyEvent.VK_5,true));
        map.put("sqrt",new ButtonForTest("#sqrtBtn",/*KeyCode.DIGIT2*/KeyEvent.VK_2,true));
        map.put("sqr",new ButtonForTest("#sqrBtn",/*KeyCode.Q*/KeyEvent.VK_Q,false));
        map.put("1/x",new ButtonForTest("#fractionBtn",/*KeyCode.R*/KeyEvent.VK_R,false));
        map.put("CE",new ButtonForTest("#clearEnteredBtn",/*KeyCode.DELETE*/KeyEvent.VK_DELETE,false));
        map.put("C",new ButtonForTest("#clearBtn",/*KeyCode.C*/KeyEvent.VK_C,false));
        map.put("⟵",new ButtonForTest("#backSpaceBtn",/*KeyCode.BACK_SPACE*/KeyEvent.VK_BACK_SPACE,false));
        map.put("=",new ButtonForTest("#equalsBtn",/*KeyCode.ENTER*/KeyEvent.VK_ENTER,false));
        map.put("±",new ButtonForTest("#negateBtn",/*KeyCode.F9*/KeyEvent.VK_F9,false));
        map.put("MC",new ButtonForTest("#clearAllMemoryBtn",/*KeyCode.L*/KeyEvent.VK_L,false));
        map.put("MS",new ButtonForTest("#memoryStoreBtn",/*KeyCode.M*/KeyEvent.VK_M,false));
        map.put("MR",new ButtonForTest("#memoryRecallBtn",/*KeyCode.O*/KeyEvent.VK_O,false));
        map.put("M+",new ButtonForTest("#memoryAddBtn",/*KeyCode.P*/KeyEvent.VK_P,false));
        map.put("M-",new ButtonForTest("#memorySubtractBtn",/*KeyCode.S*/KeyEvent.VK_S,false));
        map.put("MENU",new ButtonForTest("#menuBtn",/*KeyCode.M*/KeyEvent.VK_M,true));
        map.put("FS",new ButtonForTest("#fullScreenBtn",/*KeyCode.F*/KeyEvent.VK_F,true));
        map.put("HD",new ButtonForTest("#hideBtn",/*KeyCode.H*/KeyEvent.VK_H,true));
        return map;
    }

    public static HashMap<String, Event> createMap() {

        HashMap<String, Event> map = new HashMap<>();
        map.put("0", new ZeroDigit());
        map.put("1", new OneDigit());
        map.put("2", new TwoDigit());
        map.put("3", new ThreeDigit());
        map.put("4", new FourDigit());
        map.put("5", new FiveDigit());
        map.put("6", new SixDigit());
        map.put("7", new SevenDigit());
        map.put("8", new EightDigit());
        map.put("9", new NineDigit());
        map.put(",", new Coma());
        map.put("+", new Add());
        map.put("-", new Subtract());
        map.put("*", new Multiply());
        map.put("/", new Divide());
        map.put("%", new Percent());
        map.put("sqrt", new Sqrt());
        map.put("sqr", new Sqr());
        map.put("1/x", new Fraction());
        map.put("CE", new ClearDisplay());
        map.put("C", new Clear());
        map.put("⟵", new BaskSpace());
        map.put("=", new Enter());
        map.put("±", new Negate());
        map.put("MC", new ClearMemory());
        map.put("MS", new StoreMemory());
        map.put("MR", new RecallMemory());
        map.put("M+", new AddToMemory());
        map.put("M-", new SubtractMemory());
        return map;
    }


}

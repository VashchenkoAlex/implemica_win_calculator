package win_calculator.GUITests;

import javafx.scene.control.Button;
import javafx.stage.Stage;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.loadui.testfx.GuiTest;
import org.testfx.api.FxRobot;
import org.testfx.framework.junit5.*;
import org.testfx.matcher.control.LabeledMatchers;
import win_calculator.MainApp;

import java.io.IOException;
import java.util.HashMap;

import static org.loadui.testfx.GuiTest.find;
import static org.testfx.api.FxAssert.verifyThat;

class UITest extends ApplicationTest{

    private MainApp app = new MainApp();
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
        map.put(",","#buttonComa");
        map.put("+","#buttonPlus");
        map.put("-","#buttonMinus");
        map.put("*","#buttonMultiply");
        map.put("/","#buttonDivide");
        map.put("%","buttonPercent");
        map.put("sqrt","#buttonSqrt");
        map.put("sqr","#buttonSqr");
        map.put("1/x","buttonFraction");
        map.put("CE","#buttonClearEntered");
        map.put("C","#buttonClear");
        map.put("<-","buttonBackSpace");
        map.put("=","buttonEnter");
        map.put("±","buttonNegate");
        return map;
    }

    @Start
    public void start(Stage primaryStage) throws IOException {

        app.setUpApp();
    }

    @Test
    void TestButtons(){

        test(", , ,", "0,","");
        test("1 , , ,", "1,","");
        test("1 , , , 2", "1,2","");
        test(", , , 2", "0,2","");
        test(", , , 2 , , , ,", "0,2","");
        test("1 2 , , , 3 4 5 , , , 6 7 ,", "12,34567","");
        test("C","0","");
        test("1","1","");
        test("1 2","12","");
        test("1 2 3","123","");
        test("1 2 3 4","1 234","");
        test("1 2 3 4 5","12 345","");
        test("1 2 3 4 5 6","123 456","");
        test("1 2 3 4 5 6 7","1 234 567","");
        test("1 2 3 4 5 6 7 8","12 345 678","");
        test("1 2 3 4 5 6 7 8 9","123 456 789","");
        test("1 2 3 4 5 6 7 8 9 0","1 234 567 890","");
        test("1 2 3 4 5 6 7 8 9 0 ,","1 234 567 890,","");
        test("1 2 3 4 5 6 7 8 9 0 , 0","1 234 567 890,0","");
        test("1 2 3 4 5 6 7 8 9 0 , 0 9","1 234 567 890,09","");
        test("1 2 3 4 5 6 7 8 9 0 , 0 9 8","1 234 567 890,098","");
        test("1 2 3 4 5 6 7 8 9 0 , 0 9 8 7","1 234 567 890,0987","");
        test("1 2 3 4 5 6 7 8 9 0 , 0 9 8 7 6","1 234 567 890,09876","");
        test("1 2 3 4 5 6 7 8 9 0 , 0 9 8 7 6 5","1 234 567 890,098765","");
        test("1 2 3 4 5 6 7 8 9 0 , 0 9 8 7 6 5 4","1 234 567 890,098765","");
        test("1 2 3 4 5 6 7 8 9 0 , 0 9 8 7 6 5 4 ,","1 234 567 890,098765","");
        test("1 2 3 4 5 6 7 8 9 0 , 0 9 8 7 6 5 4 , C","0","");
        test("2","2","");
        test("2 +","2","2  +  ");
        test("2 + 3","3","2  +  ");
        test("2 + 3 -","5","2  +  3  -  ");
        test("2 + 3 - *","5","2  +  3  ×  ");
        test("2 + 3 - * 4","4","2  +  3  ×  ");
        test("2 + 3 - * 4 /","20","2  +  3  ×  4  ÷  ");
        test("2 + 3 - * 4 / 9","9","2  +  3  ×  4  ÷  ");
        test("2 + 3 - * 4 / 9 sqrt","3","2  +  3  ×  4  ÷  √( 9 )");
        test("2 + 3 - * 4 / 9 sqrt sqr","9","2  +  3  ×  4  ÷  sqr( √( 9 ) )");
        test("2 + 3 - * 4 / 9 sqrt sqr *","2,222222222222222","2  +  3  ×  4  ÷  sqr( √( 9 ) )  ×  ");
        test("2 + 3 - * 4 / 9 sqrt sqr * CE","0","2  +  3  ×  4  ÷  sqr( √( 9 ) )  ×  ");
        test("C","0","");
    }


    private void test(String expression, String display, String history){

        String[] buttons = expression.split(" ");
        for (String btnName : buttons) {
            clickOn(btnName);
        }
        verifyThat("#display",LabeledMatchers.hasText(display));
        verifyThat("#historyField",LabeledMatchers.hasText(history));
        clickOn("C");
    }

    private void clickOn(String btnName){

        Button button = find(actions.get(btnName));
        this.interact(button::fire);
    }

}

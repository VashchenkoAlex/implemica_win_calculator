package win_calculator.GUITests;

import javafx.application.Platform;
import javafx.event.Event;
import javafx.scene.control.Button;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyCodeCombination;
import javafx.scene.input.KeyCombination;
import javafx.scene.input.KeyEvent;
import javafx.stage.Stage;
import org.junit.jupiter.api.*;
import org.testfx.framework.junit5.*;
import org.testfx.matcher.control.LabeledMatchers;
import org.testfx.util.WaitForAsyncUtils;
import win_calculator.MainApp;

import java.io.IOException;
import java.util.HashMap;

import static javafx.scene.input.KeyCode.SHIFT;
import static org.loadui.testfx.GuiTest.find;
import static org.testfx.api.FxAssert.verifyThat;

class UITest extends ApplicationTest{

    private MainApp app = new MainApp();
    private static final HashMap<String,TestButton> actions = createMap();
    private static HashMap<String,TestButton> createMap(){

        HashMap<String, TestButton> map = new HashMap<>();
        map.put("0",new TestButton("#buttonZero",KeyCode.DIGIT0));
        map.put("1",new TestButton("#buttonOne",KeyCode.DIGIT1));
        map.put("2",new TestButton("#buttonTwo",KeyCode.DIGIT2));
        map.put("3",new TestButton("#buttonThree",KeyCode.DIGIT3));
        map.put("4",new TestButton("#buttonFour",KeyCode.DIGIT4));
        map.put("5",new TestButton("#buttonFive",KeyCode.DIGIT5));
        map.put("6",new TestButton("#buttonSix",KeyCode.DIGIT6));
        map.put("7",new TestButton("#buttonSeven",KeyCode.DIGIT7));
        map.put("8",new TestButton("#buttonEight",KeyCode.DIGIT8));
        map.put("9",new TestButton("#buttonNine",KeyCode.DIGIT9));
        map.put(",",new TestButton("#buttonComa",KeyCode.COMMA));
        map.put("+",new TestButton("#buttonPlus",KeyCode.ADD));
        map.put("-",new TestButton("#buttonMinus",KeyCode.SUBTRACT));
        map.put("*",new TestButton("#buttonMultiply",KeyCode.MULTIPLY));
        map.put("/",new TestButton("#buttonDivide",KeyCode.DIVIDE));
        map.put("%",new TestButton("#buttonPercent",
                new KeyCodeCombination(KeyCode.DIGIT5,KeyCombination.SHIFT_DOWN).getCode()));
        map.put("sqrt",new TestButton("#buttonSqrt",
                new KeyCodeCombination(KeyCode.DIGIT2,KeyCombination.SHIFT_DOWN).getCode()));
        map.put("sqr",new TestButton("#buttonSqr",KeyCode.Q));
        map.put("1/x",new TestButton("#buttonFraction",KeyCode.R));
        map.put("CE",new TestButton("#buttonClearEntered",KeyCode.DELETE));
        map.put("C",new TestButton("#buttonClearEntered",KeyCode.C));
        map.put("<-",new TestButton("#buttonBackSpace",KeyCode.BACK_SPACE));
        map.put("=",new TestButton("#buttonEnter",KeyCode.ENTER));
        map.put("±",new TestButton("#buttonNegate",KeyCode.F9));
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

    @Test
    void testKeyboard(){

        testKey("1 2 + 3 4 - 5 6 * 7 8 / 9 0 = ", "-8,666666666666667","");
        testKey("1 , , , 2 + 3 4 - 5 6 * 7 8 / 9 0 = ", "-18,02666666666667","");
        testKey("1 , , , 2 + 3 4 - 5 6 * 7 8 / 9 0 = C", "0","");
        testKey("1 + 2 + 3 + 4 =", "10","");

        testKey("9 8 7 6 5 4 3 2 1 0 + 2 4 6 8 0 1 3 5 7 9 % - 2 4 6 8 1 3 5 7 9 * 1 2 3 / 4 5 8 9 0 = ", "-8979834690,108306","");
        testKey("9876543210 % % + 36487136 - 374138274 * 93755 / 3890 = % %", "-897983,4690108306","");
        testKey(" 9876543210 % %  =", "987654,321","");
        testKey(" 9876543210 ± ± ± ± ± ± ± ± % %  = ", "987654,321","");
        testKey(" 9876543210 ±  % %  = ", "-987654,321","");
        testKey(" 9876543210 ± ± % % % % % % % %  = ", "0,000000987654321","");

        testKey(" . 00000001", "0,00000001","");
        testKey("", "0","");
        testKey(" . 00000001 ±", "-0,00000001","");
        testKey(" . 000 . . . 00001 = = = =", "0,00000001","");
        testKey(" . 00000001 % % %", "0,00000000000001","");
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

        Button button = find(actions.get(btnName).getId());
        this.interact(button::fire);
    }

    private void pressOn(String key){

        WaitForAsyncUtils.waitForFxEvents();
        TestButton testButton = actions.get(key);
        Button button = find(testButton.getId());
        KeyCode keyCode = testButton.getKeyCode();
        Platform.runLater(()-> Event.fireEvent(button, new KeyEvent(KeyEvent.KEY_PRESSED,
                "","",keyCode,false,false,false,false)));
        Platform.runLater(()-> Event.fireEvent(button, new KeyEvent(KeyEvent.KEY_RELEASED,
                "","",keyCode,false,false,false,false)));
    }

    private void testKey(String expression, String display, String history){

        String[] buttons = expression.split(" ");
        for (String btnName : buttons) {
            pressOn(btnName);
        }
        WaitForAsyncUtils.waitForFxEvents();
        verifyThat("#display",LabeledMatchers.hasText(display));
        verifyThat("#historyField",LabeledMatchers.hasText(history));
        pressOn("C");
    }

}

/////////////////////////////////////////////////////////////////
//    @Test
//    public void testExpression() throws Exception {
//        //test many buttons, that font size is decreasing
//        testStyle("11111111111111111111111111111111111111111111111111111");

//
//    }
//
//    @Test
//    public void testMOperation() throws Exception {
//        testApp("mc mr", "0");
//        testApp("2 m+ mr", "2");
//        testApp("10 m+ mr", "12");
//        testApp("112 m- mr", "-100");
//        testApp("999999 m+ m- mr", "-100");
//        testApp("100 m+ 200", "200");
//        testApp("300 m- 999", "999");
//        testApp("mr", "-300");
//        testApp("mr 234", "234");
//        testApp("mc 190", "190");
//        testApp("mr", "0");
//
//        //test button m+
//        testApp("mc mr", "0");
//        testApp("22 + 2 = m+ mr", "24");
//        testApp("999999 + 999999 - 999999 = m+ mr", "1000023");
//        testApp("999999 m+ mr", "2000022");
//        testApp("999999 + 1 m+ + 2 m+ mr", "2000025");
//        testApp("mc mr", "0");
//        testApp("22 + 2 = m+ 2", "2");
//        testApp("22 + 2 = m+ 2 + / * + = =", "6");
//        testApp("mr", "48");
//        testApp("mc mr", "0");
//
//        //test button m-
//        testApp("22 + 2 = m- mr", "-24");
//        testApp("22 + 2 = m- mr 23", "23");
//        testApp("999999 + 999999 - 999999 m+ = m- mr", "-48");
//        testApp("mc mr", "0");
//        testApp("999999 m- mr", "-999999");
//        testApp("999999 m- mr", "-1999998");
//        testApp("mc mr", "0");
//        testApp("22 + 2 = m- mr 23 + =", "46");
//        testApp("mc mr", "0");
//        testApp("1234567890 + 2 = m- mr * 23 + ", "-28395061516");
//        testApp("mc mr", "0");
//        testApp("1234567890 + 2 = m- mr * 23 + =", "-56790123032");
//        testApp("mc mr", "0");
//
//        //test with = m+,m-,mc,mr,+,-,/,*,%
//        testApp("22 + 2 = m+ 2 + / * + = = + 22 =", "28");
//        testApp("22 + 2 = m+ 2 + / * + = 2 + = = + 4 = =", "14");
//        testApp("22 + 2 = m+ 2 / * + = = 34", "34");
//        testApp("22 + 2 = m- 2 * / * + = =", "6");
//        testApp("22 + 2 = m- 8 - / + * = * 8 = + 3 =", "515");
//        testApp("22 + 2 = m- 123 / + * = * 8 = + 123 =", "121155");
//        testApp("123 / + * = * 8 = + 123 = = = mr", "0");
//        testApp("12 + 34 = m+ m- 2 + / * + = = + 22 =", "28");
//        testApp("3425 * 3220923 = m- m+ 2 + / * + = 2 + = = + 4 = =", "14");
//        testApp("784334 + 23784 = m+ m- mr 2 / * + = = 34", "34");
//        testApp("784334 + 23784 = m+ + 2 / * + = = ", "2424360");
//        testApp("mc", "0");
//        testApp("mr", "0");
//
//        testApp("1000000000 * = m+ mr", "1e18");
//        testApp("mc mr", "0");
//        testApp("1000000000 * = m+ mr m+ mr", "2e18");
//        testApp("mc mr", "0");
//
//    }

//    @Test
//    public void testExit() throws Exception {
//        final boolean[] invoked = new boolean[1];
//
//        ExitController.instance = new ExitController() {
//            @Override
//            public void exit() {
//                invoked[0] = true;
//            }
//        };
//        //click exit
//        WaitForAsyncUtils.waitForFxEvents();
//        TestButton close = find("#close");
//        Platform.runLater(close::fire);
//        WaitForAsyncUtils.waitForFxEvents();
//        assertTrue(invoked[0]);
//    }

//
//    @Test
//    public void testMouseClickedOnButton() throws Exception {
//        testClickMouseButton("AC 12 + 34 - 56 * 78 / 90 = ", "-2,533333333333333");
//        testClickMouseButton("AC 1 . . . 2 + 34 - 56 * 78 / 90 = ", "-13,33333333333333");
//        testClickMouseButton("AC 1 . . . 2 + 34 - 56 * 78 / 90 = AC", "0");
//
//        testClickMouseButton("AC 9876543210 % % + 36487136 - 374138274 * 93755 / 3890 = ", "-8979834690,108306");
//        testClickMouseButton("AC 9876543210 % % + 36487136 - 374138274 * 93755 / 3890 =  % %", "-897983,4690108306");
//        testClickMouseButton("AC 9876543210 % %  =", "987654,321");
//        testClickMouseButton("AC 9876543210 ± ± ± ± ± ± ± ± % %  = ", "987654,321");
//
//        testClickMouseButton("AC . 00000001", "0,00000001");
//        testClickMouseButton("AC . 00000001 ±", "-0,00000001");
//        testClickMouseButton("AC . 000 . . . 00001 = = = =", "0,00000001");
//        testClickMouseButton("AC . 00000001 % % % % %", "1e-18");
//
//        //test click on mc m+,m-,mr.mc
//        testClickMouseButton("AC 2 m+ mr", "2");
//        testClickMouseButton("AC 112 m- mr", "-110");
//        testClickMouseButton("AC 999999 m+ m- mr", "-110");
//        testClickMouseButton("AC 100 m+ 200", "200");
//        testClickMouseButton("AC 300 m- 999", "999");
//        testClickMouseButton("AC mr", "-310");
//        testClickMouseButton("AC mr 234", "234");
//        testClickMouseButton("AC mc 190", "190");
//        testClickMouseButton("AC mr", "0");
//    }
//
//    @Test
//    public void testKeyBoard() throws Exception {
//    }
//
//    @Test
//    public void testMouseDragged() throws Exception {
//        assertWindowLocation(700, 400);
//        assertWindowLocation(700, 300);
//        assertWindowLocation(800, 300);
//        assertWindowLocation(600, 500);
//        assertWindowLocation(500, 400);
//        assertWindowLocation(400, 300);
//        assertWindowLocation(900, 400);
//        assertWindowLocation(200, 700);
//        assertWindowLocation(1400, 400);
//        assertWindowLocation(1300, 200);
//        assertWindowLocation(1200, 100);
//        assertWindowLocation(1100, 300);
//
//        Random random = new Random();
//        assertWindowLocation(random.nextInt(1200) + 100, random.nextInt(750) + 50);
//        assertWindowLocation(random.nextInt(1200) + 100, random.nextInt(750) + 50);
//        assertWindowLocation(random.nextInt(1200) + 100, random.nextInt(750) + 50);
//        assertWindowLocation(random.nextInt(1200) + 100, random.nextInt(750) + 50);
//        assertWindowLocation(700, 400);
//    }

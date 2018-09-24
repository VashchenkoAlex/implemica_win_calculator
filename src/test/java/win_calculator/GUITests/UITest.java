package win_calculator.GUITests;

import javafx.application.Platform;
import javafx.event.Event;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseButton;
import javafx.stage.Stage;
import javafx.stage.Window;
import org.junit.jupiter.api.*;
import org.testfx.api.FxRobot;
import org.testfx.framework.junit5.*;
import org.testfx.matcher.control.LabeledMatchers;
import org.testfx.util.WaitForAsyncUtils;
import win_calculator.MainApp;
import win_calculator.controller.view_handlers.CaptionHandler;

import java.io.IOException;
import java.util.HashMap;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.loadui.testfx.GuiTest.find;
import static org.testfx.api.FxAssert.verifyThat;
import static win_calculator.GUITests.TestUtils.createButtonsMap;

class UITest extends ApplicationTest{


    private FxRobot fxRobot = new FxRobot();
    private Scene scene;
    private MainApp app = new MainApp();
    private static final HashMap<String,TestButton> buttons = createButtonsMap();

    @Start
    public void start(Stage primaryStage) throws IOException {

        app.setUpApp();
        scene = app.getStage().getScene();
    }

    @Test
    void TestUI(){

        testKey(", ±","-0,","");
        testKey(", 0 0 ±","-0,00","");
        testKey(", 0 0 5 ±","-0,005","");
        testKey(", 0 0 ± ±","0,00","");

        testKey("C <-","0","");

        testKey(", 0 0 5 0 0 ,","0,00500","");

        testKey("1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 ,","1 111 111 111 111 111,","");

        testKey("2 0 + %","4","20  +  4");
        testKey("1 2 + 3 4 - 5 6 * 7 8 / 9 0 = ", "-8,666666666666667","");
        testKey("1 , , , 2 + 3 4 - 5 6 * 7 8 / 9 0 = ", "-18,02666666666667","");
        testKey("1 , , , 2 + 3 4 - 5 6 * 7 8 / 9 0 = C", "0","");
        testKey("1 + 2 + 3 + 4 =", "10","");

        testKey("1 2 3 ± + 4 5 % - 6 7 sqr * 8 9 0 sqrt / 2 4 1/x = + 6 8 - 0 1 * 3 5 / 7 9 + 2 , 4 = - 6 8 * 1 3 / 5 7 =", "-337 673,2047848424","");
        testKey("9 8 7 6 5 4 3 2 1 0 + 1 3 5 7 9 1 3 5 % - 2 4 6 8 0 * 1 2 3 4 / 4 3 2 1 = sqr sqrt", "383 010 928 309 372,9","√( sqr( 383010928309372,9 ) )");
        testKey("9 8 7 6 5 4 3 2 1 0 + % =", "9,754610676665143e+17","");
        testKey("9 8 7 6 5 4 3 2 1 0 ± ± ± ± ± ± ± ± + % % = =", "1,926836657386991e+26","");
        testKey("9 8 7 6 5 4 3 2 1 0 ± % = ", "0","");
        testKey("1 2 3 4 5 6 7 8 9 0 sqr + sqrt ± % % %", "-4,371241896208856e+57","sqr( 1234567890 )  +  -4,371241896208856e+57");

        testKey(", 0 0 0 0 0 0 0 1", "0,00000001","");
        testKey(", 0 0 0 0 0 0 1 ±", "-0,0000001","");
        testKey(", 0 0 0 , , , 0 0 0 0 1 = = = =", "0,00000001","");
        testKey(", 0 0 0 0 0 0 0 1 - % % %", "1,e-38","0,00000001  -  1,e-38");
        testKey("1 2 3 4 5 6 7 8 9 0 , 0 1 2 3 4 5 6 7 *", "1 234 567 890,012345","1234567890,012345  ×  ");
        testKey("2 5 ± <-","-2","");
        testKey("1 2 3 4 ± <- <- <-","-1","");
        testKey("1 2 3 4 ± <- <- / 2 -","-6","-12  ÷  2  -  ");

        testKey("1 2 3 4 5 6 7 8 9 0 , 0 1 2 3 4 5 6 7 <- <- <- <- /", "1 234 567 890,01","1234567890,01  ÷  ");
        testKey("1 2 3 4 5 6 7 8 9 0 , 5 6 7 ± <- <- <- <- /", "-1 234 567 890","-1234567890  ÷  ");
        testKey("1 2 3 4 5 6 7 8 9 0 , 5 6 7 ± sqr <- <- <- <- /", "1,524157876419052e+18","sqr( -1234567890,567 )  ÷  ");
        testKey("2 5 6 7 8 9 0 , 1 3 4 ± <- <- <- <- sqr sqrt sqrt sqrt", "40,03078475546843","√( √( √( sqr( -2567890 ) ) ) )");

        testKey("0 , 0 0 0 0 0 0 0 0 0 <-", "0,00000000", "");
        testKey("0 , 0 0 0 0 0 0 0 0 0 <- <- <-", "0,000000", "");
        testKey("0 , 0 0 0 0 0 0 0 0 0 <- <- <- 0 0", "0,00000000", "");

        testKey("1 2 3 0 , 0 0 0 0 0 0 0 0 0 <-", "1 230,00000000", "");
        testKey("1 2 3 0 , 0 0 0 0 0 0 0 0 0 <- <- <-", "1 230,000000", "");

        test("1 + 2 0 % - 3 sqr * 4 sqrt / 5 1/x + 6 7 <- - 7 , 8 ± * 8 CE 9 / , 1 +","-5 778","1  +  0,2  -  sqr( 3 )  ×  √( 4 )  ÷  1/( 5 )  +  6  -  -7,8  ×  9  ÷  0,1  +  ");

        test("C","0","");
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

        test("/ =","Result is undefined","0  ÷  ");
        test("/ = MC","Result is undefined","0  ÷  ");
        test("/ = MR","Result is undefined","0  ÷  ");
        test("/ = M+","Result is undefined","0  ÷  ");
        test("/ = M-","Result is undefined","0  ÷  ");
        test("/ = MS","Result is undefined","0  ÷  ");
        test("/ = %","Result is undefined","0  ÷  ");
        test("/ = sqrt","Result is undefined","0  ÷  ");
        test("/ = sqr","Result is undefined","0  ÷  ");
        test("/ = /","Result is undefined","0  ÷  ");
        test("/ = *","Result is undefined","0  ÷  ");
        test("/ = -","Result is undefined","0  ÷  ");
        test("/ = +","Result is undefined","0  ÷  ");
        test("/ = ±","Result is undefined","0  ÷  ");
        test("/ = ,","Result is undefined","0  ÷  ");

        test("/ = CE","0","");
        test("/ = C","0","");
        test("/ = <-","0","");
        test("/ = 1","1","");
        test("/ = 2","2","");
        test("/ = 3","3","");
        test("/ = 4","4","");
        test("/ = 5","5","");
        test("/ = 6","6","");
        test("/ = 7","7","");
        test("/ = 8","8","");
        test("/ = 9","9","");
        test("/ = 0","0","");

        test("1 / 0 =","Cannot divide by zero","1  ÷  ");
        test("1 / 0 = MC","Cannot divide by zero","1  ÷  ");
        test("1 / 0 = MR","Cannot divide by zero","1  ÷  ");
        test("1 / 0 = MS","Cannot divide by zero","1  ÷  ");
        test("1 / 0 = M+","Cannot divide by zero","1  ÷  ");
        test("1 / 0 = M-","Cannot divide by zero","1  ÷  ");
        test("1 / 0 = %","Cannot divide by zero","1  ÷  ");
        test("1 / 0 = /","Cannot divide by zero","1  ÷  ");
        test("1 / 0 = *","Cannot divide by zero","1  ÷  ");
        test("1 / 0 = -","Cannot divide by zero","1  ÷  ");
        test("1 / 0 = +","Cannot divide by zero","1  ÷  ");
        test("1 / 0 = ±","Cannot divide by zero","1  ÷  ");
        test("1 / 0 = ,","Cannot divide by zero","1  ÷  ");

        test("1 / 0 = CE","0","");
        test("1 / 0 = C","0","");
        test("1 / 0 = <-","0","");
        test("1 / 0 = 1","1","");
        test("1 / 0 = 2","2","");
        test("1 / 0 = 3","3","");
        test("1 / 0 = 4","4","");
        test("1 / 0 = 5","5","");
        test("1 / 0 = 6","6","");
        test("1 / 0 = 7","7","");
        test("1 / 0 = 8","8","");
        test("1 / 0 = 9","9","");
        test("1 / 0 = 0","0","");

        test(", = + / , , - MC","Result is undefined","0  ÷  0  -  ");
        test(", , , , , = = , , , , , = = + - * / , , , , , -","Result is undefined","0  ÷  0  -  ");

        test("0 1/x","Cannot divide by zero","1/( 0 )");
        test("2 / 0 +","Cannot divide by zero","2  ÷  0  +  ");
        test("3 / 0 sqr +","Cannot divide by zero","3  ÷  sqr( 0 )  +  ");
        test("4 / 0 sqrt +","Cannot divide by zero","4  ÷  √( 0 )  +  ");

        test("1 ± sqrt","Invalid input","√( -1 )");
        test("1 ± sqrt 1 + 2 -","3","1  +  2  -  ");

        test("1 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 sqr sqr sqr sqr sqr sqr sqr sqr sqr sqr","Overflow","sqr( sqr( sqr( sqr( sqr( sqr( sqr( sqr( sqr( sqr( 1000000000000000 ) ) ) ) ) ) ) ) ) )");
        test("0 , 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 1 sqr sqr sqr sqr sqr sqr sqr sqr sqr sqr","Overflow","sqr( sqr( sqr( sqr( sqr( sqr( sqr( sqr( sqr( sqr( 0,0000000000000001 ) ) ) ) ) ) ) ) ) )");

        testMenuList();

        testByRobot("1 + 2 - 3 C", "0", "");
        testByRobot("1 + 2 - 3 CE 4 -", "-1", "1  +  2  -  4  -  ");
        testByRobot("1 M+ + 2 - MR + M- MR * 3 = MC", "3", "");
        testByRobot("1 + 2 0 % - 3 sqr * 4 sqrt / 5 1/x + 6 7 <- - 7 , 8 ± * 8 CE 9 / , 1 +", "-5 778", "1  +  0,2  -  sqr( 3 )  ×  √( 4 )  ÷  1/( 5 )  +  6  -  -7,8  ×  9  ÷  0,1  +  ");

        testDrag(0, 500, 200, 700);
        testDrag(0, -500, 200, 200);

        testDrag(20, 60, 220, 260);
        testDrag(-20, -60, 200, 200);
        testDrag(100, 100, 300, 300);
        testDrag(100, 200, 400, 500);
        testDrag(-100, -150, 300, 350);
        testDrag(-100, -150, 200, 200);

        testResize(-100, -100, false, false, 419, 601);
        testResize(100, 100, false, false, 320, 502);
        testResize(0, -100, false, false, 320, 601);
        testResize(0, 100, false, false, 320, 502);
        testResize(-100, 0, false, false, 419, 502);
        testResize(100, 0, false, false, 320, 502);

        testResize(100, 150, true, true, 420, 652);
        testResize(-100, -150, true, true, 320, 502);
        testResize(0, 100, true, true, 320, 602);
        testResize(0, -100, true, true, 320, 502);
        testResize(100, 0, true, true, 420, 502);
        testResize(-100, 0, true, true, 320, 502);

        testResize(100, -150, true, false, 420, 651);
        testResize(-100, 150, true, false, 320, 502);
        testResize(0, -100, true, false, 320, 601);
        testResize(0, 100, true, false, 320, 502);
        testResize(100, 0, true, false, 420, 502);
        testResize(-100, 0, true, false, 320, 502);

        testResize(-100, 150, false, true, 419, 652);
        testResize(100, -150, false, true, 320, 502);
        testResize(0, 100, false, true, 320, 602);
        testResize(0, -100, false, true, 320, 502);
        testResize(-100, 0, false, true, 419, 502);
        testResize(100, 0, false, true, 320, 502);

        testResize(100, 100, false, false, 320, 502);
        testResize(-100, -150, true, true, 320, 502);
        testResize(-100, 150, true, false, 320, 502);
        testResize(100, -150, false, true, 320, 502);

        testFSWithDrag(0, -300);
        testFSWithDrag(-300, 0);

        testFullScreen();
        testHide();

        testExit();
    }

    private void test(String expression, String display, String history){

        String[] buttons = expression.split(" ");
        for (String btnName : buttons) {
            clickOn(btnName);
        }
        WaitForAsyncUtils.waitForFxEvents();
        verifyThat("#display",LabeledMatchers.hasText(display));
        verifyThat("#historyField",LabeledMatchers.hasText(history));
        clickOn("C");
    }

    private void clickOn(String btnName){

        WaitForAsyncUtils.waitForFxEvents();
        Button button = find(buttons.get(btnName).getId());
        this.interact(button::fire);
    }

    private void pressOn(String key){

        WaitForAsyncUtils.waitForFxEvents();
        TestButton testButton = buttons.get(key);
        Button button = find(testButton.getId());
        boolean shiftPressed = testButton.isShiftPressed();
        if (shiftPressed){
            Platform.runLater(()->Event.fireEvent(button,new KeyEvent(KeyEvent.KEY_PRESSED,"","",
                    KeyCode.SHIFT,false,false,false,false)));
        }
        Platform.runLater(()-> Event.fireEvent(button, new KeyEvent(KeyEvent.KEY_PRESSED,
                "","",testButton.getKeyCode(),shiftPressed,false,false,false)));
        Platform.runLater(()-> Event.fireEvent(button, new KeyEvent(KeyEvent.KEY_RELEASED,
                "","",testButton.getKeyCode(),shiftPressed,false,false,false)));
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

    private void testByRobot(String expression, String display, String history) {

        String[] buttonKeys = expression.split(" ");
        for (String key : buttonKeys) {
            clickByRobot(key);
        }
        WaitForAsyncUtils.waitForFxEvents();
        verifyThat("#display", LabeledMatchers.hasText(display));
        verifyThat("#historyField", LabeledMatchers.hasText(history));
    }

    private void clickByRobot(String key) {

        fxRobot.clickOn(buttons.get(key).getId());
    }

    private void testMenuList() {

        clickOn("MENU");
        verifyThat("#droppedList", Node::isVisible);
        verifyThat("#menuBtnPressed", Node::isVisible);
        verifyThat("#menuBtnPressed", Node::isVisible);
        verifyThat("#aboutBtn", Node::isVisible);
        clickOn("MENU");
    }

    private void testDrag(double x, double y, double expectedX, double expectedY) {

        double[] coordinates = drag(x, y);
        assertEquals(expectedX, coordinates[0]);
        assertEquals(expectedY, coordinates[1]);
    }


    private void testResize(double x, double y, boolean rightBorder, boolean bottomBorder, double expectedWidth, double expectedHeight) {

        Window window = scene.getWindow();
        double leftMargin = 1;
        if (rightBorder) {
            leftMargin = window.getWidth() - 1;
        }
        double upMargin = 1;
        if (bottomBorder) {
            upMargin = window.getHeight() - 1;
        }
        double startX = window.getX() + leftMargin;
        double startY = window.getY() + upMargin;
        fxRobot.moveTo(startX, startY);
        fxRobot.press(MouseButton.PRIMARY);
        fxRobot.moveTo(startX + x, startY + y);
        fxRobot.release(MouseButton.PRIMARY);
        assertEquals(expectedWidth, scene.getWidth());
        assertEquals(expectedHeight, scene.getHeight());
    }

    private void testFSWithDrag(double x, double y) {

        drag(x, y);
        assertTrue(((Stage) scene.getWindow()).isMaximized());
        fxRobot.clickOn("#fullScreenBtn");
    }

    private void testFullScreen() {

        fxRobot.clickOn("#fullScreenBtn");
        assertTrue(((Stage) scene.getWindow()).isMaximized());
        fxRobot.clickOn("#fullScreenBtn");
    }

    private double[] drag(double x, double y) {

        Window window = scene.getWindow();
        double minX = window.getX();
        double minY = window.getY();
        fxRobot.drag(minX + 5, minY + 5);
        fxRobot.moveTo(minX + x + 5, minY + y + 5);
        fxRobot.release(MouseButton.PRIMARY);
        return new double[]{window.getX(), window.getY()};
    }

    private void testHide() {

        fxRobot.clickOn("#hideBtn");
        Stage stage = (Stage) scene.getWindow();
        assertTrue(stage.isIconified());
        this.interact(()->stage.setIconified(false));
        assertFalse(stage.isIconified());
    }

    private void testExit() {

        final boolean[] closed = new boolean[1];
        CaptionHandler.instance = new CaptionHandler() {
            @Override
            public void close() {
                closed[0] = true;
            }
        };
        Button close = find("#closeBtn");
        Platform.runLater(close::fire);
        WaitForAsyncUtils.waitForFxEvents();
        assertTrue(closed[0]);
    }
}

package win_calculator;

import javafx.application.Platform;
import javafx.geometry.Bounds;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.stage.Window;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.loadui.testfx.GuiTest;
import org.testfx.api.FxToolkit;
import org.testfx.matcher.control.LabeledMatchers;
import org.testfx.util.WaitForAsyncUtils;
import win_calculator.controller.view_handlers.CaptionHandler;

import java.awt.*;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.util.*;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.loadui.testfx.GuiTest.find;
import static org.loadui.testfx.GuiTest.getWindowByIndex;
import static org.testfx.api.FxAssert.verifyThat;
import static win_calculator.TestUtils.createButtonsMap;

class GUITest {

    private static Robot robot;
    private static final Map<String, ButtonForTest> buttons = createButtonsMap();
    private static final String IS_DIGIT_REGEX = "\\d*(,*\\d*,*)?";
    private static final String COMA = ",";
    private static final int[] START_COORDINATES = new int[] {200,200};
    private static final int[] START_SIZE = new int[] {320,502};
    private static final String DISPLAY_LABEL_ID = "#display";
    private static final String HISTORY_LABEL_ID = "#historyField";
    private static final String MENU_CONTAINER_ID = "#dropDownContainer";

    @BeforeAll
    static void prepare() throws Exception {
        new Thread("FXApp") {
            public void run() {
                MainApp.main(new String[0]);
            }
        }.start();
        while (!FxToolkit.isFXApplicationThreadRunning()){
            Thread.sleep(1);
        }
        WaitForAsyncUtils.waitForFxEvents();
        robot = new Robot();
    }

    @Test
    void testByKeysPress(){

        testKeys(", ±","-0,","");
        testKeys(",00 ±","-0,00","");
        testKeys(",005 ±","-0,005","");
        testKeys(",00 ± ±","0,00","");

        testKeys("C ⟵","0","");

        testKeys(",00500,","0,00500","");

        testKeys("1111111111111111,","1 111 111 111 111 111,","");

        testKeys("20 + %","4","20  +  4");
        testKeys("12 + 34 - 56 * 78 / 90 = ", "-8,666666666666667","");
        testKeys("1, , , 2 + 34 - 56 * 78 / 90 = ", "-18,02666666666667","");
        testKeys("1 , , , 2 + 34 - 56 * 78 / 90 = C", "0","");
        testKeys("1 + 2 + 3 + 4 =", "10","");

        testKeys("123 ± + 45 % - 67 sqr * 890 sqrt / 24 1/x = + 68 - 01 * 35 / 79 + 2,4 = - 68 * 13 / 57 =", "-337 673,2047848424","");
        testKeys("9876543210 + 13579135 % - 24680 * 1234 / 4321 = sqr sqrt", "383 010 928 309 372,9","√( sqr( 383010928309372,9 ) )");
        testKeys("9876543210 + % =", "9,754610676665143e+17","");
        testKeys("9876543210 ± ± ± ± ± ± ± ± + % % = =", "1,926836657386991e+26","");
        testKeys("9876543210 ± % = ", "0","");
        testKeys("1234567890 sqr + sqrt ± % % %", "-4,371241896208856e+57","sqr( 1234567890 )  +  -4,371241896208856e+57");

        testKeys(",00000001", "0,00000001","");
        testKeys(",0000001 ±", "-0,0000001","");
        testKeys(",000 , , , 00001 = = = =", "0,00000001","");
        testKeys(",00000001 - % % %", "1,e-38","0,00000001  -  1,e-38");
        testKeys("1234567890,01234567 *", "1 234 567 890,012345","1234567890,012345  ×  ");
        testKeys("25 ± ⟵","-2","");
        testKeys("1234 ± ⟵ ⟵ ⟵","-1","");
        testKeys("1234 ± ⟵ ⟵ / 2 -","-6","-12  ÷  2  -  ");

        testKeys("1234567890,01234567 ⟵ ⟵ ⟵ ⟵ /", "1 234 567 890,01","1234567890,01  ÷  ");
        testKeys("1234567890,567 ± ⟵ ⟵ ⟵ ⟵ /", "-1 234 567 890","-1234567890  ÷  ");
        testKeys("1234567890,567 ± sqr ⟵ ⟵ ⟵ ⟵ /", "1,524157876419052e+18","sqr( -1234567890,567 )  ÷  ");
        testKeys("2567890,134 ± ⟵ ⟵ ⟵ ⟵ sqr sqrt sqrt sqrt", "40,03078475546843","√( √( √( sqr( -2567890 ) ) ) )");

        testKeys("0,000000000 ⟵", "0,00000000", "");
        testKeys("0,000000000 ⟵ ⟵ ⟵", "0,000000", "");
        testKeys("0,000000000 ⟵ ⟵ ⟵ 00", "0,00000000", "");

        testKeys("1230,000000000 ⟵", "1 230,00000000", "");
        testKeys("1230,000000000 ⟵ ⟵ ⟵", "1 230,000000", "");
    }

    @Test
    void testFullScreenBtn(){

        assertFalse(((Stage) getWindowByIndex(0)).isMaximized());
        clickByRobotOn("FS");
        WaitForAsyncUtils.waitForFxEvents();
        assertTrue(((Stage) getWindowByIndex(0)).isMaximized());
        clickByRobotOn("FS");
    }

    @Test
    void testHideBtn(){

        Stage stage = (Stage) getWindowByIndex(0);
        assertFalse(stage.isIconified());
        clickByRobotOn("HD");
        WaitForAsyncUtils.waitForFxEvents();
        assertTrue(stage.isIconified());
        Platform.runLater(()->stage.setIconified(false));
        WaitForAsyncUtils.waitForFxEvents();
        assertFalse(stage.isIconified());
    }

    @Test
    void testMenuBtn(){

        assertEquals(0,((AnchorPane)find(MENU_CONTAINER_ID)).getChildren().size());
        clickByRobotOn("MENU");
        WaitForAsyncUtils.waitForFxEvents();
        verifyThat("#droppedList", Node::isVisible);
        verifyThat("#menuBtnPressed", Node::isVisible);
        verifyThat("#menuBtnPressed", Node::isVisible);
        verifyThat("#aboutBtn", Node::isVisible);
        clickByRobotOn("MENU");
        WaitForAsyncUtils.waitForFxEvents();
        assertEquals(0,((AnchorPane)find(MENU_CONTAINER_ID)).getChildren().size());
    }

    @Test
    void testByMouseClicks(){

        test("C","0","");
        test(",,,", "0,","");
        test("1,,,", "1,","");
        test("1,,,2", "1,2","");
        test(",,,2", "0,2","");
        test(",,,2,,,,", "0,2","");
        test("12,,,345,,, 67,", "12,34567","");
        test("C","0","");
        test("1234567890,09876","1 234 567 890,09876","");
        test("1234567890,098765","1 234 567 890,098765","");
        test("1234567890,0987654","1 234 567 890,098765","");
        test("1234567890,0987654,","1 234 567 890,098765","");
        test("1234567890,0987654, C","0","");

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
        test("/ = ⟵","0","");
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
        test("1 / 0 = ⟵","0","");
        test("0,0000000000000000 ⟵ 1","0,0000000000000001","");
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
        test(",,,,, = = ,,,,, = = + - * / ,,,,, -","Result is undefined","0  ÷  0  -  ");

        test("0 1/x","Cannot divide by zero","1/( 0 )");
        test("2 / 0 +","Cannot divide by zero","2  ÷  0  +  ");
        test("3 / 0 sqr +","Cannot divide by zero","3  ÷  sqr( 0 )  +  ");
        test("4 / 0 sqrt +","Cannot divide by zero","4  ÷  √( 0 )  +  ");

        test("1 ± sqrt","Invalid input","√( -1 )");
        test("1 ± sqrt 1 + 2 -","3","1  +  2  -  ");

        test("1000000000000000 sqr sqr sqr sqr sqr sqr sqr sqr sqr sqr","Overflow","sqr( sqr( sqr( sqr( sqr( sqr( sqr( sqr( sqr( sqr( 1000000000000000 ) ) ) ) ) ) ) ) ) )");
        test("0,0000000000000001 sqr sqr sqr sqr sqr sqr sqr sqr sqr sqr","Overflow","sqr( sqr( sqr( sqr( sqr( sqr( sqr( sqr( sqr( sqr( 0,0000000000000001 ) ) ) ) ) ) ) ) ) )");

        test("1 + 2 - 3 C", "0", "");
        test("1 + 2 - 3 CE 4 -", "-1", "1  +  2  -  4  -  ");
        test("1 M+ + 2 - MR + M- MR * 3 = MC", "3", "");
        test("1 + 20 % - 3 sqr * 4 sqrt / 5 1/x + 67 ⟵ - 7,8 ± * 8 CE 9 / ,1 +", "-5 778", "1  +  0,2  -  sqr( 3 )  ×  √( 4 )  ÷  1/( 5 )  +  6  -  -7,8  ×  9  ÷  0,1  +  ");

        test("1 + 20 % - 3 sqr * 4 sqrt / 5 1/x + 67 ⟵ - 7,8 ± * 8 CE 9 / ,1 +","-5 778","1  +  0,2  -  sqr( 3 )  ×  √( 4 )  ÷  1/( 5 )  +  6  -  -7,8  ×  9  ÷  0,1  +  ");

    }

    @Test
    void testDragWindow(){

        testDrag(0, 500, 200, 700);
        testDrag(0, -150, 200, 50);

        testDrag(500,0,700, 200);
        testDrag(-150,0,  50, 200);

        testDrag(20, 60, 220, 260);
        testDrag(-20, -60, 180, 140);
        testDrag(100, 100, 300, 300);
        testDrag(100, 200, 300, 400);
        testDrag(-100, -150, 100, 50);
    }

    @Test
    void testResizeWindow(){

        testResize(-100, -100, false, false, 419, 601);
        testResize(0, -100, false, false, 320, 601);
        testResize(-100, 0, false, false, 419, 502);

        testResize(100, 150, true, true, 420, 652);
        testResize(0, 100, true, true, 320, 602);
        testResize(100, 0, true, true, 420, 502);

        testResize(100, -150, true, false, 420, 651);
        testResize(0, -100, true, false, 320, 601);
        testResize(100, 0, true, false, 420, 502);

        testResize(-100, 150, false, true, 419, 652);
        testResize(0, 100, false, true, 320, 602);
        testResize(-100, 0, false, true, 419, 502);

        testResize(100, 100, false, false, 320, 502);
        testResize(-100, -150, true, true, 320, 502);
        testResize(100, -150, false, true, 320, 502);
//        testResize(-100, 150, true, false, 320, 502);

    }

    @Test
    void testFullScreenAfterDrag(){

        testFSWithDrag(0, -300);
        testFSWithDrag(-300, 0);
    }

    @Test
    void testExitMethod(){

        final boolean[] closed = new boolean[1];
        CaptionHandler.instance = new CaptionHandler() {
            @Override
            public void close() {
                closed[0] = true;
            }
        };
        Button close = find("#closeBtn");
        assertFalse(closed[0]);
        Platform.runLater(close::fire);
        WaitForAsyncUtils.waitForFxEvents();
        assertTrue(closed[0]);
    }

    private void testDrag(int x, int y, int expectedX, int expectedY) {

        int[] coordinates = getCurrentCoordinates();
        assertEquals(START_COORDINATES[0],coordinates[0]);
        assertEquals(START_COORDINATES[1],coordinates[1]);
        coordinates = robotDrag(x, y);
        assertEquals(expectedX, coordinates[0]);
        assertEquals(expectedY, coordinates[1]);
        robotDrag(-x,-y);
    }

    private int[] getCurrentCoordinates(){

        Window window = getWindowByIndex(0);
        return new int[]{(int) window.getX(),(int) window.getY()};
    }

    private void testFSWithDrag(int x, int y) {

        WaitForAsyncUtils.waitForFxEvents();
        assertFalse(((Stage) getWindowByIndex(0)).isMaximized());
        robotDrag(x, y);
        assertTrue(((Stage) getWindowByIndex(0)).isMaximized());
        WaitForAsyncUtils.waitForFxEvents();
        clickByRobotOn("FS");
    }

    private int[] robotDrag(int x, int y) {

        Window window = getWindowByIndex(0);
        int minX = (int) window.getX();
        int minY = (int) window.getY();
        robot.mouseMove(minX + 5,minY + 5);
        robot.mousePress(InputEvent.BUTTON1_MASK);
        robot.mouseMove(minX + x + 5,minY + y + 5);
        robot.mouseRelease(InputEvent.BUTTON1_MASK);
        WaitForAsyncUtils.waitForFxEvents();
        return new int[]{(int)window.getX(),(int)window.getY()};
    }

    private void testResize(int x, int y, boolean rightSide, boolean bottomSide, double expectedWidth, double expectedHeight) {

        Scene scene = getWindowByIndex(0).getScene();
        assertEquals(START_SIZE[0], scene.getWidth());
        assertEquals(START_SIZE[1], scene.getHeight());
        Window window = scene.getWindow();
        int leftMargin = 1;
        if (rightSide) {
            leftMargin = (int)window.getWidth() - 1;
        }
        int upMargin = 1;
        if (bottomSide) {
            upMargin = (int)window.getHeight() - 1;
        }
        int startX = (int)window.getX() + leftMargin;
        int startY = (int)window.getY() + upMargin;
        resizeByMouse(startX,startY,startX+x,startY+y);
        assertEquals(expectedWidth, scene.getWidth());
        assertEquals(expectedHeight, scene.getHeight());
        resizeByMouse(startX+x,startY+y,startX,startY);
        WaitForAsyncUtils.waitForFxEvents();
        assertEquals(START_SIZE[0], scene.getWidth());
        assertEquals(START_SIZE[1], scene.getHeight());
    }

    private void resizeByMouse(int startX, int startY, int endX,int endY){

        WaitForAsyncUtils.waitForFxEvents();
        robot.mouseMove(startX,startY);
        robot.mousePress(InputEvent.BUTTON1_MASK);
        robot.mouseMove(endX,endY);
        robot.mouseRelease(InputEvent.BUTTON1_MASK);
        WaitForAsyncUtils.waitForFxEvents();
    }

    private void testKeys(String expression, String display, String history){

        for (String btnName : parseExpression(expression)) {
            pressOn(btnName);
        }
        waitAndAssert(DISPLAY_LABEL_ID,display);
        waitAndAssert(HISTORY_LABEL_ID,history);
        pressOn("C");
    }

    private void pressOn(String key){

        ButtonForTest buttonForTest = buttons.get(key);
        boolean shiftPressed = buttonForTest.isShiftPressed();
        if (shiftPressed){
            robot.keyPress(KeyEvent.VK_SHIFT);
            robot.keyPress(buttonForTest.getKeyCode());
            WaitForAsyncUtils.waitForFxEvents();
            robot.keyRelease(buttonForTest.getKeyCode());
            robot.keyRelease(KeyEvent.VK_SHIFT);
        }else {
            robot.keyPress(buttonForTest.getKeyCode());
            WaitForAsyncUtils.waitForFxEvents();
        }
    }

    private void clickByRobotOn(String key){

        Node node = find(buttons.get(key).getId());
        Bounds bounds = node.localToScreen(node.getBoundsInLocal());
        int x = (int)bounds.getMinX()+3;
        int y = (int)bounds.getMinY()+3;
        robot.mouseMove(x,y);
        robot.mousePress(java.awt.event.InputEvent.BUTTON1_MASK);
        robot.mouseRelease(java.awt.event.InputEvent.BUTTON1_MASK);
    }

    private void test(String expression, String display, String history){

        WaitForAsyncUtils.waitForFxEvents();
        for (String btnName : parseExpression(expression)) {
            clickByRobotOn(btnName);
        }
        waitAndAssert(DISPLAY_LABEL_ID,display);
        waitAndAssert(HISTORY_LABEL_ID,history);
        clickByRobotOn("C");
    }

    private void waitAndAssert(String id, String expected){

        Label label = find(id);
        try {
            GuiTest.waitUntil(label,LabeledMatchers.hasText(expected),2);
        } catch (Exception e){
            assertEquals(expected,label.getText());
        }

    }

    private ArrayList<String> parseExpression(String expression){

        String[] parsedEventStrings = expression.split(" ");
        ArrayList<String> events = new ArrayList<>();
        for (String str : parsedEventStrings) {
            if (str.matches(IS_DIGIT_REGEX) || COMA.equals(str)) {
                for (char ch : str.toCharArray()) {
                    events.add(ch + "");
                }
            } else {
                events.add(str);
            }
        }
        return events;
    }
}
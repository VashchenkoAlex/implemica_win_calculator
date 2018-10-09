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
import win_calculator.view.handlers.CaptionHandler;

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
import static win_calculator.InitializerTestMaps.createButtonsMap;
import static win_calculator.WindowBorder.*;

/**
 * Class with tests for GUI.
 * Uses java.awt.Robot for mouse and keyboard events.
 * Tests for hide, fullscreen, exit and menu additional buttons.
 * Tests for drag and resize actions.
 * Tests for each button clicking by mouse
 * Tests for each button pressing by keyboard
 */
class GUITest {
    /**
     * Class constants
     */
    private static final Map<String, ButtonForTest> buttons = createButtonsMap();
    private static final int[] START_COORDINATES = new int[]{200, 200};
    private static final int[] START_SIZE = new int[]{320, 502};

    private static final String DISPLAY_LABEL_ID = "#display";
    private static final String HISTORY_LABEL_ID = "#historyField";
    private static final String MENU_CONTAINER_ID = "#dropDownContainer";
    private static final String IS_DIGIT_REGEX = "\\d*(,*\\d*,*)?";
    private static final String COMA = ",";

    /**
     * awt Robot
     */
    private static Robot robot;

    /**
     * Initializes application and robot before tests
     * @throws Exception when something went wrong
     */
    @BeforeAll
    static void initialize() throws Exception {
        new Thread("FXApp") {
            public void run() {
                WinCalculator.main(new String[0]);
            }
        }.start();
        while (!FxToolkit.isFXApplicationThreadRunning()) {
            Thread.sleep(1);
        }
        WaitForAsyncUtils.waitForFxEvents();
        robot = new Robot();
    }

    @Test
    void testByKeysPress() {

        //testing cases for keyboard buttons by robot
        testKeys(", ±", "-0,", "");
        testKeys(",00 ±", "-0,00", "");
        testKeys(",005 ±", "-0,005", "");
        testKeys(",00 ± ±", "0,00", "");
        testKeys("C ⟵", "0", "");
        testKeys(",00500,", "0,00500", "");
        testKeys("1111111111111111,", "1 111 111 111 111 111,", "");
        testKeys("20 + %", "4", "20  +  4");
        testKeys("12 + 34 - 56 * 78 / 90 =", "-8,666666666666667", "");
        testKeys("1, , , 2 + 34 - 56 * 78 / 90 =", "-18,02666666666667", "");
        testKeys("1 , , , 2 + 34 - 56 * 78 / 90 = C", "0", "");
        testKeys("1 + 2 + 3 + 4 =", "10", "");
        testKeys("123 ± + 45 % - 67 sqr * 890 sqrt / 24 1/x = + 68 - 01 * 35 / 79 + 2,4 = - 68 * 13 / 57 =", "-337 673,2047848424", "");
        testKeys("9876543210 + 13579135 % - 24680 * 1234 / 4321 = sqr sqrt", "383 010 928 309 372,9", "√( sqr( 383010928309372,9 ) )");
        testKeys("9876543210 + % =", "9,754610676665143e+17", "");
        testKeys("9876543210 ± ± ± ± ± ± ± ± + % % = =", "1,926836657386991e+26", "");
        testKeys("9876543210 ± % = ", "0", "");
        testKeys("1234567890 sqr + sqrt ± % % %", "-4,371241896208856e+57", "sqr( 1234567890 )  +  -4,371241896208856e+57");
        testKeys(",00000001", "0,00000001", "");
        testKeys(",0000001 ±", "-0,0000001", "");
        testKeys(",000 , , , 00001 = = = =", "0,00000001", "");
        testKeys(",00000001 - % % %", "1,e-38", "0,00000001  -  1,e-38");
        testKeys("1234567890,01234567 *", "1 234 567 890,012345", "1234567890,012345  ×  ");
        testKeys("25 ± ⟵", "-2", "");
        testKeys("1234 ± ⟵ ⟵ ⟵", "-1", "");
        testKeys("1234 ± ⟵ ⟵ / 2 -", "-6", "-12  ÷  2  -  ");
        testKeys("1234567890,01234567 ⟵ ⟵ ⟵ ⟵ /", "1 234 567 890,01", "1234567890,01  ÷  ");
        testKeys("1234567890,567 ± ⟵ ⟵ ⟵ ⟵ /", "-1 234 567 890", "-1234567890  ÷  ");
        testKeys("1234567890,567 ± sqr ⟵ ⟵ ⟵ ⟵ /", "1,524157876419052e+18", "sqr( -1234567890,567 )  ÷  ");
        testKeys("2567890,134 ± ⟵ ⟵ ⟵ ⟵ sqr sqrt sqrt sqrt", "40,03078475546843", "√( √( √( sqr( -2567890 ) ) ) )");
        testKeys("0,000000000 ⟵", "0,00000000", "");
        testKeys("0,000000000 ⟵ ⟵ ⟵", "0,000000", "");
        testKeys("0,000000000 ⟵ ⟵ ⟵ 00", "0,00000000", "");
        testKeys("1230,000000000 ⟵", "1 230,00000000", "");
        testKeys("1230,000000000 ⟵ ⟵ ⟵", "1 230,000000", "");
    }

    @Test
    void testFullScreenBtnByMouseClick() {

        assertFalse(((Stage) getWindowByIndex(0)).isMaximized());
        clickByRobotOn("FS");
        WaitForAsyncUtils.waitForFxEvents();
        assertTrue(((Stage) getWindowByIndex(0)).isMaximized());
        clickByRobotOn("FS");
    }

    @Test
    void testHideBtnByMouseClick() {

        Stage stage = (Stage) getWindowByIndex(0);
        assertFalse(stage.isIconified());
        clickByRobotOn("HD");
        WaitForAsyncUtils.waitForFxEvents();
        assertTrue(stage.isIconified());
        Platform.runLater(() -> stage.setIconified(false));
        WaitForAsyncUtils.waitForFxEvents();
    }

    @Test
    void testMenuBtnByMouseClick() {

        assertEquals(0, ((AnchorPane) find(MENU_CONTAINER_ID)).getChildren().size());
        clickByRobotOn("MENU");
        WaitForAsyncUtils.waitForFxEvents();
        verifyThat("#droppedList", Node::isVisible);
        verifyThat("#menuBtnPressed", Node::isVisible);
        verifyThat("#menuBtnPressed", Node::isVisible);
        verifyThat("#aboutBtn", Node::isVisible);
        clickByRobotOn("MENU");
        WaitForAsyncUtils.waitForFxEvents();
        assertEquals(0, ((AnchorPane) find(MENU_CONTAINER_ID)).getChildren().size());
    }

    @Test
    void testByMouseClicks() {

        //testing cases for mouse clicks by robot
        testByMouse("C", "0", "");
        testByMouse(",,,", "0,", "");
        testByMouse("1,,,", "1,", "");
        testByMouse("1,,,2", "1,2", "");
        testByMouse(",,,2", "0,2", "");
        testByMouse(",,,2,,,,", "0,2", "");
        testByMouse("12,,,345,,, 67,", "12,34567", "");
        testByMouse("C", "0", "");
        testByMouse("1234567890,09876", "1 234 567 890,09876", "");
        testByMouse("1234567890,098765", "1 234 567 890,098765", "");
        testByMouse("1234567890,0987654", "1 234 567 890,098765", "");
        testByMouse("1234567890,0987654,", "1 234 567 890,098765", "");
        testByMouse("1234567890,0987654, C", "0", "");

        testByMouse("2", "2", "");
        testByMouse("2 +", "2", "2  +  ");
        testByMouse("2 + 3", "3", "2  +  ");
        testByMouse("2 + 3 -", "5", "2  +  3  -  ");
        testByMouse("2 + 3 - *", "5", "2  +  3  ×  ");
        testByMouse("2 + 3 - * 4", "4", "2  +  3  ×  ");
        testByMouse("2 + 3 - * 4 /", "20", "2  +  3  ×  4  ÷  ");
        testByMouse("2 + 3 - * 4 / 9", "9", "2  +  3  ×  4  ÷  ");
        testByMouse("2 + 3 - * 4 / 9 sqrt", "3", "2  +  3  ×  4  ÷  √( 9 )");
        testByMouse("2 + 3 - * 4 / 9 sqrt sqr", "9", "2  +  3  ×  4  ÷  sqr( √( 9 ) )");
        testByMouse("2 + 3 - * 4 / 9 sqrt sqr *", "2,222222222222222", "2  +  3  ×  4  ÷  sqr( √( 9 ) )  ×  ");
        testByMouse("2 + 3 - * 4 / 9 sqrt sqr * CE", "0", "2  +  3  ×  4  ÷  sqr( √( 9 ) )  ×  ");

        testByMouse("/ =", "Result is undefined", "0  ÷  ");
        testByMouse("/ = MC", "Result is undefined", "0  ÷  ");
        testByMouse("/ = MR", "Result is undefined", "0  ÷  ");
        testByMouse("/ = M+", "Result is undefined", "0  ÷  ");
        testByMouse("/ = M-", "Result is undefined", "0  ÷  ");
        testByMouse("/ = MS", "Result is undefined", "0  ÷  ");
        testByMouse("/ = %", "Result is undefined", "0  ÷  ");
        testByMouse("/ = sqrt", "Result is undefined", "0  ÷  ");
        testByMouse("/ = sqr", "Result is undefined", "0  ÷  ");
        testByMouse("/ = /", "Result is undefined", "0  ÷  ");
        testByMouse("/ = *", "Result is undefined", "0  ÷  ");
        testByMouse("/ = -", "Result is undefined", "0  ÷  ");
        testByMouse("/ = +", "Result is undefined", "0  ÷  ");
        testByMouse("/ = ±", "Result is undefined", "0  ÷  ");
        testByMouse("/ = ,", "Result is undefined", "0  ÷  ");

        testByMouse("/ = CE", "0", "");
        testByMouse("/ = C", "0", "");
        testByMouse("/ = ⟵", "0", "");
        testByMouse("/ = 1", "1", "");
        testByMouse("/ = 2", "2", "");
        testByMouse("/ = 3", "3", "");
        testByMouse("/ = 4", "4", "");
        testByMouse("/ = 5", "5", "");
        testByMouse("/ = 6", "6", "");
        testByMouse("/ = 7", "7", "");
        testByMouse("/ = 8", "8", "");
        testByMouse("/ = 9", "9", "");
        testByMouse("/ = 0", "0", "");

        testByMouse("1 / 0 =", "Cannot divide by zero", "1  ÷  ");
        testByMouse("1 / 0 = MC", "Cannot divide by zero", "1  ÷  ");
        testByMouse("1 / 0 = MR", "Cannot divide by zero", "1  ÷  ");
        testByMouse("1 / 0 = MS", "Cannot divide by zero", "1  ÷  ");
        testByMouse("1 / 0 = M+", "Cannot divide by zero", "1  ÷  ");
        testByMouse("1 / 0 = M-", "Cannot divide by zero", "1  ÷  ");
        testByMouse("1 / 0 = %", "Cannot divide by zero", "1  ÷  ");
        testByMouse("1 / 0 = /", "Cannot divide by zero", "1  ÷  ");
        testByMouse("1 / 0 = *", "Cannot divide by zero", "1  ÷  ");
        testByMouse("1 / 0 = -", "Cannot divide by zero", "1  ÷  ");
        testByMouse("1 / 0 = +", "Cannot divide by zero", "1  ÷  ");
        testByMouse("1 / 0 = ±", "Cannot divide by zero", "1  ÷  ");
        testByMouse("1 / 0 = ,", "Cannot divide by zero", "1  ÷  ");

        testByMouse("1 / 0 = CE", "0", "");
        testByMouse("1 / 0 = C", "0", "");
        testByMouse("1 / 0 = ⟵", "0", "");
        testByMouse("0,0000000000000000 ⟵ 1", "0,0000000000000001", "");
        testByMouse("1 / 0 = 1", "1", "");
        testByMouse("1 / 0 = 2", "2", "");
        testByMouse("1 / 0 = 3", "3", "");
        testByMouse("1 / 0 = 4", "4", "");
        testByMouse("1 / 0 = 5", "5", "");
        testByMouse("1 / 0 = 6", "6", "");
        testByMouse("1 / 0 = 7", "7", "");
        testByMouse("1 / 0 = 8", "8", "");
        testByMouse("1 / 0 = 9", "9", "");
        testByMouse("1 / 0 = 0", "0", "");
        testByMouse(", = + / , , - MC", "Result is undefined", "0  ÷  0  -  ");
        testByMouse(",,,,, = = ,,,,, = = + - * / ,,,,, -", "Result is undefined", "0  ÷  0  -  ");
        testByMouse("0 1/x", "Cannot divide by zero", "1/( 0 )");
        testByMouse("2 / 0 +", "Cannot divide by zero", "2  ÷  0  +  ");
        testByMouse("3 / 0 sqr +", "Cannot divide by zero", "3  ÷  sqr( 0 )  +  ");
        testByMouse("4 / 0 sqrt +", "Cannot divide by zero", "4  ÷  √( 0 )  +  ");
        testByMouse("1 ± sqrt", "Invalid input", "√( -1 )");
        testByMouse("1 ± sqrt 1 + 2 -", "3", "1  +  2  -  ");
        testByMouse("1000000000000000 sqr sqr sqr sqr sqr sqr sqr sqr sqr sqr", "Overflow", "sqr( sqr( sqr( sqr( sqr( sqr( sqr( sqr( sqr( sqr( 1000000000000000 ) ) ) ) ) ) ) ) ) )");
        testByMouse("0,0000000000000001 sqr sqr sqr sqr sqr sqr sqr sqr sqr sqr", "Overflow", "sqr( sqr( sqr( sqr( sqr( sqr( sqr( sqr( sqr( sqr( 0,0000000000000001 ) ) ) ) ) ) ) ) ) )");
        testByMouse("1 + 2 - 3 C", "0", "");
        testByMouse("1 + 2 - 3 CE 4 -", "-1", "1  +  2  -  4  -  ");
        testByMouse("1 M+ + 2 - MR + M- MR * 3 = MC", "3", "");
        testByMouse("1 + 20 % - 3 sqr * 4 sqrt / 5 1/x + 67 ⟵ - 7,8 ± * 8 CE 9 / ,1 +", "-5 778", "1  +  0,2  -  sqr( 3 )  ×  √( 4 )  ÷  1/( 5 )  +  6  -  -7,8  ×  9  ÷  0,1  +  ");
        testByMouse("1 + 20 % - 3 sqr * 4 sqrt / 5 1/x + 67 ⟵ - 7,8 ± * 8 CE 9 / ,1 +", "-5 778", "1  +  0,2  -  sqr( 3 )  ×  √( 4 )  ÷  1/( 5 )  +  6  -  -7,8  ×  9  ÷  0,1  +  ");
    }

    @Test
    void testDragWindowByMouse() {

        testDrag(0, 5, 200, 205);
        testDrag(0, 50, 200, 250);
        testDrag(0, 100, 200, 300);
        testDrag(0, 500, 200, 700);
        testDrag(0, 693, 200, 893);
        testDrag(0, 694, 200, 894);
        testDrag(0, 695, 200, 894);
        testDrag(0, -5, 200, 195);
        testDrag(0, -50, 200, 150);
        testDrag(0, -100, 200, 100);
        testDrag(0, -199, 200, 1);
        testDrag(0, -200, 200, 0);

        testDrag(5, 0, 205, 200);
        testDrag(50, 0, 250, 200);
        testDrag(100, 0, 300, 200);
        testDrag(500, 0, 700, 200);
        testDrag(1393, 0, 1593, 200);
        testDrag(1394, 0, 1594, 200);
        testDrag(1395, 0, 1594, 200);
        testDrag(-5, 0, 195, 200);
        testDrag(-50, 0, 150, 200);
        testDrag(-100, 0, 100, 200);
        testDrag(-199, 0, 1, 200);
        testDrag(-200, 0, 0, 200);

        testDrag(7, 7, 207, 207);
        testDrag(70, 70, 270, 270);
        testDrag(150, 150, 350, 350);
        testDrag(700, 400, 900, 600);
        testDrag(1393, 693, 1593, 893);
        testDrag(1394, 694, 1594, 894);
        testDrag(1395, 695, 1594, 894);
        testDrag(-7, -7, 193, 193);
        testDrag(-70, -70, 130, 130);
        testDrag(-150, -150, 50, 50);
        testDrag(-200, -199, 0, 1);
        testDrag(-200, -200, 0, 0);
        testDrag(-7, 7, 193, 207);
        testDrag(-70, 70, 130, 270);
        testDrag(-150, 150, 50, 350);
        testDrag(-200, 400, 0, 600);
        testDrag(-200, 693, 0, 893);
        testDrag(-200, 694, 0, 894);
        testDrag(-200, 695, 0, 894);
        testDrag(7, -7, 207, 193);
        testDrag(70, -70, 270, 130);
        testDrag(150, -150, 350, 50);
        testDrag(700, -199, 900, 1);
        testDrag(1393, -200, 1593, 0);
        testDrag(1394, -200, 1594, 0);
        testDrag(1395, -200, 1594, 0);
    }

    @Test
    void testResizeWindowFromAngleByMouse() {

        //Tests from right border
        testResizeFromBorder(5,0,RIGHT,325,502);
        testResizeFromBorder(50,0,RIGHT,370,502);
        testResizeFromBorder(200,0,RIGHT,520,502);
        testResizeFromBorder(1081,0,RIGHT,1401,502);
        testResizeFromBorder(1082,0,RIGHT,1401,502);
        testResizeFromBorder(-20,0,RIGHT,320,502);
        testResizeFromBorder(-50,0,RIGHT,320,502);
        testResizeFromBorder(-200,0,RIGHT,320,502);
        testResizeFromBorder(-300,0,RIGHT,320,502);

        //Tests from left border
        testResizeFromBorder(-5,0,LEFT,324,502);
        testResizeFromBorder(-50,0,LEFT,369,502);
        testResizeFromBorder(-200,0,LEFT,519,502);
        testResizeFromBorder(-201,0,LEFT,520,502);
        testResizeFromBorder(-202,0,LEFT,520,502);
        testResizeFromBorder(5,0,LEFT,320,502);
        testResizeFromBorder(50,0,LEFT,320,502);
        testResizeFromBorder(200,0,LEFT,320,502);
        testResizeFromBorder(315,0,LEFT,320,502);

        //Tests from bottom border
        testResizeFromBorder(0, 5,BOTTOM,320,507);
        testResizeFromBorder(0, 50,BOTTOM,320,552);
        testResizeFromBorder(0, 197,BOTTOM,320,699);
        testResizeFromBorder(0, 198,BOTTOM,320,700);
        testResizeFromBorder(0, 199,BOTTOM,320,700);
        testResizeFromBorder(0, -5,BOTTOM,320,502);
        testResizeFromBorder(0, -50,BOTTOM,320,502);
        testResizeFromBorder(0, -300,BOTTOM,320,502);

        //Tests from top border
        testResizeFromBorder(0, -5,TOP,320,506);
        testResizeFromBorder(0, -50,TOP,320,551);
        testResizeFromBorder(0, -200,TOP,320,701);
        testResizeFromBorder(0, -201,TOP,320,702);
        testResizeFromBorder(0, -250,TOP,320,702);
        testResizeFromBorder(0, 5,TOP,320,502);
        testResizeFromBorder(0, 50,TOP,320,502);
        testResizeFromBorder(0, 400,TOP,320,502);

        //Tests from left-top angle
        testResizeFromAngle(-5, -5, false, false, 324, 506);
        testResizeFromAngle(-100, -100, false, false, 419, 601);
        testResizeFromAngle(-200, -200, false, false, 519, 701);
        testResizeFromAngle(-201, -201, false, false, 520, 702);
        testResizeFromAngle(-202, -202, false, false, 520, 702);
        testResizeFromAngle(5, 5, false, false, 320, 502);
        testResizeFromAngle(100, 100, false, false, 320, 502);
        testResizeFromAngle(200, 200, false, false, 320, 502);
        testResizeFromAngle(320, 502, false, false, 320, 502);
        testResizeFromAngle(420, 602, false, false, 320, 502);
        testResizeFromAngle(0, -5, false, false, 320, 506);
        testResizeFromAngle(0, -100, false, false, 320, 601);
        testResizeFromAngle(0, -200, false, false, 320, 701);
        testResizeFromAngle(0, -201, false, false, 320, 702);
        testResizeFromAngle(0, -202, false, false, 320, 702);
        testResizeFromAngle(0, 5, false, false, 320, 502);
        testResizeFromAngle(0, 100, false, false, 320, 502);
        testResizeFromAngle(0, 200, false, false, 320, 502);
        testResizeFromAngle(0, 502, false, false, 320, 502);
        testResizeFromAngle(0, 602, false, false, 320, 502);
        testResizeFromAngle(-5, 0, false, false, 324, 502);
        testResizeFromAngle(-100, 0, false, false, 419, 502);
        testResizeFromAngle(-200, 0, false, false, 519, 502);
        testResizeFromAngle(-201, 0, false, false, 520, 502);
        testResizeFromAngle(-213, 0, false, false, 520, 502);
        testResizeFromAngle(-220, 0, false, false, 520, 502);
        testResizeFromAngle(5, 0, false, false, 320, 502);
        testResizeFromAngle(100, 0, false, false, 320, 502);
        testResizeFromAngle(200, 0, false, false, 320, 502);
        testResizeFromAngle(320, 0, false, false, 320, 502);
        testResizeFromAngle(420, 0, false, false, 320, 502);

        //Tests from right-bottom angle
        testResizeFromAngle(5, 5, true, true, 325, 507);
        testResizeFromAngle(100, 100, true, true, 420, 602);
        testResizeFromAngle(1077, 197, true, true, 1397, 699);
        testResizeFromAngle(1078, 198, true, true, 1398, 700);
        testResizeFromAngle(1079, 199, true, true, 1399, 700);
        testResizeFromAngle(1080, 200, true, true, 1400, 700);
        testResizeFromAngle(1081, 201, true, true, 1400, 700);
        testResizeFromAngle(-5, -5, true, true, 320, 502);
        testResizeFromAngle(-100, -100, true, true, 320, 502);
        testResizeFromAngle(-320, -502, true, true, 320, 502);
        testResizeFromAngle(-420, -502, true, true, 320, 502);
        testResizeFromAngle(0, 5, true, true, 320, 507);
        testResizeFromAngle(0, 100, true, true, 320, 602);
        testResizeFromAngle(0, 197, true, true, 320, 699);
        testResizeFromAngle(0, 198, true, true, 320, 700);
        testResizeFromAngle(0, 199, true, true, 320, 700);
        testResizeFromAngle(0, -5, true, true, 320, 502);
        testResizeFromAngle(0, -100, true, true, 320, 502);
        testResizeFromAngle(0, -502, true, true, 320, 502);
        testResizeFromAngle(0, -502, true, true, 320, 502);
        testResizeFromAngle(5, 0, true, true, 325, 502);
        testResizeFromAngle(100, 0, true, true, 420, 502);
        testResizeFromAngle(1079, 0, true, true, 1399, 502);
        testResizeFromAngle(1080, 0, true, true, 1400, 502);
        testResizeFromAngle(1081, 0, true, true, 1400, 502);
        testResizeFromAngle(-5, 0, true, true, 320, 502);
        testResizeFromAngle(-100, 0, true, true, 320, 502);
        testResizeFromAngle(-320, 0, true, true, 320, 502);
        testResizeFromAngle(-420, 0, true, true, 320, 502);

        //Tests from right-top angle
        testResizeFromAngle(5, -5, true, false, 325, 506);
        testResizeFromAngle(100, -100, true, false, 420, 601);
        testResizeFromAngle(1077, -197, true, false, 1397, 698);
        testResizeFromAngle(1078, -198, true, false, 1398, 699);
        testResizeFromAngle(1079, -199, true, false, 1399, 700);
        testResizeFromAngle(1080, -200, true, false, 1400, 701);
        testResizeFromAngle(1080, -201, true, false, 1400, 702);
        testResizeFromAngle(1080, -202, true, false, 1400, 702);
        testResizeFromAngle(-5, 5, true, false, 320, 502);
        testResizeFromAngle(-100, 100, true, false, 320, 502);
        testResizeFromAngle(-320, 502, true, false, 320, 502);
        testResizeFromAngle(-420, 502, true, false, 320, 502);
        testResizeFromAngle(0, -5, true, false, 320, 506);
        testResizeFromAngle(0, -100, true, false, 320, 601);
        testResizeFromAngle(0, -200, true, false, 320, 701);
        testResizeFromAngle(0, -201, true, false, 320, 702);
        testResizeFromAngle(0, -202, true, false, 320, 702);
        testResizeFromAngle(5, 0, true, false, 325, 502);
        testResizeFromAngle(100, 0, true, false, 420, 502);
        testResizeFromAngle(1079, 0, true, false, 1399, 502);
        testResizeFromAngle(1080, 0, true, false, 1400, 502);
        testResizeFromAngle(1080, 0, true, false, 1400, 502);

        //Tests from left-bottom angle
        testResizeFromAngle(-5, 5, false, true, 324, 507);
        testResizeFromAngle(-100, 100, false, true, 419, 602);
        testResizeFromAngle(-200, 197, false, true, 519, 699);
        testResizeFromAngle(-201, 198, false, true, 520, 700);
        testResizeFromAngle(-202, 199, false, true, 520, 700);
        testResizeFromAngle(5, -5, false, true, 320, 502);
        testResizeFromAngle(100, -100, false, true, 320, 502);
        testResizeFromAngle(320, -502, false, true, 320, 502);
        testResizeFromAngle(321, -503, false, true, 320, 502);
        testResizeFromAngle(1400, -720, false, true, 320, 502);
        testResizeFromAngle(0, 5, false, true, 320, 507);
        testResizeFromAngle(0, 100, false, true, 320, 602);
        testResizeFromAngle(0, 197, false, true, 320, 699);
        testResizeFromAngle(0, 198, false, true, 320, 700);
        testResizeFromAngle(0, 199, false, true, 320, 700);
        testResizeFromAngle(-5, 0, false, true, 324, 502);
        testResizeFromAngle(-100, 0, false, true, 419, 502);
        testResizeFromAngle(-200, 0, false, true, 519, 502);
        testResizeFromAngle(-201, 0, false, true, 520, 502);
        testResizeFromAngle(-202, 0, false, true, 520, 502);
    }


    @Test
    void testFullScreenAfterDragByMouse() {

        testFullScreenWithDrag(0, -300);
        testFullScreenWithDrag(-300, 0);
    }

    @Test
    void testExitMethodByMouse() {

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

    private void testDrag(int x, int y, int expectedX, int expectedY) {

        WaitForAsyncUtils.waitForFxEvents();
        int[] coordinates = getCurrentCoordinates();
        assertEquals(START_COORDINATES[0], coordinates[0]);
        assertEquals(START_COORDINATES[1], coordinates[1]);
        coordinates = robotDrag(x, y);
        assertEquals(expectedX, coordinates[0]);
        assertEquals(expectedY, coordinates[1]);
        resetWindow();
    }

    private int[] getCurrentCoordinates() {

        Window window = getWindowByIndex(0);
        return new int[]{(int) window.getX(), (int) window.getY()};
    }

    private void testFullScreenWithDrag(int x, int y) {

        WaitForAsyncUtils.waitForFxEvents();
        assertFalse(((Stage) getWindowByIndex(0)).isMaximized());
        robotDrag(x, y);
        WaitForAsyncUtils.waitForFxEvents();
        assertTrue(((Stage) getWindowByIndex(0)).isMaximized());
        WaitForAsyncUtils.waitForFxEvents();
        clickByRobotOn("FS");
    }

    private int[] robotDrag(int x, int y) {

        Window window = getWindowByIndex(0);
        int minX = (int) window.getX();
        int minY = (int) window.getY();
        robot.mouseMove(minX + 5, minY + 5);
        robot.mousePress(InputEvent.BUTTON1_MASK);
        robot.mouseMove(minX + x + 5, minY + y + 5);
        robot.mouseRelease(InputEvent.BUTTON1_MASK);
        WaitForAsyncUtils.waitForFxEvents();
        return new int[]{(int) window.getX(), (int) window.getY()};
    }

    private void testResizeFromAngle(int x, int y, boolean rightSide, boolean bottomSide, double expectedWidth, double expectedHeight) {

        WaitForAsyncUtils.waitForFxEvents();
        Scene scene = getWindowByIndex(0).getScene();
        assertEquals(START_SIZE[0], scene.getWidth());
        assertEquals(START_SIZE[1], scene.getHeight());
        int[] angleCoordinates = getStartAngleCoordinates(rightSide, bottomSide);
        resizeByMouse(angleCoordinates[0], angleCoordinates[1], angleCoordinates[0] + x, angleCoordinates[1] + y);
        assertEquals(expectedWidth, scene.getWidth());
        assertEquals(expectedHeight, scene.getHeight());
        resetWindow();
    }

    private void resetWindow(){

        Window window = getWindowByIndex(0);
        window.setX(START_COORDINATES[0]);
        window.setY(START_COORDINATES[1]);
        window.setWidth(START_SIZE[0]);
        window.setHeight(START_SIZE[1]);
    }

    private void testResizeFromBorder(int x, int y, WindowBorder windowBorder,double expectedWidth, double expectedHeight) {

        WaitForAsyncUtils.waitForFxEvents();
        Scene scene = getWindowByIndex(0).getScene();
        assertEquals(START_SIZE[0], scene.getWidth());
        assertEquals(START_SIZE[1], scene.getHeight());
        int[] pointCoordinates = getStartCoordinatesOnBorder(windowBorder);
        resizeByMouse(pointCoordinates[0], pointCoordinates[1], pointCoordinates[0] + x,pointCoordinates[1] + y );
        assertEquals(expectedWidth, scene.getWidth());
        assertEquals(expectedHeight, scene.getHeight());
        resetWindow();
    }

    private int[] getStartCoordinatesOnBorder(WindowBorder windowBorder) {

        int[] coordinates = new int[]{200,200};
        Window window = getWindowByIndex(0);
        switch (windowBorder) {
            case TOP: {
                coordinates[0] += 150;
                coordinates[1] += 1;
                break;
            }
            case BOTTOM: {
                coordinates[0] += 150;
                coordinates[1] += window.getHeight() - 1;
                break;
            }
            case LEFT: {
                coordinates[0] += 1;
                coordinates[1] += 260;
                break;
            }
            case RIGHT: {
                coordinates[0] += window.getWidth() - 2;
                coordinates[1] += 260;
                break;
            }
        }
        return coordinates;
    }

    private int[] getStartAngleCoordinates(boolean rightSide, boolean bottomSide) {

        Window window = getWindowByIndex(0);
        int leftMargin = 1;
        if (rightSide) {
            leftMargin = (int) window.getWidth() - 1;
        }
        int upMargin = 1;
        if (bottomSide) {
            upMargin = (int) window.getHeight() - 1;
        }
        return new int[]{(int) window.getX() + leftMargin, (int) window.getY() + upMargin};
    }

    private void resizeByMouse(int startX, int startY, int endX, int endY) {

        robot.mouseMove(startX, startY);
        robot.mousePress(InputEvent.BUTTON1_MASK);
        robot.mouseMove(endX, endY);
        robot.mouseRelease(InputEvent.BUTTON1_MASK);
        WaitForAsyncUtils.waitForFxEvents();
    }

    private void testKeys(String expression, String display, String history) {

        WaitForAsyncUtils.waitForFxEvents();
        for (String btnName : parseExpression(expression)) {
            pressOn(btnName);
        }
        waitAndAssert(DISPLAY_LABEL_ID, display);
        waitAndAssert(HISTORY_LABEL_ID, history);
        pressOn("C");
    }

    private void pressOn(String key) {

        ButtonForTest buttonForTest = buttons.get(key);
        boolean shiftPressed = buttonForTest.isShiftPressed();
        if (shiftPressed) {
            robot.keyPress(KeyEvent.VK_SHIFT);
            robot.keyPress(buttonForTest.getKeyCode());
            WaitForAsyncUtils.waitForFxEvents();
            robot.keyRelease(buttonForTest.getKeyCode());
            robot.keyRelease(KeyEvent.VK_SHIFT);
        } else {
            robot.keyPress(buttonForTest.getKeyCode());
            WaitForAsyncUtils.waitForFxEvents();
        }
    }

    private void clickByRobotOn(String key) {

        Node node = find(buttons.get(key).getId());
        Bounds bounds = node.localToScreen(node.getBoundsInLocal());
        int x = (int) bounds.getMinX() + 3;
        int y = (int) bounds.getMinY() + 3;
        robot.mouseMove(x, y);
        robot.mousePress(java.awt.event.InputEvent.BUTTON1_MASK);
        robot.mouseRelease(java.awt.event.InputEvent.BUTTON1_MASK);
    }

    private void testByMouse(String expression, String display, String history) {

        WaitForAsyncUtils.waitForFxEvents();
        for (String btnName : parseExpression(expression)) {
            clickByRobotOn(btnName);
        }
        waitAndAssert(DISPLAY_LABEL_ID, display);
        waitAndAssert(HISTORY_LABEL_ID, history);
        clickByRobotOn("C");
        clickByRobotOn("MC");
    }

    private void waitAndAssert(String id, String expected) {

        Label label = find(id);
        try {
            GuiTest.waitUntil(label, LabeledMatchers.hasText(expected), 2);
        } catch (Exception e) {
            assertEquals(expected, label.getText());
        }

    }

    private ArrayList<String> parseExpression(String expression) {

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
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
import win_calculator.controller.handlers.CaptionHandler;

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
import static win_calculator.WindowBorder.*;

class GUITest {

    private static Robot robot;
    private static final Map<String, ButtonForTest> buttons = createButtonsMap();
    private static final String IS_DIGIT_REGEX = "\\d*(,*\\d*,*)?";
    private static final String COMA = ",";
    private static final int[] START_COORDINATES = new int[]{200, 200};
    private static final int[] START_SIZE = new int[]{320, 502};
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
        while (!FxToolkit.isFXApplicationThreadRunning()) {
            Thread.sleep(1);
        }
        WaitForAsyncUtils.waitForFxEvents();
        robot = new Robot();
    }

    @Test
    void testByKeysPress() {

        testKeys(", ±", "-0,", "");
        testKeys(",00 ±", "-0,00", "");
        testKeys(",005 ±", "-0,005", "");
        testKeys(",00 ± ±", "0,00", "");

        testKeys("C ⟵", "0", "");

        testKeys(",00500,", "0,00500", "");

        testKeys("1111111111111111,", "1 111 111 111 111 111,", "");

        testKeys("20 + %", "4", "20  +  4");
        testKeys("12 + 34 - 56 * 78 / 90 = ", "-8,666666666666667", "");
        testKeys("1, , , 2 + 34 - 56 * 78 / 90 = ", "-18,02666666666667", "");
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
    void testFullScreenBtn() {

        assertFalse(((Stage) getWindowByIndex(0)).isMaximized());
        clickByRobotOn("FS");
        WaitForAsyncUtils.waitForFxEvents();
        assertTrue(((Stage) getWindowByIndex(0)).isMaximized());
        clickByRobotOn("FS");
    }

    @Test
    void testHideBtn() {

        Stage stage = (Stage) getWindowByIndex(0);
        assertFalse(stage.isIconified());
        clickByRobotOn("HD");
        WaitForAsyncUtils.waitForFxEvents();
        assertTrue(stage.isIconified());
        Platform.runLater(() -> stage.setIconified(false));
        WaitForAsyncUtils.waitForFxEvents();
    }

    @Test
    void testMenuBtn() {

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

        test("C", "0", "");
        test(",,,", "0,", "");
        test("1,,,", "1,", "");
        test("1,,,2", "1,2", "");
        test(",,,2", "0,2", "");
        test(",,,2,,,,", "0,2", "");
        test("12,,,345,,, 67,", "12,34567", "");
        test("C", "0", "");
        test("1234567890,09876", "1 234 567 890,09876", "");
        test("1234567890,098765", "1 234 567 890,098765", "");
        test("1234567890,0987654", "1 234 567 890,098765", "");
        test("1234567890,0987654,", "1 234 567 890,098765", "");
        test("1234567890,0987654, C", "0", "");

        test("2", "2", "");
        test("2 +", "2", "2  +  ");
        test("2 + 3", "3", "2  +  ");
        test("2 + 3 -", "5", "2  +  3  -  ");
        test("2 + 3 - *", "5", "2  +  3  ×  ");
        test("2 + 3 - * 4", "4", "2  +  3  ×  ");
        test("2 + 3 - * 4 /", "20", "2  +  3  ×  4  ÷  ");
        test("2 + 3 - * 4 / 9", "9", "2  +  3  ×  4  ÷  ");
        test("2 + 3 - * 4 / 9 sqrt", "3", "2  +  3  ×  4  ÷  √( 9 )");
        test("2 + 3 - * 4 / 9 sqrt sqr", "9", "2  +  3  ×  4  ÷  sqr( √( 9 ) )");
        test("2 + 3 - * 4 / 9 sqrt sqr *", "2,222222222222222", "2  +  3  ×  4  ÷  sqr( √( 9 ) )  ×  ");
        test("2 + 3 - * 4 / 9 sqrt sqr * CE", "0", "2  +  3  ×  4  ÷  sqr( √( 9 ) )  ×  ");

        test("/ =", "Result is undefined", "0  ÷  ");
        test("/ = MC", "Result is undefined", "0  ÷  ");
        test("/ = MR", "Result is undefined", "0  ÷  ");
        test("/ = M+", "Result is undefined", "0  ÷  ");
        test("/ = M-", "Result is undefined", "0  ÷  ");
        test("/ = MS", "Result is undefined", "0  ÷  ");
        test("/ = %", "Result is undefined", "0  ÷  ");
        test("/ = sqrt", "Result is undefined", "0  ÷  ");
        test("/ = sqr", "Result is undefined", "0  ÷  ");
        test("/ = /", "Result is undefined", "0  ÷  ");
        test("/ = *", "Result is undefined", "0  ÷  ");
        test("/ = -", "Result is undefined", "0  ÷  ");
        test("/ = +", "Result is undefined", "0  ÷  ");
        test("/ = ±", "Result is undefined", "0  ÷  ");
        test("/ = ,", "Result is undefined", "0  ÷  ");

        test("/ = CE", "0", "");
        test("/ = C", "0", "");
        test("/ = ⟵", "0", "");
        test("/ = 1", "1", "");
        test("/ = 2", "2", "");
        test("/ = 3", "3", "");
        test("/ = 4", "4", "");
        test("/ = 5", "5", "");
        test("/ = 6", "6", "");
        test("/ = 7", "7", "");
        test("/ = 8", "8", "");
        test("/ = 9", "9", "");
        test("/ = 0", "0", "");

        test("1 / 0 =", "Cannot divide by zero", "1  ÷  ");
        test("1 / 0 = MC", "Cannot divide by zero", "1  ÷  ");
        test("1 / 0 = MR", "Cannot divide by zero", "1  ÷  ");
        test("1 / 0 = MS", "Cannot divide by zero", "1  ÷  ");
        test("1 / 0 = M+", "Cannot divide by zero", "1  ÷  ");
        test("1 / 0 = M-", "Cannot divide by zero", "1  ÷  ");
        test("1 / 0 = %", "Cannot divide by zero", "1  ÷  ");
        test("1 / 0 = /", "Cannot divide by zero", "1  ÷  ");
        test("1 / 0 = *", "Cannot divide by zero", "1  ÷  ");
        test("1 / 0 = -", "Cannot divide by zero", "1  ÷  ");
        test("1 / 0 = +", "Cannot divide by zero", "1  ÷  ");
        test("1 / 0 = ±", "Cannot divide by zero", "1  ÷  ");
        test("1 / 0 = ,", "Cannot divide by zero", "1  ÷  ");

        test("1 / 0 = CE", "0", "");
        test("1 / 0 = C", "0", "");
        test("1 / 0 = ⟵", "0", "");
        test("0,0000000000000000 ⟵ 1", "0,0000000000000001", "");
        test("1 / 0 = 1", "1", "");
        test("1 / 0 = 2", "2", "");
        test("1 / 0 = 3", "3", "");
        test("1 / 0 = 4", "4", "");
        test("1 / 0 = 5", "5", "");
        test("1 / 0 = 6", "6", "");
        test("1 / 0 = 7", "7", "");
        test("1 / 0 = 8", "8", "");
        test("1 / 0 = 9", "9", "");
        test("1 / 0 = 0", "0", "");

        test(", = + / , , - MC", "Result is undefined", "0  ÷  0  -  ");
        test(",,,,, = = ,,,,, = = + - * / ,,,,, -", "Result is undefined", "0  ÷  0  -  ");

        test("0 1/x", "Cannot divide by zero", "1/( 0 )");
        test("2 / 0 +", "Cannot divide by zero", "2  ÷  0  +  ");
        test("3 / 0 sqr +", "Cannot divide by zero", "3  ÷  sqr( 0 )  +  ");
        test("4 / 0 sqrt +", "Cannot divide by zero", "4  ÷  √( 0 )  +  ");

        test("1 ± sqrt", "Invalid input", "√( -1 )");
        test("1 ± sqrt 1 + 2 -", "3", "1  +  2  -  ");

        test("1000000000000000 sqr sqr sqr sqr sqr sqr sqr sqr sqr sqr", "Overflow", "sqr( sqr( sqr( sqr( sqr( sqr( sqr( sqr( sqr( sqr( 1000000000000000 ) ) ) ) ) ) ) ) ) )");
        test("0,0000000000000001 sqr sqr sqr sqr sqr sqr sqr sqr sqr sqr", "Overflow", "sqr( sqr( sqr( sqr( sqr( sqr( sqr( sqr( sqr( sqr( 0,0000000000000001 ) ) ) ) ) ) ) ) ) )");

        test("1 + 2 - 3 C", "0", "");
        test("1 + 2 - 3 CE 4 -", "-1", "1  +  2  -  4  -  ");
        test("1 M+ + 2 - MR + M- MR * 3 = MC", "3", "");
        test("1 + 20 % - 3 sqr * 4 sqrt / 5 1/x + 67 ⟵ - 7,8 ± * 8 CE 9 / ,1 +", "-5 778", "1  +  0,2  -  sqr( 3 )  ×  √( 4 )  ÷  1/( 5 )  +  6  -  -7,8  ×  9  ÷  0,1  +  ");

        test("1 + 20 % - 3 sqr * 4 sqrt / 5 1/x + 67 ⟵ - 7,8 ± * 8 CE 9 / ,1 +", "-5 778", "1  +  0,2  -  sqr( 3 )  ×  √( 4 )  ÷  1/( 5 )  +  6  -  -7,8  ×  9  ÷  0,1  +  ");
    }

    //////////////////////////////////////

    @Test
    void testAddOperation(){

        test("+", "0", "0  +  ");
        test("+ =", "0", "");
        test("+ 2 =", "2", "");
        test("2 + 2 =", "4", "");
        test("2 + 2 = +", "4", "4  +  ");
        test("0 + = = =", "0", "");
        test("1 + = = =", "4", "");
        test("2 + = = =", "8", "");
        test("13 + = = =", "52", "");
        test("9999 + = = =", "39 996", "");

        test("0,2 + 0,2 =", "0,4", "");
        test("0,2 ± + 0,2 =", "0", "");
        test("2 + 3 + = =", "15", "");
        test("1 + 2 + 3 = =", "9", "");
        test("1 + 2 + 3 = + 4 +", "10", "6  +  4  +  ");
        test("1 + 2 + 3 + 4 = =", "14", "");
        test("2 + 3 + 4 + 5 = =", "19", "");
        test("101 + 102 + 103 + 104 = =", "514", "");
        test("102 + 103 + 104 + 105 = =", "519", "");
        test("1 + 2 =", "3", "");
        test("9 + 5 =", "14", "");
        test("123 + 3 =", "126", "");
        test("123456789 + 987654321 =", "1 111 111 110", "");
        test("724387928792 + 724387928792 =", "1 448 775 857 584", "");
        test("724387928792 + 724387928792 + 724387928792 + 724387928792 =", "2 897 551 715 168", "");
        test("724387928792 + 724387928792 + 724387928792 + 724387928792 + 724387928792 + 724387928792 =", "4 346 327 572 752", "");

        test("123456789 ± + 01111 =", "-123 455 678", "");
        test("00003456 + 00002 ± =", "3 454", "");
        test("000 + 2 =", "2", "");
        test("2 + 0000 =", "2", "");
        test("2 ± + 0,00000 =", "-2", "");
        test("2 + 0 =", "2", "");
        test("2 ± + 0 =", "-2", "");
        test("0 + 2 ± =", "-2", "");
        test("0 + 0002 ± =", "-2", "");

        test("2 + 3 = 123456789 ± + 01111 +", "-123 455 678", "-123456789  +  1111  +  ");
        test("2 + 3 = = 00003456 + 00002 ± +", "3 454", "3456  +  -2  +  ");
        test("2 + 3 = = = 000 + 2 +", "2", "0  +  2  +  ");
        test("2 + 3 = 2 + 0000 +", "2", "2  +  0  +  ");
        test("2 + 3 = 2 ± + 0,00000 +", "-2", "-2  +  0  +  ");
        test("2 + 3 = 2 + 0 +", "2", "2  +  0  +  ");
        test("2 + 3 = + 2 ± + 0 +", "3", "5  +  -2  +  0  +  ");
        test("2 + 3 = + 0 + 2 ± +", "3", "5  +  0  +  -2  +  ");
        test("2 + 3 = + 0 + 0002 ± +", "3", "5  +  0  +  -2  +  ");

        test("5,6 + 2,7 =", "8,3", "");
        test("0,99 + 0,03 =", "1,02", "");
        test("0,999999999999 + 0,0000000003 =", "1,000000000299", "");
        test("0000,999999999999 + 0000,0000000003 =", "1,000000000299", "");
        test("0,999999999999 + 000,0000000003 =", "1,000000000299", "");
        test("123456789,987654321 + 987654321,123456789 =", "1 111 111 111,111111", "");
        test("1,111111111 + 2,999999999 =", "4,11111111", "");

        test("1 - * / + 2 =", "3", "");
        test("1 - * / + + - + 2 =", "3", "");
        test("1 - * / + + - + 2 =", "3", "");
        test("123456789 * / + + - + 2 =", "123 456 791", "");
        test("123456789 * / + = =", "370 370 367", "");

        test("999 ± + = = = =", "-4 995", "");
        test("191919 + 2 ± =", "191 917", "");
        test("0,99 + 0,03 ± =", "0,96", "");
        test("35 ± + 23 ± =", "-58", "");

        test("2 + + + = = = =", "10", "");
        test("2 + = = = =", "10", "");

        test("1 - * / + + - + 2 +", "3", "1  +  2  +  ");
        test("1234567 + - + 2 ± +", "1 234 565", "1234567  +  -2  +  ");
        test("1 - * / + + - + 2 ± +", "-1", "1  +  -2  +  ");
        test("2 ± + + + +", "-2", "-2  +  ");


        //Add with negate
        test("1 ± +", "-1", "-1  +  ");
        test("1 ± + 2 ±", "-2", "-1  +  ");
        test("1 ± + 2 ± +", "-3", "-1  +  -2  +  ");
        test("1 ± + 2 ± + 3 ±", "-3", "-1  +  -2  +  ");
        test("1 ± + 2 ± + 3 ± +", "-6", "-1  +  -2  +  -3  +  ");
        test("1 ± + 2 ± + 3 ± + 4 ±", "-4", "-1  +  -2  +  -3  +  ");
        test("1 ± + 2 ± + 3 ± + 4 ± =", "-10", "");
        test("1 ± + 2 ± + 3 ± + 4 ± = =", "-14", "");
        test("2 ± + 3 ± + 4 ± + 5 ± = =", "-19", "");
        test("101 ± + 102 ± + 103 ± + 104 ± = =", "-514", "");
        test("102 ± + 103 ± + 104 ± + 105 ± = =", "-519", "");
        test("1 ± + 2 + 3 ± + 4 = =", "6", "");
        test("2 + 3 ± + 4 + 5 ± = =", "-7", "");
        test("101 ± + 102 + 103 ± + 104 = =", "106", "");
        test("102 + 103 ± + 104 + 105 ± = =", "-107", "");
        test("1,111111111 ± + 2,999999999 =", "1,888888888", "");
        test("1,111111111 ± + 2,999999999 ± ± =", "1,888888888", "");
        test("1,111111111 ± ± + 2,999999999 ± ± =", "4,11111111", "");
        test("1,111111111 + 2,999999999 ± =", "-1,888888888", "");
        test("1,111111111 + 2,999999999 ± ± =", "4,11111111", "");

        test("12 + 34 + 45 = 102 + 103 ± + 104 + 105 ± +", "-2", "102  +  -103  +  104  +  -105  +  ");
        test("1,2 + 3,4 + 4,5 = + 1,02 + 10,3 ± + 104,5 + 1050,6 ± +", "-946,28", "9,1  +  1,02  +  -10,3  +  104,5  +  -1050,6  +  ");
        test("0,0000000000000001 + 1000000000000000 = - 1000000000000000 +", "0,0000000000000001", "1000000000000000  -  1000000000000000  +  ");
        test("0,1 M+ + 1000000000000000 = + MR + MR + MR + MR +", "1 000 000 000 000 001", "1000000000000000  +  0,1  +  0,1  +  0,1  +  0,1  +  ");
        test("30 + 15 + 30 % + 15 ± + sqrt + 4 sqr + 5 1/x = MS CE 654 ⟵ + MR +","131,2954529791365","65  +  66,29545297913646  +  ");
    }

    @Test
    void testSubtractOperation(){

        test("-", "0", "0  -  ");
        test("- =", "0", "");
        test("- 2 =", "-2", "");
        test("2 - 2 =", "0", "");
        test("2 - 3 =", "-1", "");
        test("2 - 2 = -", "0", "0  -  ");
        test("0 - = = =", "0", "");
        test("1 - = = =", "-2", "");
        test("2 - = = =", "-4", "");
        test("13 - = = =", "-26", "");
        test("7 - 3 =", "4", "");
        test("2 - 8 =", "-6", "");
        test("5 - 5 =", "0", "");
        test("99 - 5 =", "94", "");
        test("35 - 8 =", "27", "");
        test("234 - 123 =", "111", "");
        test("9999 - = = =", "-19 998", "");
        test("0,2 - 0,2 =", "0", "");
        test("0,2 ± - 0,2 =", "-0,4", "");
        test("2 - 3 - = =", "1", "");
        test("1 - 2 - 3 = =", "-7", "");
        test("1 - 2 - 3 = - 4 -", "-8", "-4  -  4  -  ");
        test("1 - 2 - 3 - 4 = =", "-12", "");
        test("2 - 3 - 4 - 5 = =", "-15", "");
        test("101 - 102 - 103 - 104 = =", "-312", "");
        test("102 - 103 - 104 - 105 = =", "-315", "");

        test("123456789 ± * / - 01111 =", "-123 457 900", "");
        test("00003456 + - 00002 ± =", "3 458", "");
        test("000 / - 2 =", "-2", "");
        test("2 - - 0000 =", "2", "");
        test("2 ± - + - 0,00000 =", "-2", "");
        test("2 - / - 0 =", "2", "");
        test("2 ± - * * - 0 =", "-2", "");
        test("0 - 2 ± =", "2", "");
        test("0 - 0002 ± =", "2", "");

        test("99 - 5 ± =", "104", "");
        test("5 - 3 sqr * 2 =", "-8", "");
        test("99 - 5 ± ± ± =", "104", "");
        test("99 ± ± - 5 ± ± ± =", "104", "");
        test("35 ± - 8 ± =", "-27", "");
        test("99 + - 5 ± =", "104", "");
        test("35 ± * - 8 ± =", "-27", "");
        test("99 - - 5 ± =", "104", "");
        test("35 ± * / + - 8 ± =", "-27", "");

        test("1 ± - 2 ± - 3 ± - 4 ± = =", "12", "");
        test("2 ± - 3 ± - 4 ± - 5 ± = =", "15", "");
        test("101 ± - 102 ± - 103 ± - 104 ± = =", "312", "");
        test("102 ± - 103 ± - 104 ± - 105 ± = =", "315", "");
        test("1 ± - 2 - 3 ± - 4 = =", "-8", "");
        test("2 - 3 ± - 4 - 5 ± = =", "11", "");
        test("101 ± - 102 - 103 ± - 104 = =", "-308", "");
        test("102 - 103 ± - 104 - 105 ± = =", "311", "");

        test("2,5 - 0,5 =", "2", "");
        test("2,5 / - 0,5 =", "2", "");
        test("123456789,9876543 - 987654321,1234567 =", "-864 197 531,1358024", "");
        test("987654321,1234567 - 123456789,9876543 =", "864 197 531,1358024", "");
        test("987654321,1234567 + / * * - 987654321,1234567 ± =", "1 975 308 642,246913", "");
        test("123456789,1234567 ± - / * + + - 123456789,9876543 =", "-246 913 579,111111", "");

        test("999 ± - = = = =", "2 997", "");
        test("191919 + - 2 =", "191 917", "");
        test("191919 + + + + + - - - 2 =", "191 917", "");
        test("191919 * * * * / / / - 2 =", "191 917", "");
        test("11111 * * * * / / / - = = = = = = = = = = = = = =", "-144 443", "");

        test("12 - 34 - 45 = 102 - 103 ± - 104 - 105 ± -", "206", "102  -  -103  -  104  -  -105  -  ");
        test("1,2 - 3,4 - 4,5 = - 1,02 - 10,3 ± - 104,5 - 1050,6 ± -", "948,68", "-6,7  -  1,02  -  -10,3  -  104,5  -  -1050,6  -  ");
        test("0,0000000000000001 - 1000000000000000 = + 1000000000000000 -", "0,0000000000000001", "-1000000000000000  +  1000000000000000  -  ");
        test("0,1 M+ - 1000000000000000 = - MR - MR - MR - MR - MR - MR -", "-1 000 000 000 000 001", "-999999999999999,9  -  0,1  -  0,1  -  0,1  -  0,1  -  0,1  -  0,1  -  ");
        test("30 - 15 - 30 % - 15 - ± sqrt - 4 sqr - 5 1/x = MS CE 654 ⟵ - MR -","87,82132034355964","65  -  -22,82132034355964  -  ");
    }

    @Test
    void testMultiplyOperation(){

        test("*", "0", "0  ×  ");
        test("* =", "0", "");
        test("* 2 =", "0", "");
        test("1 * 1 =", "1", "");
        test("2 * 2 =", "4", "");
        test("2 * 2 = *", "4", "4  ×  ");
        test("1 * * * 9 *", "9", "1  ×  9  ×  ");
        test("1 * * * 9 =", "9", "");
        test("1 * * * 9 = = = =", "6 561", "");
        test("1 + / * 2 =", "2", "");
        test("0 * = = =", "0", "");
        test("1 * = = =", "1", "");
        test("2 * = = =", "16", "");
        test("13 * = = =", "28 561", "");
        test("9999 * = = =", "9 996 000 599 960 001", "");
        test("98765432 * 9 =", "888 888 888", "");
        test("0,2 * 0,2 =", "0,04", "");
        test("0,2 ± * 0,2 =", "-0,04", "");
        test("0,2 ± + / - * 5 +", "-1", "-0,2  ×  5  +  ");
        test("0,3 ± ± ± + / - * 3 ± ± +", "-0,9", "-0,3  ×  3  +  ");
        test(", + / - * 5 ± ± =", "0", "");
        test(", 0 + / - * 6 =", "0", "");
        test("0,0 + / - * 7 =", "0", "");
        test("0 + * 8 =", "0", "");
        test(", + / - * 1 ± ± +", "0", "0  ×  1  +  ");
        test("0,0000 + - / * 2 =", "0", "");
        test("0 , + * 3 =", "0", "");
        test("012345 + / * 4 =", "49 380", "");
        test("012345 + / * 0005 +", "61 725", "12345  ×  5  +  ");
        test("2 * 3 * = =", "216", "");
        test("1 * 2 * 3 = =", "18", "");
        test("1 * 2 * 3 = * 4 *", "24", "6  ×  4  ×  ");
        test("1 * 2 * 3 * 4 = =", "96", "");
        test("2 * 3 * 4 * 5 = =", "600", "");
        test("101 * 102 * 103 * 104 = =", "11 476 922 496", "");
        test("102 * 103 * 104 * 105 = =", "12 046 179 600", "");
        test("987654321 * 987654321 =", "9,75461057789971e+17", "");
        test("9876543210987654 * 9876543210987654 =", "9,754610579850632e+31", "");
        test("9876543210987654 * 9876543210987654 * 9876543210987654 =", "9,63418328982521e+47", "");
        test("0,987654321 * 0,987654321 =", "0,975461057789971", "");

        test("1 ± * 9 =", "-9", "");
        test("1 ± * 2 ± * 3 ± * 4 ± = =", "-96", "");
        test("2 ± * 3 ± * 4 ± * 5 ± = =", "-600", "");
        test("101 ± * 102 ± * 103 ± * 104 ± = =", "-11 476 922 496", "");
        test("102 ± * 103 ± * 104 ± * 105 ± = =", "-12 046 179 600", "");
        test("1 ± * 2 * 3 ± * 4 = =", "96", "");
        test("2 * 3 ± * 4 * 5 ± = =", "-600", "");
        test("101 ± * 102 * 103 ± * 104 = =", "11 476 922 496", "");
        test("102 * 103 ± * 104 * 105 ± = =", "-12 046 179 600", "");
        test("98765432 ± * 9 ± =", "888 888 888", "");
        test("98765432 ± * 9 =", "-888 888 888", "");

        test("12 * 34 = = * 56 * ± =","-603 467 956 224","");
        test("12 * 34 = = 56 ± * % * sqr * 1/x *","1","-56  ×  31,36  ×  sqr( -1756,16 )  ×  1/( -5416169448,144896 )  ×  ");
        test("3,333333333333333 sqrt * =","3,333333333333333","");
        test("0 sqrt * = * 1234 =","0","");
        test("0 sqrt * = * 1234 = 1234 * =","1 522 756","");

    }

    @Test
    void testDivideOperation(){

        test("/", "0", "0  ÷  ");
        test("/ 2 =", "0", "");
        test("2 / / / / ", "2", "2  ÷  ");
        test("0 / 2 =", "0", "");
        test("0 / 987654321 ± =", "0", "");
        test("0 ± / 987654321 ± =", "0", "");
        test("2 / 2 =", "1", "");
        test("10 / =", "1", "");
        test("10 / = = =", "0,01", "");
        test("10 / 10 =", "1", "");
        test("2 / 2 = /", "1", "1  ÷  ");
        test("1 / = = =", "1", "");
        test("2 / = = =", "0,25", "");
        test("2 / / / / = = =", "0,25", "");
        test("13 / = = =", "0,0059171597633136", "");
        test("9999 / = = =", "1,000200030004001e-8", "");
        test("0,2 / 0,2 =", "1", "");
        test("0,2 ± / 0,2 =", "-1", "");
        test("2 / 3 =", "0,6666666666666667", "");
        test("2 / 3 / = =", "1,5", "");
        test("3 / 9 =", "0,3333333333333333", "");
        test("9 / 5 =", "1,8", "");
        test("10 / 3 =", "3,333333333333333", "");
        test("10 / 3 = =", "1,111111111111111", "");
        test("10 / 3 = = =", "0,3703703703703704", "");
        test("19191919191 / 354834693643 =", "0,0540869298713756", "");
        test("1 / 2 / 3 =", "0,1666666666666667", "");
        test("1 / 2 / 3 = =", "0,0555555555555556", "");
        test("1 / 2 / 3 = / 4 /", "0,0416666666666667", "0,1666666666666667  ÷  4  ÷  ");
        test("1 / 2 / 3 / 4 = =", "0,0104166666666667", "");
        test("2 / 3 / 4 / 5 = =", "0,0066666666666667", "");
        test("101 / 102 / 103 / 104 = =", "8,888271227374158e-7", "");
        test("102 / 103 / 104 / 105 = =", "8,636763144391438e-7", "");

        test("91919191919 / = ", "1", "");
        test("91919191919 / = = =", "1,183552711030181e-22", "");

        test("00000 ± / 0000743278423 ± =", "0", "");
        test("0000743278423 ± / 0000743278423 =", "-1", "");
        test("438974723 / 438974723 ± =", "-1", "");
        test("438974723 ± / 438974723 =", "-1", "");
        test("7865947546 ± / 7865947546 ± =", "1", "");
        test("828742387 ± / 7865947546 ± =", "0,1053582396975725", "");

        test("1000000 / = = = = = = = = = = = = = = = = =", "1,e-96", "");
        test("1000000 / = = = = = = = = = = = = = = = = = = =", "1,e-108", "");
        test("1 / 99999999 = = = = = = = = = = =", "1,000000110000007e-88", "");
        test("1 / 99999999 = = = = = = = = = = = =", "1,000000120000008e-96", "");
        test("1 / 99999999 = = = = = = = = = = = = =", "1,000000130000009e-104", "");
        test("2 / 8888888888 = = = = = = = = = =", "6,49464205743146e-100", "");
        test("2 / 8888888888 = = = = = = = = = = = = = = = = = = = = = ", "2,372646119533627e-209", "");
        test("2 / 8888888888 = = = = = = = = = = = = = = = = = = = = = +", "2,372646119533627e-209", "2,372646119533627e-209  +  ");
        test("2 / 8888888888 = = = = = = = = = = = = = = = = = = = = = /", "2,372646119533627e-209", "2,372646119533627e-209  ÷  ");
        test("2 / 8888888888 = = = = = = = = = = = = = = = = = = = = = + / *", "2,372646119533627e-209", "2,372646119533627e-209  ×  ");
        test("2 / 8888888888 = = = = = = = = = = = = = = = = = = = = = + / * 2 / 2 =", "2,372646119533627e-209", "");
        test("2 / 8888888888 = = = = = = = = =", "5,773015161583996e-90", "");

        test("1 ± / 2 ± / 3 ± / 4 ± = =", "-0,0104166666666667", "");
        test("2 ± / 3 ± / 4 ± / 5 ± = =", "-0,0066666666666667", "");
        test("101 ± / 102 ± / 103 ± / 104 ± = =", "-8,888271227374158e-7", "");
        test("102 ± / 103 ± / 104 ± / 105 ± = =", "-8,636763144391438e-7", "");
        test("1 ± / 2 / 3 ± / 4 = =", "0,0104166666666667", "");
        test("2 / 3 ± / 4 / 5 ± = =", "-0,0066666666666667", "");
        test("101 ± / 102 / 103 ± / 104 = =", "8,888271227374158e-7", "");
        test("102 / 103 ± / 104 / 105 ± = =", "-8,636763144391438e-7", "");

        test("/ =","Result is undefined","0  ÷  ");
        test("1 / 0 =","Cannot divide by zero","1  ÷  ");
        test("/ , , -","Result is undefined","0  ÷  0  -  ");
        test("+ / 0 -","Result is undefined","0  ÷  0  -  ");
        test(", = + / , , -","Result is undefined","0  ÷  0  -  ");
        test(", , , , , = = , , , , , = = + - * / , , , , , -","Result is undefined","0  ÷  0  -  ");

        test("2 / 0 +","Cannot divide by zero","2  ÷  0  +  ");
        test("1,1 / 0 +","Cannot divide by zero","1,1  ÷  0  +  ");
        test("0,1 / 0 +","Cannot divide by zero","0,1  ÷  0  +  ");
        test("3 / 0 sqr +","Cannot divide by zero","3  ÷  sqr( 0 )  +  ");
        test("4 / 0 sqrt +","Cannot divide by zero","4  ÷  √( 0 )  +  ");


        test("12 / 34 = = / 56 * ± /","-3,436139365170045e-8","0,0103806228373702  ÷  56  ×  negate( -0,000185368264953 )  ÷  ");
        test("12 / 34 = = / 56 / ± =","-1","");
        test("12 / 34 = = 56 ± / % / sqr / 1/x /","0,3136","-56  ÷  31,36  ÷  sqr( -1,785714285714286 )  ÷  1/( -0,56 )  ÷  ");
        test("56 ± / % / sqr / 1/x / = = / 98 sqrt / sqr / % /","32,21149695638427","3,188775510204082  ÷  √( 98 )  ÷  sqr( 0,3221149695638427 )  ÷  0,0963780608  ÷  ");
        test("3,333333333333333 M- sqr / MR =","-3,333333333333333","");
    }

    @Test
    void testOperationCombinations(){

        test("± / * - +", "0", "negate( 0 )  +  ");
        test("± / * - + %", "0", "negate( 0 )  +  0");
        test("/ * * / - + / *", "0", "0  ×  ");
        test("/ * * / - + / * % % ", "0", "0  ×  0");
        test("/ * * / - + / * % % =", "0", "");
        test("/ / + + - - / / * * - - + + / / * * = ± = = =", "0", "");
        test(", = = = = / - + * = ±", "0", "negate( 0 )");
        test(", = = * / - + = ±", "0", "negate( 0 )");
        test(", 0 , , = = - + / * = ±", "0", "negate( 0 )");
        test("0 , = = - + / * =", "0", "");
        test("0 , 0000000 = = / * * / - + / * =", "0", "");
        test("- = - +", "0", "0  +  ");

        test("4 / + =", "8", "");
        test("/ + 4 =", "4", "");
        test("4 + / =", "1", "");
        test("4 + / 2 =", "2", "");
        test("25 + 25 + 25 * 25 * 25 = =", "1 171 875", "");
        test("9876543210 + 123456789 / 123456789 * 123456789 - 123456789 ± =", "10 123 456 788", "");
        test("5 / 3 * 3 =", "5", "");
        test("2 + 5 / 3 * 3 =", "7", "");
        test("10 + 20 * 30 + 40 / 3 * 3 -", "940", "10  +  20  ×  30  +  40  ÷  3  ×  3  -  ");
        test("2 * 5 / 3 * 3 =", "10", "");
        test("10 / 3 *", "3,333333333333333", "10  ÷  3  ×  ");
        test("10 / 3 * sqr", "11,11111111111111", "10  ÷  3  ×  sqr( 3,333333333333333 )");
        test("9 / 3 * 3 =", "9", "");
        test("10 / 3 + 1 =", "4,333333333333333", "");

        test("2 ± + 2 * 2 -", "0", "-2  +  2  ×  2  -  ");
        test("2 + 2 + * 2 ± =", "-8", "");
        test("2 + 2 * 2 ± =", "-8", "");
        test("2 + 2 * 2 ± = =", "16", "");
        test("2 + 2 * 2 ± = = =", "-32", "");
        test("2 + 3 * 7 ± =", "-35", "");
        test("2 + 3 * 7 ± = =", "245", "");
        test("2 + 3 * 7 ± = = =", "-1 715", "");
        test("2 + 2 ± + * 2 = = = =", "0", "");
        test("2 + 2 * =", "16", "");
        test("2 + 2 * = = =", "256", "");
        test("23 + 12 * / 2 + = = =", "70", "");

        test("3 + 3 * 3 =", "18", "");
        test("4 + 4 - * 4 =", "32", "");
        test("5 + 5 - * 5 = = = =", "6 250", "");
        test("6 + 6 + * 6 + = = = =", "360", "");
        test("1 + 2 - 3 / 7 * 9 =", "0", "");
        test("1 ± + 2 - 3 / 7 * 9 =", "-2,571428571428571", "");
        test("7 ± + 8,8 * + 9,9 =", "11,7", "");
        test("1 ± + 2,2 * + 3,3 =", "4,5", "");
        test("1 / 2 * 13 ± + 1,1 * + 0,5 -", "-4,9", "1  ÷  2  ×  -13  +  1,1  +  0,5  -  ");

        test("/ 2 =", "0", "");
        test("2 = / =", "1", "");
        test("2 = / = =", "0,5", "");
        test("2 = / = = =", "0,25", "");
        test("2 = + - / / = = =", "0,25", "");
        test("2 + = + - / / = = =", "0,0625", "");
        test("1 + = = - +", "3", "3  +  ");
        test("1 / + = = 2 +", "2", "2  +  ");
        test("1 / + = = 2 + = ", "4", "");
        test("1 / + = = - + 2 = ", "5", "");
        test("2 + 2 = + 2 =", "6", "");
        test("1 / + = = - + 2 = = ", "7", "");
        test("2 + - / * + 2 = + + - - + 2 =", "6", "");
        test("19 = * =", "361", "");
        test("191919 + = - 2 =", "383 836", "");
        test("191919 + + + + + = - - - 2 =", "383 836", "");
        test("0 + 0,3 = = = = * 3 = = =", "32,4", "");
        test("000000 + 00000,3 = = = = * 00003 = = =", "32,4", "");
        test("0 , , , + 0 , , , , 7 = = * 000003 , , , , = = =", "37,8", "");
        test("/ 2 = = 2 + 3 = = =", "11", "");
        test("/ 2 = = 2 + 3 =", "5", "");
        test("/ 2 = = 2 / 3 =", "0,6666666666666667", "");
        test("/ 2 = = 2 / 3 = = =", "0,0740740740740741", "");
        test("/ 2 = = 2 / 3 = + 2 = =", "4,666666666666667", "");
    }

    @Test
    void testSqr(){

        test("sqr + 5 sqr", "25", "sqr( 0 )  +  sqr( 5 )");
        test("0 sqr sqr sqr = =", "0", "");
        test("0 sqr sqr sqr", "0", "sqr( sqr( sqr( 0 ) ) )");
        test("1 sqr sqr sqr = =", "1", "");
        test("1 sqr sqr sqr", "1", "sqr( sqr( sqr( 1 ) ) )");
        test("16 sqr sqr sqr = =", "4 294 967 296", "");
        test("16 sqr sqr sqr", "4 294 967 296", "sqr( sqr( sqr( 16 ) ) )");
        test("5 sqrt sqr", "5", "sqr( √( 5 ) )");
        test("16 sqr + sqr = =", "131 328", "");
        test("1 + 2 sqr * 3 = =", "45", "");

        test("1000000000000000 sqr","1,e+30","sqr( 1000000000000000 )");
        test("1000000000000000 sqr sqr","1,e+60","sqr( sqr( 1000000000000000 ) )");
        test("1000000000000000 sqr sqr sqr","1,e+120","sqr( sqr( sqr( 1000000000000000 ) ) )");
        test("1000000000000000 sqr sqr sqr sqr","1,e+240","sqr( sqr( sqr( sqr( 1000000000000000 ) ) ) )");
        test("1000000000000000 sqr sqr sqr sqr sqr","1,e+480","sqr( sqr( sqr( sqr( sqr( 1000000000000000 ) ) ) ) )");
        test("1000000000000000 sqr sqr sqr sqr sqr sqr","1,e+960","sqr( sqr( sqr( sqr( sqr( sqr( 1000000000000000 ) ) ) ) ) )");
        test("1000000000000000 sqr sqr sqr sqr sqr sqr sqr","1,e+1920","sqr( sqr( sqr( sqr( sqr( sqr( sqr( 1000000000000000 ) ) ) ) ) ) )");
        test("1000000000000000 sqr sqr sqr sqr sqr sqr sqr sqr","1,e+3840","sqr( sqr( sqr( sqr( sqr( sqr( sqr( sqr( 1000000000000000 ) ) ) ) ) ) ) )");
        test("1000000000000000 sqr sqr sqr sqr sqr sqr sqr sqr sqr","1,e+7680","sqr( sqr( sqr( sqr( sqr( sqr( sqr( sqr( sqr( 1000000000000000 ) ) ) ) ) ) ) ) )");
        test("1000000000000000 sqr sqr sqr sqr sqr sqr sqr sqr sqr sqr","Overflow","sqr( sqr( sqr( sqr( sqr( sqr( sqr( sqr( sqr( sqr( 1000000000000000 ) ) ) ) ) ) ) ) ) )");

        test("0,0000000000000001 sqr","1,e-32","sqr( 0,0000000000000001 )");
        test("0,0000000000000001 sqr sqr","1,e-64","sqr( sqr( 0,0000000000000001 ) )");
        test("0,0000000000000001 sqr sqr sqr","1,e-128","sqr( sqr( sqr( 0,0000000000000001 ) ) )");
        test("0,0000000000000001 sqr sqr sqr sqr","1,e-256","sqr( sqr( sqr( sqr( 0,0000000000000001 ) ) ) )");
        test("0,0000000000000001 sqr sqr sqr sqr sqr","1,e-512","sqr( sqr( sqr( sqr( sqr( 0,0000000000000001 ) ) ) ) )");
        test("0,0000000000000001 sqr sqr sqr sqr sqr sqr","1,e-1024","sqr( sqr( sqr( sqr( sqr( sqr( 0,0000000000000001 ) ) ) ) ) )");
        test("0,0000000000000001 sqr sqr sqr sqr sqr sqr sqr","1,e-2048","sqr( sqr( sqr( sqr( sqr( sqr( sqr( 0,0000000000000001 ) ) ) ) ) ) )");
        test("0,0000000000000001 sqr sqr sqr sqr sqr sqr sqr sqr","1,e-4096","sqr( sqr( sqr( sqr( sqr( sqr( sqr( sqr( 0,0000000000000001 ) ) ) ) ) ) ) )");
        test("0,0000000000000001 sqr sqr sqr sqr sqr sqr sqr sqr sqr","1,e-8192","sqr( sqr( sqr( sqr( sqr( sqr( sqr( sqr( sqr( 0,0000000000000001 ) ) ) ) ) ) ) ) )");
        test("0,0000000000000001 sqr sqr sqr sqr sqr sqr sqr sqr sqr sqr","Overflow","sqr( sqr( sqr( sqr( sqr( sqr( sqr( sqr( sqr( sqr( 0,0000000000000001 ) ) ) ) ) ) ) ) ) )");


        test("3 sqr sqrt - 3 =","0","");
        test("3 sqr sqrt - 10 % = sqr sqr 1/x + sqr * sqr - sqr / sqr % sqr =","5,759700640885393e+29","");
        test("25 * 25 = = = = = sqr sqr sqr =","3,155443620884047e+68","");
        test("25 * 25 = = = = = sqr sqr sqr = sqr sqr = sqr =","1,535689537429126e+552","");
        test("25 * 25 * sqr sqr sqr * sqr sqr = sqr","4,257959840008151e+251","sqr( 6,525304467998525e+125 )");
        test("25 * 25 = = = = = sqr 1/x sqr 1/x = sqr 1/x = sqr 1/x =","2,489206111144457e+138","");
    }

    @Test
    void testSqrt(){

        test("4 sqrt + = =", "6", "");
        test("25 sqrt + = =", "15", "");

        test("0 sqrt sqrt sqrt = =", "0", "");
        test("1 sqrt sqrt sqrt = =", "1", "");
        test("16 sqrt sqrt sqrt = =", "1,414213562373095", "");
        test("256 sqrt sqrt sqrt = =", "2", "");
        test("25 sqrt 16 sqrt + ", "4", "√( 16 )  +  ");
        test("25 sqrt + - * / ", "5", "√( 25 )  ÷  ");
        test("25 sqrt + 16 sqrt = =", "13", "");
        test("25 sqrt + 16 sqrt = sqrt", "3", "√( 9 )");
        test("25 sqrt + 16 sqrt = sqrt sqr", "9", "sqr( √( 9 ) )");
        test("25 sqrt + 16 sqrt + = =", "27", "");
        test("25 sqrt - 16 sqrt = =", "-3", "");
        test("25 sqrt - 16 sqrt - = =", "-1", "");
        test("16 sqrt + sqrt = =", "8", "");
        test("1 + 4 sqrt * 3 = =", "27", "");

        test("5 sqrt sqr - 4 sqrt -", "3", "sqr( √( 5 ) )  -  √( 4 )  -  ");
        test("5 sqrt sqr sqrt sqr - 4 sqrt -", "3", "sqr( √( sqr( √( 5 ) ) ) )  -  √( 4 )  -  ");

        test("5 sqrt sqr - 3 sqrt sqr -", "2", "sqr( √( 5 ) )  -  sqr( √( 3 ) )  -  ");
        test("5 sqrt sqr - 3 sqrt sqr = =", "-1", "");

        test("5 sqrt sqr sqrt sqr - 3 sqrt sqr sqrt sqr -", "2", "sqr( √( sqr( √( 5 ) ) ) )  -  sqr( √( sqr( √( 3 ) ) ) )  -  ");
        test("5 sqrt sqr sqrt sqr - 3 sqrt sqr sqrt sqr = =", "-1", "");
        test("5 sqrt sqr sqrt sqr + 3 sqrt sqr sqrt sqr +", "8", "sqr( √( sqr( √( 5 ) ) ) )  +  sqr( √( sqr( √( 3 ) ) ) )  +  ");
        test("5 sqrt sqr sqrt sqr + 3 sqrt sqr sqrt sqr = =", "11", "");

        test("0,0000000000000001 sqr sqr sqr sqr sqr sqr sqr sqr sqr", "1,e-8192", "sqr( sqr( sqr( sqr( sqr( sqr( sqr( sqr( sqr( 0,0000000000000001 ) ) ) ) ) ) ) ) )");

        test("1 ± sqrt","Invalid input","√( -1 )");
        test("12 ± sqrt","Invalid input","√( -12 )");
        test("123 ± sqrt","Invalid input","√( -123 )");
        test("12,3 ± sqrt","Invalid input","√( -12,3 )");

        test("0,1 sqrt sqrt sqrt sqrt sqrt sqrt sqrt sqrt sqrt sqrt sqrt sqrt sqrt sqrt sqrt sqrt sqrt sqrt sqrt sqrt sqrt sqrt sqrt sqrt sqrt sqrt sqrt sqrt sqrt sqrt sqrt sqrt sqrt sqrt sqrt sqrt sqrt sqrt sqrt sqrt sqrt sqrt sqrt sqrt sqrt sqrt sqrt sqrt sqrt sqrt sqrt sqrt sqrt sqrt","0,9999999999999999","√( √( √( √( √( √( √( √( √( √( √( √( √( √( √( √( √( √( √( √( √( √( √( √( √( √( √( √( √( √( √( √( √( √( √( √( √( √( √( √( √( √( √( √( √( √( √( √( √( √( √( √( √( √( 0,1 ) ) ) ) ) ) ) ) ) ) ) ) ) ) ) ) ) ) ) ) ) ) ) ) ) ) ) ) ) ) ) ) ) ) ) ) ) ) ) ) ) ) ) ) ) ) ) ) ) ) ) ) ) )");
        test("0,1 sqrt sqrt sqrt sqrt sqrt sqrt sqrt sqrt sqrt sqrt sqrt sqrt sqrt sqrt sqrt sqrt sqrt sqrt sqrt sqrt sqrt sqrt sqrt sqrt sqrt sqrt sqrt sqrt sqrt sqrt sqrt sqrt sqrt sqrt sqrt sqrt sqrt sqrt sqrt sqrt sqrt sqrt sqrt sqrt sqrt sqrt sqrt sqrt sqrt sqrt sqrt sqrt sqrt sqrt sqrt sqrt","1","√( √( √( √( √( √( √( √( √( √( √( √( √( √( √( √( √( √( √( √( √( √( √( √( √( √( √( √( √( √( √( √( √( √( √( √( √( √( √( √( √( √( √( √( √( √( √( √( √( √( √( √( √( √( √( √( 0,1 ) ) ) ) ) ) ) ) ) ) ) ) ) ) ) ) ) ) ) ) ) ) ) ) ) ) ) ) ) ) ) ) ) ) ) ) ) ) ) ) ) ) ) ) ) ) ) ) ) ) ) ) ) ) ) )");


        test("128 sqrt sqrt - 10 % = sqrt sqrt 1/x +","0,7581213888342921","1/( √( √( 3,027227094913372 ) ) )  +  ");
        test("128 sqrt sqrt - 10 % = sqrt sqrt 1/x + sqrt * sqrt - sqrt / sqrt % - sqrt ±","-11,19352517702115","1/( √( √( 3,027227094913372 ) ) )  +  √( 0,7581213888342921 )  ×  √( 1,628823053490041 )  -  √( 2,078791159415937 )  ÷  0,0050839201106993  -  negate( √( 125,2950058886063 ) )");
        test("128 sqrt sqrt - 10 % = sqrt sqrt 1/x + sqrt * sqrt - sqrt / sqrt % - sqrt ± +","136,4885310656274","1/( √( √( 3,027227094913372 ) ) )  +  √( 0,7581213888342921 )  ×  √( 1,628823053490041 )  -  √( 2,078791159415937 )  ÷  0,0050839201106993  -  negate( √( 125,2950058886063 ) )  +  ");
        test("0,9999999999999999 sqrt - 1 + 9 sqrt * sqrt -","5,196152422706632","√( 0,9999999999999999 )  -  1  +  √( 9 )  ×  √( 3 )  -  ");
        test("9999999999999999 sqrt - sqrt =","99 990 000","");

    }

    @Test
    void testFraction(){

        test("1 1/x", "1", "1/( 1 )");
        test("2 1/x", "0,5", "1/( 2 )");
        test("3 1/x", "0,3333333333333333", "1/( 3 )");
        test("4 1/x", "0,25", "1/( 4 )");
        test("5 1/x", "0,2", "1/( 5 )");
        test("6 1/x", "0,1666666666666667", "1/( 6 )");
        test("7 1/x", "0,1428571428571429", "1/( 7 )");
        test("8 1/x", "0,125", "1/( 8 )");
        test("9 1/x", "0,1111111111111111", "1/( 9 )");
        test("10 1/x", "0,1", "1/( 10 )");

        test("1 ± 1/x", "-1", "1/( -1 )");
        test("2 ± 1/x", "-0,5", "1/( -2 )");
        test("3 ± 1/x", "-0,3333333333333333", "1/( -3 )");
        test("4 ± 1/x", "-0,25", "1/( -4 )");
        test("5 ± 1/x", "-0,2", "1/( -5 )");
        test("6 ± 1/x", "-0,1666666666666667", "1/( -6 )");
        test("7 ± 1/x", "-0,1428571428571429", "1/( -7 )");
        test("8 ± 1/x", "-0,125", "1/( -8 )");
        test("9 ± 1/x", "-0,1111111111111111", "1/( -9 )");
        test("10 ± 1/x", "-0,1", "1/( -10 )");

        test("1 1/x * 3 +", "3", "1/( 1 )  ×  3  +  ");
        test("2 1/x * 2 +", "1", "1/( 2 )  ×  2  +  ");
        test("3 1/x * 3 +", "1", "1/( 3 )  ×  3  +  ");
        test("4 1/x * 4 +", "1", "1/( 4 )  ×  4  +  ");
        test("5 1/x * 5 +", "1", "1/( 5 )  ×  5  +  ");
        test("6 1/x * 6 +", "1", "1/( 6 )  ×  6  +  ");
        test("7 1/x * 7 +", "1", "1/( 7 )  ×  7  +  ");
        test("8 1/x * 8 +", "1", "1/( 8 )  ×  8  +  ");
        test("9 1/x * 9 +", "1", "1/( 9 )  ×  9  +  ");

        test("1 + 4 1/x * 3 = =", "11,25", "");
        test("9999999999999999 1/x 1/x", "9 999 999 999 999 999", "1/( 1/( 9999999999999999 ) )");
        test("9999999999999999 1/x 1/x 1/x", "0,0000000000000001", "1/( 1/( 1/( 9999999999999999 ) ) )");
        test("10 + 1/x = 1/x", "0,099009900990099", "1/( 10,1 )");
        test("10 + 1/x = 1/x / 1/x +", "0,0098029604940692", "1/( 10,1 )  ÷  1/( 0,099009900990099 )  +  ");
        test("9999999999999999 1/x 1/x = 1/x", "0,0000000000000001", "1/( 9999999999999999 )");
        test("9999999999999999 1/x 1/x = 1/x / 1/x +", "1,e-32", "1/( 9999999999999999 )  ÷  1/( 0,0000000000000001 )  +  ");
        test("9999999999999999 1/x 1/x = ± 1/x / 1/x +", "1,e-32", "1/( negate( 9999999999999999 ) )  ÷  1/( -0,0000000000000001 )  +  ");

        test("1 / 3 = 1/x","3","1/( 0,3333333333333333 )");
        test("1 / 6 = 1/x","6","1/( 0,1666666666666667 )");
        test("1 / 3 = 1/x - 3 -","0","1/( 0,3333333333333333 )  -  3  -  ");
        test("1 / 6 = 1/x - 6 -","0","1/( 0,1666666666666667 )  -  6  -  ");

        test("1000000000000000 sqr 1/x","1,e-30","1/( sqr( 1000000000000000 ) )");
        test("1000000000000000 sqr ± 1/x","-1,e-30","1/( negate( sqr( 1000000000000000 ) ) )");
        test( "1234567890123456 1/x sqrt","2,846049906958767e-8","√( 1/( 1234567890123456 ) )");
        test( "1234567890123456 ± 1/x","-8,100000072900006e-16","1/( -1234567890123456 )");

        test("0,1 1/x","10","1/( 0,1 )");
        test("0,1 ± 1/x","-10","1/( -0,1 )");
        test("0,0000000000000001 1/x","1,e+16","1/( 0,0000000000000001 )");
        test("0,0000000000000001 ± 1/x","-1,e+16","1/( -0,0000000000000001 )");
        test("0,0000000000000001 sqr 1/x","1,e+32","1/( sqr( 0,0000000000000001 ) )");
        test("0,0000000000000001 sqr ± 1/x","-1,e+32","1/( negate( sqr( 0,0000000000000001 ) ) )");

        test( "9876543210 sqr ± sqr sqr 1/x","1,104486101070964e-80","1/( sqr( sqr( negate( sqr( 9876543210 ) ) ) ) )");
        test( "9876543210 sqrt 1/x sqrt 1/x sqrt sqr  ","315,2472030020438","sqr( √( 1/( √( 1/( √( 9876543210 ) ) ) ) ) )");
        test( "9876543210 sqrt 1/x sqrt ± ±","0,0031721137903118","negate( negate( √( 1/( √( 9876543210 ) ) ) ) )");
        test( "9876543210 1/x ± sqr" ,"1,025156249974371e-20","sqr( negate( 1/( 9876543210 ) ) )");
        test( "9876543210 ± sqr sqrt sqrt sqrt sqr","99 380,79900061178","sqr( √( √( √( sqr( -9876543210 ) ) ) ) )");
        test( "9876543210 1/x - 0,0000000001012499 =","9,999873437500002e-17","");
        test( "9876543210 1/x sqr sqrt sqr sqr 1/x ±","-9,515242752647292e+39","negate( 1/( sqr( sqr( √( sqr( 1/( 9876543210 ) ) ) ) ) ) )");
        test( "9876543210 sqrt sqrt 1/x ± sqr sqrt ±","-0,0031721137903118","negate( √( sqr( negate( 1/( √( √( 9876543210 ) ) ) ) ) ) )");
        test( "9876543210 ± sqr sqr sqrt ± ± 1/x","1,025156249974371e-20","1/( negate( negate( √( sqr( sqr( -9876543210 ) ) ) ) ) )");
        test( "9876543210 sqrt sqr ± 1/x ± ±","-1,012499999987344e-10","negate( negate( 1/( negate( sqr( √( 9876543210 ) ) ) ) ) )");

        test("123 + 456 = + 789 - 1/x -","1 367,999269005848","579  +  789  -  1/( 1368 )  -  ");
        test("123 * 456 = / 789 * 1/x /","1","56088  ÷  789  ×  1/( 71,08745247148289 )  ÷  ");
        test("123 * 456 = / 789 * 123 1/x /","0,5779467680608365","56088  ÷  789  ×  1/( 123 )  ÷  ");

        test("3 1/x * 9 = 1/x * 9 = 1/x * 9 *","3","1/( 3 )  ×  9  ×  ");

        test("3 1/x + % 1/x","900","1/( 3 )  +  1/( 0,0011111111111111 )");
        test("3 1/x + % 1/x =","900,3333333333333","");

        test("0 1/x","Cannot divide by zero","1/( 0 )");
        test("3 - 3 + 1/x","Cannot divide by zero","3  -  3  +  1/( 0 )");
    }

    @Test
    void testNegate(){

        test("0 ± -", "0", "0  -  ");
        test("± -", "0", "negate( 0 )  -  ");
        test("5 ± ±", "5", "");
        test("5 ± ± ± -", "-5", "-5  -  ");
        test("5 ± ± ± - =", "0", "");
        test("5 ± ± ± - 6 - ±", "11", "-5  -  6  -  negate( -11 )");
        test("5 ± ± ± - 6 - ± ±", "-11", "-5  -  6  -  negate( negate( -11 ) )");
        test("5 ± ± ± - 6 - ± ± ±", "11", "-5  -  6  -  negate( negate( negate( -11 ) ) )");

        test("9 ± ± sqrt -", "3", "√( 9 )  -  ");

        test("20 ±", "-20", "");
        test("20 + ±", "-20", "20  +  negate( 20 )");
        test("20 + 20 = ±", "-40", "negate( 40 )");
        test("20 + 20 = ± + 20 -", "-20", "negate( 40 )  +  20  -  ");

        test("1 ± + = =", "-3", "");
        test("1 ± - = =", "1", "");
        test("1 ± * = =", "-1", "");
        test("1 ± / = =", "-1", "");
        test("2 ± ± ± ± + = = = =", "10", "");
        test("2 ± + = = = =", "-10", "");

        test("1 ± ± ± = =", "-1", "");
        test("16 ± ± ± = =", "-16", "");
        test("256 ± ± ± = =", "-256", "");
        test("1 + 20 % - 3 sqr * 4 sqrt / 5 1/x + 67 ⟵ - 7,8 ±", "-7,8", "1  +  0,2  -  sqr( 3 )  ×  √( 4 )  ÷  1/( 5 )  +  6  -  ");


        test(", ±","-0,","");
        test("20 + 40 % ± =","12","");
        test("20 + 40 % ± = ±","-12","negate( 12 )");
        test("20 + 40 % ± = ± sqr ± ± sqrt","12","√( negate( negate( sqr( negate( 12 ) ) ) ) )");
        test("5 - ± = ± / 4 * 6 ± 1/x -","0,4166666666666667","negate( 10 )  ÷  4  ×  1/( -6 )  -  ");
        test("5 sqr ± * ± sqrt - + / 5 ± = ± / 4 * 6 ± 1/x =","-1,041666666666667","");
    }

    @Test
    void testPercent(){

        test("%", "0", "0");
        test("0 % % %", "0", "0");
        test("0 % =", "0", "");
        test("1 % % %", "0", "0");
        test("10 % % % % % % % % % % % % % % % % % % % % % % % % % % % % % % % % % %", "0", "0");
        test("1 %", "0", "0");
        test("20 %", "0", "0");
        test("9999999999999999 % =", "0", "");
        test("9999999999999999 % = %", "0", "0");
        test("9999999999999999 % = % %", "0", "0");

        test("20 + 0 %", "0", "20  +  0");
        test("20 + 20 % = %", "5,76", "5,76");

        test("20 + 10 % = =", "24", "");
        test("20 + 10 %", "2", "20  +  2");
        test("20 - 10 % = =", "16", "");
        test("20 - 10 %", "2", "20  -  2");
        test("20 * 10 % = =", "80", "");
        test("20 * 10 %", "2", "20  ×  2");
        test("20 / 10 % = =", "5", "");
        test("20 / 10 %", "2", "20  ÷  2");

        test("20 + 20 % = %", "5,76", "5,76");
        test("20 + 20 % = % =", "9,76", "");
        test("200 + 0 = %", "400", "400");
        test("200 + 10 = %", "441", "441");
        test("200 + 0 = % %", "800", "800");
        test("200 + 0 = % % =", "800", "");
        test("200 + 10 = % %", "926,1", "926,1");
        test("200 + 10 = % % =", "936,1", "");

        test("20 - 20 % = %", "2,56", "2,56");
        test("200 - 0 = %", "400", "400");
        test("200 - 10 = %", "361", "361");
        test("200 - 0 = % %", "800", "800");
        test("200 - 10 = % %", "685,9", "685,9");

        test("20 + % = =", "28", "");
        test("20 + %", "4", "20  +  4");
        test("20 - % = =", "12", "");
        test("20 - %", "4", "20  -  4");
        test("20 * % = =", "320", "");
        test("20 * %", "4", "20  ×  4");
        test("20 / % = =", "1,25", "");
        test("20 / %", "4", "20  ÷  4");

        test("20 + 10 % + 15 % =", "25,3", "");
        test("20 + 10 % + 15 % = =", "28,6", "");
        test("20 - 10 % - 15 % = =", "12,6", "");
        test("20 * 10 % * 15 % = =", "1 440", "");
        test("20 / 10 % / 15 % = =", "4,444444444444444", "");

        test("20 + 10 % + = =", "66", "");
        test("20 - 10 % - = =", "-18", "");
        test("20 * 10 % * = =", "64 000", "");
        test("20 / 10 % / = =", "0,1", "");
        test("2 + 3 - 4 * 5 / 6 % =", "16,66666666666667", "");
        test("3 % + 4 - 5 * 6 ± / 7 %", "0,42", "0  +  4  -  5  ×  -6  ÷  0,42");
        test("3 % + 4 - 5 * 6 ± / 7 % =", "14,28571428571429", "");
        test("10 % + 9 % - 8 + 7 * 6 % =", "0,06", "");

        test("20 ± + %", "4", "-20  +  4");
        test("20 ± + % =", "-16", "");
        test("20 + 50 % = =", "40", "");
        test("20 + 50 % = = =", "50", "");

        test("20 - %", "4", "20  -  4");
        test("20 - % = = =", "8", "");
        test("20 - 50 % = = =", "-10", "");
        test("20 ± - %", "4", "-20  -  4");
        test("20 ± - % =", "-24", "");
        test("20 ± - + * - %", "4", "-20  -  4");
        test("20 ± - 50 % = = =", "10", "");

        test("20 / % =", "5", "");
        test("20 / % = = =", "0,3125", "");
        test("20 / 50 % =", "2", "");
        test("20 / 50 % = =", "0,2", "");
        test("20 / 1234567 % =", "8,100005913004316e-5", "");

        test("20 ± % % =", "0", "");
        test("20 ± + 10 ± % % =", "-20,4", "");
        test("20 ± % ± % =", "0", "");
        test("20 ± % ± % ± =", "0", "");
        test("20 ± ± ± % ± % ± =", "0", "");
        test("1234567899876543 ± ± ± % ± % ± =", "0", "");
        test("1234567899876543 ± ± ± % ± % ± = = =", "0", "");

        test("320 - 20 % ", "64", "320  -  64");
        test("320 - 20 % =", "256", "");
        test("320 * 20 % =", "20 480", "");
        test("320 * + / - % =", "-704", "");
        test("320 + - % =", "-704", "");
        test("320 ± - % = =", "-2 368", "");

        test("12345 / 2 %", "246,9", "12345  ÷  246,9");
        test("12345 / 2 % =", "50", "");
        test("2 / 12345 %", "246,9", "2  ÷  246,9");
        test("2 / 12345 % =", "0,0081004455245038", "");

        test("25 + sqrt %", "1,25", "25  +  1,25");
        test("25 + sqrt ± %", "-1,25", "25  +  -1,25");
        test("25 sqr + sqrt ± % % %", "-6 103,515625", "sqr( 25 )  +  -6103,515625");

        test("37 + 48 = % sqrt","8,5","√( 72,25 )");
        test("37 + 48 = % sqrt %","7,225","7,225");
        test("128 sqrt sqrt - 10 %","0,3363585661014858","√( √( 128 ) )  -  0,3363585661014858");
        test("3 + 3 = % =","3,36","");
        test("3 * 3 = % =","2,43","");

    }

    @Test
    void testScalingAndRounding(){

        test("10 / 9 * 0,1 *", "0,1111111111111111", "10  ÷  9  ×  0,1  ×  ");
        test("10 / 11 =", "0,9090909090909091", "");
        test("1000000000000000 / 1111111111111111 =", "0,9000000000000001", "");
        test("1,999999999999999 * 0,1 = ", "0,1999999999999999", "");
        test("1,999999999999999 * 0,1 = =", "0,02", "");

        test("1 / 3 /", "0,3333333333333333", "1  ÷  3  ÷  ");
        test("3 1/x", "0,3333333333333333", "1/( 3 )");
        test("1 / 6 /", "0,1666666666666667", "1  ÷  6  ÷  ");
        test("1 / 3 * 3 +", "1", "1  ÷  3  ×  3  +  ");
        test("1 / 3 * 0,0000000000000001 * 0,00000000001 * 10000000000000000 * 10000000000000 * 3 *","10","1  ÷  3  ×  0,0000000000000001  ×  0,00000000001  ×  1000000000000000  ×  10000000000000  ×  3  ×  ");

        test("2 sqrt", "1,414213562373095", "√( 2 )");
        test("3 sqrt", "1,732050807568877", "√( 3 )");
        test("10 / 9 /", "1,111111111111111", "10  ÷  9  ÷  ");
        test("10 / 3 * ", "3,333333333333333", "10  ÷  3  ×  ");
        test("20 sqrt", "4,472135954999579", "√( 20 )");
        test("30 sqrt", "5,477225575051661", "√( 30 )");

        test("100 / 9 /", "11,11111111111111", "100  ÷  9  ÷  ");
        test("100 / 3 * ", "33,33333333333333", "100  ÷  3  ×  ");
        test("200 sqrt", "14,14213562373095", "√( 200 )");
        test("30 sqrt", "5,477225575051661", "√( 30 )");

        test("1000000000000000 / 9 /", "111 111 111 111 111,1", "1000000000000000  ÷  9  ÷  ");
        test("100000000000000 / 9 /", "11 111 111 111 111,11", "100000000000000  ÷  9  ÷  ");

        test("1000000000000000 ± / 9 =", "-111 111 111 111 111,1", "");
        test("100000000000000 ± / 9 =", "-11 111 111 111 111,11", "");

        test("1000000000000000 ± / 9 /", "-111 111 111 111 111,1", "-1000000000000000  ÷  9  ÷  ");
        test("100000000000000 ± / 9 /", "-11 111 111 111 111,11", "-100000000000000  ÷  9  ÷  ");

        test("9999999999 * 9999999999 * 99 / 9999999999999999 =", "989 999,9998020001", "");

        test("1000000000000000 + 0,1 + 0,1 + 0,1 + 0,1 + ","1 000 000 000 000 000","1000000000000000  +  0,1  +  0,1  +  0,1  +  0,1  +  ");
        test("1000000000000000 + 0,1 + 0,1 + 0,1 + 0,1 + 0,1 +","1 000 000 000 000 001","1000000000000000  +  0,1  +  0,1  +  0,1  +  0,1  +  0,1  +  ");
        test("1000000000000000 + 0,1 + 0,1 + 0,1 + 0,1 + 0,1 + 0,1 +","1 000 000 000 000 001","1000000000000000  +  0,1  +  0,1  +  0,1  +  0,1  +  0,1  +  0,1  +  ");
        test("1000000000000000 + 0,1 + 0,1 + 0,1 + 0,1 + 0,1 + 0,1 + 0,1 + 0,1 + 0,1 + 0,1 + 0,1 + 0,1 + 0,1 + 0,1 + 0,1 +","1 000 000 000 000 002","1000000000000000  +  0,1  +  0,1  +  0,1  +  0,1  +  0,1  +  0,1  +  0,1  +  0,1  +  0,1  +  0,1  +  0,1  +  0,1  +  0,1  +  0,1  +  0,1  +  ");

        test("1,000000000000001 * 0,01 = ","0,01","");
        test("1,000000000000005 * 0,01 = ","0,0100000000000001","");
        test("9,999999999999999 * 0,01 = ","0,1","");

        test("9999999999999999 * 10 + 10 -","1,e+17","9999999999999999  ×  10  +  10  -  ");
        test("9999999999999999 * 10 + 1 = = = = =","1,e+17","");


        test("0,0123456789123459 * 0,1 =", "0,0012345678912346", "");
        test("0,0123456789123499 * 0,1 =", "0,001234567891235", "");
        test("0,1234567891234567 * 0,1 =", "0,0123456789123457", "");
    }

    @Test
    void testEMinusValues(){

        test("1 * 0,1 =", "0,1", "");
        test("1 * 0,1 = =", "0,01", "");
        test("1 * 0,1 = = =", "0,001", "");
        test("1 * 0,1 = = = =", "0,0001", "");
        test("1 * 0,1 = = = = =", "0,00001", "");
        test("25 sqrt sqr sqrt sqr 1/x", "0,04", "1/( sqr( √( sqr( √( 25 ) ) ) ) )");

        test("1 / 1000000000000000 = ", "0,000000000000001", "");
        test("1 / 1000000000000000 / 10 = ", "0,0000000000000001", "");
        test("1 / 1000000000000000 / 10 = =", "1,e-17", "");
        test("1 / 1000000000000000 / 10 = = *", "1,e-17", "1,e-17  ×  ");
        test("1 / 1000000000000000 = =", "1,e-30", "");
        test("22 / 100000000 =", "0,00000022", "");
        test("22 / 1000000000 =", "0,000000022", "");
        test("22 / 10000000000 =", "0,0000000022", "");
        test("22 / 100000000000 =", "0,00000000022", "");
        test("22 / 1000000000000 =", "0,000000000022", "");
        test("22 / 10000000000000 =", "0,0000000000022", "");
        test("22 / 100000000000000 =", "0,00000000000022", "");
        test("22 / 1000000000000000 =", "0,000000000000022", "");
        test("22 / 1000000000000000 / 10 =", "0,0000000000000022", "");
        test("22 / 1000000000000000 / 10 = =", "2,2e-16", "");
        test("22 / 1000000000000000 / 10 = = =", "2,2e-17", "");
        test("22 / 1000000000000000 = =", "2,2e-29", "");

        test("333 / 1000000000000000 = =", "3,33e-28", "");
        test("333 / 1000000000000000 = =", "3,33e-28", "");
        test("1 / 1000000000000000 " + addEquals(100), "1,e-1500", "");

        test("0,0000000000000023 * 0,1 =", "2,3e-16", "");
        test("0,0000000000000234 * 0,1 =", "2,34e-15", "");
        test("0,0000000000002345 * 0,1 =", "2,345e-14", "");
        test("0,0000000000023456 * 0,1 =", "2,3456e-13", "");
        test("0,0000000000234567 * 0,1 =", "2,34567e-12", "");
        test("0,0000000002345678 * 0,1 =", "2,345678e-11", "");
        test("0,0000000023456789 * 0,1 =", "2,3456789e-10", "");
        test("0,0000000234567891 * 0,1 =", "2,34567891e-9", "");
        test("0,0000002345678912 * 0,1 =", "2,345678912e-8", "");
        test("0,0000023456789123 * 0,1 =", "2,3456789123e-7", "");
        test("0,0000123456789123 * 0,1 =", "1,23456789123e-6", "");
        test("0,0001234567891234 * 0,1 =", "1,234567891234e-5", "");
        test("0,0012345678912341 * 0,1 =", "1,2345678912341e-4", "");
        test("1,234567891234567 * 0,1 =", "0,1234567891234567", "");
        test("0,0001111111111111 * 0,1 =", "1,111111111111e-5", "");

        test("0,0000000000000023 * 0,1 = ±", "-2,3e-16", "negate( 2,3e-16 )");
        test("0,0000000000000234 * 0,1 = ±", "-2,34e-15", "negate( 2,34e-15 )");
        test("0,0000000000002345 * 0,1 = ±", "-2,345e-14", "negate( 2,345e-14 )");
        test("0,0000000000023456 * 0,1 = ±", "-2,3456e-13", "negate( 2,3456e-13 )");
        test("0,0000000000234567 * 0,1 = ±", "-2,34567e-12", "negate( 2,34567e-12 )");
        test("0,0000000002345678 * 0,1 = ±", "-2,345678e-11", "negate( 2,345678e-11 )");
        test("0,0000000023456789 * 0,1 = ±", "-2,3456789e-10", "negate( 2,3456789e-10 )");
        test("0,0000000234567891 * 0,1 = ±", "-2,34567891e-9", "negate( 2,34567891e-9 )");
        test("0,0000002345678912 * 0,1 = ±", "-2,345678912e-8", "negate( 2,345678912e-8 )");
        test("0,0000023456789123 * 0,1 = ±", "-2,3456789123e-7", "negate( 2,3456789123e-7 )");
        test("0,0123456789123456 * 0,1 = ±", "-0,0012345678912346", "negate( 0,0012345678912346 )");
        test("0,1234567891234567 * 0,1 = ±", "-0,0123456789123457", "negate( 0,0123456789123457 )");

        test("0,1111111111111111 * 0,1 =", "0,0111111111111111", "");
        test("0,1111111111111111 * 0,1 = =", "0,0011111111111111", "");
        test("0,1111111111111111 * 0,1 = = =", "1,111111111111111e-4", "");
        test("0,1111111111111111 * 0,1 = = = =", "1,111111111111111e-5", "");
        test("0,1111111111111111 * 0,1 = = = = =", "1,111111111111111e-6", "");
        test("0,1111111111111111 * 0,1 = = = = = =", "1,111111111111111e-7", "");

        test("5 / 10000 / 3 =", "1,666666666666667e-4", "");
        test("5 / 3 = = = = / 100 =", "6,17283950617284e-4", "");

    }

    @Test
    void testMaxPossibleValues(){

        //get 9,9999999999999990e+9999 and add 1,e+9983 4 times
        test("1000000000000000 sqr sqr sqr sqr sqr sqr sqr sqr sqr * 1000000000000000 sqr sqr sqr sqr sqr sqr sqr * 1000000 sqr sqr sqr sqr sqr sqr / 10 * MS 1000000000000000 * 10 = - MR * 10 = + MR = = = =","9,999999999999999e+9999","");
        //get 9,9999999999999990e+9999 and add 1,e+9983 5 times
        test("1000000000000000 sqr sqr sqr sqr sqr sqr sqr sqr sqr * 1000000000000000 sqr sqr sqr sqr sqr sqr sqr * 1000000 sqr sqr sqr sqr sqr sqr / 10 * MS 1000000000000000 * 10 = - MR * 10 = + MR = = = = =","Overflow","");
        //get 9,99999999999999940e+9999 and add 1,e+9982 9 times
        test("1000000000000000 sqr sqr sqr sqr sqr sqr sqr sqr sqr * 1000000000000000 sqr sqr sqr sqr sqr sqr sqr * 1000000 sqr sqr sqr sqr sqr sqr / 100 * MS 1000000000000000 * 100 = - MR = = = = = = * 10 = + MR "+addEquals(9),"9,999999999999999e+9999","");
        //get 9,99999999999999940e+9999 and add 1,e+9982 10 times
        test("1000000000000000 sqr sqr sqr sqr sqr sqr sqr sqr sqr * 1000000000000000 sqr sqr sqr sqr sqr sqr sqr * 1000000 sqr sqr sqr sqr sqr sqr / 100 * MS 1000000000000000 * 100 = - MR = = = = = = * 10 = + MR "+addEquals(10),"Overflow","");
        //get 9,999999999999999499999...
        test("1000000000000000 sqr sqr sqr sqr sqr sqr sqr sqr sqr * 1000000000000000 sqr sqr sqr sqr sqr sqr sqr * 1000000 sqr sqr sqr sqr sqr sqr / 100 * MS 1000000000000000 * 100 = - MR = = = = = - 0,0000000000000001 * 10 =","9,999999999999999e+9999","");
        //get 9,999999999999999499999... and add 0,000000000000001
        test("1000000000000000 sqr sqr sqr sqr sqr sqr sqr sqr sqr * 1000000000000000 sqr sqr sqr sqr sqr sqr sqr * 1000000 sqr sqr sqr sqr sqr sqr / 100 * MS 1000000000000000 * 100 = - MR = = = = = - 0,0000000000000001 * 10 = + 0,000000000000001 =","Overflow","");

        //get -9,9999999999999990e+9999 and subtract 1,e+9983 4 times
        test("1000000000000000 sqr sqr sqr sqr sqr sqr sqr sqr sqr * 1000000000000000 sqr sqr sqr sqr sqr sqr sqr * 1000000 sqr sqr sqr sqr sqr sqr / 10 * MS 1000000000000000 * 10 = - MR * 10 = ± - MR = = = =","-9,999999999999999e+9999","");
        //get -9,9999999999999990e+9999 and subtract 1,e+9983 5 times
        test("1000000000000000 sqr sqr sqr sqr sqr sqr sqr sqr sqr * 1000000000000000 sqr sqr sqr sqr sqr sqr sqr * 1000000 sqr sqr sqr sqr sqr sqr / 10 * MS 1000000000000000 * 10 = - MR * 10 = ± - MR = = = = =","Overflow","");
        //get -9,99999999999999940e+9999 and subtract 1,e+9982 9 times
        test("1000000000000000 sqr sqr sqr sqr sqr sqr sqr sqr sqr * 1000000000000000 sqr sqr sqr sqr sqr sqr sqr * 1000000 sqr sqr sqr sqr sqr sqr / 100 * MS 1000000000000000 * 100 = - MR = = = = = = * 10 = ± - MR "+addEquals(9),"-9,999999999999999e+9999","");
        //get -9,99999999999999940e+9999 and subtract 1,e+9982 10 times
        test("1000000000000000 sqr sqr sqr sqr sqr sqr sqr sqr sqr * 1000000000000000 sqr sqr sqr sqr sqr sqr sqr * 1000000 sqr sqr sqr sqr sqr sqr / 100 * MS 1000000000000000 * 100 = - MR = = = = = = * 10 = ± - MR "+addEquals(10),"Overflow","");
        //get -9,999999999999999499999...
        test("1000000000000000 sqr sqr sqr sqr sqr sqr sqr sqr sqr * 1000000000000000 sqr sqr sqr sqr sqr sqr sqr * 1000000 sqr sqr sqr sqr sqr sqr / 100 * MS 1000000000000000 * 100 = - MR = = = = = ± + 0,0000000000000001 * 10 =","-9,999999999999999e+9999","");
        //get -9,999999999999999499999... and subtract 0,000000000000001
        test("1000000000000000 sqr sqr sqr sqr sqr sqr sqr sqr sqr * 1000000000000000 sqr sqr sqr sqr sqr sqr sqr * 1000000 sqr sqr sqr sqr sqr sqr / 100 * MS 1000000000000000 * 100 = - MR = = = = = ± + 0,0000000000000001 * 10 = - 0,000000000000001 =","Overflow","");

        //get 1,e-9999
        test("0,0000000000000001 sqr sqr sqr sqr sqr sqr sqr sqr sqr * 0,00000000000001 sqr sqr sqr sqr sqr sqr sqr * 0,000000000000001 *","1,e-9999","sqr( sqr( sqr( sqr( sqr( sqr( sqr( sqr( sqr( 0,0000000000000001 ) ) ) ) ) ) ) ) )  ×  sqr( sqr( sqr( sqr( sqr( sqr( sqr( 0,00000000000001 ) ) ) ) ) ) )  ×  0,000000000000001  ×  ");
        //get 1,e-9999 and multiply by 0,9999999999999999
        test("0,0000000000000001 sqr sqr sqr sqr sqr sqr sqr sqr sqr * 0,00000000000001 sqr sqr sqr sqr sqr sqr sqr * 0,000000000000001 * 0,9999999999999999 =","Overflow","");

        //get -1,e-9999
        test("0,0000000000000001 sqr sqr sqr sqr sqr sqr sqr sqr sqr * 0,00000000000001 sqr sqr sqr sqr sqr sqr sqr * 0,000000000000001 ± *","-1,e-9999","sqr( sqr( sqr( sqr( sqr( sqr( sqr( sqr( sqr( 0,0000000000000001 ) ) ) ) ) ) ) ) )  ×  sqr( sqr( sqr( sqr( sqr( sqr( sqr( 0,00000000000001 ) ) ) ) ) ) )  ×  -0,000000000000001  ×  ");
        //get -1,e-9999 and multiply by 0,9999999999999999
        test("0,0000000000000001 sqr sqr sqr sqr sqr sqr sqr sqr sqr * 0,00000000000001 sqr sqr sqr sqr sqr sqr sqr * 0,000000000000001 ± * 0,9999999999999999 =","Overflow","");
    }

    @Test
    void testEPlusValues(){

        test("1000000000000000 + ", "1 000 000 000 000 000", "1000000000000000  +  ");
        test("1000000000000000 * 10 *", "1,e+16", "1000000000000000  ×  10  ×  ");
        test("1000000000000000 * 10 * 10 *", "1,e+17", "1000000000000000  ×  10  ×  10  ×  ");
        test("1000000000000000 * 10 * 10 / 10 /", "1,e+16", "1000000000000000  ×  10  ×  10  ÷  10  ÷  ");
        test("1000000000000000 + = = = = = = = = =", "1,e+16", "");
        test("1000000000000000 + = = = = = = = = = + 1 =", "1,e+16", "");

        test("12 * 1000000000000000 +","1,2e+16","12  ×  1000000000000000  +  ");
        test("123 * 1000000000000000 +","1,23e+17","123  ×  1000000000000000  +  ");
        test("1234 * 1000000000000000 +","1,234e+18","1234  ×  1000000000000000  +  ");
        test("12345 * 1000000000000000 +","1,2345e+19","12345  ×  1000000000000000  +  ");
        test("123456 * 1000000000000000 +","1,23456e+20","123456  ×  1000000000000000  +  ");
        test("1234567 * 1000000000000000 +","1,234567e+21","1234567  ×  1000000000000000  +  ");
        test("12345678 * 1000000000000000 +","1,2345678e+22","12345678  ×  1000000000000000  +  ");
        test("123456789 * 1000000000000000 +","1,23456789e+23","123456789  ×  1000000000000000  +  ");
        test("1234567890 * 1000000000000000 +","1,23456789e+24","1234567890  ×  1000000000000000  +  ");
        test("12345678901 * 1000000000000000 +","1,2345678901e+25","12345678901  ×  1000000000000000  +  ");
        test("123456789012 * 1000000000000000 +","1,23456789012e+26","123456789012  ×  1000000000000000  +  ");
        test("1234567890123 * 1000000000000000 +","1,234567890123e+27","1234567890123  ×  1000000000000000  +  ");
        test("12345678901234 * 1000000000000000 +","1,2345678901234e+28","12345678901234  ×  1000000000000000  +  ");
        test("123456789012345 * 1000000000000000 +","1,23456789012345e+29","123456789012345  ×  1000000000000000  +  ");
        test("1234567890123456 * 1000000000000000 +","1,234567890123456e+30","1234567890123456  ×  1000000000000000  +  ");

        test("1,2 * 1000000000000000 = =","1,2e+30","");
        test("1,23 * 1000000000000000 = =","1,23e+30","");
        test("1,234 * 1000000000000000 = =","1,234e+30","");
        test("1,2345 * 1000000000000000 = =","1,2345e+30","");
        test("1,23456 * 1000000000000000 = =","1,23456e+30","");
        test("1,234567 * 1000000000000000 = =","1,234567e+30","");
        test("1,2345678 * 1000000000000000 = =","1,2345678e+30","");
        test("1,23456789 * 1000000000000000 = =","1,23456789e+30","");
        test("1,234567890 * 1000000000000000 = =","1,23456789e+30","");
        test("1,2345678901 * 1000000000000000 = =","1,2345678901e+30","");
        test("1,23456789012 * 1000000000000000 = =","1,23456789012e+30","");
        test("1,234567890123 * 1000000000000000 = =","1,234567890123e+30","");
        test("1,2345678901234 * 1000000000000000 = =","1,2345678901234e+30","");
        test("1,23456789012345 * 1000000000000000 = =","1,23456789012345e+30","");
        test("1,234567890123456 * 1000000000000000 = =","1,234567890123456e+30","");

        test("1000000000000000 * =", "1,e+30", "");
        test("1000000000000000 * = *", "1,e+30", "1,e+30  ×  ");
        test("1000000000000000 * = * =", "1,e+60", "");
        test("256 sqr sqr sqr = =", "1,844674407370955e+19", "");
        test("9999999999999999 + " + addEquals(20), "2,1e+17", "");


        test("1234567890987654 * 4567890987654321 =","5,639371542889907e+30","");
        test("1000000000000000 / 0,1 =","1,e+16","");
        test("1000000000000000 / 0,1 = - 1 +","9 999 999 999 999 999","1,e+16  -  1  +  ");
        test("1000000000000000 / 0,1 = - 1000000000000000 =","9 000 000 000 000 000","");
        test("1000000000000000 / 0,01 =","1,e+17","");
        test("1000000000000000 / 0,0000000000000001 =","1,e+31","");
    }

    @Test
    void testClear(){

        test("5 C","0","");
        test("4 sqr C","0","");
        test("4 sqrt C","0","");
        test("4 1/x C","0","");
        test("4 % C","0","");
        test("4 ± C","0","");

        test("4 + C + 1 =","1","");
        test("4 + 5 C + 1 +","1","0  +  1  +  ");
        test("4 - C - 1 =","-1","");
        test("4 - 5 C - 1 -","-1","0  -  1  -  ");
        test("4 * C * 1 =","0","");
        test("4 * 5 C * 1 *","0","0  ×  1  ×  ");
        test("4 / C / 1 =","0","");
        test("4 / 5 C / 1 /","0","0  ÷  1  ÷  ");

        test("5 + 3 = C + 2 +", "2", "0  +  2  +  ");
        test("5 - 3 = C - 2 -", "-2", "0  -  2  -  ");
        test("5 * 3 = C * 2 *", "0", "0  ×  2  ×  ");
        test("5 / 3 = C / 2 /", "0", "0  ÷  2  ÷  ");

        test("5 + 3 = sqr C + 2 +", "2", "0  +  2  +  ");
        test("5 - 3 = sqrt C - 2 -", "-2", "0  -  2  -  ");
        test("5 * 3 = 1/x C * 2 *", "0", "0  ×  2  ×  ");
        test("5 / 3 = + % C / 2 /", "0", "0  ÷  2  ÷  ");

        test("5 MS + 3 = C - MR -", "-5", "0  -  5  -  ");
    }

    @Test
    void testClearEntered(){

        test("1 CE 2 - 4 +", "-2", "2  -  4  +  ");
        test("12 CE 34 - 12 +", "22", "34  -  12  +  ");

        test("16 sqr CE 16 - 12 +", "4", "16  -  12  +  ");
        test("16 sqrt CE 16 - 12 +", "4", "16  -  12  +  ");
        test("10 1/x CE 16 - 12 +", "4", "16  -  12  +  ");
        test("10 + 30 % CE", "0", "10  +  ");

        test("5 - 2333 CE", "0", "5  -  ");
        test("9 ± ± sqrt - CE", "0", "√( 9 )  -  ");
        test("9 ± ± sqrt - 2 = CE", "0", "");
        test("5 ± ± ± - 6 - ± ± ± CE", "0", "-5  -  6  -  ");
        test("5 ± ± ± - 6 - ± ± ± CE 25 -", "-36", "-5  -  6  -  25  -  ");

        test("10 + 30 % CE 16 -", "26", "10  +  16  -  ");
        test("10 + 30 % CE 16 - 12 +", "14", "10  +  16  -  12  +  ");


        test("10 + 30 = CE 16 - 12 +", "4", "16  -  12  +  ");
        test("1 + 20 % - 3 sqr * 4 sqrt / 5 1/x + 67 ⟵ - 7,8 ± * 8 CE 9 *","-577,8","1  +  0,2  -  sqr( 3 )  ×  √( 4 )  ÷  1/( 5 )  +  6  -  -7,8  ×  9  ×  ");
        test("1234 CE 3456 ⟵ ⟵ ⟵ + % -","3,09","3  +  0,09  -  ");
        test("1234 sqrt CE 3456 ⟵ ⟵ ⟵ + =","6","");
        test("1234 + 3456 = CE sqrt ⟵ ⟵ ⟵ +","0","√( 0 )  +  ");
    }

    @Test
    void testBackSpace(){

        test("⟵","0","");
        test("C ⟵","0","");
        test(", ⟵", "0", "");
        test("5 ⟵", "0", "");
        test("5 , ⟵", "5", "");
        test("5 , ± ⟵", "-5", "");
        test("25 ± ⟵", "-2", "");
        test("1234 ⟵ ⟵ ⟵", "1", "");
        test("1234 ± ⟵ ⟵ ⟵", "-1", "");

        test("987 ⟵ ⟵ sqrt 2 -", "2", "2  -  ");
        test("987 ⟵ ⟵ sqrt ± -", "-3", "negate( √( 9 ) )  -  ");
        test("987 ⟵ ⟵ sqrt + 2 -", "5", "√( 9 )  +  2  -  ");
        test("987 ± ⟵ ⟵ + 10 % -", "-9,9", "-9  +  -0,9  -  ");
        test("1234 ± ⟵ ⟵ / 2 -", "-6", "-12  ÷  2  -  ");

        test("1234567890 ⟵ ⟵ ⟵ ⟵ ⟵ ⟵ ⟵ ⟵ ⟵ ⟵", "0", "");
        test("1234567890,01234567 ⟵ ⟵ ⟵ ⟵ ⟵ ⟵ ⟵ ⟵ ⟵ ⟵ ⟵ ⟵ ⟵ ⟵ ⟵ ⟵ ⟵ ⟵ ⟵", "0", "");
        test("1234567890,01234567 ⟵ ⟵ ⟵ ⟵ /", "1 234 567 890,01", "1234567890,01  ÷  ");
        test("1234567890,567 ± ⟵ ⟵ ⟵ ⟵ /", "-1 234 567 890", "-1234567890  ÷  ");
        test("1234567890,567 ± sqr ⟵ ⟵ ⟵ ⟵ /", "1,524157876419052e+18", "sqr( -1234567890,567 )  ÷  ");
        test("1234567890,567 ± sqr ⟵ ⟵ ⟵ ⟵ / =", "1", "");
        test("4 sqr ⟵ ⟵ ⟵ ⟵ +", "16", "sqr( 4 )  +  ");
        test("2567890,134 ± ⟵ ⟵ ⟵ ⟵ sqr sqrt sqrt sqrt", "40,03078475546843", "√( √( √( sqr( -2567890 ) ) ) )");
        test("9 ± ± sqrt - ⟵ +", "3", "√( 9 )  +  ");
        test("9 ± ± 1/x - ⟵ +", "0,1111111111111111", "1/( 9 )  +  ");
        test("25 - 200 ⟵ % +", "20", "25  -  5  +  ");


        test("12 ⟵ ⟵ ⟵ ⟵ ⟵ ⟵ ⟵ ⟵ ⟵ ⟵", "0", "");
        test("0 ⟵ ⟵ ⟵ ⟵ ⟵ ⟵ ⟵ ⟵ ⟵ ⟵ 1", "1", "");
        test("0 ⟵ ⟵ ⟵ ⟵ ⟵ ⟵ ⟵ ⟵ ⟵ ⟵ 1 + 2 -", "3", "1  +  2  -  ");
        test("1234567890 ⟵ ⟵ ⟵ ⟵ ⟵ ⟵ ⟵ ⟵ ⟵ ⟵ ⟵", "0", "");
        test("12 + 34 = ⟵ ⟵ ⟵ + 5678 ⟵ ⟵ =","102","");
        test("1234 CE 3456 ⟵ ⟵ ⟵ sqr","9","sqr( 3 )");
        test("1234 + % ⟵ ⟵ ⟵ sqrt","123,4","1234  +  √( 15227,56 )");

        test("9876543 ⟵ ⟵ ⟵ ⟵ ⟵ ⟵ sqrt ⟵","3","√( 9 )");
        test("98 ⟵ sqrt + 23 ⟵ sqr = ⟵ ⟵","7","");
        test("98 ⟵ sqrt + 23 ⟵ sqr = 12345 ⟵ ⟵","123","");
        test("257654 ⟵ ⟵ ⟵ ⟵ ⟵ sqr ⟵","4","sqr( 2 )");
        test("257654 ⟵ ⟵ ⟵ ⟵ 1/x ⟵","0,04","1/( 25 )");
        test("257654 ⟵ ⟵ ⟵ ⟵ ± ⟵","-2","");
        test("257654 ⟵ ⟵ ⟵ ⟵ - ⟵","25","25  -  ");
        test("257654 ⟵ ⟵ ⟵ ⟵ * ⟵","25","25  ×  ");
        test("257654 ⟵ ⟵ ⟵ ⟵ / ⟵","25","25  ÷  ");
        test("257654 ⟵ ⟵ ⟵ ⟵ + ⟵ - 5 +","20","25  -  5  +  ");
        test("25 + 20 % ⟵ ⟵ ⟵ ⟵ ⟵ -","30","25  +  5  -  ");
    }

    @Test
    void testMemory(){

        test("MS 10 MR","0","");
        test("20 MS", "20", "");
        test("20 MS + 10 + MR", "20", "20  +  10  +  ");
        test("20 MS + 10 + MR =", "50", "");
        test("20 MS + 10 + MR = ⟵", "50", "");
        test("20 MS + 10 + MR = MC", "50", "");
        test("20 MS + 10 + MR 5 =", "35", "");
        test("20 MS + 10 + MR ±", "-20", "20  +  10  +  ");
        test("20 MS + 10 + MR 1/x", "0,05", "20  +  10  +  1/( 20 )");
        test("20 MS + 10 + MR %", "6", "20  +  10  +  6");
        test("2 + 3 MS / MR =", "1,666666666666667", "");
        test("2 + 3 sqr MS / MR =", "1,222222222222222", "");
        test("20 + 40 % MS / MR =", "3,5", "");

        test("M- - MR +", "0", "0  -  0  +  ");
        test("20 M- - MR +", "40", "20  -  -20  +  ");
        test("20 M+ - MR +", "0", "20  -  20  +  ");

        test("MS MR", "0", "");
        test("C MS MR", "0", "");
        test("3 M+ MR", "3", "");
        test("3 M+ ⟵ MR", "3", "");
        test("1 M+ 2 + MR +", "3", "2  +  1  +  ");
        test("123 M- MR", "-123", "");
        test("10 MS 123456789 M+ M- MR", "10", "");
        test("1 M+ 2", "2", "");
        test("1 M- 2", "2", "");
        test("1 M+ 2 M- MR", "-1", "");

        test("12 + 3 = M+ MR", "15", "");
        test("12 + 3 = M+ 123456789 + = M+ MR", "246 913 593", "");
        test("12 + 3 = M+ 123456789 + = M+ 100000000 M+ MR", "346 913 593", "");
        test("123456789 + 1 M+", "1", "123456789  +  ");
        test("123456789 + 1 M+ + 2 M+ MR", "3", "123456789  +  1  +  ");
        test("123456789 + 1 M+ + 2 M+ = 34 + 5 = M+ MR", "42", "");
        test("123456789 + 1 M+ + 2 M+ = 34 + 5 = M+ 1", "1", "");
        test("123456789 + 987654321 = M+ + 321 / - + * MR = =", "1,371742504663922e+27", "");

        test("12 + 3 = M- MR", "-15", "");
        test("12 + 3 = M- MR 21", "21", "");
        test("12 + 3 = M- 123456789 + 123456789 = M- MR", "-246 913 593", "");
        test("12 + 3 = M- 123456789 + 123456789 = M- 100000000 M- MR", "-346 913 593", "");
        test("987654321 + 1 M- + 2 M- MR", "-3", "987654321  +  1  +  ");
        test("987654321 + 1 M- + 2 M- = 32 + 3 = M- MR", "-38", "");
        test("987654321 + 1 M- + 2 M- = 32 + 3 = M- 10", "10", "");
        test("987654321 + 123456789 = M- + 123 - + / * MR = =", "1,371742260219478e+27", "");
        test("9999999999999999 M- MR", "-9 999 999 999 999 999", "");
        test("9999999999999999 M- M- MR", "-2,e+16", "");

        test("12 + 1 = M- MR 34 + =", "68", "");
        test("1234567890 + 10 = M- MR * 23 + ", "-28 395 061 700", "-1234567900  ×  23  +  ");
        test("1234567890 - 10 = M- MR * 23 + =", "-56 790 122 480", "");

        test("1000000000000000 * = M+ MR", "1,e+30", "");
        test("1000000000000000 * = M+ M+ MR", "2,e+30", "");
        test("1000000000 * = M+ MR M+ MR", "2,e+18", "");
        test("1000000000000000 * = M- MR", "-1,e+30", "");
        test("1000000000000000 * = M- M- MR", "-2,e+30", "");
        test("1000000000 * = M+ MR M+ MR", "2,e+18", "");

        test("M+","0","");
        test("M-","0","");
        test("MS","0","");
        test("M+","0","");
        test("MS M+","0","");
        test("M+ M- MR","0","");
        test("2 MS MR MC","2","");
        test("4 MS C MR + 16 -","20","4  +  16  -  ");
        test("2 MS 0 MR","2","");
        test("8 M+ MS MR","8","");
        test("124 MS M- MR","0","");
        test("5 ± MS 4 MR","-5","");
        test("1 + 2 MS 5 MR","2","1  +  ");
        test("2 MS 90 MR","2","");
        test("51 + MS 1 MR","51","51  +  ");
        test("91 MS 1 M+ MR","92","");
        test("30 MS 4 M+ MR","34","");
        test("0,5 MS 2 MR + 2 * 3 =","7,5","");
        test("4 MS / 2 + MR =","6","");
        test("34 MS 2 + 1 = MR","34","");
        test("1 MS C C M+ C 32 MR","1","");
        test("2 + 1 MS C MR * 34","34","1  ×  ");
        test("1244 MS 57422 MR","1 244","");
        test("2 MS 3 M+ 2 MR * 38 =","190","");
        test("64 MS CE 300 M+ MR", "364","");
        test("5 MS 53 / 2 M- M- + 4","4","53  ÷  2  +  ");
        test("4 MS + 3 M+ * 2 MR","7","4  +  3  ×  ");
        test("22 - 2 MS / 2 C MR * 2","2","2  ×  ");
        test("0,53 MS - 3 M+ MR","3,53","0,53  -  ");
        test("1 MS 0,12 M- M- * 3 = MR","0,76","");
        test("93 MS - 4 M+ * 2 MR M-","97","93  -  4  ×  ");
        test("53 + 4 MS 4 M- 5 M+ * 12 MR =","290","");
        test("64 sqrt MS CE 300 + 22 - MR / 2 =", "157","");
        test("40 MS 2,341 M- M- / 2 + MR =","36,4885","");
        test("57 MS 2 M+ M+ M+ MR / 2 +","31,5","63  ÷  2  +  ");
        test("43 MS + 5325 1/x C ⟵ M- - 3 * MR =", "-129","");
        test("7320 / 3 * 1 + 3 sqrt MS C MR * 2" , "2","1,732050807568877  ×  ");
    }

    ////////////////////////////////////////////

    @Test
    void testDragWindow() {

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
    void testResizeWindowFromAngle() {

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
    void testFullScreenAfterDrag() {

        testFullScreenWithDrag(0, -300);
        testFullScreenWithDrag(-300, 0);
    }

    @Test
    void testExitMethod() {

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

    private void test(String expression, String display, String history) {

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

    private String addEquals(int count) {

        StringBuilder result = new StringBuilder("=");
        for (int i = 1; i < count; i++) {
            result.append(" =");
        }
        return result.toString();
    }
}
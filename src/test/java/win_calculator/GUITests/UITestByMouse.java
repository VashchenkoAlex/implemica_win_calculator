package win_calculator.GUITests;

import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.input.MouseButton;
import org.testfx.api.FxRobot;
import javafx.stage.Stage;
import org.junit.jupiter.api.Test;
import org.testfx.framework.junit5.ApplicationTest;
import org.testfx.framework.junit5.Start;
import org.testfx.matcher.control.LabeledMatchers;
import org.testfx.robot.Motion;
import win_calculator.MainApp;

import java.io.IOException;
import java.util.HashMap;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static win_calculator.GUITests.TestUtils.createButtonsMap;
import static org.testfx.api.FxAssert.verifyThat;

public class UITestByMouse extends ApplicationTest {

    private MainApp app = new MainApp();
    private FxRobot fxRobot = new FxRobot();
    private Scene scene;
    private static final HashMap<String, TestButton> buttons = createButtonsMap();

    @Start
    public void start(Stage primaryStage) throws IOException {

        app.setUpApp();
        scene = app.getStage().getScene();
    }

    @Test
    void testUI() {

        testMenuList();

        test("1 + 2 - 3 C","0","");
        test("1 + 2 - 3 CE 4 -","-1","1  +  2  -  4  -  ");
        test("1 MS + 2 - MR + M- MR * 3 = MC","3","");
        test("1 + 2 0 % - 3 sqr * 4 sqrt / 5 1/x + 6 7 <- - 7 , 8 ± * 8 CE 9 / , 1 +","-5 778","1  +  0,2  -  sqr( 3 )  ×  √( 4 )  ÷  1/( 5 )  +  6  -  -7,8  ×  9  ÷  0,1  +  ");
    }

    @Test
    void runTestDrag(){

        testDrag(20,60);
        testDrag(100,100);
        testDrag(100,200);

    }

    private void test(String expression, String display, String history) {

        String[] buttonKeys = expression.split(" ");
        for (String key : buttonKeys) {
            clickOn(key);
        }
        verifyThat("#display", LabeledMatchers.hasText(display));
        verifyThat("#historyField", LabeledMatchers.hasText(history));
    }

    private void testMenuList(){

        clickOn("MENU");
        verifyThat("#droppedList",Node::isVisible);
        verifyThat("#menuBtnPressed",Node::isVisible);
        verifyThat("#menuBtnPressed",Node::isVisible);
        verifyThat("#aboutBtn",Node::isVisible);
        clickOn("MENU");
    }

    private void testDrag(double x, double y){

        Node node = scene.getRoot();
        double minX = node.localToScreen(node.getBoundsInLocal()).getMinX();
        double minY = node.localToScreen(node.getBoundsInLocal()).getMinY();
        System.out.println(minX + " : " + minY);
        fxRobot.moveTo(minX+20,minY+20);
        fxRobot.press(MouseButton.PRIMARY);
        fxRobot.moveTo(minX + x,minY + y,Motion.HORIZONTAL_FIRST);
        fxRobot.release(MouseButton.PRIMARY);
        ((Parent) node).layout();
        System.out.println(node.localToScreen(node.getBoundsInLocal()).getMinX() + " : " + node.localToScreen(node.getBoundsInLocal()).getMinY());
//        assertEquals(minX - x,node.localToScreen(node.getBoundsInLocal()).getMinX());
//        assertEquals(minY + y - 20,node.localToScreen(node.getBoundsInLocal()).getMinY());
    }

    private void clickOn(String key) {

        fxRobot.clickOn(buttons.get(key).getId());
    }

    private void testResize(){

        double height = scene.getHeight();
        double width = scene.getWidth();
        fxRobot.moveTo(0,0);
        fxRobot.press(MouseButton.PRIMARY);
        fxRobot.moveTo(width,height);
        fxRobot.release(MouseButton.PRIMARY);
    }
}

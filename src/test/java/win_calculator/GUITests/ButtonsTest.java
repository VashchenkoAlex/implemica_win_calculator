package win_calculator.GUITests;

import javafx.stage.Stage;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.testfx.api.FxRobot;
import org.testfx.framework.junit5.*;
import org.testfx.matcher.control.LabeledMatchers;
import win_calculator.MainApp;

import java.io.IOException;

import static org.testfx.api.FxAssert.verifyThat;


@ExtendWith(ApplicationExtension.class)
class ButtonsTest {

    private MainApp app = new MainApp();
    private FxRobot robot;

    @Start
    void start(Stage primaryStage) throws IOException {

        app.setUpApp();
    }

    @Test
    void TestNumbers(FxRobot robot){

        this.robot = robot;
        test("#buttonOne","1","");
        test("#buttonTwo","12","");
        test("#buttonThree","123","");
        test("#buttonFour","1 234","");
        test("#buttonFive","12 345","");
        test("#buttonSix","123 456","");
        test("#buttonSeven","1 234 567","");
        test("#buttonEight","12 345 678","");
        test("#buttonNine","123 456 789","");
        test("#buttonZero","1 234 567 890","");
        test("#buttonComa","1 234 567 890,","");
        test("#buttonZero","1 234 567 890,0","");
        test("#buttonNine","1 234 567 890,09","");
        test("#buttonEight","1 234 567 890,098","");
        test("#buttonSeven","1 234 567 890,0987","");
        test("#buttonSix","1 234 567 890,09876","");
        test("#buttonFive","1 234 567 890,098765","");
        test("#buttonFour","1 234 567 890,098765","");
        test("#buttonComa","1 234 567 890,098765","");
        test("#buttonClear","0","");
    }

    @Test
    void TestMainOperations(FxRobot robot){

        this.robot = robot;
        test("#buttonTwo","2","");
        test("#buttonPlus","2","2  +  ");
        test("#buttonThree","3","2  +  ");
        test("#buttonMinus","5","2  +  3  -  ");
        test("#buttonMultiply","5","2  +  3  ×  ");
        test("#buttonFour","4","2  +  3  ×  ");
        test("#buttonDivide","20","2  +  3  ×  4  ÷  ");
        test("#buttonNine","9","2  +  3  ×  4  ÷  ");
        test("#buttonSqrt","3","2  +  3  ×  4  ÷  √( 9 )");
        test("#buttonSqr","9","2  +  3  ×  4  ÷  sqr( √( 9 ) )");
        test("#buttonMultiply","2,222222222222222","2  +  3  ×  4  ÷  sqr( √( 9 ) )  ×  ");
        test("#buttonClear","0","");
    }

    private void test(String btnId, String display,String history){

        robot.clickOn(btnId);
        verifyThat("#display",LabeledMatchers.hasText(display));
        verifyThat("#historyField",LabeledMatchers.hasText(history));
    }
}

package win_calculator.GUITests;

import javafx.stage.Stage;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.testfx.api.FxRobot;
import org.testfx.framework.junit5.*;
import org.testfx.matcher.control.LabeledMatchers;
import org.testfx.matcher.control.TextInputControlMatchers;
import win_calculator.MainApp;

import java.io.IOException;

import static org.testfx.api.FxAssert.verifyThat;


@ExtendWith(ApplicationExtension.class)
class DigitButtonsTest {

    private MainApp app = new MainApp();
    private FxRobot robot;

    @Start
    void start(Stage primaryStage) throws IOException {

        app.setUpApp();
    }

    @Test
    void numbersTest(FxRobot robot){

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
        test("#buttonFour","1 234 567 890,0987654","");
        test("#buttonThree","1 234 567 890,09876543","");
        test("#buttonTwo","1 234 567 890,098765432","");
        test("#buttonOne","1 234 567 890,0987654321","");
        test("#buttonZero","1 234 567 890,09876543210","");
        test("#buttonComa","1 234 567 890,09876543210","");
    }

    private void test(String btnId, String display,String history){

        robot.clickOn(btnId);
        verifyThat("#display",TextInputControlMatchers.hasText(display));
        verifyThat("#historyField",LabeledMatchers.hasText(history));
    }
}

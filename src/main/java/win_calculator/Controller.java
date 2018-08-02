package win_calculator;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import org.apache.commons.lang.StringUtils;

public class Controller
{

    @FXML private TextField firstNameField;
    @FXML private Label messageLabel;

    public void sayHello() {

        String firstName = firstNameField.getText();

        StringBuilder builder = new StringBuilder();

        if (!StringUtils.isEmpty(firstName)) {
            builder.append(firstName);
        }

        if (builder.length() > 0) {
            String name = builder.toString();
            messageLabel.setText("Hello " + name);
        } else {
            messageLabel.setText("Hello mysterious person");
        }
    }

    public void buttonPercentClick(){

    }

    public void buttonSqrtClick(){

    }

    public void buttonSqrClick(){

    }

    public void buttonFractionOneClick(){

    }

    public void buttonClearEnteredClick(){

    }

    public void buttonClearClick(){

    }

    public void buttonBackSpaceClick(){

    }

    public void buttonDivideClick(){

    }

    public void buttonSevenClick(){

    }

    public void buttonEightClick(){

    }

    public void buttonNineClick(){

    }


    public void buttonMultiplyClick(){

    }

    public void buttonFourClick(){

    }

    public void buttonFiveClick(){

    }

    public void buttonSixClick(){

    }

    public void buttonMinusClick(){

    }

    public void buttonOneClick(){

    }

    public void buttonTwoClick(){

    }

    public void buttonThreeClick(){

    }

    public void buttonPlusClick(){

    }

    public void buttonNegateClick(){

    }

    public void buttonZeroClick(){

    }

    public void buttonComaClick(){

    }

    public void buttonEnterClick(){

    }

    public void buttonClearAllMemoryClick(){

    }

    public void buttonMemoryRecallClick(){

    }

    public void buttonMemoryAddClick(){

    }

    public void buttonMemorySubtractClick(){

    }

    public void buttonMemoryStoreClick(){

    }

    public void buttonMemoryClick(){

    }




}

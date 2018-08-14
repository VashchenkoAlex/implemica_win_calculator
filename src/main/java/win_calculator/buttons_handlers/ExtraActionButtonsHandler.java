package win_calculator.buttons_handlers;

import win_calculator.exceptions.MyException;
import win_calculator.extra_operations.ExtraOperation;

import java.math.BigDecimal;

public class ExtraActionButtonsHandler {

    public String doOperation(ExtraOperation operation,BigDecimal number){
        String result;
        try {
            result = operation.calculate(number).toString();
        } catch (MyException e) {
            result = e.getMessage();
        }
        return result;
    }
}

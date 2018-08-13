package win_calculator.buttons_handlers;

import win_calculator.extra_operations.ExtraOperation;

import java.math.BigDecimal;

public class ExtraActionButtonsHandler {

    public String doOperation(ExtraOperation operation,BigDecimal firstNumber,BigDecimal secondNumber){

        String result;
        if (firstNumber != null){
            result = operation.calculate(firstNumber,secondNumber).toString();
        }else {
            result = "0";
        }
        return result;
    }
}

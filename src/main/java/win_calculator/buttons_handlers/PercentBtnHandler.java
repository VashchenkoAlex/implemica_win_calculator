package win_calculator.buttons_handlers;

import win_calculator.extra_operations.Percent;

import java.math.BigDecimal;

public class PercentBtnHandler {

    public String doOperation(Percent percent, BigDecimal firstNumber, BigDecimal secondNumber){

        String result;
        if (firstNumber != null){
            result = percent.calculate(firstNumber,secondNumber).toString();
        }else {
            result = "0";
        }
        return result;
    }
}

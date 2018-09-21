package win_calculator.model.button_handlers;

import win_calculator.controller.nodes.digits.Number;
import win_calculator.model.nodes.actions.extra_operations.Percent;
import win_calculator.model.OperationProcessor;

import java.math.BigDecimal;

public class PercentHandler {

    private OperationProcessor operationProcessor;
    public PercentHandler(OperationProcessor operationProcessor){
        this.operationProcessor = operationProcessor;
    }
    public BigDecimal doOperation(Percent percent,Number number){

        BigDecimal[] numbers = operationProcessor.selectNumbersForPercent(number);
        BigDecimal result;
        if (numbers[0] != null){
            result = percent.calculate(numbers[0],numbers[1]);
        }else {
            result = BigDecimal.ZERO;
        }
        return result;
    }
}

package win_calculator.model;

import win_calculator.model.nodes.actions.Number;
import win_calculator.model.nodes.actions.extra_operations.Percent;

import java.math.BigDecimal;

class PercentHandler {

    private OperationProcessor operationProcessor;
    PercentHandler(OperationProcessor operationProcessor){
        this.operationProcessor = operationProcessor;
    }
    BigDecimal doOperation(Percent percent, Number number){

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

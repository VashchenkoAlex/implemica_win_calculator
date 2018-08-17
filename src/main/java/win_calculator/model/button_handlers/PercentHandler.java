package win_calculator.model.button_handlers;

import win_calculator.model.AppModel;
import win_calculator.model.nodes.actions.extra_operations.Percent;

import java.math.BigDecimal;

public class PercentHandler {

    private AppModel model;
    public PercentHandler(AppModel model){
        this.model = model;
    }
    public BigDecimal doOperation(Percent percent){

        BigDecimal firstNumber = model.getHistoryHandler().getPreviousNumber();
        BigDecimal secondNumber = model.getHistoryHandler().getLastAssembledNumber();
        BigDecimal result;
        if (firstNumber != null){
            result = percent.calculate(firstNumber,secondNumber);
        }else {
            result = BigDecimal.ZERO;
        }
        return result;
    }
}

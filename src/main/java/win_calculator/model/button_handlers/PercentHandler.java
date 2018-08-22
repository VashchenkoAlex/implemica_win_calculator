package win_calculator.model.button_handlers;

import win_calculator.model.nodes.actions.extra_operations.Percent;
import win_calculator.model.response_handlers.HistoryHandler;

import java.math.BigDecimal;

public class PercentHandler {

    private HistoryHandler historyHandler;
    public PercentHandler(HistoryHandler historyHandler){
        this.historyHandler = historyHandler;
    }
    public BigDecimal doOperation(Percent percent){

        BigDecimal firstNumber;
        if (historyHandler.getResultNumber()!=null){
            firstNumber = historyHandler.getResultNumber();
        }else {
            firstNumber = historyHandler.getPreviousNumber();
        }
        BigDecimal secondNumber = historyHandler.getLastNumber();
        BigDecimal result;
        if (firstNumber != null){
            result = percent.calculate(firstNumber,secondNumber);
        }else {
            result = BigDecimal.ZERO;
        }
        return result;
    }
}

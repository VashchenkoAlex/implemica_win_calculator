package win_calculator.model.button_handlers;

import win_calculator.controller.nodes.digits.Number;
import win_calculator.model.nodes.actions.extra_operations.Percent;
import win_calculator.model.response_handlers.HistoryHandler;

import java.math.BigDecimal;

import static win_calculator.utils.ActionType.NEGATE;
import static win_calculator.utils.ActionType.NUMBER;
import static win_calculator.utils.ActionType.PERCENT;

public class PercentHandler {

    private HistoryHandler historyHandler;
    public PercentHandler(HistoryHandler historyHandler){
        this.historyHandler = historyHandler;
    }
    public BigDecimal doOperation(Percent percent,Number number){

        BigDecimal firstNumber = BigDecimal.ZERO;
        BigDecimal secondNumber = BigDecimal.ZERO;
        if (number != null) {
            secondNumber = number.getBigDecimalValue();
            if (historyHandler.getResultNumber()!=null){
                firstNumber = historyHandler.getResultNumber();
            }else if (historyHandler.isMOperationBefore()&&!NUMBER.equals(historyHandler.getLastActionType())){
                firstNumber = historyHandler.getLastNumber();
            }else {
                firstNumber = historyHandler.getPreviousNumber();
            }
        }else if (historyHandler.getResultNumber()!=null) {
            firstNumber = historyHandler.getResultNumber();
            secondNumber = historyHandler.getResultNumber();
        }else if (NEGATE.equals(historyHandler.getLastActionType())){
            firstNumber = historyHandler.getPreviousNumber();
            secondNumber = historyHandler.getLastNumber();
        }else if (historyHandler.getLastNumber()!=null){
            firstNumber = historyHandler.getLastNumber();
            if (historyHandler.getPreviousNumber()!=null&&PERCENT.equals(historyHandler.getLastActionType())){
                secondNumber = historyHandler.getPreviousNumber();
            }else {
                secondNumber = historyHandler.getLastNumber();
            }
        }
        BigDecimal result;
        if (firstNumber != null){
            result = percent.calculate(firstNumber,secondNumber);
        }else {
            result = BigDecimal.ZERO;
        }
        return result;
    }
}

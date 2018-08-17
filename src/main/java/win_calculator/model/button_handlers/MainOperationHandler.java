package win_calculator.model.button_handlers;

import win_calculator.exceptions.MyException;
import win_calculator.model.nodes.History;
import win_calculator.model.nodes.actions.enter.Enter;
import win_calculator.model.nodes.actions.main_operations.MainOperation;
import win_calculator.model.response_handlers.HistoryHandler;

import java.math.BigDecimal;

import static win_calculator.utils.ActionType.MAIN_OPERATION;
import static win_calculator.utils.StringUtils.optimizeString;

public class MainOperationHandler {

    private HistoryHandler historyHandler;
    private BigDecimal firstNumber;
    private BigDecimal secondNumber;
    private BigDecimal resultNumber;
    private boolean operationRepeated;
    private boolean enterRepeated;
    private boolean mOperationBefore = false;
    private MainOperation lastOperation;

    public MainOperationHandler(HistoryHandler historyHandler) {
        this.historyHandler = historyHandler;
    }

    public void doOperation(MainOperation operation) throws MyException {

        initVariables();
        if (MAIN_OPERATION.equals(historyHandler.getLastActionType())){
            historyHandler.changeLastAction(operation);
        }else {
            doCalculation();
            if (mOperationBefore){
                historyHandler.setOperationRepeated(true);
            }
            historyHandler.setResultNumber(resultNumber);
            historyHandler.addActionToHistory(operation);
        }
        lastOperation = operation;
    }

    private void doCalculation() throws MyException {
        if (mOperationBefore||enterRepeated){
            if (isSecondNumberEmpty()){
                if (isResultEmpty()){
                    resultNumber = lastOperation.calculate(firstNumber);
                }else {
                    resultNumber = lastOperation.calculate(resultNumber,firstNumber);
                }
            }else {
                if (enterRepeated || !isResultEmpty()){
                    resultNumber = lastOperation.calculate(resultNumber,firstNumber);
                }else {
                    resultNumber = lastOperation.calculate(secondNumber,firstNumber);
                }
            }
        }
    }
    public void doEnter() throws MyException {

        initVariables();
        if (mOperationBefore||enterRepeated){
            if (operationRepeated){
                firstNumber = historyHandler.getLastAssembledNumber();
            }
            doCalculation();
            setEnterRepeated(true);
            setOperationRepeated(false);
            historyHandler.setMOperationBefore(false);
        }else {
            resultNumber = historyHandler.getLastAssembledNumber();
        }
        historyHandler.setResultNumber(resultNumber);
        historyHandler.setHistory(new History());
        secondNumber = null;
    }

    public void resetValues(){
        firstNumber = null;
        secondNumber = null;
        resultNumber = null;
        enterRepeated = false;
    }

    private boolean isResultEmpty(){

        return resultNumber==null;
    }

    private boolean isSecondNumberEmpty(){

        return secondNumber == null;
    }

    public void setEnterRepeated(boolean val){

        enterRepeated = val;
    }

    public void setOperationRepeated(boolean val){

        operationRepeated = val;
    }

    private void initVariables(){

        BigDecimal lastNumber = historyHandler.getLastAssembledNumber();
        if (lastNumber !=null){
            firstNumber = lastNumber;
        }
        BigDecimal previousNumber = historyHandler.getPreviousNumber();
        if (previousNumber!=null){
            secondNumber = previousNumber;
        }
        BigDecimal resultNumFromHistory = historyHandler.getResultNumber();
        if (resultNumFromHistory!=null){
            resultNumber = resultNumFromHistory;
        }
    }
}

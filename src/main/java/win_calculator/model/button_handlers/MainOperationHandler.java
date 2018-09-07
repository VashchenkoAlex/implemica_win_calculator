package win_calculator.model.button_handlers;

import win_calculator.exceptions.MyException;
import win_calculator.model.nodes.History;
import win_calculator.model.nodes.actions.main_operations.MainOperation;
import win_calculator.model.response_handlers.HistoryHandler;

import java.math.BigDecimal;

import static win_calculator.utils.ActionType.MAIN_OPERATION;
import static win_calculator.utils.StringUtils.isOverflow;

public class MainOperationHandler {

    private HistoryHandler historyHandler;
    private BigDecimal lastNumber;
    private BigDecimal previousNumber;
    private BigDecimal resultNumber;
    private boolean enterRepeated;
    private boolean mOperationBefore = false;
    private MainOperation lastOperation;
    private static final String OVERFLOW_MSG = "Overflow";

    public MainOperationHandler(HistoryHandler historyHandler) {
        this.historyHandler = historyHandler;
    }

    public BigDecimal doOperation(MainOperation operation) throws MyException {

        initVariables();
        setEnterRepeated(false);
        if (MAIN_OPERATION.equals(historyHandler.getLastActionType())){
            historyHandler.changeLastAction(operation);
            historyHandler.removeLastNumberAtHistoryIfExists();
        }else {
            doCalculation();
            historyHandler.setResultNumber(resultNumber);
            historyHandler.addActionToHistory(operation);
        }
        historyHandler.setMOperationBefore(true);
        lastOperation = operation;
        return resultNumber;
    }

    private void doCalculation() throws MyException {
        if (mOperationBefore||enterRepeated){
            if (isPreviousNumberEmpty()){
                if (isResultEmpty()){
                    resultNumber = lastOperation.calculate(lastNumber);
                }else {
                    resultNumber = lastOperation.calculate(resultNumber, lastNumber);
                }
            }else {
                if (enterRepeated || !isResultEmpty()){
                    resultNumber = lastOperation.calculate(resultNumber, lastNumber);
                }else {
                    resultNumber = lastOperation.calculate(previousNumber, lastNumber);
                }
            }
            if (isOverflow(resultNumber)){
                throw new MyException(OVERFLOW_MSG);
            }
        }
    }
    public BigDecimal doEnter() throws MyException {

        initVariables();
        if (mOperationBefore||enterRepeated){
            doCalculation();
            setEnterRepeated(true);
            historyHandler.setMOperationBefore(false);
        }else {
            resultNumber = historyHandler.getResultNumber();
        }
        historyHandler.setResultNumber(resultNumber);
        historyHandler.setHistory(new History());
        previousNumber = null;
        return resultNumber;
    }

    public void resetValues(){
        lastNumber = null;
        previousNumber = null;
        resultNumber = null;
        enterRepeated = false;
        mOperationBefore = false;
    }

    private boolean isResultEmpty(){

        return resultNumber==null;
    }

    private boolean isPreviousNumberEmpty(){

        return previousNumber == null;
    }

    private void setEnterRepeated(boolean val){

        enterRepeated = val;
    }

    private void initVariables(){

        BigDecimal lastNumber = historyHandler.getLastNumber();
        if (lastNumber !=null){
            this.lastNumber = lastNumber;
        }
        BigDecimal lastExtraResult = historyHandler.getLastExtraResult();
        if (lastExtraResult !=null){
            this.lastNumber = lastExtraResult;
        }
        BigDecimal previousNumber = historyHandler.getPreviousNumber();
        if (previousNumber!=null){
            this.previousNumber = previousNumber;
        }
        BigDecimal resultNumFromHistory = historyHandler.getResultNumber();
        if (resultNumFromHistory!=null){
            resultNumber = resultNumFromHistory;
        }
        mOperationBefore = historyHandler.isMOperationBefore();
    }
}

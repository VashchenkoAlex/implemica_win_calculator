package win_calculator.model;

import win_calculator.model.exceptions.MyException;
import win_calculator.model.nodes.History;
import win_calculator.model.nodes.actions.main_operations.MainOperation;

import java.math.BigDecimal;

import static win_calculator.model.nodes.actions.ActionType.MAIN_OPERATION;
import static win_calculator.model.utils.ModelUtils.checkOnOverflow;
import static win_calculator.model.utils.ModelUtils.roundNumber;

class MainOperationHandler {

    private OperationProcessor operationProcessor;
    private BigDecimal lastNumber;
    private BigDecimal previousNumber;
    private BigDecimal resultNumber;
    private boolean enterRepeated;
    private boolean mOperationBefore = false;
    private MainOperation lastOperation;

    MainOperationHandler(OperationProcessor operationProcessor) {
        this.operationProcessor = operationProcessor;
    }

    BigDecimal doOperation(MainOperation operation) throws MyException {

        initVariables();
        setEnterRepeated(false);
        if (MAIN_OPERATION.equals(operationProcessor.getLastActionType())){
            operationProcessor.changeLastAction(operation);
        }else {
            operationProcessor.addActionToHistory(operation);
            doCalculation();
        }
        operationProcessor.setMOperationBefore(true);
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
            resultNumber = roundNumber(resultNumber);
            checkOnOverflow(resultNumber);
            operationProcessor.setResultNumber(resultNumber);
        }else {
            resultNumber = null;
        }
    }
    BigDecimal doEnter() throws MyException {

        initVariables();
        if (mOperationBefore||enterRepeated){
            doCalculation();
            setEnterRepeated(true);
            operationProcessor.setMOperationBefore(false);
        }else {
            resultNumber = operationProcessor.getResultNumber();
        }
        operationProcessor.setResultNumber(resultNumber);
        operationProcessor.setHistory(new History());
        previousNumber = null;
        return resultNumber;
    }

    void resetValues(){
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

        BigDecimal lastNumber = operationProcessor.getLastNumber();
        if (lastNumber !=null){
            this.lastNumber = lastNumber;
        }
        BigDecimal lastExtraResult = operationProcessor.getLastExtraResult();
        if (lastExtraResult !=null){
            this.lastNumber = lastExtraResult;
        }
        BigDecimal previousNumber = operationProcessor.getPreviousNumber();
        if (previousNumber!=null){
            this.previousNumber = previousNumber;
        }
        BigDecimal resultNumFromHistory = operationProcessor.getResultNumber();
        if (resultNumFromHistory!=null){
            resultNumber = resultNumFromHistory;
        }
        mOperationBefore = operationProcessor.isMOperationBefore();
    }
}

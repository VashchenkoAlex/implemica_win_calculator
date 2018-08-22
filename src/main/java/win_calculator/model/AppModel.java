package win_calculator.model;

import win_calculator.DTOs.ResponseDTO;
import win_calculator.exceptions.MyException;
import win_calculator.model.nodes.actions.Action;
import win_calculator.model.nodes.actions.digits.Number;
import win_calculator.model.nodes.actions.digits.ZeroDigit;
import win_calculator.model.nodes.actions.extra_operations.ExtraOperation;
import win_calculator.model.nodes.actions.extra_operations.Percent;
import win_calculator.model.nodes.actions.main_operations.MainOperation;
import win_calculator.model.response_handlers.HistoryHandler;
import win_calculator.model.button_handlers.ExtraOperationHandler;
import win_calculator.model.button_handlers.MainOperationHandler;
import win_calculator.model.button_handlers.PercentHandler;

import java.math.BigDecimal;

import static win_calculator.utils.ActionType.MAIN_OPERATION;
import static win_calculator.utils.StringUtils.optimizeString;
import static win_calculator.utils.StringUtils.removeCapacity;
import static win_calculator.utils.StringUtils.replaceComaToDot;

public class AppModel {

    private HistoryHandler historyHandler = new HistoryHandler();
    private MainOperationHandler mOperationHandler = new MainOperationHandler(historyHandler);
    private ExtraOperationHandler eOperationHandler = new ExtraOperationHandler();
    private PercentHandler percentHandler = new PercentHandler(historyHandler);
    private String responseNumber;
    private Number lastNumber;

    public ResponseDTO toDo(Action action,Number number) throws MyException {

        switch (action.getType()){
            case MAIN_OPERATION:{
                processMainOperation(action,number);
                break;
            }
            case ENTER:{
                processEnter(number);
                break;
            }
            case EXTRA_OPERATION:{
                processExtraOperation(action, number);
                break;
            }
            case PERCENT:{
                processPercent(action,number);
                break;
            }
            case CLEAR:{
                processClear();
                break;
            }
        }
        return new ResponseDTO(responseNumber,historyHandler.getHistoryString());
    }

    private void processClear(){

        lastNumber = null;
        responseNumber = null;
        historyHandler.clearHistory();
        mOperationHandler.resetValues();
    }

    private void processExtraOperation(Action eOperation,Number number) throws MyException {

        lastNumber = number;
        BigDecimal operationNumber;
        if (lastNumber!=null){
            historyHandler.addActionToHistory(lastNumber);
            operationNumber = new BigDecimal(lastNumber.getValue());
        }else {
            operationNumber = new BigDecimal(replaceComaToDot(responseNumber));
            historyHandler.setLastNumber(operationNumber);
            if (MAIN_OPERATION.equals(historyHandler.getLastActionType())){
                historyHandler.addActionToHistory(new Number(operationNumber));
            }
        }
        BigDecimal resultNum = eOperationHandler.doOperation(operationNumber,(ExtraOperation) eOperation);
        historyHandler.setResultNumber(resultNum);
        responseNumber = resultNum.toString();
        historyHandler.addActionToHistory(eOperation);
    }

    private void processPercent(Action percent, Number number){

        if (number!=null){
            historyHandler.setLastNumber(number.getBigDecimalValue());
        }else if(responseNumber!=null){
            historyHandler.setLastNumber(new BigDecimal(replaceComaToDot(removeCapacity(responseNumber))));
        }
        BigDecimal result = percentHandler.doOperation((Percent) percent);
        responseNumber = result.toString();
        if (!"0".equals(responseNumber)){
            BigDecimal tempNumber = new BigDecimal(historyHandler.getPreviousNumber().toString());
            if (historyHandler.isPercentLast()){
                historyHandler.changeLastActionNumber(new Number(result));
            }else {
                historyHandler.addActionToHistory(new Number(result));
            }
            historyHandler.changeLastNumber(result);
            historyHandler.changePreviousNumber(tempNumber);
        }else if (!"0".equals(historyHandler.getHistoryString())){
            historyHandler.addActionToHistory(new ZeroDigit());
        }
        historyHandler.addActionToHistory(percent);
    }

    private void processEnter(Number number) throws MyException {

        if (number !=null){
            historyHandler.addActionToHistory(number);
        }else {
            BigDecimal resultNumber = historyHandler.getResultNumber();
            if (!historyHandler.isEnterRepeated() && resultNumber!=null && MAIN_OPERATION.equals(historyHandler.getLastActionType())){
                historyHandler.setLastNumber(resultNumber);
            }
        }
        BigDecimal operationResult = mOperationHandler.doEnter();
        if (operationResult!=null){
            responseNumber = optimizeString(operationResult.toString());
        }else {
            responseNumber = lastNumber.getValue();
        }
        historyHandler.setEnterRepeated(true);
    }

    private void processMainOperation(Action mOperation,Number number) throws MyException {

        if (number !=null ){
            historyHandler.addActionToHistory(number);
        }else {
            historyHandler.changeLastNumber(new BigDecimal(responseNumber));
        }
        BigDecimal operationResult = mOperationHandler.doOperation((MainOperation) mOperation);
        if (operationResult!=null){
            responseNumber = optimizeString(operationResult.toString());
        }else if (number!=null && (responseNumber==null||"".equals(responseNumber))){
            responseNumber = number.getValue();
        }
    }
}

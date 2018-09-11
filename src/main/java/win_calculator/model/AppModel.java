package win_calculator.model;

import win_calculator.DTOs.ResponseDTO;
import win_calculator.controller.view_handlers.MemoryHandler;
import win_calculator.exceptions.MyException;
import win_calculator.model.nodes.actions.Action;
import win_calculator.controller.nodes.digits.Number;
import win_calculator.model.nodes.actions.clear.Clear;
import win_calculator.model.nodes.actions.extra_operations.ExtraOperation;
import win_calculator.model.nodes.actions.extra_operations.Negate;
import win_calculator.model.nodes.actions.extra_operations.Percent;
import win_calculator.model.nodes.actions.main_operations.MainOperation;
import win_calculator.model.nodes.actions.memory.MemoryAction;
import win_calculator.model.response_handlers.HistoryHandler;
import win_calculator.model.button_handlers.MainOperationHandler;
import win_calculator.model.button_handlers.PercentHandler;

import java.math.BigDecimal;

import static win_calculator.utils.ActionType.*;
import static win_calculator.utils.AppUtils.checkOnOverflow;

public class AppModel {

    private HistoryHandler historyHandler = new HistoryHandler();
    private MainOperationHandler mOperationHandler = new MainOperationHandler(historyHandler);
    private PercentHandler percentHandler = new PercentHandler(historyHandler);
    private MemoryHandler memoryHandler = new MemoryHandler();
    private BigDecimal responseNumber;
    private Number lastNumber;

    public ResponseDTO toDo(Action action, Number number) throws MyException {

        switch (action.getType()) {
            case MAIN_OPERATION: {
                processMainOperation(action, number);
                break;
            }
            case ENTER: {
                processEnter(number);
                break;
            }
            case EXTRA_OPERATION: {
                processExtraOperation(action, number);
                break;
            }
            case NEGATE: {
                processNegate(action, number);
                break;
            }
            case PERCENT: {
                processPercent(action, number);
                break;
            }
            case CLEAR: {
                processClear();
                break;
            }
            case MEMORY: {
                processMemoryOperation(action, number);
                break;
            }
            case CLEAR_EXTRA: {
                historyHandler.rejectLastNumberWithExtraOperations();
            }
        }
        return new ResponseDTO(responseNumber, historyHandler.getHistoryString());
    }

    private void processClear() {

        lastNumber = null;
        responseNumber = null;
        historyHandler.clearHistory();
        mOperationHandler.resetValues();
    }

    private void processExtraOperation(Action eOperation, Number number) throws MyException {

        BigDecimal operationNumber;
        if (number != null) {
            historyHandler.addActionToHistory(number);
            operationNumber = new BigDecimal(number.getValue());
        } else if (responseNumber != null) {
            operationNumber = responseNumber;
            if (MAIN_OPERATION.equals(historyHandler.getLastActionType())||historyHandler.isEnterRepeated()) {
                historyHandler.addActionToHistory(new Number(operationNumber));
            }
        } else {
            operationNumber = BigDecimal.ZERO;
            historyHandler.addActionToHistory(new Number(operationNumber));
        }
        BigDecimal resultNum = checkOnOverflow(((ExtraOperation) eOperation).calculate(operationNumber));
        historyHandler.setLastExtraResult(resultNum);
        responseNumber = resultNum;
        historyHandler.addActionToHistory(eOperation);
    }

    private void processPercent(Action percent, Number number) throws MyException {

        BigDecimal result = checkOnOverflow(percentHandler.doOperation((Percent) percent, number));
        responseNumber = result;
        if (!BigDecimal.ZERO.equals(responseNumber) && !"0.00".equals(result.toString())) {
            BigDecimal tempNumber;
            if (historyHandler.getPreviousNumber()!=null){
                tempNumber = historyHandler.getPreviousNumber();
            }else {
                tempNumber = historyHandler.getLastNumber();
            }
            historyHandler.changeLastActionNumber(new Number(result));
            historyHandler.setEnterRepeated(false);
            historyHandler.changeLastNumberAtActions(result);
            historyHandler.changePreviousNumber(tempNumber);
            historyHandler.addActionToHistory(percent);
        } else if (!"0".equals(historyHandler.getHistoryString())) {
            if (MAIN_OPERATION.equals(historyHandler.getLastActionType()) || EXTRA_OPERATION.equals(historyHandler.getLastActionType())) {
                historyHandler.addActionToHistory(new Number(BigDecimal.ZERO));
            } else {
                historyHandler.addZeroToHistory();
            }
        }
    }

    private void processEnter(Number number) throws MyException {

        if (number != null) {
            historyHandler.addActionToHistory(number);
            responseNumber = number.getBigDecimalValue();
        } else {
            BigDecimal resultNumber = historyHandler.getResultNumber();
            if (!historyHandler.isEnterRepeated() && resultNumber != null && MAIN_OPERATION.equals(historyHandler.getLastActionType())) {
                historyHandler.setLastNumber(resultNumber);
            }
        }
        BigDecimal operationResult = mOperationHandler.doEnter();
        if (operationResult != null) {
            responseNumber = operationResult;
        } else if (lastNumber != null) {
            responseNumber = lastNumber.getBigDecimalValue();
        }
        historyHandler.setEnterRepeated(true);
        historyHandler.resetLastExtraResult();
        historyHandler.setLastAction(new Clear());
    }

    private void processMainOperation(Action mOperation, Number number) throws MyException {

        BigDecimal lastExtraResult = historyHandler.getLastExtraResult();
        if (number != null) {
            historyHandler.addActionToHistory(number);
            responseNumber = number.getBigDecimalValue();
            if (historyHandler.isEnterRepeated()) {
                historyHandler.resetResultNumber();
            }
            if (!historyHandler.isMOperationBefore()) {
                historyHandler.resetPreviousNumber();
            }
        } else if (lastExtraResult != null) {
            historyHandler.changeLastNumberAtActions(lastExtraResult);
        } else if (responseNumber != null) {
            historyHandler.changeLastNumberAtActions(responseNumber);
        } else {
            historyHandler.addZeroToHistory();
        }
        BigDecimal operationResult = mOperationHandler.doOperation((MainOperation) mOperation);
        if (operationResult != null) {
            responseNumber = operationResult;
        }
        historyHandler.resetLastExtraResult();
    }

    private void processMemoryOperation(Action action, Number number) {

        responseNumber = memoryHandler.doAction((MemoryAction) action, number).getBigDecimalValue();
    }

    private void processNegate(Action negate, Number number) {

        if (number == null && historyHandler.lastActionNotNumber()) {
            if (responseNumber != null) {
                if (MAIN_OPERATION.equals(historyHandler.getLastActionType()) || CLEAR.equals(historyHandler.getLastActionType())) {
                    historyHandler.addActionToHistory(new Number(responseNumber));
                }
            } else {
                responseNumber = BigDecimal.ZERO;
                historyHandler.addActionToHistory(new Number(responseNumber));
            }
            responseNumber = ((Negate) negate).calculate(responseNumber);
            historyHandler.changeLastNumber(responseNumber);
            historyHandler.addActionToHistory(negate);
        } else if (number != null) {
            responseNumber = ((Negate) negate).calculate(number.getBigDecimalValue());
            historyHandler.setLastNumber(responseNumber);
            historyHandler.setLastAction(negate);
        } else{
            responseNumber = ((Negate) negate).calculate(historyHandler.getLastNumber());
            historyHandler.changeLastNumber(responseNumber);
            if (historyHandler.isHistoryContainNegate()){
                historyHandler.addActionToHistory(negate);
            }
        }
    }
}

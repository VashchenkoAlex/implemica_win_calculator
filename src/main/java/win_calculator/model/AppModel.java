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

import static win_calculator.utils.ActionType.CLEAR;
import static win_calculator.utils.ActionType.MAIN_OPERATION;

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
            if (MAIN_OPERATION.equals(historyHandler.getLastActionType())) {
                historyHandler.addActionToHistory(new Number(operationNumber));
            }
        } else {
            operationNumber = BigDecimal.ZERO;
            historyHandler.addActionToHistory(new Number(operationNumber));
        }
        BigDecimal resultNum = ((ExtraOperation) eOperation).calculate(operationNumber);
        historyHandler.setLastExtraResult(resultNum);
        responseNumber = resultNum;
        historyHandler.addActionToHistory(eOperation);
    }

    private void processPercent(Action percent, Number number) {

        if (number != null) {
            historyHandler.setLastNumber(number.getBigDecimalValue());
        } else if (responseNumber != null) {
            historyHandler.setLastNumber(responseNumber);
        }
        BigDecimal result = percentHandler.doOperation((Percent) percent);
        responseNumber = result;
        if (!BigDecimal.ZERO.equals(responseNumber)) {
            BigDecimal tempNumber = new BigDecimal(historyHandler.getPreviousNumber().toString());
            if (historyHandler.isPercentLast()) {
                historyHandler.changeLastActionNumber(new Number(result));
            } else {
                historyHandler.addActionToHistory(new Number(result));
            }
            historyHandler.setEnterRepeated(false);
            historyHandler.changeLastNumber(result);
            historyHandler.changePreviousNumber(tempNumber);
        } else if (!"0".equals(historyHandler.getHistoryString())) {
            historyHandler.addActionToHistory(new Number(BigDecimal.ZERO));
        }
        historyHandler.addActionToHistory(percent);
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
//        historyHandler.setMOperationBefore(false);
        historyHandler.resetLastExtraResult();
        historyHandler.setLastAction(new Clear());
    }

    private void processMainOperation(Action mOperation, Number number) throws MyException {

        BigDecimal lastExtraResult = historyHandler.getLastExtraResult();
        if (number != null) {
            historyHandler.addActionToHistory(number);
        } else if (lastExtraResult != null) {
            historyHandler.changeLastNumber(lastExtraResult);
        } else if (responseNumber != null) {
            historyHandler.changeLastNumber(responseNumber);
        } else {
            historyHandler.addZeroToHistory();
//            historyHandler.addActionToHistory(new Number(BigDecimal.ZERO));
        }
        BigDecimal operationResult = mOperationHandler.doOperation((MainOperation) mOperation);
        if (operationResult != null) {
            responseNumber = operationResult;
        } else if (number != null && (responseNumber == null)) {
            responseNumber = number.getBigDecimalValue();
        }
        historyHandler.resetLastExtraResult();
    }

    private void processMemoryOperation(Action action, Number number) {

        responseNumber = memoryHandler.doAction((MemoryAction) action, number).getBigDecimalValue();
    }

    private void processNegate(Action negate, Number number) {

        if (number == null && historyHandler.lastActionNotNumber()) {
            if (responseNumber != null) {
                if (MAIN_OPERATION.equals(historyHandler.getLastActionType())||CLEAR.equals(historyHandler.getLastActionType())) {
                    historyHandler.addActionToHistory(new Number(responseNumber));
                }
            } else {
                responseNumber = BigDecimal.ZERO;
                historyHandler.addActionToHistory(new Number(responseNumber));
            }
            responseNumber = ((Negate) negate).calculate(responseNumber);
            historyHandler.addActionToHistory(negate);

        } else if (number!=null){
            responseNumber = ((Negate) negate).calculate(number.getBigDecimalValue());
            historyHandler.setLastNumber(responseNumber);
            historyHandler.setLastAction(negate);
        }else if (historyHandler.isHistoryNotEmpty()){
            responseNumber = ((Negate) negate).calculate(historyHandler.getLastNumber());
            historyHandler.changeLastActionNumber(new Number(responseNumber));
        }

    }
}

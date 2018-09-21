package win_calculator.model;

import win_calculator.DTOs.ResponseDTO;
import win_calculator.exceptions.MyException;
import win_calculator.model.nodes.actions.Action;
import win_calculator.controller.nodes.digits.Number;
import win_calculator.model.nodes.actions.clear.Clear;
import win_calculator.model.nodes.actions.extra_operations.ExtraOperation;
import win_calculator.model.nodes.actions.extra_operations.Negate;
import win_calculator.model.nodes.actions.extra_operations.Percent;
import win_calculator.model.nodes.actions.main_operations.MainOperation;
import win_calculator.model.button_handlers.MainOperationHandler;
import win_calculator.model.button_handlers.PercentHandler;
import win_calculator.utils.ActionType;

import java.math.BigDecimal;

import static win_calculator.utils.ActionType.*;
import static win_calculator.utils.AppUtils.checkOnOverflow;
import static win_calculator.utils.AppUtils.convertToString;

public class AppModel {

    private static final String DISPLAY_PATTERN = "#############,###.################";

    private OperationProcessor operationProcessor = new OperationProcessor();
    private MainOperationHandler mOperationHandler = new MainOperationHandler(operationProcessor);
    private PercentHandler percentHandler = new PercentHandler(operationProcessor);
    private BigDecimal responseNumber;
    private Number lastNumber;

    public ResponseDTO toDo(Action action, Number number){

        ResponseDTO responseDTO;
        try{
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
                case CLEAR_ENTERED: {
                    operationProcessor.rejectLastNumberWithExtraOperations();
                    responseNumber = null;
                    break;
                }
                case CLEAR_EXTRA: {
                    operationProcessor.rejectLastNumberWithExtraOperations();
                    break;
                }
            }
            responseDTO = new ResponseDTO(convertToString(responseNumber,DISPLAY_PATTERN), operationProcessor.getHistoryString());
        }catch (MyException e){
            responseDTO = new ResponseDTO(e.getMessage(),operationProcessor.getHistoryString());
        }
        return responseDTO;
    }

    private void processClear() {

        lastNumber = null;
        responseNumber = null;
        operationProcessor.clearHistory();
        mOperationHandler.resetValues();
    }

    private void processExtraOperation(Action eOperation, Number number) throws MyException {

        BigDecimal operationNumber;
        if (number != null) {
            operationProcessor.addNumberToHistory(number);
            operationNumber = new BigDecimal(number.getValue());
        } else if (responseNumber != null) {
            operationNumber = responseNumber;
            ActionType type = operationProcessor.getLastActionType();
            if (MAIN_OPERATION.equals(type) || (operationProcessor.isEnterRepeated()
                    && !EXTRA_OPERATION.equals(type) && !NEGATE.equals(type))) {
                operationProcessor.addNumberToHistory(new Number(operationNumber));
            }
        } else {
            operationNumber = BigDecimal.ZERO;
            operationProcessor.addZeroToHistory();
        }
        operationProcessor.addActionToHistory(eOperation);
        BigDecimal resultNum = checkOnOverflow(((ExtraOperation) eOperation).calculate(operationNumber));
        operationProcessor.setLastExtraResult(resultNum);
        responseNumber = resultNum;

    }

    private void processPercent(Action percent, Number number) throws MyException {

        BigDecimal result = checkOnOverflow(percentHandler.doOperation((Percent) percent, number));
        responseNumber = result;
        if (!BigDecimal.ZERO.equals(responseNumber) && !"0.00".equals(result.toString())) {
            BigDecimal tempNumber;
            if (operationProcessor.getPreviousNumber() != null) {
                tempNumber = operationProcessor.getPreviousNumber();
            } else {
                tempNumber = operationProcessor.getLastNumber();
            }
            operationProcessor.rejectLastNumberWithExtraOperations();
            operationProcessor.changeLastActionNumber(new Number(result));
            operationProcessor.setEnterRepeated(false);
            operationProcessor.changeLastNumberAtActions(result);
            operationProcessor.changePreviousNumber(tempNumber);
            operationProcessor.addActionToHistory(percent);
        } else if (!"0".equals(operationProcessor.getHistoryString())) {
            if (MAIN_OPERATION.equals(operationProcessor.getLastActionType()) || EXTRA_OPERATION.equals(operationProcessor.getLastActionType())) {
                operationProcessor.addNumberToHistory(new Number(BigDecimal.ZERO));
            } else {
                operationProcessor.addZeroToHistory();
            }
        }
    }

    private void processEnter(Number number) throws MyException {

        if (number != null) {
            operationProcessor.resetResultNumber();
            operationProcessor.setLastNumber(number.getBigDecimalValue());
            responseNumber = number.getBigDecimalValue();
        } else {
            BigDecimal resultNumber = operationProcessor.getResultNumber();
            ActionType lastActionType = operationProcessor.getLastActionType();
            if (!operationProcessor.isEnterRepeated() && resultNumber != null && (MAIN_OPERATION.equals(lastActionType)||MEMORY.equals(lastActionType))) {
                operationProcessor.setLastNumber(resultNumber);
            }
        }
        BigDecimal operationResult = mOperationHandler.doEnter();
        if (operationResult != null) {
            responseNumber = operationResult;
        } else if (lastNumber != null) {
            responseNumber = lastNumber.getBigDecimalValue();
        }
        operationProcessor.setEnterRepeated(true);
        operationProcessor.resetLastExtraResult();
        operationProcessor.setLastAction(new Clear());
    }

    private void processMainOperation(Action mOperation, Number number) throws MyException {

        if (number != null) {
            operationProcessor.addNumberToHistory(number);
            responseNumber = number.getBigDecimalValue();
            if (operationProcessor.isEnterRepeated()) {
                operationProcessor.resetResultNumber();
            }
            if (!operationProcessor.isMOperationBefore()) {
                operationProcessor.resetPreviousNumber();
            }
        } else if (responseNumber != null) {
            if (!operationProcessor.hasExtraOperations()) {
                operationProcessor.changeLastNumberAtActions(responseNumber);
            }
        } else {
            operationProcessor.addZeroToHistory();
        }
        BigDecimal operationResult = mOperationHandler.doOperation((MainOperation) mOperation);
        if (operationResult != null) {
            responseNumber = operationResult;
        }
        operationProcessor.setEnterRepeated(false);
        operationProcessor.resetLastExtraResult();
    }

    private void processNegate(Action negate, Number number) {

        if (number == null && operationProcessor.lastActionNotNumber()) {
            if (responseNumber != null) {
                if (MAIN_OPERATION.equals(operationProcessor.getLastActionType()) || CLEAR.equals(operationProcessor.getLastActionType())) {
                    operationProcessor.addNumberToHistory(new Number(responseNumber));
                }
            } else {
                responseNumber = BigDecimal.ZERO;
                operationProcessor.addNumberToHistory(new Number(responseNumber));
            }
            responseNumber = ((Negate) negate).calculate(responseNumber);
            operationProcessor.changeLastNumber(responseNumber);
            operationProcessor.addActionToHistory(negate);
        } else {
            responseNumber = ((Negate) negate).calculate(operationProcessor.getLastNumber());
            operationProcessor.changeLastNumber(responseNumber);
            if (operationProcessor.isHistoryContainNegate()) {
                operationProcessor.addActionToHistory(negate);
            }
        }
    }
}

package win_calculator.model;

import win_calculator.model.DTOs.ResponseDTO;
import win_calculator.model.exceptions.MyException;
import win_calculator.model.nodes.actions.Action;
import win_calculator.model.nodes.actions.Number;
import win_calculator.model.nodes.actions.clear.Clear;
import win_calculator.model.nodes.actions.extra_operations.Percent;
import win_calculator.model.nodes.actions.main_operations.MainOperation;
import win_calculator.model.nodes.actions.ActionType;

import java.math.BigDecimal;

import static win_calculator.model.nodes.actions.ActionType.*;
import static win_calculator.model.utils.ModelUtils.checkOnOverflow;
import static win_calculator.model.utils.ModelUtils.roundNumber;

public class AppModel {

    private OperationProcessor operationProcessor = new OperationProcessor();
    private MainOperationHandler mOperationHandler = new MainOperationHandler(operationProcessor);
    private PercentHandler percentHandler = new PercentHandler(operationProcessor);
    private BigDecimal responseNumber;

    public ResponseDTO toDo(Number number, Action action) {

        ResponseDTO responseDTO;
        try {
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
                    responseNumber = operationProcessor.processExtraOperation(action,number,responseNumber);
                    break;
                }
                case NEGATE: {
                    responseNumber = operationProcessor.processNegate(action,number,responseNumber);
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
            responseDTO = new ResponseDTO(responseNumber, operationProcessor.getHistory());
        } catch (MyException e) {
            responseDTO = new ResponseDTO(e.getMessage(), operationProcessor.getHistory());
        }
        return responseDTO;
    }

    private void processClear() {

        responseNumber = null;
        operationProcessor.clearHistory();
        mOperationHandler.resetValues();
    }

    private void processPercent(Action percent, Number number) throws MyException {

        responseNumber = roundNumber(percentHandler.doOperation((Percent) percent, number));
        checkOnOverflow(responseNumber);
        if (!BigDecimal.ZERO.equals(responseNumber) && !"0.00".equals(responseNumber.toString())) {
            BigDecimal tempNumber;
            if (operationProcessor.getPreviousNumber() != null) {
                tempNumber = operationProcessor.getPreviousNumber();
            } else {
                tempNumber = operationProcessor.getLastNumber();
            }
            operationProcessor.rejectLastNumberWithExtraOperations();
            operationProcessor.changeLastActionNumber(responseNumber);
            operationProcessor.setEnterRepeated(false);
            operationProcessor.changePreviousNumber(tempNumber);
            operationProcessor.addActionToHistory(percent);
        } else if (MAIN_OPERATION.equals(operationProcessor.getLastActionType()) || EXTRA_OPERATION.equals(operationProcessor.getLastActionType())) {
            operationProcessor.addNumberToHistory(new Number(BigDecimal.ZERO));
        } else {
            operationProcessor.addZeroToHistory();
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
            if (!operationProcessor.isEnterRepeated() && resultNumber != null && (MAIN_OPERATION.equals(lastActionType) || MEMORY.equals(lastActionType))) {
                operationProcessor.setLastNumber(resultNumber);
            }
        }
        BigDecimal operationResult = mOperationHandler.doEnter();
        if (operationResult != null) {
            responseNumber = operationResult;
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

}

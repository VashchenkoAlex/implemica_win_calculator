package win_calculator.model;

import win_calculator.model.exceptions.*;
import win_calculator.model.memory.MemoryOperation;
import win_calculator.model.operations.Operation;
import win_calculator.model.operations.OperationType;

import java.math.BigDecimal;
import java.util.LinkedList;

import static win_calculator.model.operations.OperationType.*;
import static win_calculator.model.utils.ModelUtils.checkOnOverflow;
import static win_calculator.model.utils.ModelUtils.roundNumber;

public class CalcModel {

    private OperationProcessor operationProcessor = new OperationProcessor();
    private BigDecimal responseNumber;
    private BigDecimal lastInputtedNumber;

    public void toDo(BigDecimal number) {

        lastInputtedNumber = number;
    }

    public BigDecimal toDo(Operation operation) throws OperationException {

        OperationType type = operation.getType();
        if (MAIN_OPERATION.equals(type)) {
            responseNumber = operationProcessor.processMainOperation(operation, lastInputtedNumber, responseNumber);
        } else if (EQUAL.equals(type)) {
            responseNumber = operationProcessor.processEnter(lastInputtedNumber, responseNumber);
        } else if (EXTRA_OPERATION.equals(type)) {
            responseNumber = operationProcessor.processExtraOperation(operation, lastInputtedNumber, responseNumber);
        } else if (NEGATE.equals(type)) {
            responseNumber = operationProcessor.processNegate(operation, lastInputtedNumber, responseNumber);
        } else if (PERCENT.equals(type)) {
            responseNumber = operationProcessor.processPercent(operation, lastInputtedNumber);
        } else if (CLEAR.equals(type)) {
            responseNumber = operationProcessor.processClear();
        } else if (CLEAR_ENTERED.equals(type)) {
            operationProcessor.processClearEntered();
            responseNumber = null;
        } else if (MEMORY.equals(type)) {
            BigDecimal result = operationProcessor.processMemory((MemoryOperation) operation, lastInputtedNumber);
            if (result != null) {
                responseNumber = result;
            }
        }
        lastInputtedNumber = null;
        checkOnOverflow(responseNumber);
        return roundNumber(responseNumber);
    }

    public LinkedList<Operation> getHistory() {

        return operationProcessor.getHistory();
    }

    public void clearLastExtra() {

        operationProcessor.rejectLastNumberWithExtraOperations();
    }
}

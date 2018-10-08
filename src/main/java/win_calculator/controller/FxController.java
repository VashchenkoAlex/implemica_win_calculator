package win_calculator.controller;

import win_calculator.controller.entities.Digit;
import win_calculator.model.CalcModel;
import win_calculator.model.exceptions.ExceptionType;
import win_calculator.model.exceptions.OperationException;
import win_calculator.model.memory.MemoryOperation;
import win_calculator.model.memory.MemoryType;
import win_calculator.model.operations.Operation;
import win_calculator.model.operations.OperationType;

import java.math.BigDecimal;

import static win_calculator.controller.utils.CalculatorUtils.convertToString;
import static win_calculator.controller.utils.CalculatorUtils.getHistoryString;
import static win_calculator.controller.utils.CalculatorUtils.isNotNumber;
import static win_calculator.model.memory.MemoryType.STORE;
import static win_calculator.model.operations.OperationType.*;
import static win_calculator.model.operations.OperationType.EQUAL;
import static win_calculator.model.operations.OperationType.EXTRA_OPERATION;

public class FxController {

    private static final String NEGATIVE_VALUE_FOR_SQRT_MSG = "Invalid input";
    private static final String OVERFLOW_MSG = "Overflow";
    private static final String DIVIDE_BY_ZERO_MSG = "Cannot divide by zero";
    private static final String ZERO_DIVIDE_BY_ZERO_MSG = "Result is undefined";
    private static final String ZERO_STR = "0";
    private static final String DISPLAY_PATTERN = "#############,###.################";


    private CalcModel model = new CalcModel();
    private NumberBuilder numberBuilder = new NumberBuilder();

    private String lastDisplay;
    private OperationType lastOperationType;
    private String lastHistoryText;

    private boolean wasException = false;

    public String[] handleOperation(Operation operation) {

        OperationType type = operation.getType();
        try {
            if (CLEAR_ENTERED.equals(type)) {
                handleClearEntered(operation);
            } else if (BACKSPACE.equals(type)) {
                handleBackSpace();
            } else if (NEGATE.equals(type) && numberBuilder.containsNumber()) {
                handleNegate();
            } else {
                lastDisplay = convertToString(doOperationWithModel(operation), DISPLAY_PATTERN);
            }
        } catch (OperationException e) {
            lastDisplay = selectMessageAfterException(e.getType());
            wasException = true;
        }

        setLastHistoryText(type);
        setLastOperationType(type);

        return new String[]{lastDisplay, lastHistoryText};
    }

    public String[] handleDigit(Digit digit){

        if (lastDisplay != null && isNotNumber(lastDisplay)) {
            lastHistoryText = "";
            numberBuilder.clear();
        }
        if (EXTRA_OPERATION.equals(lastOperationType)) {
            model.clearLastExtra();
            lastHistoryText = getHistoryString(model.getHistory());
        }
        lastDisplay = numberBuilder.addDigit(digit);
        setLastHistoryText(DIGIT);
        setLastOperationType(DIGIT);
        return new String[]{lastDisplay,lastHistoryText};
    }

    public OperationType getLastOperationType() {
        return lastOperationType;
    }

    private void handleClearEntered(Operation operation) throws OperationException {

        lastDisplay = ZERO_STR;
        numberBuilder.clear();
        model.toDo(operation);
    }

    private void handleBackSpace(){

        if (isBackSpacePossible()) {
            lastDisplay = numberBuilder.doBackSpace();
        } else {
            if (lastDisplay == null || isNotNumber(lastDisplay)) {
                lastDisplay = ZERO_STR;
            }
        }
    }

    private boolean isBackSpacePossible() {

        return !MAIN_OPERATION.equals(lastOperationType)
                && !EXTRA_OPERATION.equals(lastOperationType)
                && !EQUAL.equals(lastOperationType)
                && numberBuilder.containsNumber();
    }

    private void handleNegate() {

        lastDisplay =  numberBuilder.negate(MEMORY.equals(lastOperationType));
    }

    private BigDecimal doOperationWithModel(Operation operation) throws OperationException {

        BigDecimal currentNum = numberBuilder.finish();
        model.toDo(currentNum);
        BigDecimal result = model.toDo(operation);
        if (MEMORY.equals(operation.getType())) {
            MemoryType memoryType = ((MemoryOperation) operation).getMemoryType();
            if (!STORE.equals(memoryType)){
                numberBuilder.clear();
                numberBuilder.setNumber(result);
            } else if (currentNum != null){
                numberBuilder.setNumber(currentNum);
                result = currentNum;
            }
        } else if (!NEGATE.equals(operation.getType()) && !MEMORY.equals(operation.getType())) {
            numberBuilder.clear();
        }
        if (EQUAL.equals(operation.getType())) {
            lastHistoryText = "";
        }
        return result;
    }

    private String selectMessageAfterException(ExceptionType type){

        String message = "";
        switch (type){
            case OVERFLOW: {
                message = OVERFLOW_MSG;
                break;
            }
            case DIVIDE_BY_ZERO: {
                message = DIVIDE_BY_ZERO_MSG;
                break;
            }
            case ZERO_DIVEDE_BY_ZERO: {
                message = ZERO_DIVIDE_BY_ZERO_MSG;
                break;
            }
            case NEGATIVE_VALUE_FOR_SQRT: {
                message = NEGATIVE_VALUE_FOR_SQRT_MSG;
                break;
            }
        }
        return message;
    }

    private void setLastHistoryText(OperationType type){

        if (wasException && isEventTypeResettingOverflow(type)) {
            lastHistoryText = "";
            wasException = false;
        } else {
            lastHistoryText = getHistoryString(model.getHistory());
        }
    }

    private void setLastOperationType(OperationType type){

        lastOperationType = type;
    }

    private boolean isEventTypeResettingOverflow(OperationType type) {

        return BACKSPACE.equals(type) || CLEAR.equals(type)
                || DIGIT.equals(type) || CLEAR_ENTERED.equals(type)
                || (EQUAL.equals(lastOperationType) && EQUAL.equals(type));
    }
}

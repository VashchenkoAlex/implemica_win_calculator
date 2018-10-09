package win_calculator.model;

import win_calculator.model.exceptions.OperationException;
import win_calculator.model.operations.memory_operations.Memory;
import win_calculator.model.operations.memory_operations.MemoryOperation;
import win_calculator.model.operations.memory_operations.MemoryType;
import win_calculator.model.operations.Operation;
import win_calculator.model.operations.Number;
import win_calculator.model.operations.extra_operations.ExtraOperation;
import win_calculator.model.operations.extra_operations.Negate;
import win_calculator.model.operations.OperationType;
import win_calculator.model.operations.extra_operations.Percent;
import win_calculator.model.operations.main_operations.MainOperation;

import java.math.BigDecimal;
import java.util.LinkedList;

import static win_calculator.model.operations.memory_operations.MemoryType.*;
import static win_calculator.model.operations.memory_operations.MemoryType.SUBTRACT_FROM_MEMORY;
import static win_calculator.model.operations.OperationType.*;
import static win_calculator.model.utils.ModelUtils.roundNumber;

/**
 * Class processes calculator operations
 * Initializes the {@link History} instance
 * Initializes the {@link Memory} instance
 * Provides methods for calculator operations
 * Return BigDecimal lastBinaryResult of calculations
 */
class OperationProcessor {

    /**
     * The instance of {@link History}
     */
    private History history = new History();
    /**
     * The instance of {@link Memory}
     */
    private Memory memory = new Memory();

    /**
     * Stores {@link OperationType} of last operation
     */
    private OperationType lastOperationType = OperationType.CLEAR;
    /**
     * Stores last binary operation for calculations
     */
    private MainOperation lastBinaryOperation;

    /**
     * Stores last used number at calculations
     */
    private BigDecimal lastNumber;
    /**
     * Stores previous used number at calculations
     */
    private BigDecimal previousNumber;
    /**
     * Stores result number of last binary operation
     */
    private BigDecimal lastBinaryResult;
    /**
     * Stores result number of last extra operation
     */
    private BigDecimal lastExtraResult;
    /**
     * Stores result number of last operation
     */
    private BigDecimal operationResult;
    /**
     * Stores last inputted number
     */
    private BigDecimal lastInputtedNumber;

    /**
     * Stores was it enter repeated last time for binary operations
     */
    private boolean enterForOperationRepeated;
    /**
     * Stores was it enter repeated last time
     */
    private boolean enterRepeated;
    /**
     * Stores was it binary operation before
     */
    private boolean mOperationBefore = false;

    /**
     * Rejects last added number and extra operations on it from the history
     * @return true if rejection was successful
     */
    boolean rejectLastNumberWithExtraOperations() {

        boolean done = false;
        if (historyNotEmpty()) {
            if (historyContainsExtraOperations()) {
                LinkedList<Operation> operations = new LinkedList<>(history.getOperations());
                OperationType type;
                for (int i = operations.size() - 1; i > 0; i--) {
                    type = operations.getLast().getType();
                    if (EXTRA_OPERATION.equals(type) || NEGATE.equals(type) || PERCENT.equals(type)) {
                        operations.removeLast();
                    }
                    if (NUMBER.equals(type)) {
                        break;
                    }
                }
                operations.removeLast();
                history.setOperations(operations);
                lastNumber = null;
                setLastExtraResult(null);
                done = true;
            }
        }
        return done;
    }

    /**
     * Selects number for the {@link Negate} operation in depends on previous operation
     * Sends selected number to the history
     * Calls method calculate() at {@link Negate} with selected number
     * @param negate - given {@link Negate} operation
     * @param inputtedNumber - given last inputted number
     * @param responseNumber - given last response number
     * @return BigDecimal result of calculations
     */
    BigDecimal processNegate(Operation negate, BigDecimal inputtedNumber, BigDecimal responseNumber) {

        BigDecimal result = responseNumber;
        if (inputtedNumber == null && lastEventNotNumberAndNotNegate()) {
            if (responseNumber != null) {
                if (MAIN_OPERATION.equals(lastOperationType) || CLEAR.equals(lastOperationType)) {
                    addNumberToHistory(result);
                    setLastNumber(result);
                    resetResultNumber();
                }
            } else {
                result = BigDecimal.ZERO;
                addNumberToHistory(result);
                setLastNumber(result);
                resetResultNumber();
            }
            result = ((Negate) negate).calculate(result);
            lastNumber = result;
            addEventToHistory(negate);
        } else {
            result = ((Negate) negate).calculate(lastNumber);
            lastNumber = result;
            if (isHistoryContainNegate()) {
                addEventToHistory(negate);
            }
        }
        return result;
    }

    LinkedList<Operation> getHistory() {

        return history.getOperations();
    }

    BigDecimal processExtraOperation(Operation operation, BigDecimal number, BigDecimal responseNumber) throws OperationException {

        BigDecimal resultNum;
        if (number != null) {
            lastInputtedNumber = number;
            addNumberToHistory(number);
            setLastNumber(number);
            resultNum = number;
        } else if (responseNumber != null) {
            resultNum = responseNumber;
            if (MAIN_OPERATION.equals(lastOperationType) || (enterRepeated
                    && !EXTRA_OPERATION.equals(lastOperationType) && !NEGATE.equals(lastOperationType))) {
                addNumberToHistory(resultNum);
                setLastNumber(resultNum);
                resetResultNumber();
            }
        } else {
            resultNum = BigDecimal.ZERO;
            addZeroToHistory();
        }
        addEventToHistory(operation);
        resultNum = ((ExtraOperation) operation).calculate(resultNum);
        setLastExtraResult(resultNum);
        return resultNum;
    }

    BigDecimal processMainOperation(Operation operation, BigDecimal number, BigDecimal responseNumber) throws OperationException {

        BigDecimal result = responseNumber;
        if (number != null) {
            addNumberToHistory(number);
            setLastNumber(number);
            result = number;
            lastInputtedNumber = number;
            if (enterRepeated) {
                resetResultNumber();
            }
            if (!mOperationBefore) {
                previousNumber = null;
            }
        } else if (responseNumber != null) {
            if (!hasExtraOperations()) {
                changeLastNumberAtEvents(result);
            }
        } else {
            addZeroToHistory();
            result = BigDecimal.ZERO;
        }
        BigDecimal operationResult = doOperation(operation);
        if (operationResult != null) {
            result = operationResult;
        }
        enterRepeated = false;
        resetLastExtraResult();
        return result;
    }

    BigDecimal processClear() {

        resetValues();
        return null;
    }

    BigDecimal processEnter(BigDecimal number, BigDecimal previousResponse) throws OperationException {

        BigDecimal response = previousResponse;
        if (number != null) {
            resetResultNumber();
            setLastNumber(number);
            response = number;
            lastInputtedNumber = number;
        } else {
            if (PERCENT.equals(lastOperationType)) {
                lastBinaryResult = previousNumber;
            }
            if (EXTRA_OPERATION.equals(lastOperationType) && enterRepeated) {
                lastBinaryResult = lastInputtedNumber;
            }
            if (!enterRepeated && lastBinaryResult != null && (MAIN_OPERATION.equals(lastOperationType) || MEMORY.equals(lastOperationType))) {
                setLastNumber(lastBinaryResult);
            }
        }
        BigDecimal operationResult = doEnter();
        if (operationResult != null) {
            response = operationResult;
        }
        enterRepeated = true;
        resetLastExtraResult();
        lastOperationType = OperationType.CLEAR;
        history = new History();
        return response;
    }

    BigDecimal processMemory(MemoryOperation event, BigDecimal number) {

        MemoryType memoryType = event.getMemoryType();
        BigDecimal response = number;
        if (CLEAR_MEMORY.equals(memoryType)) {
            memory = new Memory();
        } else {
            if (response != null) {
                lastInputtedNumber = number;
            }
            if (response == null && lastExtraResult != null) {
                response = lastExtraResult;
            }
            if (response == null && operationResult != null) {
                response = operationResult;
            }
            if (response == null && lastNumber != null) {
                response = lastNumber;
            }
            if (response == null) {
                response = BigDecimal.ZERO;
            }
            BigDecimal result = doMemoryEvent(event, response);
            if (result != null) {
                response = result;
            }
        }
        lastOperationType = event.getType();
        return response;
    }

    BigDecimal processPercent(Operation operation, BigDecimal number) {

        if (number != null) {
            lastInputtedNumber = number;
        }
        BigDecimal response = roundNumber(doPercent((Percent) operation, number));
        if (!BigDecimal.ZERO.equals(response) && !"0.00".equals(response.toString())) {
            if (previousNumber == null) {
                previousNumber = lastNumber;
            } else if (number != null) {
                previousNumber = lastBinaryResult;
            }
            rejectLastNumberWithExtraOperations();
            addNumberToHistory(response);
            lastNumber = response;
            enterRepeated = false;
            addEventToHistory(operation);
        } else if (MAIN_OPERATION.equals(lastOperationType) || EXTRA_OPERATION.equals(lastOperationType)) {
            addNumberToHistory(BigDecimal.ZERO);
            setLastNumber(number);
            resetResultNumber();
        } else {
            addZeroToHistory();
        }
        return response;
    }

    void processClearEntered() {

        lastInputtedNumber = null;
        boolean done = rejectLastNumberWithExtraOperations();
        if (!done) {
            rejectLastNumber(); // ??
        }
        if (!enterRepeated ) {
            lastExtraResult = operationResult;
            if (lastExtraResult == null) {
                lastExtraResult = previousNumber;
            }
        }
    }

    private void rejectLastNumber() {

        if (historyNotEmpty() && !lastEventNotNumberAndNotNegate()) {
            LinkedList<Operation> operations = new LinkedList<>(history.getOperations());
            OperationType type;
            for (int i = operations.size() - 1; i > 0; i--) {
                type = operations.get(i).getType();
                if (NUMBER.equals(type)) {
                    operations.removeLast();
                    break;
                }
            }
            history.setOperations(operations);
        }
    }

    private void setLastExtraResult(BigDecimal lastExtraResult) {
        this.lastExtraResult = lastExtraResult;
    }

    private void resetLastExtraResult() {
        if (lastExtraResult != null) {
            lastNumber = lastExtraResult;
        }
        lastExtraResult = null;
    }

    private boolean lastEventNotNumberAndNotNegate() {

        boolean result = true;
        if (lastOperationType != null) {
            result = !NUMBER.equals(lastOperationType) && !NEGATE.equals(lastOperationType);
        }
        return result;
    }

    private boolean historyNotEmpty() {

        return !history.getOperations().isEmpty();
    }

    private void addZeroToHistory() {

        Number number = new Number(BigDecimal.ZERO);
        if (!historyNotEmpty()) {
            history.addOperation(number);
        } else {
            history.changeNumberAtFirstPosition(number);
        }
        lastNumber = BigDecimal.ZERO;
        mOperationBefore = false;
    }

    private void resetResultNumber() {

        lastBinaryResult = null;
    }

    private boolean isHistoryContainNegate() {

        return history.isContainsGivenOperationType(NEGATE);
    }

    private boolean historyContainsExtraOperations() {

        return history.isContainsGivenOperationType(NEGATE) || history.isContainsGivenOperationType(EXTRA_OPERATION) || history.isContainsGivenOperationType(PERCENT);
    }

    private BigDecimal[] selectNumbersForPercent(BigDecimal number) {

        BigDecimal firstNumber = BigDecimal.ZERO;
        BigDecimal secondNumber = BigDecimal.ZERO;
        if (number != null) {
            secondNumber = number;
            if (lastBinaryResult != null) {
                firstNumber = lastBinaryResult;
            } else if (mOperationBefore && !NUMBER.equals(lastOperationType)) {
                firstNumber = lastNumber;
            } else {
                firstNumber = previousNumber;
            }
        } else if (lastBinaryResult != null) {
            if (PERCENT.equals(lastOperationType)) {
                secondNumber = lastNumber;
            } else if (EXTRA_OPERATION.equals(lastOperationType) && lastExtraResult != null) {
                secondNumber = lastExtraResult;
            } else {
                secondNumber = lastBinaryResult;
            }
            firstNumber = lastBinaryResult;
        } else if (NEGATE.equals(lastOperationType)) {
            firstNumber = previousNumber;
            secondNumber = lastNumber;
        } else if (lastNumber != null) {
            firstNumber = lastNumber;
            if (previousNumber != null && PERCENT.equals(lastOperationType) && mOperationBefore) {
                secondNumber = previousNumber;
            } else if (lastExtraResult != null) {
                secondNumber = lastExtraResult;
            } else {
                secondNumber = lastNumber;
            }
        }
        return new BigDecimal[]{firstNumber, secondNumber};
    }

    private boolean hasExtraOperations() {

        return history.isContainsGivenOperationType(EXTRA_OPERATION);
    }

    private BigDecimal doPercent(Percent percent, BigDecimal number) {

        BigDecimal[] numbers = selectNumbersForPercent(number);
        BigDecimal result;
        if (numbers[0] != null) {
            result = percent.calculate(numbers[0], numbers[1]);
        } else {
            result = BigDecimal.ZERO;
        }
        return result;
    }

    private BigDecimal doOperation(Operation operation) throws OperationException {

        initVariables();
        enterForOperationRepeated = false;
        BigDecimal result = null;
        if (MAIN_OPERATION.equals(lastOperationType)) {
            changeLastEvent(operation);
        } else {
            addEventToHistory(operation);
            result = doCalculation();
        }
        mOperationBefore = true;
        lastBinaryOperation = (MainOperation) operation;
        return result;
    }

    private BigDecimal doCalculation() throws OperationException {
        BigDecimal result = null;
        if (mOperationBefore || enterForOperationRepeated) {
            if (isPreviousNumberEmpty()) {
                if (isResultEmpty()) {
                    result = lastBinaryOperation.calculate(lastNumber);
                } else {
                    result = lastBinaryOperation.calculate(operationResult, lastNumber);
                }
            } else {
                if (enterForOperationRepeated || !isResultEmpty()) {
                    result = lastBinaryOperation.calculate(operationResult, lastNumber);
                } else {
                    result = lastBinaryOperation.calculate(previousNumber, lastNumber);
                }
            }
            this.lastBinaryResult = result;
        }
        operationResult = result;
        return result;
    }

    private BigDecimal doEnter() throws OperationException {

        initVariables();
        if (mOperationBefore || enterForOperationRepeated) {
            doCalculation();
            enterForOperationRepeated = true;
            mOperationBefore = false;
        }
        lastBinaryResult = operationResult;
        setHistory(new History());
        previousNumber = null;
        return lastBinaryResult;
    }

    private void resetValues() {

        lastNumber = null;
        previousNumber = null;
        operationResult = null;
        enterForOperationRepeated = false;
        mOperationBefore = false;
        history = new History();
        lastBinaryResult = null;
        enterRepeated = false;
        lastExtraResult = null;
        lastInputtedNumber = null;
    }

    private boolean isResultEmpty() {

        return operationResult == null;
    }

    private boolean isPreviousNumberEmpty() {

        return previousNumber == null;
    }

    private void initVariables() {

        if (lastExtraResult != null) {
            if (lastEventNotNumberAndNotNegate()) {
                lastNumber = lastExtraResult;
            } else if (lastBinaryResult != null || NUMBER.equals(lastOperationType)){
                operationResult = lastExtraResult;
            }
        }
        if (lastBinaryResult != null) {
            operationResult = lastBinaryResult;
        }
    }

    private BigDecimal doMemoryEvent(MemoryOperation event, BigDecimal number) {

        MemoryType memoryType = event.getMemoryType();
        BigDecimal result = null;
        if (ADD_TO_MEMORY.equals(memoryType)) {
            memory.addToStoredNumber(number);
        } else if (CLEAR_MEMORY.equals(memoryType)) {
            memory.clear();
        } else if (STORE.equals(memoryType)) {
            memory.storeNumber(number);
        } else if (RECALL.equals(memoryType)) {
            result = memory.getStoredNumber();
        } else if (SUBTRACT_FROM_MEMORY.equals(memoryType)) {
            memory.subtractFromStoredNumber(number);
        }
        return result;
    }

    private void addNumberToHistory(BigDecimal number) {

        addEventToHistory(new Number(number));
    }

    private void addEventToHistory(Operation operation) {

        history.addOperation(operation);
        lastOperationType = operation.getType();
    }

    private void setHistory(History history) {
        this.history = history;
    }

    private void changeLastEvent(Operation operation) {

        history.addMainOperation(operation);
    }

    private void setLastNumber(BigDecimal number) {

        previousNumber = lastNumber;
        lastNumber = number;
    }

    private void changeLastNumberAtEvents(BigDecimal number) {

        lastNumber = number;
        if (enterRepeated && !NEGATE.equals(lastOperationType)) {
            history.addOperation(new Number(number));
        } else if (!enterRepeated && NEGATE.equals(lastOperationType)) {
            history.changeLastNumber(new Number(number));
        }
    }

}

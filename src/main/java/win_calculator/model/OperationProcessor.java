package win_calculator.model;

import win_calculator.model.exceptions.OperationException;
import win_calculator.model.memory.Memory;
import win_calculator.model.memory.MemoryEvent;
import win_calculator.model.memory.MemoryType;
import win_calculator.model.nodes.History;
import win_calculator.model.nodes.events.Event;
import win_calculator.model.nodes.events.Number;
import win_calculator.model.nodes.events.extra_operations.ExtraOperation;
import win_calculator.model.nodes.events.extra_operations.Negate;
import win_calculator.model.nodes.events.EventType;
import win_calculator.model.nodes.events.extra_operations.Percent;
import win_calculator.model.nodes.events.main_operations.MainOperation;

import java.math.BigDecimal;
import java.util.LinkedList;

import static win_calculator.model.memory.MemoryType.*;
import static win_calculator.model.memory.MemoryType.SUBTRACT_FROM_MEMORY;
import static win_calculator.model.nodes.events.EventType.*;
import static win_calculator.model.utils.ModelUtils.roundNumber;

class OperationProcessor {

    private History history = new History();
    private Memory memory = new Memory();
    private EventType lastEventType = EventType.CLEAR;
    private MainOperation lastOperation;

    private BigDecimal lastNumber;
    private BigDecimal previousNumber;
    private BigDecimal result;
    private BigDecimal lastExtraResult;
    private BigDecimal operationResult;
    private BigDecimal lastInputedNumber;

    private boolean enterForOperationRepeated;
    private boolean enterRepeated;
    private boolean mOperationBefore = false;

    private void addNumberToHistory(BigDecimal number) {

        addEventToHistory(new Number(number));
    }

    private void addEventToHistory(Event event) {

        history.addEvent(event);
        lastEventType = event.getType();
    }

    private void setHistory(History history) {
        this.history = history;
    }

    private void changeLastEvent(Event event) {

        history.changeLastEvent(event);
    }

    private void setLastNumber(BigDecimal number) {

        previousNumber = lastNumber;
        lastNumber = number;
    }

    private void changeLastNumberAtEvents(BigDecimal number) {

        lastNumber = number;
        if (enterRepeated && !NEGATE.equals(lastEventType)) {
            history.addEvent(new Number(number));
        } else if (!enterRepeated && NEGATE.equals(lastEventType)) {
            history.changeLastNumber(new Number(number));
        }
    }

    boolean rejectLastNumberWithExtraOperations() {

        boolean done = false;
        if (historyNotEmpty()) {
            if (historyContainsExtraOperations()) {
                LinkedList<Event> events = new LinkedList<>(history.getEvents());
                EventType type;
                for (int i = events.size() - 1; i > 0; i--) {
                    type = events.getLast().getType();
                    if (EXTRA_OPERATION.equals(type) || NEGATE.equals(type) || PERCENT.equals(type)) {
                        events.removeLast();
                    }
                    if (NUMBER.equals(type)) {
                        break;
                    }
                }
                events.removeLast();
                history.setEvents(events);
                lastNumber = null;
                setLastExtraResult(null);
                done = true;
            }
        }
        return done;
    }

    private void rejectLastNumber() {

        if (historyNotEmpty() && !lastEventNotNumberAndNotNegate()) {
            LinkedList<Event> events = new LinkedList<>(history.getEvents());
            EventType type;
            for (int i = events.size() - 1; i > 0; i--) {
                type = events.get(i).getType();
                if (NUMBER.equals(type)) {
                    events.removeLast();
                    break;
                }
            }
            history.setEvents(events);
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
        if (lastEventType != null) {
            result = !NUMBER.equals(lastEventType) && !NEGATE.equals(lastEventType);
        }
        return result;
    }

    private boolean historyNotEmpty() {

        return !history.getEvents().isEmpty();
    }


    private void addZeroToHistory() {

        Number number = new Number(BigDecimal.ZERO);
        if (!historyNotEmpty()) {
            history.addEvent(number);
        } else {
            history.changeNumberAtFirstPosition(number);
        }
        lastNumber = BigDecimal.ZERO;
        mOperationBefore = false;
    }

    private void resetResultNumber() {

        result = null;
    }

    private boolean isHistoryContainNegate() {

        return history.isContain(NEGATE);
    }

    private boolean historyContainsExtraOperations() {

        return history.isContain(NEGATE) || history.isContain(EXTRA_OPERATION) || history.isContain(PERCENT);
    }

    private BigDecimal[] selectNumbersForPercent(BigDecimal number) {

        BigDecimal firstNumber = BigDecimal.ZERO;
        BigDecimal secondNumber = BigDecimal.ZERO;
        if (number != null) {
            secondNumber = number;
            if (result != null) {
                firstNumber = result;
            } else if (mOperationBefore && !NUMBER.equals(lastEventType)) {
                firstNumber = lastNumber;
            } else {
                firstNumber = previousNumber;
            }
        } else if (result != null) {
            if (PERCENT.equals(lastEventType)) {
                secondNumber = lastNumber;
            } else if (EXTRA_OPERATION.equals(lastEventType) && lastExtraResult != null) {
                secondNumber = lastExtraResult;
            } else {
                secondNumber = result;
            }
            firstNumber = result;
        } else if (NEGATE.equals(lastEventType)) {
            firstNumber = previousNumber;
            secondNumber = lastNumber;
        } else if (lastNumber != null) {
            firstNumber = lastNumber;
            if (previousNumber != null && PERCENT.equals(lastEventType) && mOperationBefore) {
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

        return history.isContain(EXTRA_OPERATION);
    }

    BigDecimal processNegate(Event negate, BigDecimal number, BigDecimal responseNumber) {

        BigDecimal result = responseNumber;
        if (number == null && lastEventNotNumberAndNotNegate()) {
            if (responseNumber != null) {
                if (MAIN_OPERATION.equals(lastEventType) || CLEAR.equals(lastEventType)) {
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

    LinkedList<Event> getHistory() {

        return history.getEvents();
    }

    BigDecimal processExtraOperation(Event event, BigDecimal number, BigDecimal responseNumber) throws OperationException {

        BigDecimal resultNum;
        if (number != null) {
            lastInputedNumber = number;
            addNumberToHistory(number);
            setLastNumber(number);
            resultNum = number;
        } else if (responseNumber != null) {
            resultNum = responseNumber;
            if (MAIN_OPERATION.equals(lastEventType) || (enterRepeated
                    && !EXTRA_OPERATION.equals(lastEventType) && !NEGATE.equals(lastEventType))) {
                addNumberToHistory(resultNum);
                setLastNumber(resultNum);
                resetResultNumber();
            }
        } else {
            resultNum = BigDecimal.ZERO;
            addZeroToHistory();
        }
        addEventToHistory(event);
        resultNum = ((ExtraOperation) event).calculate(resultNum);
        setLastExtraResult(resultNum);
        return resultNum;
    }

    BigDecimal processPercent(Event event, BigDecimal number) {

        if (number != null) {
            lastInputedNumber = number;
        }
        BigDecimal response = roundNumber(doPercent((Percent) event, number));
        if (!BigDecimal.ZERO.equals(response) && !"0.00".equals(response.toString())) {
            if (previousNumber == null) {
                previousNumber = lastNumber;
            } else if (number != null) {
                previousNumber = result;
            }
            rejectLastNumberWithExtraOperations();
            addNumberToHistory(response);
            lastNumber = response;
            enterRepeated = false;
            addEventToHistory(event);
        } else if (MAIN_OPERATION.equals(lastEventType) || EXTRA_OPERATION.equals(lastEventType)) {
            addNumberToHistory(BigDecimal.ZERO);
            setLastNumber(number);
            resetResultNumber();
        } else {
            addZeroToHistory();
        }
        return response;
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

    BigDecimal processMainOperation(Event event, BigDecimal number, BigDecimal responseNumber) throws OperationException {

        BigDecimal result = responseNumber;
        if (number != null) {
            addNumberToHistory(number);
            setLastNumber(number);
            result = number;
            lastInputedNumber = number;
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
        BigDecimal operationResult = doOperation(event);
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
            lastInputedNumber = number;
        } else {
            if (PERCENT.equals(lastEventType)) {
                result = previousNumber;
            }
            if (EXTRA_OPERATION.equals(lastEventType) && enterRepeated) {
                result = lastInputedNumber;
            }
            if (!enterRepeated && result != null && (MAIN_OPERATION.equals(lastEventType) || MEMORY.equals(lastEventType))) {
                setLastNumber(result);
            }
        }
        BigDecimal operationResult = doEnter();
        if (operationResult != null) {
            response = operationResult;
        }
        enterRepeated = true;
        resetLastExtraResult();
        lastEventType = EventType.CLEAR;
        history = new History();
        return response;
    }

    void processClearEntered() {

        lastInputedNumber = null;
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

    private BigDecimal doOperation(Event event) throws OperationException {

        initVariables();
        enterForOperationRepeated = false;
        BigDecimal result = null;
        if (MAIN_OPERATION.equals(lastEventType)) {
            changeLastEvent(event);
        } else {
            addEventToHistory(event);
            result = doCalculation();
        }
        mOperationBefore = true;
        lastOperation = (MainOperation) event;
        return result;
    }

    private BigDecimal doCalculation() throws OperationException {
        BigDecimal result = null;
        if (mOperationBefore || enterForOperationRepeated) {
            if (isPreviousNumberEmpty()) {
                if (isResultEmpty()) {
                    result = lastOperation.calculate(lastNumber);
                } else {
                    result = lastOperation.calculate(operationResult, lastNumber);
                }
            } else {
                if (enterForOperationRepeated || !isResultEmpty()) {
                    result = lastOperation.calculate(operationResult, lastNumber);
                } else {
                    result = lastOperation.calculate(previousNumber, lastNumber);
                }
            }
            this.result = result;
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
        result = operationResult;
        setHistory(new History());
        previousNumber = null;
        return result;
    }

    private void resetValues() {

        lastNumber = null;
        previousNumber = null;
        operationResult = null;
        enterForOperationRepeated = false;
        mOperationBefore = false;
        history = new History();
        result = null;
        enterRepeated = false;
        lastExtraResult = null;
        lastInputedNumber = null;
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
            } else if (result != null || NUMBER.equals(lastEventType)){
                operationResult = lastExtraResult;
            }
        }
        if (result != null) {
            operationResult = result;
        }
    }

    BigDecimal processMemory(MemoryEvent event, BigDecimal number) {

        MemoryType memoryType = event.getMemoryType();
        BigDecimal response = number;
        if (CLEAR_MEMORY.equals(memoryType)) {
            memory = new Memory();
        } else {
            if (response != null) {
                lastInputedNumber = number;
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
        lastEventType = event.getType();
        return response;
    }

    private BigDecimal doMemoryEvent(MemoryEvent event, BigDecimal number) {

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
}

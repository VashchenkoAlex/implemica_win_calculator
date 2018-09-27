package win_calculator.model;

import win_calculator.model.exceptions.MyException;
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

import static win_calculator.model.nodes.events.EventType.*;
import static win_calculator.model.utils.ModelUtils.checkOnOverflow;
import static win_calculator.model.utils.ModelUtils.roundNumber;

class OperationProcessor {

    private History history = new History();
    private PercentHandler percentHandler = new PercentHandler(this);
    private EventType lastEventType = EventType.CLEAR;

    // operation vars
    private MainOperation lastOperation;

    private BigDecimal lastNumber;
    private BigDecimal previousNumber;
    private BigDecimal result;
    private BigDecimal lastExtraResult;
    private BigDecimal operationResult;

    private boolean enterForOperationRepeated;
    private boolean enterRepeated;
    private boolean mOperationBefore = false;

    private void addNumberToHistory(Number number) {

        history.addEvent(number);
        lastEventType = EventType.NUMBER;
        resetResultNumber();
        setLastNumber(number.getBigDecimalValue());
    }

    private void addEventToHistory(Event event) {

        history.addEvent(event);
        lastEventType = event.getType();
    }

    private void clearHistory() {

        history = new History();
        result = null;
        enterRepeated = false;
        mOperationBefore = false;
        lastNumber = null;
        previousNumber = null;
        lastExtraResult = null;
    }

    private void setHistory(History history) {
        this.history = history;
    }

    private EventType getLastEventType() {

        return lastEventType;
    }

    private BigDecimal getLastNumber() {

        return lastNumber;
    }

    private void changeLastEvent(Event event) {

        history.changeLastMOperation(event);
    }

    private BigDecimal getPreviousNumber() {

        return previousNumber;
    }

    private void setMOperationBefore(boolean mOperationBefore) {
        this.mOperationBefore = mOperationBefore;
    }

    private void setResult(BigDecimal result) {
        this.result = result;
    }

    private void setLastNumber(BigDecimal number) {

        previousNumber = lastNumber;
        lastNumber = number;
    }

    private void changeLastNumber(BigDecimal lastNumber) {

        this.lastNumber = lastNumber;
    }

    private void changePreviousNumber(BigDecimal number) {

        previousNumber = number;
    }

    private BigDecimal getResult() {

        return result;
    }

    private void changeLastEventNumber(BigDecimal number) {

        history.changeLastNumber(new Number(number));
        lastNumber = number;
    }

    private void changeLastNumberAtEvents(BigDecimal number) {

        lastNumber = number;
        if (enterRepeated && !NEGATE.equals(lastEventType)) {
            history.addEvent(new Number(number));
        } else if (!enterRepeated && NEGATE.equals(lastEventType)) {
            changeLastEventNumber(number);
        }
    }

    boolean rejectLastNumberWithExtraOperations() {

        boolean done = false;
        if (historyNotEmpty()) {
            if (historyContainsExtraOperations()){
                LinkedList<Event> events = new LinkedList<>(history.getEvents());
                EventType type;
                for (int i = events.size() - 1; i > 0; i--) {
                    type = events.getLast().getType();
                    if (EXTRA_OPERATION.equals(type) || NEGATE.equals(type) || PERCENT.equals(type)) {
                        events.removeLast();
                    }
                    if (NUMBER.equals(type)){
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

    private void rejectLastNumber(){

        if (historyNotEmpty()){
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

    private BigDecimal getLastExtraResult() {
        return lastExtraResult;
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

    private boolean lastEventNotNumber() {

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

    private void resetPreviousNumber() {

        previousNumber = null;
    }

    private boolean isHistoryContainNegate() {

        return history.isContain(NEGATE);
    }

    private boolean historyContainsExtraOperations() {

        return history.isContain(NEGATE) || history.isContain(EXTRA_OPERATION) || history.isContain(PERCENT);
    }

    BigDecimal[] selectNumbersForPercent(Number number) {

        BigDecimal firstNumber = BigDecimal.ZERO;
        BigDecimal secondNumber = BigDecimal.ZERO;
        if (number != null) {
            secondNumber = number.getBigDecimalValue();
            if (result != null) {
                firstNumber = result;
            } else if (mOperationBefore && !NUMBER.equals(getLastEventType())) {
                firstNumber = lastNumber;
            } else {
                firstNumber = previousNumber;
            }
        } else if (result != null) {
            firstNumber = result;
            secondNumber = result;
        } else if (NEGATE.equals(getLastEventType())) {
            firstNumber = previousNumber;
            secondNumber = lastNumber;
        } else if (lastNumber != null) {
            firstNumber = lastNumber;
            if (previousNumber != null && PERCENT.equals(getLastEventType())) {
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

    BigDecimal processNegate(Event negate, Number number, BigDecimal responseNumber) {

        BigDecimal result = responseNumber;
        if (number == null && lastEventNotNumber()) {
            if (responseNumber != null) {
                if (MAIN_OPERATION.equals(getLastEventType()) || CLEAR.equals(getLastEventType())) {
                    addNumberToHistory(new Number(result));
                }
            } else {
                result = BigDecimal.ZERO;
                addNumberToHistory(new Number(result));
            }
            result = ((Negate) negate).calculate(result);
            changeLastNumber(result);
            addEventToHistory(negate);
        } else {
            result = ((Negate) negate).calculate(getLastNumber());
            changeLastNumber(result);
            if (isHistoryContainNegate()) {
                addEventToHistory(negate);
            }
        }
        return result;
    }

    LinkedList<Event> getHistory() {

        return history.getEvents();
    }

    BigDecimal processExtraOperation(Event event, Number number, BigDecimal responseNumber) throws MyException {

        BigDecimal resultNum;
        if (number != null) {
            addNumberToHistory(number);
            resultNum = new BigDecimal(number.getValue());
        } else if (responseNumber != null) {
            resultNum = responseNumber;
            EventType type = getLastEventType();
            if (MAIN_OPERATION.equals(type) || (enterRepeated
                    && !EXTRA_OPERATION.equals(type) && !NEGATE.equals(type))) {
                addNumberToHistory(new Number(resultNum));
            }
        } else {
            resultNum = BigDecimal.ZERO;
            addZeroToHistory();
        }
        addEventToHistory(event);
        resultNum = roundNumber(((ExtraOperation) event).calculate(resultNum));
        checkOnOverflow(resultNum);
        setLastExtraResult(resultNum);
        return resultNum;
    }

    BigDecimal processPercent(Event event, Number number) throws MyException {

        BigDecimal result = roundNumber(percentHandler.doOperation((Percent) event, number));
        checkOnOverflow(result);
        if (!BigDecimal.ZERO.equals(result) && !"0.00".equals(result.toString())) {
            BigDecimal tempNumber;
            if (getPreviousNumber() != null) {
                tempNumber = getPreviousNumber();
            } else {
                tempNumber = getLastNumber();
            }
            rejectLastNumberWithExtraOperations();
            changeLastEventNumber(result);
            enterRepeated = false;
            changePreviousNumber(tempNumber);
            addEventToHistory(event);
        } else if (MAIN_OPERATION.equals(getLastEventType()) || EXTRA_OPERATION.equals(getLastEventType())) {
            addNumberToHistory(new Number(BigDecimal.ZERO));
        } else {
            addZeroToHistory();
        }
        return result;
    }

    BigDecimal processMainOperation(Event event, Number number, BigDecimal responseNumber) throws MyException {

        BigDecimal result = responseNumber;
        if (number != null) {
            addNumberToHistory(number);
            result = number.getBigDecimalValue();
            if (enterRepeated) {
                resetResultNumber();
            }
            if (!mOperationBefore) {
                resetPreviousNumber();
            }
        } else if (responseNumber != null) {
            if (!hasExtraOperations()) {
                changeLastNumberAtEvents(result);
            }
        } else {
            addZeroToHistory();
        }
        BigDecimal operationResult = doOperation((MainOperation) event);
        if (operationResult != null) {
            result = operationResult;
        }
        enterRepeated = false;
        resetLastExtraResult();
        return result;
    }

    BigDecimal processClear() {

        clearHistory();
        resetValues();
        return null;
    }

    BigDecimal processEnter(Number number,BigDecimal responseNumber) throws MyException {

        if (number != null) {
            resetResultNumber();
            setLastNumber(number.getBigDecimalValue());
            responseNumber = number.getBigDecimalValue();
        } else {
            BigDecimal resultNumber = getResult();
            EventType lastEventType = getLastEventType();
            if (!enterRepeated && resultNumber != null && (MAIN_OPERATION.equals(lastEventType) || MEMORY.equals(lastEventType))) {
                setLastNumber(resultNumber);
            }
        }
        BigDecimal operationResult = doEnter();
        if (operationResult != null) {
            responseNumber = operationResult;
        }
        enterRepeated = true;
        resetLastExtraResult();
        lastEventType = EventType.CLEAR;
        return responseNumber;
    }

    void processClearEntered(){

        boolean done = rejectLastNumberWithExtraOperations();
        if (!done){
            rejectLastNumber();
        }
        if (!enterRepeated){
            lastExtraResult = operationResult;
            if (lastExtraResult == null){
                lastExtraResult = previousNumber;
            }
        }
    }

    //main operations

    private BigDecimal doOperation(MainOperation operation) throws MyException {

        initVariables();
        setEnterForOperationRepeated(false);
        if (MAIN_OPERATION.equals(getLastEventType())) {
            changeLastEvent(operation);
        } else {
            addEventToHistory(operation);
            doCalculation();
        }
        setMOperationBefore(true);
        lastOperation = operation;
        return operationResult;
    }

    private void doCalculation() throws MyException {
        if (mOperationBefore || enterForOperationRepeated) {
            if (isPreviousNumberEmpty()) {
                if (isResultEmpty()) {
                    operationResult = lastOperation.calculate(lastNumber);
                } else {
                    operationResult = lastOperation.calculate(operationResult, lastNumber);
                }
            } else {
                if (enterForOperationRepeated || !isResultEmpty()) {
                    operationResult = lastOperation.calculate(operationResult, lastNumber);
                } else {
                    operationResult = lastOperation.calculate(previousNumber, lastNumber);
                }
            }
            operationResult = roundNumber(operationResult);
            checkOnOverflow(operationResult);
            setResult(operationResult);
        } else {
            operationResult = null;
        }
    }

    private BigDecimal doEnter() throws MyException {

        initVariables();
        if (mOperationBefore || enterForOperationRepeated) {
            doCalculation();
            setEnterForOperationRepeated(true);
            setMOperationBefore(false);
        } else {
            operationResult = getResult();
        }
        setResult(operationResult);
        setHistory(new History());
        previousNumber = null;
        return operationResult;
    }

    private void resetValues() {
        lastNumber = null;
        previousNumber = null;
        operationResult = null;
        setEnterForOperationRepeated(false);
        mOperationBefore = false;
    }

    private boolean isResultEmpty() {

        return operationResult == null;
    }

    private boolean isPreviousNumberEmpty() {

        return previousNumber == null;
    }

    private void setEnterForOperationRepeated(boolean val) {

        enterForOperationRepeated = val;
    }

    private void initVariables() {

        BigDecimal lastExtraResult = getLastExtraResult();
        if (lastExtraResult != null) {
            if (lastEventNotNumber()){
                lastNumber = lastExtraResult;
            }else {
                operationResult = lastExtraResult;
            }
        }
        BigDecimal resultNumFromHistory = getResult();
        if (resultNumFromHistory != null) {
            operationResult = resultNumFromHistory;
        }
    }
}

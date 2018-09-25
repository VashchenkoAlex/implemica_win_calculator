package win_calculator.model;

import win_calculator.model.exceptions.MyException;
import win_calculator.model.nodes.History;
import win_calculator.model.nodes.actions.Action;
import win_calculator.model.nodes.actions.Number;
import win_calculator.model.nodes.actions.clear.Clear;
import win_calculator.model.nodes.actions.extra_operations.ExtraOperation;
import win_calculator.model.nodes.actions.extra_operations.Negate;
import win_calculator.model.nodes.actions.ActionType;

import java.math.BigDecimal;
import java.util.LinkedList;

import static win_calculator.model.nodes.actions.ActionType.*;
import static win_calculator.model.utils.ModelUtils.checkOnOverflow;
import static win_calculator.model.utils.ModelUtils.roundNumber;

class OperationProcessor {

    private History history = new History();
    private boolean enterRepeated;
    private boolean mOperationBefore;
    private BigDecimal lastNumber;
    private BigDecimal previousNumber;
    private BigDecimal resultNumber;
    private BigDecimal lastExtraResult;
    private Action lastAction = new Clear();

    void addNumberToHistory(Number number){

        history.addAction(number);
        lastAction = number;
        resetResultNumber();
        setLastNumber(number.getBigDecimalValue());
    }

    void addActionToHistory(Action action) {

        history.addAction(action);
        if (MAIN_OPERATION.equals(action.getType())) {
            setMOperationBefore(true);
        }
        lastAction = action;
    }

    void clearHistory() {

        history = new History();
        resultNumber = null;
        enterRepeated = false;
        mOperationBefore = false;
        lastNumber = null;
        previousNumber = null;
        lastExtraResult = null;
    }

    void setHistory(History history) {
        this.history = history;
    }

    ActionType getLastActionType() {

        ActionType result = null;
        Action action = lastAction;
        if (action != null) {
            result = action.getType();
        }
        return result;
    }

    BigDecimal getLastNumber() {

        return lastNumber;
    }

    void changeLastAction(Action action) {

        history.changeLastMOperation(action);
    }

    BigDecimal getPreviousNumber() {

        return previousNumber;
    }

    boolean isMOperationBefore() {

        return mOperationBefore;
    }

    boolean isEnterRepeated() {

        return enterRepeated;
    }

    void setEnterRepeated(boolean enterRepeated) {
        this.enterRepeated = enterRepeated;
    }

    void setMOperationBefore(boolean mOperationBefore) {
        this.mOperationBefore = mOperationBefore;
    }

    void setResultNumber(BigDecimal resultNumber) {
        this.resultNumber = resultNumber;
    }

    void setLastNumber(BigDecimal number) {

        previousNumber = lastNumber;
        lastNumber = number;
    }

    private void changeLastNumber(BigDecimal lastNumber) {

        this.lastNumber = lastNumber;
    }

    void changePreviousNumber(BigDecimal number) {

        previousNumber = number;
    }

    BigDecimal getResultNumber() {

        return resultNumber;
    }

    void changeLastActionNumber(BigDecimal number) {

        history.changeLastNumber(new Number(number));
        lastNumber = number;
    }

    void changeLastNumberAtActions(BigDecimal number) {

        lastNumber = number;
        if (enterRepeated && !NEGATE.equals(lastAction.getType())) {
            history.addAction(new Number(number));
        } else if (!enterRepeated && NEGATE.equals(lastAction.getType())) {
            changeLastActionNumber(number);
        }
    }

    void rejectLastNumberWithExtraOperations() {

        if (isHistoryNotEmpty() && isRejectionPossible()) {
            LinkedList<Action> actions = new LinkedList<>(history.getActions());
            ActionType type;
            for (int i = actions.size() - 1; i > 0; i--) {
                type = actions.getLast().getType();
                if (EXTRA_OPERATION.equals(type) || NEGATE.equals(type)) {
                    actions.removeLast();
                }
            }
            actions.removeLast();
            history.setActions(actions);
            lastNumber = null;
            previousNumber = null;
            setLastExtraResult(null);
        }
    }

    BigDecimal getLastExtraResult() {
        return lastExtraResult;
    }

    private void setLastExtraResult(BigDecimal lastExtraResult) {
        this.lastExtraResult = lastExtraResult;
    }

    void resetLastExtraResult() {
        if (lastExtraResult != null) {
            lastNumber = lastExtraResult;
        }
        lastExtraResult = null;
    }

    private boolean lastActionNotNumber() {

        ActionType type = getLastActionType();
        boolean result = true;
        if (type != null) {
            result = !NUMBER.equals(type) && !NEGATE.equals(type);
        }
        return result;
    }

    void setLastAction(Action lastAction) {

        this.lastAction = lastAction;
    }

    private boolean isHistoryNotEmpty() {

        return !history.getActions().isEmpty();
    }


    void addZeroToHistory() {

        Number number = new Number(BigDecimal.ZERO);
        if (!isHistoryNotEmpty()) {
            history.addAction(number);
        } else {
            history.changeNumberAtFirstPosition(number);
        }
        lastNumber = BigDecimal.ZERO;
        mOperationBefore = false;
    }

    void resetResultNumber() {

        resultNumber = null;
    }

    void resetPreviousNumber() {

        previousNumber = null;
    }

    private boolean isHistoryContainNegate() {

        return history.isContain(NEGATE);
    }

    private boolean isRejectionPossible() {

        return history.isContain(NEGATE) || history.isContain(EXTRA_OPERATION);
    }

    BigDecimal[] selectNumbersForPercent(Number number) {

        BigDecimal firstNumber = BigDecimal.ZERO;
        BigDecimal secondNumber = BigDecimal.ZERO;
        if (number != null) {
            secondNumber = number.getBigDecimalValue();
            if (resultNumber != null) {
                firstNumber = resultNumber;
            } else if (mOperationBefore && !NUMBER.equals(getLastActionType())) {
                firstNumber = lastNumber;
            } else {
                firstNumber = previousNumber;
            }
        } else if (resultNumber != null) {
            firstNumber = resultNumber;
            secondNumber = resultNumber;
        } else if (NEGATE.equals(getLastActionType())) {
            firstNumber = previousNumber;
            secondNumber = lastNumber;
        } else if (lastNumber != null) {
            firstNumber = lastNumber;
            if (previousNumber != null && PERCENT.equals(getLastActionType())) {
                secondNumber = previousNumber;
            } else if (lastExtraResult != null) {
                secondNumber = lastExtraResult;
            } else {
                secondNumber = lastNumber;
            }
        }
        return new BigDecimal[]{firstNumber, secondNumber};
    }

    boolean hasExtraOperations(){

        return history.isContain(EXTRA_OPERATION);
    }

    BigDecimal processNegate(Action negate,Number number,BigDecimal responseNumber){

        BigDecimal result = responseNumber;
        if (number == null && lastActionNotNumber()) {
            if (responseNumber != null) {
                if (MAIN_OPERATION.equals(getLastActionType()) || CLEAR.equals(getLastActionType())) {
                    addNumberToHistory(new Number(result));
                }
            } else {
                result = BigDecimal.ZERO;
                addNumberToHistory(new Number(result));
            }
            result = ((Negate) negate).calculate(result);
            changeLastNumber(result);
            addActionToHistory(negate);
        } else {
            result = ((Negate) negate).calculate(getLastNumber());
            changeLastNumber(result);
            if (isHistoryContainNegate()) {
                addActionToHistory(negate);
            }
        }
        return result;
    }

    public LinkedList<Action> getHistory(){

        return history.getActions();
    }

    BigDecimal processExtraOperation(Action action, Number number, BigDecimal responseNumber) throws MyException {

        BigDecimal resultNum;
        if (number != null) {
            addNumberToHistory(number);
            resultNum = new BigDecimal(number.getValue());
        } else if (responseNumber != null) {
            resultNum = responseNumber;
            ActionType type = getLastActionType();
            if (MAIN_OPERATION.equals(type) || (isEnterRepeated()
                    && !EXTRA_OPERATION.equals(type) && !NEGATE.equals(type))) {
                addNumberToHistory(new Number(resultNum));
            }
        } else {
            resultNum = BigDecimal.ZERO;
            addZeroToHistory();
        }
        addActionToHistory(action);
        resultNum = roundNumber(((ExtraOperation) action).calculate(resultNum));
        checkOnOverflow(resultNum);
        setLastExtraResult(resultNum);
        return resultNum;
    }
}

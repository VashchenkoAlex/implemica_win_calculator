package win_calculator.model.response_handlers;

import win_calculator.model.nodes.History;
import win_calculator.model.nodes.actions.Action;
import win_calculator.controller.nodes.digits.Number;
import win_calculator.model.nodes.actions.clear.Clear;
import win_calculator.utils.ActionType;

import java.math.BigDecimal;
import java.util.LinkedList;

import static win_calculator.utils.ActionType.*;
import static win_calculator.utils.StringUtils.*;

public class HistoryHandler {

    private History history = new History();
    private boolean enterRepeated;
    private boolean mOperationBefore;
    private BigDecimal lastNumber;
    private BigDecimal previousNumber;
    private BigDecimal resultNumber;
    private BigDecimal lastExtraResult;
    private static final String HISTORY_PATTERN = "################.###############";
    private Action lastAction = new Clear();

    public String getHistoryString(){

        LinkedList<Action> actions = history.getActions();
        String result = "";
        for (Action action : actions) {
            if (EXTRA_OPERATION.equals(action.getType())){
                result = addExtraOperationToString(result,action.getValue());
            }else if (NUMBER.equals(action.getType())){
                result+=convertToString(((Number)action).getBigDecimalValue(),HISTORY_PATTERN);
            }else if (NEGATE.equals(action.getType())){
                result = addExtraOperationToString(result,action.getValue());
            }
            else {
                result+=action.getValue();
            }
        }
        return result;
    }

    public void addActionToHistory(Action action){

        history.addAction(action);
        if (MAIN_OPERATION.equals(action.getType())){
            setMOperationBefore(true);
        }else {
            if (NUMBER.equals(action.getType()) && !NEGATE.equals(lastAction.getType())){
                previousNumber = lastNumber;
                lastNumber = new BigDecimal(action.getValue());
            }
        }
        lastAction = action;
    }

    public void clearHistory(){

        history = new History();
        resultNumber = null;
        enterRepeated = false;
        mOperationBefore = false;
        lastNumber = null;
        previousNumber = null;
        lastExtraResult = null;
    }

    public void setHistory(History history) {
        this.history = history;
    }

    public ActionType getLastActionType(){

        ActionType result = null;
        Action action = lastAction;
        if (action!=null){
            result = action.getType();
        }
        return result;
    }

    public BigDecimal getLastNumber(){

        return lastNumber;
    }

    public void changeLastAction(Action action){

        history.changeLastAction(action);
    }

    public BigDecimal getPreviousNumber(){

        return previousNumber;
    }

    public boolean isMOperationBefore(){

        return mOperationBefore;
    }

    public boolean isEnterRepeated() {

        return enterRepeated;
    }

    public void setEnterRepeated(boolean enterRepeated) {
        this.enterRepeated = enterRepeated;
    }

    public void setMOperationBefore(boolean mOperationBefore) {
        this.mOperationBefore = mOperationBefore;
    }

    public void setResultNumber(BigDecimal resultNumber) {
        this.resultNumber = resultNumber;
    }

    public void setLastNumber(BigDecimal number){

        previousNumber = lastNumber;
        lastNumber = new BigDecimal(number.toString());
    }

    public void changeLastNumber(BigDecimal number){

        lastNumber = number;
        if (enterRepeated){
            addActionToHistory(new Number(number));
        }else if (NEGATE.equals(lastAction.getType())){
            changeLastActionNumber(new Number(number));
        }
    }

    public void changePreviousNumber(BigDecimal number){

        previousNumber = number;
    }

    public BigDecimal getResultNumber(){

        return resultNumber;
    }

    public boolean isPercentLast(){

        return PERCENT.equals(lastAction.getType());
    }

    public void changeLastActionNumber(Number number){

        history.changeLastNumber(number);
    }

    public void rejectLastNumberWithExtraOperations(){

        LinkedList<Action> actions = new LinkedList<>(history.getActions());
        for (int i = actions.size()-1; i>0 && EXTRA_OPERATION.equals(actions.getLast().getType());i--) {
            actions.removeLast();
        }
        actions.removeLast();
        history.setActions(actions);
        lastNumber = null;
        previousNumber = null;
        setLastExtraResult(null);
    }

    public BigDecimal getLastExtraResult() {
        return lastExtraResult;
    }

    public void setLastExtraResult(BigDecimal lastExtraResult) {
        this.lastExtraResult = lastExtraResult;
    }

    public void resetLastExtraResult(){
        if (lastExtraResult!=null){
            lastNumber = new BigDecimal(lastExtraResult.toString());
        }
        lastExtraResult = null;
    }

    public boolean lastActionNotNumber(){

        ActionType type = getLastActionType();
        boolean result = true;
        if (type!=null){
            result = !NUMBER.equals(type) && !NEGATE.equals(type);
        }
        return result;
    }

    public void setLastAction(Action lastAction) {

        this.lastAction = lastAction;
    }
}

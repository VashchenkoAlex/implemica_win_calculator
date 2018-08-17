package win_calculator.model.response_handlers;

import win_calculator.model.nodes.History;
import win_calculator.model.nodes.actions.Action;
import win_calculator.utils.ActionType;

import java.math.BigDecimal;
import java.util.LinkedList;

import static win_calculator.utils.ActionType.*;
import static win_calculator.utils.StringUtils.*;

public class HistoryHandler {

    private History history = new History();
    private boolean operationRepeated;
    private boolean enterRepeated;
    private boolean mOperationBefore;
    private boolean numberEntered;
    private BigDecimal resultNumber;

    public String getHistoryString(){

        LinkedList<Action> actions = history.getActions();
        String result = "";
        for (Action action : actions) {
            if (EXTRA_OPERATION.equals(action.getType())){
                addExtraOperationToString(result,action.getValue());
            }
            result+=action.getValue();
        }
        /*
        void addOperationToHistory(MainOperation operation){

        String currentHistory = historyField.getText();
        String result;
        if (inputtedNumber){
            if (currentHistory.contains(BRACKET)){
                result = currentHistory+operation.getValue();
            }else {
                result = currentHistory+removeCapacity(display.getText())+operation.getValue();
            }
        }else {
            if ("".equals(currentHistory)){
                result = removeCapacity(display.getText())+operation.getValue();
            }else {
                result = currentHistory.substring(0,currentHistory.length()-3)+operation.getValue();
            }
        }
        historyField.setText(result);
    }
         */
        return result;
    }

    public void addActionToHistory(Action action){

        history.addAction(action);
        if (MAIN_OPERATION.equals(action.getType())){
            setNumberEntered(false);
            setMOperationBefore(true);
        }else {
            setNumberEntered(true);
        }
    }

    public void clearHistory(){

        history = new History();
        setNumberEntered(true);
        setMOperationBefore(false);
        setOperationRepeated(false);
        resultNumber = null;
    }

    public void setHistory(History history) {
        this.history = history;
    }

    public ActionType getLastActionType(){

        ActionType result = null;
        Action action = history.getLastAction();
        if (action!=null){
            result = action.getType();
        }
        return result;
    }

    public String getLastActionValue(){

        String result = "";
        Action action = history.getLastAction();
        if (action!=null){
            result = action.getValue();
        }
        return result;
    }

    public BigDecimal getLastAssembledNumber(){

        LinkedList<Action> actions = history.getActions();
        return getLastNumberFrom(actions);
    }

    public boolean isZeroNotFirst(){

        return !BigDecimal.ZERO.equals(getLastAssembledNumber());
    }

    public boolean isLastNumberWhole(){

        return isDotAbsent(getLastAssembledNumber());
    }

    public boolean isComaFirst(){

        return !NUMBER.equals(getLastActionType());
    }

    public boolean isComaLast(){

        return ".".equals(getLastActionValue());
    }

    public void changeLastAction(Action action){

        history.changeLastAction(action);
    }

    public BigDecimal getPreviousNumber(){

        LinkedList<Action> actions = new LinkedList<>(history.getActions());
        actions = removeLastOperations(actions);
        actions = removeLastNumber(actions);
        return getLastNumberFrom(actions);
    }

    public boolean isMOperationBefore(){

        return mOperationBefore;
    }

    private boolean isNumber(LinkedList<Action> actions,int index){

        return NUMBER.equals(actions.get(index).getType());
    }

    private boolean isOperation(LinkedList<Action> actions, int index){

        ActionType type = actions.get(index).getType();
        return MAIN_OPERATION.equals(type) || EXTRA_OPERATION.equals(type) || ENTER.equals(type);
    }

    private LinkedList<Action> removeLastNumber(LinkedList<Action> actions){

        for (int i = actions.size() - 1; i >=0 && isNumber(actions,i); i--) {
            actions.removeLast();
        }
        return actions;
    }

    private BigDecimal getLastNumberFrom(LinkedList<Action> actions){

        BigDecimal resultNum = null;
        if (actions.size()>0){
            LinkedList<Action> tempActions = new LinkedList<>(actions);
            tempActions = removeLastOperations(tempActions);
            StringBuilder resultStr = new StringBuilder();
            for (int i = tempActions.size() - 1; i >= 0 && isNumber(tempActions,i); i--) {
                resultStr.insert(0, tempActions.get(i).getValue());
            }
            if (!"".equals(resultStr.toString())){
                resultNum = new BigDecimal(resultStr.toString());
            }
        }
        return resultNum;
    }

    private LinkedList<Action> removeLastOperations(LinkedList<Action> actions){

        for (int i = actions.size() - 1; i >=0 && isOperation(actions,i); i--) {
                actions.removeLast();
        }
        return actions;
    }

    public boolean isOperationRepeated() {
        return operationRepeated;
    }

    public void setOperationRepeated(boolean operationRepeated) {
        this.operationRepeated = operationRepeated;
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

    public boolean isNumberEntered() {
        return numberEntered;
    }

    public void setNumberEntered(boolean numberEntered) {

        this.numberEntered = numberEntered;
    }

    public void setResultNumber(BigDecimal resultNumber) {
        this.resultNumber = resultNumber;
    }

    public BigDecimal getResultNumber(){

        return resultNumber;
    }

    public BigDecimal getResultNumberForResponse(){

        ActionType lastActionType = getLastActionType();
        BigDecimal result = resultNumber;
        if (NUMBER.equals(lastActionType)){
            result = getLastAssembledNumber();
        }
        if (MAIN_OPERATION.equals(lastActionType)){
            result = resultNumber;
            if (result == null){
                result = getLastAssembledNumber();
            }
        }
        /*BigDecimal result = resultNumber;
        */
        return result;
    }
}

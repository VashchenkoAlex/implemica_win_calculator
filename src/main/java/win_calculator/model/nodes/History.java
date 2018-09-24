package win_calculator.model.nodes;

import win_calculator.model.nodes.actions.Action;
import win_calculator.controller.nodes.digits.Number;
import win_calculator.model.nodes.actions.clear.Clear;
import win_calculator.utils.ActionType;

import java.util.LinkedList;

import static win_calculator.utils.ActionType.MAIN_OPERATION;
import static win_calculator.utils.ActionType.NUMBER;

public class History {

    private LinkedList<Action> actions;

    public History() {

        actions = new LinkedList<>();
    }

    public void addAction(Action action) {

        actions.add(action);
    }

    public void setActions(LinkedList<Action> actions) {

        this.actions = actions;
    }

    public LinkedList<Action> getActions() {

        return actions;
    }

    private Action getLastAction() {

            return actions.getLast();
    }

    public void changeLastMOperation(Action action) {

        if (!actions.isEmpty() && isChangingMOperationPossible()) {
            for (int i = actions.size() - 1; i > 0; i--) {
                if (MAIN_OPERATION.equals(actions.get(i).getType())) {
                    actions.set(i, action);
                    break;
                }
            }
        } else {
            actions.add(action);
        }
    }

    public void changeLastNumber(Number number) {

        if (isChangingNumberPossible()) {
            for (int i = actions.size() - 1; i >= 0; i--) {
                if (NUMBER.equals(actions.get(i).getType())) {
                    actions.set(i, number);
                    break;
                }
            }
        } else {
            actions.add(number);
        }

    }

    private boolean isChangingNumberPossible() {

        boolean result = false;
        for (Action action : actions) {
            if (NUMBER.equals(action.getType())) {
                result = true;
                break;
            }
        }
        result = result && !MAIN_OPERATION.equals(getLastAction().getType());
        return result;
    }

    private boolean isChangingMOperationPossible() {

        boolean result = false;
        for (Action action : actions) {
            if (MAIN_OPERATION.equals(action.getType())) {
                result = true;
                break;
            }
        }
        return result;
    }

    public void changeNumberAtFirstPosition(Number number) {

        if (NUMBER.equals(actions.getFirst().getType())) {
            actions.set(0, number);
        }
    }

    public boolean isContain(ActionType expectedType){

        boolean result = false;
        ActionType type;
        if (!actions.isEmpty()) {
            for (int i = actions.size() - 1; i > 0; i--) {
                type = actions.get(i).getType();
                if (expectedType.equals(type)){
                    result = true;
                }
                if (MAIN_OPERATION.equals(type)){
                    break;
                }
            }
        }
        return result;
    }
}

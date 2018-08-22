package win_calculator.model.nodes;

import win_calculator.model.nodes.actions.Action;
import win_calculator.model.nodes.actions.digits.Number;

import java.util.LinkedList;

import static win_calculator.utils.ActionType.NUMBER;

public class History {

    private LinkedList<Action> actions;

    public History() {

        actions = new LinkedList<>();
    }

    public void addAction(Action action){

        actions.add(action);
    }

    public void setActions(LinkedList<Action> actions){

        this.actions = actions;
    }

    public LinkedList<Action> getActions() {

        return actions;
    }

    public Action getLastAction(){

        if (actions.isEmpty()){
            return null;
        }else {
            return actions.getLast();
        }
    }

    public void changeLastAction(Action action){

        if (!actions.isEmpty()){
            actions.set(actions.size()-1,action);
        }else {
            actions.add(action);
        }
    }

    public void changeLastNumber(Number number){

        for (int i = actions.size()-1; i >0 ; i--) {
            if (NUMBER.equals(actions.get(i).getType())){
                actions.set(i,number);
                break;
            }
        }
    }
}

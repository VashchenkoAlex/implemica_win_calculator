package win_calculator.model.nodes.actions.clear;

import win_calculator.model.nodes.actions.Action;
import win_calculator.utils.ActionType;

public class BaskSpace implements Action {

    private static final String VALUE = " <- ";
    private static final ActionType TYPE = ActionType.BACKSPACE;

    public String getValue() {
        return VALUE;
    }

    @Override
    public ActionType getType() {
        return TYPE;
    }
}
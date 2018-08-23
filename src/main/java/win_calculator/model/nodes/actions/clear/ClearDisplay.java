package win_calculator.model.nodes.actions.clear;

import win_calculator.model.nodes.actions.Action;
import win_calculator.utils.ActionType;

public class ClearDisplay implements Action {


    private static final String VALUE = " CE ";
    private static final ActionType TYPE = ActionType.CLEAR_ENTERED;

    public String getValue() {
        return VALUE;
    }

    @Override
    public ActionType getType() {
        return TYPE;
    }
}

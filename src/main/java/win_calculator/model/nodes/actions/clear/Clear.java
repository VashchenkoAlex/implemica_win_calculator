package win_calculator.model.nodes.actions.clear;

import win_calculator.model.nodes.actions.Action;
import win_calculator.model.nodes.actions.ActionType;

public class Clear implements Action {

    private static final ActionType TYPE = ActionType.CLEAR;

    @Override
    public ActionType getType() {
        return TYPE;
    }
}

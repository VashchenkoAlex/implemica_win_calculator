package win_calculator.model.nodes.actions.clear;

import win_calculator.model.nodes.actions.Action;
import win_calculator.utils.ActionType;

public class LastExtraCleaner implements Action {

    private static final ActionType TYPE = ActionType.CLEAR_EXTRA;

    @Override
    public ActionType getType() {
        return TYPE;
    }
}

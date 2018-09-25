package win_calculator.model.nodes.actions.enter;

import win_calculator.model.nodes.actions.Action;
import win_calculator.model.nodes.actions.ActionType;

public class Enter implements Action {

    private static final ActionType TYPE = ActionType.ENTER;

    @Override
    public ActionType getType() {

        return TYPE;
    }
}

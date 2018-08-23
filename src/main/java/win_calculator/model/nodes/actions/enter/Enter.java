package win_calculator.model.nodes.actions.enter;

import win_calculator.model.nodes.actions.Action;
import win_calculator.utils.ActionType;

public class Enter implements Action {

    private static final String VALUE = " = ";
    private static final ActionType TYPE = ActionType.ENTER;

    public String getValue() {
        return VALUE;
    }

    @Override
    public ActionType getType() {

        return TYPE;
    }
}
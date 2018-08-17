package win_calculator.model.nodes.actions.numbers;

import win_calculator.utils.ActionType;

public class FourNumber implements Number {

    private static final String VALUE = "4";
    private static final ActionType TYPE = ActionType.NUMBER;
    @Override
    public String getValue() {
        return VALUE;
    }

    @Override
    public ActionType getType() {

        return TYPE;
    }
}

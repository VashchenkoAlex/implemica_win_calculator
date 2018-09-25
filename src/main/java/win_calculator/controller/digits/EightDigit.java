package win_calculator.controller.digits;

import win_calculator.model.nodes.actions.ActionType;

public class EightDigit implements Digit {

    private static final String VALUE = "8";
    private static final ActionType TYPE = ActionType.DIGIT;

    @Override
    public String getValue() {
        return VALUE;
    }

    @Override
    public ActionType getType() {

        return TYPE;
    }
}

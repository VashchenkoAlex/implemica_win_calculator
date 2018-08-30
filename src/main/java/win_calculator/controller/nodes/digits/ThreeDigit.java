package win_calculator.controller.nodes.digits;

import win_calculator.utils.ActionType;

public class ThreeDigit implements Digit {

    private static final String VALUE = "3";
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

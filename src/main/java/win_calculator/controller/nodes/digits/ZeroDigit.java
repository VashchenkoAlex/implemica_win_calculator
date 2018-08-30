package win_calculator.controller.nodes.digits;

import win_calculator.utils.ActionType;

public class ZeroDigit implements Digit {

    private static final String VALUE = "0";
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

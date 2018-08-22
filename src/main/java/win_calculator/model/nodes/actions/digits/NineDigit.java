package win_calculator.model.nodes.actions.digits;

import win_calculator.utils.ActionType;

public class NineDigit implements Digit {

    private static final String VALUE = "9";
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

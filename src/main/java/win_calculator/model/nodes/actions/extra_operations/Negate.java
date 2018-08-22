package win_calculator.model.nodes.actions.extra_operations;

import win_calculator.utils.ActionType;

import java.math.BigDecimal;

public class Negate implements ExtraOperation {

    private static final ActionType TYPE = ActionType.EXTRA_OPERATION;
    private static final String VALUE = "negate( ";

    @Override
    public BigDecimal calculate(BigDecimal number){

        return number.negate();
    }

    @Override
    public String getValue() {
        return VALUE;
    }

    @Override
    public ActionType getType() {
        return TYPE;
    }
}

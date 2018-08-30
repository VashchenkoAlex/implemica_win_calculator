package win_calculator.model.nodes.actions.extra_operations;

import win_calculator.utils.ActionType;

import java.math.BigDecimal;

public class Negate implements ExtraOperation {

    private static final ActionType TYPE = ActionType.EXTRA_OPERATION;
    private static final String VALUE = "negate( ";
    private static final String EXTRA_VALUE = "-";

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

    public String getExtraValue() {
        return EXTRA_VALUE;
    }
}

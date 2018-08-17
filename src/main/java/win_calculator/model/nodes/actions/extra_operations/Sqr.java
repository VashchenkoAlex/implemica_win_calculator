package win_calculator.model.nodes.actions.extra_operations;

import win_calculator.utils.ActionType;

import java.math.BigDecimal;

public class Sqr implements ExtraOperation {

    private static final String SYMBOL = "sqr( ";
    private static final int POW = 2;
    private static final ActionType TYPE = ActionType.EXTRA_OPERATION;

    @Override
    public BigDecimal calculate(BigDecimal number){

        return number.pow(POW);
    }

    @Override
    public String getValue() {

        return SYMBOL;
    }

    @Override
    public ActionType getType() {

        return TYPE;
    }
}

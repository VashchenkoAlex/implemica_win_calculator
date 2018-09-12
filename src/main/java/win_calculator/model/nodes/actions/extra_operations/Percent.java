package win_calculator.model.nodes.actions.extra_operations;

import win_calculator.model.nodes.actions.Action;
import win_calculator.utils.ActionType;

import java.math.BigDecimal;

public class Percent implements Action {

    private static final String VALUE = "";
    private static final BigDecimal PERCENT = BigDecimal.valueOf(0.01);
    private static final ActionType TYPE = ActionType.PERCENT;

    public BigDecimal calculate(BigDecimal firstNumber, BigDecimal secondNumber){

        return firstNumber.multiply(secondNumber.multiply(PERCENT));
    }

    public String getValue() {
        return VALUE;
    }

    @Override
    public ActionType getType() {

        return TYPE;
    }

}

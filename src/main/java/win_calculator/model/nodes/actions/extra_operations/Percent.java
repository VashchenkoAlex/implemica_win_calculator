package win_calculator.model.nodes.actions.extra_operations;

import win_calculator.model.nodes.actions.Action;
import win_calculator.utils.ActionType;

import java.math.BigDecimal;
import java.math.RoundingMode;

public class Percent implements Action {

    private static final String VALUE = "";
    private static final BigDecimal HUNDRED = BigDecimal.valueOf(100);
    private static final int SCALE = 10000;
    private static final ActionType TYPE = ActionType.PERCENT;

    public BigDecimal calculate(BigDecimal firstNumber, BigDecimal secondNumber){

        return firstNumber.multiply(secondNumber.divide(HUNDRED,SCALE,RoundingMode.HALF_DOWN));
    }

    public String getValue() {
        return VALUE;
    }

    @Override
    public ActionType getType() {

        return TYPE;
    }

    /*public BigDecimal calculate(BigDecimal number){

        return BigDecimal.ZERO;
    }*/


}

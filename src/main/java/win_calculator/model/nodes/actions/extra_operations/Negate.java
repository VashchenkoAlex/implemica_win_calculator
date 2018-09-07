package win_calculator.model.nodes.actions.extra_operations;

import win_calculator.utils.ActionType;

import java.math.BigDecimal;

public class Negate implements ExtraOperation {

    private static final ActionType TYPE = ActionType.NEGATE;
    private static final String VALUE = "negate( ";

    @Override
    public BigDecimal calculate(BigDecimal number){

        BigDecimal result;
        if (number.compareTo(BigDecimal.ZERO)>0){
            result = number.negate();
        }else {
            result = number.abs();
        }
        return result;
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

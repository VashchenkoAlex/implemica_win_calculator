package win_calculator.model.nodes.actions.main_operations;

import win_calculator.utils.ActionType;
import java.math.BigDecimal;

public class Multiply implements MainOperation {

    private static final String VALUE = "  \u00D7  ";
    private static final ActionType TYPE = ActionType.MAIN_OPERATION;

    @Override
    public BigDecimal calculate(BigDecimal number){

        return number.multiply(number);
    }

    @Override
    public BigDecimal calculate(BigDecimal firstNumber, BigDecimal secondNumber){

        return firstNumber.multiply(secondNumber);
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

package win_calculator.model.nodes.actions.main_operations;

import win_calculator.utils.ActionType;
import java.math.BigDecimal;

public class Subtract implements MainOperation {

    private static final String VALUE = "  -  ";
    private static final ActionType TYPE = ActionType.MAIN_OPERATION;

    @Override
    public BigDecimal calculate(BigDecimal number){

        return number.subtract(number);
    }

    @Override
    public BigDecimal calculate(BigDecimal firstNumber, BigDecimal secondNumber){

        return firstNumber.subtract(secondNumber);
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

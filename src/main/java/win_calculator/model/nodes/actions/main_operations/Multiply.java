package win_calculator.model.nodes.actions.main_operations;

import win_calculator.exceptions.MyException;
import win_calculator.utils.ActionType;

import java.math.BigDecimal;

import static win_calculator.utils.StringUtils.isOverflow;

public class Multiply implements MainOperation {

    private static final String VALUE = "  \u00D7  ";
    private static final ActionType TYPE = ActionType.MAIN_OPERATION;
    private static final int MAX_RATE = 9989;
    private static final String OVERFLOW_MSG = "Overflow";

    @Override
    public BigDecimal calculate(BigDecimal number) throws MyException {
        if (isOverflow(number.toString(),MAX_RATE)){
            throw new MyException(OVERFLOW_MSG);
        }
        return number.multiply(number);
    }

    @Override
    public BigDecimal calculate(BigDecimal firstNumber, BigDecimal secondNumber) throws MyException {

        if (isOverflow(firstNumber.toString(),MAX_RATE)){
            throw new MyException(OVERFLOW_MSG);
        }
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

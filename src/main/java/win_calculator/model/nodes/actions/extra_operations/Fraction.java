package win_calculator.model.nodes.actions.extra_operations;

import win_calculator.exceptions.MyException;
import win_calculator.utils.ActionType;

import java.math.BigDecimal;
import java.math.RoundingMode;

public class Fraction implements ExtraOperation {

    private static final String VALUE = "1/( ";
    private static final ActionType TYPE = ActionType.EXTRA_OPERATION;
    private static final int SCALE = 10000;
    private static final String EXCEPTION_MSG = "Cannot divide by zero";

    @Override
    public BigDecimal calculate(BigDecimal number) throws MyException {
        BigDecimal result;
        try{
            result = BigDecimal.ONE.divide(number,SCALE,RoundingMode.HALF_UP);
        }catch (ArithmeticException e){
            throw new MyException(EXCEPTION_MSG);
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

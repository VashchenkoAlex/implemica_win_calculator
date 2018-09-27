package win_calculator.model.nodes.events.extra_operations;

import win_calculator.model.exceptions.MyException;
import win_calculator.model.nodes.events.EventType;

import java.math.BigDecimal;
import java.math.RoundingMode;

public class Fraction implements ExtraOperation {

    private static final String VALUE = "1/( ";
    private static final EventType TYPE = EventType.EXTRA_OPERATION;
    private static final int SCALE_BEFORE = 10050;
    private static final int SCALE_AFTER = 10000;
    private static final String EXCEPTION_MSG = "Cannot divide by zero";

    @Override
    public BigDecimal calculate(BigDecimal number) throws MyException {
        BigDecimal result;
        try{
            result = BigDecimal.ONE.divide(number, SCALE_BEFORE,RoundingMode.HALF_UP).setScale(SCALE_AFTER,RoundingMode.HALF_UP);
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
    public EventType getType() {

        return TYPE;
    }
}

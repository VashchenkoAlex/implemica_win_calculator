package win_calculator.model.nodes.events.main_operations;

import win_calculator.model.exceptions.MyException;
import win_calculator.model.nodes.events.EventType;

import java.math.BigDecimal;
import java.math.RoundingMode;

public class Divide implements MainOperation {

    private static final String VALUE = "  \u00F7  ";
    private static final EventType TYPE = EventType.MAIN_OPERATION;
    private static final String CANNOT_DIVIDE_EXCEPTION_MSG = "Cannot divide by zero";
    private static final String UNDEFINED_EXCEPTION_MSG = "Result is undefined";
    private static final int SCALE = 10030;

    @Override
    public BigDecimal calculate(BigDecimal number) throws MyException {

        BigDecimal result;
        try{
            result = number.divide(number,SCALE,RoundingMode.HALF_DOWN);
        }catch (ArithmeticException e){
            throw new MyException(UNDEFINED_EXCEPTION_MSG);
        }
        return result;
    }

    @Override
    public BigDecimal calculate(BigDecimal firstNumber, BigDecimal secondNumber) throws MyException {

        BigDecimal result;
        if (firstNumber.equals(BigDecimal.ZERO) && secondNumber.equals(BigDecimal.ZERO)){
            throw new MyException(UNDEFINED_EXCEPTION_MSG);
        }
        try{
            result = firstNumber.divide(secondNumber,SCALE,RoundingMode.HALF_UP);
        }catch (ArithmeticException e){
            throw new MyException(CANNOT_DIVIDE_EXCEPTION_MSG);
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

package win_calculator.extra_operations;

import win_calculator.exceptions.MyException;

import java.math.BigDecimal;
import java.math.RoundingMode;

public class Fraction implements ExtraOperation {

    private static final String SYMBOL = "1/( ";
    private static final int SCALE = 16;
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
    public String getSymbol() {
        return SYMBOL;
    }
}

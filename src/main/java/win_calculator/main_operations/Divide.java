package win_calculator.main_operations;

import win_calculator.exceptions.MyException;

import java.math.BigDecimal;

public class Divide implements MainOperation {

    private static final String VALUE = " \u00F7 ";
    private static final String NAME = "DIVIDE";

    @Override
    public BigDecimal calculate(BigDecimal number) throws MyException {

        BigDecimal result;
        try{
            result = number.divide(number);
        }catch (ArithmeticException e){
            throw new MyException("Cannot divide by zero");
        }
        return result;
    }

    @Override
    public BigDecimal calculate(BigDecimal firstNumber, BigDecimal secondNumber) throws MyException {

        BigDecimal result;
        try{
            result = firstNumber.divide(secondNumber);
        }catch (ArithmeticException e){
            throw new MyException("Cannot divide by zero");
        }
        return result;
    }

    @Override
    public String getValue() {
        return VALUE;
    }

    @Override
    public String getName() {
        return NAME;
    }
}

package win_calculator.main_operations;

import win_calculator.exceptions.MyException;

import java.math.BigDecimal;

public interface MainOperation {

    BigDecimal calculate(BigDecimal firstNumber, BigDecimal secondNumber) throws MyException;

    BigDecimal calculate(BigDecimal number) throws MyException;

    String getValue();
}

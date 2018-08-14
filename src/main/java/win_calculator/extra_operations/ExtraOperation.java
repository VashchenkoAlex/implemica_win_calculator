package win_calculator.extra_operations;

import win_calculator.exceptions.MyException;

import java.math.BigDecimal;

public interface ExtraOperation {

    BigDecimal calculate(BigDecimal number) throws MyException;

    String getSymbol();
}

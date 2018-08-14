package win_calculator.extra_operations;

import win_calculator.exceptions.MyException;

import java.math.BigDecimal;

public class Sqr implements ExtraOperation {

    private static final String SYMBOL = "sqr( ";
    private static final int POW = 2;
    @Override
    public BigDecimal calculate(BigDecimal number){

        return number.pow(POW);
    }

    @Override
    public String getSymbol() {

        return SYMBOL;
    }
}

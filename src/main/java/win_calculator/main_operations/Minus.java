package win_calculator.main_operations;

import java.math.BigDecimal;

public class Minus implements MainOperation {

    private static final String VALUE = " - ";

    @Override
    public BigDecimal calculate(BigDecimal number) {
        return number.subtract(number);
    }

    @Override
    public BigDecimal calculate(BigDecimal firstNumber, BigDecimal secondNumber) {

        return firstNumber.subtract(secondNumber);
    }

    @Override
    public String getValue() {
        return VALUE;
    }

}

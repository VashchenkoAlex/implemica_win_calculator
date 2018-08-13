package win_calculator.main_operations;

import java.math.BigDecimal;

public class Multiply implements MainOperation {

    private static final String VALUE = " \u00D7 ";

    @Override
    public BigDecimal calculate(BigDecimal number) {
        return number.multiply(number);
    }

    @Override
    public BigDecimal calculate(BigDecimal firstNumber, BigDecimal secondNumber) {

        return firstNumber.multiply(secondNumber);
    }

    @Override
    public String getValue() {
        return VALUE;
    }


}

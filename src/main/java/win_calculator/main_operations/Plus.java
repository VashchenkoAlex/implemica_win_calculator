package win_calculator.main_operations;

import java.math.BigDecimal;

public class Plus implements MainOperation{

    private static final String VALUE = " + ";
    private static final String NAME = "PLUS";

    @Override
    public BigDecimal calculate(BigDecimal number) {
        return number.add(number);
    }

    @Override
    public BigDecimal calculate(BigDecimal firstNumber, BigDecimal secondNumber) {

        return firstNumber.add(secondNumber);
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

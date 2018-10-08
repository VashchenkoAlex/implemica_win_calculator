package win_calculator.model.operations.extra_operations;

import win_calculator.model.operations.Operation;
import win_calculator.model.operations.OperationType;

import java.math.BigDecimal;

public class Percent implements Operation {

    private static final String VALUE = "";
    private static final BigDecimal PERCENT = BigDecimal.valueOf(0.01);
    private static final OperationType TYPE = OperationType.PERCENT;

    public BigDecimal calculate(BigDecimal firstNumber, BigDecimal secondNumber){

        return firstNumber.multiply(secondNumber.multiply(PERCENT));
    }

    public String getValue() {
        return VALUE;
    }

    @Override
    public OperationType getType() {

        return TYPE;
    }

}

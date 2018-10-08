package win_calculator.model.operations;

import java.math.BigDecimal;

public class Number implements Operation {

    private BigDecimal bigDecimalValue;
    private static final OperationType TYPE = OperationType.NUMBER;

    public Number(BigDecimal bigDecimalValue) {
        this.bigDecimalValue = bigDecimalValue;
    }

    @Override
    public OperationType getType() {
        return TYPE;
    }

    public BigDecimal getBigDecimalValue(){

        return bigDecimalValue;
    }

}

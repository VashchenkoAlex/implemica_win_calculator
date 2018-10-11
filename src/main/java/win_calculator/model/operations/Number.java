package win_calculator.model.operations;

import java.math.BigDecimal;

/**
 * Entity class of number operation
 */
public class Number implements Operation {

    /**
     * Stores BigDecimal value of current number
     */
    private BigDecimal bigDecimalValue;

    /**
     * Constructs {@link Number} with given BigDecimal value
     * @param bigDecimalValue - given BigDecimal value
     */
    public Number(BigDecimal bigDecimalValue) {
        this.bigDecimalValue = bigDecimalValue;
    }

    @Override
    public OperationType getType() {
        return OperationType.NUMBER;
    }

    /**
     * Getter for the bigDecimalValue;
     * @return BigDecimal value of current number
     */
    public BigDecimal getValue(){
        return bigDecimalValue;
    }

}

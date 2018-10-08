package win_calculator.model.operations.extra_operations;

import win_calculator.model.operations.OperationKind;
import win_calculator.model.operations.OperationType;

import java.math.BigDecimal;

public class Negate implements ExtraOperation {

    private static final OperationType TYPE = OperationType.NEGATE;
    private static final OperationKind kind = OperationKind.FRACTION;

    @Override
    public BigDecimal calculate(BigDecimal number){

        return number.negate();
    }

    @Override
    public OperationType getType() {
        return TYPE;
    }

    @Override
    public OperationKind getKind() {

        return kind;
    }
}

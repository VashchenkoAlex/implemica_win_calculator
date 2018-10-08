package win_calculator.model.operations.extra_operations;

import win_calculator.model.operations.OperationKind;
import win_calculator.model.operations.OperationType;

import java.math.BigDecimal;
import java.math.RoundingMode;

public class Sqr implements ExtraOperation {

    private static final int POW = 2;
    private static final OperationType TYPE = OperationType.EXTRA_OPERATION;
    private static final OperationKind kind = OperationKind.SQR;

    @Override
    public BigDecimal calculate(BigDecimal number){

        return number.setScale(10050,RoundingMode.HALF_UP).pow(POW);
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

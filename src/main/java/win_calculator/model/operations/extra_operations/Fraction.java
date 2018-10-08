package win_calculator.model.operations.extra_operations;

import win_calculator.model.exceptions.OperationException;
import win_calculator.model.operations.OperationKind;
import win_calculator.model.operations.OperationType;

import java.math.BigDecimal;
import java.math.RoundingMode;

import static win_calculator.model.exceptions.ExceptionType.DIVIDE_BY_ZERO;

public class Fraction implements ExtraOperation {

    private static final OperationType TYPE = OperationType.EXTRA_OPERATION;
    private static final OperationKind kind = OperationKind.FRACTION;
    private static final int SCALE = 10000;

    @Override
    public BigDecimal calculate(BigDecimal number) throws OperationException {

        try{
            return BigDecimal.ONE.divide(number, SCALE,RoundingMode.HALF_UP);
        }catch (ArithmeticException e){
            throw new OperationException(DIVIDE_BY_ZERO);
        }
    }

    @Override
    public OperationType getType() {

        return TYPE;
    }

    @Override
    public OperationKind getKind(){

        return kind;
    }
}

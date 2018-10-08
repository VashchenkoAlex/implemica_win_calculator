package win_calculator.model.operations.main_operations;

import win_calculator.model.operations.OperationKind;
import win_calculator.model.operations.OperationType;

import java.math.BigDecimal;

public class Multiply implements MainOperation {

    private static final OperationType TYPE = OperationType.MAIN_OPERATION;
    private static final OperationKind KIND = OperationKind.MULTIPLY;

    @Override
    public BigDecimal calculate(BigDecimal number){

        return number.multiply(number);
    }

    @Override
    public BigDecimal calculate(BigDecimal firstNumber, BigDecimal secondNumber){

        return firstNumber.multiply(secondNumber);
    }

    @Override
    public OperationKind getKind() {
        return KIND;
    }

    @Override
    public OperationType getType() {
        return TYPE;
    }


}

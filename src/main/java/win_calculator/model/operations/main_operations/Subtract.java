package win_calculator.model.operations.main_operations;

import win_calculator.model.operations.OperationKind;
import win_calculator.model.operations.OperationType;

import java.math.BigDecimal;

public class Subtract implements MainOperation {

    private static final OperationType TYPE = OperationType.MAIN_OPERATION;
    private static final OperationKind kind = OperationKind.SUBTRACT;

    @Override
    public BigDecimal calculate(BigDecimal number){

        return number.subtract(number);
    }

    @Override
    public BigDecimal calculate(BigDecimal firstNumber, BigDecimal secondNumber){

        return firstNumber.subtract(secondNumber);
    }

    @Override
    public OperationKind getKind() {
        return kind;
    }

    @Override
    public OperationType getType() {
        return TYPE;
    }

}

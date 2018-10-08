package win_calculator.model.operations.main_operations;

import win_calculator.model.operations.OperationKind;
import win_calculator.model.operations.OperationType;

import java.math.BigDecimal;


public class Add implements MainOperation {

    private static final OperationType TYPE = OperationType.MAIN_OPERATION;
    private static final OperationKind KIND = OperationKind.ADD;

    @Override
    public BigDecimal calculate(BigDecimal number){

        return number.add(number);
    }

    @Override
    public BigDecimal calculate(BigDecimal firstNumber, BigDecimal secondNumber){

        return firstNumber.add(secondNumber);
    }

    @Override
    public OperationType getType() {
        return TYPE;
    }

    @Override
    public OperationKind getKind() {
        return KIND;
    }
}

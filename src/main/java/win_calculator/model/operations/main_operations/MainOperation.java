package win_calculator.model.operations.main_operations;

import win_calculator.model.operations.Operation;
import win_calculator.model.exceptions.OperationException;
import win_calculator.model.operations.OperationKind;

import java.math.BigDecimal;

public interface MainOperation extends Operation {

    BigDecimal calculate(BigDecimal firstNumber, BigDecimal secondNumber) throws OperationException;

    BigDecimal calculate(BigDecimal number) throws OperationException;

    String getValue();

    OperationKind getKind();
}

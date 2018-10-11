package win_calculator.model.operations.binary_operations;

import win_calculator.model.operations.Operation;
import win_calculator.model.exceptions.OperationException;

import java.math.BigDecimal;

/**
 * Interface class for the binary operations at the {@link win_calculator.model.CalcModel}
 */
public interface BinaryOperation extends Operation {

    BigDecimal calculate(BigDecimal firstNumber, BigDecimal secondNumber) throws OperationException;

    BigDecimal calculate(BigDecimal number) throws OperationException;
}

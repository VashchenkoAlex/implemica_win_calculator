package win_calculator.model.operations.extra_operations;

import win_calculator.model.operations.Operation;
import win_calculator.model.operations.OperationType;
import win_calculator.model.exceptions.OperationException;

import java.math.BigDecimal;

/**
 * Interface class for the extra operations at the {@link win_calculator.model.CalcModel}
 */
public interface ExtraOperation extends Operation {

   BigDecimal calculate(BigDecimal number) throws OperationException;

   OperationType getType();
}

package win_calculator.model.operations.clear;

import win_calculator.model.operations.Operation;
import win_calculator.model.operations.OperationType;

/**
 * Entity class for clear operation at {@link win_calculator.model.CalcModel}
 */
public class Clear implements Operation {

    @Override
    public OperationType getType() {
        return OperationType.CLEAR;
    }
}

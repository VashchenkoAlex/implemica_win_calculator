package win_calculator.model.operations.clear;

import win_calculator.model.operations.Operation;
import win_calculator.model.operations.OperationType;

public class ClearEntered implements Operation {

    private static final OperationType TYPE = OperationType.CLEAR_ENTERED;

    @Override
    public OperationType getType() {
        return TYPE;
    }
}

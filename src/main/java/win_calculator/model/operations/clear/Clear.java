package win_calculator.model.operations.clear;

import win_calculator.model.operations.Operation;
import win_calculator.model.operations.OperationType;

public class Clear implements Operation {

    private static final OperationType TYPE = OperationType.CLEAR;

    @Override
    public OperationType getType() {
        return TYPE;
    }
}

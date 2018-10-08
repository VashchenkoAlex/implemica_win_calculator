package win_calculator.model.operations.clear;

import win_calculator.model.operations.Operation;
import win_calculator.model.operations.OperationType;

public class BaskSpace implements Operation {

    private static final OperationType TYPE = OperationType.BACKSPACE;

    @Override
    public OperationType getType() {
        return TYPE;
    }
}

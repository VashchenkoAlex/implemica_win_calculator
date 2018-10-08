package win_calculator.model.operations.enter;

import win_calculator.model.operations.Operation;
import win_calculator.model.operations.OperationType;

public class Equal implements Operation {

    private static final OperationType TYPE = OperationType.EQUAL;

    @Override
    public OperationType getType() {

        return TYPE;
    }
}

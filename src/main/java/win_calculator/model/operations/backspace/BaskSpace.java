package win_calculator.model.operations.backspace;

import win_calculator.model.operations.Operation;
import win_calculator.model.operations.OperationType;

/**
 * Entity class for backspace operation at {@link win_calculator.model.CalcModel}
 */
public class BaskSpace implements Operation {

    @Override
    public OperationType getType() {
        return OperationType.BACKSPACE;
    }
}

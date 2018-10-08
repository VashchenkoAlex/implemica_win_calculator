package win_calculator.model.memory;

import win_calculator.model.operations.Operation;
import win_calculator.model.operations.OperationType;

public interface MemoryOperation extends Operation {

    MemoryType getMemoryType();
    OperationType getType();
}

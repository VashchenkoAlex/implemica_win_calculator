package win_calculator.model.memory;

import win_calculator.model.operations.OperationType;

import static win_calculator.model.memory.MemoryType.CLEAR_MEMORY;

public class ClearMemory implements MemoryOperation {

    private static final OperationType TYPE = OperationType.MEMORY;
    private static final MemoryType MEMORY_TYPE = CLEAR_MEMORY;

    @Override
    public MemoryType getMemoryType() {
        return MEMORY_TYPE;
    }

    @Override
    public OperationType getType() {
        return TYPE;
    }
}

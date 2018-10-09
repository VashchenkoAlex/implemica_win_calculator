package win_calculator.model.operations.memory_operations;

import win_calculator.model.operations.OperationType;

import static win_calculator.model.operations.memory_operations.MemoryType.SUBTRACT_FROM_MEMORY;

public class SubtractMemory implements MemoryOperation {

    private static final OperationType TYPE = OperationType.MEMORY;
    private static final MemoryType MEMORY_TYPE = SUBTRACT_FROM_MEMORY;

    @Override
    public MemoryType getMemoryType() {
        return MEMORY_TYPE;
    }

    @Override
    public OperationType getType() {
        return TYPE;
    }
}

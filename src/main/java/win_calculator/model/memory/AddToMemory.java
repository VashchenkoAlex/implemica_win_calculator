package win_calculator.model.memory;

import win_calculator.model.operations.OperationType;

import static win_calculator.model.memory.MemoryType.ADD_TO_MEMORY;

public class AddToMemory implements MemoryOperation {

    private static final OperationType TYPE = OperationType.MEMORY;
    private static final MemoryType MEMORY_TYPE = ADD_TO_MEMORY;

    @Override
    public MemoryType getMemoryType() {
        return MEMORY_TYPE;
    }

    @Override
    public OperationType getType() {
        return TYPE;
    }
}

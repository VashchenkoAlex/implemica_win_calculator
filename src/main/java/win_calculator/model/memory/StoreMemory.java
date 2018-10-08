package win_calculator.model.memory;

import win_calculator.model.operations.OperationType;

import static win_calculator.model.memory.MemoryType.STORE;

public class StoreMemory implements MemoryOperation {

    private static final OperationType TYPE = OperationType.MEMORY;
    private static final MemoryType MEMORY_TYPE = STORE;

    @Override
    public MemoryType getMemoryType() {
        return MEMORY_TYPE;
    }

    @Override
    public OperationType getType() {
        return TYPE;
    }
}

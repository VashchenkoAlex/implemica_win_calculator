package win_calculator.model.memory;

import win_calculator.model.operations.OperationType;

import static win_calculator.model.memory.MemoryType.RECALL;

public class RecallMemory implements MemoryOperation {

    private static final OperationType TYPE = OperationType.MEMORY;
    private static final MemoryType MEMORY_TYPE = RECALL;

    @Override
    public MemoryType getMemoryType() {
        return MEMORY_TYPE;
    }

    @Override
    public OperationType getType() {
        return TYPE;
    }


}

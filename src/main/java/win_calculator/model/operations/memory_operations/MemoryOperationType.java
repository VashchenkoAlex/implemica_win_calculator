package win_calculator.model.operations.memory_operations;

/**
 * Enumerate possible memory operations types at {@link win_calculator.model.CalcModel}
 */
public enum MemoryOperationType {

    /**
     * Marker for add number to memory operation
     */
    ADD_TO_MEMORY,
    /**
     * Marker for clear memory operation
     */
    CLEAR_MEMORY,
    /**
     * Marker for store number to memory operation
     */
    STORE,
    /**
     * Marker for recall number from memory operation
     */
    RECALL,
    /**
     * Marker for subtract number from memory operation
     */
    SUBTRACT_FROM_MEMORY,

}

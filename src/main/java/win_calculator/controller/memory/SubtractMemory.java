package win_calculator.controller.memory;

import win_calculator.model.nodes.actions.ActionType;

import static win_calculator.controller.memory.MemoryType.SUBTRACT_FROM_MEMORY;

public class SubtractMemory implements MemoryAction {

    private static final ActionType TYPE = ActionType.MEMORY;
    private static final MemoryType MEMORY_TYPE = SUBTRACT_FROM_MEMORY;

    @Override
    public MemoryType getMemoryType() {
        return MEMORY_TYPE;
    }

    @Override
    public ActionType getType() {
        return TYPE;
    }
}

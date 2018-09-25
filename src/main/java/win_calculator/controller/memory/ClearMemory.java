package win_calculator.controller.memory;

import win_calculator.model.nodes.actions.ActionType;

import static win_calculator.controller.memory.MemoryType.CLEAR;

public class ClearMemory implements MemoryAction {

    private static final ActionType TYPE = ActionType.MEMORY;
    private static final MemoryType MEMORY_TYPE = CLEAR;

    @Override
    public MemoryType getMemoryType() {
        return MEMORY_TYPE;
    }

    @Override
    public ActionType getType() {
        return TYPE;
    }
}

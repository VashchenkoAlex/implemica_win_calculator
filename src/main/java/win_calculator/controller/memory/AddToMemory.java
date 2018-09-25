package win_calculator.controller.memory;

import win_calculator.model.nodes.actions.ActionType;

import static win_calculator.controller.memory.MemoryType.ADD_TO_MEMORY;

public class AddToMemory implements MemoryAction {

    private static final ActionType TYPE = ActionType.MEMORY;
    private static final MemoryType MEMORY_TYPE = ADD_TO_MEMORY;

    @Override
    public MemoryType getMemoryType() {
        return MEMORY_TYPE;
    }

    @Override
    public ActionType getType() {
        return TYPE;
    }
}

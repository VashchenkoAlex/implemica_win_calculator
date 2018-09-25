package win_calculator.controller.memory;

import win_calculator.model.nodes.actions.ActionType;

import static win_calculator.controller.memory.MemoryType.RECALL;

public class RecallMemory implements MemoryAction {

    private static final ActionType TYPE = ActionType.MEMORY;
    private static final MemoryType MEMORY_TYPE = RECALL;

    @Override
    public MemoryType getMemoryType() {
        return MEMORY_TYPE;
    }

    @Override
    public ActionType getType() {
        return TYPE;
    }


}

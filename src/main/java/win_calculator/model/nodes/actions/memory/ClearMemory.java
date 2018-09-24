package win_calculator.model.nodes.actions.memory;

import win_calculator.utils.ActionType;
import win_calculator.utils.MemoryType;

import static win_calculator.utils.MemoryType.CLEAR;

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

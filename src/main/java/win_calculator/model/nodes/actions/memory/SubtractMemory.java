package win_calculator.model.nodes.actions.memory;

import win_calculator.utils.ActionType;
import win_calculator.utils.MemoryType;

import static win_calculator.utils.MemoryType.SUBTRACT_FROM_MEMORY;

public class SubtractMemory implements MemoryAction {

    private static final String VALUE = " M- ";
    private static final ActionType TYPE = ActionType.MEMORY;
    private static final MemoryType MEMORY_TYPE = SUBTRACT_FROM_MEMORY;

    @Override
    public MemoryType getMemoryType() {
        return MEMORY_TYPE;
    }

    @Override
    public String getValue() {
        return VALUE;
    }

    @Override
    public ActionType getType() {
        return TYPE;
    }
}

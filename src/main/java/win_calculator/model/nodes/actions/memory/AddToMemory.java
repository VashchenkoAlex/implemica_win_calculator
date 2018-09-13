package win_calculator.model.nodes.actions.memory;

import win_calculator.utils.ActionType;
import win_calculator.utils.MemoryType;

import static win_calculator.utils.MemoryType.ADD_TO_MEMORY;

public class AddToMemory implements MemoryAction {

    private static final String VALUE = " M+ ";
    private static final ActionType TYPE = ActionType.MEMORY;
    private static final MemoryType MEMORY_TYPE = ADD_TO_MEMORY;

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

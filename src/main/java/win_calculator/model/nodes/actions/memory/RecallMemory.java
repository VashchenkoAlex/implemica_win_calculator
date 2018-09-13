package win_calculator.model.nodes.actions.memory;

import win_calculator.utils.ActionType;
import win_calculator.utils.MemoryType;

import static win_calculator.utils.MemoryType.RECALL;
import static win_calculator.utils.MemoryType.STORE;

public class RecallMemory implements MemoryAction {

    private static final String VALUE = " MR ";
    private static final ActionType TYPE = ActionType.MEMORY;
    private static final MemoryType MEMORY_TYPE = RECALL;

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

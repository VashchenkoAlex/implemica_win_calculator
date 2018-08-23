package win_calculator.model.nodes.actions.memory;

import win_calculator.model.nodes.actions.Action;
import win_calculator.utils.ActionType;
import win_calculator.utils.MemoryType;

public interface MemoryAction extends Action {

    MemoryType getMemoryType();
    ActionType getType();
}

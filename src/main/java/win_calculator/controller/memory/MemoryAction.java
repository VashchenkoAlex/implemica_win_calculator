package win_calculator.controller.memory;

import win_calculator.model.nodes.actions.Action;
import win_calculator.model.nodes.actions.ActionType;

public interface MemoryAction extends Action {

    MemoryType getMemoryType();
    ActionType getType();
}

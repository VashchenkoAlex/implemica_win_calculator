package win_calculator.controller.view_handlers;

import win_calculator.model.nodes.Memory;
import win_calculator.model.nodes.actions.digits.Number;
import win_calculator.model.nodes.actions.memory.MemoryAction;
import win_calculator.utils.MemoryType;

import static win_calculator.utils.MemoryType.*;

public class MemoryHandler {
    private Memory memory = new Memory();
    public Number doAction(MemoryAction action,Number number){

        MemoryType memoryType = action.getMemoryType();
        Number result = null;
        if (ADD_TO_MEMORY.equals(memoryType)){
            memory.addToStoredNumber(number);
        }else if (CLEAR.equals(memoryType)){
            memory = new Memory();
        }else if (STORE.equals(memoryType)){
            memory.storeNumber(number);
        }else if (RECALL.equals(memoryType)){
            result = memory.getStoredNumber();
        }else if (SUBTRACT_FROM_MEMORY.equals(memoryType)){
            memory.subtractFromStoredNumber(number);
        }
        return result;
    }
}

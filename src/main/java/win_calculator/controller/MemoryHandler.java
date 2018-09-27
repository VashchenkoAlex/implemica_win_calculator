package win_calculator.controller;

import win_calculator.controller.memory.Memory;
import win_calculator.controller.memory.MemoryEvent;
import win_calculator.controller.memory.MemoryType;

import java.math.BigDecimal;

import static win_calculator.controller.memory.MemoryType.*;

class MemoryHandler {
    private Memory memory = new Memory();
    BigDecimal doEvent(MemoryEvent event, BigDecimal number){

        MemoryType memoryType = event.getMemoryType();
        BigDecimal result = null;
        if (ADD_TO_MEMORY.equals(memoryType)){
            memory.addToStoredNumber(number);
        }else if (CLEAR_MEMORY.equals(memoryType)){
            memory.clear();
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

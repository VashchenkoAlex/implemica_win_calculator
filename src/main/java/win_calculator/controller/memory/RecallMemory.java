package win_calculator.controller.memory;

import win_calculator.model.nodes.events.EventType;

import static win_calculator.controller.memory.MemoryType.RECALL;

public class RecallMemory implements MemoryEvent {

    private static final EventType TYPE = EventType.MEMORY;
    private static final MemoryType MEMORY_TYPE = RECALL;

    @Override
    public MemoryType getMemoryType() {
        return MEMORY_TYPE;
    }

    @Override
    public EventType getType() {
        return TYPE;
    }


}

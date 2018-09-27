package win_calculator.controller.memory;

import win_calculator.model.nodes.events.Event;
import win_calculator.model.nodes.events.EventType;

public interface MemoryEvent extends Event {

    MemoryType getMemoryType();
    EventType getType();
}

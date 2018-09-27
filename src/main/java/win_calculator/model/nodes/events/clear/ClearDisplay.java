package win_calculator.model.nodes.events.clear;

import win_calculator.model.nodes.events.Event;
import win_calculator.model.nodes.events.EventType;

public class ClearDisplay implements Event {

    private static final EventType TYPE = EventType.CLEAR_ENTERED;

    @Override
    public EventType getType() {
        return TYPE;
    }
}

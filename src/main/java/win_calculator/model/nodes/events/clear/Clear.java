package win_calculator.model.nodes.events.clear;

import win_calculator.model.nodes.events.Event;
import win_calculator.model.nodes.events.EventType;

public class Clear implements Event {

    private static final EventType TYPE = EventType.CLEAR;

    @Override
    public EventType getType() {
        return TYPE;
    }
}

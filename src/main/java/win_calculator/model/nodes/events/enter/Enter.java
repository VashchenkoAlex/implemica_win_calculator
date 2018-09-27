package win_calculator.model.nodes.events.enter;

import win_calculator.model.nodes.events.Event;
import win_calculator.model.nodes.events.EventType;

public class Enter implements Event {

    private static final EventType TYPE = EventType.ENTER;

    @Override
    public EventType getType() {

        return TYPE;
    }
}

package win_calculator.model.nodes;

import win_calculator.model.nodes.events.Event;
import win_calculator.model.nodes.events.Number;
import win_calculator.model.nodes.events.EventType;

import java.util.LinkedList;

import static win_calculator.model.nodes.events.EventType.MAIN_OPERATION;
import static win_calculator.model.nodes.events.EventType.NUMBER;

public class History {

    private LinkedList<Event> events;

    public History() {

        events = new LinkedList<>();
    }

    public void addEvent(Event event) {

        events.add(event);
    }

    public void setEvents(LinkedList<Event> events) {

        this.events = events;
    }

    public LinkedList<Event> getEvents() {

        return events;
    }

    private Event getLastEvent() {

            return events.getLast();
    }

    public void changeLastMOperation(Event event) {

        if (!events.isEmpty() && isChangingMOperationPossible()) {
            for (int i = events.size() - 1; i > 0; i--) {
                if (MAIN_OPERATION.equals(events.get(i).getType())) {
                    events.set(i, event);
                    break;
                }
            }
        } else {
            events.add(event);
        }
    }

    public void changeLastNumber(Number number) {

        if (isChangingNumberPossible()) {
            for (int i = events.size() - 1; i >= 0; i--) {
                if (NUMBER.equals(events.get(i).getType())) {
                    events.set(i, number);
                    break;
                }
            }
        } else {
            events.add(number);
        }

    }

    private boolean isChangingNumberPossible() {

        boolean result = false;
        for (Event event : events) {
            if (NUMBER.equals(event.getType())) {
                result = true;
                break;
            }
        }
        result = result && !MAIN_OPERATION.equals(getLastEvent().getType());
        return result;
    }

    private boolean isChangingMOperationPossible() {

        boolean result = false;
        for (Event event : events) {
            if (MAIN_OPERATION.equals(event.getType())) {
                result = true;
                break;
            }
        }
        return result;
    }

    public void changeNumberAtFirstPosition(Number number) {

        if (NUMBER.equals(events.getFirst().getType())) {
            events.set(0, number);
        }
    }

    public boolean isContain(EventType expectedType){

        boolean result = false;
        EventType type;
        if (!events.isEmpty()) {
            for (int i = events.size() - 1; i > 0; i--) {
                type = events.get(i).getType();
                if (expectedType.equals(type)){
                    result = true;
                }
                if (MAIN_OPERATION.equals(type)){
                    break;
                }
            }
        }
        return result;
    }
}

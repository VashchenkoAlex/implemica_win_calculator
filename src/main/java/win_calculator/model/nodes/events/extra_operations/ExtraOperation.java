package win_calculator.model.nodes.events.extra_operations;

import win_calculator.model.exceptions.MyException;
import win_calculator.model.nodes.events.Event;
import win_calculator.model.nodes.events.EventType;

import java.math.BigDecimal;

public interface ExtraOperation extends Event {

    BigDecimal calculate(BigDecimal number) throws MyException;

    String getValue();
    EventType getType();
}

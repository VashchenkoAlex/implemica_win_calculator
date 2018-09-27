package win_calculator.model.nodes.events.extra_operations;

import win_calculator.model.nodes.events.Event;
import win_calculator.model.nodes.events.EventType;

import java.math.BigDecimal;

public class Percent implements Event {

    private static final String VALUE = "";
    private static final BigDecimal PERCENT = BigDecimal.valueOf(0.01);
    private static final EventType TYPE = EventType.PERCENT;

    public BigDecimal calculate(BigDecimal firstNumber, BigDecimal secondNumber){

        return firstNumber.multiply(secondNumber.multiply(PERCENT));
    }

    public String getValue() {
        return VALUE;
    }

    @Override
    public EventType getType() {

        return TYPE;
    }

}

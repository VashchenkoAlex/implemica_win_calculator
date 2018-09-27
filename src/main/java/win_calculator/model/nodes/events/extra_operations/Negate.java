package win_calculator.model.nodes.events.extra_operations;

import win_calculator.model.nodes.events.EventType;

import java.math.BigDecimal;

public class Negate implements ExtraOperation {

    private static final EventType TYPE = EventType.NEGATE;
    private static final String VALUE = "negate( ";

    @Override
    public BigDecimal calculate(BigDecimal number){

        BigDecimal result;
        if (number.compareTo(BigDecimal.ZERO)>0){
            result = number.negate();
        }else {
            result = number.abs();
        }
        return result;
    }

    @Override
    public String getValue() {
        return VALUE;
    }

    @Override
    public EventType getType() {
        return TYPE;
    }
}

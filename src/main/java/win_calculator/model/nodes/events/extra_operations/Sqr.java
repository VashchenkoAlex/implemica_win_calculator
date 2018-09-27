package win_calculator.model.nodes.events.extra_operations;

import win_calculator.model.nodes.events.EventType;

import java.math.BigDecimal;
import java.math.RoundingMode;

public class Sqr implements ExtraOperation {

    private static final String SYMBOL = "sqr( ";
    private static final int POW = 2;
    private static final EventType TYPE = EventType.EXTRA_OPERATION;

    @Override
    public BigDecimal calculate(BigDecimal number){

        return number.setScale(10050,RoundingMode.HALF_UP).pow(POW);
    }

    @Override
    public String getValue() {

        return SYMBOL;
    }

    @Override
    public EventType getType() {

        return TYPE;
    }
}

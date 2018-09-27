package win_calculator.model.nodes.events;

import java.math.BigDecimal;

public class Number implements Event {

    private BigDecimal bigDecimalValue;
    private static final EventType TYPE = EventType.NUMBER;

    public Number(BigDecimal bigDecimalValue) {
        this.bigDecimalValue = bigDecimalValue;
    }

    public Number() {
    }


    public String getValue() {

        return bigDecimalValue.toString();
    }

    @Override
    public EventType getType() {
        return TYPE;
    }

    public BigDecimal getBigDecimalValue(){

        return bigDecimalValue;
    }

    public void setBigDecimalValue(BigDecimal bigDecimalValue) {
        this.bigDecimalValue = bigDecimalValue;
    }
}

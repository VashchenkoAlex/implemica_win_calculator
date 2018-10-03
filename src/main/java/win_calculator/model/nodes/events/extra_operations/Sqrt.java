package win_calculator.model.nodes.events.extra_operations;

import win_calculator.model.exceptions.NegativeValueForSQRTException;
import win_calculator.model.nodes.events.EventType;

import java.math.BigDecimal;
import java.math.RoundingMode;

public class Sqrt implements ExtraOperation {

    private static final String SYMBOL = "√( ";//"\uE94B(";
    private static final EventType TYPE = EventType.EXTRA_OPERATION;
    private static final int SCALE = 10050;
    @Override
    public BigDecimal calculate(BigDecimal number) throws NegativeValueForSQRTException {

        BigDecimal x0 = BigDecimal.ZERO;
        BigDecimal x1;
        try{
            x1 = new BigDecimal(Math.sqrt(number.doubleValue()));
        }catch (NumberFormatException e){
            throw new NegativeValueForSQRTException();
        }
        while (!x0.equals(x1)){
            x0 = x1;
            x1 = number.divide(x0,SCALE,RoundingMode.HALF_UP);
            x1 = x1.add(x0);
            x1 = x1.divide(BigDecimal.valueOf(2),SCALE,RoundingMode.HALF_UP);
        }
        return x1;
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

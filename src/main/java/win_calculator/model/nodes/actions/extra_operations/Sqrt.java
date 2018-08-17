package win_calculator.model.nodes.actions.extra_operations;

import win_calculator.exceptions.MyException;
import win_calculator.utils.ActionType;

import java.math.BigDecimal;
import java.math.RoundingMode;

public class Sqrt implements ExtraOperation {

    private static final String SYMBOL = "√( ";//"\uE94B(";
    private static final ActionType TYPE = ActionType.EXTRA_OPERATION;
    private static final String EXCEPTION_MSG = "Invalid input";
    private static final int SCALE = 15;
    @Override
    public BigDecimal calculate(BigDecimal number) throws MyException {

        BigDecimal x0 = BigDecimal.ZERO;
        BigDecimal x1;
        try{
            x1 = new BigDecimal(Math.sqrt(number.doubleValue()));
        }catch (NumberFormatException e){
            throw new MyException(EXCEPTION_MSG);
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
    public ActionType getType() {

        return TYPE;
    }
}

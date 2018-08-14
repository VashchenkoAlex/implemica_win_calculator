package win_calculator.extra_operations;

import java.math.BigDecimal;
import java.math.RoundingMode;

public class Percent{

    private static final String VALUE = " % ";
    private static final BigDecimal HUNDRED = BigDecimal.valueOf(100);
    private static final int SCALE = 16;

    public BigDecimal calculate(BigDecimal firstNumber, BigDecimal secondNumber){

        return firstNumber.multiply(secondNumber.divide(HUNDRED,SCALE,RoundingMode.HALF_DOWN));
    }

    /*public BigDecimal calculate(BigDecimal number){

        return BigDecimal.ZERO;
    }*/
}

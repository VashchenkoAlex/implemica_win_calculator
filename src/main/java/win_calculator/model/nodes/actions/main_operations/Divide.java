package win_calculator.model.nodes.actions.main_operations;

import win_calculator.exceptions.MyException;
import win_calculator.utils.ActionType;

import java.math.BigDecimal;
import java.math.RoundingMode;

import static win_calculator.utils.StringUtils.isOverflow;

public class Divide implements MainOperation {

    private static final String VALUE = "  \u00F7  ";
    private static final ActionType TYPE = ActionType.MAIN_OPERATION;
    private static final String EXCEPTION_MSG = "Cannot divide by zero";
    private static final int SCALE = 10000;
    private static final int MAX_RATE = 9989;
    private static final String OVERFLOW_MSG = "Overflow";

    @Override
    public BigDecimal calculate(BigDecimal number) throws MyException {

        BigDecimal result;
        try{
            result = number.divide(number,SCALE,RoundingMode.HALF_DOWN);
        }catch (ArithmeticException e){
            throw new MyException(EXCEPTION_MSG);
        }
        return result;
    }

    @Override
    public BigDecimal calculate(BigDecimal firstNumber, BigDecimal secondNumber) throws MyException {

        BigDecimal result;
        if (isOverflow(firstNumber.toString(),MAX_RATE)){
            throw new MyException(OVERFLOW_MSG);
        }
        try{
            result = firstNumber.divide(secondNumber,SCALE,RoundingMode.HALF_DOWN);
        }catch (ArithmeticException e){
            throw new MyException(EXCEPTION_MSG);
        }
        return result;
    }

    @Override
    public String getValue() {
        return VALUE;
    }

    @Override
    public ActionType getType() {
        return TYPE;
    }

}

package win_calculator.model.nodes.actions.main_operations;

import win_calculator.exceptions.MyException;
import win_calculator.utils.ActionType;

import java.math.BigDecimal;
import java.math.RoundingMode;

public class Divide implements MainOperation {

    private static final String VALUE = "  \u00F7  ";
    private static final ActionType TYPE = ActionType.MAIN_OPERATION;
    private static final String EXCEPTION_MSG = "Cannot divide by zero";
    private static final int SCALE = 10000;

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

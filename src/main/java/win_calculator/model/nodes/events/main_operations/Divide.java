package win_calculator.model.nodes.events.main_operations;

import win_calculator.model.exceptions.DivideByZeroException;
import win_calculator.model.exceptions.OperationException;
import win_calculator.model.exceptions.UndefinedResultException;
import win_calculator.model.nodes.events.EventType;

import java.math.BigDecimal;
import java.math.RoundingMode;

public class Divide implements MainOperation {

    private static final String VALUE = "  \u00F7  ";
    private static final EventType TYPE = EventType.MAIN_OPERATION;
    private static final int SCALE = 10030;

    @Override
    public BigDecimal calculate(BigDecimal number) throws UndefinedResultException {

        BigDecimal result;
        try{
            result = number.divide(number,SCALE,RoundingMode.HALF_DOWN);
        }catch (ArithmeticException e){
            throw new UndefinedResultException();
        }
        return result;
    }

    @Override
    public BigDecimal calculate(BigDecimal firstNumber, BigDecimal secondNumber) throws UndefinedResultException, DivideByZeroException {

        BigDecimal result;
        if (firstNumber.equals(BigDecimal.ZERO) && secondNumber.equals(BigDecimal.ZERO)){
            throw new UndefinedResultException();
        }
        try{
            result = firstNumber.divide(secondNumber,SCALE,RoundingMode.HALF_UP);
        }catch (ArithmeticException e){
            throw new DivideByZeroException();
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
